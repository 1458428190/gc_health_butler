package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.entity.FoodDetail;
import com.gdufe.health_butler.dao.FoodDetailMapper;
import com.gdufe.health_butler.service.FoodDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 食物详请表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
@Transactional
public class FoodDetailServiceImpl extends ServiceImpl<FoodDetailMapper, FoodDetail> implements FoodDetailService {

    @Override
    public FoodDetail getByFid(long fid) {
        QueryWrapper<FoodDetail> foodDetailQueryWrapper = new QueryWrapper<>();
        foodDetailQueryWrapper.lambda().eq(FoodDetail::getFid, fid);
        return getOne(foodDetailQueryWrapper);
    }
}
