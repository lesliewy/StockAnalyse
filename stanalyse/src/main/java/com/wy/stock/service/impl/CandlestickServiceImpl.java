/**
 * 
 */
package com.wy.stock.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.wy.stock.domain.CandleLine;
import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.ExchangeInfo;
import com.wy.stock.service.CandlestickService;
import com.wy.stock.service.StockHistoryService;
import com.wy.stock.service.ExchangeInfoService;
import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class CandlestickServiceImpl implements CandlestickService {
	
	private static Logger LOGGER = Logger.getLogger(CandlestickServiceImpl.class.getName());
	
	private StockHistoryService stockHistoryService;
	
	private ExchangeInfoService exchangeInfoService;

	/**
	 * 启明星: 见底转向.
	 */
	public StringBuilder morningStarAnalyse(Timestamp lowestTime) {
		StringBuilder sb = new StringBuilder("\n启明星 \n");
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return sb;
		}

		for(ExchangeInfo stock : allStocks){
			sb.append(morningStarSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTime));
		}
		LOGGER.info(sb.toString());
		return sb;
	}

	/**
	 * 黄昏星: 见顶转向.
	 */
	public StringBuilder duskStarAnalyse(Timestamp lowestTime) {
		StringBuilder sb = new StringBuilder("\n黄昏星 \n");
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return sb;
		}
		for(ExchangeInfo stock : allStocks){
			sb.append(duskStarSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTime));
		}
		LOGGER.info(sb.toString());
		return sb;
	}
	
	/**
	 * 十字星: 趋势反转
	 */
	public StringBuilder crossStarAnalyse(Timestamp lowestTime){
		StringBuilder sb = new StringBuilder("\n十字星 \n");
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return sb;
		}
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(crossStarSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTime));
			}
		}
		LOGGER.info(sb.toString());
		return sb;
	}
	
	/**
	 * 射击星: 见顶转向.
	 */
	public StringBuilder shootingStarAnalyse(){
		StringBuilder sb = new StringBuilder("\n射击星 \n");
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return sb;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(shootingStarSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
		return sb;
	}
	
	/**
	 * 孕线: 趋势反转.
	 */
	public StringBuilder pregnantLineAnalyse(Timestamp lowestTime){
		StringBuilder sb = new StringBuilder("\n孕线 \n");
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return sb;
		}
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(pregnantLineSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTime));
			}
		}
		LOGGER.info(sb.toString());
		return sb;
	}
	
	/**
	 * 乌云盖顶: 见顶转向.
	 */
	public StringBuilder blackCloudCoverAnalyse(){
		StringBuilder sb = new StringBuilder("\n乌云盖顶 \n");
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return sb;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(blackCloudCoverSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
		return sb;
	}
	
	/**
	 * 穿头破脚: 趋势反转
	 */
	public void breakTopBottomAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n穿头破脚 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(breakTopBottomSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 锤头: 见底转向
	 */
	public void hammerAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n锤头 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(hammerSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 吊颈: 见顶转向
	 */
	public void hangLineAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n吊颈 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(hangLineSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 曙光初现: 见底转向.  可比较乌云盖顶.
	 */
	public void dawnAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n曙光初现 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(dawnSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 红三兵: 大市持续上升.
	 */
	public void redThreeAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n红三兵 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(redThreeSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 两只乌鸦: 见顶转向. 
	 */
	public void twoCrowsAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n两只乌鸦 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(twoCrowsSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 三只乌鸦: 见顶转向
	 */
	public void threeCrowsAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n三只乌鸦 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(threeCrowsSingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 上升三部曲: 持续上升
	 */
	public void ascTrilogyAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n上升三部曲 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(ascTrilogySingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 下跌三部曲: 持续下跌
	 */
	public void descTrilogyAnalyse(){
		// 查询所有的公司.
		List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
		if(allStocks == null){
			LOGGER.info("allStocks is null, return now...");
			return;
		}
		String lowestTimeStr = "2013-01-01 00:00:00";
		StringBuilder sb = new StringBuilder("\n下跌三部曲 \n");
		for(ExchangeInfo stock : allStocks){
			if("上证".equals(stock.getExchange()) && "A".equals(stock.getType())){
				sb.append(descTrilogySingle(stock.getCode(), stock.getExchange(), stock.getType(), lowestTimeStr));
			}
		}
		LOGGER.info(sb.toString());
	}
	
	/**
	 * 查询某个股票的启明星标志, 启明星是见底标志.
	 * 包括一般情况和可能性更大的情况.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 */
	private StringBuilder morningStarSingle(String code, String exchange, String type, Timestamp lowestTime){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKeyRange(code, exchange, type, lowestTime);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		boolean bool5 = false;
		boolean bool6 = false;
		boolean enhanceBool1 = false;
		boolean enhanceBool2 = false;
		boolean enhanceBool3 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 2; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			CandleLine firstLine = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine secondLine = StockUtils.translateStock2CandleLine(stocks.get(i+1));
			CandleLine thirdLine = StockUtils.translateStock2CandleLine(stocks.get(i+2));
			
			/*
			 * 一般情况.
			 */
			// 第一根K线是较长的阴线.
			bool1 = firstLine.getClose()/firstLine.getOpen() < 0.95;
			// 第二根低开， 实体部分小， 下影线长.
			bool2 = secondLine.getOpen() < firstLine.getClose();
			bool3 = secondLine.getEntity()/secondLine.getClose() < 0.03;
			bool4 = secondLine.getLowerLine() > secondLine.getEntity() * 2;
			// 第三根 是阳线，收盘价接近于最高价.
			bool5 = thirdLine.getType().equals("A");
			bool6 = thirdLine.getClose()/thirdLine.getHigh() > 0.95;
			
			/*
			 *  启明星正确可能性更高情况
			 */
			// 第一根与第二根之间， 第二根与第三根之间出现缺口.
			enhanceBool1 = secondLine.getOpen() < firstLine.getClose() && thirdLine.getOpen() > secondLine.getClose();
			// 第三根实体部分进入第一根实体部分2／3以上
			enhanceBool2 = thirdLine.getClose() > (firstLine.getClose() + firstLine.getEntity() * 2/3);
			// 第一根K线成交量较小， 第三根成交量较大.
			if(firstLine.getVolumn().compareTo(new BigDecimal("0")) > 0 &&  thirdLine.getVolumn().compareTo(new BigDecimal("0")) > 0){
				enhanceBool3 = firstLine.getVolumn().divide(thirdLine.getVolumn(), RoundingMode.HALF_UP).floatValue() < 0.5;
			}
			
			if(bool1 && bool2 && bool3 && bool4 && bool5 && bool6){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				
				if(enhanceBool1 && enhanceBool2 && enhanceBool3){
					sb.append("enhance");
				}
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 查询某个股票的黄昏星标志, 黄昏星是见顶标志.
	 * 包括一般情况和可能性更大的情况.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 */
	private StringBuilder duskStarSingle(String code, String exchange, String type, Timestamp lowestTime){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKeyRange(code, exchange, type, lowestTime);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		boolean bool5 = false;
		boolean bool6 = false;
		boolean enhanceBool1 = false;
		boolean enhanceBool2 = false;
		boolean enhanceBool3 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 2; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			CandleLine firstLine = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine secondLine = StockUtils.translateStock2CandleLine(stocks.get(i+1));
			CandleLine thirdLine = StockUtils.translateStock2CandleLine(stocks.get(i+2));
			
			/*
			 * 一般情况.
			 */
			// 第一根K线是较长的阳线.
			bool1 = firstLine.getClose()/firstLine.getOpen() > 1.05;
			// 第二根高开， 实体部分小， 有上下影线成星星状.
			bool2 = secondLine.getOpen() > firstLine.getClose();
			bool3 = secondLine.getEntity()/secondLine.getClose() < 0.03;
			bool4 = secondLine.getHigh()/secondLine.getClose() > 1.05 && secondLine.getLow()/secondLine.getOpen() < 0.95;
			// 第三根 是阴线，收盘价接近于最低价.
			bool5 = thirdLine.getType().equals("D");
			bool6 = thirdLine.getClose()/thirdLine.getLow() > 0.95;
			
			/*
			 *  黄昏星正确可能性更高情况
			 */
			// 第一根与第二根之间， 第二根与第三根之间出现缺口.
			enhanceBool1 = secondLine.getOpen() > firstLine.getClose() && thirdLine.getOpen() < secondLine.getClose();
			// 第三根实体部分进入第一根实体部分2／3以上
			enhanceBool2 = thirdLine.getClose() > (firstLine.getOpen() - firstLine.getEntity() * 2/3);
			// 第一根K线成交量较小， 第三根成交量较大.
			if(firstLine.getVolumn().compareTo(new BigDecimal("0")) > 0 &&  thirdLine.getVolumn().compareTo(new BigDecimal("0")) > 0){
				enhanceBool3 = firstLine.getVolumn().divide(thirdLine.getVolumn(), RoundingMode.HALF_UP).floatValue() < 0.5;
			}
			
			if(bool1 && bool2 && bool3 && bool4 && bool5 && bool6){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				
				if(enhanceBool1 && enhanceBool2 && enhanceBool3){
					sb.append("enhance");
				}
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 查询某个股票的十字星标志, 十字星预示可能趋势反转.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 */
	private StringBuilder crossStarSingle(String code, String exchange, String type, Timestamp lowestTime){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKeyRange(code, exchange, type, lowestTime);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 2; i++){
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i + 1));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			String tradeDateStr = stocks.get(i + 1).getTradeDate().toString();
			
			/*
			 * 一般情况.
			 */
			// 收盘价非常接近于开盘价
			bool1 = (line1.getClose() / line1.getOpen() > 0.999) && 
					(line1.getClose() / line1.getOpen() < 1.001);
			// 十字星前后的状态不一致.
			bool2 = !line0.getType().equals(line2.getType());
			// 第三根验证K线的实体较长
			bool3 = (line2.getClose()/line2.getOpen() > 1.03) ||
					(line2.getClose()/line2.getOpen() < 0.97);
			
			if(bool1 && bool2 && bool3){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 射击星表示市势已经失去上升的动力.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 */
	private StringBuilder shootingStarSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		int size = stocks.size();
		for(int i = 0; i < size; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine candleLine = StockUtils.translateStock2CandleLine(stocks.get(i));
			
			/*
			 * 一般情况.
			 */
			// 顶部: 实体短，上影线很长, 下影线很短
			bool1 = candleLine.getClose()/candleLine.getOpen() > 0.98 && candleLine.getClose()/candleLine.getOpen() < 1.02;
			bool2 = candleLine.getHigh()/candleLine.getOpen() > 1.1;
			bool3 = candleLine.getLow()/candleLine.getClose() > 0.98;
			if(bool1 && bool2 && bool3){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
			// 底部: 实体短， 下影线很长，上影线很短.
			
		}
		return sb;
	}
	
	/**
	 * 孕线: 可能趋势反转, 如果第二根K线是个十字星， 则构成十字胎线。 趋势反转可能性更高.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder pregnantLineSingle(String code, String exchange, String type, Timestamp lowestTime){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKeyRange(code, exchange, type, lowestTime);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i + 1));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			String tradeDateStr = stocks.get(i + 4).getTradeDate().toString();
			/*
			 * 一般情况.
			 */
			// 上升势: 第一根阳线较大; 第二根K线实体很小.
			boolean bool0 = line4.getClose() > line2.getClose() && line2.getClose() > line0.getClose();
			bool1 = line4.getType().equals("A") && (line4.getClose()/line4.getOpen() > 1.10);
			bool2 = line5.getClose()/line5.getOpen() > 0.98 && line5.getClose()/line5.getOpen() < 1.02;
			if(bool0 && bool1 && bool2){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
			
			// 下降势: 第一根阴线较大; 第二根K线实体很小.
			boolean bool5 = line0.getType().equals("D") && line1.getType().equals("D");
			bool3 = line4.getType().equals("D") && (line4.getClose()/line4.getOpen() < 0.90);
			bool4 = line5.getClose()/line5.getOpen() > 0.98 && line5.getClose()/line5.getOpen() < 1.02;
			if(bool3 && bool4 && bool5){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
		}
		return sb;
	}
	
	
	/**
	 * 乌云盖顶: 可能见顶回落.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder blackCloudCoverSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		boolean bool5 = false;
		boolean bool6 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			String tradeDateStr1 = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr1.compareTo(lowestTimeStr) < 0){
				continue;
			}
			String tradeDateStr2 = stocks.get(i + 4).getTradeDate().toString();
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			/*
			 * 一般情况.
			 */
			// 事先是上升趋势
			bool1 = line4.getClose() > line2.getClose() && line2.getClose() > line0.getClose();
			// 第一根是阳线，实体部分较长.
			bool2 = line4.getType().equals("A");
			bool3 = (line4.getClose()/line4.getOpen() > 1.10);
			// 第二根高开, 阴线, 实体插入第一根超过1/2.
			bool4 = line5.getOpen() > line4.getClose();
			bool5 = line5.getType().equals("D");
			bool6 = line5.getClose() < (line4.getClose() - line4.getEntity() * 1/2);
			if(bool1 && bool2 && bool3 && bool4 && bool5 && bool6){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr2).append(" ");
				sb.append("\n");
			}
			
			/*
			 * 增强情况
			 */
			// 第二根K线开盘初段的成交量越大，表示中埋伏的越多，市势转向的可能性越大.
			
		}
		return sb;
	}
	
	/**
	 * 穿头破脚: 趋势反转.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder breakTopBottomSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		boolean bool5 = false;
		boolean boola = false;
		boolean boolb = false;
		boolean boolc = false;
		boolean boold = false;
		boolean boole = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 1));
			CandleLine line3 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 3));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line6 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			/*
			 * 上升趋势一般情况.
			 */
			// 事先有明显的上升趋势.
			bool1 = line1.getType().equals("A")  && line2.getType().equals("A")  && line3.getType().equals("A")
					&& line4.getType().equals("A")  && line5.getType().equals("A");
			// 第一根是阳线， 第二根是较长的阴线, 阴线的实体部分包括了前一根阳线的实体部分.
			bool2 = line6.getType().equals("D");
			bool3 = line6.getOpen() > line5.getClose() && line6.getClose() < line5.getOpen();
			bool4 = line6.getEntity() > 2 * line5.getEntity();
			
			/*
			 * 上升趋势增强情况
			 */
			// 两根K线的比例越悬殊，可能性越大.
			bool5 = line6.getEntity() > 4 * line5.getEntity();
			// 第二根K线的成交量越大，可能性越大.
			
			if(bool1 && bool2 && bool3 && bool4){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				if(bool5){
					sb.append("enhance");
				}
				sb.append("\n");
			}
			
			/*
			 * 下降趋势一般情况
			 */
			// 事先有明显的下降趋势.
			boola = line1.getType().equals("D")  && line2.getType().equals("D")  && line3.getType().equals("D")
					&& line4.getType().equals("D")  && line5.getType().equals("D");
			// 第一根是阴线， 第二根是较长的阳线, 阳线的实体部分包括了前一根阴线的实体部分.
			boolb = line6.getType().equals("A");
			boolc = line6.getOpen() < line5.getClose() && line6.getClose() > line5.getOpen();
			boold = line6.getEntity() > 2 * line5.getEntity();
			
			/*
			 * 下降趋势增强情况
			 */
			// 两根K线的比例越悬殊，可能性越大.
			boole = line6.getEntity() > 4 * line5.getEntity();
			// 第二根K线的成交量越大，可能性越大.
			
			if(boola && boolb && boolc && boold){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				if(boole){
					sb.append("enhance");
				}
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 锤头: 见底转向.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder hammerSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		boolean bool5 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line6 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			/*
			 * 一般情况.
			 */
			// 事先有下降趋势.
			bool1 = line5.getClose() < line1.getClose();
			// 实体很短, 下影线较长, 至少是实体的2倍长度, 几乎没有上影线
			bool2 = line6.getClose()/line6.getOpen() > 0.98 && line6.getClose()/line6.getOpen() < 1.02;
			bool3 = line6.getLowerLine() > 3 * line6.getEntity();
			bool4 = line6.getUpperLine() < line6.getEntity();
			
			if(bool1 && bool2 && bool3 && bool4){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				if(bool5){
					sb.append("enhance");
				}
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 吊颈: 见顶转向.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder hangLineSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		boolean bool5 = false;
		boolean bool6 = false;
		boolean bool7 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 6; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line6 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			CandleLine line7 = StockUtils.translateStock2CandleLine(stocks.get(i + 6));
			
			/*
			 * 一般情况.
			 */
			// 事先上升趋势.
			bool1 = line5.getClose() > line1.getClose();
			// 实体很短, 下影线较长, 至少是实体的2倍长度, 几乎没有上影线
			bool2 = line6.getClose()/line6.getOpen() > 0.98 && line6.getClose()/line6.getOpen() < 1.02;
			bool3 = line6.getLowerLine() > 3 * line6.getEntity();
			bool4 = line6.getUpperLine() < line6.getEntity();
			
			/*
			 * 增强情况
			 */
			// K线是阴线.
			bool5 = line6.getType().equals("D");
			// 后面的K线是阴线, 收盘价低于吊颈线的.
			bool6 = line7.getType().equals("D");
			bool7 = line7.getClose() < line6.getClose();
			if(bool1 && bool2 && bool3 && bool4){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				if(bool5){
					sb.append("enhance1").append(" ");
				}
				if(bool6 && bool7){
					sb.append("enhance2").append(" ");
				}
				if(bool5 && bool6 && bool7){
					sb.append("enhance3").append(" ");
				}
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 吊颈: 见顶转向.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder dawnSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		boolean bool5 = false;
		boolean bool6 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			/*
			 * 一般情况.
			 */
			// 事先是下降趋势
			bool1 = line4.getClose() < line2.getClose() && line2.getClose() < line0.getClose();
			// 第一根是阴线，实体部分较长.
			bool2 = line4.getType().equals("D");
			bool3 = (line4.getClose()/line4.getOpen() > 1.05);
			// 第二根低开, 阳线, 实体插入第一根超过1/2.
			bool4 = line5.getOpen() < line4.getClose();
			bool5 = line5.getType().equals("A");
			bool6 = line5.getClose() > (line4.getClose() + line4.getEntity() * 1/2);
			if(bool1 && bool2 && bool3 && bool4 && bool5 && bool6){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
			
			/*
			 * 增强情况
			 */
			// 第二根K线开盘初段的成交量越大，表示中埋伏的越多，市势转向的可能性越大.
			
		}
		return sb;
	}
	
	/**
	 * 红三兵: 升势持续.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder redThreeSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line3 = StockUtils.translateStock2CandleLine(stocks.get(i + 3));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			/*
			 * 一般情况.
			 */
			// 3根K线都是阳线
			bool1 = line3.getType().equals("A") && line4.getType().equals("A") && line5.getType().equals("A");
			// 3根K线的收盘价持续增加.
			bool2 = line3.getClose() < line4.getClose() && line4.getClose() < line5.getClose();
			// 第3根K线上影线不要太长, 太长意味着没有足够力量以较高价位收盘.
			bool3 = line5.getHigh()/line5.getClose() < 1.05;
			// 3根K线实体部分不能依次递减.
			bool4 = !(line5.getEntity() < line4.getEntity() && line4.getEntity() < line3.getEntity());
			
			if(bool1 && bool2 && bool3 && bool4){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 两只乌鸦: 见顶转向.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder twoCrowsSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			CandleLine line3 = StockUtils.translateStock2CandleLine(stocks.get(i + 3));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			/*
			 * 一般情况.
			 */
			// 之前是上升势
			bool1 = line4.getClose() > line2.getClose() && line2.getClose() > line0.getClose();
			// 两根都是阴线.
			bool2 = line4.getType().equals("D") && line5.getType().equals("D");
			// 第一根阴线实体部分与前一根阳线的实体部分有缺口.
			bool3 = line4.getClose() > line3.getClose();
			if(bool1 && bool2 && bool3){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 三只乌鸦: 见顶转向.  黑三兵
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder threeCrowsSingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 5; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line3 = StockUtils.translateStock2CandleLine(stocks.get(i + 3));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			CandleLine line5 = StockUtils.translateStock2CandleLine(stocks.get(i + 5));
			
			/*
			 * 一般情况.
			 */
			// 3根K线都是阴线
			bool1 = line3.getType().equals("D") && line4.getType().equals("D") && line5.getType().equals("D");
			// 3根K线的收盘价持续降低.
			bool2 = line5.getClose() < line4.getClose() && line4.getClose() < line3.getClose();
			// 第3根K线下影线不要太长, 太长意味着没有足够力量以较低价位收盘.
			bool3 = line5.getLow()/line5.getClose() > 0.97;
			
			if(bool1 && bool2 && bool3 && bool4){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 上升三部曲: 持续上升.  与三只乌鸦主要区别是前后是否有大的阳线.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder ascTrilogySingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 4; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i + 1));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			CandleLine line3 = StockUtils.translateStock2CandleLine(stocks.get(i + 3));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			
			/*
			 * 一般情况.
			 */
			// 第一根是大的阳线
			bool1 = line0.getType().equals("A") && line0.getClose()/line0.getOpen() > 1.05;
			// 中间是三个小阴线， 收盘价都不低于第一根阳线的开盘价.
			bool2 = line1.getType().equals("D") && line2.getType().equals("D") && line3.getType().equals("D");
			bool3 = line1.getClose() > line0.getOpen() && line2.getClose() > line0.getOpen() && line3.getClose() > line0.getOpen();
			// 最后是一根长的阳线，收盘价高于第一根的收盘价.
			bool4 = line4.getType().equals("A") && line4.getClose() > line0.getClose();
			
			if(bool1 && bool2 && bool3 && bool4){
				sb.append(code).append(" ").append(exchange).append(" ").append(type).append(" ")
				.append(tradeDateStr).append(" ");
				sb.append("\n");
			}
		}
		return sb;
	}
	
	/**
	 * 下跌三部曲: 持续下跌.
	 * 
	 * @param code
	 * @param exchange
	 * @param type
	 * @param lowestTimeStr
	 */
	private StringBuilder descTrilogySingle(String code, String exchange, String type, String lowestTimeStr){
		StringBuilder sb = new StringBuilder("");
		// 查询历史数据, 需要按照时间顺序排序.
		List<StockHistory> stocks = stockHistoryService.queryHistListByKey(code, exchange, type);
		if(stocks == null || stocks.isEmpty()){
			LOGGER.info("stocks is null: " + code + " " + exchange + " " + type + "; return now...");
			return sb;
		}
		
		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = false;
		boolean bool4 = false;
		int size = stocks.size();
		for(int i = 0; i < size - 4; i++){
			String tradeDateStr = stocks.get(i).getTradeDate().toString();
			if(tradeDateStr.compareTo(lowestTimeStr) < 0){
				continue;
			}
			CandleLine line0 = StockUtils.translateStock2CandleLine(stocks.get(i));
			CandleLine line1 = StockUtils.translateStock2CandleLine(stocks.get(i + 1));
			CandleLine line2 = StockUtils.translateStock2CandleLine(stocks.get(i + 2));
			CandleLine line3 = StockUtils.translateStock2CandleLine(stocks.get(i + 3));
			CandleLine line4 = StockUtils.translateStock2CandleLine(stocks.get(i + 4));
			
			/*
			 * 一般情况.
			 */
			// 第一根是大的阴线
			bool1 = line0.getType().equals("D") && line0.getClose()/line0.getOpen() < 0.95;
			// 中间是三个小阳线， 收盘价都不高于第一根阴线的开盘价.
			bool2 = line1.getType().equals("A") && line2.getType().equals("A") && line3.getType().equals("A");
			bool3 = line1.getClose() < line0.getOpen() && line2.getClose() < line0.getOpen() && line3.getClose() < line0.getOpen();
			// 最后是一根长的阴线，收盘价低于第一根的收盘价.
			bool4 = line4.getType().equals("D") && line4.getClose() < line0.getClose();
			
			if(bool1 && bool2 && bool3 && bool4){
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

	public ExchangeInfoService getExchangeInfoService() {
		return exchangeInfoService;
	}

	public void setExchangeInfoService(ExchangeInfoService exchangeInfoService) {
		this.exchangeInfoService = exchangeInfoService;
	}

}
