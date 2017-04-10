/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_INFO
 * @author leslie
 *
 */
public class ExchangeInfo {
	
	private String name;
	
	private String code;
	
	private String exchange;
	
	private String type;
	
	private Timestamp timestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass().isInstance(this)){
			ExchangeInfo o = (ExchangeInfo)obj;
			if(o.getCode().equals(this.getCode())
					&& o.getExchange().equals(this.getExchange())
					&& o.getType().equals(this.getType())){
				return true;
			}
		}
		return false;
	}
	
	
}
