package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.bean.vo.CoinDetailVO;
import com.gdufe.health_butler.entity.CoinDetail;
import com.gdufe.health_butler.dao.CoinDetailMapper;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.CoinDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 健康币交易明细表 服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@Service
public class CoinDetailServiceImpl extends ServiceImpl<CoinDetailMapper, CoinDetail> implements CoinDetailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Override
    public CoinDetailVO getDetail(String token) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:getDetail, uid:{}, token:{}]", user.getId(), token);

        CoinDetailVO coinDetailVO = new CoinDetailVO();
        coinDetailVO.setHealthCoin(user.getHealthCoin());

        QueryWrapper<CoinDetail> coinDetailQueryWrapper = new QueryWrapper<>();
        coinDetailQueryWrapper.lambda().eq(CoinDetail::getUid, user.getId()).orderByDesc(CoinDetail::getCreateTime);
        List<CoinDetail> list = list(coinDetailQueryWrapper);

        AtomicLong sumIncome = new AtomicLong();
        AtomicLong sumExpend = new AtomicLong();
        List<CoinDetail> incomeDetail = new ArrayList<>();
        List<CoinDetail> expendDetail = new ArrayList<>();

        list.forEach(coinDetail -> {
            if(coinDetail.getCoin() < 0) {
                expendDetail.add(coinDetail);
                sumExpend.addAndGet(Math.abs(coinDetail.getCoin()));
            } else {
                incomeDetail.add(coinDetail);
                sumIncome.addAndGet(Math.abs(coinDetail.getCoin()));
            }
        });

        coinDetailVO.setIncomeDetail(incomeDetail);
        coinDetailVO.setExpendDetail(expendDetail);
        coinDetailVO.setSumIncome(sumIncome.get());
        coinDetailVO.setSumExpend(sumExpend.get());

        return coinDetailVO;
    }
}
