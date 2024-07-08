package com.kele.redbirds_backend.model.dto;/*
 * @author kele
 * @version 1.0
 * 队伍查询封装类
 */

import com.kele.redbirds_backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {

    /**
     *
     */
    private Long id;

    /**
     * 搜索关键词，同时对队伍名称和描述搜索
     */
    private String searchText;

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
     * 用户id
     */
    private Long userId;

    /**
     * 用户身份 user - 用户 admin - 管理员 ban - 封号
     */
    private Integer teamStatus;
}
