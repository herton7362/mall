package com.framework.module.product.service;

import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.Sku;
import com.kratos.common.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService extends CrudService<Product> {
    /**
     * 查询总数
     * @return 总数
     */
    Long count();

    /**
     * 获取库存数量
     * @param pageRequest 分页参数
     * @param maxStockCount 最大库存数量
     */
    Page<Product> getStock(PageRequest pageRequest, String maxStockCount) throws Exception;

    /**
     * 根据传入的 规格 条目id来匹配具体的sku
     * @param productId 产品id
     * @param idArr 规格 条目id
     * @return sku
     */
    Sku getSkuByProductStandardItemIds(String productId, String[] idArr) throws Exception;
}
