/**
 * Project Name:stanalyse  
 * File Name:AnalyseLhbController.java  
 * Package Name:com.wy.stock.controller  
 * Date:Jan 12, 2018 9:49:01 PM  
 * Copyright (c) 2018, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.stock.tools.AnalyseLhbTool;

/**
 * date: Jan 12, 2018 9:49:01 PM <br/>  
 *  
 * @author leslie  
 * @version   
 * @since version 1.0  
 */
@RestController
@RequestMapping(value = "/lhb")
public class AnalyseLhbController {

	private static Logger LOGGER = Logger.getLogger(AnalyseLhbController.class
			.getName());

	@Autowired
	private AnalyseLhbTool analyseLhbTool;

	@RequestMapping(value = "/statics", method = RequestMethod.GET)
	public String queryLhbStatics(
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "callback", required = true) String callback) {
		LOGGER.info("from: " + from + " to: " + to + " callback: " + callback);
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			LOGGER.error("from or to must not be blank, return now...");
			return null;
		}

		List<Map<String, String>> sb = analyseLhbTool.queryLhbStatics(from, to);
		/*
		 * String result = sb == null ? "null" : sb.toString();
		 * LOGGER.info(result);
		 * 
		 * JSONObject json = new JSONObject(); json.put("notionHot", result);
		 * 
		 * return callback + "(" + json.toString() + ")";
		 */
		return "";
	}
	
	public String queryAggregate(
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "callback", required = true) String callback) {
		LOGGER.info("from: " + from + " to: " + to + " callback: " + callback);
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			LOGGER.error("from or to must not be blank, return now...");
			return null;
		}

		Map<String, Map<String, String>> sb = analyseLhbTool.queryAggregate(from, to);
		/*
		 * String result = sb == null ? "null" : sb.toString();
		 * LOGGER.info(result);
		 * 
		 * JSONObject json = new JSONObject(); json.put("notionHot", result);
		 * 
		 * return callback + "(" + json.toString() + ")";
		 */
		return "";
	}

	public AnalyseLhbTool getAnalyseLhbTool() {
		return analyseLhbTool;
	}

	public void setAnalyseLhbTool(AnalyseLhbTool analyseLhbTool) {
		this.analyseLhbTool = analyseLhbTool;
	}

}
