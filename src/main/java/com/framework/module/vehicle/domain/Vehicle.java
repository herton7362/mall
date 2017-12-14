package com.framework.module.vehicle.domain;

import com.framework.module.sheetmetalpaint.domain.Paint;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@ApiModel("车")
public class Vehicle extends BaseEntity {
    @ApiModelProperty(value = "上级分类")
    @ManyToOne(fetch = FetchType.EAGER)
    private VehicleCategory parent;
    @ApiModelProperty(value = "发动机排量")
    @Column(length = 50)
    private String engineDisplacement;
    @ApiModelProperty(value = "生产年份")
    private Integer productionYear;
    @ApiModelProperty(value = "车辆喷漆")
    @ManyToMany
    @JoinTable(name="vehicle_paints",joinColumns={@JoinColumn(name="vehicle_id")},inverseJoinColumns={@JoinColumn(name="paint_id")})
    private List<Paint> paints;

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

    public List<Paint> getPaints() {
        return paints;
    }

    public void setPaints(List<Paint> paints) {
        this.paints = paints;
    }
}
