package com.framework.module.member.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberAddressRepository extends PageRepository<MemberAddress> {
    @Modifying
    @Query("update MemberAddress m set m.defaultAddress = false where m.memberId=?1")
    void clearDefaultAddress(String memberId);
}
