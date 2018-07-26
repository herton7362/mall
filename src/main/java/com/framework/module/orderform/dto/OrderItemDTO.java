package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderItem;
import com.kratos.dto.BaseDTO;
import com.kratos.dto.Parent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

@Component
@ApiModel("订单条目")
public class OrderItemDTO extends BaseDTO<OrderItemDTO, OrderItem> {
    @Parent(fieldName = "orderForm.id")
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
    @ApiModelProperty(value = "商品规格")
    private String productStandardNames;
    @ApiModelProperty(value = "商品封面地址")
    private String coverImageUrl;
    @ApiModelProperty(value = "商品名称")
    private String productName;

    public String getOrderId() {
        return orderId;
    }

    public OrderItemDTO setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public OrderItemDTO setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getSkuId() {
        return skuId;
    }

    public OrderItemDTO setSkuId(String skuId) {
        this.skuId = skuId;
        return this;
    }

    public Double getCount() {
        return count;
    }

    public OrderItemDTO setCount(Double count) {
        this.count = count;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public OrderItemDTO setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getProductStandardNames() {
        return productStandardNames;
    }

    public OrderItemDTO setProductStandardNames(String productStandardNames) {
        this.productStandardNames = productStandardNames;
        return this;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public OrderItemDTO setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public OrderItemDTO setProductName(String productName) {
        this.productName = productName;
        return this;
    }
}
