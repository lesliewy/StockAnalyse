/**
 * 
 */
package com.wy.stock.controller;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author leslie
 *
 */
public class TestAnalyseLhbController {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"development/applicationContext.xml"); // 实例化
	}

	@Test
	public void testQueryByDateCode() {
		AnalyseLhbController analyseLhbController = (AnalyseLhbController) applicationContext
				.getBean("analyseLhbController");
		String from = "2017-12-01 15:00:00";
		String to = "2017-12-30 15:00:00";
		String callback = "000001";
		String a = analyseLhbController.queryLhbStatics(from, to, callback);
	}

	@Test
	public void testQueryAggregate() {
		AnalyseLhbController analyseLhbController = (AnalyseLhbController) applicationContext
				.getBean("analyseLhbController");
		String from = "2017-12-01 15:00:00";
		String to = "2017-12-30 15:00:00";
		String callback = "000001";
		String a = analyseLhbController.queryAggregate(from, to, callback);
	}
}
