--
SELECT A.TRADE_DATE DATE, C.INDEX_NAME I_NAME, C.CHANGE_PCT I_PCT, A.RANK N_R, A.NOTION_NAME N_NAME, A.CHANGE_PCT N_PCT, B.RANK S_RANK, B.STOCK_NAME S_NAME, B.NEW NEW, B.CHANGE_PCT S_PCT 
  FROM ST_NOTION_HOT A, ST_NOTION_HOT_STOCKS B, ST_INDEX C 
 WHERE LEFT(A.TRADE_DATE, 10) = LEFT(B.TRADE_DATE, 10)
       AND A.NOTION_NAME = B.NOTION_NAME 
       AND LEFT(A.TRADE_DATE,10) = LEFT(C.TRADE_DATE, 10)
       AND A.NOTION_NAME LIKE '%农业%' 
       AND C.INDEX_NAME='上证指数' 
ORDER BY A.TRADE_DATE, A.RANK, B.RANK;
--
SELECT A.TRADE_DATE DATE, C.INDEX_NAME I_NAME, C.VOLUMN_AMOUNT VOL_A, C.CHANGE_PCT I_PCT, A.RANK N_R, A.INDUSTRY_NAME N_NAME, A.CHANGE_PCT N_PCT, B.RANK S_RANK, B.STOCK_NAME S_NAME, B.NEW NEW, B.CHANGE_PCT S_PCT 
  FROM ST_INDUSTRY_HOT A, ST_INDUSTRY_HOT_STOCKS B, ST_INDEX C 
 WHERE LEFT(A.TRADE_DATE, 10) = LEFT(B.TRADE_DATE, 10)
       AND A.INDUSTRY_NAME = B.INDUSTRY_NAME 
       AND LEFT(A.TRADE_DATE,10) = LEFT(C.TRADE_DATE, 10)
       AND A.INDUSTRY_NAME LIKE '%农业%' 
       AND C.INDEX_NAME='上证指数' 
ORDER BY A.TRADE_DATE, A.RANK, B.RANK;

-- BEGIN
-- 导出成csv格式文件, 用到windows系统中，需要iconv转换下编码格式，列名需要手动添加到csv中.
-- com.wy.stock.tools.AnalyseStockToolImpl.genNotionHotCsv()
-- 热点板块   列名:日期,序号,概念名称,涨跌幅,领涨股,涨跌幅,时间
SELECT TRADE_DATE, RANK, NOTION_NAME, CHANGE_PCT, RISE_LEAD_STOCK_NAME, RISE_LEAD_STOCK_CHANGE_PCT, TIMESTAMP 
  FROM ST_NOTION_HOT 
 WHERE (RANK <= 30 OR RANK > 130) 
   AND SOURCE='THS' 
  ORDER BY LEFT(TRADE_DATE, 10), RANK
into outfile '/home/leslie/MyProject/StockAnalyse/script/notion.csv'   
fields terminated by ',' 
lines terminated by '\r\n'; 

-- 热点板块对应的热点股票   列名:日期,序号,概念名称,涨跌幅,序号,股票代码,股票名称,最新价,涨跌幅,时间
SELECT B.TRADE_DATE, A.RANK, B.NOTION_NAME, A.CHANGE_PCT, B.RANK, B.CODE, B.STOCK_NAME, B.NEW, B.CHANGE_PCT, B.TIMESTAMP 
  FROM ST_NOTION_HOT A, ST_NOTION_HOT_STOCKS B 
 WHERE LEFT(A.TRADE_DATE, 10) = LEFT(B.TRADE_DATE, 10) 
   AND A.NOTION_NAME=B.NOTION_NAME 
   AND (A.RANK <= 5 OR A.RANK > 130)
   AND B.RANK <= 5
   ORDER BY LEFT(B.TRADE_DATE, 10), A.RANK, B.RANK
into outfile '/home/leslie/MyProject/StockAnalyse/script/notionStocks.csv'   
fields terminated by ','
lines terminated by '\r\n';
-- END


