/**
 * 
 */
package com.wy.stock.tools;

import java.io.File;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class TestStockDownloadToolTHS {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"conf/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testDownloadBoardHotHtmlFiles(){
		StockDownloadToolTHS stockDownloadTHS = (StockDownloadToolTHS) applicationContext
				.getBean("stockDownloadToolTHS");
		stockDownloadTHS.downloadBoardHotHtmlFiles(new File(StockUtils.getDailyStockSaveDir("B")), "NOTION");
		stockDownloadTHS.downloadBoardHotHtmlFiles(new File(StockUtils.getDailyStockSaveDir("B")), "INDUSTRY");
	}
	
	@Test
	public void testDownloadIndexFiles(){
		StockDownloadToolTHS stockDownloadTHS = (StockDownloadToolTHS) applicationContext
				.getBean("stockDownloadToolTHS");
		stockDownloadTHS.downloadIndexFiles();
	}
	
	@Test
	public void testDownloadBoardHotStocksFiles(){
		StockDownloadToolTHS stockDownloadTHS = (StockDownloadToolTHS) applicationContext
				.getBean("stockDownloadToolTHS");
		stockDownloadTHS.downloadBoardHotStocksFiles("151112", "NOTION");
//		stockDownloadTHS.downloadBoardHotStocksFiles("151112", "INDUSTRY");
	}
}
