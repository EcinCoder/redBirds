package com.kele.redbirds_backend.model.requset;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户名查询
 */

@Data
public class UserSearchRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户名查询
     */
    private String username;
}

