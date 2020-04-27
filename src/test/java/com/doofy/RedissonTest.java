package com.doofy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description:
 * @Author DooFy
 * @Date 2020/4/21
 * @Version
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Component
public class RedissonTest {
    @Autowired
    RedissonClient redissonClient;

    @Test
    public void set(){
        // 设置字符串
        RBucket<String> keyObj = redissonClient.getBucket("k1");
        keyObj.set("v1234");
        String name = keyObj.getName();
        System.out.println(name);
    }

    /**
     * RKeys getKeys()
     * 封装对key的操作
     */
    @Test
    public void RKeysTest(){
        RKeys keys = redissonClient.getKeys();
        Iterable<String> allKeys = keys.getKeys();
        Iterator<String> iterator1 = allKeys.iterator();
        while (iterator1.hasNext()){
            System.out.println(iterator1.next());
        }
        System.out.println("-----------------");
        Iterable<String> foundedKeys = keys.getKeysByPattern("k*");
        Iterator<String> iterator = foundedKeys.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        long numOfDeletedKeys = keys.delete("obj1", "obj2", "obj3");
        System.out.println("-----------------");
        System.out.println(numOfDeletedKeys);
        long deletedKeysAmount = keys.deleteByPattern("test?");
        System.out.println(deletedKeysAmount);
        String randomKey = keys.randomKey();
        System.out.println(randomKey);
        long keysAmount = keys.count();
        System.out.println(keysAmount);
    }


    /**
     * 通用对象桶---存放任意类型的数据
     */
    @Test
    public void RBucketTest() throws InterruptedException {
        RBucket<Long> bucket = redissonClient.getBucket("anyObject");
//        bucket.set(new Long(1));
        Long obj = bucket.get();
        System.out.println(obj);
        boolean delete = bucket.delete();
        System.out.println(obj+"->"+delete);

        //setnx
        boolean bool = bucket.trySet(new Long(3));
        System.out.println("trySet:"+bool);
        boolean b = bucket.compareAndSet(new Long(3), new Long(5));
        System.out.println("compareAndSet："+b);
        Long oldV = bucket.getAndSet(new Long(6));
        System.out.println(oldV);

    }

    @Test
    public void RBucketsTest(){
        RBuckets buckets = redissonClient.getBuckets();
        Map<String, RBucket> loadedBuckets = buckets.get("b1", "b2", "b3");
        for (Map.Entry entry:loadedBuckets.entrySet()) {
            System.out.println(entry.getKey()+","+entry.getValue());
        }

        Map<String, String> map = new HashMap();

        map.put("myBucket1", "aaa");
        map.put("myBucket2", "bbb");

// 利用Redis的事务特性，同时保存所有的通用对象桶，如果任意一个通用对象桶已经存在则放弃保存其他所有数据。
        buckets.trySet(map);
// 同时保存全部通用对象桶。
        buckets.set(map);
    }
}
