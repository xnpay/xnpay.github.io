using System;
using System.Text;
using System.Diagnostics;
using System.Security;
using System.Security.Cryptography;
using System.Collections.Generic;
using System.Web;
using System.Net;
using BCrypt.Net;
public class Demo
{
	/**
	* api key
	*/
	public static String API_KEY = "982549aacbc046de97de28bffca9e68b";
	
	
	public static void Main()
	{
		//创建订单
		createOrder();
		
		//验签
		Console.WriteLine(checkHash("J9yMyqsbweqKqGk9cqQ/ESv1BdiUDVLXxB5BbF2eck8=", "$2a$10$6qizWcRhDOraGeIPWWE7M.UL0CvvlUxJQwpoZpCq/wD2w9GNfDhd2"));
	
	}
	
	
	/**
	 *	创建订单
	 */
	public static void createOrder()
	{
		String ts = ((DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000000) + "";
		
		SortedDictionary<string, string> parameters = new SortedDictionary<string, string>();
		parameters.Add("orderNo", ts);
		parameters.Add("merchantNo", "20191204192421307122140114");
		parameters.Add("amount", "100");
		parameters.Add("payMode", "100001");
		parameters.Add("ts", ts);
		parameters.Add("notifyUrl", "https://www.baidu.com/");
		parameters.Add("returnUrl", "https://www.baidu.com/");
		
		String paramsOrder = getParamsOrder(parameters);
		Console.WriteLine(paramsOrder);
		
		String signStr = sign(API_KEY, paramsOrder);
		
		//请求地址
		String host = "网关地址/pay-order/#/?" + paramsOrder + "&sign=" + signStr;
		Console.WriteLine(host);

	}
	
	
	
	/*
	 * 验签
	 * plainText: 原文(base64后字符串	Base64(SHA-256(apiKey + paramsOrder + apiKey))
	 * hashText : 密文(BCrypt后字符串	BCrypt(Base64(SHA-256(apiKey + paramsOrder + apiKey)))
	 */
	public static Boolean checkHash(String plainText, String hashText)
	{
		return BCrypt.Net.BCrypt.Verify(plainText, hashText);
	}
	
	
	
	
	/*
	 * 签名
	 */
	public static String sign(String apiKey, String paramsOrder)
	{
		String signStr = apiKey + paramsOrder + apiKey;
		byte[] tmpByte = new SHA256Managed().ComputeHash(Encoding.UTF8.GetBytes(signStr));
		String base64Str = Convert.ToBase64String(tmpByte);
		//UrlEncode
		return WebUtility.UrlEncode(BCrypt.Net.BCrypt.HashPassword(base64Str));
	}

	/*
	 * 排序拼接
	 */
	public static string getParamsOrder(IDictionary<string, string> parameters)
	{
		//排序
		IDictionary<string, string> sortedParams = new SortedDictionary<string, string>(parameters);
		IEnumerator<KeyValuePair<string, string>> dem = sortedParams.GetEnumerator();

		//拼接
		StringBuilder query = new StringBuilder("");
		while (dem.MoveNext())
		{
			string key = dem.Current.Key;
			string value = dem.Current.Value;
			if (!string.IsNullOrEmpty(key) && !string.IsNullOrEmpty(value))
			{
				query.Append(key).Append("=").Append(WebUtility.UrlEncode(value)).Append("&");				
			}
		}
		return query.ToString().Substring(0, query.Length - 1);
	}
}
