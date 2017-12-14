package com.framework.module.sheetmetalpaint.domain;

import com.framework.module.marketing.domain.Coupon;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberAddress;
import com.framework.module.orderform.domain.OrderItem;
import com.framework.module.shop.domain.Shop;
import com.framework.module.vehicle.domain.Vehicle;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@ApiModel("钣金喷漆订单")
public class PaintOrderForm extends BaseEntity {
    @ApiModelProperty(value = "车")
    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;
    @ApiModelProperty(value = "店铺")
    @ManyToOne(fetch = FetchType.EAGER)
    private Shop shop;
    @ApiModelProperty(value = "会员")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;
    @ApiModelProperty(value = "优惠券")
    @ManyToOne(fetch = FetchType.EAGER)
    private Coupon coupon;
    @ApiModelProperty(value = "会员收货地址")
    @ManyToOne(fetch = FetchType.EAGER)
    private MemberAddress deliverToAddress;
    @ApiModelProperty(value = "现金支付")
    @Column(length = 11, precision = 2)
    private Double cash;
    @ApiModelProperty(value = "储值支付")
    @Column(length = 11, precision = 2)
    private Double balance;
    @ApiModelProperty(value = "积分支付")
    private Integer point;
    @ApiModelProperty(value = "订单状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PaintOrderForm.OrderStatus status;
    @ApiModelProperty(value = "订单条目")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderForm")
    private List<OrderItem> items;
    @ApiModelProperty(value = "结单日期")
    private Long finishedDate;
    @ApiModelProperty(value = "支付状态")
    @Column(length = 20)
    private PaintOrderForm.PaymentStatus paymentStatus;
    @ApiModelProperty(value = "退款金额")
    @Column(length = 11, precision = 2)
    private Double returnedMoney;
    @ApiModelProperty(value = "退款余额")
    @Column(length = 11, precision = 2)
    private Double returnedBalance;
    @ApiModelProperty(value = "退款积分")
    private Integer returnedPoint;
    @ApiModelProperty(value = "退款备注")
    @Column(length = 500)
    private String returnedRemark;
    @ApiModelProperty(value = "申请退款备注")
    @Column(length = 500)
    private String applyRejectRemark;

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public MemberAddress getDeliverToAddress() {
        return deliverToAddress;
    }

    public void setDeliverToAddress(MemberAddress deliverToAddress) {
        this.deliverToAddress = deliverToAddress;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Long getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Long finishedDate) {
        this.finishedDate = finishedDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getReturnedMoney() {
        return returnedMoney;
    }

    public void setReturnedMoney(Double returnedMoney) {
        this.returnedMoney = returnedMoney;
    }

    public Double getReturnedBalance() {
        return returnedBalance;
    }

    public void setReturnedBalance(Double returnedBalance) {
        this.returnedBalance = returnedBalance;
    }

    public Integer getReturnedPoint() {
        return returnedPoint;
    }

    public void setReturnedPoint(Integer returnedPoint) {
        this.returnedPoint = returnedPoint;
    }

    public String getReturnedRemark() {
        return returnedRemark;
    }

    public void setReturnedRemark(String returnedRemark) {
        this.returnedRemark = returnedRemark;
    }

    public String getApplyRejectRemark() {
        return applyRejectRemark;
    }

    public void setApplyRejectRemark(String applyRejectRemark) {
        this.applyRejectRemark = applyRejectRemark;
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
