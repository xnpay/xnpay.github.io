package com.rongpay.javademo.commom;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Wei Jianhua
 * @since 2019/12/11
 */
public class ApiUtil {
    /**
     * 生成签名
     */
    public static String generateSign(Map<String, String> param, String apiKey) {
        return BCrypt.hashpw(encodeParamString(param, apiKey),
                BCrypt.gensalt());

    }

    /**
     * 生成参数字符串
     */
    public static String generateParamString(Map<String, String> param) {
        if (CollectionUtils.isEmpty(param)) {
            throw new RuntimeException("参数不能为空");
        }
        TreeMap<String, String> soredParam = new TreeMap<>(param);
        String paramString = soredParam.entrySet().stream().filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .filter(entry -> !entry.getKey().equals("sign"))
                .reduce(new StringBuilder(), (sb, entry) -> {
                            try {
                                sb.append(entry.getKey())
                                        .append('=')
                                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()))
                                        .append("&");
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e.getMessage());
                            }
                            return sb;
                        }
                        , StringBuilder::append).toString();
        paramString = paramString.substring(0, paramString.length() - 1);
        return paramString;
    }

    /**
     * encode参数字符串
     */
    private static String encodeParamString(Map<String, String> param, String apiKey) {
        return Base64.encodeBase64String(DigestUtils.sha256(apiKey + generateParamString(param) + apiKey));
    }

    /**
     * 验证签名
     */
    public static boolean checkSign(Map<String, String> param, String apiKey, String sign) {
        return BCrypt.checkpw(encodeParamString(param, apiKey), sign);
    }
}
