package org.soho.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.soho.common.constant.Constant;
import org.soho.common.exception.BusinessException;
import org.soho.common.model.dto.user.UserRegisterDTO;
import org.soho.common.model.entity.UserEntity;
import org.soho.common.model.enums.ErrorCode;
import org.soho.common.service.MinioService;
import org.soho.common.util.MessageUtil;
import org.soho.portal.mapper.UserMapper;
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

    @Resource
    private MessageUtil messageUtil;

    @Resource
    private MinioService minioService;

    @Override
    public long registerUser(UserRegisterDTO userRegisterDTO) {
        String userPassword = userRegisterDTO.getUserPassword();
        String username = userRegisterDTO.getUsername();
        String checkPassword = userRegisterDTO.getCheckPassword();
        if (!checkPassword.equals(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, messageUtil.getMessage("exception.register.password.dif"));
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setUserId(username);
        userEntity.setPassword(userPassword);
        try {
            //上传头像
            minioService.uploadAvatar(userRegisterDTO.getAvatar(), username);
            boolean saveResult = this.save(userEntity);
            if (!saveResult) {//若未存储成功
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, messageUtil.getMessage("exception.register.dataBase"));
            }
        } catch (Exception e) {
            if (e.getMessage().contains(Constant.Duplicate_Entry)) {// 唯一键账号重复
                throw new BusinessException(ErrorCode.PARAMS_ERROR, messageUtil.getMessage("exception.register.account.exits"));
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, messageUtil.getMessage("exception.register.dataBase") + e.getMessage());
            }
        }

        return userEntity.getId();
    }
}
