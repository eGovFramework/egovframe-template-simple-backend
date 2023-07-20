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

public class HTMLTagFilter implements Filter{

	@SuppressWarnings("unused")
	private FilterConfig config;

	// 필터 동작을 처리하는 메서드
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// 요청을 HTMLTagFilterRequestWrapper로 감싸서 전달합니다.
        	// 이렇게 함으로써, 요청 파라미터에서 HTML 태그를 제거하는 작업을 수행합니다.
		chain.doFilter(new HTMLTagFilterRequestWrapper((HttpServletRequest)request), response);
	}

	// 필터 초기화 메서드 (실행 시 필요한 초기화 작업을 수행합니다.)
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	// 필터 종료 메서드 (필터 사용 후 작업을 수행합니다.)
	public void destroy() {
		// 필요한 경우 종료 시 정리 작업을 수행할 수 있습니다.
	}
}
