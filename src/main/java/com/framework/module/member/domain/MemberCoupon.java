package com.framework.module.member.domain;

import com.framework.module.marketing.domain.Coupon;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel("会员优惠券关联表")
public class MemberCoupon extends BaseEntity {
    @ApiModelProperty(value = "会员")
    @Column(length = 36)
    private String memberId;
    @ApiModelProperty(value = "优惠券")
    @ManyToOne(fetch = FetchType.EAGER)
    private Coupon coupon;
    @ApiModelProperty(value = "是否使用")
    private Boolean used;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
