package com.framework.module.sheetmetalpaint.service;

import com.framework.module.sheetmetalpaint.domain.Paint;
import com.framework.module.sheetmetalpaint.domain.PaintRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class PaintServiceImpl extends AbstractCrudService<Paint> implements PaintService {
    private final PaintRepository paintRepository;
    @Override
    protected PageRepository<Paint> getRepository() {
        return paintRepository;
    }
    @Autowired
    public PaintServiceImpl(PaintRepository paintRepository) {
        this. paintRepository = paintRepository;
    }
}
