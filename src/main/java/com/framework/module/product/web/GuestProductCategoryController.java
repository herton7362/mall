package com.framework.module.product.web;

import com.framework.module.product.domain.ProductCategory;
import com.framework.module.product.service.ProductCategoryService;
import com.framework.module.product.web.vo.VoHomePage;
import com.framework.module.product.web.vo.VoProductCategory;
import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "游客产品接口，无权限过滤")
@RestController
@RequestMapping("/productCategory")
public class GuestProductCategoryController extends AbstractReadController<ProductCategory> {
    private final ProductCategoryService productCategoryService;

    @Override
    protected CrudService<ProductCategory> getService() {
        return productCategoryService;
    }

    @Autowired
    public GuestProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * 获得所有产品分类信息
     */
    @ApiOperation(value = "获得所有产品分类")
    @RequestMapping(value = "/getAllProductCategory", method = RequestMethod.GET)
    public ResponseEntity<List<VoProductCategory>> getAllProductCategory() {
        return new ResponseEntity<>(productCategoryService.getAllProductCategory(), HttpStatus.OK);
    }
}
