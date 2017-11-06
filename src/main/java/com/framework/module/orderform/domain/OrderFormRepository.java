package com.framework.module.orderform.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderFormRepository extends PageRepository<OrderForm> {
    @Query("select count(o) from OrderForm o where o.status = ?1")
    Integer countByStatus(OrderForm.OrderStatus orderStatus);
}
