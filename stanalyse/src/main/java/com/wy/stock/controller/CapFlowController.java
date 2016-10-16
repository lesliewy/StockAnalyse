/**
 * 
 */
package com.wy.stock.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.stock.domain.StockCapFlow;
import com.wy.stock.service.StockCapFlowService;

/**
 * Rest 控制器, 查询个股资金流向.  RestController Spring默认返回的是JSON格式.
 * @author leslie
 */
@RestController
@RequestMapping(value = "/capFlow")
public class CapFlowController {
	
	private static Logger LOGGER = Logger.getLogger(CapFlowController.class
			.getName());
	
	@Autowired
	private StockCapFlowService stockCapFlowService;

	/**
	 * 查询给定日期、code的资金流向情况.
	 * 查询给定日期前后20天.
	 * @param tradeDateStr
	 * @param code
	 * @return
	 */
    @RequestMapping(value="/stockCap", method = RequestMethod.GET)
    public List<StockCapFlow> queryByDateCode(@RequestParam(value = "tradeDate", required=false) String tradeDateStr,
    		@RequestParam(value = "code", required=true) String code) {
    	// 默认取当前日期
    	Timestamp tradeDate = null;
    	Calendar now = Calendar.getInstance();
    	LOGGER.info("code: " + code + " tradeDateStr: " + tradeDateStr);
    	if(StringUtils.isEmpty(tradeDateStr)){
    		tradeDate = Timestamp.valueOf(new SimpleDateFormat("YYYY-MM-dd").format(now.getTime()) + " 15:00:00");
    	}else{
    		tradeDate = Timestamp.valueOf(tradeDateStr);
    	}
    	LOGGER.info("tradeDate: " + tradeDate);
    	/*
    	 * 设置前后30天时间.
    	 */
    	Calendar calendarLower = Calendar.getInstance();
    	calendarLower.setTimeInMillis(tradeDate.getTime());
    	calendarLower.add(Calendar.DAY_OF_MONTH, -30);
    	
    	Calendar calendarUpper = Calendar.getInstance();
    	calendarUpper.setTimeInMillis(tradeDate.getTime());
    	calendarUpper.add(Calendar.DAY_OF_MONTH, 30);
    	Timestamp tradeDateLower = Timestamp.valueOf(new SimpleDateFormat("YYYY-MM-dd").format(calendarLower.getTime()) + " 15:00:00");
    	Timestamp tradeDateUpper = Timestamp.valueOf(new SimpleDateFormat("YYYY-MM-dd").format(calendarUpper.getTime()) + " 15:00:00");
    	
    	LOGGER.info("stockCapFlowService: " + stockCapFlowService);
    	List<StockCapFlow> capFlowList = stockCapFlowService.queryCapFlowByDateRange(tradeDateLower, tradeDateUpper, code);
    	return capFlowList;
    }

}
