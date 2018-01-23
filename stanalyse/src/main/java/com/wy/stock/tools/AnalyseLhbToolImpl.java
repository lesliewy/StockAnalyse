/**
 * Project Name:stanalyse  
 * File Name:AnalyseLhbToolImpl.java  
 * Package Name:com.wy.stock.tools  
 * Date:Jan 4, 2018 9:43:34 PM  
 * Copyright (c) 2018, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import com.wy.stock.domain.StockLhbDetail;
import com.wy.stock.service.StockLhbDetailService;

/**
 *  
 * @author leslie  
 * @version   
 * @since version 1.0  
 */
public class AnalyseLhbToolImpl implements AnalyseLhbTool {

	private static Logger LOGGER = Logger.getLogger(AnalyseLhbToolImpl.class
			.getName());

	private StockLhbDetailService stockLhbDetailService;

	/** 日期 **/
	private static final String TRADE_DATE = "tradeDate";

	/** 机构家数, 包含买入和卖出 **/
	private static final String NUM_OF_BUS = "numOfBus";

	/** 一线游资家数, 包含买入和卖出 **/
	private static final String NUM_OF_TOP_YZ = "numOfTopYz";

	/** 机构和一线游资总家数, 包含买入和卖出 **/
	private static final String SUM_OF_BUS_TOP_YZ = "sumOfBusTopYz";

	/** 机构买入金额, 包含买入和卖出 **/
	private static final String VOLUMN_OF_BUYIN_BUS = "volumnOfBuyinBus";

	/** 一线游资买入金额, 包含买入和卖出 **/
	private static final String VOLUMN_OF_BUYIN_TOP_YZ = "volumnOfBuyinTopYz";

	/** 机构和一线游资买入金额, 包含买入和卖出 **/
	private static final String SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ = "sumVolumnOfBuyinBusTopYz";

	/** 机构卖出金额, 包含买入和卖出 **/
	private static final String VOLUMN_OF_SELLOUT_BUS = "volumnOfSelloutBus";

	/** 一线游资卖出金额, 包含买入和卖出 **/
	private static final String VOLUMN_OF_SELLOUT_TOP_YZ = "volumnOfSelloutTopYz";

	/** 机构和一线游资卖出金额, 包含买入和卖出 **/
	private static final String SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ = "sumVolumnOfSelloutBusTopYz";

	/** 机构净额, 包含买入和卖出 **/
	private static final String NET_VOLUMN_OF_BUS = "netVolumnOfBus";

	/** 一线游资净额, 包含买入和卖出 **/
	private static final String NET_VOLUMN_OF_TOP_YZ = "netVolumnOfTopYz";

	/** 机构买入净额, 只包含买入 **/
	private static final String NET_VOLUMN_OF_ONLY_BUYIN_BUS = "netVolumnOfOnlyBuyInBus";

	/** 一线游资买入净额, 只包含买入 **/
	private static final String NET_VOLUMN_OF_ONLY_BUYIN_TOP_YZ = "netVolumnOfOnlyBuyInTopYz";

	/** 机构卖出净额, 只包含卖出 **/
	private static final String NET_VOLUMN_OF_ONLY_SELLOUT_BUS = "netVolumnOfOnlySellOutBus";

	/** 一线游资卖出净额, 只包含卖出 **/
	private static final String NET_VOLUMN_OF_ONLY_SELLOUT_TOP_YZ = "netVolumnOfOnlySellOutTopYz";

	/** 买入机构家数, 只包含买入 **/
	private static final String NUM_OF_ONLY_BUYIN_BUS = "numOfOnlyBuyinBus";

	/** 卖出机构家数， 只包含卖出 **/
	private static final String NUM_OF_ONLY_SELLOUT_BUS = "numOfOnlySelloutBus";

	/** 买入一线游资家数, 只包含买入 **/
	private static final String NUM_OF_ONLY_BUYIN_TOP_YZ = "numOfOnlyfBuyInTopYz";

