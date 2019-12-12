package com.rongpay.javademo.api;

import com.rongpay.javademo.commom.ApiUtil;
import com.rongpay.javademo.commom.AppProperties;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Wei Jianhua
 * @since 2019/12/11
 */
@RestController
@RequestMapping("/callback")
public class CallbackController {

    private AppProperties appProperties;

    public CallbackController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * 返回回调
     */
    @GetMapping("return-callback")
    public String returnCallback(@RequestParam Map<String, String> param) {
        //验签
        boolean checkResult = ApiUtil.checkSign(param, appProperties.getApiKey(), param.get("sign"));
        if (checkResult) {
            //处理操作
            return "success";
        }
        return "fail";
    }

    /**
     * 通知回调
     */
    @PostMapping("notify-callback")
    public String notifyCallback(@RequestBody Map<String, String> param) {
        //验签
        boolean checkResult = ApiUtil.checkSign(param, appProperties.getApiKey(), param.get("sign"));
        if (checkResult) {
            //处理操作
            return "success";
        }
        return "fail";
    }
}
