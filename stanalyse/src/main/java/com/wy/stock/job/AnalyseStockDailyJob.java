/**
 * 
 */
package com.wy.stock.job;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.wy.stock.domain.StockJob;
import com.wy.stock.service.CandlestickService;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.StockParseTool;
import com.wy.stock.utils.MailUtils;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * @author leslie
 *
 */
public class AnalyseStockDailyJob {
	
	private static Logger LOGGER = Logger.getLogger(AnalyseStockDailyJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockParseTool stockParseTool;
	
	private CandlestickService candlestickService;
	
	private String jobType = "Q";
	
	public void analyseStock(){
		LOGGER.info("JOB " + jobType + " - analyseStock begin...");
		long begin = System.currentTimeMillis();
		
		// 清理超长R状态的job, 修改状态为D.
		stockJobService.cleanLongTimeJob(StockConstant.DEL_R_UPPER_LIMIT, jobType);
		String jobDate = new SimpleDateFormat("YYMMDDHHmm").format(Calendar.getInstance().getTime());
		
		// 查询当前是否有正在运行的job
		List<StockJob> runningJobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_RUNNING);
		if(runningJobs != null && !runningJobs.isEmpty()){
			LOGGER.info("JOB + " + jobType + " - running job exists. " + runningJobs + ". return now...");
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
		
		/*
		 * 解析数据, 并存入ST_HISTORY;
		 */
		File dir = new File(StockUtils.getDailyStockSaveDir("H"));
		stockParseTool.parseAllHistCsv(dir);
		
		/*
		 * 分析数据, 并发邮件.
		 */
		StringBuilder sb = new StringBuilder("");
		Calendar threeDaysAgo = Calendar.getInstance();
		threeDaysAgo.add(Calendar.DAY_OF_MONTH, -3);
		Calendar sixDaysAgo = Calendar.getInstance();
		sixDaysAgo.add(Calendar.DAY_OF_MONTH, -6);
		Calendar tenDaysAgo = Calendar.getInstance();
		tenDaysAgo.add(Calendar.DAY_OF_MONTH, -10);
		// 启明星
		sb.append(candlestickService.morningStarAnalyse(new Timestamp(tenDaysAgo.getTimeInMillis())));
		// 黄昏星
		sb.append(candlestickService.duskStarAnalyse(new Timestamp(tenDaysAgo.getTimeInMillis())));
		
		status = StockConstant.JOB_STATE_SUCCESS;
		remark = "success";
		// 发邮件, 发送到163邮箱的, 手机上可以实时提示;  gmail 会延迟或者不会提醒，需手动接收;
		long beginMail = System.currentTimeMillis();
		try {
			Map<String, String> mailMap = new HashMap<String, String>();
			mailMap.put("jobType", jobType);
			mailMap.put("jobDate", jobDate);
			MailUtils.sendMailTo163(sb.toString(), mailMap);
		} catch (MessagingException e) {
			status = StockConstant.JOB_STATE_FAILED;
			remark = "send mail failed";
			LOGGER.error("send mail failed, " + e);
		}
		LOGGER.info("JOB " + jobType + " - send mail success, total time: " + (System.currentTimeMillis() - beginMail)/1000 + " s.");
		
		// 更新JOB状态.
		job.setJobDate(jobDate);
		job.setJobType(jobType);
		job.setJobFlag(jobType);
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

	public CandlestickService getCandlestickService() {
		return candlestickService;
	}

	public void setCandlestickService(CandlestickService candlestickService) {
		this.candlestickService = candlestickService;
	}
	
}
