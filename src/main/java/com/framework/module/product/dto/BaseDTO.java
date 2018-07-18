package com.framework.module.product.dto;

import com.kratos.entity.BaseEntity;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/18 09:37
 */
public interface BaseDTO {
    /**
     * 将DO转换为VO
     * @param po
     * @param <T>
     */
    <T extends BaseEntity> void convertFromPo(T po);
}
