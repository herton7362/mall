package com.framework.module.sheetmetalpaint.domain;

import com.framework.module.orderform.base.BaseOrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("订单项")
public class PaintOrderItem extends BaseOrderItem<PaintOrderForm> {
    @ApiModelProperty(value = "购买的商品")
    @ManyToOne(fetch = FetchType.EAGER)
    private PaintSurface paintSurface;
    @ApiModelProperty(value = "油漆")
    @ManyToOne(fetch = FetchType.EAGER)
    private Paint paint;

    public PaintSurface getPaintSurface() {
        return paintSurface;
    }

    public void setPaintSurface(PaintSurface paintSurface) {
        this.paintSurface = paintSurface;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
