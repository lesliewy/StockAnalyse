/**
 * 
 */
package com.wy.stock.tools;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.StockInfo;
import com.wy.stock.domain.StockMovingAvg;
import com.wy.stock.service.StockHistoryService;
import com.wy.stock.service.StockInfoService;
import com.wy.stock.service.StockMovingAvgService;

/**
 * @author leslie
 *
 */
public class StockMovingAvgToolImpl implements StockMovingAvgTool {

	private static Logger LOGGER = Logger.getLogger(StockMovingAvgToolImpl.class
			.getName());
	
	private StockHistoryService stockHistoryService;
	
	private StockMovingAvgService maService;
	
	private StockInfoService stockInfoService;
	
	/**
	 * 查找均线多头排列或者空头排列。
	 * @param code
	 * @param exchange
	 * @param type
	 * @param periods   均线的周期, 按顺序由小到大排列
	 * @param seqType  多-多头排列;  空-空头排列
	 * @param days  多头排列或空头排列持续的天数.
	 */
	public void analyseMovingAvgSequence(String code, String exchange, String type, Integer[] periods, String seqType, int days){
		// 查询 ST_MA, 按照 tradeDate 排序.
		List<StockMovingAvg> maList	 = maService.queryAllByCode(code, exchange, type);
		if(maList == null || maList.isEmpty()){
			LOGGER.info("maList is blank, return now..." + " " + code + " " + exchange + " " + type);
			return;
		}
		int size = maList.size();
		List<StockMovingAvg> oneDayMaList = new ArrayList<StockMovingAvg>();
		int comparedDays = 0;
		for(int i = 1; i < size; i++ ){
			StockMovingAvg preMa = maList.get(i - 1);
			StockMovingAvg ma = maList.get(i);
			Timestamp tradeDatePre = preMa.getTradeDate();
			Timestamp tradeDate = ma.getTradeDate();
			if(tradeDatePre.compareTo(tradeDate) == 0){
				oneDayMaList.add(preMa);
				continue;
			}
			oneDayMaList.add(preMa);
			if(oneDayMaList.size() > 1 && isSeq(oneDayMaList, periods, seqType)){
				comparedDays++;
			}else{
				comparedDays = 0;
			}
			oneDayMaList.clear();
			
			if(comparedDays == days){
				LOGGER.info(tradeDatePre + " " + code + " " + exchange + " " + type + " " + Arrays.toString(periods) + " " + seqType + " " + days);
				comparedDays--;
			}
		}
		
	}
	
	/**
	 * 判断某个交易日，股票是否按照多头或空头排列.
	 * @param maList  某个股票某日的数据.
	 * @param periods
	 * @param seqType
	 * @return
	 */
	private boolean isSeq(List<StockMovingAvg> maList, Integer[] periods, String seqType){
		int periodsSize = periods.length;
		List<Float> maValueList = new ArrayList<Float>();
		for(int i = 0; i < periodsSize; i++){
			StockMovingAvg ma = getMovingAvgByPeriod(maList, periods[i]);
			if (ma == null){
				return false;
			}
			maValueList.add(ma.getValue());
		}
		// 多头排列时， value越来越小; 空头排列时, value 越来越大.
		return "多".equalsIgnoreCase(seqType) ? isOrder(maValueList, "desc") : 
			("空".equalsIgnoreCase(seqType) ? isOrder(maValueList, "asc") : false);
	}
	
	private boolean isOrder(List<Float> values, String type){
		if(values == null || values.isEmpty()){
			return false;
		}
		int size = values.size();
		if("asc".equalsIgnoreCase(type)){
			for(int i = 0; i < size - 1; i ++){
				Float value1 = values.get(i);
				Float value2 = values.get(i + 1);
				if(value1 > value2){
					return false;
				}
			}
		}else if("desc".equalsIgnoreCase(type)){
			for(int i = 0; i < size - 1; i ++){
				Float value1 = values.get(i);
				Float value2 = values.get(i + 1);
				if(value1 < value2){
					return false;
				}
			}
		}
		return true;
	}
	
