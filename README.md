# 融付接口文档
## 特别注意：
1. **时间戳为秒级，非毫秒级，毫秒级请/1000**
2. **returnUrl/notifyUrl 为完整地址,含有协议+端口**
3. **金额为整数，非小数，以分为单位，不能包含有“·”号**，例：123 即 1.23 元。
1. **商户编号需要从商户后台首页获取，并非登陆账号**，商户密钥（apikey）也需要从首页刷新一次保存即可。
1. **商户接收异步通知时，请获取所有参数后进行签名，如果只接收部分参数，会导致签名验证失败。**


### 接口规范 
1. 字符编码：UTF-8
1. Content-Type：application/json
1. URL 传输参数需要对参数进行 UrlEncode

### 接口调用所需条件 
1. 网关地址：请联系客服
1. 商户编号(merchantNo)
2. 商户密钥(apiKey)

### 签名（sign）算法
```
BCrypt(Base64(SHA-256(apiKey+originalStr+apiKey)))
```
1. originalStr: 除**sign参数外**其他**参数值非空**（空值或者空字符串）的参数按**参数名称字母正序排序**然后以name=UrlEncode(value)形式组合，
通过&拼接不同参数。
    1. **注：空值（空值或者空字符串）不参与签名。**
    2. **注：value需要进行UrlEncode编码**
1. BCrypt(Base64(SHA-256(apiKey+originalStr+apiKey))) 
    1. 用SHA-256算法将“apiKey+originalStr+apiKey”进行签名得到签名信息（二进制,有些工具会生成16进制）
    1. 使用Base64编码对SHA-256二进制签名信息进行编码
    1. 使用BCrypt对编码字符串进行签名得到最终签名字符串
