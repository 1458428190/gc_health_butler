package com.gdufe.health_butler.bean.vo;
import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description: 兑换的商品
 * @Date: 2019/3/1 12:02
 */
@Data
public class ConversionGoodsVO {

    /**
     * 耗费健康币
     */
    private long coin;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 兑换时间
     */
    private long time;

    /**
     * 商品图片
     */
    private String imgUrl;
}
