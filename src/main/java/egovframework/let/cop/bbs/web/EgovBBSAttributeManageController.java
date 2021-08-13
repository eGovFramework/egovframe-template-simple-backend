package egovframework.let.cop.bbs.web;
import java.util.List;
import java.util.Map;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.bbs.service.BoardMaster;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.utl.fcc.service.EgovStringUtil;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;

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
@Controller
public class EgovBBSAttributeManageController {

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
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

    /**
     * 운영자 권한을 확인한다.(로그인 여부를 확인한다.)
     *
     * @param boardMaster
     * @throws EgovBizException
     */
    protected boolean checkAuthority(ModelMap model) throws Exception {
    	// 사용자권한 처리
    	if(!EgovUserDetailsHelper.isAuthenticated()) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return false;
    	}else{
    		return true;
    	}
    }

	/**
     * 신규 게시판 마스터 등록을 위한 등록페이지로 이동한다.
     *
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/addBBSMaster.do")
    public String addBBSMaster(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	BoardMaster boardMaster = new BoardMaster();

	    ComDefaultCodeVO vo = new ComDefaultCodeVO();

		vo.setCodeId("COM004");

		List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);

		model.addAttribute("typeList", codeResult);

		vo.setCodeId("COM009");

		codeResult = cmmUseService.selectCmmCodeDetail(vo);

		model.addAttribute("attrbList", codeResult);
		model.addAttribute("boardMaster", boardMaster);

		return "cop/bbs/EgovBoardMstrRegist";
    }

    /**
     * 신규 게시판 마스터 정보를 등록한다.
     *
     * @param boardMasterVO
     * @param boardMaster
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/insertBBSMasterInf.do")
    public String insertBBSMasterInf(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster,
	    BindingResult bindingResult, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		beanValidator.validate(boardMaster, bindingResult);
		if (bindingResult.hasErrors()) {

		    ComDefaultCodeVO vo = new ComDefaultCodeVO();

		    vo.setCodeId("COM004");

		    List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);

		    model.addAttribute("typeList", codeResult);

		    vo.setCodeId("COM009");

		    codeResult = cmmUseService.selectCmmCodeDetail(vo);

		    model.addAttribute("attrbList", codeResult);

		    return "cop/bbs/EgovBoardMstrRegist";
		}

		if (isAuthenticated) {
		    boardMaster.setFrstRegisterId(user.getUniqId());
		    boardMaster.setUseAt("Y");
		    boardMaster.setTrgetId("SYSTEMDEFAULT_REGIST");
		    boardMaster.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));

		    bbsAttrbService.insertBBSMastetInf(boardMaster);
		}

		return "forward:/cop/bbs/SelectBBSMasterInfs.do";
    }

    /**
     * 게시판 마스터 목록을 조회한다.
     *
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/SelectBBSMasterInfs.do")
    public String selectBBSMasterInfs(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	boardMasterVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
		paginationInfo.setPageSize(boardMasterVO.getPageSize());

		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = bbsAttrbService.selectBBSMasterInfs(boardMasterVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);

		return "cop/bbs/EgovBoardMstrList";
    }

    /**
     * 게시판 마스터 상세내용을 조회한다.
     *
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/SelectBBSMasterInf.do")
    public String selectBBSMasterInf(@ModelAttribute("searchVO") BoardMasterVO searchVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(searchVO);
		model.addAttribute("result", vo);

		return "cop/bbs/EgovBoardMstrUpdt";
    }

    /**
     * 게시판 마스터 정보를 수정한다.
     *
     * @param boardMasterVO
     * @param boardMaster
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/UpdateBBSMasterInf.do")
    public String updateBBSMasterInf(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster,
	    BindingResult bindingResult, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		beanValidator.validate(boardMaster, bindingResult);
		if (bindingResult.hasErrors()) {
		    BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(boardMasterVO);

		    model.addAttribute("result", vo);

		    return "cop/bbs/EgovBoardMstrUpdt";
		}

		if (isAuthenticated) {
		    boardMaster.setLastUpdusrId(user.getUniqId());
		    boardMaster.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));
		    bbsAttrbService.updateBBSMasterInf(boardMaster);
		}

		return "forward:/cop/bbs/SelectBBSMasterInfs.do";
    }

    /**
     * 게시판 마스터 정보를 삭제한다.
     *
     * @param boardMasterVO
     * @param boardMaster
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/DeleteBBSMasterInf.do")
    public String deleteBBSMasterInf(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster,
	    ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		if (isAuthenticated) {
		    boardMaster.setLastUpdusrId(user.getUniqId());
		    bbsAttrbService.deleteBBSMasterInf(boardMaster);
		}

		return "forward:/cop/bbs/SelectBBSMasterInfs.do";
    }

    /**
     * 게시판 마스터 선택 팝업을 위한 목록을 조회한다.
     *
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/SelectBBSMasterInfsPop.do")
    public String selectBBSMasterInfsPop(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	boardMasterVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
		paginationInfo.setPageSize(boardMasterVO.getPageSize());

		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		boardMasterVO.setUseAt("Y");

		Map<String, Object> map = bbsAttrbService.selectNotUsedBdMstrList(boardMasterVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);

		return "cop/bbs/EgovBoardMstrListPop";
    }

    /**
     * 게시판 사용을 위한 신규 게시판 속성정보를 생성한다.
     *
     * @param boardMasterVO
     * @param boardMaster
     * @param bindingResult
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/insertBdMstrByTrget.do")
    public String insertBdMstrByTrget(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO,
	    @ModelAttribute("boardMaster") BoardMaster boardMaster, BindingResult bindingResult, SessionStatus status, ModelMap model)
	    throws Exception {

		if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		beanValidator.validate(boardMaster, bindingResult);
		if (bindingResult.hasErrors()) {

		    ComDefaultCodeVO vo = new ComDefaultCodeVO();
		    vo.setCodeId("COM004");
		    List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);
		    model.addAttribute("typeList", codeResult);
		    vo.setCodeId("COM009");
		    codeResult = cmmUseService.selectCmmCodeDetail(vo);
		    model.addAttribute("attrbList", codeResult);

		    return "cop/bbs/EgovBdMstrRegistByTrget";
		}

		boardMaster.setFrstRegisterId(user.getUniqId());
		boardMaster.setUseAt("Y");
		boardMaster.setBbsUseFlag("Y");

		String registSeCode = "REGC06";

		if ("CLB".equals(EgovStringUtil.cutString(boardMaster.getTrgetId(), 3))) {
		    registSeCode = "REGC05";
		}
		boardMaster.setRegistSeCode(registSeCode);

		if (isAuthenticated) {
		    bbsAttrbService.insertBBSMastetInf(boardMaster);
		    model.addAttribute("S_FLAG", "S");
		}

		return "forward:/cop/bbs/selectBdMstrListByTrget.do";
    }

    /**
     * 사용중인 게시판 속성 정보의 목록을 조회 한다.
     *
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectBdMstrListByTrget.do")
    public String selectBdMstrListByTrget(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		boardMasterVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
		paginationInfo.setPageSize(boardMasterVO.getPageSize());

		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = bbsAttrbService.selectBdMstrListByTrget(boardMasterVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));
		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("trgetId", boardMasterVO.getTrgetId());

		return "cop/bbs/EgovBBSListByTrget";
    }

    /**
     * 게시판 사용을 위한 게시판 속성정보 한 건을 상세조회한다.
     *
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/SelectBBSMasterInfByTrget.do")
    public String selectBBSMasterInfByTrget(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO,
    		@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(boardMasterVO);
		vo.setTrgetId(boardMasterVO.getTrgetId());
		model.addAttribute("result", vo);

		return "cop/bbs/EgovBdMstrUpdtByTrget";
    }

    /**
     * 게시판 사용을 위한 게시판 속성정보를 수정한다.
     *
     * @param boardMasterVO
     * @param boardMaster
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/UpdateBBSMasterInfByTrget.do")
    public String updateBBSMasterInfByTrget(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO,
	    @ModelAttribute("boardMaster") BoardMaster boardMaster, BindingResult bindingResult, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		beanValidator.validate(boardMaster, bindingResult);
		if (bindingResult.hasErrors()) {
		    BoardMasterVO vo = new BoardMasterVO();
		    vo = bbsAttrbService.selectBBSMasterInf(boardMasterVO);
		    model.addAttribute("result", vo);

		    return "cop/bbs/EgovBoardMstrUpdt";
		}

		boardMaster.setLastUpdusrId(user.getUniqId());
		boardMaster.setUseAt("Y");

		if (isAuthenticated) {
		    bbsAttrbService.updateBBSMasterInf(boardMaster);
		}

		return "forward:/cop/bbs/selectBdMstrListByTrget.do";
    }

    /**
     * 커뮤니티, 동호회에서 사용을 위한 게시판 마스터 등록 화면으로 이동한다.
     *
     * @param boardMasterVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/addBBSMasterByTrget.do")
    public String addBBSMasterByTrget(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		vo.setCodeId("COM004");
		List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("typeList", codeResult);

		vo.setCodeId("COM009");
		codeResult = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("attrbList", codeResult);

		BoardMaster boardMaster = new BoardMaster();
		model.addAttribute("boardMaster", boardMaster);

		return "cop/bbs/EgovBdMstrRegistByTrget";
    }

    /**
     * 등록된 게시판 속성정보를 삭제한다.
     *
     * @param boardMasterVO
     * @param boardMaster
     * @param sessionVO
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/DeleteBBSMasterInfByTrget.do")
    public String deleteBBSMasterInfByTrget(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO,
	    @ModelAttribute("boardMaster") BoardMaster boardMaster, SessionStatus status, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		boardMaster.setLastUpdusrId(user.getUniqId());

		if (isAuthenticated) {
		    bbsAttrbService.deleteBBSMasterInf(boardMaster);
		}

		return "forward:/cop/bbs/selectBdMstrListByTrget.do";
    }

    /**
     * 커뮤니티, 동호회에서 사용중인 게시판 속성 정보의 목록 조회한다.
     *
     * @param commandMap
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectAllBdMstrByTrget.do")
    public String selectAllBdMstrByTrget(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	String trgetId = (String)commandMap.get("param_trgetId");
		BoardMasterVO vo = new BoardMasterVO();

		vo.setTrgetId(trgetId);
		List<BoardMasterVO> result = bbsAttrbService.selectAllBdMstrByTrget(vo);
		model.addAttribute("resultList", result);

		return "cop/bbs/EgovBdListPortlet";
    }
}
