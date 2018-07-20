package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderItem;
import com.kratos.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

@Component
@ApiModel("订单条目")
public class OrderItemDTO extends BaseDTO<OrderItemDTO, OrderItem> {
    @ApiModelProperty("订单 id")
    private String orderId;
    @ApiModelProperty("产品id")
    private String productId;
    @ApiModelProperty("sku id")
    private String skuId;
    @ApiModelProperty("数量")
    private Double count;
    @ApiModelProperty("单价")
    private Double price;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
