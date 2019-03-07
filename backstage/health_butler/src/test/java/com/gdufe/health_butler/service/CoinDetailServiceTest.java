package com.gdufe.health_butler.service;

import com.gdufe.health_butler.HealthButlerApplicationTests;
import com.gdufe.health_butler.aop.AuthToken;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/4 12:15
 */
public class CoinDetailServiceTest extends HealthButlerApplicationTests {

    @Autowired
    private CoinDetailService coinDetailService;

    @Test
    public void getDetail() {
        System.out.println(coinDetailService.list());
    }
}
