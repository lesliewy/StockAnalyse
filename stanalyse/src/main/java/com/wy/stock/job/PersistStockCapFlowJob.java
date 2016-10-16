/**
 * 
 */
package com.wy.stock.job;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wy.stock.domain.StockCapFlow;
import com.wy.stock.domain.StockInfo;
import com.wy.stock.domain.StockJob;
import com.wy.stock.service.ConfigService;
import com.wy.stock.service.StockCapFlowService;
import com.wy.stock.service.StockInfoService;
import com.wy.stock.service.StockJobService;
import com.wy.stock.tools.StockDownloadTool;
import com.wy.stock.tools.StockParseTool;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 将所有个股的资金流向情况记入数据库ST_STOCK_CAP_FLOW,  资金url: http://data.eastmoney.com/zjlx/detail.html
 * @author leslie
 *
 */
public class PersistStockCapFlowJob {

	private static Logger LOGGER = Logger.getLogger(PersistStockCapFlowJob.class.getName());
	
	private StockJobService stockJobService;
	
	private ConfigService configService;
	
	private StockDownloadTool stockDownloadTool;
	
	private StockParseTool stockParseTool;
	
	private StockCapFlowService stockCapFlowService;
	
	private StockInfoService stockInfoService;
	
	private String jobType = StockConstant.JOB_TYPE_CAP_FLOW;
	
	public void persistStockCapFlow(){
		LOGGER.info("JOB " + jobType + " - persistStockCapFlow job begin...");
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
			LOGGER.info("JOB " + jobType + " existed: " + jobs);
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
		
		// 查询记录资金流向的模式  A: 记录当天的所有个股的资金流向, 只有当天一天.   B: 记录每个个股的历史资金情况.
		Map<String, String> configs = configService.queryAllConfigInMap();
		String persistCapFlowMode = configs.get(StockConstant.PERSIST_CAP_FLOW_MODE);
		
		File savedDir =  new File(StockUtils.getDailyStockSaveDir("C"));
		/*
		 * A: 只一天的资金流向: http://data.eastmoney.com/zjlx/detail.html
		 * B: 所有个股的历史: http://data.eastmoney.com/zjlx/600736.html 
		 */
		if("A".equalsIgnoreCase(persistCapFlowMode)){
			/*
			 * 下载文件.
			 */
			File capFlowFile = stockDownloadTool.downloadStockCapFlow(savedDir);
			
			/*
			 * 插入数据库, 先删除当日的所有数据, tradeDate 不是执行程序的时间，而是文件内的.
			 */
			List<StockCapFlow> capFlowList = stockParseTool.parseStockCapFlow(capFlowFile);
			if(capFlowList == null || capFlowList.isEmpty()){
				LOGGER.info("capFlowList is null, return now...");
				// 更新job
				remark = "capFlowList is null";
				status = StockConstant.JOB_STATE_DELETE;
				job.setStatus(status);
				job.setRemark(remark);
				job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				stockJobService.updateRunningJob(job);
				return;
			}
			Timestamp tradeDate = capFlowList.get(0).getTradeDate();
			stockCapFlowService.deleteCapFlowByDate(tradeDate);
			stockCapFlowService.insertStockCapFlowBatch(capFlowList);
		}else if("B".equalsIgnoreCase(persistCapFlowMode)){
			// 查询所有的沪深A股, 遍历每个股票.
			List<StockInfo> allA = stockInfoService.queryStockInfoByType("A");
			for(StockInfo stock : allA){
				String code = stock.getCode();
				// 下载html文件.
				File html = stockDownloadTool.downloadStockCapFlowHist(savedDir, code);
				// 解析文件.
				List<StockCapFlow> capFlowList = stockParseTool.parseStockCapFlowHist(html);
				if(capFlowList == null || capFlowList.isEmpty()){
					continue;
				}
				// 这里不直接将 capFlowList 插入，先做个筛查，去掉数据库中已经存在的，防止报错太多.
				List<Timestamp> tradeDateExistedList = stockCapFlowService.queryCapFlowDateListByCode(code);
				for(StockCapFlow capFlow : capFlowList){
					if(tradeDateExistedList != null && tradeDateExistedList.contains(capFlow.getTradeDate())){
						continue;
					}
					stockCapFlowService.insertStockCapFlow(capFlow);
				}
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

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
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

	public StockCapFlowService getStockCapFlowService() {
		return stockCapFlowService;
	}

	public void setStockCapFlowService(StockCapFlowService stockCapFlowService) {
		this.stockCapFlowService = stockCapFlowService;
	}

	public StockInfoService getStockInfoService() {
		return stockInfoService;
	}

	public void setStockInfoService(StockInfoService stockInfoService) {
		this.stockInfoService = stockInfoService;
	}
	
}
