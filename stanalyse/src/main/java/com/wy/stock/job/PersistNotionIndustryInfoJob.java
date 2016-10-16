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
import com.wy.stock.tools.StockParseToolTHS;
import com.wy.stock.utils.StockConstant;

/**
 * 
 * 定时更新 ST_NOTION_INFO, ST_INDUSTRY_INFO中的url, 这里的概念信息可能修改、新增.
 * 否则PersistBoardHotTHSJob找不到新增的概念url, 无法登记到ST_NOTION_HOT_STOCKS, ST_INDUSTRY_HOT_STOCKS.
 * @author leslie
 *
 */
public class PersistNotionIndustryInfoJob {

	private static Logger LOGGER = Logger.getLogger(PersistNotionIndustryInfoJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockParseToolTHS stockParseToolTHS;
	
	private String jobType = StockConstant.JOB_TYPE_NOTION_INDUSTRY_INFO;
	
	public void persistNotionIndustryInfo(){
		LOGGER.info("JOB " + jobType + " - persistNotionIndustryInfo job begin...");
		long begin = System.currentTimeMillis();
		
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
		
		// 先查找当天的文件，没有的话查找上一天的，一直查找到前10天.
		int count = 0;
		while(count < 10){
			File html = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/" + buildDatePath(count) + "notionHot.html");
			if(!html.exists()){
				count++;
				continue;
			}
			stockParseToolTHS.persistNotionIndustryInfo(html, "NOTION");
			break;
		}
		
		count = 0;
		while(count < 10){
			File html = new File("/home/leslie/MyProject/StockAnalyse/html/boardHot/" + buildDatePath(count) + "industryHot.html");
			if(!html.exists()){
				count++;
				continue;
			}
			stockParseToolTHS.persistNotionIndustryInfo(html, "INDUSTRY");
			break;
		}
		
		// 更新JOB状态.
		remark = "success";
		status = StockConstant.JOB_STATE_SUCCESS;
		job.setStatus(status);
		job.setRemark(remark);
		job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		stockJobService.updateRunningJob(job);
		
		LOGGER.info("JOB " + jobType + " finished. eclipsed time: " + (System.currentTimeMillis() - begin)/(1000 * 60) + " min.");
	}
	
	private String buildDatePath(int count){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -count);
		
		return cal.get(Calendar.YEAR) + "/" 
		+ StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0") +"/"
		+ StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0") + "/";
	}

	public StockJobService getStockJobService() {
		return stockJobService;
	}

	public void setStockJobService(StockJobService stockJobService) {
		this.stockJobService = stockJobService;
	}

	public StockParseToolTHS getStockParseToolTHS() {
		return stockParseToolTHS;
	}

	public void setStockParseToolTHS(StockParseToolTHS stockParseToolTHS) {
		this.stockParseToolTHS = stockParseToolTHS;
	}

}
