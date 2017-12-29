package com.framework.module.member.service;

import com.framework.config.OAuth2Properties;
import com.framework.module.member.domain.Member;
import com.framework.module.member.web.DeductBalanceParam;
import com.framework.module.member.web.FastIncreasePointParam;
import com.framework.module.orderform.base.BaseOrderForm;
import com.framework.module.recharge.web.RechargeParam;
import com.kratos.common.AbstractCrudClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component("memberService")
public class MemberClientServiceImpl extends AbstractCrudClientService<Member> implements MemberService {
    private final RestTemplate restTemplate;
    private final OAuth2Properties oAuth2Properties;
    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Override
    protected String getDomainUri() {
        return oAuth2Properties.getMemberDomainUri();
    }

    @Override
    protected OAuth2RestTemplate getOAuth2RestTemplate() {
        return oAuth2RestTemplate;
    }

    @Override
    public Member findOneByLoginName(String loginName) {
        ResponseEntity<Member> responseEntity = restTemplate.getForEntity(
                URI.create(String.format(oAuth2Properties.getFindMemberByMobileUrl(), loginName)), Member.class);
        return responseEntity.getBody();
    }

    @Override
    public Member findOneByCardNo(String cardNo) {
        ResponseEntity<Member> responseEntity = oAuth2RestTemplate.getForEntity(
                URI.create(String.format(oAuth2Properties.getFindMemberByCardNoUrl(), cardNo)), Member.class);
        return responseEntity.getBody();
    }

    @Override
    public void fastIncreasePoint(String id, Integer point) throws Exception {
        FastIncreasePointParam fastIncreasePointParam = new FastIncreasePointParam();
        fastIncreasePointParam.setMemberId(id);
        fastIncreasePointParam.setPoint(point);
        oAuth2RestTemplate.postForEntity(
                URI.create(oAuth2Properties.getFastIncreasePointUrl()), fastIncreasePointParam, null);
    }

    @Override
    public void increaseBalance(String id, Double balance) throws Exception {
        RechargeParam rechargeParam = new RechargeParam();
        rechargeParam.setMemberId(id);
        rechargeParam.setAmount(balance);
        oAuth2RestTemplate.postForEntity(
                URI.create(oAuth2Properties.getRechargeUrl()), rechargeParam, null);
    }

    @Override
    public void deductBalance(String memberId, Double amount) throws Exception {
        DeductBalanceParam deductBalanceParam = new DeductBalanceParam();
        deductBalanceParam.setMemberId(memberId);
        deductBalanceParam.setAmount(amount);
        oAuth2RestTemplate.postForEntity(
                URI.create(oAuth2Properties.getDeductBalanceUrl()), deductBalanceParam, null);
    }

    @Override
    public Long count() {
        ResponseEntity<Long> responseEntity = oAuth2RestTemplate.getForEntity(
                URI.create(oAuth2Properties.getGetMemberCountUrl()), Long.class);
        return responseEntity.getBody();
    }

    @Override
    public void consumeModifyMemberAccount(BaseOrderForm orderForm) throws Exception {
        MemberServiceImpl memberService = new MemberServiceImpl(null, null,null);
        Member member = memberService.calculateMemberAccountAfterConsume(orderForm);
        save(member);
    }

    @Override
    public Integer getAvailableCouponCount(String memberId) throws Exception {
        ResponseEntity<Integer> responseEntity = oAuth2RestTemplate.getForEntity(
                String.format(oAuth2Properties.getGetAvailableCouponCountUrl(), memberId),  Integer.class);
        return responseEntity.getBody();
    }

    @Autowired
    public MemberClientServiceImpl(
            OAuth2Properties oAuth2Properties,
            RestTemplateBuilder restTemplateBuilder,
            OAuth2RestTemplate oAuth2RestTemplate
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.oAuth2Properties = oAuth2Properties;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }
}
