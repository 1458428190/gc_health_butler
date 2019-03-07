package com.gdufe.health_butler.service;

import com.gdufe.health_butler.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 查询用户可以查看到的商品信息
     * @return
     */
    List<Goods> listToUser();

    /**
     * 兑换商品
     * @param token
     * @param id
     *          商品id
     * @return
     */
    String conversion(String token, long id);
}
