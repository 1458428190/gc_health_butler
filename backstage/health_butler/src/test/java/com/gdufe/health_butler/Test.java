package com.gdufe.health_butler;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdufe.health_butler.common.util.ThreadPoolUtils;
import com.gdufe.health_butler.dao.GoodsMapper;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: laichengfeng
 * @Description: 测试
 * @Date: 2019/3/5 15:54
 */
public class Test {

    @org.junit.Test
    public void Test1() {
//        String ss = "https://play.minio.io:9000/laichengfeng-health-butler/b3dbbfd02b79f7235b20bad30bc96fd7.png";
//        List<String> list = new ArrayList<>();
////        System.out.println(list);
//        String s1 = new String("https://play.minio.io:9000/laichengfeng-health-bu");
//        list.add(s1);
//        list.add(ss);
//        System.out.println(String.join("#^$^#", list));

        System.out.println(GoodsMapper.class.equals(BaseMapper.class));


        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        for(int i=0; i<10; i++) {
            map.put("a", map.get("a") + 1);
        }
    }

    @org.junit.Test
    public void testThread() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.createDefaultThreadPoolExecutor(8, 10);
        for(int i=1; i<=8; i++) {
            int finalI = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for(int j=0; j<1000000; j++) {
                        System.out.println(finalI);
                    }
                }
            });
        }
        Thread.sleep(Long.MAX_VALUE);
    }

}
