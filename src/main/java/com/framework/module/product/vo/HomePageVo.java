package com.framework.module.product.vo;

import com.framework.module.product.domain.Product;
import com.framework.module.product.dto.ProductVo;
import com.kratos.common.PageResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/16 15:17
 */
public class HomePageVo {
    @ApiModelProperty(value = "所有首页商品")
    List<ProductVo> allProduct = new ArrayList<>();
    @ApiModelProperty(value = "最新商品")
    List<ProductVo> newestProduct = new ArrayList<>();
    @ApiModelProperty(value = "必买好货")
    List<ProductVo> recommendProduct = new ArrayList<>();

    public List<ProductVo> getAllProduct() {
        return allProduct;
    }

    public void setAllProduct(List<ProductVo> allProduct) {
        this.allProduct = allProduct;
    }

    public List<ProductVo> getNewestProduct() {
        return newestProduct;
    }

    public void setNewestProduct(List<ProductVo> newestProduct) {
        this.newestProduct = newestProduct;
    }

    public List<ProductVo> getRecommendProduct() {
        return recommendProduct;
    }

    public void setRecommendProduct(List<ProductVo> recommendProduct) {
        this.recommendProduct = recommendProduct;
    }

    public void addAllProduct(PageResult<Product> productList) {
        for (Product p : productList.getContent()) {
            ProductVo voProduct = new ProductVo();
            voProduct.convertFromPo(p);
            getAllProduct().add(voProduct);
        }
    }

    public void addNewestProduct(PageResult<Product> productList) {
        for (Product p : productList.getContent()) {
            ProductVo voProduct = new ProductVo();
            voProduct.convertFromPo(p);
            getNewestProduct().add(voProduct);
        }
    }

    public void addRecommendProduct(PageResult<Product> productList) {
        for (Product p : productList.getContent()) {
            ProductVo voProduct = new ProductVo();
            voProduct.convertFromPo(p);
            getRecommendProduct().add(voProduct);
        }
    }
}
