package com.framework.module.orderform.base;

import com.framework.module.marketing.domain.Coupon;
import com.framework.module.member.domain.Member;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class BaseOrderForm<T extends BaseOrderItem> extends BaseEntity {
    @ApiModelProperty(value = "会员")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;
    @ApiModelProperty(value = "优惠券")
    @ManyToOne(fetch = FetchType.EAGER)
    private Coupon coupon;
    @ApiModelProperty(value = "订单号，系统自动生成")
    @Column(length = 20)
    private String orderNumber;
    @ApiModelProperty(value = "现金支付")
    @Column(length = 11, precision = 2)
    private Double cash;
    @ApiModelProperty(value = "储值支付")
    @Column(length = 11, precision = 2)
    private Double balance;
    @ApiModelProperty(value = "积分支付")
    private Integer point;
    @ApiModelProperty(value = "订单条目")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderForm")
    private List<T> items;
    @ApiModelProperty(value = "结单日期")
    private Long finishedDate;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Long getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Long finishedDate) {
        this.finishedDate = finishedDate;
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

    public void addItem(T item) {
        if(items == null) {
            items = new ArrayList<>();
        }
        item.setOrderForm(this);
        items.add(item);
    }
}
