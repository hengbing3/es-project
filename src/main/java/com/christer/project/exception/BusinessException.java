package com.christer.project.exception;

import com.christer.project.common.ResultCode;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-09-04 21:22
 * Description:
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 2793595362781826216L;
    /**
     * 异常状态码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        // -9999 自定义异常
        this.code = -9999;
    }

    public BusinessException(ResultCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ResultCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BusinessException(ResultCode errorCode, String message, Throwable e) {
        super(message, e);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
