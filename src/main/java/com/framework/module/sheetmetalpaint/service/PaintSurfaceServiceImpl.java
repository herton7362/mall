package com.framework.module.sheetmetalpaint.service;

import com.framework.module.sheetmetalpaint.domain.PaintSurface;
import com.framework.module.sheetmetalpaint.domain.PaintSurfaceRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class PaintSurfaceServiceImpl extends AbstractCrudService<PaintSurface> implements PaintSurfaceService {
    private final PaintSurfaceRepository paintSurfaceRepository;
    @Override
    protected PageRepository<PaintSurface> getRepository() {
        return paintSurfaceRepository;
    }
    @Autowired
    public PaintSurfaceServiceImpl(PaintSurfaceRepository paintSurfaceRepository) {
        this.paintSurfaceRepository = paintSurfaceRepository;
    }
}
