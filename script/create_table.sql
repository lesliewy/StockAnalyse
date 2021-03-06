-- 股票信息表 EXCHANGE --交易所名称, 如: 上证 深证   TYPE--股票市场类型  A B H 
CREATE TABLE ST_INFO(
	NAME VARCHAR(30) NOT NULL,
	CODE VARCHAR(6) NOT NULL,
	EXCHANGE VARCHAR(10) NOT NULL, 
	TYPE VARCHAR(1) NOT NULL,       
        TIMESTAMP DATETIME NOT NULL,
        PRIMARY KEY (CODE, EXCHANGE, TYPE)
);

-- 股票历史表, 解析从yahoo获取的csv文件.
CREATE TABLE ST_HISTORY(
	CODE VARCHAR(6) NOT NULL,
	EXCHANGE VARCHAR(10) NOT NULL, 
	TYPE VARCHAR(1) NOT NULL,       
	TRADE_DATE DATETIME NOT NULL,
	OPEN NUMERIC(7,2) NOT NULL,
	HIGH NUMERIC(7,2) NOT NULL,
	LOW NUMERIC(7,2) NOT NULL,
	CLOSE NUMERIC(7,2) NOT NULL,
	VOLUMN INT NOT NULL,
	ADJ_CLOSE NUMERIC(10,5) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
        PRIMARY KEY(CODE, EXCHANGE, TYPE, TRADE_DATE) 
);

-- 记录定时JOB信息.  JOB_DATE类型: 1506171756(YYMMDDHHmm)
CREATE TABLE ST_JOB(
        JOB_DATE VARCHAR(10) NOT NULL,
        JOB_TYPE VARCHAR(2) NOT NULL,
        STATUS VARCHAR(1) NOT NULL,
        REMARK VARCHAR(50) NOT NULL,
        BEGIN_TIME DATETIME NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
        PRIMARY KEY(JOB_DATE, JOB_TYPE) 
);

-- 记录均线信息.
CREATE TABLE ST_MA(
	CODE VARCHAR(6) NOT NULL,
	EXCHANGE VARCHAR(10) NOT NULL, 
	TYPE VARCHAR(1) NOT NULL,       
	TRADE_DATE DATETIME NOT NULL,
        PERIOD INT NOT NULL,
        VALUE NUMERIC(7,2) NOT NULL,	
        TIMESTAMP DATETIME NOT NULL,
        PRIMARY KEY(CODE, EXCHANGE, TYPE, TRADE_DATE, PERIOD) 
);

-- 记录概念热点板块信息
CREATE TABLE ST_NOTION_HOT(
	TRADE_DATE DATETIME NOT NULL,
	RANK INT NOT NULL,
	NOTION_NAME VARCHAR(20) NOT NULL,
        CHANGE_PCT NUMERIC(4,2) NOT NULL,
	TOT_MARKET_CAP NUMERIC(8,2) NOT NULL,   -- 单位: 亿
	TURNOVER_RATE NUMERIC(8,2) NOT NULL,
	RISE_STOCKS_NUM INT NOT NULL,
	FALL_STOCKS_NUM INT NOT NULL,
	RISE_LEAD_STOCK_NAME VARCHAR(20) NOT NULL,
	RISE_LEAD_STOCK_CHANGE_PCT NUMERIC(4,2) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
        PRIMARY KEY(TRADE_DATE, RANK)	
);

-- 记录行业热点板块信息
CREATE TABLE ST_INDUSTRY_HOT(
	TRADE_DATE DATETIME NOT NULL,
	RANK INT NOT NULL,
	INDUSTRY_NAME VARCHAR(20) NOT NULL,
        CHANGE_PCT NUMERIC(4,2) NOT NULL,
	TOT_MARKET_CAP NUMERIC(8,2) NOT NULL,   -- 单位: 亿
	TURNOVER_RATE NUMERIC(8,2) NOT NULL,
	RISE_STOCKS_NUM INT NOT NULL,
	FALL_STOCKS_NUM INT NOT NULL,
	RISE_LEAD_STOCK_NAME VARCHAR(20) NOT NULL,
	RISE_LEAD_STOCK_CHANGE_PCT NUMERIC(4,2) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
        PRIMARY KEY(TRADE_DATE, RANK)	
   
);

