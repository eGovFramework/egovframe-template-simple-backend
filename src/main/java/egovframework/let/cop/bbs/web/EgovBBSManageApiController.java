package egovframework.let.cop.bbs.web;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.web.EgovFileDownloadController;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 게시물 관리를 위한 컨트롤러 클래스
 * 
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.19
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *   2009.03.19  이삼섭          최초 생성
 *   2009.06.29  한성곤          2단계 기능 추가 (댓글관리, 만족도조사)
 *   2011.08.31  JJY           경량환경 템플릿 커스터마이징버전 생성
 *   2024.08.26  이백행          컨트리뷰션 롬복 생성자 기반 종속성 주입
 *
 *      </pre>
 */
@RestController
@Tag(name = "EgovBBSManageApiController", description = "게시물 관리")
@RequiredArgsConstructor
public class EgovBBSManageApiController {

	private static final String HEADER_STRING = "Authorization";

	private final EgovJwtTokenUtil egovJwtTokenUtil;

	/**
	 * 게시물 관리를 위한 서비스 인터페이스 클래스
	 */
	private final EgovBBSManageService egovBBSManageService;

	/**
	 * 게시판 속성관리를 위한 서비스 인터페이스 클래스
	 */
	private final EgovBBSAttributeManageService egovBBSAttributeManageService;

	/**
	 * 파일정보의 관리를 위한 서비스 인터페이스
	 */
	private final EgovFileMngService egovFileMngService;

	/**
	 * 파일정보의 관리를 위한 유틸리티
	 */
	private final EgovFileMngUtil egovFileMngUtil;

	/**
	 * Property 서비스의 인터페이스 클래스
	 */
	private final EgovPropertyService egovPropertyService;

	/**
	 * 암호화서비스
	 */
	private final EgovCryptoService egovCryptoService;

	// ---------------------------------
	// 2009.06.29 : 2단계 기능 추가
	// ---------------------------------
	// SHT-CUSTOMIZING//@Resource(name = "EgovBBSCommentService")
	// SHT-CUSTOMIZING//private EgovBBSCommentService bbsCommentService;

	// SHT-CUSTOMIZING//@Resource(name = "EgovBBSSatisfactionService")
	// SHT-CUSTOMIZING//private EgovBBSSatisfactionService bbsSatisfactionService;

	// SHT-CUSTOMIZING//@Resource(name = "EgovBBSScrapService")
	// SHT-CUSTOMIZING//private EgovBBSScrapService bbsScrapService;
	//// -------------------------------

	private final DefaultBeanValidator beanValidator;

