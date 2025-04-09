package com.dbg.datawork.exception;

import com.dbg.datawork.infra.common.ErrorCode;

/**
 * @author 15968
 * @version 1.0
 * @description: 自定义异常类 -- 数据源连接错误
 * @date 2025/3/17 19:13
 */
public class DataSourceConnectionException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    public DataSourceConnectionException(int code, String message) {
        super(message);
        this.code = code;
    }

    public DataSourceConnectionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public DataSourceConnectionException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }

}
