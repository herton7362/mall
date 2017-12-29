package com.framework.module.orderform.service;

import com.framework.module.orderform.base.BaseOrderForm;
import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.web.ApplyRejectParam;
import com.framework.module.orderform.web.RejectParam;
import com.framework.module.orderform.web.SendOutParam;
import com.kratos.common.CrudService;

import java.util.List;
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

    /**
     * 发货
     * @param sendOutParam 发货参数
     */
    OrderForm saveShippingInfo(SendOutParam sendOutParam) throws Exception;

    /**
     * 确认收货
     * @param id 订单id
     */
    OrderForm receive(String id) throws Exception;

    /**
     * 申请退货
     * @param applyRejectParam 参数
     */
    OrderForm applyReject(ApplyRejectParam applyRejectParam) throws Exception;

    /**
     * 退款
     * @param rejectParam 参数
     */
    OrderForm reject(RejectParam rejectParam) throws Exception;

    /**
     * 获取今日销售额
     * @return 今日销售额
     */
    Double getTodaySale() throws Exception;

    /**
     * 获取本月销售额
     * @return 本月销售额
     */
    Double getMonthSale() throws Exception;

    /**
     * 获取每日销售额
     * @return 每日销售额
     */
    List<Map<String, Object>> getEverydaySale() throws Exception;

    /**
     * 要求外部订单号必须唯一。
     * @return 订单号
     */
    String getOutTradeNo() throws Exception;

    /**
     * 表单价格校验
     * 先计算实际货物的总价格
     * @param orderForm 订单对象
     * @throws Exception {@link com.kratos.exceptions.BusinessException}逻辑异常
     */
    void validAccount(BaseOrderForm orderForm) throws Exception;
}
