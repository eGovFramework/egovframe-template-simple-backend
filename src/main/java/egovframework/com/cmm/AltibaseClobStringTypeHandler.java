package egovframework.com.cmm;
/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egovframe.rte.psl.orm.ibatis.support.AbstractLobTypeHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * iBATIS TypeHandler implementation for Strings that get mapped to CLOBs.
 * Retrieves the LobHandler to use from SqlMapClientFactoryBean at config time.
 *
 * <p>Particularly useful for storing Strings with more than 4000 characters in an
 * Oracle database (only possible via CLOBs), in combination with OracleLobHandler.
 *
 * <p>Can also be defined in generic iBATIS mappings, as DefaultLobCreator will
 * work with most JDBC-compliant database drivers. In this case, the field type
 * does not have to be BLOB: For databases like MySQL and MS SQL Server, any
 * large enough binary type will work.
 *
 * @author Juergen Hoeller
 * @since 1.1.5
 * @see org.springframework.orm.ibatis.SqlMapClientFactoryBean#setLobHandler
 */
@Slf4j
@SuppressWarnings("deprecation")
public class AltibaseClobStringTypeHandler extends AbstractLobTypeHandler {

	/**
	 * Constructor used by iBATIS: fetches config-time LobHandler from
	 * SqlMapClientFactoryBean.
	 * @see org.springframework.orm.ibatis.SqlMapClientFactoryBean#getConfigTimeLobHandler
	 */
	public AltibaseClobStringTypeHandler() {
		super();
	}

	/**
	 * Constructor used for testing: takes an explicit LobHandler.
	 */
	protected AltibaseClobStringTypeHandler(LobHandler lobHandler) {
		super(lobHandler);
	}

	@Override
	protected void setParameterInternal(
			PreparedStatement ps, int index, Object value, String jdbcType, LobCreator lobCreator)
			throws SQLException {
		lobCreator.setClobAsString(ps, index, (String) value);
	}


	@Override
	protected Object getResultInternal(ResultSet rs, int index, LobHandler lobHandler)
			throws SQLException {

		StringBuffer read_data = new StringBuffer("");
	    int read_length;

		char [] buf = new char[1024];

		Reader rd =  lobHandler.getClobAsCharacterStream(rs, index);
	    try {
			while( (read_length=rd.read(buf))  != -1) {
				read_data.append(buf, 0, read_length);
			}
	    } catch (IOException ie) {
	    	log.debug("ie: {}", ie);//SQLException sqle = new SQLException(ie.getMessage());
	    	//throw sqle;
    	// 2011.10.10 보안점검 후속조치
	    } finally {
		    
			try {
			    rd.close();
			} catch (IOException ignore) {
				log.debug("IGNORE: {}", ignore.getMessage());
			}
		    
		}

	    return read_data.toString();

		//return lobHandler.getClobAsString(rs, index);
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}

}
