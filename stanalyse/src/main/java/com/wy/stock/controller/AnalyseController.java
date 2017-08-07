/**
 * 
 */
package com.wy.stock.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.stock.tools.AnalyseStockTool;

/**
 * Rest 控制器, 查询时间段内的概念、行业涨跌幅排名.  RestController Spring默认返回的是JSON格式.
 * @author leslie
 */
@RestController
@RequestMapping(value = "/analyse")
public class AnalyseController {

	private static Logger LOGGER = Logger.getLogger(AnalyseController.class
			.getName());

	@Autowired
	private AnalyseStockTool analyseStockTool;

	/**
	 * 查询给定时间段内的概念指数涨跌幅排名.
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping(value = "/notionHot", method = RequestMethod.GET)
	public String queryNotionHotBetween(
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "callback", required = true) String callback) {
		LOGGER.info("from: " + from + " to: " + to + " callback: " + callback);
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			LOGGER.error("from or to must not be blank, return now...");
			return null;
		}

		StringBuilder sb = analyseStockTool.getNotionHotPhraseString(from, to,
				false);
		String result = sb == null ? "null" : sb.toString();
		LOGGER.info(result);

		JSONObject json = new JSONObject();
		json.put("notionHot", result);

		// Angular 的 jsonp 需要这样回传.
		return callback + "(" + json.toString() + ")";
	}

	/**
	 * 查询给定时间段内的行业指数涨跌幅排名.
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping(value = "/industryHot", method = RequestMethod.GET)
	public String queryIndustryHotBetween(
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "callback", required = true) String callback) {
		LOGGER.info("from: " + from + " to: " + to + " callback: " + callback);
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			LOGGER.error("from or to must not be blank, return now...");
			return null;
		}

		StringBuilder sb = analyseStockTool.getIndustryHotPhraseString(from, to,
				false);
		String result = sb == null ? "null" : sb.toString();
		LOGGER.info(result);

		JSONObject json = new JSONObject();
		json.put("industryHot", result);

		// Angular 的 jsonp 需要这样回传.
		return callback + "(" + json.toString() + ")";
	}

	/**
	 * 查询给定时间段内的概念个股涨跌幅排名.
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping(value = "/notionHotStocks", method = RequestMethod.GET)
	public String queryNotionHotStocksBetween(
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "callback", required = true) String callback) {
		LOGGER.info("from: " + from + " to: " + to + " callback: " + callback);
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			LOGGER.error("from or to must not be blank, return now...");
			return null;
		}

		StringBuilder sb = analyseStockTool.getNotionHotStocksPhraseString(from,
				to, false);
		String result = sb == null ? "null" : sb.toString();
		LOGGER.info(result);

		JSONObject json = new JSONObject();
		json.put("notionHotStocks", result);

		// Angular 的 jsonp 需要这样回传.
		return callback + "(" + json.toString() + ")";
	}

	/**
	 * 查询给定时间段内的行业个股涨跌幅排名.
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping(value = "/industryHotStocks", method = RequestMethod.GET)
	public String queryIndustryHotStocksBetween(
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "callback", required = true) String callback) {
		LOGGER.info("from: " + from + " to: " + to + " callback: " + callback);
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			LOGGER.error("from or to must not be blank, return now...");
			return null;
		}

		StringBuilder sb = analyseStockTool.getIndustryHotStocksPhraseString(
				from, to, false);
		String result = sb == null ? "null" : sb.toString();
		LOGGER.info(result);

		JSONObject json = new JSONObject();
		json.put("industryHotStocks", result);

		// Angular 的 jsonp 需要这样回传.
		return callback + "(" + json.toString() + ")";
	}
}
