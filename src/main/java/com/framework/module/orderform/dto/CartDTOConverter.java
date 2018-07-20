package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.Cart;
import com.framework.module.orderform.domain.CartItem;
import com.framework.module.orderform.service.CartService;
import com.framework.module.product.domain.ProductProductStandard;
import com.framework.module.product.domain.ProductStandardItem;
import com.kratos.dto.SimpleDTOConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartDTOConverter extends SimpleDTOConverter<CartDTO, Cart> {
    private final Logger LOG = LoggerFactory.getLogger(CartItemDTOConverter.class);
    private final CartService cartService;
    private final CartItemDTO cartItemDTO;

    @Override
    protected Cart doForward(CartDTO cartDTO) {
        Cart cart = new Cart();
        if (StringUtils.isNotBlank(cartDTO.getId())) {
            try {
                cart = cartService.findOne(cartDTO.getId());
            } catch (Exception e) {
                LOG.error("dto 转换出错", e);
            }
        }
        cart.setMemberId(cartDTO.getMemberId());
        return cart;
    }

    @Override
    protected CartDTO doBackward(Cart cart) {
        CartDTO cartDTO = super.doBackward(cart);
        if (!CollectionUtils.isEmpty(cart.getItems())) {
            List<CartItemDTO> cartItemDTOS = new ArrayList<>();
            for (CartItem item : cart.getItems()) {
                CartItemDTO itemDTO = cartItemDTO.convertFor(item);
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                if (item.getSku() != null) {
                    itemDTO.setSkuId(item.getSku().getId());
                    itemDTO.setPrice(item.getSku().getPrice());
                    itemDTO.setCoverImageUrl("/attachment/download/" + item.getSku().getId() + "." + item.getSku().getCoverImage().getFormat());
                    if (!CollectionUtils.isEmpty(item.getProduct().getProductProductStandards()) && !CollectionUtils.isEmpty(item.getProduct().getProductProductStandards().get(0).getProductStandardItems())) {
                        StringBuilder builder = new StringBuilder();
                        for (ProductStandardItem d : item.getProduct().getProductProductStandards().get(0).getProductStandardItems()) {
                            builder.append(d.getName());
                        }
                        itemDTO.setProductStandardNames(builder.toString());

                    }
                }
                itemDTO.setCartId(item.getCart().getId());
                cartItemDTOS.add(itemDTO);
            }
            cartDTO.setItems(cartItemDTOS);
        }
        return cartDTO;
    }

    @Autowired
    public CartDTOConverter(CartService cartService, CartItemDTO cartItemDTO) {
        this.cartService = cartService;
        this.cartItemDTO = cartItemDTO;
    }
}
