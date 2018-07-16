package com.framework.module.product.web.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/16 15:17
 */
public class VoHomePage {
    @ApiModelProperty(value = "所有首页商品")
    List<VoProduct> allProduct = new ArrayList<>();
    @ApiModelProperty(value = "最新商品")
    List<VoProduct> newestProduct = new ArrayList<>();
    @ApiModelProperty(value = "必买好货")
    List<VoProduct> recommendProduct = new ArrayList<>();

    public List<VoProduct> getAllProduct() {
        return allProduct;
    }

    public void setAllProduct(List<VoProduct> allProduct) {
        this.allProduct = allProduct;
    }

    public List<VoProduct> getNewestProduct() {
        return newestProduct;
    }

    public void setNewestProduct(List<VoProduct> newestProduct) {
        this.newestProduct = newestProduct;
    }

    public List<VoProduct> getRecommendProduct() {
        return recommendProduct;
    }

    public void setRecommendProduct(List<VoProduct> recommendProduct) {
        this.recommendProduct = recommendProduct;
    }
}
