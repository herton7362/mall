package com.framework.module.home.web;

import com.framework.module.home.domain.BannerImg;
import com.framework.module.home.service.BannerImgService;
import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("首页banner管理")
@RestController
@RequestMapping("/bannerImg")
public class GuestBannerImgController extends AbstractReadController<BannerImg> {
    private final BannerImgService bannerImgService;
    @Override
    protected CrudService<BannerImg> getService() {
        return bannerImgService;
    }
    @Autowired
    public GuestBannerImgController(BannerImgService bannerImgService) {
        this.bannerImgService = bannerImgService;
    }
}