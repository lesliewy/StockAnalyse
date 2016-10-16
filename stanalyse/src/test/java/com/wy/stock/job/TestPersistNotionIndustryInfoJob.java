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
public class TestPersistNotionIndustryInfoJob {

	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"conf/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testpersistNotionIndustryInfo(){
		PersistNotionIndustryInfoJob persistNotionIndustryInfoJob = (PersistNotionIndustryInfoJob) applicationContext
				.getBean("persistNotionIndustryInfoJob");
		persistNotionIndustryInfoJob.persistNotionIndustryInfo();
	}

}
