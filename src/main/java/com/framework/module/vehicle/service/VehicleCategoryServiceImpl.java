package com.framework.module.vehicle.service;

import com.framework.module.vehicle.domain.VehicleCategory;
import com.framework.module.vehicle.domain.VehicleCategoryRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class VehicleCategoryServiceImpl extends AbstractCrudService<VehicleCategory> implements VehicleCategoryService {
    private final VehicleCategoryRepository vehicleCategoryRepository;
    @Override
    protected PageRepository<VehicleCategory> getRepository() {
        return vehicleCategoryRepository;
    }
    @Autowired
    public VehicleCategoryServiceImpl(VehicleCategoryRepository vehicleCategoryRepository) {
        this.vehicleCategoryRepository = vehicleCategoryRepository;
    }
}
