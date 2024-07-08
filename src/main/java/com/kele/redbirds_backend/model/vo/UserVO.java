package com.kele.redbirds_backend.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 *  用户包装类（脱敏）
 * @TableName user
 */
@Data
public class UserVO implements Serializable {
    /**
     * 
     */
    private Integer id;

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
     * 电话
     */
    private String userPhone;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * zhuang't
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
     * 用户身份 user - 用户 admin - 管理员 ban - 封号
     */
    private String userRole;

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

    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;
}