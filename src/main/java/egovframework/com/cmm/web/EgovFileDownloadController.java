package egovframework.com.cmm.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 파일 다운로드를 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.3.25  이삼섭          최초 생성
 *
 * Copyright (C) 2009 by MOPAS  All right reserved.
 * </pre>
 */
@Slf4j
@Controller
@Tag(name="EgovFileDownloadController",description = "파일 다운로드")
public class EgovFileDownloadController {

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;
	
	/** 암호화서비스 */
    @Resource(name="egovARIACryptoService")
    EgovCryptoService cryptoService;
	
	public static final String ALGORITM_KEY = EgovProperties.getProperty("Globals.crypto.algoritm");

	/**
	 * 브라우저 구분 얻기.
	 *
	 * @param request
	 * @return
	 */
	private String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Trident") > -1) { // IE11 문자열 깨짐 방지
			return "Trident";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	/**
	 * Disposition 지정하기.
	 *
	 * @param filename
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Trident")) { // IE11 문자열 깨짐 방지
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			//throw new RuntimeException("Not supported browser");
			throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

		if ("Opera".equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
	}

	/**
	 * 첨부파일로 등록된 파일에 대하여 다운로드를 제공한다.
	 *
	 * @param commandMap
	 * @param response
	 * @throws Exception
	 */
	
	@Operation(
			summary = "파일 다운로드",
			description = "첨부파일로 등록된 파일에 대하여 다운로드를 제공",
			tags = {"EgovFileDownloadController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
	})
	@GetMapping(value = "/file")
	public void cvplFileDownload(
			@Parameter(
					in = ParameterIn.QUERY,
					schema = @Schema(type = "object",
							additionalProperties = Schema.AdditionalPropertiesValue.TRUE, 
							ref = "#/components/schemas/fileMap"),
					style = ParameterStyle.FORM,
					explode = Explode.TRUE
			) @RequestParam Map<String, Object> commandMap, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			// 암호화된 atchFileId 를 복호화 (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
			String param_atchFileId = (String) commandMap.get("atchFileId");
			param_atchFileId = param_atchFileId.replaceAll(" ", "+");
			byte[] decodedBytes = Base64.getDecoder().decode(param_atchFileId);
			String decodedFileId = new String(cryptoService.decrypt(decodedBytes,ALGORITM_KEY));
			String fileSn = (String) commandMap.get("fileSn");
			
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(decodedFileId);
			fileVO.setFileSn(fileSn);
			FileVO fvo = fileService.selectFileInf(fileVO);

			String fileStreCours = EgovWebUtil.filePathBlackList(fvo.getFileStreCours());
			String streFileNm = EgovWebUtil.filePathBlackList(fvo.getStreFileNm());

			File uFile = new File(fileStreCours, streFileNm);
			long fSize = uFile.length();

			if (fSize > 0) {
				//String mimetype = "application/x-msdownload";
				String mimetype = "application/x-stuff";

				//response.setBufferSize(fSize);	// OutOfMemeory 발생
				response.setContentType(mimetype);
				//response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
				setDisposition(fvo.getOrignlFileNm(), request, response);
				//response.setContentLength(fSize);

				/*
				 * FileCopyUtils.copy(in, response.getOutputStream());
				 * in.close();
				 * response.getOutputStream().flush();
				 * response.getOutputStream().close();
				 */
				
				// Try-with-resources를 이용한 자원 해제 처리 (try 구문에 선언한 리소스를 자동 반납)
				// try에 전달할 수 있는 자원은 java.lang.AutoCloseable 인터페이스의 구현 객체로 한정
				try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(uFile));
				     BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());){
				        FileCopyUtils.copy(in, out);
					out.flush();
				} catch (FileNotFoundException ex) {
					log.debug("IGNORED: {}", ex.getMessage());
				}

			} else {
				throw new EgovBizException();
			}
		}
}