	/** 卖出一线游资家数， 只包含卖出 **/
	private static final String NUM_OF_ONLY_SELLOUT_TOP_YZ = "numOfOnlySelloutTopYz";

	public Map<String, Map<String, String>> queryAggregate(String lowTradeDateStr, String highTradeDateStr){
		List<Map<String, String>>  staticsList = queryLhbStatics(lowTradeDateStr, highTradeDateStr);
		if(CollectionUtils.isEmpty(staticsList)){
			return null;
		}
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		for(Map<String, String> map : staticsList){
			String tradeDate = map.get(TRADE_DATE);
			Map<String, String> oneDayMap = result.get(tradeDate);
			int numOfBus = getIntFromMap(map, NUM_OF_BUS);
			int numOfTopYz = getIntFromMap(map, NUM_OF_TOP_YZ);
			int sumOfBusTopYz = getIntFromMap(map, SUM_OF_BUS_TOP_YZ);
			float volumnOfBuyInBus = getFloatFromMap(map, VOLUMN_OF_BUYIN_BUS);
			float volumnOfBuyInTopYz = getFloatFromMap(map, VOLUMN_OF_BUYIN_TOP_YZ);
			float sumVolumnOfBuyInBusTopYz = getFloatFromMap(map, SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ);
			float volumnOfSellOutBus = getFloatFromMap(map, VOLUMN_OF_SELLOUT_BUS);
			float volumnOfSellOutTopYz = getFloatFromMap(map, VOLUMN_OF_SELLOUT_TOP_YZ);
			float sumVolumnOfSellOutBusTopYz = getFloatFromMap(map, SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ);
			float netVolumnOfBus = getFloatFromMap(map, NET_VOLUMN_OF_BUS);
			float netVolumnOfTopYz = getFloatFromMap(map, NET_VOLUMN_OF_TOP_YZ);
			int numOfOnlyBuyInBus = getIntFromMap(map, NUM_OF_ONLY_BUYIN_BUS);
			int numOfOnlySellOutBus = getIntFromMap(map, NUM_OF_ONLY_SELLOUT_BUS);
			int numOfOnlyBuyInTopYz = getIntFromMap(map, NUM_OF_ONLY_BUYIN_TOP_YZ);
			int numOfOnlySellOutTopYz = getIntFromMap(map, NUM_OF_ONLY_SELLOUT_TOP_YZ);
			if(oneDayMap == null){
				oneDayMap = new HashMap<String, String>();
				oneDayMap.put(NUM_OF_BUS, String.valueOf(numOfBus));
				oneDayMap.put(NUM_OF_TOP_YZ, String.valueOf(numOfTopYz));
				oneDayMap.put(SUM_OF_BUS_TOP_YZ, String.valueOf(sumOfBusTopYz));
				oneDayMap.put(VOLUMN_OF_BUYIN_BUS, String.valueOf(volumnOfBuyInBus));
				oneDayMap.put(VOLUMN_OF_BUYIN_TOP_YZ, String.valueOf(volumnOfBuyInTopYz));
				oneDayMap.put(SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ, String.valueOf(sumVolumnOfBuyInBusTopYz));
				oneDayMap.put(VOLUMN_OF_SELLOUT_BUS, String.valueOf(volumnOfSellOutBus));
				oneDayMap.put(VOLUMN_OF_SELLOUT_TOP_YZ, String.valueOf(volumnOfSellOutTopYz));
				oneDayMap.put(SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ, String.valueOf(sumVolumnOfSellOutBusTopYz));
				oneDayMap.put(NET_VOLUMN_OF_BUS, String.valueOf(netVolumnOfBus));
				oneDayMap.put(NET_VOLUMN_OF_TOP_YZ, String.valueOf(netVolumnOfTopYz));
				oneDayMap.put(NUM_OF_ONLY_BUYIN_BUS, String.valueOf(numOfOnlyBuyInBus));
				oneDayMap.put(NUM_OF_ONLY_SELLOUT_BUS, String.valueOf(numOfOnlySellOutBus));
				oneDayMap.put(NUM_OF_ONLY_BUYIN_TOP_YZ, String.valueOf(numOfOnlyBuyInTopYz));
				oneDayMap.put(NUM_OF_ONLY_SELLOUT_TOP_YZ, String.valueOf(numOfOnlySellOutTopYz));
			}else{
				oneDayMap.put(NUM_OF_BUS, String.valueOf(Integer.valueOf(oneDayMap.get(NUM_OF_BUS)) + numOfBus));
				oneDayMap.put(NUM_OF_TOP_YZ, getSumFromMap(oneDayMap.get(NUM_OF_TOP_YZ), numOfTopYz));
				oneDayMap.put(SUM_OF_BUS_TOP_YZ, getSumFromMap(oneDayMap.get(SUM_OF_BUS_TOP_YZ), sumOfBusTopYz));
				oneDayMap.put(VOLUMN_OF_BUYIN_BUS, getSumFromMap(oneDayMap.get(VOLUMN_OF_BUYIN_BUS), volumnOfBuyInBus));
				oneDayMap.put(VOLUMN_OF_BUYIN_TOP_YZ, getSumFromMap(oneDayMap.get(VOLUMN_OF_BUYIN_TOP_YZ), volumnOfBuyInTopYz));
				oneDayMap.put(SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ, getSumFromMap(oneDayMap.get(SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ), sumVolumnOfBuyInBusTopYz));
				oneDayMap.put(VOLUMN_OF_SELLOUT_BUS, getSumFromMap(oneDayMap.get(VOLUMN_OF_SELLOUT_BUS), volumnOfSellOutBus));
				oneDayMap.put(VOLUMN_OF_SELLOUT_TOP_YZ, getSumFromMap(oneDayMap.get(VOLUMN_OF_SELLOUT_TOP_YZ), volumnOfSellOutTopYz));
				oneDayMap.put(SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ, getSumFromMap(oneDayMap.get(SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ), sumVolumnOfSellOutBusTopYz));
				oneDayMap.put(NET_VOLUMN_OF_BUS, getSumFromMap(oneDayMap.get(NET_VOLUMN_OF_BUS), netVolumnOfBus));
				oneDayMap.put(NET_VOLUMN_OF_TOP_YZ, getSumFromMap(oneDayMap.get(NET_VOLUMN_OF_TOP_YZ), netVolumnOfTopYz));
				oneDayMap.put(NUM_OF_ONLY_BUYIN_BUS, getSumFromMap(oneDayMap.get(NUM_OF_ONLY_BUYIN_BUS), numOfOnlyBuyInBus));
				oneDayMap.put(NUM_OF_ONLY_SELLOUT_BUS, getSumFromMap(oneDayMap.get(NUM_OF_ONLY_SELLOUT_BUS), numOfOnlySellOutBus));
				oneDayMap.put(NUM_OF_ONLY_BUYIN_TOP_YZ, getSumFromMap(oneDayMap.get(NUM_OF_ONLY_BUYIN_TOP_YZ), numOfOnlyBuyInTopYz));
				oneDayMap.put(NUM_OF_ONLY_SELLOUT_TOP_YZ, getSumFromMap(oneDayMap.get(NUM_OF_ONLY_SELLOUT_TOP_YZ), numOfOnlySellOutTopYz));
			}
			result.put(tradeDate, oneDayMap);
		}
		return result;
	}
	
