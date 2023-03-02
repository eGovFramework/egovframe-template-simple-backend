package egovframework.let.cop.com.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.jwt.config.JwtVerification;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.com.service.BoardUseInfVO;
import egovframework.let.cop.com.service.EgovBBSUseInfoManageService;

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
public class EgovBBSUseInfoManageApiController {
	
	/** JwtVerification */
	@Autowired
	private JwtVerification jwtVerification;

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
	@PostMapping(value ="/cop/com/selectBBSUseInfsAPI.do")
	public ResultVO selectBBSUseInfs(HttpServletRequest request,
		@RequestBody BoardUseInfVO bdUseVO) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 기존 세션 체크 인증에서 토큰 방식으로 변경
		if (!jwtVerification.isVerification(request)) {
			return handleAuthError(resultVO); // 토큰 확인
		}

		bdUseVO.setPageUnit(propertyService.getInt("pageUnit"));
		bdUseVO.setPageSize(propertyService.getInt("pageSize"));

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
	@PostMapping(value ="/cop/com/selectBBSUseInfAPI.do")
	public ResultVO selectBBSUseInf(HttpServletRequest request,
		@RequestBody BoardUseInfVO bdUseVO) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		BoardUseInfVO vo = bbsUseService.selectBBSUseInf(bdUseVO);// bbsItrgetId

		// 기존 세션 체크 인증에서 토큰 방식으로 변경
		if (!jwtVerification.isVerification(request)) {
			return handleAuthError(resultVO); // 토큰 확인
		}

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
	@PostMapping(value ="/cop/com/insertBBSUseInfAPI.do")
	public ResultVO insertBBSUseInf(HttpServletRequest request,
		BoardUseInfVO bdUseVO,
		BindingResult bindingResult

	) throws Exception {

		ResultVO resultVO = new ResultVO();

		// 기존 세션 체크 인증에서 토큰 방식으로 변경
		if (!jwtVerification.isVerification(request)) {
			return handleAuthError(resultVO); // 토큰 확인
		}

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

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
		bdUseVO.setFrstRegisterId(user.getUniqId());

		if (isAuthenticated) {
			bbsUseService.insertBBSUseInf(bdUseVO);

			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		}

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
	@PutMapping(value ="/cop/com/updateBBSUseInfAPI/{bbsId}.do")
	public ResultVO updateBBSUseInf(HttpServletRequest request,
		@RequestBody BoardUseInfVO bdUseVO,
		@PathVariable("bbsId") String bbsId) throws Exception {

		ResultVO resultVO = new ResultVO();

		// 기존 세션 체크 인증에서 토큰 방식으로 변경
		if (!jwtVerification.isVerification(request)) {
			return handleAuthError(resultVO); // 토큰 확인
		}

		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		if (isAuthenticated) {
			bdUseVO.setBbsId(bbsId);
			bbsUseService.updateBBSUseInf(bdUseVO);

			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		}

		return resultVO;
	}

	private ResultVO handleAuthError(ResultVO resultVO) {
		resultVO.setResultCode(ResponseCode.AUTH_ERROR.getCode());
		resultVO.setResultMessage(ResponseCode.AUTH_ERROR.getMessage());
		return resultVO;
	}

	/**
	 * 운영자 권한을 확인한다.(로그인 여부를 확인한다.)
	 *
	 * @throws EgovBizException
	 */
	protected boolean checkAuthority() throws Exception {
		// 사용자권한 처리
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			return false;
		} else {
			return true;
		}
	}

}
