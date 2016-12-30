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
public class TestStockParseToolTHS {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"conf/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testPersistNotionIndustryInfo(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
				.getBean("stockParseToolTHS");
//		File html = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/2016/06/01/notionHot.html");
//		stockParseTHS.persistNotionIndustryInfo(html, "NOTION");
		
		File html = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/2015/10/27/industryHot.html");
		stockParseTHS.persistNotionIndustryInfo(html, "INDUSTRY");
	}
	
	@Test
	public void testPersistIndexFromHtml(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
				.getBean("stockParseToolTHS");
		stockParseTHS.persistIndexFromHtml(new File(StockUtils.getDailyStockSaveDir("B") + "index.html"));
	}
	
	@Test
	public void testPersistNotionIndustryHot(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
				.getBean("stockParseToolTHS");
//		stockParseTHS.persistNotionIndustryHot(new File(StockUtils.getDailyStockSaveDir("B") + "notionHot.html"), "NOTION");
		stockParseTHS.persistNotionIndustryHot(new File(StockUtils.getDailyStockSaveDir("B") + "industryHot.html"), "INDUSTRY");
	}
	
	@Test
	public void testPersistNotionIndustryHotStocksFromhtml(){
//		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
//				.getBean("stockParseToolTHS");
//		stockParseTHS.persistNotionIndustryHotStocksFromhtml("151028", "NOTION");
//		stockParseTHS.persistNotionIndustryHotStocksFromhtml("151028", "INDUSTRY");
	}
	
	@Test
	public void testPersistNotionHotFromCsv(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
				.getBean("stockParseToolTHS");
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHotHistory/2016/09/05");
		stockParseTHS.persistNotionHotHistFromCsv(dir);
	}
	
	@Test
	public void testPersistIndustryHotHistFromHtml(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
				.getBean("stockParseToolTHS");
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHotHistory/2016/09/05");
		stockParseTHS.persistIndustryHotHistFromHtml(dir);
	}
	
	@Test
	public void testPersistNotionHotStocksFromCsv(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
				.getBean("stockParseToolTHS");
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHotHistory/2016/09/05/");
		stockParseTHS.persistNotionHotStocksHistFromCsv(dir);
	}
	
	@Test
	public void testGenTHSStocksChromeUrl(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
		.getBean("stockParseToolTHS");
		String date = "2016-09-05";
		stockParseTHS.genTHSStocksChromeUrl(date);
	}
	
	@Test
	public void testPersistIndexHistFromHtml(){
		StockParseToolTHS stockParseTHS = (StockParseToolTHS) applicationContext
		.getBean("stockParseToolTHS");
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHotHistory/2016/09/05");
		stockParseTHS.persistIndexHistFromHtml(dir);
	}
}
