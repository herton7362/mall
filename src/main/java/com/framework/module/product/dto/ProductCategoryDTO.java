package com.framework.module.product.dto;

import com.framework.module.product.domain.ProductCategory;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/17 11:23
 */
public class ProductCategoryDTO {
    @ApiModelProperty(value = "商品分类ID")
    private String id;
    @ApiModelProperty(value = "商品分类名称")
    private String name;
    @ApiModelProperty(value = "商品分类名称")
    private String coverImageUrl;

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

    public void convertFromPo(ProductCategory productCategory) {
        BeanUtils.copyProperties(productCategory, this);
        if (productCategory.getCoverImage() != null) {
            setCoverImageUrl("/attachment/download/" + productCategory.getCoverImage().getId() + "." + productCategory.getCoverImage().getFormat());
        }
    }
}