	private StockMovingAvg getMovingAvgByPeriod(List<StockMovingAvg> maList, int period){
		for(StockMovingAvg ma : maList){
			int periodInMa = ma.getPeriod();
			if(periodInMa == period){
				return ma;
			}
		}
		return null;
	}
	
	public void calcAllMovingAvg(){
    	// 查询所有的股票信息.
    	List<StockInfo> allStocks = stockInfoService.queryAllStockInfo();
    	if(allStocks == null || allStocks.isEmpty()){
    		LOGGER.info("allStocks is empty, return now...");
    		return;
    	}
    	Integer[] periods = new Integer[]{5, 10, 30, 60, 120, 250};
    	// 查询所有的ST_MA, 已经处理过的就跳过.
    	List<StockMovingAvg> allMovingAvgs = maService.queryAllDistinct();
    	List<String> allMovingAvgFlags = translate2String(allMovingAvgs);
    	int num = 0;
    	for(StockInfo stock : allStocks){
    		String code = stock.getCode();
    		String exchange = stock.getExchange();
    		String type = stock.getType();
    		for(Integer period : periods){
    			if(allMovingAvgFlags.contains(code + "_" + exchange + "_" + type + "_" + period)){
    				continue;
    			}
    			LOGGER.info(num++ + " process " + " " + code + " " + exchange + " " + type + " " + period);
    			calcMovingAvg(code, exchange, type, period);
    		}
    	}
	}
	
	private List<String> translate2String(List<StockMovingAvg> allMovingAvgs){
		List<String> result = new ArrayList<String>();
		if(allMovingAvgs == null || allMovingAvgs.isEmpty()){
			return result;
		}
		for(StockMovingAvg ma : allMovingAvgs){
			result.add(ma.getCode() + "_" + ma.getExchange() + "_" + ma.getType() + "_" + ma.getPeriod());
		}
		return result;
	}
	
	public void calcMovingAvg(String code, String exchange, String type, int period){
		// 从ST_HISTORY中查询该股票，按照交易日期排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null or empty, return now..." + code + " " + exchange + " " + type);
			return;
		}
		// 先删除该股票该周期下的数据.
		maService.deleteByCodePeriod(code, exchange, type, period);
		
		int size = stocks.size();
		Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
		List<StockMovingAvg> maList = new ArrayList<StockMovingAvg>();
		for(int i = period - 1; i < size; i++){
			Float sum = calcuSum(stocks, i - period + 1, i);
			Timestamp tradeDate = stocks.get(i).getTradeDate();
			if(sum == null || sum < 0.5){
				continue;
			}
			Float value = sum / period;
			StockMovingAvg ma = new StockMovingAvg();
			ma.setCode(code);
			ma.setExchange(exchange);
			ma.setType(type);
			ma.setPeriod(period);
			ma.setTradeDate(tradeDate);
			ma.setValue(value);
			ma.setTimestamp(timestamp);
			maList.add(ma);
		}
		maService.insertStockMovingAvgBatch(maList);
	}
	
	/**
	 * 计算指定区间中收盘价的总和. [begin, end]
	 * @param stocks
	 * @param begin
	 * @param end
	 * @return
	 */
	private Float calcuSum(List<StockHistory> stocks, int begin, int end){
		if(begin < 0 || end < 0 || begin > end){
			return null;
		}
		int size = stocks.size();
		if(size < end){
			return null;
		}
		Float sum = 0f;
		for(int i = begin; i <= end; i++){
			Float close = stocks.get(i).getClose();
			sum += close;
		}
		return sum;
	}

	public StockHistoryService getStockHistoryService() {
		return stockHistoryService;
	}

	public void setStockHistoryService(StockHistoryService stockHistoryService) {
		this.stockHistoryService = stockHistoryService;
	}

	public StockMovingAvgService getMaService() {
		return maService;
	}

	public void setMaService(StockMovingAvgService maService) {
		this.maService = maService;
	}

	public StockInfoService getStockInfoService() {
		return stockInfoService;
	}

	public void setStockInfoService(StockInfoService stockInfoService) {
		this.stockInfoService = stockInfoService;
	}
	
}
