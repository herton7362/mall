package com.framework.module.shop.web;

import com.framework.module.marketing.domain.Coupon;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.service.ShopService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.auth.service.OauthClientDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "店铺管理")
@RestController
@RequestMapping("/api/shop")
public class ShopController extends AbstractCrudController<Shop> {
    private final ShopService shopService;
    private final OauthClientDetailsService oauthClientDetailsService;
    @Override
    protected CrudService<Shop> getService() {
        return shopService;
    }
    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Shop> save(@RequestBody Shop shop) throws Exception {
        if(shop.getOauthClientDetails() != null && StringUtils.isNotBlank(shop.getOauthClientDetails().getClientId())) {
            shop.setOauthClientDetails(oauthClientDetailsService.findOneByClientId(shop.getOauthClientDetails().getClientId()));
        } else {
            shop.setOauthClientDetails(null);
        }
        shop = shopService.save(shop);
        return new ResponseEntity<>(shop, HttpStatus.OK);
    }
    @Autowired
    public ShopController(
            ShopService shopService,
            OauthClientDetailsService oauthClientDetailsService
    ) {
        this.shopService  = shopService;
        this.oauthClientDetailsService = oauthClientDetailsService;
    }
}
