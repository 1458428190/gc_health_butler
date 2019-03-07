package com.gdufe.health_butler.bean.vo;

import com.gdufe.health_butler.entity.Task;
import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description: 任务VO
 * @Date: 2019/2/28 18:10
 */
@Data
public class TaskVO {

    /**
     * 任务
     */
    private Task task;

    /**
     * 完成情况
     */
    private boolean finish;

}
