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

public class HTMLTagFilterRequestWrapper extends HttpServletRequestWrapper {

    public HTMLTagFilterRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    // 매개변수로 전달된 파라미터의 값들 중에서 HTML 태그를 필터링하여 처리합니다.
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                // HTML 태그를 변경하여 필터링한 결과를 새로운 배열로 반환합니다.
                values[i] = filterHTMLTags(values[i]);
            } else {
                values[i] = null;
            }
        }

        return values;
    }

    // 매개변수로 전달된 파라미터의 값에서 HTML 태그를 필터링하여 처리합니다.
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        if (value == null) {
            return null;
        }

        // HTML 태그를 변경하여 필터링한 결과를 반환합니다.
        return filterHTMLTags(value);
    }

    // 문자열에서 HTML 태그를 변경하여 필터링합니다.
    private String filterHTMLTags(String input) {
        StringBuffer strBuff = new StringBuffer();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
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

        return strBuff.toString();
    }
}
