/**
 * 
 */
package com.wy.stock.utils;

/**
 * @author leslie
 *
 */
public interface StockConstant {
	
	/*
	 * 网上获取股票历史数据文件的url前缀.
	 */
	String STOCK_CSV_URL_PRE = "http://table.finance.yahoo.com/table.csv?s=";
	
	/*
	 * 本地存储历史数据的目录.
	 */
	String HISTORY_FILE_PATH = "/home/leslie/MyProject/StockAnalyse/history/";
	
	/*
	 * 概念热点、行业热点html文件目录 
	 */
	String BOARD_HOT_FILE_PATH = "/home/leslie/MyProject/StockAnalyse/html/boardHot/";
	
	/*
	 * 个股资金文件目录, 仍然放在html目录下.
	 */
	String CAP_FLOW_FILE_PATH = "/home/leslie/MyProject/StockAnalyse/html/capFlow/";
	
	/*
	 * 龙虎榜文件目录.
	 */
	String LHB_FILE_PATH = "/home/leslie/MyProject/StockAnalyse/html/LHB/";
	
	/*
	 * 需要清理的状态为R的超时时间. (单位: s)
	 */
	int DEL_R_UPPER_LIMIT = 12000;
	
	/**
	 * job 的状态
	 */
	String JOB_STATE_RUNNING = "R";
	String JOB_STATE_SUCCESS = "S";
	String JOB_STATE_FAILED = "F";
	String JOB_STATE_DELETE = "D";
	
	/*
	 * 需要详细查询的概念热点板块个数.
	 */
	int NOTION_HOT_UPPER = 20;
	
	/*
	 * 需要详细查询的行业热点板块个数.
	 */
	int INDUSTRY_HOT_UPPER = 20;
	
	/*
	 * 概念热点下查询的股票个数
	 */
	int NOTION_HOT_STOCKS_UPPER = 20;
	
	/*
	 * 行业热点下查询的股票个数
	 */
	int INDUSTRY_HOT_STOCKS_UPPER = 20;
	
	/*
	 * 概念名称
	 */
	String NOTION_IDENTIFIER = "notion";
	
	/*
	 * 行业名称
	 */
	String INDUSTRY_IDENTIFIER = "industry";
	
	String NOTION_HOT_IDENTIFIER = "notionHot";
	String INDUSTRY_HOT_IDENTIFIER = "industryHot";
	
	/*
	 * 同花顺标识: THS
	 */
	String THS_FLAG = "THS";
	
	/*
	 * 东方财富标识: DFCF
	 */
	String DFCF_FLAG = "DFCF";
	
	/**
	 * 文件名称
	 */
	String NOTION_HOT_HTML_FILE = "notionHot.html";
	String INDUSTRY_HOT_HTML_FILE = "industryHot.html";
	String INDEX_HTML_FILE = "index.html";
	
	/**
	 * JOB 类型标识.
	 */
	String JOB_TYPE_FIVE_CHANGE = "U";
	String JOB_TYPE_HISTORY = "P";
	String JOB_TYPE_THS_BOARD_HOT = "T";
	String JOB_TYPE_CAP_FLOW = "C";
	String JOB_TYPE_CAP_FLOW_DETAIL = "E";
	String JOB_TYPE_NOTION_INDUSTRY_INFO = "I";
	String JOB_TYPE_FETCH_STOCK_INFO = "G";
	String JOB_TYPE_THS_LHB = "R";
	
	/**
	 * config 配置参数
	 */
	String PERSIST_CAP_FLOW_MODE = "PERSIST_CAP_FLOW_MODE";
	
	/**
	 * 概念板块列表每页行数: http://q.10jqka.com.cn/stock/gn/
	 */
	int NOTION_HOT_PAGE_SIZE = 50;
	
	/**
	 * 行业板块列表每页行数: http://q.10jqka.com.cn/stock/thshy/
	 */
	int INDUSTRY_HOT_PAGE_SIZE = 50;
	
	/**
	 * ST_NOTION_INFO 中 type  A0:同花顺开始的概念页面.   A1:同花顺改版后的概念页面
	 */
	String THS_NOTION_TYPE0 = "A0";
	String THS_NOTION_TYPE1 = "A1";
	
}
