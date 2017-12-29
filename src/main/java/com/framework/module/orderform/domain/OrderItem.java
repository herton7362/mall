package com.framework.module.orderform.domain;

import com.framework.module.orderform.base.BaseOrderItem;
import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.Sku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("订单项")
public class OrderItem extends BaseOrderItem<OrderForm> {
    @ApiModelProperty(value = "购买的商品")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    @ApiModelProperty(value = "购买的商品sku")
    @ManyToOne(fetch = FetchType.EAGER)
    private Sku sku;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }
}
