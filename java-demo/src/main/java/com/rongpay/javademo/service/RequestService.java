package com.rongpay.javademo.service;

import feign.Headers;
import feign.RequestLine;

import java.util.Map;

/**
 * @author Wei Jianhua
 * @since 2019/12/11
 */
public interface RequestService {
    /**
     * 发起订单查询请求
     */
    @RequestLine("POST /any-pay/open/order/query")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Map<String,String> queryOrder(Map<String,String> param);
}
