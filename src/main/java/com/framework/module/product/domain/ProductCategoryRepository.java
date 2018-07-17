package com.framework.module.product.domain;

import com.kratos.common.PageRepository;

import java.util.List;

public interface ProductCategoryRepository extends PageRepository<ProductCategory> {

    List<ProductCategory> findAllByLogicallyDeletedFalseOrderBySortNumber();
}
