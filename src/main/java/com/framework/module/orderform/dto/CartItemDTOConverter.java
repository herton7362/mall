package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.CartItem;
import com.framework.module.orderform.service.CartService;
import com.framework.module.product.domain.ProductStandardItem;
import com.framework.module.product.service.ProductService;
import com.framework.module.product.service.SkuService;
import com.kratos.dto.SimpleDTOConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Component
public class CartItemDTOConverter extends SimpleDTOConverter<CartItemDTO, CartItem> {
    private final Logger LOG = LoggerFactory.getLogger(CartItemDTOConverter.class);
    private final CartService cartService;
    private final ProductService productService;
    private final SkuService skuService;

    @Override
    protected CartItem doForward(CartItemDTO cartItemDTO) {
        CartItem cartItem = super.doForward(cartItemDTO);
        try {
            if(StringUtils.isNotBlank(cartItemDTO.getCartId())) {
                cartItem.setCart(cartService.findOne(cartItemDTO.getCartId()));
            }
            if(StringUtils.isNotBlank(cartItemDTO.getProductId())) {
                cartItem.setProduct(productService.findOne(cartItemDTO.getProductId()));
            }
            if(StringUtils.isNotBlank(cartItemDTO.getSkuId())) {
                cartItem.setSku(skuService.findOne(cartItemDTO.getSkuId()));
            }
        } catch (Exception e) {
            LOG.error("dto 转换出错", e);
        }
        return cartItem;
    }

    @Override
    protected CartItemDTO doBackward(CartItem item) {
        CartItemDTO itemDTO = super.doBackward(item);
        if(item.getProduct() != null) {
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setPrice(item.getProduct().getPrice());
            if(item.getProduct().getCoverImage() != null) {
                itemDTO.setCoverImageUrl("/attachment/download/" + item.getProduct().getCoverImage().getId());
            }
        }
        if (item.getSku() != null) {
            itemDTO.setSkuId(item.getSku().getId());
            itemDTO.setPrice(item.getSku().getPrice());
            if(item.getSku().getCoverImage() != null) {
                itemDTO.setCoverImageUrl("/attachment/download/" + item.getSku().getCoverImage().getId());
            }
            if (!CollectionUtils.isEmpty(item.getProduct().getProductProductStandards())) {
                itemDTO.setProductStandardNames(String.join(",", item.getSku()
                        .getProductStandardItems()
                        .stream()
                        .map(ProductStandardItem::getName).collect(Collectors.toList())));
            }
        }
        itemDTO.setCartId(item.getCart().getId());
        return itemDTO;
    }

    @Autowired
    public CartItemDTOConverter(
            CartService cartService,
            ProductService productService,
            SkuService skuService
    ) {
        this.cartService = cartService;
        this.productService = productService;
        this.skuService = skuService;
    }
}
