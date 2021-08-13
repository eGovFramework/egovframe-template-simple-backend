package egovframework.let.cop.com.web;
import java.util.List;
import java.util.Map;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.com.service.EgovTemplateManageService;
import egovframework.let.cop.com.service.TemplateInf;
import egovframework.let.cop.com.service.TemplateInfVO;

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
 * 템플릿 관리를 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.03.18
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.18  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Controller
public class EgovTemplateManageController {

    @Resource(name = "EgovTemplateManageService")
    private EgovTemplateManageService tmplatService;

    @Resource(name = "EgovCmmUseService")
    private EgovCmmUseService cmmUseService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Autowired
    private DefaultBeanValidator beanValidator;

    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

    /**
     * 템플릿 목록을 조회한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/com/selectTemplateInfs.do")
    public String selectTemplateInfs(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

	    tmplatInfVO.setPageUnit(propertyService.getInt("pageUnit"));
		tmplatInfVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(tmplatInfVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(tmplatInfVO.getPageUnit());
		paginationInfo.setPageSize(tmplatInfVO.getPageSize());

		tmplatInfVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		tmplatInfVO.setLastIndex(paginationInfo.getLastRecordIndex());
		tmplatInfVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = tmplatService.selectTemplateInfs(tmplatInfVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);

		return "cop/com/EgovTemplateList";
    }

    /**
     * 템플릿에 대한 상세정보를 조회한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/com/selectTemplateInf.do")
    public String selectTemplateInf(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		ComDefaultCodeVO codeVO = new ComDefaultCodeVO();

		codeVO.setCodeId("COM005");
		List<?> result = cmmUseService.selectCmmCodeDetail(codeVO);

		TemplateInfVO vo = tmplatService.selectTemplateInf(tmplatInfVO);

		model.addAttribute("TemplateInfVO", vo);
		model.addAttribute("resultList", result);

		return "cop/com/EgovTemplateUpdt";
    }

    /**
     * 템플릿 정보를 등록한다.
     *
     * @param searchVO
     * @param tmplatInfo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/com/insertTemplateInf.do")
    public String insertTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, @ModelAttribute("templateInf") TemplateInf templateInf,
	    BindingResult bindingResult, SessionStatus status, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		beanValidator.validate(templateInf, bindingResult);

		if (bindingResult.hasErrors()) {
		    ComDefaultCodeVO vo = new ComDefaultCodeVO();

		    vo.setCodeId("COM005");

		    List<?> result = cmmUseService.selectCmmCodeDetail(vo);

		    model.addAttribute("resultList", result);

		    return "cop/com/EgovTemplateRegist";
		}

		templateInf.setFrstRegisterId(user.getUniqId());

		if (isAuthenticated) {
		    tmplatService.insertTemplateInf(templateInf);
		}

		return "forward:/cop/com/selectTemplateInfs.do";
    }

    /**
     * 템플릿 등록을 위한 등록페이지로 이동한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/com/addTemplateInf.do")
    public String addTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	ComDefaultCodeVO vo = new ComDefaultCodeVO();

		vo.setCodeId("COM005");

		List<?> result = cmmUseService.selectCmmCodeDetail(vo);

		model.addAttribute("resultList", result);

		return "cop/com/EgovTemplateRegist";
    }

    /**
     * 템플릿 정보를 수정한다.
     *
     * @param searchVO
     * @param tmplatInfo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/com/updateTemplateInf.do")
    public String updateTemplateInf(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, @ModelAttribute("templateInf") TemplateInf templateInf,
	    BindingResult bindingResult, SessionStatus status, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		beanValidator.validate(templateInf, bindingResult);

		if (bindingResult.hasErrors()) {
		    ComDefaultCodeVO codeVO = new ComDefaultCodeVO();

		    codeVO.setCodeId("COM005");

		    List<?> result = cmmUseService.selectCmmCodeDetail(codeVO);

		    TemplateInfVO vo = tmplatService.selectTemplateInf(tmplatInfVO);

		    model.addAttribute("TemplateInfVO", vo);
		    model.addAttribute("resultList", result);

		    return "cop/com/EgovTemplateUpdt";
		}

		templateInf.setLastUpdusrId(user.getUniqId());

		if (isAuthenticated) {
		    tmplatService.updateTemplateInf(templateInf);
		}

		return "forward:/cop/com/selectTemplateInfs.do";
    }

    /**
     * 템플릿 정보를 삭제한다.
     *
     * @param searchVO
     * @param tmplatInfo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/deleteTemplateInf.do")
    public String deleteTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, @ModelAttribute("tmplatInf") TemplateInf tmplatInf,
	    SessionStatus status, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		tmplatInf.setLastUpdusrId(user.getUniqId());

		if (isAuthenticated) {
		    tmplatService.deleteTemplateInf(tmplatInf);
		}

		return "forward:/cop/com/selectTemplateInfs.do";
    }

    /**
     * 팝업을 위한 템플릿 목록을 조회한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/com/selectTemplateInfsPop.do")
    public String selectTemplateInfsPop(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO,
    		@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

		String typeFlag = (String)commandMap.get("typeFlag");

		if ("CLB".equals(typeFlag)) {
		    tmplatInfVO.setTypeFlag(typeFlag);
		    tmplatInfVO.setTmplatSeCode("TMPT03");
		} else if ("CMY".equals(typeFlag)) {
		    tmplatInfVO.setTypeFlag(typeFlag);
		    tmplatInfVO.setTmplatSeCode("TMPT02");
		} else {
		    tmplatInfVO.setTypeFlag(typeFlag);
		    tmplatInfVO.setTmplatSeCode("TMPT01");
		}

		tmplatInfVO.setPageUnit(propertyService.getInt("pageUnit"));
		tmplatInfVO.setPageSize(propertyService.getInt("pageSize"));
		//CMY, CLB

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(tmplatInfVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(tmplatInfVO.getPageUnit());
		paginationInfo.setPageSize(tmplatInfVO.getPageSize());

		tmplatInfVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		tmplatInfVO.setLastIndex(paginationInfo.getLastRecordIndex());
		tmplatInfVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());


		Map<String, Object> map = tmplatService.selectTemplateInfs(tmplatInfVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);

		return "cop/com/EgovTemplateInqirePopup";
    }

    /**
     * 팝업 페이지를 호출한다.
     *
     * @param userVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/com/openPopup.do")
    public String openPopupWindow(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

    	if (!checkAuthority(model)) return "cmm/uat/uia/EgovLoginUsr";	// server-side 권한 확인

    	String requestUrl = (String)commandMap.get("requestUrl");
    	String trgetId = (String)commandMap.get("trgetId");
    	String width = (String)commandMap.get("width");
    	String height = (String)commandMap.get("height");
    	String typeFlag = (String)commandMap.get("typeFlag");

    	if (trgetId != null && trgetId != "") {
    	    if (typeFlag != null && typeFlag != "") {
    		model.addAttribute("requestUrl", requestUrl + "?trgetId=" + trgetId + "&PopFlag=Y&typeFlag=" + typeFlag);
    	    } else {
    		model.addAttribute("requestUrl", requestUrl + "?trgetId=" + trgetId + "&PopFlag=Y");
    	    }
    	} else {
    	    if (typeFlag != null && typeFlag != "") {
    		model.addAttribute("requestUrl", requestUrl + "?PopFlag=Y&typeFlag=" + typeFlag);
    	    } else {
    		model.addAttribute("requestUrl", requestUrl + "?PopFlag=Y");
    	    }

    	}

    	model.addAttribute("width", width);
    	model.addAttribute("height", height);

    	return "/cop/com/EgovModalPopupFrame";
    }

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
}
