package com.framework.module.member.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.config.OAuth2Properties;
import com.framework.module.member.domain.Member;
import com.framework.module.member.web.DeductBalanceParam;
import com.framework.module.member.web.FastIncreasePointParam;
import com.framework.module.recharge.web.RechargeParam;
import com.kratos.common.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Component("memberService")
public class ClientMemberServiceImpl extends MemberServiceImpl implements MemberService {
    private final RestTemplate restTemplate;
    private final OAuth2Properties oAuth2Properties;
    private final OAuth2RestTemplate oAuth2RestTemplate;

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
    @SuppressWarnings("unchecked")
    public PageResult<Member> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        Map<String, String> newParam = new HashMap<>();
        newParam.put("currentPage", String.valueOf(pageRequest.getPageNumber()));
        newParam.put("pageSize", String.valueOf(pageRequest.getPageSize()));
        List<String> params = new ArrayList<>();
        List<String> sorts = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        pageRequest.getSort().forEach(sort->{
            sorts.add(sort.getProperty());
            orders.add(sort.getDirection().name().toLowerCase());
        });
        newParam.put("sort", String.join(",", sorts));
        newParam.put("order", String.join(",", orders));
        param.forEach((k, v) -> newParam.put(k, v[0]));
        newParam.forEach((k, v)->params.add(k + "=" + v));
        ResponseEntity<PageResult> responseEntity = oAuth2RestTemplate.getForEntity(
                oAuth2Properties.getFindAllMemberUrl() + "?" + String.join("&", params),  PageResult.class);
        return responseEntity.getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Member> findAll(Map<String, String[]> param) throws Exception {
        Map<String, String> newParam = new HashMap<>();
        List<String> params = new ArrayList<>();
        param.forEach((k, v) -> newParam.put(k, v[0]));
        newParam.forEach((k, v)->params.add(k + "=" + v));
        ResponseEntity<List> responseEntity = oAuth2RestTemplate.getForEntity(
                oAuth2Properties.getFindAllMemberUrl(),  List.class, newParam);
        return responseEntity.getBody();
    }

    @Override
    public Member findOne(String id) throws Exception {
        ResponseEntity<Member> responseEntity = oAuth2RestTemplate.getForEntity(
                URI.create(String.format(oAuth2Properties.getFindMemberUrl(), id)), Member.class);
        return responseEntity.getBody();
    }

    @Override
    public void delete(String id) throws Exception {
        restTemplate.delete(
                URI.create(String.format(oAuth2Properties.getFindMemberUrl(), id)));
    }

    @Override
    public Member save(Member member) throws Exception {
        ResponseEntity<Member> responseEntity = oAuth2RestTemplate.postForEntity(
                URI.create(oAuth2Properties.getSaveMemberUrl()), member, Member.class);
        return responseEntity.getBody();
    }

    @Autowired
    public ClientMemberServiceImpl(
            OAuth2Properties oAuth2Properties,
            RestTemplateBuilder restTemplateBuilder,
            OAuth2RestTemplate oAuth2RestTemplate
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.oAuth2Properties = oAuth2Properties;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }
}
