package org.soho.common.model.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.soho.common.annotation.NotHaveBlank;
import org.springframework.web.multipart.MultipartFile;

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
    @NotHaveBlank(message = "{validate.account.cantHaveBlank.message}")
    private String username;

    //用户密码
    @NotHaveBlank(message = "{validate.password.cantHaveBlank.message}")
    @Size(min = 5,max = 20,message = "{validate.account.size.message}")
    private String userPassword;

    //校验密码
    @NotHaveBlank(message = "{validate.password.cantHaveBlank.message}")
    private String checkPassword;

    //头像
    private MultipartFile avatar;
}
