/**
 * 
 */
package com.wy.stock.job;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wy.stock.tools.StockParseToolTHS;
import com.wy.stock.utils.StockConstant;

/**
 * @author leslie
 *
 */
public class TestPersistBoardHotTHSJob {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"conf/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testPersistBoardHot(){
		PersistBoardHotTHSJob persistBoardHotTHSJob = (PersistBoardHotTHSJob) applicationContext
				.getBean("persistBoardHotTHSJob");
		persistBoardHotTHSJob.persistBoardHot();
	}
	
	@Test
	public void persistOldFiles(){
		StockParseToolTHS stockParseToolTHS = (StockParseToolTHS) applicationContext
		.getBean("stockParseToolTHS");
		
		int count = 1;
		while (count < 2){
			String datePath = buildDatePath(count);
			File notionHtml = new File(StockConstant.BOARD_HOT_FILE_PATH + datePath + StockConstant.NOTION_HOT_HTML_FILE);
			File industryHtml = new File(StockConstant.BOARD_HOT_FILE_PATH + datePath + StockConstant.INDUSTRY_HOT_HTML_FILE);
			/*
			 * 解析概念、行业板块热点html文件并登记
			 */
			stockParseToolTHS.persistNotionIndustryHot(notionHtml, "NOTION");
			stockParseToolTHS.persistNotionIndustryHot(industryHtml, "INDUSTRY");
		}
	}
	
	private String buildDatePath(int count){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -count);
		
		return cal.get(Calendar.YEAR) + "/" 
		+ StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0") +"/"
		+ StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0") + "/";
	}
}
