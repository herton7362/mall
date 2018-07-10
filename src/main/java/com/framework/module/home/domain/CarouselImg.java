package com.framework.module.home.domain;

import com.kratos.entity.BaseEntity;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("首页轮播图片")
public class CarouselImg extends BaseEntity {
    @ApiModelProperty(value = "图片")
    @ManyToOne
    private Attachment image;
    @ApiModelProperty(value = "商品id")
    @Column(length = 36)
    private String productId;
    @ApiModelProperty(value = "跳转url地址")
    @Column(length = 2000)
    private String url;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Attachment getImage() {
        return image;
    }

    public void setImage(Attachment image) {
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
