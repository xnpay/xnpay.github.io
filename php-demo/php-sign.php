<?php
print_r("==================订单创建==================");
//定义apikey为超级全局变量
$GLOBALS['apiKey']=$apiKey = '982549aacbc046de97de28bffca9e68b';
$array = array(
    'orderNo'=>'1575966096740',
    'merchantNo'=>'20191204192421307122140114',
    'amount'=>100,
    'payMode'=>'100002',
    'ts'=>strtotime(date('Y-m-d H:i:s')),
    'notifyUrl'=>'https://www.baidu.com/',
    'returnUrl'=>'');
 
echo "\n";
//按键顺序正序排序
ksort($array);
print_r($array);
echo "\n";

//生成原始待签名字符串
$original_str=get_original_str($array);
//生成签名
$sign = sign($original_str,$apiKey); 
//创建订单链接
$url = create_order_link($original_str,$sign);
echo "请将该链接复制到浏览器打开-->".$url;

function get_original_str($array){
//拼接 
$original_str = '';
foreach ($array as $key=>$value) {
    if(!empty($value)){
        $original_str.=$key.'='.urlencode($value).'&';
    }
}
    echo "\n";
    return $original_str = rtrim($original_str,'&');
    echo "original_str:".$original_str; 
}

//生成签名
function sign($original_str,$apiKey){
    echo "\n\n";
    $aoa = $apiKey.$original_str.$apiKey;
    echo "aoa:".$aoa;
    
    echo "\n";
    $GLOBALS['base64']=$base64 = base64_encode(hash('sha256', $apiKey.$original_str.$apiKey, true));
    echo "base64:".$base64;
    
    //生成签名
    echo "\n";
    $sign = password_hash($base64,PASSWORD_BCRYPT);
    echo "sign_soure:".$sign;
    
    echo "\n";
    // 把抬头 $2y 替换成$2a
    return $sign = str_replace('$2y','$2a',$sign);
    echo "sign:".$sign;   
}

function create_order_link($original_str,$sign){
    echo "\n\n";
    return $url =  '网关地址/pay-order/#/?'.$original_str.'&sign='.urlencode($sign);
}

echo "\n\n";
print_r("==================订单查询==================");
echo "\n";
$post_data = array(
    'merchantNo'=>'20191204192421307122140114',
    'orderNo'=>'1576061857',
    'ts'=>strtotime(date('Y-m-d H:i:s'))
    );
    print_r($post_data);
//查询订单
$result = query_order($post_data);
var_dump($result);

function query_order($post_data){
    $query_original_str=get_original_str($post_data);
    $query_sign=sign($query_original_str,$GLOBALS['apiKey']);
    echo "query_sign".$query_sign;
    echo "\n\n";
    //验证签名 
    $sign_verify = password_verify($GLOBALS['base64'],$query_sign);
    if($sign_verify){
        echo "sign_verify success";
    } else{
        echo "sign_verify fail";
    }
    echo "\n";
    //向数组里添加sign
    $post_data['sign']=$query_sign;
    print_r($post_data);
    
    $url = "网关地址/any-pay/open/order/query";
    //转json
    $params = json_encode($post_data);
    //使用CURL发起psot请求 
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array(
        'Content-Type: application/json',
        'Content-Length: ' . strlen($params)
    ));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_BINARYTRANSFER, true);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $params);
     
    $result = curl_exec($ch);
    return $result;
    curl_close($ch);
    
}

?>
