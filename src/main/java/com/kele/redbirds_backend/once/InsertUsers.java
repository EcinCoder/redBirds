package com.kele.redbirds_backend.once;/*
 * @author kele
 * @version 1.0
 *
 */

import cn.hutool.core.date.StopWatch;
import com.kele.redbirds_backend.mapper.UserMapper;
import com.kele.redbirds_backend.model.domain.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InsertUsers {
    
    @Resource
    private UserMapper userMapper;

    /**
     * 批量插入用户数据
     */
    public void doInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 10000000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假用户" + i);
            user.setUserAccount("假用户" + i);
            user.setAvatarUrl("https://abei-1256557411.cos.ap-chengdu.myqcloud.com/blogs/202405102124088.jpeg");
            user.setGender("男");
            user.setUserPassword("11111111");
            user.setUserPhone("12345678");
            user.setUserEmail("12345678");
            user.setUserStatus(0);
            user.setIsDelete(0);
            user.setUserRole(0);
            user.setUserTags("[\"Java\"]");
            user.setUserProfile("这是假用户");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
