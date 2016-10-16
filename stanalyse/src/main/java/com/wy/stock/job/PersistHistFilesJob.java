/**
 * 
 */
package com.wy.stock.job;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.wy.stock.domain.StockJob;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.StockDownloadTool;
import com.wy.stock.tools.StockParseTool;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class PersistHistFilesJob {

	private static Logger LOGGER = Logger.getLogger(PersistHistFilesJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockDownloadTool stockDownloadTool;
	
	private StockParseTool stockParseTool;
	
	private String jobType = StockConstant.JOB_TYPE_HISTORY;
	
	public void persistHistFiles(){
		LOGGER.info("JOB " + jobType + " - persistHistFiles job begin...");
		long begin = System.currentTimeMillis();
		
		// 清理超长R状态的job, 修改状态为D.
		stockJobService.cleanLongTimeJob(StockConstant.DEL_R_UPPER_LIMIT, jobType);
		String jobDate = new SimpleDateFormat("YYMMddHHmm").format(Calendar.getInstance().getTime());
		
		// 查询当前是否有正在运行的job
		List<StockJob> runningJobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_RUNNING);
		if(runningJobs != null && !runningJobs.isEmpty()){
			LOGGER.info("JOB " + jobType + " - running job exists. " + runningJobs + ". return now...");
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
		 *  获取所有股票的CSV文件， 放入指定的目录下.
		 */
		File dir = new File(StockUtils.getDailyStockSaveDir("H"));
		stockDownloadTool.downloadCsvFiles(dir);
		// 再次执行
		stockDownloadTool.downloadCsvFiles(dir);
		stockDownloadTool.downloadCsvFiles(dir);
		
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

	public StockDownloadTool getStockDownloadTool() {
		return stockDownloadTool;
	}

	public void setStockDownloadTool(StockDownloadTool stockDownloadTool) {
		this.stockDownloadTool = stockDownloadTool;
	}
	
}
