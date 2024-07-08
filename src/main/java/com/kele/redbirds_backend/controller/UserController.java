package com.kele.redbirds_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kele.redbirds_backend.common.BaseResponse;
import com.kele.redbirds_backend.common.ErrorCode;
import com.kele.redbirds_backend.common.ResultUtils;
import com.kele.redbirds_backend.exception.BusinessException;
import com.kele.redbirds_backend.model.domain.User;
import com.kele.redbirds_backend.model.requset.UserLoginRequest;
import com.kele.redbirds_backend.model.requset.UserRegisterRequest;
import com.kele.redbirds_backend.model.requset.UserSearchRequest;
import com.kele.redbirds_backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kele.redbirds_backend.contant.UserContant.USER_LOGIN_STATE;


/**
 * 用户接口
 */

@Tag(name = "用户接口")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long l = userService.UserRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(l);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return ResultUtils.success(userService.userLogin(userAccount, userPassword, request));
    }

    @PostMapping("/loginout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return ResultUtils.success(userService.userLogout(request));
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "当前用户为空");
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        return ResultUtils.success(userService.getSafetyUser(user));
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request) {
        if (userSearchRequest == null) {
            return null;
        }
        String username = userSearchRequest.getUsername();
        return ResultUtils.success(userService.userSearch(username, request));
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody long id, HttpServletRequest request) {
        if (id <= 0) {
            return null;
        }
        return ResultUtils.success(userService.userDelete(id, request));
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        // 1.校验非空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Integer result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> tagsSearch(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签为空");
        }
        List<User> users = userService.tagsSearch(tagNameList);
        return ResultUtils.success(users);
    }

    @GetMapping("/recommend")
    public BaseResponse<IPage<User>> recommendUsers(@RequestParam(required = false) List<String> tagNameList,long pageSize,long pageNum,HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String redisKey = String.format("redBirds:user:recommend:%s", loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 如果有缓存，直接读缓存
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if(userPage != null) {
            return ResultUtils.success(userPage);
        }
        // 无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        IPage<User> userList = userService.page(new Page<>(pageNum,pageSize),queryWrapper);
        try {
            valueOperations.set(redisKey,userList,300000, TimeUnit.MICROSECONDS);
        } catch (Exception e) {
            log.error("redis key set error : UserController - 150",e);
        }
        return ResultUtils.success(userList);
    }

}
