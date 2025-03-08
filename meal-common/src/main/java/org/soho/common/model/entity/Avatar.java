package org.soho.common.model.entity;

import lombok.Data;

/**
 * @author wesoho
 * @version 1.0
 * @description: 文件信息
 * @date 2024/11/5 0:48
 */
@Data
public class Avatar {
    private int id;

    private String objectName;

    private String bucketName;
}
