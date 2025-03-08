package org.soho.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.soho.common.model.dto.user.UserRegisterDTO;
import org.soho.common.model.entity.UserEntity;

public interface UserService  extends IService<UserEntity> {
    long registerUser(UserRegisterDTO userRegisterDTO);
}
