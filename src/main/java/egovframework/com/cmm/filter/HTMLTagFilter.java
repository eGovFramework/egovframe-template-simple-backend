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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.cmm.filter
 * @filename : HTMLTagFilter.java
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

public class HTMLTagFilter implements Filter{

	@SuppressWarnings("unused")
	private FilterConfig config;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		chain.doFilter(new HTMLTagFilterRequestWrapper((HttpServletRequest)request), response);
	}

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void destroy() {

	}
}
