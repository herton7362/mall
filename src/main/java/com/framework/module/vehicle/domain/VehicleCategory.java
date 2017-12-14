package com.framework.module.vehicle.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("车型")
public class VehicleCategory extends BaseEntity {
    @ApiModelProperty(value = "上级分类")
    @ManyToOne(fetch = FetchType.EAGER)
    private VehicleCategory parent;
    @ApiModelProperty(value = "名称")
    @Column(length = 50)
    private String name;

    public VehicleCategory getParent() {
        return parent;
    }

    public void setParent(VehicleCategory parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
