/*
 * Copyright 2008-2009 MOPAS(Ministry of Public Administration and Security).
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
package egovframework.com.cmm.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.cmm.filter
 * @filename : HTMLTagFilterRequestWrapper.java
 * @modificationInformation
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일            수정자             수정내용
 *  ----------   ----------   ----------------------
 *  2023. 8. 9.    정완배             주석추가
 * </pre>
 *
 *
 */

public class HTMLTagFilterRequestWrapper extends HttpServletRequestWrapper {

	public HTMLTagFilterRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String[] getParameterValues(String parameter) {

		String[] values = super.getParameterValues(parameter);
		
		if(values==null){
			return null;			
		}
		
		for (int i = 0; i < values.length; i++) {			
			if (values[i] != null) {				
				StringBuffer strBuff = new StringBuffer();
				for (int j = 0; j < values[i].length(); j++) {
					char c = values[i].charAt(j);
					switch (c) {
					case '<':
						strBuff.append("&lt;");
						break;
					case '>':
						strBuff.append("&gt;");
						break;
					//case '&':
						//strBuff.append("&amp;");
						//break;
					case '"':
						strBuff.append("&quot;");
						break;
					case '\'':
						strBuff.append("&apos;");
						break;
					default:
						strBuff.append(c);
						break;
					}
				}				
				values[i] = strBuff.toString();
			} else {
				values[i] = null;
			}
		}

		return values;
	}

	public String getParameter(String parameter) {
		
		String value = super.getParameter(parameter);
		
		if(value==null){
			return null;
		}
		
		StringBuffer strBuff = new StringBuffer();

		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '<':
				strBuff.append("&lt;");
				break;
			case '>':
				strBuff.append("&gt;");
				break;
			case '&':
				strBuff.append("&amp;");
				break;
			case '"':
				strBuff.append("&quot;");
				break;
			case '\'':
				strBuff.append("&apos;");
				break;	
			default:
				strBuff.append(c);
				break;
			}
		}
		
		value = strBuff.toString();
		
		return value;
	}

}