-- BEGIN JOB T：　PersistBoardHotTHSJob  注意替换掉日期
DELETE FROM ST_JOB WHERE JOB_TYPE='T' AND JOB_DATE LIKE '161111%';
DELETE FROM ST_NOTION_HOT WHERE SOURCE='THS' AND TRADE_DATE LIKE '2016-11-11%';
DELETE FROM ST_INDUSTRY_HOT WHERE SOURCE='THS' AND TRADE_DATE LIKE '2016-11-11%';
DELETE FROM ST_NOTION_HOT_STOCKS WHERE TRADE_DATE LIKE '2016-11-11%';
DELETE FROM ST_INDUSTRY_HOT_STOCKS WHERE TRADE_DATE LIKE '2016-11-11%';
DELETE FROM ST_INDEX WHERE TRADE_DATE LIKE '2016-11-11%';

SELECT * FROM ST_JOB WHERE JOB_TYPE='T' AND JOB_DATE LIKE '161111%';
SELECT COUNT(*) FROM ST_NOTION_HOT WHERE SOURCE='THS' AND TRADE_DATE LIKE '2016-11-11%';
SELECT COUNT(*) FROM ST_INDUSTRY_HOT WHERE SOURCE='THS' AND TRADE_DATE LIKE '2016-11-11%';
SELECT COUNT(*) FROM ST_NOTION_HOT_STOCKS WHERE TRADE_DATE LIKE '2016-11-11%';
SELECT COUNT(*) FROM ST_INDUSTRY_HOT_STOCKS WHERE TRADE_DATE LIKE '2016-11-11%';
SELECT COUNT(*) FROM ST_INDEX WHERE TRADE_DATE LIKE '2016-11-11%';
-- END JOB T.

-- BEGIN: 爱问财下载历史数据(2016-04-25之前的)
SELECT DISTINCT TRADE_DATE, COUNT(*) FROM ST_NOTION_HOT WHERE SOURCE='THS' GROUP BY TRADE_DATE;
SELECT DISTINCT LEFT(TRADE_DATE, 10), COUNT(*) FROM ST_NOTION_HOT_STOCKS GROUP BY TRADE_DATE;
SELECT TRADE_DATE, COUNT(DISTINCT NOTION_NAME) FROM ST_NOTION_HOT_STOCKS WHERE TRADE_DATE LIKE '2016-01-0%' GROUP BY TRADE_DATE;
SELECT * FROM ST_NOTION_HOT WHERE TRADE_DATE = '2016-01-21 00:00:00' ORDER BY RANK ASC LIMIT 10;
SELECT NOTION_NAME, COUNT(*) FROM ST_NOTION_HOT_STOCKS WHERE LEFT(TRADE_DATE, 10)='2016-03-17' GROUP BY NOTION_NAME;
-- END.

-- 某个时间段概念板块总的涨跌幅.
SELECT NOTION_NAME, SUM(CHANGE_PCT) SUMPCT FROM ST_NOTION_HOT WHERE TRADE_DATE >= '2016-06-12' AND TRADE_DATE < '2016-07-09' GROUP BY NOTION_NAME ORDER BY SUMPCT DESC;
-- 某个时间段个股总的涨跌幅.
SELECT STOCK_NAME, SUM(CHANGE_PCT) SUMPCT FROM ST_NOTION_HOT_STOCKS WHERE TRADE_DATE >= '2016-06-12' AND TRADE_DATE < '2016-07-09' GROUP BY STOCK_NAME ORDER BY SUMPCT ASC LIMIT 100;
-- 个股所属的概念板块
SELECT STOCK_NAME, GROUP_CONCAT(DISTINCT NOTION_NAME SEPARATOR '|') NOTION_NAMES FROM ST_NOTION_HOT_STOCKS WHERE TRADE_DATE LIKE '2016-06-17%' GROUP BY STOCK_NAME;

