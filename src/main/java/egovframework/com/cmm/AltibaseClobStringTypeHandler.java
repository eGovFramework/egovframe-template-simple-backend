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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.egovframe.rte.psl.orm.ibatis.support.AbstractLobTypeHandler;

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
@SuppressWarnings("deprecation")
public class AltibaseClobStringTypeHandler extends AbstractLobTypeHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AltibaseClobStringTypeHandler.class);

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

	protected void setParameterInternal(
			PreparedStatement ps, int index, Object value, String jdbcType, LobCreator lobCreator)
			throws SQLException {
		lobCreator.setClobAsString(ps, index, (String) value);
	}


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
	    	LOGGER.debug("ie: {}", ie);//SQLException sqle = new SQLException(ie.getMessage());
	    	//throw sqle;
    	// 2011.10.10 보안점검 후속조치
	    } finally {
		    if (rd != null) {
			try {
			    rd.close();
			} catch (Exception ignore) {
				LOGGER.debug("IGNORE: {}", ignore.getMessage());
			}
		    }
		}

	    return read_data.toString();

		//return lobHandler.getClobAsString(rs, index);
	}

	public Object valueOf(String s) {
		return s;
	}

}
