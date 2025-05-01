package org.soho.common.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2025/4/26 15:34
 */
@Getter
@Setter
@Builder
public class UserVo {
    private String userId;

    private String username;

    // 积分
    private Integer points;

    // vip余额
    private BigDecimal vipBalance;

    // vip等级
    private int vipLevel;

    private String avatarUrl;
}
