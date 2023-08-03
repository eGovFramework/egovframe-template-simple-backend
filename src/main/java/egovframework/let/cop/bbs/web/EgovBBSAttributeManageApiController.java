package egovframework.let.cop.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.12  이삼섭          최초 생성
 *  2009.06.26	한성곤		2단계 기능 추가 (댓글관리, 만족도조사)
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@RestController
@Tag(name="EgovBBSAttributeManageApiController",description = "게시판 속성관리")
public class EgovBBSAttributeManageApiController {


	/** EgovBBSAttributeManageService */
	@Resource(name = "EgovBBSAttributeManageService")
	private EgovBBSAttributeManageService bbsAttrbService;

	/** EgovCmmUseService */
	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;

	/** DefaultBeanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

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
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@PostMapping(value ="/cop/bbs/selectBBSMasterInfsAPI.do")
	public ResultVO selectBBSMasterInfs(HttpServletRequest request,
		@RequestBody BoardMasterVO boardMasterVO)
		throws Exception {

		ResultVO resultVO = new ResultVO();

		boardMasterVO.setPageUnit(propertyService.getInt("Globals.pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("Globals.pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

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

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
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
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@PostMapping(value ="/cop/bbs/selectBBSMasterInfAPI.do")
	public ResultVO selectBBSMasterInf(HttpServletRequest request,
		@RequestBody BoardMasterVO searchVO)
		throws Exception {
		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(searchVO);
		resultMap.put("boardMasterVO", vo);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		// return "cop/bbs/EgovBoardMstrUpdt";
		return resultVO;
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
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping(value ="/cop/bbs/insertBBSMasterInfAPI.do")
	public ResultVO insertBBSMasterInf(HttpServletRequest request,
									   BoardMasterVO boardMasterVO,
									   BindingResult bindingResult,
									   @AuthenticationPrincipal LoginVO loginVO
	)
		throws Exception {
		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		beanValidator.validate(boardMasterVO, bindingResult);
		if (bindingResult.hasErrors()) {

			ComDefaultCodeVO vo = new ComDefaultCodeVO();

			vo.setCodeId("COM004");

			List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);

			resultMap.put("typeList", codeResult);

			vo.setCodeId("COM009");

			codeResult = cmmUseService.selectCmmCodeDetail(vo);

			resultMap.put("attrbList", codeResult);

			resultVO.setResult(resultMap);
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());
			
			return resultVO;
		}

		boardMasterVO.setFrstRegisterId(loginVO.getUniqId());
		boardMasterVO.setUseAt("Y");
		boardMasterVO.setTrgetId("SYSTEMDEFAULT_REGIST");
		boardMasterVO.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));

		bbsAttrbService.insertBBSMastetInf(boardMasterVO);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
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
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping(value ="/cop/bbs/updateBBSMasterInfAPI/{bbsId}.do")
	public ResultVO updateBBSMasterInf(HttpServletRequest request,
									   @PathVariable("bbsId") String bbsId,
									   @RequestBody BoardMasterVO boardMasterVO,
									   BindingResult bindingResult,
									   @AuthenticationPrincipal LoginVO loginVO
									   ) throws Exception {
		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		beanValidator.validate(boardMasterVO, bindingResult);

		if (bindingResult.hasErrors()) {
			BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(boardMasterVO);

			resultMap.put("BoardMasterVO", vo);

			resultVO.setResult(resultMap);
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());
			return resultVO;
		}

		boardMasterVO.setLastUpdusrId(loginVO.getUniqId());
		boardMasterVO.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));
		bbsAttrbService.updateBBSMasterInf(boardMasterVO);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
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
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "삭제 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@PutMapping(value ="/cop/bbs/deleteBBSMasterInfAPI/{bbsId}.do")
	public ResultVO deleteBBSMasterInf(HttpServletRequest request,
	    @AuthenticationPrincipal LoginVO loginVO,
		@PathVariable("bbsId") String bbsId,
		@RequestBody BoardMasterVO boardMasterVO) throws Exception {
		ResultVO resultVO = new ResultVO();

			boardMasterVO.setLastUpdusrId(loginVO.getUniqId());
			bbsAttrbService.deleteBBSMasterInf(boardMasterVO);

			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}



}
