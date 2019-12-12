package com.rongpay.javademo.commom;

import com.rongpay.javademo.service.RequestService;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wei Jianhua
 * @since 2019/12/11
 */
@Configuration
public class RequestConfig {

    private AppProperties appProperties;

    public RequestConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public RequestService requestService() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(RequestService.class, appProperties.getApiUrl());
    }
}
