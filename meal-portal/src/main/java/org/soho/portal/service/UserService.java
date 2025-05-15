package org.soho.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.soho.common.model.dto.user.RegisterUserDTO;
import org.soho.common.model.entity.UserEntity;
import org.soho.common.model.vo.LoginWeChatVo;
import reactor.core.publisher.Mono;

public interface UserService  extends IService<UserEntity> {
    boolean insertUser(RegisterUserDTO registerUserDTO);

    Mono<LoginWeChatVo> wechatLogin(String code);

    UserEntity smsLogin(String phone,String code);
}
