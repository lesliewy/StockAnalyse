/**
 * 
 */
package com.wy.stock.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wy.stock.domain.StockCapFlow;

/**
 * @author leslie
 *
 */
public class TestCapFlowController {
	private static ApplicationContext applicationContext = null; // 提供静态ApplicationContext
	static {
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-dev.xml"); // 实例化
	}
	
	@Test
	public void testQueryByDateCode(){
		CapFlowController capFlowController = (CapFlowController) applicationContext
				.getBean("capFlowController");
		String tradeDateStr = "2016-05-01 15:00:00";
		String code = "000001";
		List<StockCapFlow> capFlowList = capFlowController.queryByDateCode(tradeDateStr, code);
		if(capFlowList != null){
			for(StockCapFlow capFlow : capFlowList){
				System.out.println(capFlow);
			}
		}
	}
	
}
