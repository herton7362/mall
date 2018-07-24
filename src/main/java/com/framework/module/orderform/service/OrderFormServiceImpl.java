package com.framework.module.orderform.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.marketing.domain.Coupon;
import com.framework.module.marketing.service.CouponService;
import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberCardService;
import com.framework.module.member.service.MemberService;
import com.framework.module.orderform.domain.Cart;
import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.domain.OrderFormRepository;
import com.framework.module.orderform.domain.OrderItem;
import com.framework.module.orderform.dto.CartDTO;
import com.framework.module.orderform.dto.CartItemDTO;
import com.framework.module.orderform.dto.OrderFormDTO;
import com.framework.module.orderform.dto.OrderItemDTO;
import com.framework.module.orderform.web.OrderFormResult;
import com.framework.module.orderform.web.param.ApplyRejectParam;
import com.framework.module.orderform.web.param.PreOrderParam;
import com.framework.module.orderform.web.param.RejectParam;
import com.framework.module.orderform.web.param.SendOutParam;
import com.framework.module.product.domain.*;
import com.framework.module.record.domain.OperationRecord;
import com.framework.module.record.service.OperationRecordService;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.dto.CascadePersistHelper;
import com.kratos.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class OrderFormServiceImpl extends AbstractCrudService<OrderForm> implements OrderFormService {
    private final OrderFormRepository orderFormRepository;
    private final MemberService memberService;
    private final OperationRecordService operationRecordService;
    private final ProductRepository productRepository;
    private final CouponService couponService;
    private final MemberCardService memberCardService;
    private final SkuRepository skuRepository;
    private final CartService cartService;
    private final CartDTO cartDTO;

    @Override
    protected PageRepository<OrderForm> getRepository() {
        return orderFormRepository;
    }

    @Override
    public OrderForm makeOrder(OrderForm orderForm)  {
        if(orderForm.getMemberId() == null) {
            throw new BusinessException("下单账户未找到");
        }
        orderForm.setOrderNumber(getOutTradeNo());
        validAccount(orderForm);
        if(OrderForm.OrderStatus.PAYED == orderForm.getStatus()) {
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
            consumeModifyMemberAccount(orderForm);
        }
        return orderForm;
    }

    /**
     * 消费修改账户余额
     * @param orderForm 订单
     */
    private void consumeModifyMemberAccount(OrderForm orderForm)  {
        Member oldMember = memberService.findOne(orderForm.getMemberId());
        Integer productPoints = 0;
        for (OrderItem orderItem : orderForm.getItems()) {
            productPoints += orderItem.getProduct().getPoints();
        }
        oldMember.setSalePoint(subtractNumber(oldMember.getSalePoint(), orderForm.getPoint()));
        oldMember.setPoint(increaseNumber(oldMember.getPoint(), productPoints));
        oldMember.setSalePoint(increaseNumber(oldMember.getSalePoint(), productPoints));
        oldMember.setBalance(subtractMoney(oldMember.getBalance(), orderForm.getBalance()));
        memberService.save(oldMember);
        recordConsume(oldMember, orderForm.getCash(), orderForm.getBalance(), orderForm.getPoint(), orderForm.getDiscount(), orderForm.getItems());
    }

    /**
     * 退款修改账户余额
     * @param orderForm 订单
     */
    private void rejectModifyMemberAccount(OrderForm orderForm)  {
        Member oldMember = memberService.findOne(orderForm.getMemberId());
        Integer productPoints = 0;
        for (OrderItem orderItem : orderForm.getItems()) {
            productPoints += orderItem.getProduct().getPoints();
        }
        oldMember.setSalePoint(increaseNumber(oldMember.getSalePoint(), orderForm.getReturnedPoint()));
        oldMember.setSalePoint(subtractNumber(oldMember.getSalePoint(), productPoints));
        oldMember.setBalance(increaseMoney(oldMember.getBalance(), orderForm.getReturnedBalance()));
        memberService.save(oldMember);
        recordReject(oldMember, orderForm.getReturnedMoney(), orderForm.getReturnedBalance(), orderForm.getReturnedPoint(), orderForm);
    }


    @Override
    public Map<String, Integer> getOrderCounts(String memberId)  {
        OrderForm.OrderStatus[] orderStatuses = OrderForm.OrderStatus.values();
        Map<String, Integer> result = new HashMap<>();
        for (OrderForm.OrderStatus orderStatus : orderStatuses) {
            result.put(orderStatus.name(), orderFormRepository.countByStatusAndMemberId(orderStatus, memberId));
        }
        return result;
    }

    @Override
    public OrderForm pay(OrderForm orderForm)  {
        if(StringUtils.isBlank(orderForm.getId())) {
            throw new BusinessException("订单id不能为空");
        }
        orderForm = findOne(orderForm.getId());
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.UN_PAY != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        orderForm.setStatus(OrderForm.OrderStatus.PAYED);
        orderForm.setPaymentStatus(OrderForm.PaymentStatus.PAYED);
        final OrderForm newOrderForm = orderFormRepository.save(orderForm);
        consumeModifyMemberAccount(newOrderForm);
        return newOrderForm;
    }

    @Override
    public OrderForm saveShippingInfo(SendOutParam sendOutParam)  {
        OrderForm orderForm = orderFormRepository.findOne(sendOutParam.getId());
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.PAYED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        orderForm.setStatus(OrderForm.OrderStatus.DELIVERED);
        orderForm.setShippingCode(sendOutParam.getShippingCode());
        orderForm.setShippingDate(sendOutParam.getShippingDate());
        orderFormRepository.save(orderForm);
        orderForm.getItems().forEach(orderItem -> {
            Sku sku = orderItem.getSku();
            if(sku!= null) {
                Long count = 0L;
                if(sku.getStockCount() != null) {
                    count = sku.getStockCount();
                }
                sku.setStockCount(count - 1);
                skuRepository.save(sku);
            } else {
                Product product = orderItem.getProduct();
                Long count = 0L;
                if(product.getStockCount() != null) {
                    count = product.getStockCount();
                }
                product.setStockCount(count - 1);
                productRepository.save(product);
            }
        });
        return orderForm;
    }

    @Override
    public OrderForm receive(String id)  {
        OrderForm orderForm = orderFormRepository.findOne(id);
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.DELIVERED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        if(!MemberThread.getInstance().get().getId().equals(orderForm.getMemberId())) {
            throw new BusinessException("当前会员无权操作此订单");
        }
        orderForm.setStatus(OrderForm.OrderStatus.RECEIVED);
        orderForm.setFinishedDate(new Date().getTime());
        orderFormRepository.save(orderForm);
        return orderForm;
    }

    @Override
    public OrderForm applyReject(ApplyRejectParam applyRejectParam)  {
        OrderForm orderForm = orderFormRepository.findOne(applyRejectParam.getId());
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.DELIVERED != orderForm.getStatus()
                && OrderForm.OrderStatus.PAYED != orderForm.getStatus()
                && OrderForm.OrderStatus.RECEIVED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        if(!MemberThread.getInstance().get().getId().equals(orderForm.getMemberId())) {
            throw new BusinessException("当前会员无权操作此订单");
        }
        orderForm.setStatus(OrderForm.OrderStatus.APPLY_REJECTED);
        orderForm.setApplyRejectRemark(applyRejectParam.getApplyRejectRemark());
        orderFormRepository.save(orderForm);
        return orderForm;
    }

    @Override
    public OrderForm reject(RejectParam rejectParam)  {
        OrderForm orderForm = orderFormRepository.findOne(rejectParam.getId());
        if(orderForm == null) {
            throw new BusinessException("订单未找到");
        }
        if(OrderForm.OrderStatus.APPLY_REJECTED != orderForm.getStatus()) {
            throw new BusinessException("订单状态不正确");
        }
        orderForm.setStatus(OrderForm.OrderStatus.REJECTED);
        orderForm.setReturnedMoney(rejectParam.getReturnedMoney());
        orderForm.setReturnedBalance(rejectParam.getReturnedBalance());
        orderForm.setReturnedPoint(rejectParam.getReturnedPoint());
        orderForm.setApplyRejectRemark(rejectParam.getReturnedRemark());
        orderFormRepository.save(orderForm);
        // 修改账户余额
        rejectModifyMemberAccount(orderForm);
        orderForm.getItems().forEach(orderItem -> {
            Sku sku = orderItem.getSku();
            if(sku!= null) {
                sku.setStockCount(sku.getStockCount() + 1);
                skuRepository.save(sku);
            } else {
                Product product = orderItem.getProduct();
                product.setStockCount(product.getStockCount() + 1);
                productRepository.save(product);
            }
        });
        return orderForm;
    }

    @Override
    public Double getTodaySale()  {
        return orderFormRepository.getTodaySale();
    }

    @Override
    public Double getMonthSale()  {
        return orderFormRepository.getMonthSale();
    }

    @Override
    public List<Map<String, Object>> getEverydaySale()  {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map;
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDate.getTime());
        endDate.add(Calendar.DATE, 1);
        Double sale;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 8; i++) {
            map = new HashMap<>();
            sale = orderFormRepository.getSaleByDate(startDate.getTime().getTime(), endDate.getTime().getTime());
            map.put("y", s.format(startDate.getTime()));
            map.put("item1", sale);
            startDate.add(Calendar.DATE, -1);
            endDate.add(Calendar.DATE, -1);
            result.add(map);
        }
        return result;
    }

    @Override
    public void payed(String outTradeNo)  {
        Map<String, String[]> param = new HashMap<>();
        param.put("orderNumber", new String[]{outTradeNo});
        List<OrderForm> orderForms = findAll(param);
        if(orderForms != null && !orderForms.isEmpty()) {
            OrderForm orderForm = orderForms.get(0);
            orderForm.setStatus(OrderForm.OrderStatus.PAYED);
            orderForm.setPaymentStatus(OrderForm.PaymentStatus.PAYED);
            orderFormRepository.save(orderForm);
            consumeModifyMemberAccount(orderForm);
        }
    }

    @Override
    public PageResult<OrderFormResult> findAllTranslated(PageRequest pageRequest, Map<String, String[]> param)  {
        PageResult<OrderForm> page = findAll(pageRequest, param);
        PageResult<OrderFormResult> pageResult = new PageResult<>();
        pageResult.setSize(page.getSize());
        pageResult.setTotalElements(page.getTotalElements());
        pageResult.setContent(translateResults(page.getContent()));
        return pageResult;
    }

    @Override
    public List<OrderFormResult> findAllTranslated(Map<String, String[]> param)  {
        return translateResults(super.findAll(param));
    }

    @Override
    public OrderFormDTO createCartPreOrder(String cartId) {
        Cart cart = cartService.findOne(cartId);
        CartDTO myCartDTO = cartDTO.convertFor(cart);
        OrderFormDTO orderFormDTO = new OrderFormDTO();
        List<CartItemDTO> cartItemDTOS = myCartDTO.getItems();
        List<OrderItemDTO> orderItemDTOS = cartItemDTOS
                .stream()
                .filter(CartItemDTO::getChecked)
                .map(cartItemDTO -> new OrderItemDTO()
                        .setCoverImageUrl(cartItemDTO.getCoverImageUrl())
                        .setProductStandardNames(cartItemDTO.getProductStandardNames())
                        .setCount(Double.valueOf(cartItemDTO.getCount()))
                        .setPrice(cartItemDTO.getPrice())
                        .setProductId(cartItemDTO.getProductId())
                        .setSkuId(cartItemDTO.getSkuId()))
                .collect(Collectors.toList());
        orderFormDTO.setItems(orderItemDTOS);
        orderFormDTO.setTotal(this.calculateTotalPrice(orderFormDTO));
        return orderFormDTO;
    }

    @Override
    public OrderForm makeOrder(OrderFormDTO orderFormDTO) {
        OrderForm orderForm = orderFormDTO.convert();
        orderForm.setOrderNumber(getOutTradeNo());
        orderForm.setPaymentStatus(OrderForm.PaymentStatus.PAYED);
        orderFormRepository.save(orderForm);
        orderFormDTO.setId(orderForm.getId());
        CascadePersistHelper.saveChildren(orderFormDTO);
        // 修改账户余额
        if(OrderForm.OrderStatus.PAYED == orderForm.getStatus()) {
            orderForm.setCash(this.calculateDiscountedPrice(orderFormDTO, this.calculateTotalPrice(orderFormDTO)));
            consumeModifyMemberAccount(orderForm);
        }
        return orderForm;
    }

    @Override
    public OrderFormDTO createOneProductPreOrder(PreOrderParam param) {
        OrderFormDTO orderFormDTO = new OrderFormDTO();
        List<OrderItemDTO> orderItemDTOS;
        if(StringUtils.isBlank(param.getProductId())) {
            throw new BusinessException("商品id不能为空");
        }
        Product product = productRepository.findOne(param.getProductId());
        String coverImageUrl = null;
        if(product == null) {
            throw new BusinessException("商品未找到");
        }
        if(product.getLogicallyDeleted()) {
            throw new BusinessException("商品已下架");
        }
        if(param.getCount() == null || param.getCount() <= 0) {
            throw new BusinessException("请提供商品数量");
        }
        if(product.getCoverImage() != null)
            coverImageUrl = product.getCoverImage().getId();
        Sku sku;
        if(product.getSkus() != null && !product.getSkus().isEmpty() && StringUtils.isBlank(param.getSkuId())) {
            throw new BusinessException("请提供sku");
        } else {
            sku = skuRepository.findOne(param.getSkuId());
            if(sku == null) {
                throw new BusinessException("sku未找到");
            }
            if(sku.getCoverImage() != null) {
                coverImageUrl = sku.getCoverImage().getId();
            }
        }
        orderItemDTOS = new ArrayList<>();
        OrderItemDTO orderItemDTO = new OrderItemDTO()
                .setCoverImageUrl(coverImageUrl)
                .setProductName(product.getName())
                .setCount(Double.valueOf(param.getCount()))
                .setProductId(param.getProductId())
                .setSkuId(param.getSkuId());

        if(sku != null) {
            orderItemDTO.setProductStandardNames(String.join(",", sku
                    .getProductStandardItems()
                    .stream()
                    .map(ProductStandardItem::getName).collect(Collectors.toList())));
        }

        orderItemDTOS.add(orderItemDTO);
        orderFormDTO.setItems(orderItemDTOS);

        orderFormDTO.setTotal(this.calculateTotalPrice(orderFormDTO));

        return orderFormDTO;
    }

    @Override
    public Double calculateTotalPrice(OrderFormDTO orderFormDTO) {
        if(orderFormDTO.getItems() == null || orderFormDTO.getItems().isEmpty()) {
            throw new BusinessException("商品不能为空");
        }

        Double total = getProductsTotalPrice(orderFormDTO.getItems());
        total = calculateDiscountedPrice(orderFormDTO, total);
        return total;
    }

    private Double getProductsTotalPrice(List<OrderItemDTO> items) {
        return items.stream().map(item -> {
            Double price;
            if(StringUtils.isBlank(item.getProductId())) {
                throw new BusinessException("商品id为空");
            }
            Product product = productRepository.findOne(item.getProductId());
            if(product == null) {
                throw new BusinessException("商品未找到");
            }
            if(product.getLogicallyDeleted()) {
                throw new BusinessException("商品已下架");
            }
            if(item.getCount() == null || item.getCount() <= 0) {
                throw new BusinessException("请提供商品数量");
            }
            Sku sku = null;
            if(product.getSkus() != null && !product.getSkus().isEmpty() && StringUtils.isBlank(item.getSkuId())) {
                throw new BusinessException("请提供sku");
            } else if(StringUtils.isNotBlank(item.getSkuId())) {
                sku = skuRepository.findOne(item.getSkuId());
                if(sku == null) {
                    throw new BusinessException("sku未找到");
                }
            }
            if(sku != null) {
                price = sku.getPrice();
            } else {
                price = product.getPrice();
            }
            return new BigDecimal(item.getCount()).multiply(new BigDecimal(price));
        }).reduce(new BigDecimal(0), BigDecimal::add).doubleValue();
    }

    private Double calculateDiscountedPrice(OrderFormDTO orderFormDTO, Double totalPrice) {
        if(StringUtils.isNotBlank(orderFormDTO.getCouponId())) {
            Coupon coupon = couponService.findOne(orderFormDTO.getCouponId());
            if(coupon == null) {
                throw new BusinessException("优惠券未找到或已经失效");
            }
            long now = new Date().getTime();
            if(coupon.getStartDate() > now) {
                throw new BusinessException("优惠券活动未开始");
            }
            if(now > coupon.getEndDate()) {
                throw new BusinessException("优惠券活动已结束");
            }
            if(coupon.getMinAmount() > totalPrice) {
                throw new BusinessException("订单金额不符合条件");
            }
            totalPrice = totalPrice - coupon.getAmount();
        }

        if(orderFormDTO.getBalance() != null) {
            totalPrice = totalPrice - orderFormDTO.getBalance();
        }

        if(orderFormDTO.getPoint() != null) {
            totalPrice = totalPrice - (orderFormDTO.getPoint() * 100);
        }

        return totalPrice;
    }

    private List<OrderFormResult> translateResults(List<OrderForm> orderForms)  {
        List<OrderFormResult> orderFormResults = new ArrayList<>();
        for (OrderForm orderForm : orderForms) {
            orderFormResults.add(this.translateResult(orderForm));
        }
        return orderFormResults;
    }

    private OrderFormResult translateResult(OrderForm orderForm)  {
        OrderFormResult orderFormResult = new OrderFormResult();
        BeanUtils.copyProperties(orderForm, orderFormResult);
        if(StringUtils.isNotBlank(orderForm.getMemberCardId())) {
            orderFormResult.setMemberCard(memberCardService.findOne(orderForm.getMemberCardId()));
        }
        return orderFormResult;
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
     * @ {@link com.kratos.exceptions.BusinessException}逻辑异常
     */
    private void validAccount(OrderForm orderForm)  {
        if(orderForm.getCoupon() == null || StringUtils.isBlank(orderForm.getCoupon().getId())) {
            orderForm.setCoupon(null);
        }
    }

    /**
     * 记录消费记录
     * @param member 会员
     * @param items 消费项
     */
    private void recordConsume(Member member, Double cash, Double balance, Integer point, Double discount, List<OrderItem> items)  {
        OperationRecord rechargeRecord = new OperationRecord();
        rechargeRecord.setMemberId(member.getId());
        rechargeRecord.setBusinessType(OperationRecord.BusinessType.CONSUME.name());
        rechargeRecord.setClientId(MemberThread.getInstance().getClientId());
        rechargeRecord.setIpAddress(MemberThread.getInstance().getIpAddress());
        StringBuilder content = new StringBuilder();
        content.append(String.format("现金消费 %s 元，余额消费 %s 元，积分消费 %s 分，折扣 %s", cash, balance, point, discount));
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

    /**
     * 记录退款记录
     * @param member 会员
     * @param orderForm 订单实体
     */
    private void recordReject(Member member, Double cash, Double balance, Integer point, OrderForm orderForm)  {
        OperationRecord rechargeRecord = new OperationRecord();
        rechargeRecord.setMemberId(member.getId());
        rechargeRecord.setBusinessType(OperationRecord.BusinessType.REJECT.name());
        rechargeRecord.setClientId(MemberThread.getInstance().getClientId());
        rechargeRecord.setIpAddress(MemberThread.getInstance().getIpAddress());
        String content = String.format("现金退款 %s 元，余额退款 %s 元，积分退款 %s 分", cash, balance, point)
                + String.format("  订单号：%s" , orderForm.getOrderNumber());
        rechargeRecord.setContent(content);
        operationRecordService.save(rechargeRecord);
    }

    private Double subtractMoney(Double sourceMoney, Double money) {
        if(sourceMoney == null) {
            sourceMoney = 0D;
        }
        BigDecimal sp = new BigDecimal(sourceMoney);
        return sp.subtract(new BigDecimal(money)).doubleValue();
    }

    private Double increaseMoney(Double sourceMoney, Double money) {
        if(sourceMoney == null) {
            sourceMoney = 0D;
        }
        BigDecimal sp = new BigDecimal(sourceMoney);
        return sp.add(new BigDecimal(money)).doubleValue();
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
            ProductRepository productRepository,
            CouponService couponService,
            MemberCardService memberCardService,
            SkuRepository skuRepository,
            CartService cartService,
            CartDTO cartDTO
    ) {
        this.orderFormRepository = orderFormRepository;
        this.memberService = memberService;
        this.operationRecordService = operationRecordService;
        this.productRepository = productRepository;
        this.couponService = couponService;
        this.memberCardService = memberCardService;
        this.skuRepository = skuRepository;
        this.cartService = cartService;
        this.cartDTO = cartDTO;
    }
}
