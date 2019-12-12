package com.rongpay.javademo.commom;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author Wei Jianhua
 * @since 2019/12/11
 */
@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Data
public class AppProperties {
    /**
     * apiKey
     */
    @NotBlank
    private String apiKey;
    /**
     * 商户号
     */
    @NotBlank
    private String merchantNo;
    /**
     * api地址
     */
    @NotBlank
    private String apiUrl;
}
