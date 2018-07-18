package com.framework.module.marketing.dto;

import com.framework.module.marketing.domain.Coupon;
import com.kratos.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

@Component
public class CouponDTO extends BaseDTO<CouponDTO, Coupon> {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty(required = true, value = "面额")
    private Double amount;
    @ApiModelProperty(required = true, value = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
