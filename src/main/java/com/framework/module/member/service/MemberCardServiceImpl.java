package com.framework.module.member.service;

import com.framework.config.OAuth2Properties;
import com.framework.module.member.domain.MemberCard;
import com.kratos.common.AbstractCrudClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component("memberCardService")
public class MemberCardServiceImpl extends AbstractCrudClientService<MemberCard> implements MemberCardService {
    private final OAuth2Properties oAuth2Properties;
    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Override
    protected String getDomainUri() {
        return oAuth2Properties.getMemberCardDomainUri();
    }

    @Override
    protected OAuth2RestTemplate getOAuth2RestTemplate() {
        return oAuth2RestTemplate;
    }

    @Autowired
    public MemberCardServiceImpl(
            OAuth2Properties oAuth2Properties,
            OAuth2RestTemplate oAuth2RestTemplate
    ) {
        this.oAuth2Properties = oAuth2Properties;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }
}
