package com.framework.module.product.web;

import com.framework.module.product.domain.Product;
import com.framework.module.product.domain.Sku;
import com.framework.module.product.dto.ProductDetailDTO;
import com.framework.module.product.service.ProductService;
import com.framework.module.product.web.vo.VoHomePage;
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
    private final ProductDetailDTO productDetailDTO;
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
        return new ResponseEntity<>(sku, HttpStatus.OK);
    }

    /**
     * 查询一个
     */
    @ApiOperation(value="查询一个")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductDetailDTO> getDetail(@PathVariable String id) throws Exception {
        Product product = getService().findOne(id);
        return new ResponseEntity<>(productDetailDTO.convertFor(product), HttpStatus.OK);
    }

    /**
     * 首页接口
     */
    @ApiOperation(value = "首页接口")
    @RequestMapping(value = "/homePage", method = RequestMethod.GET)
    public ResponseEntity<VoHomePage> homePage() throws Exception {
        return new ResponseEntity<>(productService.homePage(), HttpStatus.OK);
    }

    @Autowired
    public GuestProductController(
            ProductService productService,
            ProductDetailDTO productDetailDTO
    ) {
        this.productService = productService;
        this.productDetailDTO = productDetailDTO;
    }
}
