package com.kele.redbirds_backend.common;


/**
 * 返回工具类
 */
public class ResultUtils {
    /**
     * 成功
     * @param data 数据
     * @return 成功信息
     * @param <T> 数据类型
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0,data,"成功");
    }

    /**
     * 失败
     * @param errorCode 错误码
     * @return 错误信息
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode 错误码
     * @return 错误信息
     */
    public static BaseResponse error(ErrorCode errorCode,String message,String description) {
        return new BaseResponse<>(errorCode.getCode(),null,message,description);
    }

    /**
     * 失败
     * @param errorCode 错误码
     * @return 错误信息
     */
    public static BaseResponse error(ErrorCode errorCode,String message) {
        return new BaseResponse<>(errorCode.getCode(),null,message);
    }

}
