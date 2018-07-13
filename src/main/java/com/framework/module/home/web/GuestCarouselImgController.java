package com.framework.module.home.web;

import com.framework.module.home.domain.CarouselImg;
import com.framework.module.home.service.CarouselImgService;
import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "#首页轮播图片管理")
@RestController
@RequestMapping("/carouselImg")
public class GuestCarouselImgController extends AbstractReadController<CarouselImg> {
    private final CarouselImgService carouselImgService;
    @Override
    protected CrudService<CarouselImg> getService() {
        return carouselImgService;
    }

    @Autowired
    public GuestCarouselImgController(CarouselImgService carouselImgService) {
        this.carouselImgService = carouselImgService;
    }
}