--记录概念热点板块内热点个股信息
CREATE TABLE ST_NOTION_HOT_STOCKS(
        TRADE_DATE DATETIME NOT NULL,
	NOTION_NAME VARCHAR(20) NOT NULL,
	RANK INT NOT NULL,
	CODE VARCHAR(6) NOT NULL,
	STOCK_NAME VARCHAR(30) NOT NULL,
	NEW NUMERIC(7,2) NOT NULL,   -- 最新价
        CHANGE_PCT NUMERIC(4,2) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(TRADE_DATE, NOTION_NAME, RANK)
);

--记录行业热点板块内热点个股信息
CREATE TABLE ST_INDUSTRY_HOT_STOCKS(
        TRADE_DATE DATETIME NOT NULL,
	INDUSTRY_NAME VARCHAR(20) NOT NULL,
	RANK INT NOT NULL,
	CODE VARCHAR(6) NOT NULL,
	STOCK_NAME VARCHAR(30) NOT NULL,
	NEW NUMERIC(7,2) NOT NULL,   -- 最新价
        CHANGE_PCT NUMERIC(4,2) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(TRADE_DATE, INDUSTRY_NAME, RANK)
);

-- 记录概念热点板块代码
CREATE TABLE ST_NOTION_INFO(
        NOTION_URL VARCHAR(60) NOT NULL,
	NOTION_NAME VARCHAR(20) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(NOTION_URL)
);

-- 记录行业热点板块代码
CREATE TABLE ST_INDUSTRY_INFO(
        INDUSTRY_URL VARCHAR(60) NOT NULL,
	INDUSTRY_NAME VARCHAR(20) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(INDUSTRY_URL)
);

-- 记录重要指数
CREATE TABLE ST_INDEX(
	TRADE_DATE DATETIME NOT NULL,
	INDEX_CODE VARCHAR(10) NOT NULL,
	INDEX_NAME VARCHAR(20) NOT NULL,
	NEW NUMERIC(7,2) NOT NULL,   -- 最新价 通常取值收盘价
	CHANGE_AMOUNT NUMERIC(7,2) NOT NULL,    -- 涨跌额
        CHANGE_PCT NUMERIC(4,2) NOT NULL, 
	OPEN NUMERIC(7,2) NOT NULL,
	HIGH NUMERIC(7,2) NOT NULL,
	LOW NUMERIC(7,2) NOT NULL,
	CLOSE NUMERIC(7,2) NOT NULL,
	LAST_CLOSE NUMERIC(7,2) NOT NULL,  -- 昨收
        AMPLITUDE NUMERIC(4,2) NOT NULL,   -- 振幅
	VOLUMN NUMERIC(7,2) NOT NULL,       -- 成交量(万手)
	VOLUMN_AMOUNT NUMERIC(7,2) NOT NULL, -- 成交金额(亿元)
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(TRADE_DATE, INDEX_CODE)
);

-- 记录概念下的股票信息
CREATE TABLE ST_NOTION_STOCK(
	NOTION_CODE VARCHAR(10) NOT NULL,
	NOTION_NAME VARCHAR(20) NOT NULL,
	CODE VARCHAR(6) NOT NULL,
	STOCK_NAME VARCHAR(30) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(NOTION_CODE, CODE)
);
-- 记录行业下的股票信息
CREATE TABLE ST_INDUSTRY_STOCK(
	INDUSTRY_CODE VARCHAR(10) NOT NULL,
	INDUSTRY_NAME VARCHAR(20) NOT NULL,
	CODE VARCHAR(6) NOT NULL,
	STOCK_NAME VARCHAR(30) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(INDUSTRY_CODE, CODE)
);

-- 记录个股5分钟涨跌幅. 
CREATE TABLE ST_STOCK_FIVE_CHANGE(
	CODE VARCHAR(6) NOT NULL,
	STOCK_NAME VARCHAR(30) NOT NULL,
        CHANGE_PCT NUMERIC(4,2) NOT NULL,
        FIVE_CHANGE_PCT NUMERIC(4,2) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(CODE)
);

