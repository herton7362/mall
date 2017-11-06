package com.framework.module.member.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.framework.module.marketing.domain.Coupon;
import com.kratos.module.auth.domain.OauthClientDetails;

public class CouponResult extends Coupon {
    @JsonIgnore
    private OauthClientDetails client;

    @Override
    public OauthClientDetails getClient() {
        return client;
    }

    @Override
    public void setClient(OauthClientDetails client) {
        this.client = client;
    }
}
