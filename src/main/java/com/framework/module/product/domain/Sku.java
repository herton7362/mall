package com.framework.module.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kratos.entity.BaseEntity;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 最小库存单位
 * @author tang he
 * @since 1.0.0
 */
@Entity
@ApiModel("最小库存单位")
public class Sku extends BaseEntity {
    @ApiModelProperty(value = "商品规格条目")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="sku_product_standard_items",joinColumns={@JoinColumn(name="sku_id")},inverseJoinColumns={@JoinColumn(name="product_standard_item_id")})
    private List<ProductStandardItem> productStandardItems;
    @ApiModelProperty(value = "商品")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Product product;
    @ApiModelProperty(value = "单价")
    @Column(length = 11, precision = 2)
    private Double price;
    @ApiModelProperty(value = "库存数量")
    private Long stockCount;
    @ApiModelProperty(value = "封面图片")
    @ManyToOne
    private Attachment coverImage;
    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;

    public List<ProductStandardItem> getProductStandardItems() {
        if(productStandardItems != null && !productStandardItems.isEmpty()) {
            return productStandardItems
                    .stream()
                    .sorted((o1, o2) -> {
                        if(o1.getProductStandard() != null && o2.getProductStandard() != null)
                            return o1.getProductStandard().getSortNumber().compareTo(o2.getProductStandard().getSortNumber());
                        else
                            return 0;
                    })
                    .collect(Collectors.toList());
        }
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getStockCount() {
        return stockCount;
    }

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public Attachment getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Attachment coverImage) {
        this.coverImage = coverImage;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
