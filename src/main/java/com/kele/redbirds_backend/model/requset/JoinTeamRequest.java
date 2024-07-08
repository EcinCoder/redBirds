package com.kele.redbirds_backend.model.requset;/*
 * @author kele
 * @version 1.0
 *
 */

import lombok.Data;

@Data
public class JoinTeamRequest {

    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队伍密码
     */
    private String teamPassword;

}
