package com.framework.module.orderform.dto;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.Member;
import com.framework.module.orderform.domain.OrderForm;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderFormDTOConverter extends SimpleDTOConverter<OrderFormDTO, OrderForm> {
    @Override
    protected OrderForm doForward(OrderFormDTO orderFormDTO) {
        OrderForm orderForm = new OrderForm();
        Member member = MemberThread.getInstance().get();
        if(member == null) {
            throw new RuntimeException("未登录");
        }

        return super.doForward(orderFormDTO);
    }
}
