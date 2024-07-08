package com.kele.redbirds_backend.common;/*
 * @author kele
 * @version 1.0
 * 通用分页请求参数
 */

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 页面大小
     */
    private  int pageSize = 10;

    /**
     * 当前是第几页
     */
    private int pageNum = 1;
}
