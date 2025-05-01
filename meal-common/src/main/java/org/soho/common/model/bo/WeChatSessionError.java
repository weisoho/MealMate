package org.soho.common.model.bo;

import lombok.Data;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2025/4/26 16:46
 */
@Data
public class WeChatSessionError extends WeChatSession{

    // 错误码
    private int errcode;

    // 错误信息
    private String errmsg;

}
