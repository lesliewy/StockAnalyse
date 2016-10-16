
      
/*
 * FileName: TestHtmlUtils.java
 * Author:   leslie
 * Date:     Jun 16, 2016 6:24:17 PM
 * Description: //模块目的、功能描述      
 */
   
package com.wy.stock.utils;

import java.io.File;

import org.junit.Test;


/**
 *
 * @author leslie
 */

public class TestHtmlUtils {

	@Test
	public void testTransTHSNotionHotStocksToCsv(){
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHotHistory/2016/09/05/");
		HtmlUtils.transTHSNotionHotStocksToCsv(dir);
	}
}

   