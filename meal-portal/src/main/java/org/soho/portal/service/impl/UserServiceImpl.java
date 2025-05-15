package org.soho.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.soho.common.exception.BusinessException;
import org.soho.common.model.bo.WeChatSession;
import org.soho.common.model.dto.user.RegisterUserDTO;
import org.soho.common.model.entity.UserEntity;
import org.soho.common.model.enums.ErrorCode;
import org.soho.common.model.vo.LoginWeChatVo;
import org.soho.common.model.vo.UserVo;
import org.soho.common.security.JwtTokenProvider;
import org.soho.common.service.MinioService;
import org.soho.common.service.WechatAuthService;
import org.soho.portal.mapper.UserMapper;
import org.soho.portal.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * @author wesoho
 * @version 1.0
 * @description: 用户服务层
 * @date 2024/10/7 21:44
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    // 登录过期时间=30分钟
    private static final int redisLoginUserExpirationMinutes = 30;

    @Resource
    private WechatAuthService weChatService;

    @Resource
    private MinioService minioService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean insertUser(RegisterUserDTO registerUserDTO) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "phone", registerUserDTO.getPhone());
        UserEntity one = this.getOne(queryWrapper);
        if (one != null) {
            log.error("{}{}", ErrorCode.DUPLICATE_PHONE_NUMBER.getMessage(), registerUserDTO.getPhone());
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

    @Override
    public Mono<LoginWeChatVo> wechatLogin(String code) {
        return weChatService.getSession(code).flatMap(this::processWeChatSession).onErrorResume(ex -> {
            // 记录错误并返回友好的错误响应
            log.error("微信登录失败", ex);
            return Mono.error(new BusinessException(ErrorCode.API_WECHAT_ERROR));
        });
    }

    // 处理微信返回session
    private Mono<LoginWeChatVo> processWeChatSession(WeChatSession weChatSession) {
        return getOrCreateUser(weChatSession.getOpenid(), weChatSession.getSession_key()).doOnNext(userEntity -> {
            RBucket<Object> bucket = redissonClient.getBucket(userEntity.getUserId());
            Mono.fromRunnable(() -> bucket.set(userEntity.getSession_key(), Duration.ofMinutes(redisLoginUserExpirationMinutes)))
                    .subscribeOn(Schedulers.boundedElastic()).subscribe();
        }).flatMap(this::createLoginResponse).doOnNext(loginVo -> log.debug("用户 {} 已获取或创建", loginVo.getUserInfo().getUserId()));
    }

    // 创建登录接口返回值，返回token和用户信息
    private Mono<LoginWeChatVo> createLoginResponse(UserEntity userEntity) {
        return Mono.fromCallable(() -> jwtTokenProvider.generateToken(userEntity.getUserId())).map(token -> {
            UserVo userVo = UserVo.builder().build();
            BeanUtils.copyProperties(userEntity, userVo);
            return LoginWeChatVo.builder().token(token).userInfo(userVo).build();
        });
    }

    /**
     * 获取或新增用户
     *
     * @param openId      用户ID
     * @param session_key 用户session_key
     * @return 获取或新增的用户
     */
    private Mono<UserEntity> getOrCreateUser(String openId, String session_key) {
        return findUserByUserId(openId).switchIfEmpty(createWeChatUserWith(openId, session_key)).flatMap(user -> updateUserSessionKey(user, session_key)).doOnNext(user -> log.info("用户处理完成: {}", user.getUserId()));
    }

    /**
     * 根据userId查询用户
     *
     * @param UserId 用户ID
     * @return 查询到用户信息
     */
    private Mono<UserEntity> findUserByUserId(String UserId) {
        return Mono.fromCallable(() -> this.getOne(new QueryWrapper<UserEntity>().eq("user_id", UserId))).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 更新用户session_key
     *
     * @param user        用户
     * @param session_key 新的session_key
     * @return 更新后用户信息
     */
    private Mono<UserEntity> updateUserSessionKey(UserEntity user, String session_key) {
        return Mono.fromCallable(() -> {
            user.setSession_key(session_key);
            this.updateById(user);
            return user;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 创建微信用户
     *
     * @param openId      用户ID
     * @param session_key session_key
     * @return 创建用户信息
     */
    private Mono<UserEntity> createWeChatUserWith(String openId, String session_key) {
        return Mono.fromCallable(() -> {
            UserEntity newUser = UserEntity.builder().userId(openId).session_key(session_key).build();
            this.save(newUser);
            return newUser;
        }).subscribeOn(Schedulers.boundedElastic()).onErrorResume(DuplicateKeyException.class, e -> findUserByUserId(openId) // 冲突后重查
        );
    }

    @Override
    public UserEntity smsLogin(String phone, String code) {

        return UserEntity.builder().username(code).phone(phone).build();
    }
}
