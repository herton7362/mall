package com.framework.module.product.service;

import com.framework.module.product.domain.ProductCategory;
import com.framework.module.product.dto.ProductCategoryVo;
import com.kratos.common.CrudService;

import java.util.List;

public interface ProductCategoryService extends CrudService<ProductCategory> {

    /**
     * 获取所有商品分类信息
     * @return 商品分类信息
     */
    List<ProductCategoryVo> getAllProductCategory();
}
