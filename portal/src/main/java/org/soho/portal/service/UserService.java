package org.soho.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.soho.portal.model.entity.UserEntity;

public interface UserService  extends IService<UserEntity> {
    long registerUser(String userAccount, String userPassword, String checkPassword);
}
