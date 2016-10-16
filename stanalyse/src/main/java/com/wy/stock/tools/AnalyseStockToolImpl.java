/**
 * 
 */
package com.wy.stock.tools;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wy.stock.domain.AnalyseCondition;
import com.wy.stock.domain.IndustryHot;
import com.wy.stock.domain.IndustryHotStocks;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.domain.NotionHotStocks;
import com.wy.stock.domain.StockCapFlow;
import com.wy.stock.service.IndexService;
import com.wy.stock.service.IndustryHotService;
import com.wy.stock.service.IndustryHotStocksService;
import com.wy.stock.service.IndustryInfoService;
import com.wy.stock.service.IndustryStockService;
import com.wy.stock.service.NotionHotService;
import com.wy.stock.service.NotionHotStocksService;
import com.wy.stock.service.NotionInfoService;
import com.wy.stock.service.StockCapFlowService;
import com.wy.stock.service.StockInfoService;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;


/**
 * @author leslie
 *
 */
public class AnalyseStockToolImpl implements AnalyseStockTool{
	
	private static Logger LOGGER = Logger.getLogger(AnalyseStockToolImpl.class
			.getName());

	private StockCapFlowService stockCapFlowService;
	
	private StockInfoService stockInfoService;
	
	private IndustryStockService industryStockService;
	
	private NotionHotService notionHotService;
	
	private IndustryHotService industryHotService;
	
	private IndustryHotStocksService industryHotStocksService;
	
	private NotionHotStocksService notionHotStocksService;
	
	private NotionInfoService notionInfoService;
	
	private IndustryInfoService industryInfoService;
	
	private IndexService indexService;
	
	private Map<String, String> codeNameMap;
	
	private Map<String, String> codeIndustryNameMap;
	
	private static final String CSV_SPLIT = ",";
	
	/**
	 * 分析给定日期前几日的累计涨跌幅.
	 */
	public void analyseAccumuChangePct(AnalyseCondition condition) {
		if(!init(condition)){
			return;
		}
		
		/*
		 *  如果设置了累计几日，和累积的涨跌幅上下限, 需要查询累积的上下限.
		 */
		Integer  accumuDays = condition.getAccumuDays();
		Float accumuDaysChangePctLower = condition.getAccumuDaysChangePctLower();
		Float accumuDaysChangePctUpper = condition.getAccumuDaysChangePctUpper();
		// key: code_A, value: accumuChangePct 累计涨跌幅.
		Map<String, Float> accumuMap = new HashMap<String, Float>();
		if(accumuDays != null && accumuDaysChangePctLower != null && accumuDaysChangePctUpper != null){
			accumuMap = getAccumuMap(accumuDays, condition.getTradeDate());
			if(accumuMap == null){
				return;
			}
		}
		
		// 查询数据库.
		List<StockCapFlow> analyseList = stockCapFlowService.analyseFall(condition);
		if(analyseList == null || analyseList.isEmpty()){
			LOGGER.info("analyseList is null or empty, return now...");
			return;
		}
		
		/*
		 * 公共过滤
		 */
		List<StockCapFlow> filteredAnalyseList = commonFilter(analyseList, condition, codeNameMap, codeIndustryNameMap);
		
		int total = 0;
		StringBuilder sb = new StringBuilder("\nanalyseAccumuChangePct:\n");
		for(StockCapFlow stock : filteredAnalyseList){
			String code = stock.getCode();
			String name = codeNameMap.get(code);
			
			/*
			 * 累计日期涨跌幅过滤
			 */
			if(accumuMap != null && !accumuMap.isEmpty()){
				// 获取该股票的累计涨跌幅.
				Float accumuChangePct = accumuMap.get(code + "_A");
				if(accumuChangePct == null || accumuChangePct < accumuDaysChangePctLower || accumuChangePct > accumuDaysChangePctUpper){
					continue;
				}
			}
			
			Formatter formatter = new Formatter();
			formatter.format("%7s %-10s %-10s %-6s %-16s %-16s %-16s %-16s %-16s\n", code,
					name, codeIndustryNameMap.get(code), stock.getChangePercent(),
					"主力: "   + stock.getMainNetIn() + "万", 
					"超大单: " + stock.getSuperLarge() + "万",
					"大单: "   + stock.getLarge() + "万",
					"中单: "  + stock.getMiddle() + "万",
					"小单: "   + stock.getSmall() + "万");
			sb.append(formatter.toString());
			formatter.close();
			total++;
		}
		sb.append("total: " + total).append("\n");
		LOGGER.info(sb);
	}
	
