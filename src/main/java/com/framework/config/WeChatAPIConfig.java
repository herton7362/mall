package com.framework.config;


import com.kratos.kits.wechat.config.annotation.builder.WeChatPayConfig;
import com.kratos.kits.wechat.config.annotation.configuration.WeChatAPIConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置
 */
@Configuration
public class WeChatAPIConfig implements WeChatAPIConfigurer {

    @Override
    public void configure(WeChatPayConfig weChatPayConfig) {
        weChatPayConfig.setAppId("");
        weChatPayConfig.setAppSecret("");
        weChatPayConfig.setApiKey("");
        weChatPayConfig.setMchId("");
        weChatPayConfig.setNotifyUrl("");
    }
}
