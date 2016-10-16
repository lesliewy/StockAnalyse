/**
 * 
 */
package com.wy.stock.tools;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class TestGenStockTool {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"conf/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testGenIndustryHotCsv(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		int year = 2016;
		analyseStockTool.genIndustryHotCsv(year);
	}
	
	@Test
	public void testGenNotionHotCsv(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		int year = 2016;
		analyseStockTool.genNotionHotCsv(year);
	}
	
	@Test
	public void testGenIndustryHotStocksCsv(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		int year = 2016;
		analyseStockTool.genIndustryHotStockCsv(year);
	}
	
	@Test
	public void testGenNotionHotStockCsv(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		int year = 2016;
		analyseStockTool.genNotionHotStockCsv(year);
	}
	
	@Test
	public void testGetIndustryHotPhraseString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimit();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	@Test
	public void testGetNotionHotPhraseString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimit();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	@Test
	public void testGetIndustryHotPhraseAddString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimitAdd();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	@Test
	public void testGetNotionHotPhraseAddString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimitAdd();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	@Test
	public void testGetIndustryHotStocksPhraseString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimit();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotStocksPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	@Test
	public void testGetNotionHotStocksPhraseString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimit();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotStocksPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	@Test
	public void testGetIndustryHotStocksPhraseAddString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimitAdd();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotStocksPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	@Test
	public void testGetNotionHotStocksPhraseAddString(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		List<String> list = StockUtils.getTradeDateLimitAdd();
		
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotStocksPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
}
