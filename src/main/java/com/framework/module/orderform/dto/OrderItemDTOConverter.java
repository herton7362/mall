package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.OrderItem;
import com.framework.module.product.service.ProductService;
import com.framework.module.product.service.SkuService;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        }
        if(orderItem.getSku() != null) {
            orderItemDTO.setSkuId(orderItem.getSku().getId());
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
