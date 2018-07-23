package com.framework.module.orderform.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("预下单参数")
public class PreOrderParam {
    @ApiModelProperty("商品 id")
    private String productId;
    @ApiModelProperty("sku id")
    private String skuId;
    @ApiModelProperty("count")
    private Integer count;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
