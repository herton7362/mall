package com.framework.module.sheetmetalpaint.domain;

import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("漆面")
public class PaintSurface {
    @ApiModelProperty(value = "名称")
    @Column(length = 100)
    private String name;
    @ApiModelProperty(value = "标准面百分比")
    @Column(length = 11, precision = 1)
    private Double standardsPercent;
    @ApiModelProperty(value = "图片")
    @ManyToOne
    private Attachment img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStandardsPercent() {
        return standardsPercent;
    }

    public void setStandardsPercent(Double standardsPercent) {
        this.standardsPercent = standardsPercent;
    }

    public Attachment getImg() {
        return img;
    }

    public void setImg(Attachment img) {
        this.img = img;
    }
}
