package com.framework.module.marketing.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("获取可用优惠券")
public class GetAvailableCouponsParam {
    @ApiModelProperty(value = "商品id")
    private String productId;
    @ApiModelProperty(value = "sku id")
    private String skuId;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "购物车条目id")
    private List<String> cartItemIds;

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

    public List<String> getCartItemIds() {
        return cartItemIds;
    }

    public void setCartItemIds(List<String> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }
}
