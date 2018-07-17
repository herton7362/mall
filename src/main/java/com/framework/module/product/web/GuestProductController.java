package com.framework.module.product.web;

import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.Sku;
import com.framework.module.product.service.ProductService;
import com.framework.module.product.web.vo.VoHomePage;
import com.framework.module.product.web.vo.VoProduct;
import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiOperation(value = "查询一个")
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


    /**
     * 首页接口
     */
    @ApiOperation(value = "首页接口")
    @RequestMapping(value = "/homePage", method = RequestMethod.GET)
    public ResponseEntity<VoHomePage> homePage() throws Exception {
        return new ResponseEntity<>(productService.homePage(), HttpStatus.OK);
    }

    /**
     * 根据类别获取产品
     */
    @ApiOperation(value = "根据类别获取产品")
    @RequestMapping(value = "/getProductsByCategoryId/{page}/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<List<VoProduct>> getProductsByCategoryId(@RequestParam Integer page, @RequestParam String categoryId) throws Exception {
        return new ResponseEntity<>(productService.getProductsByCategoryId(page, categoryId), HttpStatus.OK);
    }

}
