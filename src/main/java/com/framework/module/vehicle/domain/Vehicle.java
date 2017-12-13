package com.framework.module.vehicle.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("车")
public class Vehicle {
    @ApiModelProperty(value = "上级分类")
    @ManyToOne(fetch = FetchType.EAGER)
    private VehicleCategory parent;
    @ApiModelProperty(value = "发动机排量")
    @Column(length = 50)
    private String engineDisplacement;
    @ApiModelProperty(value = "生产年份")
    private Integer productionYear;

    public VehicleCategory getParent() {
        return parent;
    }

    public void setParent(VehicleCategory parent) {
        this.parent = parent;
    }

    public String getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(String engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }
}
