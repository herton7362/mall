package com.framework.module.auth;

import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractLoginController;
import com.kratos.common.AbstractLoginService;
import com.kratos.entity.BaseUser;
import com.kratos.module.auth.UserThread;
import com.kratos.module.auth.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("登录相关")
@RestController
public class LoginController extends AbstractLoginController {
    private final MemberService memberService;
    private final AdminService adminService;
    /**
     * 查询登录用户
     */
    @ApiOperation(value="查询登录用户")
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public ResponseEntity<BaseUser> getOne() throws Exception {
        BaseUser user = UserThread.getInstance().get();
        BaseUser newUser = memberService.findOne(user.getId());
        if(newUser == null) {
            newUser = adminService.findOne(user.getId());
        }
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
    @Autowired
    public LoginController(
            AbstractLoginService loginService,
            MemberService memberService,
            AdminService adminService
    ) {
        super(loginService);
        this.memberService = memberService;
        this.adminService = adminService;
    }
}
