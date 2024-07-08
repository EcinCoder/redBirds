package com.kele.redbirds_backend.model.requset;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 登录账号
     */
    private String userAccount;
    /**
     * 登录密码
     */
    private String userPassword;
}
