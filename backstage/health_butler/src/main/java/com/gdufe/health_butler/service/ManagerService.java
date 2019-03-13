package com.gdufe.health_butler.service;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/13 14:36
 */
public interface ManagerService {
    /**
     * 系统赠送
     * @param uid
     * @param coin
     * @return
     */
    String give(long uid, long coin);
}
