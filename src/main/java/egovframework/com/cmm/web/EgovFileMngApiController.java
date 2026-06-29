package egovframework.com.cmm.web;

import java.util.Base64;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileDeleteRequest;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.bbs.service.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;
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
 *   2009.03.25  이삼섭           최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *   2026.07     보안취약점대응     POST /file 삭제에 소유권 검증 추가
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
    private EgovCryptoService cryptoService;

    // 2026 보안취약점 대응 — 파일 소유권 검증을 위해 소유 엔티티(게시글/일정)를 조회한다.
    @Resource(name = "EgovBBSManageService")
    private EgovBBSManageService bbsMngService;

    @Resource(name = "egovIndvdlSchdulManageService")
    private EgovIndvdlSchdulManageService egovIndvdlSchdulManageService;

    /**
     * 첨부파일에 대한 삭제를 처리한다.
     *
     * <p>2026 보안취약점 대응: 소유권 검증을 수행한다.
     * 관리자(ROLE_ADMIN)는 전체 파일을, 일반 사용자는 본인이 작성한 엔티티(게시글/일정)의
     * 파일만 삭제할 수 있다. 요청에 동봉된 소유 컨텍스트(bbsId+nttId / schdulId)로
     * 엔티티를 조회해 작성자 및 atchFileId 일치 여부를 확인한다.</p>
     *
     * @param req atchFileId, fileSn + 소유 컨텍스트(bbsId+nttId 또는 schdulId)
     * @return resultVO
     * @throws Exception
     */
    @Operation(
			summary = "파일 삭제",
			description = "첨부파일에 대한 삭제를 처리(소유권 검증)",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovFileMngApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "403", description = "권한 없음")
	})
    @PostMapping(value ="/file")
    public ResultVO deleteFileInf(HttpServletRequest request, @RequestBody FileDeleteRequest req) throws Exception {
    	ResultVO resultVO = new ResultVO();

    	// 암호화된 atchFileId 를 복호화 (2022.12.06) - 파일아이디가 유추 불가능하도록 조치
    	String atchFileId = req.getAtchFileId().replaceAll(" ", "+");
    	byte[] decodedBytes = Base64.getDecoder().decode(atchFileId);
    	String decodedFileId = new String(cryptoService.decrypt(decodedBytes, EgovFileDownloadController.ALGORITM_KEY));

    	// 2026 보안취약점 대응 — 소유권 검증
    	// 규칙: ROLE_ADMIN 은 전체 / 일반 사용자는 본인 소유 엔티티의 파일만 삭제 가능
    	boolean isAdmin = EgovUserDetailsHelper.getAuthorities().contains("ROLE_ADMIN");
    	String ownerId = resolveFileOwner(req, decodedFileId);
    	LoginVO user = currentUser();

    	if (ownerId == null || (!isAdmin && !ownerId.equals(user.getUniqId()))) {
    		resultVO.setResultCode(403);
    		resultVO.setResultMessage("파일을 삭제할 권한이 없습니다.");
    		return resultVO;
    	}

    	// 서비스 호출용 FileVO 구성 (atchFileId 복호화본 + fileSn)
    	FileVO fileVO = new FileVO();
    	fileVO.setAtchFileId(decodedFileId);
    	fileVO.setFileSn(req.getFileSn());
    	fileService.deleteFileInf(fileVO);

    	resultVO.setResultCode(200);
    	resultVO.setResultMessage("삭제 성공");

    	return resultVO;
    }

    /** SecurityContext 의 인증 사용자(LoginVO)를 추출한다. 미인증 시 빈 LoginVO. */
    private LoginVO currentUser() {
    	Object principal = EgovUserDetailsHelper.getAuthenticatedUser();
    	return (principal instanceof LoginVO) ? (LoginVO) principal : new LoginVO();
    }

    /**
     * 삭제 대상 파일(decodedFileId)이 속한 소유 엔티티의 작성자 uniqId 를 반환한다.
     * 요청에 동봉된 컨텍스트로 엔티티를 조회하고, 그 엔티티의 atchFileId 가
     * 삭제 대상과 일치할 때만 작성자를 돌려준다(타 엔티티 ID 도용 차단).
     * 컨텍스트가 없거나 atchFileId 가 일치하지 않으면 null → 호출부에서 권한 없음 처리.
     */
    private String resolveFileOwner(FileDeleteRequest req, String decodedFileId) throws Exception {
    	// 1) 게시글: bbsId + nttId
    	String bbsId = req.getBbsId();
    	String nttId = req.getNttId();
    	if (bbsId != null && !bbsId.trim().isEmpty() && nttId != null && nttId.matches("\\d+")) {
    		BoardVO boardVO = new BoardVO();
    		boardVO.setBbsId(bbsId);
    		boardVO.setNttId(Long.parseLong(nttId));
    		boardVO.setPlusCount(false);
    		BoardVO article = bbsMngService.selectBoardArticle(boardVO);
    		if (article != null && decodedFileId.equals(article.getAtchFileId())) {
    			return article.getFrstRegisterId();
    		}
    		return null;
    	}
    	// 2) 일정: schdulId
    	String schdulId = req.getSchdulId();
    	if (schdulId != null && !schdulId.trim().isEmpty()) {
    		IndvdlSchdulManageVO svo = new IndvdlSchdulManageVO();
    		svo.setSchdulId(schdulId);
    		IndvdlSchdulManageVO detail = egovIndvdlSchdulManageService.selectIndvdlSchdulManageDetail(svo);
    		if (detail != null && decodedFileId.equals(detail.getAtchFileId())) {
    			return detail.getFrstRegisterId();
    		}
    		return null;
    	}
    	return null; // 소유 컨텍스트 없음 → 권한 없음 처리
    }
}
