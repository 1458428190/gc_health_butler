package com.gdufe.health_butler.service.impl;

import com.gdufe.health_butler.entity.Task;
import com.gdufe.health_butler.dao.TaskMapper;
import com.gdufe.health_butler.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
@Transactional
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

}
