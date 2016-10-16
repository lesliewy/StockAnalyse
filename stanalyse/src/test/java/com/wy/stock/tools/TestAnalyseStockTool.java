/**
 * 
 */
package com.wy.stock.tools;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wy.stock.domain.AnalyseCondition;

/**
 * @author leslie
 *
 */
public class TestAnalyseStockTool {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"conf/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testAnalyseAccumuChangePct(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		AnalyseCondition condition = new AnalyseCondition();
		// 分析日期
		Timestamp tradeDate = Timestamp.valueOf("2016-05-03 15:00:00");
		
		// 主力净流入的上下限
		BigDecimal capFlowLower = null;
		BigDecimal capFlowUpper = null;
		capFlowLower = new BigDecimal("-2000");
		capFlowUpper = new BigDecimal("-500");
		
		// 股票类型 0-深证  6-上证  3-创业板.
		List<Integer> types = new ArrayList<Integer>();
		types.add(0);
		types.add(6);
		
		// 需要排除的行业. 如果没有则默认排除了：银行、房地产开发、电力等.
		List<String> excludeIndustries = null;
		
		// 需要排除的文件名，包含该字符串就排除掉
		List<String> excludeName = null;
		excludeName = new ArrayList<String>();
		excludeName.add("ST");
		
		// 当日涨跌幅的上下限.
		Float changePctUpper = null;
		Float changePctLower = null;
		changePctUpper = 2.0f;
		changePctLower = -5.0f;
		
		// 累计几日的涨跌幅上下限.
		Integer accumuDays = null;
		Float accumuDaysChangePctUpper = null;
		Float accumuDaysChangePctLower = null;
		accumuDays = 8;
		accumuDaysChangePctUpper = -0.0f;
		accumuDaysChangePctLower = -15.0f;
		
		condition.setTradeDate(tradeDate);
		condition.setCapFlowLower(capFlowLower);
		condition.setCapFlowUpper(capFlowUpper);
		condition.setTypes(types);
		condition.setAccumuDays(accumuDays);
		condition.setAccumuDaysChangePctLower(accumuDaysChangePctLower);
		condition.setAccumuDaysChangePctUpper(accumuDaysChangePctUpper);
		condition.setExcludeIndustries(excludeIndustries);
		condition.setExcludeName(excludeName);
		condition.setChangePctUpper(changePctUpper);
		condition.setChangePctLower(changePctLower);
		analyseStockTool.analyseAccumuChangePct(condition);
	}

	@Test
	public void testAnalyseAccumuMainNetIn(){
		AnalyseStockTool analyseStockTool = (AnalyseStockTool) applicationContext
				.getBean("analyseStockTool");
		AnalyseCondition condition = new AnalyseCondition();
		// 分析日期
		Timestamp tradeDate = Timestamp.valueOf("2016-05-23 15:00:00");
		
		// 主力净流入的上下限
		BigDecimal capFlowLower = null;
		BigDecimal capFlowUpper = null;
		
		// 股票类型 0-深证  6-上证  3-创业板.
		List<Integer> types = new ArrayList<Integer>();
		types.add(0);
		types.add(6);
		
		// 需要排除的行业. 如果没有则默认排除了：银行、房地产开发、电力等.
		List<String> excludeIndustries = null;
		
		// 需要排除的文件名，包含该字符串就排除掉
		List<String> excludeName = null;
		excludeName = new ArrayList<String>();
		excludeName.add("ST");
		
		// 当日涨跌幅的上下限.
		Float changePctUpper = null;
		Float changePctLower = null;
		
		// 累计几日
		Integer accumuDays = null;
		accumuDays = 10;
		
		// 累计几日的涨跌幅上下限.
		Float accumuDaysChangePctUpper = null;
		Float accumuDaysChangePctLower = null;
		
		// 累计几日的主力净占比上下限
		Float accumuMainNetInPctUpper = null;
		Float accumuMainNetInPctLower = null;
		accumuMainNetInPctUpper = -70.0f;
		accumuMainNetInPctLower = -9999.9f;
		
		condition.setTradeDate(tradeDate);
		condition.setCapFlowLower(capFlowLower);
		condition.setCapFlowUpper(capFlowUpper);
		condition.setTypes(types);
		condition.setAccumuDays(accumuDays);
		condition.setAccumuDaysChangePctLower(accumuDaysChangePctLower);
		condition.setAccumuDaysChangePctUpper(accumuDaysChangePctUpper);
		condition.setExcludeIndustries(excludeIndustries);
		condition.setExcludeName(excludeName);
		condition.setChangePctUpper(changePctUpper);
		condition.setChangePctLower(changePctLower);
		condition.setAccumuMainNetInPctLower(accumuMainNetInPctLower);
		condition.setAccumuMainNetInPctUpper(accumuMainNetInPctUpper);
		analyseStockTool.analyseAccumuMainNetIn(condition);
	}
}
