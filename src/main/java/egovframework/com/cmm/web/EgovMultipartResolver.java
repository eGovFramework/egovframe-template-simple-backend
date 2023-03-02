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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.let.utl.fcc.service.EgovFileUploadUtil;

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
public class EgovMultipartResolver extends CommonsMultipartResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovMultipartResolver.class);

	public EgovMultipartResolver() {
	}

	/**
	 * 첨부파일 처리를 위한 multipart resolver를 생성한다.
	 *
	 * @param servletContext
	 */
	public EgovMultipartResolver(ServletContext servletContext) {
		super(servletContext);
	}

	/**
	 * multipart에 대한 parsing을 처리한다.
	 */
	@Override
	protected MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding) {

		// 스프링 3.0변경으로 수정한 부분
		MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
		Map<String, String[]> multipartParameters = new HashMap<String, String[]>();
		String whiteListFileUploadExtensions = EgovProperties.getProperty("Globals.fileUpload.Extensions");
		Map<String, String> mpParamContentTypes = new HashMap<String, String>();

		// Extract multipart files and multipart parameters.
		for (Iterator<FileItem> it = fileItems.iterator(); it.hasNext();) {
			FileItem fileItem = it.next();

			if (fileItem.isFormField()) {

				String value = null;
				if (encoding != null) {
					try {
						value = fileItem.getString(encoding);
					} catch (UnsupportedEncodingException ex) {
						LOGGER.warn("Could not decode multipart item '{}' with encoding '{}': using platform default",
								fileItem.getFieldName(), encoding);
						value = fileItem.getString();
					}
				} else {
					value = fileItem.getString();
				}
				String[] curParam = multipartParameters.get(fileItem.getFieldName());
				if (curParam == null) {
					// simple form field
					multipartParameters.put(fileItem.getFieldName(), new String[] { value });
				} else {
					// array of simple form fields
					String[] newParam = StringUtils.addStringToArray(curParam, value);
					multipartParameters.put(fileItem.getFieldName(), newParam);
				}

				//contentType 입력
				mpParamContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
			} else {

				CommonsMultipartFile file = createMultipartFile(fileItem);
				multipartFiles.add(file.getName(), file);

				LOGGER.debug("Found multipart file [{" + file.getName() + "}] of size {" + file.getSize()
						+ "} bytes with original filename [{" + file.getOriginalFilename() + "}], stored {"
						+ file.getStorageDescription() + "}");

				String fileName = file.getOriginalFilename();
				String fileExtension = EgovFileUploadUtil.getFileExtension(fileName);
				LOGGER.debug("Found File Extension = "+fileExtension);
				if (whiteListFileUploadExtensions == null || "".equals(whiteListFileUploadExtensions)) {
					LOGGER.debug("The file extension whitelist has not been set.");
				} else {
					if (fileName == null || "".equals(fileName)) {
						LOGGER.debug("No file name.");
					} else {
						if ("".equals(fileExtension)) { // 확장자 없는 경우 처리 불가
							throw new SecurityException("[No file extension] File extension not allowed.");
						}
						if ((whiteListFileUploadExtensions+".").contains("."+fileExtension.toLowerCase()+".")) {
							LOGGER.debug("File extension allowed.");
						} else {
							throw new SecurityException("["+fileExtension+"] File extension not allowed.");
						}
					}
				}

			}
		}

		return new MultipartParsingResult(multipartFiles, multipartParameters, mpParamContentTypes);//2022.01. Method call passes null for non-null parameter 처리
	}
}
