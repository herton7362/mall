package com.framework.module.sheetmetalpaint.service;

import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberService;
import com.framework.module.orderform.service.OrderFormService;
import com.framework.module.orderform.service.OrderFormServiceImpl;
import com.framework.module.sheetmetalpaint.domain.PaintOrderForm;
import com.framework.module.sheetmetalpaint.domain.PaintOrderFormRepository;
import com.framework.module.sheetmetalpaint.domain.PaintOrderItem;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.service.ShopService;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@Transactional
public class PaintOrderFormServiceImpl extends AbstractCrudService<PaintOrderForm> implements PaintOrderFormService {
    private PaintOrderFormRepository paintOrderFormRepository;
    private OrderFormService orderFormService;
    private MemberService memberService;
    private ShopService shopService;
    @Override
    protected PageRepository<PaintOrderForm> getRepository() {
        return paintOrderFormRepository;
    }

    @Override
    public PaintOrderForm makeOrder(PaintOrderForm paintOrderForm) throws Exception {
        Member member = paintOrderForm.getMember();
        if(member == null) {
            throw new BusinessException("下单账户未找到");
        }
        if(paintOrderForm.getShop() == null) {
            throw new BusinessException("请选择店铺");
        }
        Shop shop = shopService.findOne(paintOrderForm.getShop().getId());
        if(shop == null) {
            throw new BusinessException("店铺不存在");
        }
        paintOrderForm.setOrderNumber(orderFormService.getOutTradeNo());
        paintOrderForm.getItems().forEach(new ItemSetter(paintOrderForm));
        orderFormService.validAccount(paintOrderForm);
        if(PaintOrderForm.OrderStatus.PAYED == paintOrderForm.getStatus()) {
            paintOrderForm.setPaymentStatus(PaintOrderForm.PaymentStatus.PAYED);
        } else if(PaintOrderForm.OrderStatus.UN_PAY == paintOrderForm.getStatus()) {
            paintOrderForm.setPaymentStatus(PaintOrderForm.PaymentStatus.UN_PAY);
        }
        List<PaintOrderItem> items = new ArrayList<>(paintOrderForm.getItems());
        paintOrderForm.getItems().clear();
        final PaintOrderForm newOrderForm = paintOrderFormRepository.save(paintOrderForm);
        items.forEach(newOrderForm :: addItem);
        // 修改账户余额
        if(PaintOrderForm.OrderStatus.PAYED == paintOrderForm.getStatus()) {
            memberService.consumeModifyMemberAccount(paintOrderForm);
        }
        return paintOrderForm;
    }

    /**
     * 设置item的默认值
     */
    class ItemSetter implements Consumer<PaintOrderItem> {
        private PaintOrderForm orderForm;
        ItemSetter(PaintOrderForm orderForm) {
            this.orderForm = orderForm;
        }
        @Override
        public void accept(PaintOrderItem item) {
            item.setOrderForm(orderForm);
            item.setPoint(0);
            item.setPrice(item.getPaint().getPrice());
        }
    }

    @Autowired
    public PaintOrderFormServiceImpl(
            PaintOrderFormRepository paintOrderFormRepository,
            OrderFormService orderFormService,
            MemberService memberService,
            ShopService shopService
    ) {
        this.paintOrderFormRepository = paintOrderFormRepository;
        this.orderFormService = orderFormService;
        this.memberService = memberService;
        this.shopService = shopService;
    }
}
