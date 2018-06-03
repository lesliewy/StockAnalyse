/**
 * Project Name:stanalyse  
 * File Name:TestPersistLhbJob.java  
 * Package Name:com.wy.stock.job  
 * Date:Dec 25, 2017 9:52:16 PM  
 * Copyright (c) 2017, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.job;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * date: Dec 25, 2017 9:52:16 PM <br/>  
 *  
 * @author leslie  
 * @version   
 * @since version 1.0  
 */
public class TestPersistLhbJob {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"development/applicationContext.xml"); // 实例化
	}
	
	@Test
	public void testPersistLHB(){
		PersistLhbJob persistLhbJob = (PersistLhbJob) applicationContext
				.getBean("persistLhbJob");
//		persistLhbJob.persistLHB();
	}
	
	@Test
	public void testPersistLHBBefore(){
		PersistLhbJob persistLhbJob = (PersistLhbJob) applicationContext
				.getBean("persistLhbJob");
		String month = "1804";
		for(int i = 1; i <= 4; i++){
			String jobDate = month + StringUtils.leftPad(String.valueOf(i), 2, "0");
			persistLhbJob.persistLHB(jobDate);
		}
	}
}
