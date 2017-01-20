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
public class TestStockParseTool {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-dev.xml"); // 实例化
	}
	
	@Test
	public void testGetStockInfo(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		stockParse.getStockInfo();
	}
	
	@Test
	public void testPaseAllHistCsv(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		File dir = new File(StockUtils.getDailyStockSaveDir("H"));
		stockParse.parseAllHistCsv(dir);
	}
	
	@Test
	public void testDownloadParseBoardCode(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		String source = "DFCF";
		stockParse.downloadParseBoardCode(source);
	}
	
	@Test
	public void testParseNotionHotFromJson(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		
		File jsonFile = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/2015/10/18/notionHot.json");
		stockParse.parseNotionHotFromJson(jsonFile);
	}
	
	@Test
	public void testPersistNotionHotStocksFromJson(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		stockParse.persistNotionHotStocksFromJson("151021");
	}
	
	@Test
	public void testPersistIndustryHotStocksFromJson(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		
		stockParse.persistIndustryHotStocksFromJson("151021");
	}
	
	@Test
	public void testParseIndexFromJson(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		stockParse.parseIndexFromJson(new File(StockUtils.getDailyStockSaveDir("B") + "index.json"));
	}
	
	@Test
	public void testPersistIndexFromJson(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		stockParse.persistIndexFromJson(new File(StockUtils.getDailyStockSaveDir("B") + "index.json"));
	}
	
	@Test
	public void testPersistNotionIndustryStockFromJson(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/2015/10/23/");
//		stockParse.persistNotionIndustryStockFromJson(dir, "NOTION");
		stockParse.persistNotionIndustryStockFromJson(dir, "INDUSTRY");
	}
	
	@Test
	public void testPersistStockFiveChange(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/2015/10/24/");
		stockParse.persistStockFiveChange(dir);
	}
	
	@Test
	public void testPersistStockCapFlow(){
		StockParseTool stockParse = (StockParseTool) applicationContext
				.getBean("stockParseTool");
		File capFlowFile = new File("/home/leslie/MyProject/StockAnalyse/html/capFlow/2016/04/23/capFlow_20160423.json");
		stockParse.parseStockCapFlow(capFlowFile);
	}
	
}
