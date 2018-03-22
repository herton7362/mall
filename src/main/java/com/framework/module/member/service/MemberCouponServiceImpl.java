package com.framework.module.member.service;

import com.framework.module.member.domain.MemberCoupon;
import com.framework.module.member.domain.MemberCouponRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MemberCouponServiceImpl extends AbstractCrudService<MemberCoupon> implements MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    @Override
    protected PageRepository<MemberCoupon> getRepository() {
        return memberCouponRepository;
    }

    @Autowired
    public MemberCouponServiceImpl(MemberCouponRepository memberCouponRepository) {
        this.memberCouponRepository = memberCouponRepository;
    }
}
