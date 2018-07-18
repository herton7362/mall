package com.framework.module.product.vo;

import com.framework.module.product.domain.Product;
import com.framework.module.product.dto.ProductDTO;
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
    List<ProductDTO> allProduct = new ArrayList<>();
    @ApiModelProperty(value = "最新商品")
    List<ProductDTO> newestProduct = new ArrayList<>();
    @ApiModelProperty(value = "必买好货")
    List<ProductDTO> recommendProduct = new ArrayList<>();

    public List<ProductDTO> getAllProduct() {
        return allProduct;
    }

    public void setAllProduct(List<ProductDTO> allProduct) {
        this.allProduct = allProduct;
    }

    public List<ProductDTO> getNewestProduct() {
        return newestProduct;
    }

    public void setNewestProduct(List<ProductDTO> newestProduct) {
        this.newestProduct = newestProduct;
    }

    public List<ProductDTO> getRecommendProduct() {
        return recommendProduct;
    }

    public void setRecommendProduct(List<ProductDTO> recommendProduct) {
        this.recommendProduct = recommendProduct;
    }

    public void addAllProduct(PageResult<Product> productList) {
        for (Product p : productList.getContent()) {
            ProductDTO voProduct = new ProductDTO();
            voProduct.convertFromPo(p);
            getAllProduct().add(voProduct);
        }
    }

    public void addNewestProduct(PageResult<Product> productList) {
        for (Product p : productList.getContent()) {
            ProductDTO voProduct = new ProductDTO();
            voProduct.convertFromPo(p);
            getNewestProduct().add(voProduct);
        }
    }

    public void addRecommendProduct(PageResult<Product> productList) {
        for (Product p : productList.getContent()) {
            ProductDTO voProduct = new ProductDTO();
            voProduct.convertFromPo(p);
            getRecommendProduct().add(voProduct);
        }
    }
}
