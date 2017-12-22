package com.framework.module.vehicle.web;

import com.framework.module.vehicle.domain.Vehicle;
import com.framework.module.vehicle.service.VehicleService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "车辆管理")
@RestController
@RequestMapping("/api/vehicle")
public class VehicleController extends AbstractCrudController<Vehicle> {
    private VehicleService vehicleService;
    @Override
    protected CrudService<Vehicle> getService() {
        return vehicleService;
    }



    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
}
