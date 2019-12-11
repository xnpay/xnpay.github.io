<?php

$apiKey = "83906f0a50cb403c812f2d7776dca939";
$array = array(
    "orderNo"=>"1575966096740",
    "merchantNo"=>"20191123161925299103193198",
    "amount"=>100,
    "payMode"=>"100002",
    "ts"=>strtotime(date('Y-m-d H:i:s')),
    "notifyUrl"=>"https://www.baidu.com/",
    "returnUrl"=>"");
 
echo "\n";
//按键顺序正序排序
ksort($array);
print_r($array);
echo "\n";
//拼接 
$original_str = "";
foreach ($array as $key=>$value) {
    if(!empty($value)){
        $original_str.=$key."=".urlencode($value)."&";
    }
}
echo "\n";
$original_str = rtrim($original_str,"&");
echo "original_str:".$original_str;

echo "\n\n";
$aoa = $apiKey.$original_str.$apiKey;
echo "aoa:".$aoa;

echo "\n";
$base64 = base64_encode(hash("sha256", $apiKey.$original_str.$apiKey, true));
echo "base64:".$base64;

//生成签名
echo "\n";
$sign = password_hash($base64,PASSWORD_BCRYPT);
echo "sign_soure:".$sign;

echo "\n";
// 把抬头 $2y 替换成$2a
$sign = str_replace("$2y","$2a",$sign);
echo "sign:".$sign;

echo "\n\n";
echo "网关地址/pay-order/#/?".$original_str."&sign=".urlencode($sign);


echo "\n\n";
// $sign = str_replace("$2a","$2y",$sign);
//检验签名 
$sign_verify = password_verify($base64,$sign);
if($sign_verify){
    echo "sign_verify success";
} else{
    echo "sign_verify fail";
}


?>