	private Integer getIntFromMap(Map<String, String> map, String key){
		return StringUtils.isNotBlank(map.get(key)) ? Integer.valueOf(map.get(key)) : 0;
	}
	
	private Float getFloatFromMap(Map<String, String> map, String key){
		return StringUtils.isNotBlank(map.get(key)) ? Float.valueOf(map.get(key)) : 0;
	}
	
	private String getSumFromMap(String a, int b){
		return String.valueOf(Integer.parseInt(a) + b);
	}
	
	private String getSumFromMap(String a, float b){
		return String.valueOf(Float.valueOf(a) + b);
	}
	
	/**
	 * 每天的机构家数，一线游资家数，两者总家数，机构总金额
	 */
	public List<Map<String, String>> queryLhbStatics(String lowTradeDateStr,
			String highTradeDateStr) {
		long begin = System.currentTimeMillis();
		StockLhbDetail stockLhbDetail = new StockLhbDetail();
		stockLhbDetail.setBeginDate(lowTradeDateStr);
		stockLhbDetail.setEndDate(highTradeDateStr);
		List<StockLhbDetail> lhbDetailList = stockLhbDetailService
				.query(stockLhbDetail);
		if (CollectionUtils.isEmpty(lhbDetailList)) {
			LOGGER.info(lowTradeDateStr + " <= tradeDate <= " + highTradeDateStr
					+ " not exists, return now...");
			return null;
		}
		List<Map<String, String>> lhbDayList = new ArrayList<Map<String, String>>();
		for (StockLhbDetail lhbDetail : lhbDetailList) {
			String tradeDate = new SimpleDateFormat("YYYY-MM-dd").format(lhbDetail
					.getTradeDate().getTime());
			JSONObject buyInJson = lhbDetail.getBuyIn();
			JSONObject sellOutJson = lhbDetail.getSellOut();
			Map<String, String> buyInMap = calBuyIn(buyInJson);
			boolean testFlag = false;
			Map<String, String> sellOutMap = calSellOut(sellOutJson);

			Map<String, String> result = new HashMap<String, String>();
			result.put(TRADE_DATE, tradeDate);
			Integer onlyBuyInBus = Integer.valueOf(buyInMap.get(NUM_OF_BUS));
			Integer onlySellOutBus = Integer.valueOf(sellOutMap.get(NUM_OF_BUS));
			result.put(NUM_OF_BUS, String.valueOf(onlyBuyInBus + onlySellOutBus));

			Integer onlyBuyInTopYz = Integer.valueOf(buyInMap.get(NUM_OF_TOP_YZ));
			Integer onlySellOutTopYz = Integer.valueOf(sellOutMap
					.get(NUM_OF_TOP_YZ));
			result.put(NUM_OF_TOP_YZ,
					String.valueOf(onlyBuyInTopYz + onlySellOutTopYz));

			result.put(
					SUM_OF_BUS_TOP_YZ,
					String.valueOf(onlyBuyInBus + onlySellOutBus + onlyBuyInTopYz
							+ onlySellOutTopYz));

			Float volumnOfOnlyBuyInBus = Float.valueOf(buyInMap
					.get(VOLUMN_OF_BUYIN_BUS));
			Float volumnOfOnlySellOutBus = Float.valueOf(sellOutMap
					.get(VOLUMN_OF_BUYIN_BUS));
			result.put(VOLUMN_OF_BUYIN_BUS,
					String.valueOf(volumnOfOnlyBuyInBus + volumnOfOnlySellOutBus));

			Float volumnOfOnlyBuyInTopYz = Float.valueOf(buyInMap
					.get(VOLUMN_OF_BUYIN_TOP_YZ));
			Float volumnOfOnlySellOutTopYz = Float.valueOf(sellOutMap
					.get(VOLUMN_OF_BUYIN_TOP_YZ));
			result.put(VOLUMN_OF_BUYIN_TOP_YZ, String
					.valueOf(volumnOfOnlyBuyInTopYz + volumnOfOnlySellOutTopYz));

			result.put(
					SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ,
					String.valueOf(volumnOfOnlyBuyInBus + volumnOfOnlySellOutBus
							+ volumnOfOnlyBuyInTopYz + volumnOfOnlySellOutTopYz));

			Float volumnOfOnlyBuySellOutBus = Float.valueOf(buyInMap
					.get(VOLUMN_OF_SELLOUT_BUS));
			Float volumnOfOnlySellOutSellOutBus = Float.valueOf(sellOutMap
					.get(VOLUMN_OF_SELLOUT_BUS));
			result.put(
					VOLUMN_OF_SELLOUT_BUS,
					String.valueOf(volumnOfOnlyBuySellOutBus
							+ volumnOfOnlySellOutSellOutBus));

			Float volumnOfOnlyBuySellOutTopYz = Float.valueOf(buyInMap
					.get(VOLUMN_OF_SELLOUT_TOP_YZ));
			Float volumnOfOnlySellOutSellOutTopYz = Float.valueOf(sellOutMap
					.get(VOLUMN_OF_SELLOUT_TOP_YZ));
			result.put(
					VOLUMN_OF_SELLOUT_TOP_YZ,
					String.valueOf(volumnOfOnlyBuySellOutTopYz
							+ volumnOfOnlySellOutSellOutTopYz));

			result.put(
					SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ,
					String.valueOf(volumnOfOnlyBuySellOutBus
							+ volumnOfOnlySellOutSellOutBus
							+ volumnOfOnlyBuySellOutTopYz
							+ volumnOfOnlySellOutSellOutTopYz));

			Float netVolumnOfOnlyBuyBus = Float.valueOf(buyInMap
					.get(NET_VOLUMN_OF_BUS));
			Float netVolumnOfOnlySellOutBus = Float.valueOf(sellOutMap
					.get(NET_VOLUMN_OF_BUS));
			result.put(NET_VOLUMN_OF_BUS, String.valueOf(netVolumnOfOnlyBuyBus
					+ netVolumnOfOnlySellOutBus));

			Float netVolumnOfOnlyBuyTopYz = Float.valueOf(buyInMap
					.get(NET_VOLUMN_OF_TOP_YZ));
			Float netVolumnOfOnlySellOutTopYz = Float.valueOf(sellOutMap
					.get(NET_VOLUMN_OF_TOP_YZ));
			result.put(
					NET_VOLUMN_OF_TOP_YZ,
					String.valueOf(netVolumnOfOnlyBuyTopYz
							+ netVolumnOfOnlySellOutTopYz));

			result.put(NUM_OF_ONLY_BUYIN_BUS, buyInMap.get(NUM_OF_ONLY_BUYIN_BUS));
			result.put(NUM_OF_ONLY_SELLOUT_BUS,
					sellOutMap.get(NUM_OF_ONLY_SELLOUT_BUS));
			result.put(NUM_OF_ONLY_BUYIN_TOP_YZ,
					buyInMap.get(NUM_OF_ONLY_BUYIN_TOP_YZ));
			result.put(NUM_OF_ONLY_SELLOUT_TOP_YZ,
					sellOutMap.get(NUM_OF_ONLY_SELLOUT_TOP_YZ));
			lhbDayList.add(result);
		}
		return lhbDayList;
	}

