package com.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="oauth2.framework")
public class OAuth2Properties {
    private String accessTokenUri;
    private String userAuthorizationUri;
    private String findMemberUrl;
    private String findAllMemberUrl;
    private String saveMemberUrl;
    private String findMemberByMobileUrl;
    private String findMemberByCardNoUrl;
    private String fastIncreasePointUrl;
    private String deductBalanceUrl;
    private String getCouponsUrl;
    private String getMemberCountUrl;
    private String rechargeUrl;
    private String deleteMemberUrl;

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

    public String getFindMemberUrl() {
        return findMemberUrl;
    }

    public void setFindMemberUrl(String findMemberUrl) {
        this.findMemberUrl = findMemberUrl;
    }

    public String getFindAllMemberUrl() {
        return findAllMemberUrl;
    }

    public void setFindAllMemberUrl(String findAllMemberUrl) {
        this.findAllMemberUrl = findAllMemberUrl;
    }

    public String getSaveMemberUrl() {
        return saveMemberUrl;
    }

    public void setSaveMemberUrl(String saveMemberUrl) {
        this.saveMemberUrl = saveMemberUrl;
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

    public String getDeleteMemberUrl() {
        return deleteMemberUrl;
    }

    public void setDeleteMemberUrl(String deleteMemberUrl) {
        this.deleteMemberUrl = deleteMemberUrl;
    }
}
