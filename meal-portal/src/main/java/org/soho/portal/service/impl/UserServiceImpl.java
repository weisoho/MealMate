package org.soho.portal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.soho.common.exception.BusinessException;
import org.soho.common.model.bo.WeChatSession;
import org.soho.common.model.dto.user.RegisterUserDTO;
import org.soho.common.model.entity.UserEntity;
import org.soho.common.model.enums.ErrorCode;
import org.soho.common.model.vo.LoginWeChatVo;
import org.soho.common.model.vo.UserVo;
import org.soho.common.service.MinioService;
import org.soho.common.service.WechatAuthService;
import org.soho.portal.mapper.UserMapper;
import org.soho.portal.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author wesoho
 * @version 1.0
 * @description: 用户服务层
 * @date 2024/10/7 21:44
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Resource
    private WechatAuthService weChatService;

    @Resource
    private MinioService minioService;
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private Scheduler jdbcScheduler;

    @Override
    public boolean insertUser(RegisterUserDTO registerUserDTO) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "phone", registerUserDTO.getPhone());
        UserEntity one = this.getOne(queryWrapper);
        if (one != null) {
            log.error(ErrorCode.DUPLICATE_PHONE_NUMBER.getMessage() + registerUserDTO.getPhone());
            return false;
        }
        String userId = IdWorker.getIdStr();
        UserEntity userEntity = UserEntity.builder().userId(userId).phone(registerUserDTO.getPhone()).build();
        try {
            MultipartFile avatarFile = registerUserDTO.getAvatar();
            // 上传头像
            minioService.uploadAvatar(avatarFile, userId);
            return this.save(userEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public Mono<LoginWeChatVo> wechatLogin(String code, ServerWebExchange exchange) {
        return weChatService.getSession(code)
                .flatMap(weChatSession -> SaReactorUtil.initContext(exchange)
                        .then(getOrCreateUser(weChatSession.getOpenid(), weChatSession.getSession_key()))
                        .flatMap(this::createLoginResponse))
                .onErrorResume(error -> {
                    // 处理异常，例如记录日志等
                    System.err.println("WeChat login error: " + error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<LoginWeChatVo> processWeChatSession(WeChatSession weChatSession) {
        return getOrCreateUser(weChatSession.getOpenid(), weChatSession.getSession_key())
                .flatMap(this::createLoginResponse);
    }

    private Mono<LoginWeChatVo> createLoginResponse(UserEntity user) {
        return createToken(user.getUserId())
                .map(token -> {
                    StpUtil.checkLogin();
                    UserVo userVo = UserVo.builder().build();
                    BeanUtils.copyProperties(user, userVo);
                    return LoginWeChatVo.builder().token(token).userInfo(userVo).build();
                });
    }

    private Mono<String> createToken(String userId) {
        return Mono.fromCallable(() -> StpUtil.createLoginSession(userId))
                .subscribeOn(Schedulers.boundedElastic());
    }
    /**
     * 获取或新增用户
     *
     * @param openId      用户ID
     * @param session_key 用户session_key
     * @return 获取或新增的用户
     */
    private Mono<UserEntity> getOrCreateUser(String openId, String session_key) {
        return Mono.fromCallable(() -> {
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(true, "user_id", openId);
            UserEntity entityById = this.getOne(queryWrapper);
            if (entityById == null) {
                UserEntity newUserEntity = UserEntity.builder().userId(openId).session_key(session_key).build();
                boolean save = this.save(newUserEntity);
                if (save) {
                    return newUserEntity;
                } else {
                    throw new BusinessException(ErrorCode.USER_CREATE_FAILED);
                }
            }
            return entityById;
        }).subscribeOn(jdbcScheduler);
    }

    @Override
    public UserEntity smsLogin(String phone, String code) {

        return UserEntity.builder().username(code).phone(phone).build();
    }
}
