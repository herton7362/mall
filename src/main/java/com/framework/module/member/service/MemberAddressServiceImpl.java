package com.framework.module.member.service;

import com.framework.module.member.domain.MemberAddress;
import com.framework.module.member.domain.MemberAddressRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class MemberAddressServiceImpl extends AbstractCrudService<MemberAddress> implements MemberAddressService {
    private final MemberAddressRepository memberAddressRepository;
    @Override
    protected PageRepository<MemberAddress> getRepository() {
        return memberAddressRepository;
    }

    @Override
    public MemberAddress save(MemberAddress memberAddress) {
        if(memberAddress.getMemberId() == null) {
            throw new BusinessException("会员不能为空");
        }
        memberAddressRepository.clearDefaultAddress(memberAddress.getMemberId());
        memberAddress.setDefaultAddress(true);
        return super.save(memberAddress);
    }

    @Override
    public void changeDefaultAddress(String id) {
        MemberAddress memberAddress = memberAddressRepository.findOne(id);
        memberAddress.setDefaultAddress(true);
        memberAddressRepository.clearDefaultAddress(memberAddress.getMemberId());
        memberAddressRepository.save(memberAddress);
    }

    @Override
    public void delete(String id) {
        MemberAddress memberAddress = findOne(id);
        if(memberAddress != null && memberAddress.getDefaultAddress()) {
            Map<String, String[]> param = new HashMap<>();
            param.put("memberId", new String[]{ memberAddress.getMemberId() });
            List<MemberAddress> memberAddresses = findAll(param);
            // 删除一个收获地址，将其他第一个收货地址改为默认地址
            if(memberAddresses.size() > 1) {
                MemberAddress memberAddress1;
                if(memberAddresses.get(0).getId().equals(id)) {
                    memberAddress1 = memberAddresses.get(1);
                } else {
                    memberAddress1 = memberAddresses.get(0);
                }
                memberAddress1.setDefaultAddress(true);
                memberAddressRepository.save(memberAddress1);
            }
        }
        super.delete(id);
    }

    @Autowired
    public MemberAddressServiceImpl(MemberAddressRepository memberAddressRepository) {
        this.memberAddressRepository = memberAddressRepository;
    }
}
