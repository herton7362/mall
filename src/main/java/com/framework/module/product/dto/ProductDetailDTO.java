package com.framework.module.product.dto;

import com.framework.module.product.domain.Product;
import com.kratos.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ApiModel("商品详情")
public class ProductDetailDTO extends BaseDTO<ProductDetailDTO, Product> {
    @ApiModelProperty(value = "商品id")
    private String id;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "商品赠送积分")
    private Integer points;
    @ApiModelProperty(value = "商品显示价格")
    private String displayPrice;
    @ApiModelProperty(value = "封面图片路径")
    private String coverImageUrl;
    @ApiModelProperty(value = "商品样式集")
    private List<String> styleImageUrls;
    @ApiModelProperty(value = "商品详情")
    private List<String> detailImageUrls;
    @ApiModelProperty(value = "库存数量")
    private Long stockCount;
    @ApiModelProperty(value = "商品规格")
    private List<ProductProductStandardDTO> productStandards;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public List<String> getStyleImageUrls() {
        return styleImageUrls;
    }

    public void setStyleImageUrls(List<String> styleImageUrls) {
        this.styleImageUrls = styleImageUrls;
    }

    public List<String> getDetailImageUrls() {
        return detailImageUrls;
    }

    public void setDetailImageUrls(List<String> detailImageUrls) {
        this.detailImageUrls = detailImageUrls;
    }

    public Long getStockCount() {
        return stockCount;
    }

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public List<ProductProductStandardDTO> getProductStandards() {
        return productStandards;
    }

    public void setProductStandards(List<ProductProductStandardDTO> productStandards) {
        this.productStandards = productStandards;
    }


}
