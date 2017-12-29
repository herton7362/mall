package com.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="oauth2.framework")
public class OAuth2Properties {
    private String accessTokenUri;
    private String userAuthorizationUri;
    private String findMemberByMobileUrl; // 根据手机号获取会员
    private String findMemberByCardNoUrl; // 根据卡号获取会员
    private String fastIncreasePointUrl; // 快速积分消费
    private String deductBalanceUrl; // 扣除余额
    private String getCouponsUrl; // 获取优惠券
    private String getMemberCountUrl;// 获取会员数量
    private String rechargeUrl; // 充值
    private String memberDomainUri; // 会员
    private String couponDomainUri; // 优惠券
    private String getUnClaimedUrl; // 获取未获取的优惠券
    private String getAvailableCouponCountUrl; // 获取可用优惠券
    private String getMemberCouponsUrl; // 获取当前用户优惠券
    private String claimCouponUrl; // 领取优惠券
    private String useCouponUrl; // 使用优惠券

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getUserAuthorizationUri() {
        return userAuthorizationUri;
    }

    public void setUserAuthorizationUri(String userAuthorizationUri) {
        this.userAuthorizationUri = userAuthorizationUri;
    }

    public String getFindMemberByMobileUrl() {
        return findMemberByMobileUrl;
    }

    public void setFindMemberByMobileUrl(String findMemberByMobileUrl) {
        this.findMemberByMobileUrl = findMemberByMobileUrl;
    }

    public String getFindMemberByCardNoUrl() {
        return findMemberByCardNoUrl;
    }

    public void setFindMemberByCardNoUrl(String findMemberByCardNoUrl) {
        this.findMemberByCardNoUrl = findMemberByCardNoUrl;
    }

    public String getFastIncreasePointUrl() {
        return fastIncreasePointUrl;
    }

    public void setFastIncreasePointUrl(String fastIncreasePointUrl) {
        this.fastIncreasePointUrl = fastIncreasePointUrl;
    }

    public String getDeductBalanceUrl() {
        return deductBalanceUrl;
    }

    public void setDeductBalanceUrl(String deductBalanceUrl) {
        this.deductBalanceUrl = deductBalanceUrl;
    }

    public String getGetCouponsUrl() {
        return getCouponsUrl;
    }

    public void setGetCouponsUrl(String getCouponsUrl) {
        this.getCouponsUrl = getCouponsUrl;
    }

    public String getGetMemberCountUrl() {
        return getMemberCountUrl;
    }

    public void setGetMemberCountUrl(String getMemberCountUrl) {
        this.getMemberCountUrl = getMemberCountUrl;
    }

    public String getRechargeUrl() {
        return rechargeUrl;
    }

    public void setRechargeUrl(String rechargeUrl) {
        this.rechargeUrl = rechargeUrl;
    }

    public String getGetAvailableCouponCountUrl() {
        return getAvailableCouponCountUrl;
    }

    public void setGetAvailableCouponCountUrl(String getAvailableCouponCountUrl) {
        this.getAvailableCouponCountUrl = getAvailableCouponCountUrl;
    }

    public String getMemberDomainUri() {
        return memberDomainUri;
    }

    public void setMemberDomainUri(String memberDomainUri) {
        this.memberDomainUri = memberDomainUri;
    }

    public String getCouponDomainUri() {
        return couponDomainUri;
    }

    public void setCouponDomainUri(String couponDomainUri) {
        this.couponDomainUri = couponDomainUri;
    }

    public String getGetUnClaimedUrl() {
        return getUnClaimedUrl;
    }

    public void setGetUnClaimedUrl(String getUnClaimedUrl) {
        this.getUnClaimedUrl = getUnClaimedUrl;
    }

    public String getGetMemberCouponsUrl() {
        return getMemberCouponsUrl;
    }

    public void setGetMemberCouponsUrl(String getMemberCouponsUrl) {
        this.getMemberCouponsUrl = getMemberCouponsUrl;
    }

    public String getClaimCouponUrl() {
        return claimCouponUrl;
    }

    public void setClaimCouponUrl(String claimCouponUrl) {
        this.claimCouponUrl = claimCouponUrl;
    }

    public String getUseCouponUrl() {
        return useCouponUrl;
    }

    public void setUseCouponUrl(String useCouponUrl) {
        this.useCouponUrl = useCouponUrl;
    }
}
