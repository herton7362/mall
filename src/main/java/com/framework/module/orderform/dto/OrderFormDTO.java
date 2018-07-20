package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderForm;
import com.kratos.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

@Component
@ApiModel("订单")
public class OrderFormDTO extends BaseDTO<OrderFormDTO, OrderForm> {
    @ApiModelProperty("总价格")
    private Double total;
    @ApiModelProperty(value = "优惠券")
    private String couponId;
    @ApiModelProperty(value = "会员卡")
    private String memberCardId;
    @ApiModelProperty(value = "买家留言")
    private String remark;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getMemberCardId() {
        return memberCardId;
    }

    public void setMemberCardId(String memberCardId) {
        this.memberCardId = memberCardId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
