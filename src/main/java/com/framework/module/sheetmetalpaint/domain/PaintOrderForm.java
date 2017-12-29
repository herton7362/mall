package com.framework.module.sheetmetalpaint.domain;

import com.framework.module.member.domain.MemberAddress;
import com.framework.module.orderform.base.BaseOrderForm;
import com.framework.module.shop.domain.Shop;
import com.framework.module.vehicle.domain.Vehicle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel("钣金喷漆订单")
public class PaintOrderForm extends BaseOrderForm<PaintOrderItem> {
    @ApiModelProperty(value = "车")
    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;
    @ApiModelProperty(value = "店铺")
    @ManyToOne(fetch = FetchType.EAGER)
    private Shop shop;
    @ApiModelProperty(value = "会员收货地址")
    @ManyToOne(fetch = FetchType.EAGER)
    private MemberAddress deliverToAddress;
    @ApiModelProperty(value = "订单状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PaintOrderForm.OrderStatus status;
    @ApiModelProperty(value = "支付状态")
    @Column(length = 20)
    private PaintOrderForm.PaymentStatus paymentStatus;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public MemberAddress getDeliverToAddress() {
        return deliverToAddress;
    }

    public void setDeliverToAddress(MemberAddress deliverToAddress) {
        this.deliverToAddress = deliverToAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public enum PaymentStatus {
        UN_PAY("待支付"), PAYED("已支付");
        private String displayName;
        PaymentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum OrderStatus {
        UN_PAY("待支付"),
        PAYED("已支付"),
        FINISHED("已完成"),
        APPLY_REJECTED("申请退款"),
        REJECTED("已退款"),
        CANCEL("已取消");
        private String displayName;
        OrderStatus(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }
}
