package org.soho.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2025/4/22 20:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatSession {
    // 用户唯一标识
    private String openid;

    // 会话密钥
    private String session_key;

    // 用户在开放平台的唯一标识符
    private String unionid;
}
