package com.framework.module.orderform.service;

import com.framework.module.orderform.domain.CartItem;
import com.framework.module.orderform.domain.CartItemRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CartItemServiceImpl extends AbstractCrudService<CartItem> implements CartItemService {
    private final CartItemRepository cartItemRepository;
    @Override
    protected PageRepository<CartItem> getRepository() {
        return cartItemRepository;
    }

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }
}
