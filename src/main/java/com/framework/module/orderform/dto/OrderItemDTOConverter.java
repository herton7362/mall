package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderItem;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDTOConverter extends SimpleDTOConverter<OrderItemDTO, OrderItem> {
}
