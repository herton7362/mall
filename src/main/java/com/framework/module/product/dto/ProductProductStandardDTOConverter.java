package com.framework.module.product.dto;

import com.framework.module.product.domain.ProductProductStandard;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductProductStandardDTOConverter extends SimpleDTOConverter<ProductProductStandardDTO, ProductProductStandard> {
    private final ProductStandardItemDTO productStandardItemDTO;
    @Override
    protected ProductProductStandardDTO doBackward(ProductProductStandard productProductStandard) {
        ProductProductStandardDTO productProductStandardDTO = super.doBackward(productProductStandard);
        if(productProductStandard.getProductStandard() != null) {
            productProductStandardDTO.setName(productProductStandard.getProductStandard().getName());
        }
        if(productProductStandard.getProductStandardItems() != null
                && !productProductStandard.getProductStandardItems().isEmpty()) {
            productProductStandardDTO.setItems(productProductStandard.getProductStandardItems()
                    .stream()
                    .map(productStandardItemDTO::convertFor)
                    .collect(Collectors.toList()));
        }
        return productProductStandardDTO;
    }

    @Autowired
    public ProductProductStandardDTOConverter(ProductStandardItemDTO productStandardItemDTO) {
        this.productStandardItemDTO = productStandardItemDTO;
    }
}
