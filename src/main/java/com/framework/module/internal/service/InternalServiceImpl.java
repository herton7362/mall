package com.framework.module.internal.service;

import com.kratos.entity.BaseUser;
import com.kratos.kits.Kits;
import com.kratos.kits.notification.message.SmsBroadcastMessage;
import com.kratos.module.auth.domain.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InternalServiceImpl implements InternalService {
    private final Kits kits;
    @Override
    public void sendMassage(String mobile, String message) throws Exception {
        SmsBroadcastMessage smsBroadcastMessage = new SmsBroadcastMessage();
        BaseUser admin = new Admin();
        admin.setLoginName(mobile);
        admin.setMobile(mobile);
        smsBroadcastMessage.setDestUser(admin);
        smsBroadcastMessage.setMessage(message);
        kits.notification().send(smsBroadcastMessage);
    }

    @Autowired
    public InternalServiceImpl(Kits kits) {
        this.kits = kits;
    }
}
