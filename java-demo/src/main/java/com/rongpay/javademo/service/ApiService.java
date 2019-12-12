package com.rongpay.javademo.service;

import com.rongpay.javademo.commom.ApiUtil;
import com.rongpay.javademo.commom.AppProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Wei Jianhua
 * @since 2019/12/11
 */
@Service
public class ApiService {

    private AppProperties appProperties;

    public ApiService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * 生成创建订单字符串
     */
    public String generateCreateOrderLink(Map<String, String> param){
        String sign = ApiUtil.generateSign(param, appProperties.getApiKey());
        String paramString = ApiUtil.generateParamString(param);
        return appProperties.getApiUrl()+"/pay-order/#/?" +
                paramString +
                "&sign=" +
                sign;
    }

}
