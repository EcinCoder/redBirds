//package com.kele.redbirds_backend.service;/*
// * @author kele
// * @version 1.0
// *
// */
//
//import cn.hutool.core.date.StopWatch;
//import com.kele.redbirds_backend.model.domain.User;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.*;
//
//@SpringBootTest
//public class InsertUsersTest {
//
//    @Resource
//    private UserService userService;
//
//    // 自定义线程池
//    //CPU 密集型：分配的核心线程数 = CPU -1
//    // IO 密集型：分配的核心线程数可以大于CPU核数
//    private ExecutorService executorService = new ThreadPoolExecutor(60,1000,10000,TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
//
//
//    /**
//     * 批量插入用户数据
//     */
//    @Test
//    public void doInsertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 1000;
//        ArrayList<User> userList = new ArrayList<>();
//        for (int i = 0; i < INSERT_NUM; i++) {
//            User user = new User();
//            user.setUsername("假用户" + i);
//            user.setUserAccount("假用户" + i);
//            user.setAvatarUrl("https://abei-1256557411.cos.ap-chengdu.myqcloud.com/blogs/202405102124088.jpeg");
//            user.setGender("男");
//            user.setUserPassword("11111111");
//            user.setUserPhone("12345678");
//            user.setUserEmail("12345678@qq.com");
//            user.setUserStatus(0);
//            user.setIsDelete(0);
//            user.setUserRole(0);
//            user.setUserTags("[\"Java\"]");
//            user.setUserProfile("这是假用户");
//            user.setUserCode("99999999");
//            userList.add(user);
//        }
//        userService.saveBatch(userList,100);
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//
//    /**
//     * 并发批量插入用户数据
//     */
//    @Test
//    public void doConcurrencyInsertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 900000;
//        int batchSize = 45000;
//        // 分十组
//        int j = 0;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            List<User> userList = new ArrayList<>();
//            while(true) {
//                j++;
//                User user = new User();
//                user.setUsername("假用户");
//                user.setUserAccount("假用户");
//                user.setAvatarUrl("https://abei-1256557411.cos.ap-chengdu.myqcloud.com/blogs/202405102124088.jpeg");
//                user.setGender("男");
//                user.setUserPassword("1234567890");
//                user.setUserPhone("12345678");
//                user.setUserEmail("12345678");
//                user.setUserStatus(0);
//                user.setIsDelete(0);
//                user.setUserRole(0);
//                user.setUserTags("[\"Java\"]");
//                user.setUserProfile("这是假用户");
//                user.setUserCode("99999999");
//                userList.add(user);
//                if (j % batchSize == 0) {
//                    break;
//                }
//            }
//            // 异步执行
//            // 从自带的线程池中取
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                userService.saveBatch(userList,batchSize);
//            });
//            futureList.add(future);
////             从自定义的线程池中取
////            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
////                userService.saveBatch(userList,batchSize);
////            },executorService);
////            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//
//}
