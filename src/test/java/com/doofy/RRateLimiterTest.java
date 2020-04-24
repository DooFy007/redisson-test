package com.doofy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName
 * @Description:
 * @Author DooFy
 * @Date 2020/4/24
 * @Version
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Component
public class RRateLimiterTest {
    @Autowired
    RedissonClient redisson;
    @Test
    public void test() throws InterruptedException {
//        RRateLimiter rateLimiter = redisson.getRateLimiter("myRateLimiter");
// 初始化
// 最大流速 = 每1秒钟产生10个令牌
//        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);
//
//        CountDownLatch latch = new CountDownLatch(2);
//        rateLimiter.acquire(3);
//// ...
//
//        Thread t = new Thread(() -> {
//            limiter.acquire(2);
//            // ...
//        });


        // 从redisson客户端获取RRateLimiter限流器对象。
        /**
         * 将限流器的配置写入到redis中作持久化，分布式下相同名字("myRateLimiter")的限流器使用同一配置
         * 将限流器配置持久化到redis中：
         *      1.数据结构——Hash
         *      2.赋值方式--hsetNx，不会对已有字段进行覆盖
         *      3.
         */
        RRateLimiter rateLimiter = redisson.getRateLimiter("myRateLimiter");
        //设置限流器配置。基于滚动窗口实现。
        // 初始化：一分钟内最大请求数为60，分成10个粒度。
        rateLimiter.trySetRate(RateType.OVERALL, 60, 10, RateIntervalUnit.MINUTES);
        long l=0l;
        l = rateLimiter.sizeInMemory();
        System.out.println(l);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < 20; i++) {
            System.out.println(String.format("%s：获得锁结果(%s)", simpleDateFormat.format(new Date()),
                    rateLimiter.tryAcquire()));
            Thread.sleep(500L);
        }
        l = rateLimiter.sizeInMemory();
        System.out.println(l);
    }
}
