package com.framework.module.marketing.web;

import com.framework.module.auth.MemberThread;
import com.framework.module.marketing.domain.Coupon;
import com.framework.module.marketing.dto.CouponDTO;
import com.framework.module.marketing.service.CouponService;
import com.framework.module.member.domain.MemberCoupon;
import com.framework.module.member.service.MemberCouponService;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "优惠券管理")
@RestController
@RequestMapping("/api/coupon")
public class CouponController extends AbstractCrudController<Coupon> {
    private final CouponService couponService;
    private final MemberCouponService memberCouponService;
    @Override
    protected CrudService<Coupon> getService() {
        return couponService;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Coupon> save(@RequestBody Coupon coupon) {
        coupon = couponService.save(coupon);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    /**
     * 获取未获取的优惠券
     */
    @ApiOperation(value="获取未获取的优惠券")
    @RequestMapping(value = "/unClaimed", method = RequestMethod.GET)
    public ResponseEntity<List<Coupon>> getUnClaimed() throws Exception {
        List<Coupon> coupons = couponService.getUnClaimed(MemberThread.getInstance().get().getId());
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    /**
     * 获取当前用户优惠券数量
     */
    @ApiOperation(value="获取当前用户优惠券数量")
    @RequestMapping(value = "/count/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<Integer> count(@PathVariable String memberId) throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("memberId", new String[]{memberId});
        List<MemberCoupon> memberCoupons = memberCouponService.findAll(params);
        Integer count = 0;
        for (MemberCoupon memberCoupon : memberCoupons) {
            if(!memberCoupon.getUsed() && (memberCoupon.getCoupon().getEndDate() > new Date().getTime())) {
                count ++;
            }
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * 获取当前用户优惠券
     */
    @ApiOperation(value="获取当前用户优惠券")
    @RequestMapping(value = "/member/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<List<MemberCoupon>> getMemberCoupons(@PathVariable String memberId) throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("memberId", new String[]{memberId});
        List<MemberCoupon> memberCoupons = memberCouponService.findAll(params);
        return new ResponseEntity<>(memberCoupons, HttpStatus.OK);
    }

    /**
     * 获取当前用户可用优惠券
     */
    @ApiOperation(value="获取当前用户可用优惠券")
    @RequestMapping(value = "/available", method = RequestMethod.POST)
    public ResponseEntity<List<CouponDTO>> getAvailableCoupons(@RequestBody GetAvailableCouponsParam param) throws Exception {
        List<CouponDTO> memberCoupons = memberCouponService.getAvailableCoupons(param);
        return new ResponseEntity<>(memberCoupons, HttpStatus.OK);
    }

    /**
     * 领取优惠券
     */
    @ApiOperation(value="领取优惠券")
    @RequestMapping(value = "/claim", method = RequestMethod.POST)
    public ResponseEntity<?> claim(@RequestBody Coupon coupon) throws Exception {
        couponService.claim(MemberThread.getInstance().get().getId(), coupon);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public CouponController(
            CouponService couponService,
            MemberCouponService memberCouponService
    ) {
        this.couponService = couponService;
        this.memberCouponService = memberCouponService;
    }
}
