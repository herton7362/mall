package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.Cart;
import com.framework.module.orderform.service.CartService;
import com.kratos.dto.SimpleDTOConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartDTOConverter extends SimpleDTOConverter<CartDTO, Cart> {
    private final Logger LOG = LoggerFactory.getLogger(CartItemDTOConverter.class);
    private final CartService cartService;
    @Override
    protected Cart doForward(CartDTO cartDTO) {
        Cart cart = new Cart();
        if(StringUtils.isNotBlank(cartDTO.getId())) {
            try {
                cart = cartService.findOne(cartDTO.getId());
            } catch (Exception e) {
                LOG.error("dto 转换出错", e);
            }
        }
        cart.setMemberId(cartDTO.getMemberId());
        return cart;
    }
    @Autowired
    public CartDTOConverter(CartService cartService) {
        this.cartService = cartService;
    }
}
