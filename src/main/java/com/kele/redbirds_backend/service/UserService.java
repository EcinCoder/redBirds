package com.kele.redbirds_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kele.redbirds_backend.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author ROG 魔霸5R
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-07 17:02:33
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount    用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 用户ID
     */
    long UserRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  登录账户
     * @param userPassword 登录密码
     * @param request      用于往请求的session中设置值，从请求的session中读取值
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return 用户信息
     */
    List<User> userSearch(String username, HttpServletRequest request);

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     * @return 是否成功
     */
    boolean userDelete(long id, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originalUser 未脱敏用户信息
     * @return 已脱敏用户信息
     */
    User getSafetyUser(User originalUser);

    /**
     * 用户注销
     *
     * @param request 用户请求对象
     * @return 结果
     */
    int userLogout(HttpServletRequest request);


    /**
     * 根据标签查找用户
     *
     * @param tags 标签列表
     * @return 用户
     */
    List<User> tagsSearch(List<String> tags);

    /**
     * 更新数据
     *
     * @param user      用户
     * @param loginUser 登录用户
     * @return 结果
     */
    int updateUser(User user, User loginUser);

    /**
     * 获取登录用户
     *
     * @param request 请求信息
     * @return 用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 鉴权是否为管理员
     * @param request 等待鉴权的请求
     * @return 鉴权结果
     */
    boolean checkAdmin(HttpServletRequest request);

    /**
     * 鉴权是否为管理员
     * @param loginUser 当前登录用户
     * @return 返回结果
     */
    boolean checkAdmin(User loginUser);

}
