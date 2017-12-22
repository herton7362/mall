package com.framework.module.vehicle.web;

import com.framework.module.vehicle.domain.VehicleCategory;
import com.framework.module.vehicle.service.VehicleCategoryService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "车辆类型管理")
@RestController
@RequestMapping("/api/vehicleCategory")
public class VehicleCategoryController extends AbstractCrudController<VehicleCategory> {
    private final VehicleCategoryService vehicleCategoryService;
    @Override
    protected CrudService<VehicleCategory> getService() {
        return vehicleCategoryService;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<VehicleCategory> save(@RequestBody VehicleCategory vehicleCategory) throws Exception {
        if(vehicleCategory.getParent() != null && StringUtils.isNotBlank(vehicleCategory.getParent().getId())) {
            vehicleCategory.setParent(vehicleCategoryService.findOne(vehicleCategory.getParent().getId()));
        } else {
            vehicleCategory.setParent(null);
        }
        vehicleCategory = vehicleCategoryService.save(vehicleCategory);
        return new ResponseEntity<>(vehicleCategory, HttpStatus.OK);
    }

    @Autowired
    public VehicleCategoryController(VehicleCategoryService vehicleCategoryService) {
        this.vehicleCategoryService = vehicleCategoryService;
    }
}
