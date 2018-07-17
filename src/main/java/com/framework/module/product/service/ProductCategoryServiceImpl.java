package com.framework.module.product.service;

import com.framework.module.product.domain.ProductCategory;
import com.framework.module.product.domain.ProductCategoryRepository;
import com.framework.module.product.web.vo.VoProductCategory;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ProductCategoryServiceImpl extends AbstractCrudService<ProductCategory> implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    protected PageRepository<ProductCategory> getRepository() {
        return productCategoryRepository;
    }

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public List<VoProductCategory> getAllProductCategory() {
        List<VoProductCategory> resultList = new ArrayList<>();
        Iterable<ProductCategory> productCategoryList = productCategoryRepository.findAllByLogicallyDeletedFalseOrderBySortNumber();
        for (ProductCategory aProductCategoryList : productCategoryList) {
            VoProductCategory voProductCategory = new VoProductCategory();
            BeanUtils.copyProperties(aProductCategoryList, voProductCategory);
            resultList.add(voProductCategory);
        }
        return resultList;
    }
}
