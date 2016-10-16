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
import com.wy.stock.service.IndustryHotService;
import com.wy.stock.service.NotionHotService;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.StockDownloadTool;
import com.wy.stock.tools.StockParseTool;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 东方财富网: http://www.eastmoney.com/
 * @author leslie
 *
 */
public class PersistBoardHotJob {

	private static Logger LOGGER = Logger.getLogger(PersistBoardHotJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockDownloadTool stockDownloadTool;
	
	private StockParseTool stockParseTool;
	
	private NotionHotService notionHotService;
	
	private IndustryHotService industryHotService;
	
	// TODO: T 已经被PersistBoardHotTHSJob使用.
	private String jobType = "T";   
	
	public void persistBoardHot(){
		LOGGER.info("JOB " + jobType + " - persistBoardHot job begin...");
		long begin = System.currentTimeMillis();
		
		Calendar cal = Calendar.getInstance();
		// 周六、日不执行.注意法定节假日等不开市时也会执行,暂时没有添加. 
		if(StockUtils.isWeekend(cal)){
			LOGGER.info("Saturday or Sunday, return now...");
			return;
		}
		
		// 清理超长R状态的job, 修改状态为D.
		stockJobService.cleanLongTimeJob(StockConstant.DEL_R_UPPER_LIMIT, jobType);
		String jobDate = new SimpleDateFormat("YYMMddHHmm").format(Calendar.getInstance().getTime());
		
		// 查询当前是否有正在运行的job
		List<StockJob> runningJobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_RUNNING);
		if(runningJobs != null && !runningJobs.isEmpty()){
			LOGGER.info("JOB " + jobType + " - running job exists: " + runningJobs + ". return now...");
			return;
		}
		
		// 每天只执行一次
		List<StockJob> existedJobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_SUCCESS);
		if(existedJobs != null && !existedJobs.isEmpty()){
			LOGGER.info("JOB " + jobType + " - existes: " + existedJobs + ". return now...");
			return;
		}
		
		String remark = "running";
		String status = StockConstant.JOB_STATE_RUNNING;
		// 登记当前的job
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
		 *  获取概念、行业板块热点的json文件， 放入指定的目录下.
		 */
		File jsonFileDir = new File(StockUtils.getDailyStockSaveDir("B"));
		stockDownloadTool.downloadBoardHotFiles(jsonFileDir, "NOTION");
		stockDownloadTool.downloadBoardHotFiles(jsonFileDir, "NOTION");
		stockDownloadTool.downloadBoardHotFiles(jsonFileDir, "NOTION");
		stockDownloadTool.downloadBoardHotFiles(jsonFileDir, "INDUSTRY");
		stockDownloadTool.downloadBoardHotFiles(jsonFileDir, "INDUSTRY");
		stockDownloadTool.downloadBoardHotFiles(jsonFileDir, "INDUSTRY");
		
		/*
		 * 解析并登记概念板块热点json文件
		 */
		notionHotService.insertNotionHotBatch(stockParseTool.parseNotionHotFromJson(new File(jsonFileDir.getAbsolutePath() + File.separatorChar + "notionHot.json")));

		/*
		 * 解析并登记行业板块热点json文件
		 */
		industryHotService.insertIndustryHotBatch(stockParseTool.parseIndustryHotFromJson(new File(jsonFileDir.getAbsolutePath() + File.separatorChar + "industryHot.json")));

		/*
		 * 获取概念、行业热点排名靠前的板块内的股票的json文件.
		 */
		String dateStr = String.valueOf(cal.get(Calendar.YEAR)).substring(2, 4) +
		StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0") +
		StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
		stockDownloadTool.downloadBoardHotStocksFiles(dateStr, "NOTION");
		stockDownloadTool.downloadBoardHotStocksFiles(dateStr, "NOTION");
		stockDownloadTool.downloadBoardHotStocksFiles(dateStr, "NOTION");
		stockDownloadTool.downloadBoardHotStocksFiles(dateStr, "INDUSTRY");
		stockDownloadTool.downloadBoardHotStocksFiles(dateStr, "INDUSTRY");
		stockDownloadTool.downloadBoardHotStocksFiles(dateStr, "INDUSTRY");
		
		/*
		 * 解析概念板块热点排名考前的板块内的股票
		 */
		stockParseTool.persistNotionHotStocksFromJson(dateStr);
		stockParseTool.persistIndustryHotStocksFromJson(dateStr);
		
		/*
		 * 记录重要指数. ST_INDEX.
		 */
		stockDownloadTool.downloadIndexFiles();
		stockDownloadTool.downloadIndexFiles();
		stockDownloadTool.downloadIndexFiles();
		stockParseTool.persistIndexFromJson(new File(StockUtils.getDailyStockSaveDir("B") + "index.json"));
		
		// 更新JOB状态.
		remark = "success";
		status = StockConstant.JOB_STATE_SUCCESS;
		job.setStatus(status);
		job.setRemark(remark);
		job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		stockJobService.updateRunningJob(job);
		
		LOGGER.info("JOB " + jobType + " finished. eclipsed time: " + (System.currentTimeMillis() - begin)/(1000 * 60) + " min.");
	}

	public StockJobService getStockJobService() {
		return stockJobService;
	}

	public void setStockJobService(StockJobService stockJobService) {
		this.stockJobService = stockJobService;
	}

	public StockParseTool getStockParseTool() {
		return stockParseTool;
	}

	public void setStockParseTool(StockParseTool stockParseTool) {
		this.stockParseTool = stockParseTool;
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

	public StockDownloadTool getStockDownloadTool() {
		return stockDownloadTool;
	}

	public void setStockDownloadTool(StockDownloadTool stockDownloadTool) {
		this.stockDownloadTool = stockDownloadTool;
	}
	
}
