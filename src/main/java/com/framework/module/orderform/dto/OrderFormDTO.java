package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderForm;
import com.kratos.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ApiModel("订单")
public class OrderFormDTO extends BaseDTO<OrderFormDTO, OrderForm> {
    @ApiModelProperty("总价格")
    private Double total;
    @ApiModelProperty(value = "储值支付")
    private Double balance;
    @ApiModelProperty(value = "积分支付")
    private Integer point;
    @ApiModelProperty(value = "优惠券")
    private String couponId;
    @ApiModelProperty(value = "会员卡")
    private String memberCardId;
    @ApiModelProperty(value = "买家留言")
    private String remark;
    @ApiModelProperty("订单条目")
    private List<OrderItemDTO> items;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
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

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