	/**
	 * 分析给定日期前几日的主力净流入占比情况:  下跌一段时间后，出现盘整;  
	 * 出货的越干净越容易拉起来，后一个中枢最好接近之前的中枢;
	 * 主力净流出但是股价跌不下去甚至上涨的;
	 * 最近主力净流入, 新资金进入；
	 * 最近虽然主力净流出，但是超大单有净流入，新资金进入；
	 */
	public void analyseAccumuMainNetIn(AnalyseCondition condition) {
		if(!init(condition)){
			return;
		}
		
		/*
		 *  如果设置了累计几日和累计净占比(净流出占比 + 净流入占比).
		 */
		Integer  accumuDays = condition.getAccumuDays();
		Float accumuMainNetInPctLower = condition.getAccumuMainNetInPctLower();
		Float accumuMainNetInPctUpper = condition.getAccumuMainNetInPctUpper();
		// key: code_B, value: accumuMainNetInPct 累计主力净占比.
		Map<String, Float> accumuMap = new HashMap<String, Float>();
		if(accumuDays != null && accumuMainNetInPctLower != null && accumuMainNetInPctUpper != null){
			accumuMap = getAccumuMap(accumuDays, condition.getTradeDate());
			if(accumuMap == null){
				return;
			}
		}
		
		// 查询数据库.
		List<StockCapFlow> analyseList = stockCapFlowService.analyseFall(condition);
		if(analyseList == null || analyseList.isEmpty()){
			LOGGER.info("analyseList is null or empty, return now...");
			return;
		}
		
		/*
		 * 公共过滤
		 */
		List<StockCapFlow> filteredAnalyseList = commonFilter(analyseList, condition, codeNameMap, codeIndustryNameMap);
		
		int total = 0;
		StringBuilder sb = new StringBuilder("\nanalyseAccumuMainNetIn:\n");
		for(StockCapFlow stock : filteredAnalyseList){
			String code = stock.getCode();
			String name = codeNameMap.get(code);
			
			/*
			 * 累计主力净占比过滤
			 */
			// 获取该股票的累计主力净占比.
			Float accumuMainNetInPct = accumuMap.get(code + "_B");
			if(accumuMap != null && !accumuMap.isEmpty()){
				if(accumuMainNetInPct == null || accumuMainNetInPct < accumuMainNetInPctLower || accumuMainNetInPct > accumuMainNetInPctUpper){
					continue;
				}
			}
			
			Formatter formatter = new Formatter();
			formatter.format("%7s %-10s %-10s %-6s %-6s %-16s %-16s %-16s %-16s\n", code,
					name, codeIndustryNameMap.get(code), stock.getClose(), stock.getChangePercent(),
					"累计主力净占比: " + accumuMainNetInPct,
					"主力: "   + stock.getMainNetIn() + "万", 
					"超大单: " + stock.getSuperLarge() + "万",
					"大单: "   + stock.getLarge() + "万");
			sb.append(formatter.toString());
			formatter.close();
			total++;
		}
		sb.append("total: " + total).append("\n");
		LOGGER.info(sb);
	}
	
	private boolean init(AnalyseCondition condition){
		// 设置默认值.
		fillBlankCondition(condition);
		LOGGER.info("analyseFall condition: " + condition);
		
		if(!isValid(condition)){
			return false;
		}
		
		/*
		 *  code - name Map
		 */
		codeNameMap = stockInfoService.queryStockCodeNameMap();
		if(codeNameMap == null){
			LOGGER.info("codeNameMap is null, return now...");
			return false;
		}
		/*
		 *  code - industryName Map
		 */
		codeIndustryNameMap = industryStockService.queryCodeIndustryNameMap(StockConstant.THS_FLAG);
		if(codeIndustryNameMap == null){
			LOGGER.info("codeIndustryNameMap is null, return now...");
			return false;
		}
		return true;
	}
	/**
	 * 设置默认值.
	 * @param originalCondition
	 * @return
	 */
	private void fillBlankCondition(AnalyseCondition originalCondition){
		if(originalCondition.getTradeDate() == null){
			String tradeDateStr = new SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().getTime()) + " 15:00:00";
			originalCondition.setTradeDate(Timestamp.valueOf(tradeDateStr));
		}
		Float closeUpper = originalCondition.getCloseUpper();
		Float closeLower = originalCondition.getCloseLower();
		if(closeUpper == null){
			originalCondition.setCloseUpper(100.0f);
		}
		if(closeLower == null){
			originalCondition.setCloseLower(6.0f);
		}
		
