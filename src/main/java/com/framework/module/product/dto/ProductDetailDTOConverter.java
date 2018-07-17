package com.framework.module.product.dto;

import com.framework.module.product.domain.Product;
import com.kratos.dto.SimpleDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductDetailDTOConverter extends SimpleDTOConverter<ProductDetailDTO, Product> {
    private ProductProductStandardDTO productProductStandardDTO;
    @Override
    protected ProductDetailDTO doBackward(Product product) {
        ProductDetailDTO productDetailDTO = super.doBackward(product);
        if(product.getCoverImage() != null)
            productDetailDTO.setCoverImageUrl(String.format("/attachment/download/%s", product.getCoverImage().getId()));
        if(product.getStyleImages() != null && !product.getStyleImages().isEmpty()) {
            productDetailDTO.setStyleImageUrls(product.getStyleImages()
                    .stream()
                    .map(attachment -> String.format("/attachment/download/%s", attachment.getId()))
                    .collect(Collectors.toList()));
        }
        if(product.getDetailImages() != null && !product.getDetailImages().isEmpty()) {
            productDetailDTO.setDetailImageUrls(product.getDetailImages()
                    .stream()
                    .map(attachment -> String.format("/attachment/download/%s", attachment.getId()))
                    .collect(Collectors.toList()));
        }
        if(product.getSkus() != null
                && !product.getSkus().isEmpty()
                && product.getProductProductStandards() != null
                && !product.getProductProductStandards().isEmpty()) {
            productDetailDTO.setProductStandards(product.getProductProductStandards()
                    .stream()
                    .map(productProductStandardDTO::convertFor)
                    .collect(Collectors.toList()));
        }
        return productDetailDTO;
    }

    @Autowired
    public ProductDetailDTOConverter(ProductProductStandardDTO productProductStandardDTO) {
        this.productProductStandardDTO = productProductStandardDTO;
    }
}
