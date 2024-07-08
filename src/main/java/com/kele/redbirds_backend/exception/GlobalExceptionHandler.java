package com.kele.redbirds_backend.exception;


import com.kele.redbirds_backend.common.BaseResponse;
import com.kele.redbirds_backend.common.ErrorCode;
import com.kele.redbirds_backend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse bussinessExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage());
    }
}
