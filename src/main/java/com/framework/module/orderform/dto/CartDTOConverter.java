package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.Cart;
import com.framework.module.orderform.domain.CartItem;
import com.framework.module.orderform.service.CartService;
import com.kratos.dto.SimpleDTOConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartDTOConverter extends SimpleDTOConverter<CartDTO, Cart> {
    private final CartItemDTO cartItemDTO;

    @Override
    protected Cart doForward(CartDTO cartDTO) {
        return super.doForward(cartDTO);
    }

    @Override
    protected CartDTO doBackward(Cart cart) {
        CartDTO cartDTO = super.doBackward(cart);
        if (!CollectionUtils.isEmpty(cart.getItems())) {
            List<CartItemDTO> cartItemDTOS = new ArrayList<>();
            for (CartItem item : cart.getItems()) {
                CartItemDTO itemDTO = cartItemDTO.convertFor(item);
                cartItemDTOS.add(itemDTO);
            }
            cartDTO.setItems(cartItemDTOS);
        }
        return cartDTO;
    }

    @Autowired
    public CartDTOConverter(CartItemDTO cartItemDTO) {
        this.cartItemDTO = cartItemDTO;
    }
}
