package com.framework.module.auth;

import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractLoginService;
import com.kratos.entity.BaseUser;
import com.kratos.module.auth.JdbcUserDetailService;
import com.kratos.module.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ExtendedJdbcUserDetailService extends JdbcUserDetailService implements UserDetailsService {
    private final MemberService memberService;

    @Autowired
    public ExtendedJdbcUserDetailService(
            UserService adminService,
            MemberService memberService
    ) {
        super(adminService);
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = super.loadUserByUsername(username);
        if(userDetails == null) {
            try {
                BaseUser user = memberService.findOneByLoginName(username);
                return new User(username, user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getUserType())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userDetails;
    }
}
