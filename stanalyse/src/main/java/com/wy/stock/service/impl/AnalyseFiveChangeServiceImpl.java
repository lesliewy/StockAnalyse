/**
 * 
 */
package com.wy.stock.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wy.stock.domain.IndustryStock;
import com.wy.stock.domain.NotionStock;
import com.wy.stock.domain.StockFiveChange;
import com.wy.stock.service.AnalyseFiveChangeService;
import com.wy.stock.service.IndustryStockService;
import com.wy.stock.service.NotionStockService;
import com.wy.stock.service.StockFiveChangeService;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class AnalyseFiveChangeServiceImpl implements AnalyseFiveChangeService {

	private static Logger LOGGER = Logger
			.getLogger(AnalyseFiveChangeServiceImpl.class.getName());
	
	private NotionStockService notionStockService;
	
	private IndustryStockService industryStockService;
	
	private StockFiveChangeService fiveChangeService;
	
	public void analyseFiveChange() {
		LOGGER.info("analyseFiveChange begin...");
		Map<String, String> stockNotionsMap = getStockNotionNamesMap();
		Map<String, String> stockIndustrysMap = getStockIndustryNamesMap();
		Map<String, Integer> notionNameNumMap = new HashMap<String, Integer>();
		Map<String, Integer> industryNameNumMap = new HashMap<String, Integer>();
		// 查询 ST_STOCK_FIVE_CHANGE
		List<StockFiveChange> fiveChangeList = fiveChangeService.queryStockFiveChange();
		StringBuilder sb = new StringBuilder(Calendar.getInstance().getTime().toString() + ": \n");
		if(fiveChangeList != null && !fiveChangeList.isEmpty()){
			sb.append("Notion: \n");
			int j = 0;
			for(StockFiveChange fiveChange : fiveChangeList){
				j++;
				String code = fiveChange.getCode();
				String stockName = fiveChange.getStockName();
				Float changePercent = fiveChange.getChangePercent();
				Float fiveChangePercent = fiveChange.getFiveChangePercent();
				String notionNames = stockNotionsMap.get(code);
				
				// 详情显示前20个.
				if( j <= 20){
					Formatter formatter = new Formatter();
					formatter.format("%-7s %-8s %-6s %-6s %-10s\n", code, stockName, changePercent, fiveChangePercent,notionNames);
					sb.append(formatter.toString());
					formatter.close();
				}
				
				// 计数
				if(notionNames != null){
					String[] strs = notionNames.split("\\|");
					for(int i = 0; i < strs.length; i++){
						if(notionNameNumMap.containsKey(strs[i])){
							notionNameNumMap.put(strs[i], notionNameNumMap.get(strs[i]) + 1);
						}else{
							notionNameNumMap.put(strs[i], 1);
						}
					}
				}
			}
			List<Map.Entry<String,Integer>> sortList=new ArrayList<Map.Entry<String,Integer>>();
			sortList.addAll(notionNameNumMap.entrySet());  
	        Collections.sort(sortList, StockUtils.descMapComparatorInteger); 
			sb.append("Notion Summary: \n");
			sb.append(sortList.toString() + "\n");
			sb.append("\n");
			
			sb.append("Industry: \n");
			j = 0;
			for(StockFiveChange fiveChange : fiveChangeList){
				j++;
				String code = fiveChange.getCode();
				String stockName = fiveChange.getStockName();
				Float changePercent = fiveChange.getChangePercent();
				Float fiveChangePercent = fiveChange.getFiveChangePercent();
				String industryName = stockIndustrysMap.get(code);
				
				if( j <= 20){
					Formatter formatter = new Formatter();
					formatter.format("%-7s %-8s %-6s %-6s %-10s\n", code, stockName, changePercent, fiveChangePercent, industryName);
					sb.append(formatter.toString());
					formatter.close();
				}
				
				// 计数
				if(!StringUtils.isBlank(industryName)){
					if(industryNameNumMap.containsKey(industryName)){
						industryNameNumMap.put(industryName, industryNameNumMap.get(industryName) + 1);
					}else{
						industryNameNumMap.put(industryName, 1);
					}
				}
			}
			sortList.clear();
			sortList.addAll(industryNameNumMap.entrySet());  
	        Collections.sort(sortList, StockUtils.descMapComparatorInteger); 
			sb.append("Industry Summary: \n");
			sb.append(sortList.toString() + "\n");
			sb.append("\n");
		}
		
		
		File logFile = new File(StockUtils.getDailyStockSaveDir("B") + "fiveChange.log");
		StockUtils.persistByStr(logFile, sb.toString(), true);
		
		LOGGER.info("analyseFiveChange end, log file: " + logFile.getAbsolutePath());
	}
	
	private Map<String, String> getStockNotionNamesMap(){
		Map<String, String> result = new HashMap<String, String>();
		List<NotionStock> notionStockList = notionStockService.queryNotionCodesNames(StockConstant.THS_FLAG);
		if(notionStockList != null && !notionStockList.isEmpty()){
			for(NotionStock notionStock : notionStockList){
				String code = notionStock.getCode();
				result.put(code, notionStock.getNotionNames());
			}
		}
		return result;
	}
	
	private Map<String, String> getStockIndustryNamesMap(){
		Map<String, String> result = new HashMap<String, String>();
		List<IndustryStock> industryStockList = industryStockService.queryAllIndustryStock(StockConstant.THS_FLAG);
		if(industryStockList != null && !industryStockList.isEmpty()){
			for(IndustryStock industryStock : industryStockList){
				String code = industryStock.getCode();
				result.put(code, industryStock.getIndustryName());
			}
		}
		return result;
	}

	public NotionStockService getNotionStockService() {
		return notionStockService;
	}

	public void setNotionStockService(NotionStockService notionStockService) {
		this.notionStockService = notionStockService;
	}

	public IndustryStockService getIndustryStockService() {
		return industryStockService;
	}

	public void setIndustryStockService(IndustryStockService industryStockService) {
		this.industryStockService = industryStockService;
	}

	public StockFiveChangeService getFiveChangeService() {
		return fiveChangeService;
	}

	public void setFiveChangeService(StockFiveChangeService fiveChangeService) {
		this.fiveChangeService = fiveChangeService;
	}
	
}
