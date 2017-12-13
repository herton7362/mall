package com.framework.module.sheetmetalpaint.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("油漆")
public class Paint extends BaseEntity{
    @ApiModelProperty(value = "名称")
    @Column(length = 100)
    private String name;
    @ApiModelProperty(value = "价格")
    @Column(length = 11, precision = 2)
    private Double price;
    @ApiModelProperty(value = "备注")
    @Column(length = 500)
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
