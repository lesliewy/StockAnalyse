/**
 * 
 */
package com.wy.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.junit.Test;

import com.wy.stock.utils.HttpUtils;
import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class Test1 {
	@Test
	public void test1(){
		BigDecimal a = new BigDecimal("46500");
		BigDecimal b = new BigDecimal("13200");
		a.setScale(2);
		b.setScale(2);
		System.out.println(a.divide(b, RoundingMode.HALF_UP));
		System.out.println(13.14/12.75 + "  " + (13.14/12.75 < 1.001));
	}
	
	@Test
	public void test2(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        System.out.println(timestamp);
        System.out.println(tradeDate);
	}
	
	@Test
	public void test3(){
		System.out.println("aaa: " + StockUtils.convertUtfString("\\u6d77\\u5de5\\u88c5\\u5907"));
		System.out.println("aaa: " + StockUtils.convertUtfString("\\u534e\\u6570\\u4f20\\u5a92"));
	}
	
	@Test
	public void test4(){
		System.out.println("动力/锂电池".replace("/", "_"));
		
		String str = "a123=b456";
		System.out.println(str.split("=")[0]);
		System.out.println(str.split("=")[1]);
		
		BigDecimal a = new BigDecimal("-0.15");
		System.out.println("a: " + a);
		
		System.out.println(NumberUtils.isNumber("0.15"));
		System.out.println(NumberUtils.isNumber("-0.15"));
		System.out.println(NumberUtils.isNumber("-"));
	}
	
	@Test
	public void test5(){
		String str = "-1.53";
		String newStr = str.replace("亿", "").replace("万", "");
		System.out.println("newStr: " + newStr);
	}
	
	@Test
	public void test6(){
		Formatter formatter = new Formatter(); 
		formatter.format("%-15s %-15s\n", "主力: -511.16万", "超大单: -413.53万");
		System.out.println(formatter.toString());
		formatter.close();
		Formatter formatter2 = new Formatter(); 
		formatter2.format("%-15s %-15s\n", "主力: -7.18万", "超大单: 22.38万");
		System.out.println(formatter2.toString());
		formatter2.close();
	}
	
	@Test
	public void test7(){
		int a=3;
		System.out.println(-a + 8);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void test8(){
		StockUtils stockUtils = new StockUtils();
		String str = "ST板块";
		str = "新能源汽车";
		str = "上证50样本股";
		String a = stockUtils.getPinYinHeadChar(str);
		System.out.println("a: " + a);
	}
	
	@Test
	public void test9(){
		JSONObject a = new JSONObject();
		a.put("name", "leslie1");
		a.put("age", 13);
		System.out.println(a.toString());
	}
	 @Test
	 public void test10(){
		 String notionUrl = "http://q.10jqka.com.cn/gn/detail/code/gn_300200/";
		 String notionCode = notionUrl.split("/")[6].replace("gn_", "");
		 System.out.println("notionCode: " + notionCode);
	 }
	 
	 @Test
	 public void test11(){
		 DecimalFormat df = new DecimalFormat("###0.00");  
		 System.out.println(df.format(11234.123));
		 System.out.println(df.format(1.123));
		 System.out.println(df.format(10.123));
		 System.out.println(df.format(0.123));
		 System.out.println(df.format(0.12));
	 }
	 
	 @Test
	 public void test12(){
		 try {
			 File file1 = new File("/home/leslie/a" + ".json");
					HttpUtils.httpDownload("http://q.10jqka.com.cn/interface/stock/detail/zdf/desc/2/1/hxzy", "GB2312", 10 * 1000, file1);
 		} catch (FileNotFoundException e) {
 		} catch (IOException e) {
 		}
	 }
	
}
