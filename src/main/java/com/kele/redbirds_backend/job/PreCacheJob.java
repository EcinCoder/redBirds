//package com.kele.redbirds_backend.job;/*
// * @author kele
// * @version 1.0
// * 缓存预热任务
// */
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.kele.redbirds_backend.model.domain.User;
//import com.kele.redbirds_backend.service.UserService;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class PreCacheJob {
//
//    @Resource
//    private UserService userService;
//
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Resource
//    private RedissonClient redissonClient;
//
//    // 重点用户
//    private List<Integer> mainUserList = List.of(1);
//
//    // 每天执行，预热推荐用户
//    @Scheduled(cron = "0 58 23 * * *")
//    public void doCacheRecommendUser() {
//        RLock lock = redissonClient.getLock("redbirds_backend:precachejob:docache:lock");
//        try {
//            // 只有一个线程能获取到锁
//            if(lock.tryLock(0,30000L,TimeUnit.MILLISECONDS)) {
//                for (Integer userId : mainUserList) {
//                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//                    IPage<User> userList = userService.page(new Page<>(1,20),queryWrapper);
//                    String redisKey = String.format("redBirds:user:recommend:%s", userId);
//                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
//                    try {
//                        valueOperations.set(redisKey,userList,300000, TimeUnit.MICROSECONDS);
//                    } catch (Exception e) {
//                        log.error("redis key set error : UserController - 150",e);
//                    }
//                }
//            }
//        } catch (InterruptedException e) {
//            log.error("分布式锁报错 PreCacheJob - 62",e);
//        } finally {
//            // 只能释放自己的锁
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//}