	private Map<String, String> calBuyIn(JSONObject buyInJson) {
		if (buyInJson == null) {
			return null;
		}
		int numOfBus = 0;
		int numOfTopYz = 0;
		Float volumnOfBuyinBus = Float.valueOf(0);
		Float volumnOfBuyinTopYz = Float.valueOf(0);
		Float volumnOfSelloutBus = Float.valueOf(0);
		Float volumnOfSelloutTopYz = Float.valueOf(0);
		Float netVolumnOfBus = Float.valueOf(0);
		Float netVolumnOfTopYz = Float.valueOf(0);

		Iterator buyInJsonIter = buyInJson.keys();
		while (buyInJsonIter.hasNext()) {
			String key = (String) buyInJsonIter.next();
			JSONObject oneJson = buyInJson.getJSONObject(key);
			Iterator oneJsonIter = oneJson.keys();
			String name = "";
			Float buyInVolumn = Float.valueOf(0);
			Float sellOutVolumn = Float.valueOf(0);
			String busFlag = "";
			Float volumnNet = Float.valueOf(0);
			/** 先取值 **/
			while (oneJsonIter.hasNext()) {
				String oneKey = (String) oneJsonIter.next();
				String oneValue = oneJson.get(oneKey).toString();
				if ("name".equalsIgnoreCase(oneKey)) {
					name = oneValue;
				} else if ("v_in".equalsIgnoreCase(oneKey)) {
					buyInVolumn = StringUtils.isNotBlank(oneValue) ? Float
							.valueOf(oneValue) : 0;
				} else if ("v_out".equalsIgnoreCase(oneKey)) {
					sellOutVolumn = StringUtils.isNotBlank(oneValue) ? Float
							.valueOf(oneValue) : 0;
				} else if ("bus_flag".equalsIgnoreCase(oneKey)) {
					busFlag = oneValue;
				} else if ("v_net".equalsIgnoreCase(oneKey)) {
					volumnNet = StringUtils.isNotBlank(oneValue) ? Float
							.valueOf(oneValue) : 0;
				}
			}
			if ("机构专用".equalsIgnoreCase(name)) {
				numOfBus++;
				volumnOfBuyinBus += buyInVolumn;
				volumnOfSelloutBus += sellOutVolumn;
				netVolumnOfBus += volumnNet;
			}
			if ("一线游资".equalsIgnoreCase(busFlag)) {
				numOfTopYz++;
				volumnOfBuyinTopYz += buyInVolumn;
				volumnOfSelloutTopYz += sellOutVolumn;
				netVolumnOfTopYz += volumnNet;
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put(NUM_OF_BUS, String.valueOf(numOfBus));
		result.put(NUM_OF_TOP_YZ, String.valueOf(numOfTopYz));
		result.put(SUM_OF_BUS_TOP_YZ, String.valueOf(numOfBus + numOfTopYz));
		result.put(VOLUMN_OF_BUYIN_BUS, String.valueOf(volumnOfBuyinBus));
		result.put(VOLUMN_OF_BUYIN_TOP_YZ, String.valueOf(volumnOfBuyinTopYz));
		result.put(SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ,
				String.valueOf(volumnOfBuyinBus + volumnOfBuyinTopYz));
		result.put(VOLUMN_OF_SELLOUT_BUS, String.valueOf(volumnOfSelloutBus));
		result.put(VOLUMN_OF_SELLOUT_TOP_YZ, String.valueOf(volumnOfSelloutTopYz));
		result.put(SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ,
				String.valueOf(volumnOfSelloutBus + volumnOfSelloutTopYz));
		result.put(NUM_OF_ONLY_BUYIN_BUS, String.valueOf(numOfBus));
		result.put(NUM_OF_ONLY_BUYIN_TOP_YZ, String.valueOf(numOfTopYz));
		result.put(NET_VOLUMN_OF_BUS, String.valueOf(netVolumnOfBus));
		result.put(NET_VOLUMN_OF_TOP_YZ, String.valueOf(netVolumnOfTopYz));
		result.put(NET_VOLUMN_OF_ONLY_BUYIN_BUS, String.valueOf(netVolumnOfBus));
		result.put(NET_VOLUMN_OF_ONLY_BUYIN_TOP_YZ,
				String.valueOf(netVolumnOfTopYz));
		return result;
	}

	private Map<String, String> calSellOut(JSONObject sellOutJson) {
		if (sellOutJson == null) {
			return null;
		}
		int numOfBus = 0;
		int numOfTopYz = 0;
		Float volumnOfBuyinBus = Float.valueOf(0);
		Float volumnOfBuyinTopYz = Float.valueOf(0);
		Float volumnOfSelloutBus = Float.valueOf(0);
		Float volumnOfSelloutTopYz = Float.valueOf(0);
		Float netVolumnOfBus = Float.valueOf(0);
		Float netVolumnOfTopYz = Float.valueOf(0);

		Iterator sellOutJsonIter = sellOutJson.keys();
		while (sellOutJsonIter.hasNext()) {
			String key = (String) sellOutJsonIter.next();
			JSONObject oneJson = sellOutJson.getJSONObject(key);
			Iterator oneJsonIter = oneJson.keys();
			String name = "";
			Float buyInVolumn = Float.valueOf(0);
			Float sellOutVolumn = Float.valueOf(0);
			String busFlag = "";
			Float volumnNet = Float.valueOf(0);
			/** 先取值 **/
			while (oneJsonIter.hasNext()) {
				String oneKey = (String) oneJsonIter.next();
				String oneValue = oneJson.get(oneKey).toString();
				if ("name".equalsIgnoreCase(oneKey)) {
					name = oneValue;
				} else if ("v_in".equalsIgnoreCase(oneKey)) {
					buyInVolumn = StringUtils.isNotBlank(oneValue) ? Float
							.valueOf(oneValue) : 0;
				} else if ("v_out".equalsIgnoreCase(oneKey)) {
					sellOutVolumn = StringUtils.isNotBlank(oneValue) ? Float
							.valueOf(oneValue) : 0;
				} else if ("bus_flag".equalsIgnoreCase(oneKey)) {
					busFlag = oneValue;
				} else if ("v_net".equalsIgnoreCase(oneKey)) {
					volumnNet = StringUtils.isNotBlank(oneValue) ? Float
							.valueOf(oneValue) : 0;
				}
			}
			if ("机构专用".equalsIgnoreCase(name)) {
				numOfBus++;
				volumnOfBuyinBus += buyInVolumn;
				volumnOfSelloutBus += sellOutVolumn;
				netVolumnOfBus += volumnNet;
			}
			if ("一线游资".equalsIgnoreCase(busFlag)) {
				numOfTopYz++;
				volumnOfBuyinTopYz += buyInVolumn;
				volumnOfSelloutTopYz += sellOutVolumn;
				netVolumnOfTopYz += volumnNet;
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put(NUM_OF_BUS, String.valueOf(numOfBus));
		result.put(NUM_OF_TOP_YZ, String.valueOf(numOfTopYz));
		result.put(SUM_OF_BUS_TOP_YZ, String.valueOf(numOfBus + numOfTopYz));
		result.put(VOLUMN_OF_BUYIN_BUS, String.valueOf(volumnOfBuyinBus));
		result.put(VOLUMN_OF_BUYIN_TOP_YZ, String.valueOf(volumnOfBuyinTopYz));
		result.put(SUM_VOLUMN_OF_BUYIN_BUS_TOP_YZ,
				String.valueOf(volumnOfBuyinBus + volumnOfBuyinTopYz));
		result.put(VOLUMN_OF_SELLOUT_BUS, String.valueOf(volumnOfSelloutBus));
		result.put(VOLUMN_OF_SELLOUT_TOP_YZ, String.valueOf(volumnOfSelloutTopYz));
		result.put(SUM_VOLUMN_OF_SELLOUT_BUS_TOP_YZ,
				String.valueOf(volumnOfSelloutBus + volumnOfSelloutTopYz));
		result.put(NUM_OF_ONLY_SELLOUT_BUS, String.valueOf(numOfBus));
		result.put(NUM_OF_ONLY_SELLOUT_TOP_YZ, String.valueOf(numOfTopYz));
		result.put(NET_VOLUMN_OF_BUS, String.valueOf(netVolumnOfBus));
		result.put(NET_VOLUMN_OF_TOP_YZ, String.valueOf(netVolumnOfTopYz));
		result.put(NET_VOLUMN_OF_ONLY_SELLOUT_BUS, String.valueOf(netVolumnOfBus));
		result.put(NET_VOLUMN_OF_ONLY_SELLOUT_TOP_YZ,
				String.valueOf(netVolumnOfTopYz));
		return result;
	}

	public StockLhbDetailService getStockLhbDetailService() {
		return stockLhbDetailService;
	}

	public void setStockLhbDetailService(StockLhbDetailService stockLhbDetailService) {
		this.stockLhbDetailService = stockLhbDetailService;
	}

}
