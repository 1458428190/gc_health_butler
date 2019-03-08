package com.gdufe.health_butler.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: laichengfeng
 * @Description: 实体资源锁,防止多个线程操作同一个资源的锁,根据实体ID生成唯一锁,前提是实体ID的字符串唯一性 只适用于资源同时操作数量不多的系统
 * @Date: 2019/3/7 23:35
 */
public class ResourceLockUtils {

    private static final Logger log = LoggerFactory.getLogger(ResourceLockUtils.class);
    // 初始化ConcurrentHashMap锁载体
    private static final ConcurrentHashMap<Long, AtomicInteger> lockMap = new ConcurrentHashMap<>();

    public static AtomicInteger getAtomicInteger(Long key) {
        if (lockMap.get(key) == null) {// 当实体ID锁资源为空,初始化锁
            lockMap.putIfAbsent(key, new AtomicInteger(0));// 初始化一个竞争数为0的原子资源
        }
        int count = lockMap.get(key).incrementAndGet();// 线程得到该资源,原子性+1
        log.debug("资源ID为:" + key + ",争抢线程数:" + count);
        return lockMap.get(key);// 返回该ID资源锁
    }

    public static void giveUpAtomicInteger(Long key) {
        if (lockMap.get(key) != null) {// 当实体ID资源不为空,才可以操作锁,防止抛出空指针异常
            int source = lockMap.get(key).decrementAndGet();// 线程释放该资源,原子性-1
            if (source <= 0) {// 当资源没有线程竞争的时候，就删除掉该锁,防止内存溢出
                lockMap.remove(key);
                log.debug("资源ID为:" + key + "移除成功");
            }
            log.debug("资源ID为:" + key + ",争抢线程数:" + source);
        }
    }
}
