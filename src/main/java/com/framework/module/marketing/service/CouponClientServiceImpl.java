package com.framework.module.marketing.service;

import com.framework.config.OAuth2Properties;
import com.framework.module.auth.MemberThread;
import com.framework.module.marketing.domain.Coupon;
import com.framework.module.marketing.domain.CouponRepository;
import com.framework.module.marketing.web.UseCouponParam;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberCoupon;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractCrudClientService;
import com.kratos.common.PageRepository;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.UserThread;
import com.kratos.module.auth.domain.OauthClientDetails;
import com.kratos.module.auth.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("couponService")
@Transactional
public class CouponClientServiceImpl extends AbstractCrudClientService<Coupon> implements CouponService {
    private final OAuth2Properties oAuth2Properties;
    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Override
    protected String getDomainUri() {
        return oAuth2Properties.getCouponDomainUri();
    }

    @Override
    protected OAuth2RestTemplate getOAuth2RestTemplate() {
        return oAuth2RestTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Coupon> getUnClaimed(String memberId) throws Exception {
        ResponseEntity<List> responseEntity = oAuth2RestTemplate.getForEntity(
                URI.create(oAuth2Properties.getGetUnClaimedUrl()), List.class);
        return responseEntity.getBody();
    }

    @Override
    public void validCouponUseAble(String couponId, Double amount) throws Exception {
        Coupon coupon = findOne(couponId);
        String clientId = UserThread.getInstance().getClientId();
        if(!clientId.equals(coupon.getClient().getClientId())) {
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
    public Double useCoupon(final String couponId, String memberId, Double amount) throws Exception {
        UseCouponParam useCouponParam = new UseCouponParam();
        useCouponParam.setCouponId(couponId);
        useCouponParam.setMemberId(memberId);
        useCouponParam.setAmount(amount);
        ResponseEntity<Double> responseEntity = oAuth2RestTemplate.postForEntity(
                URI.create(oAuth2Properties.getUseCouponUrl()), useCouponParam, Double.class);
        return responseEntity.getBody();
    }

    @Override
    public void claim(String memberId, Coupon coupon) throws Exception {
        oAuth2RestTemplate.postForEntity(
                URI.create(oAuth2Properties.getClaimCouponUrl()), coupon, null);
    }

    @Autowired
    public CouponClientServiceImpl(
            OAuth2Properties oAuth2Properties,
            OAuth2RestTemplate oAuth2RestTemplate
    ) {
        this.oAuth2Properties = oAuth2Properties;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }
}
