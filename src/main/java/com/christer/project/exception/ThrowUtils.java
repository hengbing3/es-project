package com.christer.project.exception;

import com.christer.project.common.ResultCode;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-13 23:28
 * Description:
 * 简易的抛异常工具类
 */
public class ThrowUtils {

    private ThrowUtils() {
    }


    /**
     * 条件成立，抛出异常
     *
     * @param condition        if条件
     * @param runtimeException 运行时异常
     */
    public static void throwIf(Boolean condition, RuntimeException runtimeException) {
        if (Boolean.TRUE.equals(condition)) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立，根据异常信息，抛出自定义异常
     *
     * @param condition if条件
     * @param message   自定义异常
     */
    public static void throwIf(Boolean condition, String message) {
        if (Boolean.TRUE.equals(condition)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 条件成立，抛出自定义异常（enum code，message）
     * @param condition 条件
     * @param errorCode api状态码
     * @param message 异常信息
     */
    public static void throwIf(Boolean condition, ResultCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }


}