		BigDecimal capFlowLower = originalCondition.getCapFlowLower();
		if(capFlowLower == null){
			originalCondition.setCapFlowLower(new BigDecimal(Integer.MIN_VALUE));
		}
		BigDecimal capFlowUpper = originalCondition.getCapFlowUpper();
		if(capFlowUpper == null){
			originalCondition.setCapFlowUpper(new BigDecimal(Integer.MAX_VALUE));
		}
		List<Integer> types = originalCondition.getTypes();
		if(types == null || types.isEmpty()){
			types = new ArrayList<Integer>();
			types.add(0);
			types.add(3);
			types.add(6);
			originalCondition.setTypes(types);
		}
		List<String> excludeIndustries = originalCondition.getExcludeIndustries();
		if(excludeIndustries == null || excludeIndustries.isEmpty()){
			excludeIndustries = new ArrayList<String>();
			excludeIndustries.add("银行");
			excludeIndustries.add("保险及其他");
			excludeIndustries.add("房地产开发");
			excludeIndustries.add("电力");
			excludeIndustries.add("港口航运");
			excludeIndustries.add("钢铁");
			excludeIndustries.add("石油矿业开采");
			originalCondition.setExcludeIndustries(excludeIndustries);
		}
		Float changePctLower = originalCondition.getChangePctLower();
		if(changePctLower == null){
			originalCondition.setChangePctLower(-999999999.9f);
		}
		Float changePctUpper = originalCondition.getChangePctUpper();
		if(changePctUpper == null){
			originalCondition.setChangePctUpper(999999999.9f);
		}
	}
	
	/**
	 * 校验条件.
	 * @param condition
	 * @return
	 */
	private boolean isValid(AnalyseCondition condition){
		if(!isTradeDateValid(condition.getTradeDate()) || !isAccumuDaysValid(condition.getAccumuDays())){
			return false;
		}
		return true;
	}
	
	/**
	 * 校验tradeDate, 分析日期必填. 且 ST_STOCK_CAP_FLOW 中必须存在.
	 * @param tradeDate
	 * @return
	 */
	private boolean isTradeDateValid(Timestamp tradeDate){
		if(tradeDate == null){
			LOGGER.info("tradeDate is null, return now...");
			return false;
		}
		List<StockCapFlow> list = stockCapFlowService.queryCapFlowByDate(tradeDate);
		if(list == null || list.isEmpty()){
			LOGGER.info("tradeDate: " + tradeDate + " not exists in ST_STOCK_CAP_FLOW, return now...");
			return false;
		}
		return true;
	}
	
	/**
	 * 校验累计几日, 如果存在，限制必须小于10.
	 * @param accumuDays.
	 * @return
	 */
	private boolean isAccumuDaysValid(Integer accumuDays){
		if(accumuDays != null && accumuDays.intValue() > 10){
			LOGGER.info("accumuDays: " + accumuDays + " must less than 10, return now...");
			return false;
		}
		return true;
	}
	
	/**
	 * 获取每个股票累计几日的涨跌幅总和.
	 * 先通过一个股票获取分析日期前的日期, 然后获得累计几日前的确切日期，用于查询。
	 * @param accumuDays
	 * @param tradeDate
	 * @return
	 */
	private Map<String, Float> getAccumuMap(Integer accumuDays, Timestamp tradeDate){
		List<Timestamp> dates = stockCapFlowService.queryTradeDateBefore(tradeDate, "000001");
		if(dates == null || dates.size() < accumuDays){
			LOGGER.info("dates is null , return now...");
			return null;
		}
		Timestamp tradeDateLower = dates.get(accumuDays - 1);
		LOGGER.info("date: " + tradeDateLower + " - " + tradeDate);
		
		List<StockCapFlow> capFlowList = stockCapFlowService.queryAllAccumuChangePct(tradeDateLower, tradeDate);
		if(capFlowList == null){
			LOGGER.info("capFlowList is null, return now...");
			return null;
		}
		Map<String, Float> result = new HashMap<String, Float>();
		for(StockCapFlow capFlow : capFlowList){
			result.put(capFlow.getCode() + "_A", capFlow.getAccumuChangePct());
			result.put(capFlow.getCode() + "_B", capFlow.getAccumuMainNetInPct());
		}
		return result;
	}
	
	/**
	 * 公共的过滤.
	 * @param analyseList
	 * @param condition
	 * @param codeNameMap
	 * @return
	 */
	private List<StockCapFlow> commonFilter(List<StockCapFlow> analyseList, AnalyseCondition condition, Map<String, String> codeNameMap,
			Map<String, String> codeIndustryNameMap){
		List<StockCapFlow> result = new ArrayList<StockCapFlow>();
		List<String> excludeName = condition.getExcludeName();
		List<Integer> types = condition.getTypes();
		List<String> excludeIndustries = condition.getExcludeIndustries();
		boolean nameOmit = false;
		for(StockCapFlow capFlow : analyseList){
			String code = capFlow.getCode();
			Float close = capFlow.getClose();
			String name = codeNameMap.get(code);
			String industryName = codeIndustryNameMap.get(code);
			
			/*
			 * 股票类型过滤  0-深  6-上  3-创
			 */
			if(types != null && !types.contains(Integer.valueOf(code.substring(0, 1)))){
				continue;
			}
			
			/*
			 * 收盘价过滤
			 */
			if(close == null || close < condition.getCloseLower() || close > condition.getCloseUpper()){
				continue;
			}
			
			/*
			 * 名字过滤
			 */
			if(excludeName != null && !StringUtils.isEmpty(name)){
				for(String str : excludeName){
					if(name.contains(str)){
						nameOmit = true;
					}
				}
			}
			if(nameOmit){
				// 重置.
				nameOmit = false;
				continue;
			}
			
			/*
			 * 行业类型过滤.
			 */
			if(excludeIndustries.contains(industryName)){
				continue;
			}
			
			result.add(capFlow);
		}
		
		return result;
	}
	
	/**
	 * 生成概念板块的指数csv文件, 每天中的概念板块按照涨幅从高到低排序.
	 */
	public void genNotionHotCsv(int year) {
		long begin = System.currentTimeMillis();
		List<NotionHot> notionHotList = notionHotService.queryNotionHotForCsv(year);
		if(notionHotList == null){
			return;
		}
		StringBuilder sb = new StringBuilder("日期,上证指数(%),上证成交量(亿),深证指数(%),深证成交量(亿),创业板指数(%),创业板成交量(亿),序号,概念名称,涨跌幅,领涨股,涨跌幅,时间\n");
		SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Map<String, Float> indexMap = indexService.queryAllChangePctAndVolumnAmnt();
		if(indexMap == null){
			return;
		}
		for(NotionHot notionHot : notionHotList){
			// 排除掉所有涨跌幅是0.0的, 因为极可能是停牌.
			if(notionHot.getChangePercent() == 0){
				continue;
			}
			String tradeDateStr = format1.format(notionHot.getTradeDate());
			
			String changePctSHKey = tradeDateStr + "_" + "上证指数" + "_" + "涨跌幅";
			String changePctSZKey = tradeDateStr + "_" + "深证成指" + "_" + "涨跌幅";
			String changePctCZKey = tradeDateStr + "_" + "创业板指" + "_" + "涨跌幅";
			String volumnAmntSHKey = tradeDateStr + "_" + "上证指数" + "_" + "成交金额";
			String volumnAmntSZKey = tradeDateStr + "_" + "深证成指" + "_" + "成交金额";
			String volumnAmntCZKey = tradeDateStr + "_" + "创业板指" + "_" + "成交金额";
			String changePctSH = indexMap.containsKey(changePctSHKey) ? String.valueOf(indexMap.get(changePctSHKey)) : "";
			String changePctSZ = indexMap.containsKey(changePctSZKey) ? String.valueOf(indexMap.get(changePctSZKey)) : "";
			String changePctCZ = indexMap.containsKey(changePctCZKey) ? String.valueOf(indexMap.get(changePctCZKey)) : "";
			String volumnAmntSH = indexMap.containsKey(volumnAmntSHKey) ? String.valueOf(indexMap.get(volumnAmntSHKey)) : "";
			String volumnAmntSZ = indexMap.containsKey(volumnAmntSZKey) ? String.valueOf(indexMap.get(volumnAmntSZKey)) : "";
			String volumnAmntCZ = indexMap.containsKey(volumnAmntCZKey) ? String.valueOf(indexMap.get(volumnAmntCZKey)) : "";
					
			sb.append(tradeDateStr).append(CSV_SPLIT)
			.append(changePctSH).append(CSV_SPLIT)
			.append(volumnAmntSH).append(CSV_SPLIT)
			.append(changePctSZ).append(CSV_SPLIT)
			.append(volumnAmntSZ).append(CSV_SPLIT)
			.append(changePctCZ).append(CSV_SPLIT)
			.append(volumnAmntCZ).append(CSV_SPLIT)
			.append(notionHot.getRank()).append(CSV_SPLIT)
			.append(notionHot.getNotionName()).append(CSV_SPLIT)
			.append(notionHot.getChangePercent()).append(CSV_SPLIT)
			.append(notionHot.getRiseLeadStockName()).append(CSV_SPLIT)
			.append(notionHot.getRiseLeadStockChangePercent()).append(CSV_SPLIT)
			.append(format2.format(notionHot.getTimestamp()))
			.append("\n");
		}
		StockUtils.writeToFile("/home/leslie/MyProject/StockAnalyse/gen/notionHot" + "_" + year + ".csv", sb, "GB2312");
		long end = System.currentTimeMillis();
		LOGGER.info("elapsed time: " + (end - begin)/(1000) + " s.");
	}
	
	public void genIndustryHotCsv(int year) {
		long begin = System.currentTimeMillis();
		List<IndustryHot> industryHotList = industryHotService.queryIndustryHotForCsv(year);
		if(industryHotList == null){
			return;
		}
		StringBuilder sb = new StringBuilder("日期,上证指数(%),上证成交量(亿),深证指数(%),深证成交量(亿),创业板指数(%),创业板成交量(亿),序号,行业名称,涨跌幅,领涨股,涨跌幅,时间\n");
		SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Map<String, Float> indexMap = indexService.queryAllChangePctAndVolumnAmnt();
		if(indexMap == null){
			return;
		}
		for(IndustryHot industryHot : industryHotList){
			// 排除掉所有涨跌幅是0.0的, 因为极可能是停牌.
			if(industryHot.getChangePercent() == 0){
				continue;
			}
			String tradeDateStr = format1.format(industryHot.getTradeDate());
			
			String changePctSHKey = tradeDateStr + "_" + "上证指数" + "_" + "涨跌幅";
			String changePctSZKey = tradeDateStr + "_" + "深证成指" + "_" + "涨跌幅";
			String changePctCZKey = tradeDateStr + "_" + "创业板指" + "_" + "涨跌幅";
			String volumnAmntSHKey = tradeDateStr + "_" + "上证指数" + "_" + "成交金额";
			String volumnAmntSZKey = tradeDateStr + "_" + "深证成指" + "_" + "成交金额";
			String volumnAmntCZKey = tradeDateStr + "_" + "创业板指" + "_" + "成交金额";
			String changePctSH = indexMap.containsKey(changePctSHKey) ? String.valueOf(indexMap.get(changePctSHKey)) : "";
			String changePctSZ = indexMap.containsKey(changePctSZKey) ? String.valueOf(indexMap.get(changePctSZKey)) : "";
			String changePctCZ = indexMap.containsKey(changePctCZKey) ? String.valueOf(indexMap.get(changePctCZKey)) : "";
			String volumnAmntSH = indexMap.containsKey(volumnAmntSHKey) ? String.valueOf(indexMap.get(volumnAmntSHKey)) : "";
			String volumnAmntSZ = indexMap.containsKey(volumnAmntSZKey) ? String.valueOf(indexMap.get(volumnAmntSZKey)) : "";
			String volumnAmntCZ = indexMap.containsKey(volumnAmntCZKey) ? String.valueOf(indexMap.get(volumnAmntCZKey)) : "";
					
			sb.append(tradeDateStr).append(CSV_SPLIT)
			.append(changePctSH).append(CSV_SPLIT)
			.append(volumnAmntSH).append(CSV_SPLIT)
			.append(changePctSZ).append(CSV_SPLIT)
			.append(volumnAmntSZ).append(CSV_SPLIT)
			.append(changePctCZ).append(CSV_SPLIT)
			.append(volumnAmntCZ).append(CSV_SPLIT)
			.append(industryHot.getRank()).append(CSV_SPLIT)
			.append(industryHot.getIndustryName()).append(CSV_SPLIT)
			.append(industryHot.getChangePercent()).append(CSV_SPLIT)
			.append(industryHot.getRiseLeadStockName()).append(CSV_SPLIT)
			.append(industryHot.getRiseLeadStockChangePercent()).append(CSV_SPLIT)
			.append(format2.format(industryHot.getTimestamp()))
			.append("\n");
		}
		StockUtils.writeToFile("/home/leslie/MyProject/StockAnalyse/gen/industryHot" + "_" + year + ".csv", sb, "GB2312");
		long end = System.currentTimeMillis();
		LOGGER.info("elapsed time: " + (end - begin)/(1000) + " s.");
	}
	
	/**
	 * 生成行业板块中的个股的csv文件.  按照个股的涨幅从高到低排序.
	 */
	public void genIndustryHotStockCsv(int year) {
		long begin = System.currentTimeMillis();
		List<IndustryHotStocks> list = industryHotStocksService.queryIndustryHotStocksForCsv(year);
		if(list == null){
			return;
		}
		LOGGER.info("queryIndustryHotStocksForCsv elapsed time: " + (System.currentTimeMillis() - begin)/1000 + " s.");
		
		Map<String, Integer> nameNumMap = industryInfoService.queryIndustryCorpsNumMap(StockConstant.THS_FLAG);
		if(nameNumMap == null){
			return;
		}
		
		StringBuilder sb = new StringBuilder("日期,序号,行业名称,涨跌幅,序号,股票代码,股票名称,最新价,涨跌幅,时间\n");
		SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		// 显示多少个股票
		int rankLimit = 30;
		for(IndustryHotStocks stock : list){
			String industryName = stock.getIndustryName();
			int industryRank = stock.getIndustryRank();
			int rank = stock.getRank();
			if(stock.getChangePercent() == 0){
				continue;
			}
			if(industryRank < 50 && rank > rankLimit){
				continue;
			}
			if(nameNumMap.containsKey(industryName)){
				int corpsNum = nameNumMap.get(industryName);
				if(industryRank > 50 && rank < (corpsNum - rankLimit)){
					continue;
				}
			}
			
			sb.append(format1.format(stock.getTradeDate())).append(CSV_SPLIT)
			.append(industryRank).append(CSV_SPLIT)
			.append(industryName).append(CSV_SPLIT)
			.append(stock.getIndustryChangePercent()).append(CSV_SPLIT)
			.append(rank).append(CSV_SPLIT)
			.append(stock.getCode()).append(CSV_SPLIT)
			.append(stock.getStockName()).append(CSV_SPLIT)
			.append(stock.getNewPrice()).append(CSV_SPLIT)
			.append(stock.getChangePercent()).append(CSV_SPLIT)
			.append(format2.format(stock.getTimestamp())).append(CSV_SPLIT)
			.append("\n");
		}
		
		long beforeWriteTime = System.currentTimeMillis();
		StockUtils.writeToFile("/home/leslie/MyProject/StockAnalyse/gen/industryHotStocks" + "_" + year + ".csv", sb, "GB2312");
		LOGGER.info("writeToFile elapsed time: " + (System.currentTimeMillis() - beforeWriteTime)/1000 + " s.");
		
		long end = System.currentTimeMillis();
		LOGGER.info("total elapsed time: " + (end - begin)/(1000) + " s.");
	}

	/**
	 * 生成概念板块中的个股的csv文件.  按照个股的涨幅从高到低排序.
	 */
	public void genNotionHotStockCsv(int year) {
		long begin = System.currentTimeMillis();
		List<NotionHotStocks> list = notionHotStocksService.queryNotionHotStocksForCsv(year);
		if(list == null){
			return;
		}
		LOGGER.info("queryNotionHotStocksForCsv elapsed time: " + (System.currentTimeMillis() - begin)/1000 + " s.");
		
		Map<String, Integer> nameNumMap = notionInfoService.queryNotionNameNumMap(StockConstant.THS_FLAG);
		if(nameNumMap == null){
			return;
		}
		
		StringBuilder sb = new StringBuilder("日期,序号,概念名称,涨跌幅,序号,股票代码,股票名称,最新价,涨跌幅,时间\n");
		SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		// 显示多少个股票
		int rankLimit = 30;
		for(NotionHotStocks stock : list){
			String notionName = stock.getNotionName();
			int notionRank = stock.getNotionRank();
			int rank = stock.getRank();
			if(stock.getChangePercent() == 0){
				continue;
			}
			if(notionRank < 50 && rank > rankLimit){
				continue;
			}
			if(nameNumMap.containsKey(notionName)){
				int corpsNum = nameNumMap.get(notionName);
				if(notionRank > 50 && rank < (corpsNum - rankLimit)){
					continue;
				}
			}
			
			sb.append(format1.format(stock.getTradeDate())).append(CSV_SPLIT)
			.append(notionRank).append(CSV_SPLIT)
			.append(notionName).append(CSV_SPLIT)
			.append(stock.getNotionChangePercent()).append(CSV_SPLIT)
			.append(rank).append(CSV_SPLIT)
			.append(stock.getCode()).append(CSV_SPLIT)
			.append(stock.getStockName()).append(CSV_SPLIT)
			.append(stock.getNewPrice()).append(CSV_SPLIT)
			.append(stock.getChangePercent()).append(CSV_SPLIT)
			.append(format2.format(stock.getTimestamp())).append(CSV_SPLIT)
			.append("\n");
		}
		
		long beforeWriteTime = System.currentTimeMillis();
		StockUtils.writeToFile("/home/leslie/MyProject/StockAnalyse/gen/notionHotStocks" + "_" + year + ".csv", sb, "GB2312");
		LOGGER.info("writeToFile elapsed time: " + (System.currentTimeMillis() - beforeWriteTime)/1000 + " s.");
		
		long end = System.currentTimeMillis();
		LOGGER.info("total elapsed time: " + (end - begin)/(1000) + " s.");
	}
	
	public StringBuilder getNotionHotPhraseString(String lowTradeDateStr, String highTradeDateStr, boolean header) {
		long begin = System.currentTimeMillis();
		// lowTradeDateStr <= tradeDate <= highTradeDateStr
		List<NotionHot> notionHotList = notionHotService.queryNotionHotByDateStrBetween(lowTradeDateStr, highTradeDateStr);
		if(notionHotList == null || notionHotList.isEmpty()){
			LOGGER.info(lowTradeDateStr + " <= tradeDate <= " + highTradeDateStr + " not exists, return now...");
			return null;
		}
		
		Map<String, Float> map = new HashMap<String, Float>();
		for(NotionHot notionHot : notionHotList){
			String notionName = notionHot.getNotionName();
			if(map.containsKey(notionName)){
				map.put(notionName, map.get(notionName) + notionHot.getChangePercent());
			}else{
				map.put(notionName, notionHot.getChangePercent());
			}
		}
		
		// 大盘指数总涨跌幅
		Map<String, Float> indexMap = indexService.queryTotalChangePctBetweenMap(lowTradeDateStr, highTradeDateStr);
		if(indexMap == null){
			LOGGER.info("查询大盘指数总涨跌幅出错.");
			return null;
		}
		
		// 概念板块的公司家数.
		Map<String, Integer> notionCorpsNumMap = notionInfoService.queryNotionCorpsNumMap(StockConstant.THS_FLAG);
		if(notionCorpsNumMap == null){
			LOGGER.info("查询板块公司家数出错, return now...");
			return null;
		}
		
		StringBuilder sb = new StringBuilder("");
		if(header){
			sb.append("时间段,上证总涨跌幅(%),深成总涨跌幅(%),创业总涨跌幅(%),序号,概念名称,总涨跌幅(%),公司家数\n");
		}
		sb.append(lowTradeDateStr + " - " + highTradeDateStr).append(CSV_SPLIT)
		.append(indexMap.get("上证指数")).append(CSV_SPLIT)
		.append(indexMap.get("深证成指")).append(CSV_SPLIT)
		.append(indexMap.get("创业板指")).append("\n");
		
		// 按照Map中value降序排列;
		List<Map.Entry<String,Float>> sortList=new ArrayList<Map.Entry<String,Float>>();
		sortList.addAll(map.entrySet());  
        Collections.sort(sortList, StockUtils.descMapComparatorFloat);
        int index = 0;
		// 保留2位小数
		DecimalFormat df = new DecimalFormat("####.00");  
        for(Map.Entry<String,Float> entry : sortList){
        	String notionName = entry.getKey();
        	sb.append(CSV_SPLIT)
        	.append(CSV_SPLIT)
        	.append(CSV_SPLIT)
        	.append(CSV_SPLIT)
        	.append(++index).append(CSV_SPLIT)
        	.append(notionName).append(CSV_SPLIT)
        	.append(df.format(entry.getValue())).append(CSV_SPLIT)
        	.append(notionCorpsNumMap.containsKey(notionName) ? notionCorpsNumMap.get(notionName) : 0).append("\n");
        }
        sb.append("\n");
        
		long end = System.currentTimeMillis();
		LOGGER.info("total elapsed time: " + (end - begin)/(1000) + " s.");
		return sb;
	}
	
	public StringBuilder getIndustryHotPhraseString(String lowTradeDateStr, String highTradeDateStr, boolean header) {
		long begin = System.currentTimeMillis();
		// lowTradeDateStr <= tradeDate <= highTradeDateStr
		List<IndustryHot> industryHotList = industryHotService.queryIndustryHotByDateStrBetween(lowTradeDateStr, highTradeDateStr);
		if(industryHotList == null || industryHotList.isEmpty()){
			LOGGER.info(lowTradeDateStr + " <= tradeDate <= " + highTradeDateStr + " not exists, return now...");
			return null;
		}
		
		Map<String, Float> map = new HashMap<String, Float>();
		for(IndustryHot industryHot : industryHotList){
			String industryName = industryHot.getIndustryName();
			if(map.containsKey(industryName)){
				map.put(industryName, map.get(industryName) + industryHot.getChangePercent());
			}else{
				map.put(industryName, industryHot.getChangePercent());
			}
		}
		
		// 大盘指数总涨跌幅
		Map<String, Float> indexMap = indexService.queryTotalChangePctBetweenMap(lowTradeDateStr, highTradeDateStr);
		if(indexMap == null){
			LOGGER.info("查询大盘指数总涨跌幅出错.");
			return null;
		}
		
		// 行业板块的公司家数.
		Map<String, Integer> industryCorpsNumMap = industryInfoService.queryIndustryCorpsNumMap(StockConstant.THS_FLAG);
		if(industryCorpsNumMap == null){
			LOGGER.info("查询板块公司家数出错, return now...");
			return null;
		}
		
		StringBuilder sb = new StringBuilder("");
		if(header){
			sb.append("时间段,上证总涨跌幅(%),深成总涨跌幅(%),创业总涨跌幅(%),序号,行业名称,总涨跌幅(%),公司家数\n");
		}
		sb.append(lowTradeDateStr + " - " + highTradeDateStr).append(CSV_SPLIT)
		.append(indexMap.get("上证指数")).append(CSV_SPLIT)
		.append(indexMap.get("深证成指")).append(CSV_SPLIT)
		.append(indexMap.get("创业板指")).append("\n");
		
		// 按照Map中value降序排列;
		List<Map.Entry<String,Float>> sortList=new ArrayList<Map.Entry<String,Float>>();
		sortList.addAll(map.entrySet());  
        Collections.sort(sortList, StockUtils.descMapComparatorFloat);
        int index = 0;
		// 保留2位小数
		DecimalFormat df = new DecimalFormat("####.00");  
        for(Map.Entry<String,Float> entry : sortList){
        	String industryName = entry.getKey();
        	sb.append(CSV_SPLIT)
        	.append(CSV_SPLIT)
        	.append(CSV_SPLIT)
        	.append(CSV_SPLIT)
        	.append(++index).append(CSV_SPLIT)
        	.append(industryName).append(CSV_SPLIT)
        	.append(df.format(entry.getValue())).append(CSV_SPLIT)
        	.append(industryCorpsNumMap.containsKey(industryName) ? industryCorpsNumMap.get(industryName) : 0).append("\n");
        }
        sb.append("\n");
        
		long end = System.currentTimeMillis();
		LOGGER.info("total elapsed time: " + (end - begin)/(1000) + " s.");
		return sb;
	}
	
	public StringBuilder getIndustryHotStocksPhraseString(String lowTradeDateStr,
			String highTradeDateStr, boolean header) {
		long begin = System.currentTimeMillis();
		// lowTradeDateStr <= tradeDate <= highTradeDateStr
		List<IndustryHotStocks> industryHotStocksList = industryHotStocksService.queryIndustryHotStocksByDateStrBetween(lowTradeDateStr, highTradeDateStr);
		if(industryHotStocksList == null || industryHotStocksList.isEmpty()){
			LOGGER.info(lowTradeDateStr + " <= tradeDate <= " + highTradeDateStr + " not exists, return now...");
			return null;
		}
		
		// 股票所属板块
		Map<String, String> stockIndustriesMap = industryHotStocksService.queryStocksIndustriesBetweenMap(lowTradeDateStr, highTradeDateStr);
		
		// 股票代码
		Map<String, String> codeMap = stockInfoService.queryStockNameCodeMap();
		
		// 股票区间总的涨跌幅, 这里仍然使用notion
		Map<String, Float> stockChangePctMap = notionHotStocksService.queryStocksChangePctBetweenMap(lowTradeDateStr, highTradeDateStr);
		
		StringBuilder sb = new StringBuilder("");
		if(header){
			sb.append("时间段,序号,股票代码,股票名称,总涨跌幅(%),所属行业\n");
		}
		sb.append(lowTradeDateStr + " - " + highTradeDateStr).append("\n");
        
		// 保留2位小数
		DecimalFormat df = new DecimalFormat("####.00");
		// 只输出前后部分个股.
		int limit = 100;
		// 按照Map中value降序排列;
		List<Map.Entry<String,Float>> sortList=new ArrayList<Map.Entry<String,Float>>();
		sortList.addAll(stockChangePctMap.entrySet());  
        Collections.sort(sortList, StockUtils.descMapComparatorFloat);
        int index = 0;
        int total = sortList.size();
        for(Map.Entry<String,Float> entry : sortList){
        	index++;
        	if(index > limit && index < total - limit){
        		continue;
        	}
        	String stockName = entry.getKey();
        	String stockCode = codeMap.containsKey(stockName) ? codeMap.get(stockName) : "";
        	String industryNameStr = stockIndustriesMap.containsKey(stockName) ? stockIndustriesMap.get(stockName) : "";
        	
        	sb.append(CSV_SPLIT)
        	.append(index).append(CSV_SPLIT)
        	.append(stockCode).append(CSV_SPLIT)
        	.append(stockName).append(CSV_SPLIT)
        	.append(df.format(entry.getValue())).append(CSV_SPLIT)
        	.append(industryNameStr).append("\n");
        }
        sb.append("\n");
        
		long end = System.currentTimeMillis();
		LOGGER.info("total elapsed time: " + (end - begin)/(1000) + " s.");
		return sb;
	}
	
	public StringBuilder getNotionHotStocksPhraseString(String lowTradeDateStr,
			String highTradeDateStr, boolean header) {
		long begin = System.currentTimeMillis();
		// lowTradeDateStr <= tradeDate <= highTradeDateStr
		List<NotionHotStocks> notionHotStocksList = notionHotStocksService.queryNotionHotStocksByDateStrBetween(lowTradeDateStr, highTradeDateStr);
		if(notionHotStocksList == null || notionHotStocksList.isEmpty()){
			LOGGER.info(lowTradeDateStr + " <= tradeDate <= " + highTradeDateStr + " not exists, return now...");
			return null;
		}
		
		// 股票所属板块
		Map<String, String> stockNotionsMap = notionHotStocksService.queryStocksNotionsBetweenMap(lowTradeDateStr, highTradeDateStr);
		
		// 股票代码
		Map<String, String> codeMap = stockInfoService.queryStockNameCodeMap();
		
		// 股票区间总的涨跌幅
		Map<String, Float> stockChangePctMap = notionHotStocksService.queryStocksChangePctBetweenMap(lowTradeDateStr, highTradeDateStr);
		
		StringBuilder sb = new StringBuilder("");
		if(header){
			sb.append("时间段,序号,股票代码,股票名称,总涨跌幅(%),所属概念\n");
		}
		sb.append(lowTradeDateStr + " - " + highTradeDateStr).append("\n");
        
		// 保留2位小数
		DecimalFormat df = new DecimalFormat("####.00");
		// 只输出前后部分个股.
		int limit = 100;
		// 按照Map中value降序排列;
		List<Map.Entry<String,Float>> sortList=new ArrayList<Map.Entry<String,Float>>();
		sortList.addAll(stockChangePctMap.entrySet());  
        Collections.sort(sortList, StockUtils.descMapComparatorFloat);
        int index = 0;
        int total = sortList.size();
        for(Map.Entry<String,Float> entry : sortList){
        	index++;
        	if(index > limit && index < total - limit){
        		continue;
        	}
        	String stockName = entry.getKey();
        	String stockCode = codeMap.containsKey(stockName) ? codeMap.get(stockName) : "";
        	String notionNameStr = stockNotionsMap.containsKey(stockName) ? stockNotionsMap.get(stockName) : "";
        	
        	sb.append(CSV_SPLIT)
        	.append(index).append(CSV_SPLIT)
        	.append(stockCode).append(CSV_SPLIT)
        	.append(stockName).append(CSV_SPLIT)
        	.append(df.format(entry.getValue())).append(CSV_SPLIT)
        	.append(notionNameStr).append("\n");
        }
        sb.append("\n");
        
		long end = System.currentTimeMillis();
		LOGGER.info("total elapsed time: " + (end - begin)/(1000) + " s.");
		return sb;
	}
	
	public StockCapFlowService getStockCapFlowService() {
		return stockCapFlowService;
	}

	public void setStockCapFlowService(StockCapFlowService stockCapFlowService) {
		this.stockCapFlowService = stockCapFlowService;
	}

	public StockInfoService getStockInfoService() {
		return stockInfoService;
	}

	public void setStockInfoService(StockInfoService stockInfoService) {
		this.stockInfoService = stockInfoService;
	}

	public IndustryStockService getIndustryStockService() {
		return industryStockService;
	}

	public void setIndustryStockService(IndustryStockService industryStockService) {
		this.industryStockService = industryStockService;
	}

	public NotionHotService getNotionHotService() {
		return notionHotService;
	}

	public void setNotionHotService(NotionHotService notionHotService) {
		this.notionHotService = notionHotService;
	}

	public IndustryHotService getIndustryHotService() {
		return industryHotService;
	}

	public void setIndustryHotService(IndustryHotService industryHotService) {
		this.industryHotService = industryHotService;
	}

	public NotionHotStocksService getNotionHotStocksService() {
		return notionHotStocksService;
	}

	public void setNotionHotStocksService(
			NotionHotStocksService notionHotStocksService) {
		this.notionHotStocksService = notionHotStocksService;
	}

	public NotionInfoService getNotionInfoService() {
		return notionInfoService;
	}

	public void setNotionInfoService(NotionInfoService notionInfoService) {
		this.notionInfoService = notionInfoService;
	}

	public IndustryInfoService getIndustryInfoService() {
		return industryInfoService;
	}

	public void setIndustryInfoService(IndustryInfoService industryInfoService) {
		this.industryInfoService = industryInfoService;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public IndustryHotStocksService getIndustryHotStocksService() {
		return industryHotStocksService;
	}

	public void setIndustryHotStocksService(
			IndustryHotStocksService industryHotStocksService) {
		this.industryHotStocksService = industryHotStocksService;
	}

}
