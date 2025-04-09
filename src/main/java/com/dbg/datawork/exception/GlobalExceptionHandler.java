package com.dbg.datawork.exception;

import com.dbg.datawork.infra.common.BaseResponse;
import com.dbg.datawork.infra.common.ErrorCode;
import com.dbg.datawork.infra.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(DataSourceConnectionException.class)
    public BaseResponse<?> dataSourceConnectionException(DataSourceConnectionException e) {
        log.error("DataSourceConnectionException", e);
        return ResultUtils.error(ErrorCode.DATASOURCE_CONNECTION_ERROR, "数据源连接错误");
    }

    @ExceptionHandler(DatasetExecuteException.class)
    public BaseResponse<?> datasetExecuteException(DatasetExecuteException e) {
        log.error("DataSourceConnectionException", e);
        return ResultUtils.error(ErrorCode.DATASET_GENERATE_ERROR, e.getMessage());
    }
}
