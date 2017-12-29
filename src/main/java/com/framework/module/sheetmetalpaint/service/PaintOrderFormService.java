package com.framework.module.sheetmetalpaint.service;

import com.framework.module.sheetmetalpaint.domain.PaintOrderForm;
import com.kratos.common.CrudService;

public interface PaintOrderFormService extends CrudService<PaintOrderForm> {
    /**
     * 下订单
     * @param paintOrderForm 订单参数
     */
    PaintOrderForm makeOrder(PaintOrderForm paintOrderForm) throws Exception;
}
