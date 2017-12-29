package com.framework.module.auth;

import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractLoginService;
import com.kratos.common.utils.CacheUtils;
import com.kratos.entity.BaseUser;
import com.kratos.exceptions.BusinessException;
import com.kratos.kits.Kits;
import com.kratos.kits.notification.Notification;
import com.kratos.module.auth.UserThread;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class LoginServiceImpl extends AbstractLoginService {
    private final Kits kits;
    private final TokenEndpoint tokenEndpoint;
    private final MemberService memberService;

    @Override
    protected Notification getNotification() {
        return kits.notification();
    }

    @Override
    protected TokenEndpoint getTokenEndpoint() {
        return tokenEndpoint;
    }

    @Override
    public void editPwd(String mobile, String code, String password) throws Exception {
        if(!verifyVerifyCode(mobile, code)) {
            throw new BusinessException(String.format("验证码%s不正确", code));
        }
        Member member = memberService.findOneByLoginName(mobile);
        if(member == null) {
            throw new BusinessException("当前号码未注册");
        }
        member.setPassword(new BCryptPasswordEncoder().encode(password));
        memberService.save(member);
    }

    @Override
    public void register(String mobile, String code, String password) throws Exception {
        if(!verifyVerifyCode(mobile, code)) {
            throw new BusinessException(String.format("验证码%s不正确", code));
        }
        if(findUserByMobile(mobile) != null) {
            throw new BusinessException("该手机号已被注册，请选择找回密码或者直接登录");
        }
        if(StringUtils.isBlank(password)) {
            throw new BusinessException("密码不能为空");
        }
        if(StringUtils.isBlank(mobile)) {
            throw new BusinessException("电话不能为空");
        }
        if(password.length() < 6) {
            throw new BusinessException("密码最短为6位");
        }
        Member member = new Member();
        member.setMobile(mobile);
        member.setLoginName(mobile);
        member.setPassword(new BCryptPasswordEncoder().encode(password));
        memberService.save(member);
        clearVerifyCode(mobile);
    }

    @Override
    public BaseUser findUserByMobile(String mobile) throws Exception {
        return memberService.findOneByLoginName(mobile);
    }

    @Autowired
    public LoginServiceImpl(
            Kits kits,
            TokenEndpoint tokenEndpoint,
            MemberService memberService
    ) {
        this.kits = kits;
        this.tokenEndpoint = tokenEndpoint;
        this.memberService = memberService;
    }
}
