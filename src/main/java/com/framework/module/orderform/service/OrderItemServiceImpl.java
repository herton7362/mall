package com.framework.module.orderform.service;

import com.framework.module.orderform.domain.OrderItem;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderItemServiceImpl extends AbstractCrudService<OrderItem> implements OrderItemService {
}
