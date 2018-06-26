package com.framework.module.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@ApiModel("具体的商品规格")
public class ProductProductStandard extends BaseEntity {
    @ApiModelProperty(value = "规格")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductStandard productStandard;
    @ApiModelProperty(value = "规格条目")
    @ManyToMany
    @JoinTable(name="product_product_standard_items",joinColumns={@JoinColumn(name="product_product_standard_id")},inverseJoinColumns={@JoinColumn(name="product_standard_item_id")})
    private List<ProductStandardItem> productStandardItems;
    @ApiModelProperty(value = "商品")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Product product;

    public ProductStandard getProductStandard() {
        return productStandard;
    }

    public void setProductStandard(ProductStandard productStandard) {
        this.productStandard = productStandard;
    }

    public List<ProductStandardItem> getProductStandardItems() {
        return productStandardItems;
    }

    public void setProductStandardItems(List<ProductStandardItem> productStandardItems) {
        this.productStandardItems = productStandardItems;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
