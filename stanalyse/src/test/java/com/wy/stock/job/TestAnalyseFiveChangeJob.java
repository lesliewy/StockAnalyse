/**
 * 
 */
package com.wy.stock.job;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author leslie
 *
 */
public class TestAnalyseFiveChangeJob {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-dev.xml"); // 实例化
	}
	
	@Test
	public void testAnalyseFiveChange(){
		AnalyseFiveChangeJob analyseFiveChangeJob = (AnalyseFiveChangeJob) applicationContext
				.getBean("analyseFiveChangeJob");
		analyseFiveChangeJob.analyseFiveChange();
	}
}
