package com.framework.module.home.service;

import com.framework.module.home.domain.BannerImg;
import com.framework.module.home.domain.BannerImgRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BannerImgServiceImpl extends AbstractCrudService<BannerImg> implements BannerImgService {
    private final BannerImgRepository bannerImgRepository;
    @Override
    protected PageRepository<BannerImg> getRepository() {
        return bannerImgRepository;
    }

    @Autowired
    public BannerImgServiceImpl(BannerImgRepository bannerImgRepository) {
        this.bannerImgRepository = bannerImgRepository;
    }
}
