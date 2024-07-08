package com.kele.redbirds_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kele.redbirds_backend.common.ErrorCode;
import com.kele.redbirds_backend.exception.BusinessException;
import com.kele.redbirds_backend.mapper.UserMapper;
import com.kele.redbirds_backend.model.domain.User;
import com.kele.redbirds_backend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kele.redbirds_backend.contant.UserContant.ADMIN_ROLE;
import static com.kele.redbirds_backend.contant.UserContant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author kele
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-05-07 17:02:33
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值
     */
    private static final String SALT = "kele";

    @Resource
    private UserMapper userMapper;

    @Override
    public long UserRegister(String userAccount, String userPassword, String checkPassword) {

        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户长度不能小于4");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能小于6");
        }
        //3.账户不能包含特殊字符
        String validPattern = "\"[^a-zA-Z0-9]\"";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        System.out.println(matcher);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        //4.密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码和校验密码不相同");
        }
        //2.账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(); //继承的mybatis
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户重复");
        }
        // 生成随机的用户编号
        Random random = new Random();
        int randomNum = random.nextInt(100000);
        String userCode = String.format("%05d",randomNum);
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserCode(userCode);
        // 给用户设置默认的头像
        user.setAvatarUrl("https://abei-1256557411.cos.ap-chengdu.myqcloud.com/blogs/202405102124088.jpeg");
        // 设置用户身份
        user.setUserRole(0);
        //设置默认用户昵称为用户账号
        user.setUsername(user.getUserAccount());
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.NO_AUTH, "数据插入失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户长度不能小于4");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能小于6");
        }
        //3.账户不能包含特殊字符
        String validPattern = "\"[^a-zA-Z0-9]\"";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        System.out.println(matcher.find());
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //2.账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); //继承的mybatis
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //脱敏
        User safetyUser = getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public List<User> userSearch(String username, HttpServletRequest request) {
        // 1. 鉴权 仅管理员可查询
        if (username == null || !checkAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (users == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        //todo S 讲users转换为数据流，然后遍历整个流，将所有的password设置为null，再拼接成list
        return users.stream().map(user -> {
            user.setUserPassword(null);
            return user;
        }).collect(Collectors.toUnmodifiableList());
    }

    /**
     * 删除用户
     *
     * @param id      用户id
     * @param request 发起删除用户请求
     * @return 返回删除结果
     */
    @Override
    public boolean userDelete(long id, HttpServletRequest request) {
        //鉴权
        if (id <= 0 || !checkAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return this.removeById(id);
    }

    @Override
    public User getSafetyUser(User originalUser) {
        if (originalUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        //用户脱敏
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUsername(originalUser.getUsername());
        safetyUser.setUserAccount(originalUser.getUserAccount());
        safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setUserPhone(originalUser.getUserPhone());
        safetyUser.setUserEmail(originalUser.getUserEmail());
        safetyUser.setUserRole(originalUser.getUserRole());
        safetyUser.setUserStatus(originalUser.getUserStatus());
        safetyUser.setCreateTime(originalUser.getCreateTime());
        safetyUser.setUserTags(originalUser.getUserTags());
        safetyUser.setUserCode(originalUser.getUserCode());
        safetyUser.setUserProfile(originalUser.getUserProfile());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public List<User> tagsSearch(List<String> tags) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tag : tags) {
            queryWrapper = queryWrapper.like("userTags", tag);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        List<User> safetyUsers = new ArrayList<>();
        for (User user : users) {
            safetyUsers.add(getSafetyUser(user));
        }
        return safetyUsers;
    }

    @Override
    public int updateUser(User user, User loginUser) {
        Long userId = user.getId();
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 如果不是管理员并且不是自己的信息，则抛异常
        if (!checkAdmin(loginUser) && !userId.equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUserMessage = userMapper.selectById(userId);
        if (oldUserMessage == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return (User) userObj;
    }

    @Override
    public boolean checkAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole().equals(ADMIN_ROLE);
    }

    @Override
    public boolean checkAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole().equals(ADMIN_ROLE);
    }

}




