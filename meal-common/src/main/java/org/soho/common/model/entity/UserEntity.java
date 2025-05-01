package org.soho.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author wesoho
 * @version 1.0
 * @description: 用户对象
 * @date 2024/10/10 22:51
 */
@TableName("front_users")
@Data
@Builder
public class UserEntity {

    @TableId(value = "id", type = IdType.AUTO) // 自增主键
    private Integer id; // 主键

    @TableField("user_id")
    private String userId; // 用户ID

    @TableField("username")
    private String username; // 用户名

    @TableField("password")
    private String password; // 用户密码

    @TableField("email")
    private String email; // 用户邮箱

    @TableField("phone")
    private String phone; // 用户电话

    @TableField("points")
    private Integer points; // 用户积分，默认值0

    @TableField("vip_balance")
    private BigDecimal vipBalance; // 会员卡余额，默认值0.00

    @TableField("vip_level")
    private int vipLevel; // 会员等级，默认值0

    @TableField("created_at")
    private Timestamp createdAt; // 创建时间

    @TableField("updated_at")
    private Timestamp updatedAt; // 更新时间

    @TableField("avatar")
    private String avatar;//头像信息（桶名称+“-”+文件名称）

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("session_key")
    private String session_key;
}
