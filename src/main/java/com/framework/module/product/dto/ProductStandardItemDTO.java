package com.framework.module.product.dto;

import com.framework.module.product.domain.ProductStandardItem;
import com.kratos.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

@Component
@ApiModel("商品规格条目")
public class ProductStandardItemDTO extends BaseDTO<ProductStandardItemDTO, ProductStandardItem> {
    @ApiModelProperty(value = "名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