-- 记录个股资金流向.
CREATE TABLE ST_STOCK_CAP_FLOW(
	TRADE_DATE DATETIME NOT NULL,
	CODE VARCHAR(6) NOT NULL,
	CLOSE NUMERIC(7,2) NOT NULL,
        CHANGE_PCT NUMERIC(4,2) NOT NULL,
	MAIN_NET_IN NUMERIC(10,2) NOT NULL,                 -- 主力净流入, 注意这里数值的单位是 万
        MAIN_NET_IN_PCT NUMERIC(5,2) NOT NULL,              -- 主力净占比，主力净流入占当天成交量的比例, 有100.00的情况
        SUPER_LARGE NUMERIC(10,2) NOT NULL,                 -- 超大单
        SUPER_LARGE_PCT NUMERIC(5,2) NOT NULL,	
	LARGE NUMERIC(10,2) NOT NULL,                       -- 大单
	LARGE_PCT NUMERIC(5,2) NOT NULL,
	MIDDLE NUMERIC(10,2) NOT NULL,                      -- 中单
	MIDDLE_PCT NUMERIC(5,2) NOT NULL,
	SMALL NUMERIC(10,2) NOT NULL,                       -- 小单
	SMALL_PCT NUMERIC(5,2) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(TRADE_DATE, CODE)
);

-- 记录配置参数.
CREATE TABLE ST_CONFIG(
        NAME VARCHAR(50) NOT NULL,
        VALUE VARCHAR(100) NOT NULL,
        TIMESTAMP DATETIME NOT NULL,
        PRIMARY KEY(NAME)
);

-- 记录个股所属的概念, 没有个股所属行业表，ST_INDUSTRY_STOCK就可以用.
/*
CREATE TABLE ST_STOCK_NOTION(
	CODE VARCHAR(6) NOT NULL,
	STOCK_NAME VARCHAR(30) NOT NULL,
        NOTION_NAMES VARCHAR(200) NOT NULL,  -- 以|分隔
        NOTION_CODES VARCHAR(200) NOT NULL,  -- 以|分隔	
        TIMESTAMP DATETIME NOT NULL,
	PRIMARY KEY(CODE)
);
*/



ALTER TABLE ST_HISTORY MODIFY VOLUMN BIGINT NOT NULL; 

ALTER TABLE ST_NOTION_HOT ADD SOURCE VARCHAR(10); -- THS:同花顺  DFCF: 东方财富
ALTER TABLE ST_INDUSTRY_HOT ADD SOURCE VARCHAR(10);
ALTER TABLE ST_NOTION_INFO ADD SOURCE VARCHAR(10);
ALTER TABLE ST_INDUSTRY_INFO ADD SOURCE VARCHAR(10);

ALTER TABLE ST_NOTION_INFO ADD NOTION_CODE VARCHAR(10);
ALTER TABLE ST_INDUSTRY_INFO ADD INDUSTRY_CODE VARCHAR(10);

ALTER TABLE ST_NOTION_INFO ADD CORPS_NUM INT;
ALTER TABLE ST_INDUSTRY_INFO ADD CORPS_NUM INT;

ALTER TABLE ST_NOTION_INFO ADD TYPE VARCHAR(2);    -- 同花顺的概念板块url发生了改变, 添加此标识.
ALTER TABLE ST_NOTION_INFO DROP PRIMARY KEY;
ALTER TABLE ST_NOTION_INFO ADD PRIMARY KEY(TYPE, NOTION_URL);  -- 主键需要修改.

ALTER TABLE ST_NOTION_STOCK ADD SOURCE VARCHAR(10);
ALTER TABLE ST_INDUSTRY_STOCK ADD SOURCE VARCHAR(10);

ALTER TABLE ST_NOTION_HOT ADD AVG_PRICE NUMERIC(7,2);
ALTER TABLE ST_INDUSTRY_HOT ADD AVG_PRICE NUMERIC(7,2);

