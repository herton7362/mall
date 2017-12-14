package com.framework.module.shop.service;

import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ShopServiceImpl extends AbstractCrudService<Shop> implements ShopService {
    private final ShopRepository shopRepository;
    @Override
    protected PageRepository<Shop> getRepository() {
        return shopRepository;
    }

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }
}
