package com.framework.module.member.service;

import com.framework.module.marketing.dto.CouponDTO;
import com.framework.module.marketing.web.GetAvailableCouponsParam;
import com.framework.module.member.domain.MemberCoupon;
import com.kratos.common.CrudService;

import java.util.List;

public interface MemberCouponService extends CrudService<MemberCoupon> {
    /**
     * 获取可用优惠券
     * @param param 参数
     * @return 可用优惠券集合
     */
    List<CouponDTO> getAvailableCoupons(GetAvailableCouponsParam param) throws Exception;
}
