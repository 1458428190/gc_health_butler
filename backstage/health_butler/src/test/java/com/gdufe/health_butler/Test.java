package com.gdufe.health_butler;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdufe.health_butler.dao.GoodsMapper;
import com.gdufe.health_butler.service.UserService;
import com.gdufe.health_butler.service.impl.UserServiceImpl;

import java.util.*;

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


}
