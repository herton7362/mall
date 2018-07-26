package com.framework.module.orderform.service;

import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.dto.OrderFormDTO;
import com.framework.module.orderform.web.param.ApplyRejectParam;
import com.framework.module.orderform.web.OrderFormResult;
import com.framework.module.orderform.web.param.PreOrderParam;
import com.framework.module.orderform.web.param.RejectParam;
import com.framework.module.orderform.web.param.SendOutParam;
import com.kratos.common.CrudService;
import com.kratos.common.PageResult;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface OrderFormService extends CrudService<OrderForm> {
    /**
     * 下单
     * @param orderForm 详情请见{@link OrderForm}参数内有注释
     */
    OrderForm makeOrder(OrderForm orderForm) ;

    /**
     * 获取订单的数量
     * @param memberId 会员
     * @return 订单的数量{UN_PAY: 10, PAYED: 20}形式
     */
    Map<String,Integer> getOrderCounts(String memberId) ;

    /**
     * 支付
     * @param orderForm 订单参数
     */
    OrderForm pay(OrderForm orderForm)  ;

    /**
     * 发货
     * @param sendOutParam 发货参数
     */
    OrderForm saveShippingInfo(SendOutParam sendOutParam) ;

    /**
     * 确认收货
     * @param id 订单id
     */
    OrderForm receive(String id) ;

    /**
     * 申请退货
     * @param applyRejectParam 参数
     */
    OrderForm applyReject(ApplyRejectParam applyRejectParam) ;

    /**
     * 退款
     * @param rejectParam 参数
     */
    OrderForm reject(RejectParam rejectParam) ;

    /**
     * 获取今日销售额
     * @return 今日销售额
     */
    Double getTodaySale() ;

    /**
     * 获取本月销售额
     * @return 本月销售额
     */
    Double getMonthSale() ;

    /**
     * 获取每日销售额
     * @return 每日销售额
     */
    List<Map<String, Object>> getEverydaySale() ;

    /**
     * 已支付
     * @param outTradeNo 订单id
     */
    void payed(String outTradeNo) ;

    PageResult<OrderFormResult> findAllTranslated(PageRequest pageRequest, Map<String, String[]> param) ;

    List<OrderFormResult> findAllTranslated(Map<String, String[]> param) ;

    /**
     * 单品预下单
     * @param param 参数
     * @return 订单
     */
    OrderFormDTO createOneProductPreOrder(PreOrderParam param);

    /**
     * 计算订单价格
     * @param orderFormDTO 订单
     * @return 价格
     */
    Double calculateTotalPrice(OrderFormDTO orderFormDTO);

    /**
     * 购物车预下单
     * @param cartId 购物车id
     * @return 订单
     */
    OrderFormDTO createCartPreOrder(String cartId);

    /**
     * 下订单
     * @param orderFormDTO 参数
     * @return 订单
     */
    OrderFormDTO makeOrder(OrderFormDTO orderFormDTO);

    /**
     * 获得我的订单列表
     * @param memberId 会员ID
     * @param status 状态相关
     * @param pageSize
     * @param pageNum
     * @return 订单列表相关
     */
    List<OrderFormDTO> myOrderList(String memberId, String status, Integer pageSize, Integer pageNum);
}
