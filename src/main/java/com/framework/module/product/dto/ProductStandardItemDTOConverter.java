package com.framework.module.product.dto;

import com.framework.module.product.domain.ProductStandardItem;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.stereotype.Component;

@Component
public class ProductStandardItemDTOConverter extends SimpleDTOConverter<ProductStandardItemDTO, ProductStandardItem> {
}
