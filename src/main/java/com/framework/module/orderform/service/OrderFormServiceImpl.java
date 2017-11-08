package com.framework.module.orderform.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.marketing.service.CouponService;
import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberService;
import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.domain.OrderFormRepository;
import com.framework.module.orderform.domain.OrderItem;
import com.framework.module.orderform.web.ApplyRejectParam;
import com.framework.module.orderform.web.RejectParam;
import com.framework.module.orderform.web.SendOutParam;
import com.framework.module.product.domain.Product;
import com.framework.module.record.domain.OperationRecord;
import com.framework.module.record.service.OperationRecordService;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Transactional
public class OrderFormServiceImpl extends AbstractCrudService<OrderForm> implements OrderFormService {
    private final OrderFormRepository orderFormRepository;
    private final MemberService memberService;
    private final OperationRecordService operationRecordService;
    private final OauthClientDetailsService oauthClientDetailsService;
    private final CouponService couponService;

    @Override
    protected PageRepository<OrderForm> getRepository() {
        return orderFormRepository;
    }

    @Override
    public OrderForm makeOrder(OrderForm orderForm) throws Exception {
        Member member = orderForm.getMember();
        if(member == null) {
            throw new BusinessException("下单账户未找到");
        }
        orderForm.setOrderNumber(getOutTradeNo());
        if(OrderForm.OrderStatus.PAYED == orderForm.getStatus()) {
            validAccount(orderForm);
            orderForm.setPaymentStatus(OrderForm.PaymentStatus.PAYED);
        } else if(OrderForm.OrderStatus.UN_PAY == orderForm.getStatus()) {
            orderForm.setPaymentStatus(OrderForm.PaymentStatus.UN_PAY);
        }
        List<OrderItem> items = new ArrayList<>();
        items.addAll(orderForm.getItems());
        orderForm.getItems().clear();
        final OrderForm newOrderForm = orderFormRepository.save(orderForm);
        items.forEach(newOrderForm::addItem);
        // 修改账户余额
        if(OrderForm.OrderStatus.PAYED == orderForm.getStatus()) {
            modifyMemberAccount(orderForm);
        }
        return orderForm;
    }

    /**
     * 修改账户余额
     * @param orderForm 订单
     */
    private void modifyMemberAccount(OrderForm orderForm) throws Exception {
        Member member = orderForm.getMember();
        Integer productPoints = 0;
        for (OrderItem orderItem : orderForm.getItems()) {
            productPoints += orderItem.getProduct().getPoints();
        }
        member.setPoint(subtractNumber(member.getPoint(), orderForm.getPoint()));
        member.setPoint(increaseNumber(member.getPoint(), productPoints));
        member.setBalance(subtractMoney(member.getBalance(), orderForm.getBalance()));
        memberService.save(member);
        record(member, orderForm.getCash(), orderForm.getBalance(), orderForm.getPoint(), orderForm.getItems());
    }

    @Override
    public Map<String, Integer> getOrderCounts(String memberId) throws Exception {
        OrderForm.OrderStatus[] orderStatuses = OrderForm.OrderStatus.values();
        Map<String, Integer> result = new HashMap<>();
        for (OrderForm.OrderStatus orderStatus : orderStatuses) {
            result.put(orderStatus.name(), orderFormRepository.countByStatus(orderStatus));
        }
        return result;
    }

    @Override
    public OrderForm pay(OrderForm orderForm) throws Exception {
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.UN_PAY != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        orderForm.getItems().forEach(item -> item.setOrderForm(orderForm));
        validAccount(orderForm);
        orderForm.setStatus(OrderForm.OrderStatus.PAYED);
        orderForm.setPaymentStatus(OrderForm.PaymentStatus.PAYED);
        final OrderForm newOrderForm = orderFormRepository.save(orderForm);
        modifyMemberAccount(newOrderForm);
        return newOrderForm;
    }

    @Override
    public OrderForm saveShippingInfo(SendOutParam sendOutParam) throws Exception {
        OrderForm orderForm = orderFormRepository.findOne(sendOutParam.getId());
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.PAYED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        if(!MemberThread.getInstance().get().getId().equals(orderForm.getMember().getId())) {
            throw new BusinessException("当前会员无权操作此订单");
        }
        orderForm.setStatus(OrderForm.OrderStatus.DELIVERED);
        orderForm.setShippingCode(sendOutParam.getShippingCode());
        orderForm.setShippingDate(sendOutParam.getShippingDate());
        orderFormRepository.save(orderForm);
        return orderForm;
    }

    @Override
    public OrderForm receive(String id) throws Exception {
        OrderForm orderForm = orderFormRepository.findOne(id);
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.DELIVERED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        if(!MemberThread.getInstance().get().getId().equals(orderForm.getMember().getId())) {
            throw new BusinessException("当前会员无权操作此订单");
        }
        orderForm.setStatus(OrderForm.OrderStatus.RECEIVED);
        orderForm.setFinishedDate(new Date().getTime());
        orderFormRepository.save(orderForm);
        return orderForm;
    }

