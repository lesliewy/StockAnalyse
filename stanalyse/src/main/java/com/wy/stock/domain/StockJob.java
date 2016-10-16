/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * @author leslie
 *
 */
public class StockJob {

	private String jobDate;
	
	private String jobType;
	
	private String status;
	
	private String remark;
	
	private Timestamp beginTime;
	
	private Timestamp timestamp;
	
	// 用于查询 YYMMDD
	private String date;
	// 超过该数值，就清理掉状态为R的JOB (单位:s)
	private int delRUpperLimit;
	// job 类型标志.
	private String jobFlag;

	public String getJobDate() {
		return jobDate;
	}

	public void setJobDate(String jobDate) {
		this.jobDate = jobDate;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDelRUpperLimit() {
		return delRUpperLimit;
	}

	public void setDelRUpperLimit(int delRUpperLimit) {
		this.delRUpperLimit = delRUpperLimit;
	}

	public String getJobFlag() {
		return jobFlag;
	}

	public void setJobFlag(String jobFlag) {
		this.jobFlag = jobFlag;
	}
	
	@Override
	public String toString(){
		return "[ jobDate = " + jobDate + ", jobType = " + jobType + ", status = " + status + ", remark = " + remark +
				", beginTime = " + beginTime + ", timestamp = " + timestamp
				+ " ]";
	}
	
}