1. [php demo](https://github.com/rongpay/rongpay.github.io/tree/master/php-demo)
2. [java demo](https://github.com/rongpay/rongpay.github.io/tree/master/java-demo)
3. [c# demo](https://github.com/rongpay/rongpay.github.io/tree/master/C%23-demo)

### 同步通知 （returnUrl）
当创建订单时传入返回地址，订单结束后，用户点击“返回商户”，会在返回链接带上参数（returnUrl?urlparams）。参数内容参考统一返回参数，可通过签名算法计算签名的正确性。例：
```
returnUrl?
    amount=100&
    payMode=100001&
    ts=1575948756&
    orderStatus=50&
    payNo=20191209194326631108714792&
    payStatus=30&
    payTime=1575948756&
    merchantNo=20191204192421307122140114&
    orderNo=201912081855183951ab02e&
    sign=%242a%2410%24JwOX9nmVHrE6o8vcoSmyd.T69Yl7n322tVLmz.pVkRUz%2f.tRCjELS

```
    
### 异步回调 （notifyUrl）

当创建订单时传入异步回调地址时，订单结束后（用户取消订单(-30)、用户支付超时（-40）、订单失败（-50）、订单已完成（50））进行通知，总共通知3次，每次间隔10 分钟，超时时间为10s，处理成功后返回 *success*，返回其他字符表示处理失败，会继续进行后续通知。通知内容参考统一返回参数，可通过签名算法计算签名的正确性
示例：
```
curl -X POST "回调地址"
  -H 'content-type: application/json' 
  -d '{
    "amount":100,
    "payMode":"100001",
    "ts":1575948756,
    "orderStatus":50,
    "payNo":"20191209194326631108714792",
    "payStatus":30,
    "payTime":1575948756,
    "merchantNo":"20191204192421307122140114",
    "orderNo":"201912081855183951ab02e",
    "sign":"$2a$10$JwOX9nmVHrE6o8vcoSmyd.T69Yl7n322tVLmz.pVkRUz/.tRCjELS"
}'
```

### 接口内容
1. 创建订单接口
    1. 使用场景：当用户充值时，根据下面参数，商户生成跳转链接，返回给用户进行跳转到支付页面。
    2. 请求方式：页面跳转
    3. 请求地址：网关地址+/pay-order/#/?urlparams
    4. 请求参数 
    
        |参数名称| 必须|数据类型|示例| 参数说明 |
        |  ----  | ------------|---- |---- |------------  |
        |amount| 是|整数|100| 金额,以分为单位；最小值100，即1元 |
        |merchantNo|是|字符串|20191204192421307122140114| 商户编号|
        |orderNo|是|字符串(<50)|201912081855183951ab02e| 商户订单编号|
        |payMode|是|字符串|100001| 支付模式，请登陆商户后台获取|
        |ts|是|整数|1575948756| 商户订单时间戳（秒级）|
        |notifyUrl|否|字符串|https://www.baidu.com/notify| 后台通知地址|
        |returnUrl|否|字符串|https://www.baidu.com| 支付完成用户返回地址|
        |sign|是|字符串|$2a$10$JwOX9nmVHrE6o8vcoSmyd.T6...| 参数签名，请按照签名算法生成|

    5. 响应
    5. 示例
    ```
    网关地址+/pay-order/#/?amount=100&merchantNo=20191204192421307122140114&orderNo=1575730270288&payMode=100001&ts=1575730270&sign=%242a%2410%24JwOX9nmVHrE6o8vcoSmyd.T69Yl7n322tVLmz.pVkRUz%2F.tRCjELS
    ```
2. 查询订单接口
    1. 使用场景：当商户需要对特定订单查询时
    2. 请求方式：POST
    2. 请求地址：网关地址+ /any-pay/open/order/query
    4. 请求参数
    
        |参数名称|必须|数据类型|示例| 参数说明|
        |  ----  | ------------  |----  |----  |------------  |
        |merchantNo|是|字符串|20191204192421307122140114| 商户编号|
        |orderNo|是|字符串(<50)|201912081855183951ab02e| 商户订单编号|
        |ts|是|整数|1575948756| 商户订单时间戳（秒级）|
        |sign|是|字符串|$2a$10$JwOX9nmVHrE6o8vcoSmyd.T6...| 参数签名，请按照签名算法生成|
    
    6. 响应（参考统一返回参数）
    5. 示例
    ```
    请求： curl -X POST "网关地址+/any-pay/open/order/query"  -H  "accept:*/*"  -H  "Content-Type:application/json" -d "{\"merchantNo\":\"20191204192421307122140114\",\"orderNo\":\"201912081855183951ab02e\",\"sign\":\"$2a$10$JwOX9nmVHrE6o8vcoSmyd.T69Yl7n322tVLmz.pVkRUz/.tRCjELS\",\"ts\":1575948756}"
    响应： 
    ```
    
3. 统一返回参数

    |参数名称|必须|数据类型|示例| 参数说明|
    |  ----  | ------------  |----  |----  |------------  | 
    |amount|是|整数|100| 金额,以分为单位|
    |merchantNo|是|字符串|20191204192421307122140114| 商户编号|
    |orderNo|是|字符串(<50)|201912081855183951ab02e| 商户订单编号|
    |payMode|是|字符串|100001| 支付模式|
    |ts|是|整数|1575948756| 商户订单时间戳（秒级）|
    |orderStatus|是|整数|50| 订单状态|
    |payNo|是|字符串|20191209194326631108714792| 支付订单编号|
    |payStatus|否|整数|30| 支付状态|
    |payTime|否|整数|1575948756| 支付成功时间（秒级）|
    |sign|是|字符串|$2a$10$JwOX9nmVHrE6o8vcoSmyd.T6...| 参数签名，使用BCrypt校验方法校验|
    
    1. 订单状态枚举
        1. 30 - 支付等待中
        1. -30 - 用户取消订单
        1. -40 - 用户支付超时
        1. -50 - 订单失败
        1. 50 - 订单已完成
    2. 支付状态枚举
        1. 10 - 等待支付
        1. -10 - 支付超时
        1. -20 - 支付取消
        1. 30 - 支付成功
        1. -30 - 支付失败
        
** 以订单状态为主进行判断，支付超时后状态可能会收到支付成功状态通知，请注意处理 **
    
    
    
    
    
