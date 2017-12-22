package com.framework.module.vehicle.domain;

import com.framework.module.sheetmetalpaint.domain.Paint;
import com.kratos.entity.BaseEntity;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@ApiModel("车型")
public class VehicleCategory extends BaseEntity {
    @ApiModelProperty(value = "上级分类")
    @ManyToOne(fetch = FetchType.EAGER)
    private VehicleCategory parent;
    @ApiModelProperty(value = "名称")
    @Column(length = 50)
    private String name;
    @ApiModelProperty(value = "车辆喷漆")
    @ManyToMany
    @JoinTable(name="vehicle_category_paints",joinColumns={@JoinColumn(name="vehicle_category_id")},inverseJoinColumns={@JoinColumn(name="paint_id")})
    private List<Paint> paints;
    @ApiModelProperty(value = "热门")
    private Boolean hot;
    @ApiModelProperty(value = "logo图片")
    @ManyToOne
    private Attachment logo;

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

    public List<Paint> getPaints() {
        return paints;
    }

    public void setPaints(List<Paint> paints) {
        this.paints = paints;
    }

    public Boolean getHot() {
        return hot;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public Attachment getLogo() {
        return logo;
    }

    public void setLogo(Attachment logo) {
        this.logo = logo;
    }
}
