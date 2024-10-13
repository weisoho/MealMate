package org.soho.portal.model.dto.user;

import lombok.Data;
import org.soho.portal.annotation.NotHaveBlank;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wesoho
 * @version 1.0
 * @description: 用户注册，请求入参
 * @date 2024/10/7 21:26
 */
@Data
public class UserRegisterDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    //用户账号
    @NotHaveBlank()
    private String username;

    //用户密码
    private String userPassword;

    //校验密码
    private String checkPassword;
}
