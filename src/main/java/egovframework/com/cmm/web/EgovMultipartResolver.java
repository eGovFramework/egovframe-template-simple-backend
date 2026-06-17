package egovframework.com.cmm.web;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the ";License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS"; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.Iterator;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.let.utl.fcc.service.EgovFileUploadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 실행환경의 파일업로드 처리를 위한 기능 클래스
 *
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일                수정자             수정내용
 *  ----------   --------    ---------------------------
 *  2009.03.25   이삼섭              최초 생성
 *  2011.06.11   서준식              스프링 3.0 업그레이드 API변경으로인한 수정
 *  2020.10.27   신용호              예외처리 수정
 *  2020.10.29   신용호              허용되지 않는 확장자 업로드 제한 (globals.properties > Globals.fileUpload.Extensions)
 *
 *      </pre>
 */
@Slf4j
public class EgovMultipartResolver extends StandardServletMultipartResolver {

	public EgovMultipartResolver() {
	}

	/**
	 * multipart 요청에 대한 parsing을 처리하고 확장자 검증을 수행한다.
	 */
	@Override
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = super.resolveMultipart(request);
		
		// 확장자 제한 검증
		String whiteListFileUploadExtensions = EgovProperties.getProperty("Globals.fileUpload.Extensions");
		
		if (whiteListFileUploadExtensions != null && !"".equals(whiteListFileUploadExtensions)) {
			Iterator<String> fileNames = multipartRequest.getFileNames();
			
			while (fileNames.hasNext()) {
				String fileName = fileNames.next();
				MultipartFile file = multipartRequest.getFile(fileName);
				
				if (file != null && !file.isEmpty()) {
					String originalFileName = file.getOriginalFilename();
					
					log.debug("Found multipart file [{}] of size {} bytes with original filename [{}]",
							fileName, file.getSize(), originalFileName);
					
					if (originalFileName == null || "".equals(originalFileName)) {
						log.debug("No file name.");
						continue;
					}
					
					String fileExtension = EgovFileUploadUtil.getFileExtension(originalFileName);
					log.debug("Found File Extension = {}", fileExtension);
					
					if ("".equals(fileExtension)) {
						// 확장자 없는 경우 처리 불가
						throw new SecurityException("[No file extension] File extension not allowed.");
					}
					
					if (!(whiteListFileUploadExtensions + ".").contains("." + fileExtension.toLowerCase() + ".")) {
						throw new SecurityException("[" + fileExtension + "] File extension not allowed.");
					}
					
					log.debug("File extension allowed.");
				}
			}
		} else {
			log.debug("The file extension whitelist has not been set.");
		}
		
		return multipartRequest;
	}
}
