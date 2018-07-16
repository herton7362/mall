package com.framework.module.product.web;

import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.Sku;
import com.framework.module.product.service.ProductService;
import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "游客产品接口，无权限过滤")
@RestController
@RequestMapping("/product")
public class GuestProductController extends AbstractReadController<Product> {
    private final ProductService productService;
    @Override
    protected CrudService<Product> getService() {
        return productService;
    }

    /**
     * 根据传入的 规格 条目id来匹配具体的sku
     */
    @ApiOperation(value="查询一个")
    @RequestMapping(value = "/{productId}/sku/{productStandardItemIds}", method = RequestMethod.GET)
    public ResponseEntity<Sku> getSkuByProductStandardItemIds(@PathVariable String productId,
                                                              @PathVariable String productStandardItemIds) throws Exception {
        String[] idArr = productStandardItemIds.split(",");
        Sku sku = productService.getSkuByProductStandardItemIds(productId, idArr);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public GuestProductController(ProductService productService) {
        this.productService = productService;
    }
}