    @Override
    public OrderForm applyReject(ApplyRejectParam applyRejectParam) throws Exception {
        OrderForm orderForm = orderFormRepository.findOne(applyRejectParam.getId());
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.DELIVERED != orderForm.getStatus()
                && OrderForm.OrderStatus.PAYED != orderForm.getStatus()
                && OrderForm.OrderStatus.RECEIVED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        if(!MemberThread.getInstance().get().getId().equals(orderForm.getMember().getId())) {
            throw new BusinessException("当前会员无权操作此订单");
        }
        orderForm.setStatus(OrderForm.OrderStatus.APPLY_REJECTED);
        orderForm.setApplyRejectRemark(applyRejectParam.getApplyRejectRemark());
        orderFormRepository.save(orderForm);
        return orderForm;
    }

    @Override
    public OrderForm reject(RejectParam rejectParam) throws Exception {
        OrderForm orderForm = orderFormRepository.findOne(rejectParam.getId());
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.APPLY_REJECTED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        orderForm.setStatus(OrderForm.OrderStatus.REJECTED);
        orderForm.setReturnedMoney(rejectParam.getReturnedMoney());
        orderForm.setApplyRejectRemark(rejectParam.getReturnedRemark());
        orderFormRepository.save(orderForm);
        return orderForm;
    }

    /**
     * 要求外部订单号必须唯一。
     * @return 订单号
     */
    private synchronized String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        String rStr = (r.nextInt() +"").replace("-", "1");
        key =  key + rStr;
        key = key.substring(0, 14);
        return key;
    }

    /**
     * 表单价格校验
     * 先计算实际货物的总价格
     * @param orderForm 订单对象
     * @throws Exception {@link com.kratos.exceptions.BusinessException}逻辑异常
     */
    private void validAccount(OrderForm orderForm) throws Exception {
        BigDecimal balance = new BigDecimal(orderForm.getBalance());
        BigDecimal cash = new BigDecimal(orderForm.getCash());
        BigDecimal point = new BigDecimal(orderForm.getPoint());
        BigDecimal customerPayAmount;
        customerPayAmount = balance.add(cash).add(point.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

        BigDecimal actualTotalAmount = new BigDecimal(0);
        Product product;
        for (OrderItem orderItem : orderForm.getItems()) {
            product = orderItem.getProduct();
            actualTotalAmount = actualTotalAmount.add(new BigDecimal(product.getPrice()).multiply(new BigDecimal(orderItem.getCount())));
        }

        if(orderForm.getCoupon() != null) {
            actualTotalAmount = new BigDecimal(couponService.useCoupon(orderForm.getCoupon().getId(), orderForm.getMember().getId(), actualTotalAmount.doubleValue()));
        }

        if(customerPayAmount.compareTo(actualTotalAmount) != 0) {
            throw new BusinessException("结算金额不正确");
        }
    }

    /**
     * 记录消费记录
     * @param member 会员
     * @param items 消费项
     */
    private void record(Member member, Double cash, Double balance, Integer point, List<OrderItem> items) throws Exception {
        OperationRecord rechargeRecord = new OperationRecord();
        rechargeRecord.setMember(member);
        rechargeRecord.setBusinessType(OperationRecord.BusinessType.CONSUME.name());
        rechargeRecord.setClient(oauthClientDetailsService.findOneByClientId(MemberThread.getInstance().getClientId()));
        rechargeRecord.setIpAddress(MemberThread.getInstance().getIpAddress());
        StringBuilder content = new StringBuilder();
        content.append(String.format("现金消费 %s 元，余额消费 %s 元，积分消费 %s 分", cash, balance, point));
        content.append("  消费项：");
        items.forEach(item -> {
            Product product = item.getProduct();
            content.append(product.getName());
            content.append(" x");
            content.append(item.getCount());
            content.append(",");
        });
        content.deleteCharAt(content.length() - 1);
        rechargeRecord.setContent(content.toString());
        operationRecordService.save(rechargeRecord);
    }

    private Double subtractMoney(Double sourceMoney, Double point) {
        if(sourceMoney == null) {
            sourceMoney = 0D;
        }
        BigDecimal sp = new BigDecimal(sourceMoney);
        return sp.subtract(new BigDecimal(point)).doubleValue();
    }

    private Integer subtractNumber(Integer sourcePoint, Integer point) {
        if(sourcePoint == null) {
            sourcePoint = 0;
        }
        return sourcePoint - point;
    }

    private Integer increaseNumber(Integer sourcePoint, Integer point) {
        if(sourcePoint == null) {
            sourcePoint = 0;
        }
        return sourcePoint + point;
    }

    @Autowired
    public OrderFormServiceImpl(
            OrderFormRepository orderFormRepository,
            MemberService memberService,
            OperationRecordService operationRecordService,
            OauthClientDetailsService oauthClientDetailsService,
            CouponService couponService
    ) {
        this.orderFormRepository = orderFormRepository;
        this.memberService = memberService;
        this.operationRecordService = operationRecordService;
        this.oauthClientDetailsService = oauthClientDetailsService;
        this.couponService = couponService;
    }
}
