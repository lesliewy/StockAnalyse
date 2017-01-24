/**
 * 
 */
package com.wy.stock.tools;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author leslie
 *
 */
public class TestStockMovingAvgTool {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-dev.xml"); // 实例化
	}
	
	@Test
	public void testCalcMovingAvg(){
		StockMovingAvgTool stockMovingAvgTool = (StockMovingAvgTool) applicationContext
				.getBean("stockMovingAvgTool");
		String code = "000002";
		String exchange = "深证";
		String type = "A";
		int period = 5;
		stockMovingAvgTool.calcMovingAvg(code, exchange, type, period);
	}
	
	@Test
	public void testCalcAllMovingAvg(){
		StockMovingAvgTool stockMovingAvgTool = (StockMovingAvgTool) applicationContext
				.getBean("stockMovingAvgTool");
		stockMovingAvgTool.calcAllMovingAvg();
	}
	
	@Test
	public void testAnalyseMovingAvgSequence(){
		StockMovingAvgTool stockMovingAvgTool = (StockMovingAvgTool) applicationContext
				.getBean("stockMovingAvgTool");
		String code = "000035";
		String exchange = "深证";
		String type = "A";
		Integer[] periods = {5, 30, 60};
		String seqType = "空";
		int days = 30;
		stockMovingAvgTool.analyseMovingAvgSequence(code, exchange, type, periods, seqType, days);;
	}
	
}
