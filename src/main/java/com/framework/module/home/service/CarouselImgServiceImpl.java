package com.framework.module.home.service;

import com.framework.module.home.domain.CarouselImg;
import com.framework.module.home.domain.CarouselImgRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CarouselImgServiceImpl extends AbstractCrudService<CarouselImg> implements CarouselImgService {
    private final CarouselImgRepository carouselImgRepository;
    @Override
    protected PageRepository<CarouselImg> getRepository() {
        return carouselImgRepository;
    }

    @Autowired
    public CarouselImgServiceImpl(CarouselImgRepository carouselImgRepository) {
        this.carouselImgRepository = carouselImgRepository;
    }
}
