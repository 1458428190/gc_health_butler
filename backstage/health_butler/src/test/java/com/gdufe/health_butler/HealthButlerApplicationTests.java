package com.gdufe.health_butler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.gdufe.health_butler.dao")
public class HealthButlerApplicationTests {

    @Test
    public void contextLoads() {
    }

}
