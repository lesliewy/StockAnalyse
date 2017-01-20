/**
 * 
 */
package com.wy.stock.tools;

import java.sql.Timestamp;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wy.stock.service.CandlestickService;

/**
 * @author leslie
 *
 */
public class TestCandlestickService {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-dev.xml"); // 实例化
	}
	
	@Test
	public void testMorningStarAnalyse(){
		CandlestickService candlestickService = (CandlestickService) applicationContext
				.getBean("candlestickService");
		String lowestTimeStr = "2013-01-01 00:00:00";
		Timestamp lowestTime = Timestamp.valueOf(lowestTimeStr);
		candlestickService.morningStarAnalyse(lowestTime);
	}
	
	@Test
	public void testDuskStarAnalyse(){
		CandlestickService candlestickService = (CandlestickService) applicationContext
				.getBean("candlestickService");
		String lowestTimeStr = "2013-01-01 00:00:00";
		Timestamp lowestTime = Timestamp.valueOf(lowestTimeStr);
		candlestickService.duskStarAnalyse(lowestTime);
	}
	
	@Test
	public void testCrossStarAnalyse(){
		CandlestickService candlestickService = (CandlestickService) applicationContext
				.getBean("candlestickService");
		String lowestTimeStr = "2013-01-01 00:00:00";
		Timestamp lowestTime = Timestamp.valueOf(lowestTimeStr);
		candlestickService.crossStarAnalyse(lowestTime);
	}
	
	@Test
	public void testShootingStarAnalyse(){
		CandlestickService candlestickService = (CandlestickService) applicationContext
				.getBean("candlestickService");
		candlestickService.shootingStarAnalyse();
	}
	
	@Test
	public void testPregnantLineAnalyse(){
		CandlestickService candlestickService = (CandlestickService) applicationContext
				.getBean("candlestickService");
		String lowestTimeStr = "2013-01-01 00:00:00";
		Timestamp lowestTime = Timestamp.valueOf(lowestTimeStr);
		candlestickService.pregnantLineAnalyse(lowestTime);
	}
	
	@Test
	public void testBlackCloudCoverAnalyse(){
		CandlestickService candlestickService = (CandlestickService) applicationContext
				.getBean("candlestickService");
		candlestickService.blackCloudCoverAnalyse();
	}
	
}
