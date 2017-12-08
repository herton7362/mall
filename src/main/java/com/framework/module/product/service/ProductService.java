package com.framework.module.product.service;

import com.framework.module.product.domain.Product;
import com.kratos.common.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService extends CrudService<Product> {
    /**
     * 查询总数
     * @return 总数
     */
    Long count() throws Exception;

    /**
     * 获取库存数量
     * @param pageRequest 分页参数
     * @param maxStockCount 最大库存数量
     */
    Page<Product> getStock(PageRequest pageRequest, String maxStockCount) throws Exception;
}
