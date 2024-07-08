package com.kele.redbirds_backend.contant;


public interface UserContant {

    /**
     * 用户登录态键
     */
     String USER_LOGIN_STATE = "userLoginState";
    /**
     * 0-用户 1-VIP 2-管理员
     */
    Integer USER_ROLE = 0;
    Integer VIP_ROLE = 1;
    Integer ADMIN_ROLE = 2;

}
