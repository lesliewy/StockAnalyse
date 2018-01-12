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
