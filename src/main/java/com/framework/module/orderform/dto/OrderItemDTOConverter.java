package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderItem;
import com.framework.module.product.domain.ProductStandardItem;
import com.framework.module.product.service.ProductService;
import com.framework.module.product.service.SkuService;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Component
public class OrderItemDTOConverter extends SimpleDTOConverter<OrderItemDTO, OrderItem> {
    private final ProductService productService;
    private final SkuService skuService;

    @Override
    protected OrderItem doForward(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = super.doForward(orderItemDTO);
        orderItem.setProduct(productService.findOne(orderItemDTO.getProductId()));
        orderItem.setSku(skuService.findOne(orderItemDTO.getSkuId()));
        return orderItem;
    }

    @Override
    protected OrderItemDTO doBackward(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = super.doBackward(orderItem);
        if(orderItem.getProduct() != null) {
            orderItemDTO.setProductId(orderItem.getProduct().getId());
            orderItemDTO.setProductName(orderItem.getProduct().getName());
            if(orderItem.getProduct().getCoverImage() != null) {
                orderItemDTO.setCoverImageUrl("/attachment/download/" + orderItem.getProduct().getCoverImage().getId());
            }
            if(orderItemDTO.getPrice() == null)
                orderItemDTO.setPrice(orderItem.getProduct().getPrice());
        }
        if(orderItem.getSku() != null) {
            orderItemDTO.setSkuId(orderItem.getSku().getId());
            if(orderItem.getSku().getCoverImage() != null) {
                orderItemDTO.setCoverImageUrl("/attachment/download/" + orderItem.getSku().getCoverImage().getId());
            }
            if (!CollectionUtils.isEmpty(orderItem.getProduct().getProductProductStandards())) {
                orderItemDTO.setProductStandardNames(String.join(",", orderItem.getSku()
                        .getProductStandardItems()
                        .stream()
                        .map(ProductStandardItem::getName).collect(Collectors.toList())));
            }
            if(orderItemDTO.getPrice() == null)
                orderItemDTO.setPrice(orderItem.getSku().getPrice());
        }
        if(orderItem.getOrderForm() != null) {
            orderItemDTO.setOrderId(orderItem.getOrderForm().getId());
        }

        return orderItemDTO;
    }

    @Autowired
    public OrderItemDTOConverter(ProductService productService, SkuService skuService) {
        this.productService = productService;
        this.skuService = skuService;
    }
}
