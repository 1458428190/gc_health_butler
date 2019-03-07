package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.ClockInVO;

import java.util.List;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/3 20:49
 */
public interface ClockInService {

    /**
     * 获取用户的打卡记录
     * @param token
     * @return
     */
    List<ClockInVO> list(String token);

    /**
     * 打卡
     * @param token
     * @param type
     * @return
     */
    String clockIn(String token, int type);
}
