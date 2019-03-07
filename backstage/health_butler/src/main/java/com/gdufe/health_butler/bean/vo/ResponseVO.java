package com.gdufe.health_butler.bean.vo;

import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/2/26 19:56
 */
@Data
public class ResponseVO {
    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 结果
     */
    private Object data;

    /**
     * 错误信息
     */
    private String errMsg;

    public ResponseVO(ResponseStatusEnum responseStatus, Object data) {
        this.code = responseStatus.getCode();
        this.msg = responseStatus.getMsg();
        this.data = data;
    }

    public ResponseVO(ResponseStatusEnum responseStatus, Object data, String errMsg) {
        this.code = responseStatus.getCode();
        this.msg = responseStatus.getMsg();
        this.data = data;
        this.errMsg = errMsg;
    }
}
