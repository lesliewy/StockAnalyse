package com.wy.stock.job;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.wy.stock.domain.StockJob;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.StockParseTool;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 
 * @author leslie
 *
 */
public class FetchStockInfoJob {

	private static Logger LOGGER = Logger.getLogger(FetchStockInfoJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockParseTool stockParseTool;
	
	private String jobType = StockConstant.JOB_TYPE_FETCH_STOCK_INFO;
	
	public void fetchStockInfo(){
		LOGGER.info("JOB " + jobType + " - fetchStockInfo job begin...");
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
		
		// 至少5天执行一次
		StockJob lastJob = stockJobService.queryLastStockJob(jobType, StockConstant.JOB_STATE_SUCCESS);
		if(lastJob != null){
			String lastJobDate = lastJob.getJobDate();
			int year = Integer.valueOf("20" + lastJobDate.substring(0, 2));
			int month = Integer.valueOf(lastJobDate.substring(2, 4));
			int dayOfMonth = Integer.valueOf(lastJobDate.substring(4, 6));
			Calendar lastJobDateCal = Calendar.getInstance();
			lastJobDateCal.set(year, month - 1, dayOfMonth);
			lastJobDateCal.add(Calendar.DAY_OF_MONTH, 5);
			if(cal.compareTo(lastJobDateCal) < 0){
				LOGGER.info("JOB " + jobType + " - last job date: " + lastJobDate + ". interval less than 5 days, return now...");
				return;
			}
		}
		
		// 其他JOB执行成功后执行. 目前就是等job-C job-T 执行完成后.
		List<StockJob> jobC = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), StockConstant.JOB_TYPE_CAP_FLOW, StockConstant.JOB_STATE_SUCCESS);
		List<StockJob> jobT = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), StockConstant.JOB_TYPE_THS_BOARD_HOT, StockConstant.JOB_STATE_SUCCESS);
		if(jobC == null || jobC.isEmpty() || jobT == null || jobT.isEmpty()){
			LOGGER.info("JOB " + jobType + " - job C or job T not finished, wait next time, return now...");
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
		
		stockParseTool.getStockInfo();
		
		// 更新JOB状态.
		remark = "success";
		status = StockConstant.JOB_STATE_SUCCESS;
		job.setStatus(status);
		job.setRemark(remark);
		job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		stockJobService.updateRunningJob(job);
		
		LOGGER.info("JOB " + jobType + " finished. elapsed time: " + (System.currentTimeMillis() - begin)/(1000 * 60) + " min.");
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

}
