//package com.kele.redbirds_backend.service;
//
//import com.kele.redbirds_backend.model.domain.User;
//import jakarta.annotation.Resource;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//class UserServiceTest {
//
//
//    @Resource UserService userService;
//
//    @Test
//    void test() {
//        User user = new User();
//        user.setId(0);
//        user.setUsername("kele");
//        user.setUserAccount("我是最棒的");
//        user.setAvatarUrl("https://wx2.sinaimg.cn/mw690/007Hulo3gy1he4sac8ntqj323u35skjm.jpg");
//        user.setGender("");
//        user.setUserPassword("123456");
//        user.setUserPhone("1008611");
//        user.setUserEmail("2222@qq.com");
//        boolean save = userService.save(user);
//        Assert.assertTrue(save);
//    }
//
//    @Test
//    void userRegister() {
//        String userAccount = "xu!bi";
//        String password = "1234556";
//        String checkPassword = "1234556";
//        long l = userService.UserRegister(userAccount, password, checkPassword);
//        System.out.println(l);
//    }
//
//    @Test
//    void userLogin() {
//    }
//
//    @Test
//    void userSearch() {
//
//    }
//
//    @Test
//    void userDelete() {
//    }
//
//    @Test
//    void getSafetyUser() {
//    }
//
//    @Test
//    void userLogout() {
//    }
//
//    @Test
//    void tagsSearch() {
//        List<String> tags = new ArrayList<>();
//        tags.add("Python");
////        tags.add("Java");
//        List<User> users = userService.tagsSearch(tags);
//        System.out.println(users);
//    }
//}