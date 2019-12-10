package com.rongpay.apidemo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Wei Jianhua
 * @since 2019/12/9
 */
public class ApiDemo {

    /**
     * 生成签名
     *
     * @param param
     * @param apiKey
     * @return
     */
    public static String generateSign(Map<String, String> param, String apiKey) {
        String paramString = generateParamString(param);
        return BCrypt.hashpw(encodeParamString(paramString, apiKey),
                BCrypt.gensalt());

    }

    /**
     * 生成创建订单字符串
     *
     * @param param
     * @param apiKey
     * @return
     */
    public static String generateCreateOrderLink(Map<String, String> param, String apiKey) {
        String sign = generateSign(param, apiKey);
        String paramString = generateParamString(param);
        return "网关地址/pay-order/#/?" +
                paramString +
                "&sign=" +
                sign;
    }

    /**
     * 生成参数字符串
     *
     * @param param
     * @return
     */
    private static String generateParamString(Map<String, String> param) {
        TreeMap<String, String> soredParam = new TreeMap<>(param);
        String paramString = soredParam.entrySet().stream().filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .filter(entry -> !entry.getKey().equals("sign"))
                .reduce(new StringBuilder(), (sb, entry) -> sb.append(entry.getKey())
                                .append('=')
                                .append(URLEncoder.encode(entry.getValue()))
                                .append("&")
                        , StringBuilder::append).toString();
        paramString = paramString.substring(0, paramString.length() - 1);
        return paramString;
    }

    /**
     * encode参数字符串
     *
     * @return
     */
    private static String encodeParamString(String paramString, String apiKey) {
        return Base64.encodeBase64String(DigestUtils.sha256(apiKey + paramString + apiKey));
    }

    /**
     * 验证签名
     * @param param
     * @param apiKey
     * @param sign
     * @return
     */
    public static boolean checkSign(Map<String, String> param, String apiKey, String sign) {
        String paramString = generateParamString(param);
        String encodedParamString = encodeParamString(paramString, apiKey);
        return BCrypt.checkpw(encodedParamString, sign);
    }
}
