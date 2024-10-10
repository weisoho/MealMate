package org.soho.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.soho.portal.exception.BusinessException;
import org.soho.portal.mapper.UserMapper;
import org.soho.portal.model.entity.UserEntity;
import org.soho.portal.model.enums.ErrorCode;
import org.soho.portal.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author wesoho
 * @version 1.0
 * @description: 用户服务层
 * @date 2024/10/7 21:44
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public long registerUser(String username, String userPassword, String checkPassword) {
        if (!checkPassword.equals(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致！");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setUserId(username);
        userEntity.setPassword(userPassword);
        try {
            boolean saveResult = this.save(userEntity);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在！");
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误: " + e.getMessage());
            }
        }

        return userEntity.getId();
    }
}
