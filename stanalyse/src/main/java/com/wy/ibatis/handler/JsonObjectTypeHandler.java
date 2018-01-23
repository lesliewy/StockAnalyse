/**
 * Project Name:stanalyse  
 * File Name:JsonObjectTypeHandler.java  
 * Package Name:com.wy.ibatis.handler  
 * Date:Dec 22, 2017 9:16:15 PM  
 * Copyright (c) 2017, wy All Rights Reserved.  
 *  
 */
package com.wy.ibatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.ibatis.sqlmap.engine.type.BaseTypeHandler;

/**
 * 将jsonObject 转为string  
 * date: Dec 22, 2017 9:16:15 PM <br/>  
 *  
 * @author leslie  
 * @version   
 * @since version 1.0  
 */
public class JsonObjectTypeHandler extends BaseTypeHandler {

	public Object getResult(ResultSet resultset, String s) throws SQLException {
		return new JSONObject(resultset.getString(s));
	}

	public Object getResult(ResultSet resultset, int i) throws SQLException {
		return JSONObject.stringToValue(resultset.getString(i));
	}

	public Object getResult(CallableStatement callablestatement, int i)
			throws SQLException {
		return JSONObject.stringToValue(callablestatement.getString(i));
	}

	public void setParameter(PreparedStatement preparedstatement, int i,
			Object obj, String s) throws SQLException {

		preparedstatement.setString(i, JSONObject.valueToString(obj));

	}

	public Object valueOf(String s) {

		return JSONObject.stringToValue(s);
	}

}
