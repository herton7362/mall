package com.framework.module.marketing.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.marketing.domain.Coupon;
import com.framework.module.marketing.domain.CouponRepository;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberCoupon;
import com.framework.module.member.service.MemberCouponService;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.UserThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class CouponServiceImpl extends AbstractCrudService<Coupon> implements CouponService {
    private final CouponRepository couponRepository;
    private final MemberService memberService;
    private final MemberCouponService memberCouponService;
    @Override
    protected PageRepository<Coupon> getRepository() {
        return couponRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        if(coupon.getClientId() == null) {
            coupon.setClientId(MemberThread.getInstance().getClientId());
        }
        if(coupon.getObtainType() == null) {
            coupon.setObtainType(Coupon.ObtainType.LOGIN);
        }
        return super.save(coupon);
    }

    @Override
    public List<Coupon> getUnClaimed(String memberId) {
        Map<String, String[]> params = new HashMap<>();
        params.put("memberId", new String[]{memberId});
        List<MemberCoupon> coupons = memberCouponService.findAll(params);
        final String clientId = MemberThread.getInstance().getClientId();
        // 匹配规则，查询活动期间内，当前登录系统，用户没有获取过的，有效的优惠券
        List<Coupon> newCoupons = couponRepository.findAll((Root<Coupon> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            predicate.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), new Date().getTime()));
            predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), new Date().getTime()));
            predicate.add(criteriaBuilder.equal(root.get("logicallyDeleted"), false));
            predicate.add(criteriaBuilder.equal(root.get("clientId"), clientId));
            return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
        });

        newCoupons = newCoupons
                .stream()
                .filter(coupon -> {
                    if(coupons != null) {
                        for (MemberCoupon memberCoupon : coupons) {
                            if(memberCoupon.getCoupon().getId().equals(coupon.getId())) {
                                return false;
                            }
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
        return newCoupons;
    }

    @Override
    public void validCouponUseAble(String couponId, Double amount) {
        Coupon coupon = couponRepository.findOne(couponId);
        String clientId = UserThread.getInstance().getClientId();
        if(!clientId.equals(coupon.getClientId())) {
            throw new BusinessException("优惠券不属于当前系统");
        }
        // 如果优惠券策略是满减
        if(coupon.getMarketingType() == Coupon.MarketingType.CASH_OFF) {
            if(coupon.getMinAmount() > amount) {
                throw new BusinessException("当前优惠券不可用");
            }
        }
    }

    @Override
    public Double useCoupon(final String couponId, String memberId, Double amount) {
        validCouponUseAble(couponId, amount);
        Coupon coupon = couponRepository.findOne(couponId);
        BigDecimal newAmount = new BigDecimal(amount).subtract(new BigDecimal(coupon.getAmount()));
        Map<String, String[]> params = new HashMap<>();
        params.put("memberId", new String[]{memberId});
        List<MemberCoupon> coupons = memberCouponService.findAll(params);
        coupons.forEach(memberCoupon -> {
            if(memberCoupon.getCoupon().getId().equals(couponId)) {
                memberCoupon.setUsed(true);
            }
        });
        return newAmount.doubleValue();
    }

    @Override
    public void claim(String memberId, Coupon coupon) {
        Member member = memberService.findOne(memberId);
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setUsed(false);
        memberCoupon.setMemberId(member.getId());
        memberCoupon.setCoupon(coupon);
        memberCouponService.save(memberCoupon);
    }

    @Autowired
    public CouponServiceImpl(
            CouponRepository couponRepository,
            MemberService memberService,
            MemberCouponService memberCouponService
    ) {
        this.couponRepository = couponRepository;
        this.memberService = memberService;
        this.memberCouponService = memberCouponService;
    }
}
