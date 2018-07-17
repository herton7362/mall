package com.framework.module.product.service;

import com.framework.module.product.domain.ProductCategory;
import com.framework.module.product.web.vo.VoProductCategory;
import com.kratos.common.CrudService;

import java.util.List;

public interface ProductCategoryService extends CrudService<ProductCategory> {

    List<VoProductCategory> getAllProductCategory();
}
