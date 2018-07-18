package com.framework.module.marketing.dto;

import com.framework.module.marketing.domain.Coupon;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.stereotype.Component;

@Component
public class CouponDTOConverter extends SimpleDTOConverter<CouponDTO, Coupon> {
}
