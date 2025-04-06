package egovframework.let.cop.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 게시판 속성관리를 위한 컨트롤러  클래스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자            수정내용
 *  -------    --------        ---------------------------
 *  2009.03.12  이삼섭           최초 생성
 *  2009.06.26	한성곤		    2단계 기능 추가 (댓글관리, 만족도조사)
 *  2011.08.31  JJY             경량환경 템플릿 커스터마이징버전 생성
 *  2024.04.06  김재섭(nirsa)     생성자 주입 전환, 불필요한 필드 제거, ResultVoHelper 적용 및 1차 코드 리팩토링
 *
 *  </pre>
 */
@RestController
@RequiredArgsConstructor
@Tag(name="EgovBBSAttributeManageApiController",description = "게시판 속성관리")
public class EgovBBSAttributeManageApiController {
	private final EgovBBSAttributeManageService bbsAttrbService;
	private final EgovCmmUseService cmmUseService;
	private final EgovPropertyService propertyService;
	private final DefaultBeanValidator beanValidator;
	private final ResultVoHelper resultVoHelper;

	/**
	 * 게시판 마스터 목록을 조회한다.
	 *
	 * @param request
	 * @param boardMasterVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 조회",
			description = "게시판 마스터 목록을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value = "/bbsMaster")
	public ResultVO selectBBSMasterInfs(@ModelAttribute BoardMasterVO boardMasterVO)
		throws Exception {
		PaginationInfo paginationInfo = new PaginationInfo();
		boardMasterVO.setPageUnit(propertyService.getInt("Globals.pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("Globals.pageSize"));
		
		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
		paginationInfo.setPageSize(boardMasterVO.getPageSize());
		
		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> resultMap = bbsAttrbService.selectBBSMasterInfs(boardMasterVO);
		int totCnt = Integer.parseInt((String)resultMap.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		resultMap.put("paginationInfo", paginationInfo);
		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 게시판 마스터 상세내용을 조회한다.
	 *
	 * @param request
	 * @param searchVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 상세 조회",
			description = "게시판 마스터 상세내용을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value ="/bbsMaster/{bbsId}")
	public ResultVO selectBBSMasterInf(@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA") 
			@PathVariable("bbsId") String bbsId)
		throws Exception {
		BoardMasterVO searchVO = new BoardMasterVO();
		searchVO.setBbsId(bbsId);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(searchVO);
		resultMap.put("boardMasterVO", vo);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 신규 게시판 마스터 정보를 등록한다.
	 *
	 * @param request
	 * @param boardMasterVO
	 * @param bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 등록",
			description = "신규 게시판 마스터 정보를 등록",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping(value ="/bbsMaster")
	public ResultVO insertBBSMasterInf(BoardMasterVO boardMasterVO,
									   BindingResult bindingResult,
									   @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO
	)
		throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		beanValidator.validate(boardMasterVO, bindingResult);
		if (bindingResult.hasErrors()) {
			ComDefaultCodeVO vo = new ComDefaultCodeVO();
			vo.setCodeId("COM004");
			List<CmmnDetailCode> codeResult = cmmUseService.selectCmmCodeDetail(vo);
			resultMap.put("typeList", codeResult);

			vo.setCodeId("COM009");
			codeResult = cmmUseService.selectCmmCodeDetail(vo);
			resultMap.put("attrbList", codeResult);

			return resultVoHelper.buildFromMap(resultMap, ResponseCode.INPUT_CHECK_ERROR);
		}

		boardMasterVO.setFrstRegisterId(loginVO.getUniqId());
		boardMasterVO.setUseAt("Y");
		boardMasterVO.setTrgetId("SYSTEMDEFAULT_REGIST");
		boardMasterVO.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));

		bbsAttrbService.insertBBSMastetInf(boardMasterVO);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 게시판 마스터 정보를 수정한다.
	 *
	 * @param request
	 * @param bbsId
	 * @param boardMasterVO
	 * @param bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 수정",
			description = "게시판 마스터 정보를 수정",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping(value ="/bbsMaster/{bbsId}")
	public ResultVO updateBBSMasterInf(@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")
										@PathVariable("bbsId") String bbsId,
										@RequestBody BoardMasterVO boardMasterVO,
										BindingResult bindingResult,
										@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO
										) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		beanValidator.validate(boardMasterVO, bindingResult);

		if (bindingResult.hasErrors()) {
			BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(boardMasterVO);
			resultMap.put("BoardMasterVO", vo);

			return resultVoHelper.buildFromMap(resultMap, ResponseCode.INPUT_CHECK_ERROR);
		}

		boardMasterVO.setLastUpdusrId(loginVO.getUniqId());
		boardMasterVO.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));
		bbsAttrbService.updateBBSMasterInf(boardMasterVO);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 게시판 마스터 정보를 삭제한다.
	 *
	 * @param request
	 * @param bbsId
	 * @param boardMasterVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 삭제",
			description = "게시판 마스터 정보를 삭제",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "삭제 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@PatchMapping(value ="/bbsMaster/{bbsId}")
	public ResultVO deleteBBSMasterInf(@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO,
		@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")
		@PathVariable("bbsId") String bbsId
		) throws Exception {
			BoardMasterVO boardMasterVO = new BoardMasterVO();
			
			boardMasterVO.setLastUpdusrId(loginVO.getUniqId());
			boardMasterVO.setBbsId(bbsId);
			
			bbsAttrbService.deleteBBSMasterInf(boardMasterVO);

			return resultVoHelper.buildFromMap(new HashMap<String, Object>(), ResponseCode.SUCCESS);
	}

}
