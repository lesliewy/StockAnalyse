
      
/*
 * FileName: TestExcelUtils.java
 * Author:   leslie
 * Date:     Jun 16, 2016 5:35:26 PM
 * Description: //模块目的、功能描述      
 */
   
package com.wy.stock.utils;

import java.io.File;

import org.junit.Test;


/**
 *
 * @author leslie
 */

public class TestExcelUtils {

	@Test
	public void testTransTHSNotionHotStocksToCsv(){
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHotHistory/2016/03/07");
		ExcelUtils.transTHSNotionHotStocksToCsv(dir);
	}
}

   