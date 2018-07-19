package com.framework.module.product.dto;

import com.framework.module.product.domain.Product;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/16 11:12
 */
public class ProductDTO {
    @ApiModelProperty(value = "商品ID")
    private String id;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "商品封面地址")
    private String coverImageUrl;
    @ApiModelProperty(value = "商品价钱")
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public <T extends BaseEntity> void convertFromPo(Product p) {
        BeanUtils.copyProperties(p, this);
        setCoverImageUrl("/attachment/download/" + p.getCoverImage().getId() + "." + p.getCoverImage().getFormat());
        setPrice(p.getDisplayPrice());
    }
}
