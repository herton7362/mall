package com.framework.module.orderform.service;

import com.framework.module.orderform.domain.OrderForm;
import com.kratos.common.CrudService;

import java.util.Map;

public interface OrderFormService extends CrudService<OrderForm> {
    /**
     * 下单
     * @param orderForm 详情请见{@link OrderForm}参数内有注释
     */
    OrderForm makeOrder(OrderForm orderForm) throws Exception;

    /**
     * 获取订单的数量
     * @param memberId 会员
     * @return 订单的数量{UN_PAY: 10, PAYED: 20}形式
     */
    Map<String,Integer> getOrderCounts(String memberId) throws Exception;

    /**
     * 支付
     * @param orderForm 订单参数
     */
    OrderForm pay(OrderForm orderForm)  throws Exception;
}
