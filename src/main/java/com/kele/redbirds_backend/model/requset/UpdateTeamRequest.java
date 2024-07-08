package com.kele.redbirds_backend.model.requset;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户队伍修改请求类
 */
@Data
public class UpdateTeamRequest implements Serializable {

    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 队伍id
     */
    private Long id;

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
     * 0 - 公开 1 - 密码 2 - 私有
     */
    private Integer teamStatus;

    /**
     * 队伍密码
     */
    private String teamPassword;

}
