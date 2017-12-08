package com.framework.module.product.web;

import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.ProductCategory;
import com.framework.module.product.service.ProductCategoryService;
import com.framework.module.product.service.ProductService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.common.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "产品管理")
@RestController
@RequestMapping("/api/product")
public class ProductController extends AbstractCrudController<Product> {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    @Override
    protected CrudService<Product> getService() {
        return productService;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Product> save(@RequestBody Product product) throws Exception {
        if(product.getProductCategory() != null && StringUtils.isNotBlank(product.getProductCategory().getId())) {
            product.setProductCategory(productCategoryService.findOne(product.getProductCategory().getId()));
        } else {
            product.setProductCategory(null);
        }
        product = productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * 查询总数
     */
    @ApiOperation(value="查询总数")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseEntity<Long> count() throws Exception {
        return new ResponseEntity<>(productService.count(), HttpStatus.OK);
    }

    /**
     * 查询库存
     */
    @ApiOperation(value="查询库存")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "currentPage", value = "当前页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序属性，多个用逗号隔开", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方向，多个用逗号隔开", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/stock", method = RequestMethod.GET)
    public ResponseEntity<Page<Product>> stock(@ModelAttribute PageParam pageParam, HttpServletRequest request) throws Exception {
        String maxStockCount = request.getParameter("maxStockCount");
        return new ResponseEntity<>(productService.getStock(pageParam.getPageRequest(), maxStockCount), HttpStatus.OK);
    }

    @Autowired
    public ProductController(
            ProductService productService,
            ProductCategoryService productCategoryService
    ) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }
}
