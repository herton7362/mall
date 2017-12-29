package com.framework.module.orderform.base;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseOrderItem<T extends BaseOrderForm> extends BaseEntity {
    @ApiModelProperty(value = "订单")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private T orderForm;
    @ApiModelProperty(value = "购买的数量")
    @Column(length = 11, precision = 2)
    private Double count;
    @ApiModelProperty(value = "产品积分")
    private Integer point;
    @ApiModelProperty(value = "产品单价")
    @Column(length = 11, precision = 2)
    private Double price;

    public T getOrderForm() {
        return orderForm;
    }

    public void setOrderForm(T orderForm) {
        this.orderForm = orderForm;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
