package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.service.OrderItemService;
import com.kratos.dto.BaseDTO;
import com.kratos.dto.Children;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Component
@ApiModel("订单")
public class OrderFormDTO extends BaseDTO<OrderFormDTO, OrderForm> {
    @ApiModelProperty("总价格")
    private Double total;
    @ApiModelProperty(value = "订单号，系统自动生成")
    private String orderNumber;
    @ApiModelProperty(value = "会员收货地址")
    private String deliverToAddressId;
    @ApiModelProperty(value = "储值支付")
    private Double balance = 0D;
    @ApiModelProperty(value = "积分支付")
    private Integer point = 0;
    @ApiModelProperty(value = "优惠券")
    private String couponId;
    @ApiModelProperty(value = "会员卡")
    private String memberCardId;
    @ApiModelProperty(value = "买家留言")
    private String remark;
    @ApiModelProperty(value = "订单状态")
    private OrderForm.OrderStatus status;
    @Children(service = OrderItemService.class)
    @ApiModelProperty("订单条目")
    private List<OrderItemDTO> items;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDeliverToAddressId() {
        return deliverToAddressId;
    }

    public void setDeliverToAddressId(String deliverToAddressId) {
        this.deliverToAddressId = deliverToAddressId;
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

    public OrderForm.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderForm.OrderStatus status) {
        this.status = status;
    }
}
