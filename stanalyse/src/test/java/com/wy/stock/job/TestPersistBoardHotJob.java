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
public class TestPersistBoardHotJob {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"conf/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testPersistBoardHot(){
		PersistBoardHotJob persistBoardHotJob = (PersistBoardHotJob) applicationContext
				.getBean("persistBoardHotJob");
		persistBoardHotJob.persistBoardHot();
	}
}
