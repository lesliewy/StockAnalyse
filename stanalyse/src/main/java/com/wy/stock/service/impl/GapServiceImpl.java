/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.wy.stock.domain.CandleLine;
import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.StockInfo;
import com.wy.stock.service.GapService;
import com.wy.stock.service.StockHistoryService;
import com.wy.stock.service.StockInfoService;
import com.wy.stock.utils.StockUtils;

/**
 * 缺口分析
 * @author leslie
 *
 */
public class GapServiceImpl implements GapService{

	private static Logger LOGGER = Logger.getLogger(GapServiceImpl.class.getName());
	
	private StockHistoryService stockHistoryService;
	
	private StockInfoService stockInfoService;
	
	public void gapAnalyse() {
		// 查询所有的公司.
		List<StockInfo> allStocks = stockInfoService.queryAllStockInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		Float gapDiff = 3.0f;
		StringBuilder sb = new StringBuilder("\nGaps \n");
		for(StockInfo stock : allStocks){
			sb.append(gapAnalyseSingle(stock.getCode(), stock.getExchange(), stock.getType(), gapDiff));
		}
		LOGGER.info(sb.toString());
	}
	
	private StringBuilder gapAnalyseSingle(String code, String exchange, String type, Float gapDiff){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool10 = false;
		boolean bool11 = false;
		boolean bool20 = false;
		boolean bool21 = false;
		boolean bool30 = false;
		boolean bool31 = false;
		boolean bool40 = false;
		boolean bool41 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 1; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i+1));
			
			if(line0.getType().equals("A") && line1.getType().equals("A")){
				/*
				 *  都是阳线情况.
				 */
				// 上升
				bool10 = line1.getOpen() > line0.getClose();
				// 下降
				bool11 = line1.getClose() < line0.getOpen();
			}else if(line0.getType().equals("A") && line1.getType().equals("B")){
				/*
				 *  阳-阴情况
				 */
				// 上升
				bool20 = line1.getClose() > line0.getClose();
				// 下降
				bool21 = line1.getOpen() < line0.getOpen();
			}else if(line0.getType().equals("B") && line1.getType().equals("A")){
				/*
				 *  阴-阳情况
				 */
				// 上升
				bool30 = line1.getOpen() > line0.getOpen();
				// 下降
				bool31 = line1.getClose() < line0.getClose();
			}else if(line0.getType().equals("B") && line1.getType().equals("B")){
				/*
				 *  阴-阴情况
				 */
				// 上升
				bool40 = line1.getClose() > line1.getOpen();
				// 下降
				bool41 = line1.getOpen() < line0.getClose();
				
			}
			
			if(bool10){
				sb.append("阳-阳: 上升 ");
			}else if(bool11){
				sb.append("阳-阳: 下降 ");
			}else if(bool20){
				sb.append("阳-阴: 上升 ");
			}else if(bool21){
				sb.append("阳-阴: 下降 ");
			}else if(bool30){
				sb.append("阴-阳: 上升 ");
			}else if(bool31){
				sb.append("阴-阳: 下降 ");
			}else if(bool40){
				sb.append("阴-阴: 上升 ");
			}else if(bool41){
				sb.append("阴-阴: 下降 ");
			}
			if(bool10 || bool11 || bool20 || bool21 || bool30 || bool31 || bool40 ||bool41){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
		}
		return sb;
	}

	public StockHistoryService getStockHistoryService() {
		return stockHistoryService;
	}

	public void setStockHistoryService(StockHistoryService stockHistoryService) {
		this.stockHistoryService = stockHistoryService;
	}

	public StockInfoService getStockInfoService() {
		return stockInfoService;
	}

	public void setStockInfoService(StockInfoService stockInfoService) {
		this.stockInfoService = stockInfoService;
	}

}
