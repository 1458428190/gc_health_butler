package com.gdufe.health_butler.service.impl;

import com.gdufe.health_butler.entity.Operation;
import com.gdufe.health_butler.dao.OperationMapper;
import com.gdufe.health_butler.service.OperationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-13
 */
@Service
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {

}
