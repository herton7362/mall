package com.framework.module.product.service;

import com.framework.module.product.domain.Sku;
import com.framework.module.product.domain.SkuRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SkuServiceImpl extends AbstractCrudService<Sku> implements SkuService {
    private final SkuRepository skuRepository;
    @Override
    protected PageRepository<Sku> getRepository() {
        return skuRepository;
    }

    @Autowired
    public SkuServiceImpl(SkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }
}
