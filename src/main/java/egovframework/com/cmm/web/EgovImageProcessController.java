package egovframework.com.cmm.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.service.EgovFileMngService;
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
 * @Class Name : EgovImageProcessController.java
 * @Description :
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 4. 2.     이삼섭
 *    2011.08.31.     JJY        경량환경 템플릿 커스터마이징버전 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 4. 2.
 * @version
 * @see
 *
 */
@Slf4j
@Controller
@Tag(name="EgovImageProcessController",description = "이미지 처리")
public class EgovImageProcessController extends HttpServlet {

	/**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -6339945210971171173L;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;
	
	/** 암호화서비스 */
    @Resource(name="egovARIACryptoService")
    EgovCryptoService cryptoService;

	/**
	 * 첨부된 이미지에 대한 미리보기 기능을 제공한다.
	 *
	 * @param atchFileId
	 * @param fileSn
	 * @param sessionVO
	 * @param model
	 * @param response
	 * @throws Exception
	 */
    @Operation(
			summary = "이미지 미리보기",
			description = "첨부된 이미지에 대한 미리보기 기능을 제공",
			tags = {"EgovImageProcessController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
	})
    @GetMapping("/image")
	public void getImageInf(
			@Parameter(hidden = true)SessionVO sessionVO, 
			ModelMap model, 
			@Parameter(
					in = ParameterIn.QUERY,
					schema = @Schema(type = "object",
							additionalProperties = Schema.AdditionalPropertiesValue.TRUE, 
							ref = "#/components/schemas/fileMap"),
					style = ParameterStyle.FORM,
					explode = Explode.TRUE
			) @RequestParam Map<String, Object> commandMap,
		HttpServletResponse response) throws Exception {

		// 암호화된 atchFileId 를 복호화. (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
		String param_atchFileId = (String) commandMap.get("atchFileId");
		param_atchFileId = param_atchFileId.replaceAll(" ", "+");
		byte[] decodedBytes = Base64.getDecoder().decode(param_atchFileId);
		String decodedFileId = new String(cryptoService.decrypt(decodedBytes,EgovFileDownloadController.ALGORITM_KEY));
		String fileSn = (String) commandMap.get("fileSn");

		FileVO vo = new FileVO();

		vo.setAtchFileId(decodedFileId);
		vo.setFileSn(fileSn);

		FileVO fvo = fileService.selectFileInf(vo);

		//String fileLoaction = fvo.getFileStreCours() + fvo.getStreFileNm();
		String fileStreCours = EgovWebUtil.filePathBlackList(fvo.getFileStreCours());
		String streFileNm = EgovWebUtil.filePathBlackList(fvo.getStreFileNm());

		File file = new File(fileStreCours, streFileNm);
		
		// Try-with-resources를 이용한 자원 해제 처리 (try 구문에 선언한 리소스를 자동 반납)
		// try에 전달할 수 있는 자원은 java.lang.AutoCloseable 인터페이스의 구현 객체로 한정
		try (FileInputStream fis = new FileInputStream(file);
		     BufferedInputStream in = new BufferedInputStream(fis);
		     ByteArrayOutputStream bStream = new ByteArrayOutputStream();) {
			
			int imgByte;
			while ((imgByte = in.read()) != -1) {
				bStream.write(imgByte);
			}

			String type = "";

			if (fvo.getFileExtsn() != null && !"".equals(fvo.getFileExtsn())) {
				if ("jpg".equals(fvo.getFileExtsn().toLowerCase())) {
					type = "image/jpeg";
				} else {
					type = "image/" + fvo.getFileExtsn().toLowerCase();
				}
				type = "image/" + fvo.getFileExtsn().toLowerCase();

			} else {
				log.debug("Image fileType is null.");
			}

			response.setHeader("Content-Type", type);
			response.setContentLength(bStream.size());

			bStream.writeTo(response.getOutputStream());

			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (IOException e) {
			log.debug("{}", e);
		}
	}
}
