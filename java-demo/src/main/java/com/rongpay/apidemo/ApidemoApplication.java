package com.rongpay.apidemo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApidemoApplication {
    public static void main(String[] args) {
        Map<String, String> param = new HashMap<>();
        param.put("amount", "100");
        param.put("merchantNo", "dec10677c8394ac59effdd08d574d667");
        param.put("orderNo", UUID.randomUUID().toString().replace("-", ""));
        param.put("payMode", "pay_mode.4");
        param.put("ts", System.currentTimeMillis() / 1000 + "");
        param.put("notifyUrl", "https://www.baidu.com");
        param.put("returnUrl", "https://www.baidu.com");

        String apiKey="c106f192d0b545fab9ecb79f1b31dd65";
        String sign=ApiDemo.generateSign(param, apiKey);
        System.out.println("sign="+sign);
        System.out.println("checkSign="+ApiDemo.checkSign(param,apiKey,sign));
        System.out.println("link="+ApiDemo.generateCreateOrderLink(param, apiKey));
    }

}
