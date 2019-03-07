package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.common.enums.DealType;
import com.gdufe.health_butler.common.enums.RecordType;
import com.gdufe.health_butler.entity.CoinDetail;
import com.gdufe.health_butler.entity.Goods;
import com.gdufe.health_butler.dao.GoodsMapper;
import com.gdufe.health_butler.entity.Record;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.CoinDetailService;
import com.gdufe.health_butler.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.RecordService;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@Service
@Transactional
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private CoinDetailService coinDetailService;

    /**
     * 查询商品
     * @return
     */
    @Override
    public List<Goods> listToUser() {
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.lambda().select(Goods::getId, Goods::getName, Goods::getImgUrl, Goods::getPrice, Goods::getSurplus);
        return list(goodsQueryWrapper);
    }

    /**
     * TODO 存在并发问题, 当多个人同时进行兑换时
     * @param token
     * @param id
     *          商品id
     * @return
     */
    @Override
    public String conversion(String token, long id) {
        String openId = TokenContainer.get(token).getOpenId();
        logger.info("[op:conversion, token:{}, openId:{}, id:{}]", token, openId, id);
        User user = userService.getByOpenId(openId);
        Goods goods = getById(id);

        // 检查是否符合兑换的条件
        if(goods.getSurplus() <= 0) {
            return "商品剩余量为0, 无法兑换";
        }

        if(goods.getPrice() > user.getHealthCoin()) {
            return "您的健康币不足, 无法兑换";
        }

        // 可以兑换
        long nowTime = System.currentTimeMillis();
        goods.setSurplus(goods.getSurplus() - 1);
        goods.setModifiedTime(nowTime);
        updateById(goods);

        user.setHealthCoin(user.getHealthCoin() - goods.getPrice());
        user.setModifiedTime(nowTime);
        userService.updateById(user);

        Record record = new Record();
        record.setUid(user.getId());
        record.setType(RecordType.CONVERSION.getValue());
        record.setExtra(goods.getId() + "");
        record.setCreateTime(nowTime);
        record.setModifiedTime(nowTime);
        recordService.save(record);

        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setCoin(0 - goods.getPrice());
        coinDetail.setCreateTime(nowTime);
        coinDetail.setDescription("兑换" + goods.getName());
        coinDetail.setModifiedTime(nowTime);
        coinDetail.setType(DealType.CONVERSION.getValue());
        coinDetail.setUid(user.getId());
        coinDetailService.save(coinDetail);

        return "success$" + goods.getDetail();
    }
}
