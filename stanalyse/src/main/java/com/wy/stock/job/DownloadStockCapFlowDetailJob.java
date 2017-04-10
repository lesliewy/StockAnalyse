/**
 * 
 */
package com.wy.stock.job;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.wy.stock.domain.ExchangeInfo;
import com.wy.stock.domain.StockJob;
import com.wy.stock.service.ExchangeInfoService;
import com.wy.stock.service.StockJobService;
import com.wy.stock.service.thread.DownloadStockCapFlowDetailThread;
import com.wy.stock.tools.StockDownloadTool;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 将所有个股的资金流向情况记入数据库ST_STOCK_CAP_FLOW,  资金url: http://data.eastmoney.com/zjlx/detail.html
 * @author leslie
 *
 */
public class DownloadStockCapFlowDetailJob {

	private static Logger LOGGER = Logger.getLogger(DownloadStockCapFlowDetailJob.class.getName());
	
	private StockJobService stockJobService;
	
	private StockDownloadTool stockDownloadTool;
	
	private ExchangeInfoService exchangeInfoService;
	
	private String jobType = StockConstant.JOB_TYPE_CAP_FLOW_DETAIL;
	
	public void downloadStockCapFlowDetail(){
		LOGGER.info("JOB " + jobType + " - downloadStockCapFlowDetail job begin...");
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
			LOGGER.info("JOB " + jobType + " - running job exists. " + runningJobs + ". return now...");
			return;
		}
		
		// 查询是否已经执行过, 每天只执行一次, 执行过不再执行.
		List<StockJob> jobs = stockJobService.queryStockJobByDateStatus(jobDate.substring(0, 6), jobType, StockConstant.JOB_STATE_SUCCESS);
		if(jobs != null && jobs.size() > 0){
			LOGGER.info("JOB " + jobType + " existed: " + jobs + ". return now...");
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
		
		// 最大10个Thread, 多余的放入queue, 直至queue满才会new Thread.
		ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 30, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		
		// 将个股资金的明细数据和所有个股的当天资金json文件放在同一个目录下。
		File savedDir =  new File(StockUtils.getDailyStockSaveDir("C"));
		// 查询所有的沪深A股, 遍历每个股票.
		List<ExchangeInfo> allA = exchangeInfoService.queryExchangeInfoByType("A");
		for(ExchangeInfo stock : allA){
			String code = stock.getCode();
			/*
			 * 非线程方式.
			 */
			// 下载文件.
//				stockDownloadTool.downloadStockCapFlowDetail(savedDir, code);
//				Thread.sleep(2 * 1000);
//				stockDownloadTool.downloadStockCapFlowDetail(savedDir, code);
//				Thread.sleep(2 * 1000);
//				stockDownloadTool.downloadStockCapFlowDetail(savedDir, code);
			
			/*
			 * 线程方式, 只执行一次，可能存在某些文件因为连接超时无法下载的情况.
			 */
			DownloadStockCapFlowDetailThread thread = new DownloadStockCapFlowDetailThread();
			thread.setDir(savedDir);
			thread.setCode(code);
			thread.setStockDownloadTool(stockDownloadTool);
			pool.execute(thread);
		}
		for(;;){
			if(pool.getActiveCount() > 0){
				LOGGER.info("getActiveCount: " + pool.getActiveCount() + "; getCompletedTaskCount: " + pool.getCompletedTaskCount() + 
						"; getLargestPoolSize: " + pool.getLargestPoolSize() + "; getMaximumPoolSize: " + pool.getMaximumPoolSize() +
						"; getTaskCount: " + pool.getTaskCount());
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					LOGGER.error(e);
				}
			}else {
				break;
			}
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

	public ExchangeInfoService getExchangeInfoService() {
		return exchangeInfoService;
	}

	public void setExchangeInfoService(ExchangeInfoService exchangeInfoService) {
		this.exchangeInfoService = exchangeInfoService;
	}

}
