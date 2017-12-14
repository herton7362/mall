package com.framework.module.sheetmetalpaint.web;

import com.framework.module.sheetmetalpaint.domain.PaintSurface;
import com.framework.module.sheetmetalpaint.service.PaintSurfaceService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "漆面管理")
@RestController
@RequestMapping("/api/paintSurface")
public class PaintSurfaceController extends AbstractCrudController<PaintSurface> {
    private final PaintSurfaceService paintSurfaceService;

    @Override
    protected CrudService<PaintSurface> getService() {
        return paintSurfaceService;
    }

    @Autowired
    public PaintSurfaceController(PaintSurfaceService paintSurfaceService) {
        this.paintSurfaceService = paintSurfaceService;
    }
}
