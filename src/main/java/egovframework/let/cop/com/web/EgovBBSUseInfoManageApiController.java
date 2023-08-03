package egovframework.let.cop.com.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.com.service.BoardUseInfVO;
import egovframework.let.cop.com.service.EgovBBSUseInfoManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name="EgovBBSUseInfoManageApiController",description = "게시판 이용정보 관리")
public class EgovBBSUseInfoManageApiController {
	

	/** EgovBBSUseInfoManageService */
	@Resource(name = "EgovBBSUseInfoManageService")
	private EgovBBSUseInfoManageService bbsUseService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;

	/** EgovBBSAttributeManageService */
	@Resource(name = "EgovBBSAttributeManageService")
	private EgovBBSAttributeManageService bbsAttrbService;

	/** DefaultBeanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

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
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@PostMapping(value ="/cop/com/selectBBSUseInfsAPI.do")
	public ResultVO selectBBSUseInfs(HttpServletRequest request,
		@RequestBody BoardUseInfVO bdUseVO) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();


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

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 생성된 마스터 게시판을 조회한다.
	 * @param boardMasterVO
	 * @return
	 * @throws Exception
	 */
	@Operation(
			summary = "미사용 게시판 속성정보 목록 조회",
			description = "사용중이지 않은 게시판 속성 정보의 목록을 조회",
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@PostMapping(value ="/cop/com/selectNotUsedBdMstrList.do")
	public ResultVO selectNotUsedBdMstrList(
		BoardMasterVO boardMasterVO) throws Exception {
		ResultVO resultVO = new ResultVO();

		boardMasterVO.setFirstIndex(0);
		Map<String, Object> resultMap = bbsAttrbService.selectNotUsedBdMstrList(boardMasterVO);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
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
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@PostMapping(value ="/cop/com/selectBBSUseInfAPI.do")
	public ResultVO selectBBSUseInf(HttpServletRequest request,
		@RequestBody BoardUseInfVO bdUseVO) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		BoardUseInfVO vo = bbsUseService.selectBBSUseInf(bdUseVO);// bbsItrgetId


		// 시스템 사용 게시판의 경우 URL 표시
		if ("SYSTEM_DEFAULT_BOARD".equals(vo.getTrgetId())) {
			if (vo.getBbsTyCode().equals("BBST02")) { // 익명게시판
			} else {
				vo.setProvdUrl("/cop/bbs/selectBoardListAPI.do");//bbsId 값을 따로 넘겨줘야 함
			}
		}

		BoardMasterVO boardMasterVO = new BoardMasterVO();
		resultMap = bbsAttrbService.selectNotUsedBdMstrList(boardMasterVO);

		resultMap.put("bdUseVO", vo);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
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
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping(value ="/cop/com/insertBBSUseInfAPI.do")
	public ResultVO insertBBSUseInf(HttpServletRequest request,
		BoardUseInfVO bdUseVO,
		BindingResult bindingResult,
		@AuthenticationPrincipal LoginVO loginVO
	) throws Exception {

		ResultVO resultVO = new ResultVO();

		beanValidator.validate(bdUseVO, bindingResult);

		if (bindingResult.hasErrors()) {
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());
			return resultVO;
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

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
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
			tags = {"EgovBBSUseInfoManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping(value ="/cop/com/updateBBSUseInfAPI/{bbsId}.do")
	public ResultVO updateBBSUseInf(HttpServletRequest request,
		@RequestBody BoardUseInfVO bdUseVO,
		@PathVariable("bbsId") String bbsId,
		@AuthenticationPrincipal LoginVO loginVO
	) throws Exception {

		ResultVO resultVO = new ResultVO();
		bdUseVO.setBbsId(bbsId);
		bbsUseService.updateBBSUseInf(bdUseVO);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

}
