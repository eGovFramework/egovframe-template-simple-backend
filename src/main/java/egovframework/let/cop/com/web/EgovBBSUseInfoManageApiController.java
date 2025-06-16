package egovframework.let.cop.com.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import egovframework.let.cop.bbs.dto.request.BbsAttributeSearchRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.com.service.BoardUseInfVO;
import egovframework.let.cop.com.service.EgovBBSUseInfoManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 게시판의 이용정보를 관리하기 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.02  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@Tag(name="EgovBBSUseInfoManageApiController",description = "게시판 이용정보 관리")
public class EgovBBSUseInfoManageApiController {
	
	private final EgovBBSUseInfoManageService bbsUseService;
	private final EgovPropertyService propertyService;
	private final EgovBBSAttributeManageService bbsAttrbService;
	private final DefaultBeanValidator beanValidator;
	private final ResultVoHelper resultVoHelper;

	/**
	 * 게시판 사용정보 목록을 조회한다.
	 *
	 * @param request
	 * @param bdUseVO
	 * @return
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 사용정보 목록 조회",
			description = "게시판 사용정보 목록을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value ="/bbsUseInf")
	public ResultVO selectBBSUseInfs(HttpServletRequest request,
			@ModelAttribute BbsAttributeSearchRequestDTO searchVO) throws Exception {
		BoardUseInfVO bdUseVO = new BoardUseInfVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		bdUseVO.setPageIndex(searchVO.getPageIndex());
		bdUseVO.setSearchWrd(searchVO.getSearchWrd());
		bdUseVO.setPageUnit(propertyService.getInt("Globals.pageUnit"));
		bdUseVO.setPageSize(propertyService.getInt("Globals.pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(bdUseVO.getPageIndex());
	    paginationInfo.setRecordCountPerPage(bdUseVO.getPageUnit());
	    paginationInfo.setPageSize(bdUseVO.getPageSize());

	    bdUseVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	    bdUseVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    bdUseVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = bbsUseService.selectBBSUseInfs(bdUseVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

	    resultMap.put("resultList", map.get("resultList"));
	    resultMap.put("resultCnt", map.get("resultCnt"));
	    resultMap.put("paginationInfo", paginationInfo);

	    return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 미사용 게시판 속성정보 목록을 조회한다
	 * @return
	 * @throws Exception
	 */
	@Operation(
			summary = "미사용 게시판 속성정보 목록 조회",
			description = "사용중이지 않은 게시판 속성 정보의 목록을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@GetMapping(value ="/notUsedBbsMaster")
	public ResultVO selectNotUsedBdMstrList() throws Exception {
		
		BoardMasterVO boardMasterVO = new BoardMasterVO();
		
		boardMasterVO.setFirstIndex(0);
		Map<String, Object> resultMap = bbsAttrbService.selectNotUsedBdMstrList(boardMasterVO);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 게시판 사용정보에 대한 상세정보를 조회한다.
	 *
	 * @param request
	 * @param bdUseVO
	 * @return
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 사용정보 상세 조회",
			description = "게시판 사용정보에 대한 상세정보를 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value ="/bbsUseInf/{trgetId}/{bbsId}")
	public ResultVO selectBBSUseInf(HttpServletRequest request, 
			@Parameter(name = "trgetId", description = "대상시스템 Id", in = ParameterIn.PATH, example="SYSTEM_DEFAULT_BOARD")
			@PathVariable("trgetId") String trgetId,
			@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")
			@PathVariable("bbsId") String bbsId
				)throws Exception {
		
		BoardUseInfVO bdUseVO = new BoardUseInfVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		bdUseVO.setBbsId(bbsId);
		bdUseVO.setTrgetId(trgetId);

		BoardUseInfVO vo = bbsUseService.selectBBSUseInf(bdUseVO);// bbsItrgetId

		// 시스템 사용 게시판의 경우 URL 표시
		if ("SYSTEM_DEFAULT_BOARD".equals(vo.getTrgetId())) {
			if (vo.getBbsTyCode().equals("BBST02")) { // 익명게시판
			} else {
				vo.setProvdUrl("bbsUseInf/" + trgetId + "/" + bbsId);//bbsId 값을 따로 넘겨줘야 함
			}
		}

		BoardMasterVO boardMasterVO = new BoardMasterVO();
		resultMap = bbsAttrbService.selectNotUsedBdMstrList(boardMasterVO);

		resultMap.put("bdUseVO", vo);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 게시판 사용정보를 등록한다.
	 *
     * @param request
     * @param bdUseVO
     * @param bindingResult
     * @return ResultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 사용정보 등록",
			description = " 게시판 사용정보를 등록",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping(value ="/bbsUseInf")
	public ResultVO insertBBSUseInf(HttpServletRequest request,
		BoardUseInfVO bdUseVO,
		BindingResult bindingResult,
		@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO
	) throws Exception {

		beanValidator.validate(bdUseVO, bindingResult);

		if (bindingResult.hasErrors()) {
			return resultVoHelper.buildFromResultVO(new ResultVO(), ResponseCode.INPUT_CHECK_ERROR);
		}

		if ("CMMNTY".equals(bdUseVO.getTrgetType())) {
			bdUseVO.setRegistSeCode("REGC06");
		} else if ("CLUB".equals(bdUseVO.getTrgetType())) {
			bdUseVO.setRegistSeCode("REGC05");
		} else {
			bdUseVO.setRegistSeCode("REGC01");
		}

		bdUseVO.setUseAt("Y");
		bdUseVO.setFrstRegisterId(loginVO.getUniqId());

		bbsUseService.insertBBSUseInf(bdUseVO);

		return resultVoHelper.buildFromMap(new HashMap<String, Object>(), ResponseCode.SUCCESS);
	}

	/**
	 * 게시판 사용정보를 수정한다.
	 *
	 * @param request
	 * @param bdUseVO
	 * @param bbsId
	 * @return ResultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 사용정보 수정",
			description = " 게시판 사용정보를 수정",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping(value ="/bbsUseInf/{bbsId}")
	public ResultVO updateBBSUseInf(HttpServletRequest request,
		@RequestBody BoardUseInfVO bdUseVO,
		@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")
		@PathVariable("bbsId") String bbsId,
		@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO
	) throws Exception {
		
		bdUseVO.setBbsId(bbsId);
		bbsUseService.updateBBSUseInf(bdUseVO);

		return resultVoHelper.buildFromMap(new HashMap<String, Object>(), ResponseCode.SUCCESS);
	}

}
