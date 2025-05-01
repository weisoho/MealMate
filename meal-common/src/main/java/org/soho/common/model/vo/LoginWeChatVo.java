package org.soho.common.model.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2025/3/10 23:16
 */
@Data
@Builder
public class LoginWeChatVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;

    private UserVo userInfo;
}
