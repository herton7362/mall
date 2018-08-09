package com.framework.module.member.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.marketing.dto.CouponDTO;
import com.framework.module.marketing.web.GetAvailableCouponsParam;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberCoupon;
import com.framework.module.member.domain.MemberCouponRepository;
import com.framework.module.orderform.domain.CartItem;
import com.framework.module.orderform.service.CartItemService;
import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.Sku;
import com.framework.module.product.service.ProductService;
import com.framework.module.product.service.SkuService;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.exceptions.BusinessException;
import com.kratos.exceptions.UnauthorizedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class MemberCouponServiceImpl extends AbstractCrudService<MemberCoupon> implements MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    private final ProductService productService;
    private final SkuService skuService;
    private final CouponDTO couponDTO;
    private final CartItemService cartItemService;
    @Override
    protected PageRepository<MemberCoupon> getRepository() {
        return memberCouponRepository;
    }

    @Override
    public List<CouponDTO> getAvailableCoupons(GetAvailableCouponsParam param) throws Exception {
        if(StringUtils.isBlank(param.getProductId())
                && StringUtils.isBlank(param.getSkuId())
                && param.getCartItemIds() == null) {
            throw new BusinessException("参数不能为空");
        }
        List<MemberCoupon> memberCoupons = new ArrayList<>();
        if(StringUtils.isNotBlank(param.getProductId())) {
            Product product = productService.findOne(param.getProductId());
            if(product == null) {
                throw new BusinessException("商品未找到");
            }
            if(product.getSkus() != null && !product.getSkus().isEmpty() && StringUtils.isBlank(param.getSkuId())) {
                throw new BusinessException("未指定具体商品规格");
            }
            if(param.getCount() == null || param.getCount() <= 0) {
                throw new BusinessException("请指定商品数量");
            }
            Sku sku = null;
            if(StringUtils.isNotBlank(param.getSkuId())) {
                sku = skuService.findOne(param.getSkuId());
            }
            memberCoupons = getAvailableCouponsFromProduct(product, sku, param.getCount());
        }

        if(param.getCartItemIds() != null) {
            List<CartItem> cartItems = param.getCartItemIds().stream().map(id -> {
                try {
                    return cartItemService.findOne(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            memberCoupons = getAvailableCouponsFromCartItems(cartItems);
        }

        return memberCoupons
                .stream()
                .map(memberCoupon -> couponDTO.convertFor(memberCoupon.getCoupon()))
                .collect(Collectors.toList());
    }

    private List<MemberCoupon> getAvailableCouponsFromProduct(Product product, Sku sku, Integer count) throws Exception {
        Double price = new BigDecimal(product.getPrice()).multiply(new BigDecimal(count)).doubleValue();
        if(sku != null) {
            price = new BigDecimal(sku.getPrice()).multiply(new BigDecimal(count)).doubleValue();
        }
        return filterAvailableCoupons(findAllByLoggedMember(), price);
    }

    private List<MemberCoupon> getAvailableCouponsFromCartItems(List<CartItem> cartItems) throws Exception {
        Double price = cartItems
                .stream()
                .map(cartItem -> {
                    if(cartItem.getSku() == null) {
                        return new BigDecimal(cartItem.getProduct().getPrice())
                                .multiply(new BigDecimal(cartItem.getCount()));
                    } else {
                        return new BigDecimal(cartItem.getSku().getPrice())
                                .multiply(new BigDecimal(cartItem.getCount()));
                    }
                })
                .reduce(new BigDecimal(0D), BigDecimal::add).doubleValue();
        return filterAvailableCoupons(findAllByLoggedMember(), price);
    }

    private List<MemberCoupon> findAllByLoggedMember() throws Exception {
        Member member = MemberThread.getInstance().get();
        if(member == null) {
            throw new UnauthorizedException("您未登录");
        }

        Map<String, String[]> params = new HashMap<>();
        params.put("memberId", new String[]{member.getId()});
        return findAll(params);
    }

    private List<MemberCoupon> filterAvailableCoupons(List<MemberCoupon> memberCoupons, final Double price) {
        return memberCoupons
                .stream()
                .filter(memberCoupon -> !memberCoupon.getUsed()
                        && !memberCoupon.getCoupon().ifExpired() && memberCoupon.getCoupon().ifAvailable(price))
                .collect(Collectors.toList());
    }

    @Autowired
    public MemberCouponServiceImpl(
            MemberCouponRepository memberCouponRepository,
            ProductService productService,
            SkuService skuService,
            CouponDTO couponDTO,
            CartItemService cartItemService
    ) {
        this.memberCouponRepository = memberCouponRepository;
        this.productService = productService;
        this.skuService = skuService;
        this.couponDTO = couponDTO;
        this.cartItemService = cartItemService;
    }
}
