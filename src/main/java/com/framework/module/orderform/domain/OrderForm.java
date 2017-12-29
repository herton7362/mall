package com.framework.module.orderform.domain;

import com.framework.module.marketing.domain.Coupon;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberAddress;
import com.framework.module.orderform.base.BaseOrderForm;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 * @author tang he
 * @since 1.0.0
 */
@Entity
@ApiModel("订单")
public class OrderForm extends BaseOrderForm<OrderItem> {
    @ApiModelProperty(value = "会员收货地址")
    @ManyToOne(fetch = FetchType.EAGER)
    private MemberAddress deliverToAddress;
    @ApiModelProperty(value = "订单状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ApiModelProperty(value = "买家留言")
    @Column(length = 200)
    private String remark;
    @ApiModelProperty(value = "运单号")
    @Column(length = 36)
    private String shippingCode;
    @ApiModelProperty(value = "发货日期")
    private Long shippingDate;
    @ApiModelProperty(value = "配送状态")
    @Column(length = 20)
    private String shippingStatus;
    @ApiModelProperty(value = "支付状态")
    @Column(length = 20)
    private PaymentStatus paymentStatus;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public Long getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Long shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
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
        DELIVERED("已发货"),
        RECEIVED("已收货"),
        APPLY_REJECTED("申请退货"),
        REJECTED("已退货"),
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
