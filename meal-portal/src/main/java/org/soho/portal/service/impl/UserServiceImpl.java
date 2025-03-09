package org.soho.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.soho.common.model.dto.user.UserRegisterDTO;
import org.soho.common.model.entity.UserEntity;
import org.soho.common.model.enums.ErrorCode;
import org.soho.common.service.MinioService;
import org.soho.common.util.MessageUtil;
import org.soho.portal.mapper.UserMapper;
import org.soho.portal.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wesoho
 * @version 1.0
 * @description: 用户服务层
 * @date 2024/10/7 21:44
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Resource
    private MessageUtil messageUtil;

    @Resource
    private MinioService minioService;

    @Override
    public boolean registerUser(UserRegisterDTO userRegisterDTO) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "phone", userRegisterDTO.getPhone());
        UserEntity one = this.getOne(queryWrapper);
        if (one != null) {
            log.error(ErrorCode.DUPLICATE_PHONE_NUMBER.getMessage() + userRegisterDTO.getPhone());
            return false;
        }
        long userId = IdWorker.getId();
        UserEntity userEntity = UserEntity.builder().userId(userId).phone(userRegisterDTO.getPhone()).build();
        try {
            MultipartFile avatarFile = userRegisterDTO.getAvatar();
            // 上传头像
            minioService.uploadAvatar(avatarFile, userId);
            return this.save(userEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return true;
    }
}
