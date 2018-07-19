package com.framework.module.orderform.web;

import com.framework.module.orderform.domain.Cart;
import com.framework.module.orderform.dto.CartDTO;
import com.framework.module.orderform.service.CartService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "购物车管理")
@RestController
@RequestMapping("/api/cart")
public class CartController extends AbstractCrudController<Cart> {
    private final CartService cartService;

    @Override
    protected CrudService<Cart> getService() {
        return cartService;
    }

    /**
     * 添加商品
     */
    @ApiOperation(value = "添加商品")
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public ResponseEntity<?> makeOrder(@RequestBody Cart cart) throws Exception {
        cartService.addProduct(cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 添加商品
     */
    @ApiOperation(value = "添加商品")
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST, headers = {"version=2.0.0"})
    public ResponseEntity<?> makeOrder(@RequestBody CartDTO cart) throws Exception {
        cartService.addProduct(cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 增加购物车项数量
     */
    @ApiOperation(value = "增加购物车项数量")
    @RequestMapping(value = "/item/increase/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> increaseItem(@PathVariable String id) {
        cartService.increaseItemCount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 增加购物车项数量
     */
    @ApiOperation(value = "增加购物车项数量")
    @RequestMapping(value = "/item/reduce/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> reduceItem(@PathVariable String id) {
        cartService.reduceItemCount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 修改购物车项数量
     */
    @ApiOperation(value = "修改购物车项数量")
    @RequestMapping(value = "/item/count/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> editCount(@PathVariable String id, @RequestBody EditCountParam param) throws Exception {
        cartService.editCount(id, param);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除购物车项
     */
    @ApiOperation(value = "删除购物车项")
    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteItem(@PathVariable String id) {
        String[] ids = id.split(",");
        for (String s : ids) {
            cartService.deleteItem(s);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "获取购物车中的商品")
    @RequestMapping(value = "/getCartList", method = RequestMethod.GET)
    public ResponseEntity<CartDTO> getCartList() {
        String memberId = UserThread.getInstance().get().getId();
        CartDTO result = cartService.getCartList(memberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
}
