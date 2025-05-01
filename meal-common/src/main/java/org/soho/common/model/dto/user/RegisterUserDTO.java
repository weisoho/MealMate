package org.soho.common.model.dto.user;

import lombok.Data;
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
public class RegisterUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    //  手机号
    private String phone;

    // 头像
    private MultipartFile avatar;
}
