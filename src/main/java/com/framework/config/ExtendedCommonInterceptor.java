package com.framework.config;

import com.framework.module.member.domain.MemberRepository;
import com.kratos.common.utils.NetworkUtils;
import com.kratos.common.utils.SpringUtils;
import com.kratos.common.utils.StringUtils;
import com.kratos.config.CommonInterceptor;
import com.kratos.entity.BaseUser;
import com.kratos.module.auth.UserThread;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtendedCommonInterceptor extends CommonInterceptor {
    private MemberRepository memberRepository;
    private TokenStore tokenStore;
    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!super.preHandle(request, response, handler)) {
            return false;
        }
        if(UserThread.getInstance().get() == null) {
            memberRepository = SpringUtils.getBean(MemberRepository.class);
            tokenStore = SpringUtils.getBean(TokenStore.class);
            String accessToken = request.getParameter("access_token");
            UserThread.getInstance().setIpAddress(NetworkUtils.getIpAddress(request));
            if(StringUtils.isNotBlank(accessToken)) {
                OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
                if(oAuth2Authentication != null) {
                    User user = (User) oAuth2Authentication.getPrincipal();
                    UserThread.getInstance().setClientId(oAuth2Authentication.getOAuth2Request().getClientId());
                    BaseUser baseUser = memberRepository.findOneByLoginName(user.getUsername());
                    if(baseUser != null) {
                        baseUser.setPassword(null);
                        UserThread.getInstance().set(baseUser);
                    }
                }
            }
        }
        return true;
    }
}
