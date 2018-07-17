package com.framework.module.product.dto;

import com.framework.module.product.domain.ProductProductStandard;
import com.kratos.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ApiModel("商品规格")
public class ProductProductStandardDTO extends BaseDTO<ProductProductStandardDTO, ProductProductStandard> {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "规格条目")
    private List<ProductStandardItemDTO> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductStandardItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ProductStandardItemDTO> items) {
        this.items = items;
    }
}
