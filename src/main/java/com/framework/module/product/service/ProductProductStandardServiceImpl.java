package com.framework.module.product.service;

import com.framework.module.product.domain.ProductProductStandard;
import com.framework.module.product.domain.ProductProductStandardRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProductProductStandardServiceImpl extends AbstractCrudService<ProductProductStandard> implements ProductProductStandardService {
    private final ProductProductStandardRepository productProductStandardRepository;
    @Override
    protected PageRepository<ProductProductStandard> getRepository() {
        return productProductStandardRepository;
    }

    @Autowired
    public ProductProductStandardServiceImpl (ProductProductStandardRepository productProductStandardRepository) {
        this.productProductStandardRepository = productProductStandardRepository;
    }
}
