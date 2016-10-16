/**
 * 
 */
package com.wy.ibatis.handler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Types;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 *  ibatis 默认对BigInteger不支持， 需要添加
 * @author leslie
 *
 */
public class BigIntegerTypeHandler implements TypeHandlerCallback {

	public Object getResult(ResultGetter getter) throws SQLException {
		if(getter.wasNull()) { 
            return null; 
        } 

        Object o = getter.getObject(); 
        if(o instanceof BigDecimal) { 
            BigDecimal bd = (BigDecimal)o; 
            return bd.toBigInteger(); 
        } else if(o instanceof BigInteger) { 
            return (BigInteger)o; 
        } else { 
            return o; 
        }
	}

    public void setParameter(ParameterSetter setter, Object parameter) 
            throws SQLException { 
        if (parameter == null) { 
            setter.setNull(Types.BIGINT); 
        } else { 
            BigInteger i = (BigInteger) parameter; 
            setter.setBigDecimal(new BigDecimal(i)); 
        } 
    } 

    public Object valueOf(String s) { 
        return s; 
    } 

}
