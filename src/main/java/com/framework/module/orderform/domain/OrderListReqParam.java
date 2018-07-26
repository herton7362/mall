package com.framework.module.orderform.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/26 16:38
 */
@ApiModel("我的订单请求结构体")
public class OrderListReqParam {
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
    @ApiModelProperty(value = "请求页数")
    private Integer pageNum;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
