package com.framework.module.product.service;

import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.ProductRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProductServiceImpl extends AbstractCrudService<Product> implements ProductService {
    private final ProductRepository productRepository;
    @Override
    protected PageRepository<Product> getRepository() {
        return productRepository;
    }

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
