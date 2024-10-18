package org.soho.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.soho.portal.constant.Constant;
import org.soho.portal.exception.BusinessException;
import org.soho.portal.mapper.UserMapper;
import org.soho.portal.model.entity.UserEntity;
import org.soho.portal.model.enums.ErrorCode;
import org.soho.portal.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private MessageSource messageSource;

    @Override
    public long registerUser(String username, String userPassword, String checkPassword) {
        if (!checkPassword.equals(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, messageSource.getMessage("exception.password.dif", null, LocaleContextHolder.getLocale()));
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setUserId(username);
        userEntity.setPassword(userPassword);
        try {
            boolean saveResult = this.save(userEntity);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, messageSource.getMessage("exception.register.dataBase", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            if (e.getMessage().contains(Constant.Duplicate_Entry)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, messageSource.getMessage("exception.register.account.exits", null, LocaleContextHolder.getLocale()));
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, messageSource.getMessage("exception.register.dataBase", null, LocaleContextHolder.getLocale()) + e.getMessage());
            }
        }

        return userEntity.getId();
    }
}
