package com.kele.redbirds_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private String gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String userPhone;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 用户状态 0 - 正常 1 - 违规警告 2 - 禁言
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 用户标签
     */
    private String userTags;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 0 - 用户 1 - VIP 2 - 管理员
     */
    private Integer userRole;

    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;
}