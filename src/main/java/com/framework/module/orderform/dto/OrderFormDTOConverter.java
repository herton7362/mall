package com.framework.module.orderform.dto;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberAddress;
import com.framework.module.member.service.MemberAddressService;
import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.domain.OrderItem;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OrderFormDTOConverter extends SimpleDTOConverter<OrderFormDTO, OrderForm> {
    private final MemberAddressService memberAddressService;
    private final OrderItemDTO orderItemDTO;

    @Override
    protected OrderForm doForward(OrderFormDTO orderFormDTO) {
        OrderForm orderForm = super.doForward(orderFormDTO);
        Member member = MemberThread.getInstance().get();
        if (member == null) {
            throw new RuntimeException("未登录");
        }
        MemberAddress memberAddress = memberAddressService.findOne(orderFormDTO.getDeliverToAddressId());
        if (memberAddress == null) {
            throw new RuntimeException("收货地址未找到");
        }
        orderForm.setDeliverToAddress(memberAddress);
        orderForm.setMemberId(member.getId());
        return orderForm;
    }

    @Override
    protected OrderFormDTO doBackward(OrderForm orderForm) {
        OrderFormDTO orderFormDTO = super.doBackward(orderForm);
        List<OrderItem> items = orderForm.getItems();
        orderFormDTO.setItems(orderItemDTO.convertFor(items));
        return orderFormDTO;
    }

    @Autowired
    public OrderFormDTOConverter(MemberAddressService memberAddressService, OrderItemDTO orderItemDTO) {
        this.memberAddressService = memberAddressService;
        this.orderItemDTO = orderItemDTO;
    }
}
