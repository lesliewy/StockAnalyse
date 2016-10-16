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
import com.wy.stock.service.AnalyseFiveChangeService;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.StockDownloadTool;
import com.wy.stock.tools.StockParseTool;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class AnalyseFiveChangeJob {

	private static Logger LOGGER = Logger.getLogger(AnalyseFiveChangeJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockDownloadTool stockDownloadTool;
	
	private StockParseTool stockParseTool;
	
	private AnalyseFiveChangeService analyseFiveChangeService;
	
	private String jobType = StockConstant.JOB_TYPE_FIVE_CHANGE;
	
	public void analyseFiveChange(){
		LOGGER.info("JOB " + jobType + " - analyseFiveChange job begin...");
		long begin = System.currentTimeMillis();
		
		Calendar cal = Calendar.getInstance();
		// 周六、日不执行.注意法定节假日等不开市时也会执行,暂时没有添加. 
		if(StockUtils.isWeekend(cal)){
			LOGGER.info("Saturday or Sunday, return now...");
			return;
		}
		
		//  9:32之前不执行,15:02之后不执行.  11:30 - 13:00 不执行.
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		if(hour < 9 || hour > 15 || (hour == 9 && min <=32) || (hour == 15 && min >= 2)){
			LOGGER.info("before 9:32 or after 15:02, return now...");
			return;
		}
		if((hour == 11 && min > 30) || hour == 12 ){
			LOGGER.info("11:30 - 13:00, return now...");
			return;
		}
		
		// 清理超长R状态的job, 修改状态为D.
		stockJobService.cleanLongTimeJob(StockConstant.DEL_R_UPPER_LIMIT, jobType);
		
		String jobDate = new SimpleDateFormat("YYMMddHHmm").format(Calendar.getInstance().getTime());
		
		// 删除当天之前状态是S的
		stockJobService.deleteByDateType(jobDate.substring(0, 6), jobType);
		
		// 查询当前是否有正在运行的job
		List<StockJob> runningJobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_RUNNING);
		if(runningJobs != null && !runningJobs.isEmpty()){
			LOGGER.info("JOB " + jobType + " - running job exists: " + runningJobs + ". return now...");
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
		 *  下载当前的个股json,5分钟降序排列. http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=G&sortRule=-1&pageSize=50&page=1&jsName=quote_123&style=10
	     *  type: 10 上证；  20 深证
		 */
		File jsonFileDir = new File(StockUtils.getDailyStockSaveDir("B"));
		stockDownloadTool.downloadStockFiveChange("10");
		stockDownloadTool.downloadStockFiveChange("20");
		
		/*
		 * 解析个股页面，插入ST_STOCK_FIVE_CHANGE, 这里不区分上证、深证
		 */
		stockParseTool.persistStockFiveChange(jsonFileDir);
		
		/*
		 * 分析数据，记入log.
		 */
		analyseFiveChangeService.analyseFiveChange();
		
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

	public StockDownloadTool getStockDownloadTool() {
		return stockDownloadTool;
	}

	public void setStockDownloadTool(StockDownloadTool stockDownloadTool) {
		this.stockDownloadTool = stockDownloadTool;
	}

	public StockParseTool getStockParseTool() {
		return stockParseTool;
	}

	public void setStockParseTool(StockParseTool stockParseTool) {
		this.stockParseTool = stockParseTool;
	}

	public AnalyseFiveChangeService getAnalyseFiveChangeService() {
		return analyseFiveChangeService;
	}

	public void setAnalyseFiveChangeService(
			AnalyseFiveChangeService analyseFiveChangeService) {
		this.analyseFiveChangeService = analyseFiveChangeService;
	}
	
}
