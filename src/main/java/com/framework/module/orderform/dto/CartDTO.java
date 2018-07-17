package com.framework.module.orderform.dto;

import com.framework.module.orderform.domain.Cart;
import com.framework.module.orderform.service.CartItemService;
import com.kratos.dto.BaseDTO;
import com.kratos.dto.Children;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ApiModel("提交购物车参数")
public class CartDTO extends BaseDTO<CartDTO, Cart> {
    @ApiModelProperty(value = "会员")
    private String memberId;
    @Children(service = CartItemService.class)
    @ApiModelProperty(value = "购物车条目")
    private List<CartItemDTO> items;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
}
