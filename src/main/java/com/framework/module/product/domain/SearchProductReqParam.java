package com.framework.module.product.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/31 10:19
 */
@ApiModel("搜索商品")
public class SearchProductReqParam {
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 30;
    @ApiModelProperty(value = "请求页数")
    private Integer pageNum = 1;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
