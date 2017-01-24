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
public class TestStockDownloadTool {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-dev.xml"); // 实例化
	}
	
	@Test
	public void testDownStockHist(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
		File savedDir = new File(StockUtils.getDailyStockSaveDir("H"));
		stockDownload.downloadCsvFiles(savedDir);
	}
	
	@Test
	public void testDownloadBoardHotStocksFiles(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
		
		stockDownload.downloadBoardHotStocksFiles("151021", "NOTION");
		stockDownload.downloadBoardHotStocksFiles("151021", "INDUSTRY");
	}
	
	@Test
	public void testDownloadIndexFiles(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
		stockDownload.downloadIndexFiles();
	}
	
	@Test
	public void testDownloadBoardHotFilesAll(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
		File dir = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/2015/10/23/");
		stockDownload.downloadBoardHotFilesAll(dir, "INDUSTRY");
	}
	
	@Test
	public void testDownloadStockFiveChange(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
//		String type = "10";
		String type = "20";
		stockDownload.downloadStockFiveChange(type);
	}
	
	@Test
	public void testDownloadStockCapFlow(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
		File savedDir =  new File(StockUtils.getDailyStockSaveDir("C"));
		stockDownload.downloadStockCapFlow(savedDir);
	}
	
	@Test
	public void testDownloadStockCapFlowHist(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
		File savedDir =  new File(StockUtils.getDailyStockSaveDir("C"));
		stockDownload.downloadStockCapFlowHist(savedDir, "600736");
	}
	
	@Test
	public void testDownloadStockCapFlowDetail(){
		StockDownloadTool stockDownload = (StockDownloadTool) applicationContext
				.getBean("stockDownloadTool");
		File savedDir =  new File(StockUtils.getDailyStockSaveDir("C"));
		stockDownload.downloadStockCapFlowDetail(savedDir, "000001");
	}
}
