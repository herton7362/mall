package com.framework.module.record.domain;

import com.kratos.entity.BaseEntity;
import com.kratos.module.auth.domain.Admin;
import com.kratos.module.auth.domain.OauthClientDetails;
import com.framework.module.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel("操作记录")
public class OperationRecord extends BaseEntity {
    @ApiModelProperty(value = "业务类型")
    @Column(length = 50)
    private String businessType;
    @ApiModelProperty(value = "日志内容")
    @Column(length = 2000)
    private String content;
    @ApiModelProperty(value = "操作会员")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;
    @ApiModelProperty(value = "操作管理员")
    @ManyToOne(fetch = FetchType.EAGER)
    private Admin admin;
    @ApiModelProperty(value = "oauth2 客户端id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId", referencedColumnName = "clientId")
    private OauthClientDetails client;
    @ApiModelProperty(value = "操作ip地址")
    @Column(length = 30)
    private String ipAddress;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public OauthClientDetails getClient() {
        return client;
    }

    public void setClient(OauthClientDetails client) {
        this.client = client;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public enum BusinessType {
        FAST_INCREASE_POINT,
        CONSUME,
        RECHARGE,
        DEDUCT_BALANCE
    }
}
