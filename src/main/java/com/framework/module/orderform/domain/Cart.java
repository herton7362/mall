package com.framework.module.orderform.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 购物车
 * @author tang he
 * @since 1.0.0
 */
@Entity
@ApiModel("购物车")
public class Cart extends BaseEntity {
    @ApiModelProperty(value = "会员")
    @Column(length = 36)
    private String memberId;
    @ApiModelProperty(value = "购物车条目")
    @OneToMany(mappedBy = "cart")
    private List<CartItem> items;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
