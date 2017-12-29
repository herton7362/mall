package com.framework.module.sheetmetalpaint.web;

import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.sheetmetalpaint.domain.PaintOrderForm;
import com.framework.module.sheetmetalpaint.service.PaintOrderFormService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "车辆订单管理")
@RestController
@RequestMapping("/api/paintOrderForm")
public class PaintOrderFormController extends AbstractCrudController<PaintOrderForm> {
    private final PaintOrderFormService paintOrderFormService;
    @Override
    protected CrudService<PaintOrderForm> getService() {
        return paintOrderFormService;
    }

    /**
     * 下订单
     */
    @ApiOperation(value="下订单")
    @RequestMapping(value = "/makeOrder", method = RequestMethod.POST)
    public ResponseEntity<PaintOrderForm> makeOrder(@RequestBody PaintOrderForm paintOrderForm) throws Exception {
        return new ResponseEntity<>(paintOrderFormService.makeOrder(paintOrderForm), HttpStatus.OK);
    }

    @Autowired
    public PaintOrderFormController(PaintOrderFormService paintOrderFormService) {
        this.paintOrderFormService = paintOrderFormService;
    }
}
