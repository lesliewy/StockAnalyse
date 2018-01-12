/**
 * Project Name:stanalyse  
 * File Name:PersistLHBInfo.java  
 * Package Name:com.wy.stock.job  
 * Date:Dec 19, 2017 9:08:14 PM  
 * Copyright (c) 2017, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.job;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wy.stock.domain.StockJob;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.StockDownloadToolTHS;
import com.wy.stock.tools.StockParseToolTHS;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 记录龙虎榜信息. 
 * date: Dec 19, 2017 9:08:14 PM <br/>  
 *  
 * @author leslie  
 */
public class PersistLhbJob {
	private static Logger LOGGER = Logger.getLogger(PersistLhbJob.class
			.getName());

	private StockJobService stockJobService;

	private StockDownloadToolTHS stockDownloadToolTHS;

	private StockParseToolTHS stockParseToolTHS;

	private String jobType = StockConstant.JOB_TYPE_THS_LHB;

	public void persistLHB(String jobDate) {
		LOGGER.info("JOB " + jobType + " - persistLHB job begin...");
		long begin = System.currentTimeMillis();

		Calendar cal = Calendar.getInstance();
		/*
		 * 周六、日不执行.注意法定节假日等不开市时也会执行,暂时没有添加.
		 */
		/*
		if (StockUtils.isWeekend(cal)) {
			LOGGER.info("Saturday or Sunday, return now...");
			return;
		}
		*/

		/*
		 * 清理超长R状态的job, 修改状态为D.
		 */
		stockJobService.cleanLongTimeJob(StockConstant.DEL_R_UPPER_LIMIT, jobType);
		// TODO 先注释掉
//		String jobDate = new SimpleDateFormat("YYMMddHHmm").format(Calendar.getInstance().getTime());
		
		
		/*
		 * 查询当前是否有正在运行的job
		 */
		List<StockJob> runningJobs = stockJobService.queryStockJobByDateStatus(
				jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_RUNNING);
		if (runningJobs != null && !runningJobs.isEmpty()) {
			LOGGER.info("JOB " + jobType + " - running job exists: " + runningJobs + ". return now...");
			return;
		}

		/*
		 * 每天只执行一次
		 */
		List<StockJob> existedJobs = stockJobService.queryStockJobByDateStatus(
				jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_SUCCESS);
		if (existedJobs != null && !existedJobs.isEmpty()) {
			LOGGER.info("JOB " + jobType + " - existes: " + existedJobs + ". return now...");
			return;
		}

		String remark = "running";
		String status = StockConstant.JOB_STATE_RUNNING;
		/*
		 * 登记当前的job
		 */
		StockJob job = new StockJob();
		job.setJobDate(jobDate);
		job.setJobType(jobType);
		job.setStatus(status);
		job.setRemark(remark);
		Timestamp beginTime = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		job.setBeginTime(beginTime);
		job.setTimestamp(beginTime);
		stockJobService.insertStockJob(job);

		/*
		 * 获取龙虎榜文件, 放入指定的目录下.
		 */
		// TODO 先注释掉
//		String dateStr = String.valueOf(cal.get(Calendar.YEAR))
//				+ StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0")
//				+ StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
		String dateStr = "20" + jobDate.substring(0, 6);
		
		File savedDir = new File(StockUtils.getDailyStockSaveDirDate(jobType, dateStr));
		stockDownloadToolTHS.downloadLHBHtmlFiles(savedDir, dateStr);
		stockDownloadToolTHS.downloadLHBHtmlFiles(savedDir, dateStr);
		stockDownloadToolTHS.downloadLHBHtmlFiles(savedDir, dateStr);

		/*
		 * 文件太小的不需要解析
		 */
		try {
			if(StockUtils.getTotalLines(savedDir + File.separator + StockConstant.LHB_FILE_NAME) < 2000){
				// 删除当日的JOB
				stockJobService.deleteByDateTypeNoStatus(jobDate.substring(0, 6), jobType);
				LOGGER.info("file length < 2000, return now...");
				return;
			}
		} catch (IOException e) {
		}
		
		/*
		 * 解析龙虎榜.
		 */
		stockParseToolTHS.persistLhbFromHtml(dateStr);
		
		// 更新JOB状态.
		remark = "success";
		status = StockConstant.JOB_STATE_SUCCESS;
		job.setStatus(status);
		job.setRemark(remark);
		job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		stockJobService.updateRunningJob(job);

		LOGGER.info("JOB " + jobType + " finished. elapsed time: "
				+ (System.currentTimeMillis() - begin) / (1000 * 60) + " min.");
	}

	public StockJobService getStockJobService() {
		return stockJobService;
	}

	public StockDownloadToolTHS getStockDownloadToolTHS() {
		return stockDownloadToolTHS;
	}

	public StockParseToolTHS getStockParseToolTHS() {
		return stockParseToolTHS;
	}

	public void setStockJobService(StockJobService stockJobService) {
		this.stockJobService = stockJobService;
	}

	public void setStockDownloadToolTHS(StockDownloadToolTHS stockDownloadToolTHS) {
		this.stockDownloadToolTHS = stockDownloadToolTHS;
	}

	public void setStockParseToolTHS(StockParseToolTHS stockParseToolTHS) {
		this.stockParseToolTHS = stockParseToolTHS;
	}
}
