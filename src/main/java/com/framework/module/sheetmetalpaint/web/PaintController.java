package com.framework.module.sheetmetalpaint.web;

import com.framework.module.sheetmetalpaint.domain.Paint;
import com.framework.module.sheetmetalpaint.service.PaintService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "油漆管理")
@RestController
@RequestMapping("/api/paint")
public class PaintController extends AbstractCrudController<Paint> {
    private final PaintService paintService;
    @Override
    protected CrudService<Paint> getService() {
        return paintService;
    }


    @Autowired
    public PaintController(PaintService paintService) {
        this.paintService = paintService;
    }
}
