package com.framework.module.product.service;

import com.framework.module.product.domain.*;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.utils.IteratorUtils;
import com.kratos.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProductCategoryServiceImpl extends AbstractCrudService<ProductCategory> implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductStandardRepository productStandardRepository;
    private final ProductStandardItemRepository productStandardItemRepository;
    @Override
    protected PageRepository<ProductCategory> getRepository() {
        return productCategoryRepository;
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) throws Exception {
        deleteProductStandard(productCategory);

        IteratorUtils.forEach(productCategory.getProductStandards(), (index, productStandard) -> {
            productStandard.setSortNumber(index);
            productStandard.setProductCategory(productCategory);
            IteratorUtils.forEach(productStandard.getItems(), (i, item) -> {
                item.setSortNumber(i);
                item.setProductStandard(productStandard);
            });
            productStandardItemRepository.save(productStandard.getItems());
        });
        productStandardRepository.save(productCategory.getProductStandards());

        return super.save(productCategory);
    }

    private void deleteProductStandard(ProductCategory productCategory) throws Exception {
        if(StringUtils.isBlank(productCategory.getId())) {
            return;
        }
        ProductCategory old = productCategoryRepository.findOne(productCategory.getId());
        productCategory.getProductStandards().forEach(productStandard -> {
            if(StringUtils.isBlank(productStandard.getId())) {
                return;
            }
            ProductStandard oldProductStandard = productStandardRepository.findOne(productStandard.getId());
            oldProductStandard.getItems().removeAll(productStandard.getItems());
            productStandardItemRepository.delete(oldProductStandard.getItems());
        });
        old.getProductStandards().removeAll(productCategory.getProductStandards());
        productStandardRepository.delete(old.getProductStandards());
    }

    @Autowired
    public ProductCategoryServiceImpl(
            ProductCategoryRepository productCategoryRepository,
            ProductStandardRepository productStandardRepository,
            ProductStandardItemRepository productStandardItemRepository
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productStandardRepository = productStandardRepository;
        this.productStandardItemRepository = productStandardItemRepository;
    }
}
