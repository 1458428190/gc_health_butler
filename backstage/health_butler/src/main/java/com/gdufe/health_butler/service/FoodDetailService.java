package com.gdufe.health_butler.service;

import com.gdufe.health_butler.entity.FoodDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 食物详请表 服务类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
public interface FoodDetailService extends IService<FoodDetail> {

    /**
     * 获取食物详情
     * @param fid
     *          食物id
     * @return
     */
    FoodDetail getByFid(long fid);
}