	/**
	 * 게시판 마스터 상세내용을 조회한다. 파일 첨부 가능 여부 조회용
	 *
	 * @param request
	 * @param searchVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "게시판 파일 첨부 관련 정보 조회", description = "게시판의 파일 첨부가능 여부 및 첨부가능 파일 수 조회", tags = {
			"EgovBBSManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@GetMapping(value = "/boardFileAtch/{bbsId}")
	public ResultVO selectUserBBSMasterInf(
			@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example = "BBSMSTR_AAAAAAAAAAAA") @PathVariable("bbsId") String bbsId)
			throws Exception {
		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		BoardMasterVO searchVO = new BoardMasterVO();
		searchVO.setBbsId(bbsId);

		BoardMasterVO master = egovBBSAttributeManageService.selectBBSMasterInf(searchVO);

		// 파일 첨부 외의 다른 정보를 전달하지 않기 위해 신규 객체 생성
		BoardMasterVO masterFileAtchInfo = new BoardMasterVO();

		masterFileAtchInfo.setFileAtchPosblAt(master.getFileAtchPosblAt());
		masterFileAtchInfo.setPosblAtchFileNumber(master.getPosblAtchFileNumber());
		masterFileAtchInfo.setPosblAtchFileSize(master.getPosblAtchFileSize());

		resultMap.put("brdMstrVO", masterFileAtchInfo);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 게시물에 대한 목록을 조회한다.
	 *
	 * @param boardVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "게시물 목록 조회", description = "게시물에 대한 목록을 조회", tags = { "EgovBBSManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@GetMapping(value = "/board")
	public ResultVO selectBoardArticles(
			@Parameter(in = ParameterIn.QUERY, schema = @Schema(type = "object", additionalProperties = Schema.AdditionalPropertiesValue.TRUE, ref = "#/components/schemas/searchBbsMap"), style = ParameterStyle.FORM, explode = Explode.TRUE) @RequestParam Map<String, Object> commandMap,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception {
		ResultVO resultVO = new ResultVO();
		BoardVO boardVO = new BoardVO();

		boardVO.setBbsId((String) commandMap.get("bbsId"));
		boardVO.setSearchCnd((String) commandMap.get("searchCnd"));
		boardVO.setSearchWrd((String) commandMap.get("searchWrd"));

		BoardMasterVO vo = new BoardMasterVO();
		vo.setBbsId(boardVO.getBbsId());
		vo.setUniqId(user.getUniqId());

		BoardMasterVO master = egovBBSAttributeManageService.selectBBSMasterInf(vo);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(egovPropertyService.getInt("Globals.pageUnit"));
		paginationInfo.setPageSize(egovPropertyService.getInt("Globals.pageSize"));

		boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> resultMap = egovBBSManageService.selectBoardArticles(boardVO, vo.getBbsAttrbCode());

		int totCnt = Integer.parseInt((String) resultMap.get("resultCnt"));
		paginationInfo.setTotalRecordCount(totCnt);

		resultMap.put("boardVO", boardVO);
		resultMap.put("brdMstrVO", master);
		resultMap.put("paginationInfo", paginationInfo);
		resultMap.put("user", user);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		resultVO.setResult(resultMap);

		return resultVO;
	}

	/**
	 * 게시물에 대한 상세 정보를 조회한다.
	 *
	 * @param boardVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "게시물 상세 조회", description = "게시물에 대한 상세 정보를 조회", tags = { "EgovBBSManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@GetMapping(value = "/board/{bbsId}/{nttId}")
	public ResultVO selectBoardArticle(
			@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example = "BBSMSTR_AAAAAAAAAAAA") @PathVariable("bbsId") String bbsId,
			@Parameter(name = "nttId", description = "게시글 Id", in = ParameterIn.PATH, example = "1") @PathVariable("nttId") String nttId,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception {

		ResultVO resultVO = new ResultVO();
		BoardVO boardVO = new BoardVO();

		boardVO.setBbsId(bbsId);
		boardVO.setNttId(Long.parseLong(nttId));

		// 조회수 증가 여부 지정
		boardVO.setPlusCount(true);

		// ---------------------------------
		// 2009.06.29 : 2단계 기능 추가
		// ---------------------------------
		if (!boardVO.getSubPageIndex().equals("")) {
			boardVO.setPlusCount(false);
		}
		//// -------------------------------

		boardVO.setLastUpdusrId(user.getUniqId());
		BoardVO vo = egovBBSManageService.selectBoardArticle(boardVO);

		// ----------------------------
		// template 처리 (기본 BBS template 지정 포함)
		// ----------------------------
		BoardMasterVO master = new BoardMasterVO();

		master.setBbsId(boardVO.getBbsId());
		master.setUniqId(user.getUniqId());

		BoardMasterVO masterVo = egovBBSAttributeManageService.selectBBSMasterInf(master);

		// model.addAttribute("brdMstrVO", masterVo);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("boardVO", vo);
		resultMap.put("sessionUniqId", user.getUniqId());
		resultMap.put("brdMstrVO", masterVo);
		resultMap.put("user", user);

		// 2021-06-01 신용호 추가
		// 첨부파일 확인
		if (vo != null && vo.getAtchFileId() != null && !vo.getAtchFileId().isEmpty()) {
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(vo.getAtchFileId());
			List<FileVO> resultFiles = egovFileMngService.selectFileInfs(fileVO);

			// FileId를 유추하지 못하도록 암호화하여 표시한다. (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
			for (FileVO file : resultFiles) {
				String toEncrypt = file.atchFileId;
				file.setAtchFileId(Base64.getEncoder().encodeToString(
						egovCryptoService.encrypt(toEncrypt.getBytes(), EgovFileDownloadController.ALGORITM_KEY)));
			}

			resultMap.put("resultFiles", resultFiles);
		}

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		return resultVO;
	}

	/**
	 * 게시물에 대한 내용을 수정한다.
	 *
	 * @param boardVO
	 * @param multiRequest
	 * @param bindingResult
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "게시물 수정", description = "게시물에 대한 내용을 수정", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovBBSManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류") })
	@PutMapping(value = "/board/{nttId}")
	public ResultVO updateBoardArticle(final MultipartHttpServletRequest multiRequest, BoardVO boardVO,
			@Parameter(name = "nttId", description = "게시글 Id", in = ParameterIn.PATH, example = "1") @PathVariable("nttId") String nttId,
			BindingResult bindingResult, HttpServletRequest request) throws Exception {
		ResultVO resultVO = new ResultVO();

		// step 1. request header에서 토큰을 가져온다.
		String jwtToken = EgovStringUtil.isNullToString(request.getHeader(HEADER_STRING));
		// step 2. 토큰에 내용이 있는지 확인해서 id값을 가져옴
		String uniqId = egovJwtTokenUtil.getInfoFromToken("uniqId", jwtToken);
		String userNm = egovJwtTokenUtil.getInfoFromToken("name", jwtToken);
		// 사용자권한 처리
		LoginVO user = new LoginVO();
		user.setUniqId(uniqId); // 고정값(USRCNFRM_00000000000)에서 로그인 시 사용자 고유ID값으로 변경

		String atchFileId = boardVO.getAtchFileId().replaceAll("\\s", "");

		beanValidator.validate(boardVO, bindingResult);
		if (bindingResult.hasErrors()) {

			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());

			return resultVO;
		}

		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		if (!files.isEmpty()) {
			if ("".equals(atchFileId)) {
				List<FileVO> result = egovFileMngUtil.parseFileInf(files, "BBS_", 0, atchFileId, "");
				atchFileId = egovFileMngService.insertFileInfs(result);
				boardVO.setAtchFileId(atchFileId);
			} else {
				FileVO fvo = new FileVO();
				fvo.setAtchFileId(atchFileId);
				int cnt = egovFileMngService.getMaxFileSN(fvo);
				List<FileVO> _result = egovFileMngUtil.parseFileInf(files, "BBS_", cnt, atchFileId, "");
				egovFileMngService.updateFileInfs(_result);
			}
		}

		boardVO.setNttId(Long.parseLong(nttId));
		boardVO.setLastUpdusrId(user.getUniqId());
		boardVO.setNtcrNm(userNm); // jwt토큰값으로 추가. dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		boardVO.setPassword(EgovFileScrty.encryptPassword("", user.getUniqId())); // dummy 오류 수정 (익명이 아닌 경우 validator
																					// 처리를 위해 dummy로 지정됨)
		boardVO.setNttCn(unscript(boardVO.getNttCn())); // XSS 방지

		egovBBSManageService.updateBoardArticle(boardVO);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 게시물을 등록한다.
	 *
	 * @param multiRequest
	 * @param boardVO
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Operation(summary = "게시물 등록", description = "게시물을 등록", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovBBSManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류") })
	@PostMapping(value = "/board")
	public ResultVO insertBoardArticle(final MultipartHttpServletRequest multiRequest, BoardVO boardVO,
			BindingResult bindingResult, HttpServletRequest request) throws Exception {
		ResultVO resultVO = new ResultVO();

		// step 1. request header에서 토큰을 가져온다.
		String jwtToken = EgovStringUtil.isNullToString(request.getHeader(HEADER_STRING));
		// step 2. 토큰에 내용이 있는지 확인해서 id값을 가져옴
		String uniqId = egovJwtTokenUtil.getInfoFromToken("uniqId", jwtToken);
		String userNm = egovJwtTokenUtil.getInfoFromToken("name", jwtToken);
		// 사용자권한 처리
		LoginVO user = new LoginVO();
		user.setUniqId(uniqId); // 고정값(USRCNFRM_00000000000)에서 로그인 시 사용자 고유ID값으로 변경

		beanValidator.validate(boardVO, bindingResult);
		if (bindingResult.hasErrors()) {
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());

			return resultVO;
		}

		List<FileVO> result = null;
		String atchFileId = "";

		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		if (!files.isEmpty()) {
			result = egovFileMngUtil.parseFileInf(files, "BBS_", 0, "", "");
			atchFileId = egovFileMngService.insertFileInfs(result);
		}
		boardVO.setAtchFileId(atchFileId);
		boardVO.setFrstRegisterId(user.getUniqId());
		boardVO.setBbsId(boardVO.getBbsId());

		boardVO.setNtcrNm(userNm); // jwt토큰값으로 추가. dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		boardVO.setPassword(EgovFileScrty.encryptPassword("", user.getUniqId())); // dummy 오류 수정 (익명이 아닌 경우 validator
																					// 처리를 위해 dummy로 지정됨)
		// board.setNttCn(unscript(board.getNttCn())); // XSS 방지

		egovBBSManageService.insertBoardArticle(boardVO);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		return resultVO;
	}

	/**
	 * 게시물에 대한 답변을 등록한다.
	 *
	 * @param multiRequest
	 * @param boardVO
	 * @param bindingResult
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "게시물 답변 등록", description = "게시물에 대한 답변을 등록", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovBBSManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류") })
	@PostMapping(value = "/boardReply")
	public ResultVO replyBoardArticle(final MultipartHttpServletRequest multiRequest, BoardVO boardVO,
			BindingResult bindingResult, HttpServletRequest request) throws Exception {
		ResultVO resultVO = new ResultVO();

		// step 1. request header에서 토큰을 가져온다.
		String jwtToken = EgovStringUtil.isNullToString(request.getHeader(HEADER_STRING));
		// step 2. 토큰에 내용이 있는지 확인해서 id값을 가져옴
		String uniqId = egovJwtTokenUtil.getInfoFromToken("uniqId", jwtToken);
		String userNm = egovJwtTokenUtil.getInfoFromToken("name", jwtToken);
		// 사용자권한 처리
		LoginVO user = new LoginVO();
		user.setUniqId(uniqId); // 고정값(USRCNFRM_00000000000)에서 로그인 시 사용자 고유ID값으로 변경

		beanValidator.validate(boardVO, bindingResult);
		if (bindingResult.hasErrors()) {
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());

			return resultVO;
		}

		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		String atchFileId = "";

		if (!files.isEmpty()) {
			List<FileVO> result = egovFileMngUtil.parseFileInf(files, "BBS_", 0, "", "");
			atchFileId = egovFileMngService.insertFileInfs(result);
		}

		boardVO.setAtchFileId(atchFileId);
		boardVO.setReplyAt("Y");
		boardVO.setFrstRegisterId(user.getUniqId());
		boardVO.setBbsId(boardVO.getBbsId());
		boardVO.setParnts(Long.toString(boardVO.getNttId()));
		boardVO.setSortOrdr(boardVO.getSortOrdr());
		boardVO.setReplyLc(Integer.toString(Integer.parseInt(boardVO.getReplyLc()) + 1));

		boardVO.setNtcrNm(userNm); // jwt토큰값으로 추가. dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		boardVO.setPassword(EgovFileScrty.encryptPassword("", user.getUniqId())); // dummy 오류 수정 (익명이 아닌 경우 validator
																					// 처리를 위해 dummy로 지정됨)

		boardVO.setNttCn(unscript(boardVO.getNttCn())); // XSS 방지

		egovBBSManageService.insertBoardArticle(boardVO);

		// return "forward:/cop/bbs/selectBoardList.do";
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		return resultVO;
	}

	/**
	 * 게시물에 대한 내용을 삭제한다.
	 *
	 * @param boardVO
	 * @param nttId
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "게시물 삭제", description = "게시물에 대한 내용을 삭제", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovBBSManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "삭제 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@PatchMapping(value = "/board/{bbsId}/{nttId}")
	public ResultVO deleteBoardArticle(
			@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example = "BBSMSTR_AAAAAAAAAAAA") @PathVariable("bbsId") String bbsId,
			@Parameter(name = "nttId", description = "게시글 Id", in = ParameterIn.PATH, example = "1") @PathVariable("nttId") String nttId,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user, HttpServletRequest request)

			throws Exception {
		ResultVO resultVO = new ResultVO();
		BoardVO boardVO = new BoardVO();

		boardVO.setBbsId(bbsId);
		boardVO.setNttId(Long.parseLong(nttId));
		boardVO.setLastUpdusrId(user.getUniqId());

		egovBBSManageService.deleteBoardArticle(boardVO);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * XSS 방지 처리.
	 *
	 * @param data
	 * @return
	 */
	protected String unscript(String data) {
		if (data == null || data.trim().equals("")) {
			return "";
		}

		String ret = data;

		ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
		ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

		ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
		ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

		ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
		ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

		ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
		ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

		ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
		ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

		return ret;
	}

}