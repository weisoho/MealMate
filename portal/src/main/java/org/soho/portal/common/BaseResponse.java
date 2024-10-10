package org.soho.portal.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.soho.portal.model.enums.ErrorCode;

import java.io.Serializable;

/**
 * @author wesoho
 * @version 1.0
 * @description: 通用返回类
 * @date 2024/10/7 21:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {
    private int resultCode;

    private T data;

    private String resultInfo;

    public BaseResponse(ErrorCode errorCode) {
        this.resultCode = errorCode.getCode();
        this.resultInfo = errorCode.getMessage();
    }

    /**
     * 成功
     *
     * @param data  返回的数据
     * @param <T>   返回的数据类型
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode 错误码与错误信息
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code      错误码
     * @param message   错误信息
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode      错误码
     * @param msg   错误信息
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String msg) {
        return new BaseResponse<>(errorCode.getCode(), null, msg);
    }
}
