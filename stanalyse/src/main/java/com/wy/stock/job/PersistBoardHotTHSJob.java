/**
 * 
 */
package com.wy.stock.job;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wy.stock.domain.StockJob;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.AnalyseStockTool;
import com.wy.stock.tools.StockDownloadToolTHS;
import com.wy.stock.tools.StockParseToolTHS;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 同花顺: http://www.10jqka.com.cn/
 * 将概念板块信息(http://q.10jqka.com.cn/stock/gn/)所有的板块信息登记到 ST_NOTION_HOT, 板块中的股票涨跌幅靠前以及靠后的登记到 ST_NOTION_HOT_STOCKS;
 * 将同花顺行业信息(http://q.10jqka.com.cn/stock/thshy/)所有的板块信息登记到 ST_INDUSTRY_HOT, 板块中的股票涨跌幅靠前以及靠后的登记到 ST_INDUSTRY_HOT_STOCKS;
 * @author leslie
 *
 */
public class PersistBoardHotTHSJob {

	private static Logger LOGGER = Logger.getLogger(PersistBoardHotTHSJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockDownloadToolTHS stockDownloadToolTHS;
	
	private StockParseToolTHS stockParseToolTHS;
	
	private AnalyseStockTool analyseStockTool;
	
	private String jobType = StockConstant.JOB_TYPE_THS_BOARD_HOT;
	
	public void persistBoardHot(){
		LOGGER.info("JOB " + jobType + " - persistBoardHot job begin...");
		long begin = System.currentTimeMillis();
		
		Calendar cal = Calendar.getInstance();
		/*
		 *  周六、日不执行.注意法定节假日等不开市时也会执行,暂时没有添加.
		 */
		if(StockUtils.isWeekend(cal)){
			LOGGER.info("Saturday or Sunday, return now...");
			return;
		}
		
		/*
		 *  清理超长R状态的job, 修改状态为D.
		 */
		stockJobService.cleanLongTimeJob(StockConstant.DEL_R_UPPER_LIMIT, jobType);
		String jobDate = new SimpleDateFormat("YYMMddHHmm").format(Calendar.getInstance().getTime());
		
		/*
		 *  查询当前是否有正在运行的job
		 */
		List<StockJob> runningJobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_RUNNING);
		if(runningJobs != null && !runningJobs.isEmpty()){
			LOGGER.info("JOB " + jobType + " - running job exists: " + runningJobs + ". return now...");
			return;
		}
		
		/*
		 *  每天只执行一次
		 */
		List<StockJob> existedJobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_SUCCESS);
		if(existedJobs != null && !existedJobs.isEmpty()){
			LOGGER.info("JOB " + jobType + " - existes: " + existedJobs + ". return now...");
			return;
		}
		
		String remark = "running";
		String status = StockConstant.JOB_STATE_RUNNING;
		/*
		 *  登记当前的job
		 */
		StockJob job = new StockJob();
		job.setJobDate(jobDate);
		job.setJobType(jobType);
		job.setStatus(status);
		job.setRemark(remark);
		Timestamp beginTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		job.setBeginTime(beginTime);
		job.setTimestamp(beginTime);
		stockJobService.insertStockJob(job);
		
		/*
		 *  获取概念、行业板块文件， 放入指定的目录下.
		 */
		File savedDir = new File(StockUtils.getDailyStockSaveDir("B"));
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, "NOTION");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, "NOTION");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, "NOTION");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, "INDUSTRY");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, "INDUSTRY");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, "INDUSTRY");
		File notionHotHtml = new File(StockUtils.getDailyStockSaveDir("B") + StockConstant.NOTION_HOT_HTML_FILE);
		File industryHotHtml = new File(StockUtils.getDailyStockSaveDir("B") + StockConstant.INDUSTRY_HOT_HTML_FILE);
		if(!notionHotHtml.exists() || !industryHotHtml.exists()){
			LOGGER.info(notionHotHtml.getAbsolutePath() + " or " + industryHotHtml.getAbsolutePath() + " not exists, return now...");
			remark = "notionHot.html or industryHot.html not exists.";
			status = StockConstant.JOB_STATE_DELETE;
			job.setStatus(status);
			job.setRemark(remark);
			job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			stockJobService.updateRunningJob(job);
			return;
		}else if(notionHotHtml.length() < 30000 || industryHotHtml.length() < 30000){   // 这里根据文件大小判断下载的html是否是乱码.
			LOGGER.info(notionHotHtml.getAbsolutePath() + " or " + industryHotHtml.getAbsolutePath() + " is invalid, wait next time, return now...");
			notionHotHtml.delete();
			industryHotHtml.delete();
			remark = "notionHot.html or industryHot.html is invalid.";
			status = StockConstant.JOB_STATE_DELETE;
			job.setStatus(status);
			job.setRemark(remark);
			job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			stockJobService.updateRunningJob(job);
			return;
		}
		/*
		 * 获取概念、行业板块热点排名靠后的文件，放入指定目录下。 需要从排名靠前的html文件中先解析出总页数，然后根据页数构造排名靠后的url.
		 * 对于概念页面，没有列表，取消该操作.
		 */
		int totalPagesIndustry = stockParseToolTHS.getNotionIndustryHotTotalPages(industryHotHtml, "INDUSTRY");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, totalPagesIndustry, "INDUSTRY");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, totalPagesIndustry, "INDUSTRY");
		stockDownloadToolTHS.downloadBoardHotHtmlFiles(savedDir, totalPagesIndustry, "INDUSTRY");
		
		/*
		 * 解析概念、行业板块热点html文件并登记
		 * 对于概念页面，将概念相关信息存入ST_NOTION_INFO.
		 */
		stockParseToolTHS.persistNotionIndustryHot(savedDir, "NOTION");
		stockParseToolTHS.persistNotionIndustryHot(savedDir, "INDUSTRY");
		
		/*
		 * 获取概念、行业热点排名靠前的以及靠后的板块内的股票的文件. json文件.
		 * 对于概念页面, 需要下载notionUrl对应的页面，以及概念列表页面.  从ST_NOTION_INFO中获取相关信息来构造概念列表页面的url.
		 */
		String dateStr = String.valueOf(cal.get(Calendar.YEAR)).substring(2, 4) +
		StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0") +
		StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
		stockDownloadToolTHS.downloadBoardHotStocksFiles(dateStr, "NOTION");
		stockDownloadToolTHS.downloadBoardHotStocksFiles(dateStr, "NOTION");
		stockDownloadToolTHS.downloadBoardHotStocksFiles(dateStr, "NOTION");
		stockDownloadToolTHS.downloadBoardHotStocksFiles(dateStr, "INDUSTRY");
		stockDownloadToolTHS.downloadBoardHotStocksFiles(dateStr, "INDUSTRY");
		stockDownloadToolTHS.downloadBoardHotStocksFiles(dateStr, "INDUSTRY");
		
		/*
		 * 解析概念板块、行业板块内的股票信息.
		 */
		stockParseToolTHS.persistNotionIndustryHotStocksFromHtml(dateStr, "NOTION");
		stockParseToolTHS.persistNotionIndustryHotStocksFromHtml(dateStr, "INDUSTRY");
		
		/*
		 * 记录重要指数. ST_INDEX.
		 */
		stockDownloadToolTHS.downloadIndexFiles();
		stockDownloadToolTHS.downloadIndexFiles();
		stockDownloadToolTHS.downloadIndexFiles();
		stockParseToolTHS.persistIndexFromHtmls(savedDir);
		
		// 更新JOB状态.
		remark = "success";
		status = StockConstant.JOB_STATE_SUCCESS;
		job.setStatus(status);
		job.setRemark(remark);
		job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		stockJobService.updateRunningJob(job);
		
		LOGGER.info("JOB " + jobType + " finished. elapsed time: " + (System.currentTimeMillis() - begin)/(1000 * 60) + " min.");
		
		/*
		 *  产生文件.
		 */
		genFiles();
		LOGGER.info("JOB " + jobType + " genFiles finished. total time: " + (System.currentTimeMillis() - begin)/(1000 * 60) + " min.");
	}

	private void genFiles(){
		int year = 2017;
		analyseStockTool.genIndustryHotCsv(year);
	
		year = 2017;
		analyseStockTool.genNotionHotCsv(year);
	
		year = 2017;
		analyseStockTool.genIndustryHotStockCsv(year);
		
		year = 2017;
		analyseStockTool.genNotionHotStockCsv(year);
	
		List<String> list = StockUtils.getTradeDateLimit();
		StringBuilder sb = new StringBuilder("");
		boolean header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		String filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
		
		list = StockUtils.getTradeDateLimit();
		sb = new StringBuilder("");
		header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
		
		list = StockUtils.getTradeDateLimitAdd();
		sb = new StringBuilder("");
		header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");

		list = StockUtils.getTradeDateLimitAdd();
		sb = new StringBuilder("");
		header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
		
		list = StockUtils.getTradeDateLimit();
		sb = new StringBuilder("");
		header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotStocksPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
		
		list = StockUtils.getTradeDateLimit();
		sb = new StringBuilder("");
		header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotStocksPhrase.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
		
		list = StockUtils.getTradeDateLimitAdd();
		sb = new StringBuilder("");
		header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getIndustryHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		filePath = "/home/leslie/MyProject/StockAnalyse/gen/industryHotStocksPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
		
		list = StockUtils.getTradeDateLimitAdd();
		sb = new StringBuilder("");
		header = true;
		for(String date : list){
			String lowTradeDateStr = date.split("_")[0];
			String highTradeDateStr = date.split("_")[1];
			StringBuilder sb1 = analyseStockTool.getNotionHotStocksPhraseString(lowTradeDateStr, highTradeDateStr, header);
			if(sb1 != null){
				sb.append(sb1);
				if(header){
					header = false;
				}
			}
		}
		filePath = "/home/leslie/MyProject/StockAnalyse/gen/notionHotStocksPhraseAdd.csv";
		StockUtils.writeToFile(filePath, sb, "GB2312");
	}
	
	public StockJobService getStockJobService() {
		return stockJobService;
	}

	public void setStockJobService(StockJobService stockJobService) {
		this.stockJobService = stockJobService;
	}

	public StockDownloadToolTHS getStockDownloadToolTHS() {
		return stockDownloadToolTHS;
	}

	public void setStockDownloadToolTHS(StockDownloadToolTHS stockDownloadToolTHS) {
		this.stockDownloadToolTHS = stockDownloadToolTHS;
	}

	public StockParseToolTHS getStockParseToolTHS() {
		return stockParseToolTHS;
	}

	public void setStockParseToolTHS(StockParseToolTHS stockParseToolTHS) {
		this.stockParseToolTHS = stockParseToolTHS;
	}

	public AnalyseStockTool getAnalyseStockTool() {
		return analyseStockTool;
	}

	public void setAnalyseStockTool(AnalyseStockTool analyseStockTool) {
		this.analyseStockTool = analyseStockTool;
	}

}
