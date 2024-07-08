package com.kele.redbirds_backend.model.requset;/*
 * @author kele
 * @version 1.0
 *
 */

import lombok.Data;

import java.util.Date;

@Data
public class CreateTeamRequest {

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户身份 0 - 用户 1 - 管理员 2 - VIP
     */
    private Integer teamStatus;

    /**
     * 队伍密码
     */
    private String teamPassword;

}
