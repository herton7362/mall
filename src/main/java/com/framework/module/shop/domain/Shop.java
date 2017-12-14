package com.framework.module.shop.domain;

import com.kratos.entity.BaseEntity;
import com.kratos.module.attachment.domain.Attachment;
import com.kratos.module.auth.domain.OauthClientDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@ApiModel("店铺")
public class Shop extends BaseEntity {
    @ApiModelProperty(value = "名称")
    @Column(length = 200)
    private String name;
    @ApiModelProperty(value = "店铺样式集")
    @ManyToMany
    @JoinTable(name="shop_styles",joinColumns={@JoinColumn(name="shop_id")},inverseJoinColumns={@JoinColumn(name="attachment_id")})
    private List<Attachment> styleImages;
    @ApiModelProperty(value = "地址")
    @Column(length = 500)
    private String address;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId", referencedColumnName = "clientId")
    private OauthClientDetails oauthClientDetails;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attachment> getStyleImages() {
        return styleImages;
    }

    public void setStyleImages(List<Attachment> styleImages) {
        this.styleImages = styleImages;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OauthClientDetails getOauthClientDetails() {
        return oauthClientDetails;
    }

    public void setOauthClientDetails(OauthClientDetails oauthClientDetails) {
        this.oauthClientDetails = oauthClientDetails;
    }
}
