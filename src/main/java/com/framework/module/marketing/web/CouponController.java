package com.framework.module.marketing.web;

import com.framework.module.auth.MemberThread;
import com.framework.module.marketing.domain.Coupon;
import com.framework.module.marketing.service.CouponService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.auth.domain.Module;
import com.kratos.module.auth.service.OauthClientDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "优惠券管理")
@RestController
@RequestMapping("/api/coupon")
public class CouponController extends AbstractCrudController<Coupon> {
    private final CouponService couponService;
    private final OauthClientDetailsService oauthClientDetailsService;
    @Override
    protected CrudService<Coupon> getService() {
        return couponService;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Coupon> save(@RequestBody Coupon coupon) throws Exception {
        if(coupon.getClient() != null && StringUtils.isNotBlank(coupon.getClient().getClientId())) {
            coupon.setClient(oauthClientDetailsService.findOneByClientId(coupon.getClient().getClientId()));
        } else {
            coupon.setClient(null);
        }
        coupon = couponService.save(coupon);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    /**
     * 获取发放类型的优惠券
     */
    @ApiOperation(value="获取发放类型的优惠券")
    @RequestMapping(value = "/obtainLoginType", method = RequestMethod.GET)
    public ResponseEntity<List<Coupon>> obtainLoginType() throws Exception {
        List<Coupon> coupons = couponService.obtainLoginType(MemberThread.getInstance().get().getId());
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @Autowired
    public CouponController(
            CouponService couponService,
            OauthClientDetailsService oauthClientDetailsService
    ) {
        this.couponService = couponService;
        this.oauthClientDetailsService = oauthClientDetailsService;
    }
}
