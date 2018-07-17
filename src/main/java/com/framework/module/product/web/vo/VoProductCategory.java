package com.framework.module.product.web.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/17 11:23
 */
public class VoProductCategory {
    @ApiModelProperty(value = "商品分类ID")
    private String id;
    @ApiModelProperty(value = "商品分类名称")
    private String name;

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
}
