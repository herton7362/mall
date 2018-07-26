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
    @ApiModelProperty(required = true, value = "开始时间")
    private Long startDate;
    @ApiModelProperty(required = true, value = "结束时间")
    private Long endDate;
    @ApiModelProperty(required = true, value = "使用条件：满 ? 元，无门槛请输入0")
    private Double minAmount;

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

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }
}
