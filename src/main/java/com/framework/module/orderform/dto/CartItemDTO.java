package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.CartItem;
import com.kratos.dto.BaseDTO;
import com.kratos.dto.Parent;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

@Component
public class CartItemDTO extends BaseDTO<CartItemDTO, CartItem> {
    @Parent
    @ApiModelProperty(value = "购物车")
    private String cartId;
    @ApiModelProperty(value = "商品")
    private String productId;
    @ApiModelProperty(value = "商品展示图片地址")
    private String coverImageUrl;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "sku")
    private String skuId;
    @ApiModelProperty(value = "选中")
    private Boolean checked;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
