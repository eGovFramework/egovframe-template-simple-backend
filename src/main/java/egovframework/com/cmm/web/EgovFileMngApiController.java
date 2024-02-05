package egovframework.com.cmm.web;

import java.util.Base64;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 파일 조회, 삭제, 다운로드 처리를 위한 컨트롤러 클래스
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
 *   2009.03.25  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@RestController
@Tag(name="EgovFileMngApiController",description = "파일 관리")
public class EgovFileMngApiController {

    @Resource(name = "EgovFileMngService")
    private EgovFileMngService fileService;

	/** 암호화서비스 */
    @Resource(name="egovARIACryptoService")
    EgovCryptoService cryptoService;

    /**
     * 첨부파일에 대한 삭제를 처리한다.
     *
     * @param atchFileId
     * @param fileSn
     * @return resultVO
     * @throws Exception
     */
    @Operation(
			summary = "파일 삭제",
			description = "첨부파일에 대한 삭제를 처리",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovFileMngApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
	})
    @PostMapping(value ="/file")
    public ResultVO deleteFileInf(HttpServletRequest request, @RequestBody FileVO fileVO) throws Exception {
    	ResultVO resultVO = new ResultVO();
    	
    	// 암호화된 atchFileId 를 복호화 (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
    	String atchFileId = fileVO.getAtchFileId().replaceAll(" ", "+");
    	byte[] decodedBytes = Base64.getDecoder().decode(atchFileId);
    	String decodedFileId = new String(cryptoService.decrypt(decodedBytes,EgovFileDownloadController.ALGORITM_KEY));
    			
    	fileVO.setAtchFileId(decodedFileId);

		//Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		fileService.deleteFileInf(fileVO);

		resultVO.setResultCode(200);
		resultVO.setResultMessage("삭제 성공");


		//--------------------------------------------
		// contextRoot가 있는 경우 제외 시켜야 함
		//--------------------------------------------
		////return "forward:/cmm/fms/selectFileInfs.do";
		//return "forward:" + returnUrl;

		return resultVO;
    }
}
