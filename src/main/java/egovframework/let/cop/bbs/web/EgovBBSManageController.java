package egovframework.let.cop.bbs.web;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.bbs.service.Board;
import egovframework.let.cop.bbs.service.BoardMaster;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * 게시물 관리를 위한 컨트롤러 클래스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.19  이삼섭          최초 생성
 *  2009.06.29  한성곤	       2단계 기능 추가 (댓글관리, 만족도조사)
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@Controller
public class EgovBBSManageController {

    @Resource(name = "EgovBBSManageService")
    private EgovBBSManageService bbsMngService;

    @Resource(name = "EgovBBSAttributeManageService")
    private EgovBBSAttributeManageService bbsAttrbService;

    @Resource(name = "EgovFileMngService")
    private EgovFileMngService fileMngService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

    //---------------------------------
    // 2009.06.29 : 2단계 기능 추가
    //---------------------------------
    //SHT-CUSTOMIZING//@Resource(name = "EgovBBSCommentService")
    //SHT-CUSTOMIZING//private EgovBBSCommentService bbsCommentService;

    //SHT-CUSTOMIZING//@Resource(name = "EgovBBSSatisfactionService")
    //SHT-CUSTOMIZING//private EgovBBSSatisfactionService bbsSatisfactionService;

    //SHT-CUSTOMIZING//@Resource(name = "EgovBBSScrapService")
    //SHT-CUSTOMIZING//private EgovBBSScrapService bbsScrapService;
    ////-------------------------------

    @Autowired
    private DefaultBeanValidator beanValidator;

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

    /**
     * 게시물에 대한 목록을 조회한다.
     *
     * @param boardVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectBoardList.do")
    public String selectBoardArticles(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

	boardVO.setBbsId(boardVO.getBbsId());
	boardVO.setBbsNm(boardVO.getBbsNm());

	BoardMasterVO vo = new BoardMasterVO();

	vo.setBbsId(boardVO.getBbsId());
	vo.setUniqId(user.getUniqId());

	BoardMasterVO master = bbsAttrbService.selectBBSMasterInf(vo);

	//-------------------------------
	// 방명록이면 방명록 URL로 forward
	//-------------------------------
	if (master.getBbsTyCode().equals("BBST04")) {
	    return "forward:/cop/bbs/selectGuestList.do";
	}
	////-----------------------------

	boardVO.setPageUnit(propertyService.getInt("pageUnit"));
	boardVO.setPageSize(propertyService.getInt("pageSize"));

	PaginationInfo paginationInfo = new PaginationInfo();

	paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
	paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
	paginationInfo.setPageSize(boardVO.getPageSize());

	boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
	boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

	Map<String, Object> map = bbsMngService.selectBoardArticles(boardVO, vo.getBbsAttrbCode());
	int totCnt = Integer.parseInt((String)map.get("resultCnt"));

	paginationInfo.setTotalRecordCount(totCnt);

	//-------------------------------
	// 기본 BBS template 지정
	//-------------------------------
	if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
	    master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
	}
	////-----------------------------

	model.addAttribute("resultList", map.get("resultList"));
	model.addAttribute("resultCnt", map.get("resultCnt"));
	model.addAttribute("boardVO", boardVO);
	model.addAttribute("brdMstrVO", master);
	model.addAttribute("paginationInfo", paginationInfo);

	return "cop/bbs/EgovNoticeList";
    }

    /**
     * 게시물에 대한 상세 정보를 조회한다.
     *
     * @param boardVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectBoardArticle.do")
    public String selectBoardArticle(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
    	LoginVO user = new LoginVO();
	    if(EgovUserDetailsHelper.isAuthenticated()){
	    	user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		}

		// 조회수 증가 여부 지정
		boardVO.setPlusCount(true);

		//---------------------------------
		// 2009.06.29 : 2단계 기능 추가
		//---------------------------------
		if (!boardVO.getSubPageIndex().equals("")) {
		    boardVO.setPlusCount(false);
		}
		////-------------------------------

		boardVO.setLastUpdusrId(user.getUniqId());
		BoardVO vo = bbsMngService.selectBoardArticle(boardVO);

		model.addAttribute("result", vo);

		model.addAttribute("sessionUniqId", user.getUniqId());

		//----------------------------
		// template 처리 (기본 BBS template 지정  포함)
		//----------------------------
		BoardMasterVO master = new BoardMasterVO();

		master.setBbsId(boardVO.getBbsId());
		master.setUniqId(user.getUniqId());

		BoardMasterVO masterVo = bbsAttrbService.selectBBSMasterInf(master);

		if (masterVo.getTmplatCours() == null || masterVo.getTmplatCours().equals("")) {
		    masterVo.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}

		model.addAttribute("brdMstrVO", masterVo);

		return "cop/bbs/EgovNoticeInqire";
    }

    /**
     * 게시물 등록을 위한 등록페이지로 이동한다.
     *
     * @param boardVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/addBoardArticle.do")
    public String addBoardArticle(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
	// 사용자권한 처리
	if(!EgovUserDetailsHelper.isAuthenticated()) {
		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
    	return "cmm/uat/uia/EgovLoginUsr";
	}

    LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

	BoardMasterVO bdMstr = new BoardMasterVO();

	if (isAuthenticated) {

	    BoardMasterVO vo = new BoardMasterVO();
	    vo.setBbsId(boardVO.getBbsId());
	    vo.setUniqId(user.getUniqId());

	    bdMstr = bbsAttrbService.selectBBSMasterInf(vo);
	    model.addAttribute("bdMstr", bdMstr);
	}

	//----------------------------
	// 기본 BBS template 지정
	//----------------------------
	if (bdMstr.getTmplatCours() == null || bdMstr.getTmplatCours().equals("")) {
	    bdMstr.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
	}

	model.addAttribute("brdMstrVO", bdMstr);
	////-----------------------------

	return "cop/bbs/EgovNoticeRegist";
    }

    /**
     * 게시물을 등록한다.
     *
     * @param boardVO
     * @param board
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/insertBoardArticle.do")
    public String insertBoardArticle(final MultipartHttpServletRequest multiRequest, @ModelAttribute("searchVO") BoardVO boardVO,
	    @ModelAttribute("bdMstr") BoardMaster bdMstr, @ModelAttribute("board") Board board, BindingResult bindingResult, SessionStatus status,
	    ModelMap model) throws Exception {
	// 사용자권한 처리
	if(!EgovUserDetailsHelper.isAuthenticated()) {
		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
    	return "cmm/uat/uia/EgovLoginUsr";
	}

	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

	beanValidator.validate(board, bindingResult);
	if (bindingResult.hasErrors()) {

	    BoardMasterVO master = new BoardMasterVO();
	    BoardMasterVO vo = new BoardMasterVO();

	    vo.setBbsId(boardVO.getBbsId());
	    vo.setUniqId(user.getUniqId());

	    master = bbsAttrbService.selectBBSMasterInf(vo);

	    model.addAttribute("bdMstr", master);

	    //----------------------------
	    // 기본 BBS template 지정
	    //----------------------------
	    if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
		master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
	    }

	    model.addAttribute("brdMstrVO", master);
	    ////-----------------------------

	    return "cop/bbs/EgovNoticeRegist";
	}

	if (isAuthenticated) {
	    List<FileVO> result = null;
	    String atchFileId = "";

	    final Map<String, MultipartFile> files = multiRequest.getFileMap();
	    if (!files.isEmpty()) {
		result = fileUtil.parseFileInf(files, "BBS_", 0, "", "");
		atchFileId = fileMngService.insertFileInfs(result);
	    }
	    board.setAtchFileId(atchFileId);
	    board.setFrstRegisterId(user.getUniqId());
	    board.setBbsId(board.getBbsId());

	    board.setNtcrNm("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
	    board.setPassword("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
	    //board.setNttCn(unscript(board.getNttCn()));	// XSS 방지

	    bbsMngService.insertBoardArticle(board);
	}

	//status.setComplete();
	return "forward:/cop/bbs/selectBoardList.do";
    }

    /**
     * 게시물에 대한 답변 등록을 위한 등록페이지로 이동한다.
     *
     * @param boardVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/addReplyBoardArticle.do")
    public String addReplyBoardArticle(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
    	// 사용자권한 처리
    	if(!EgovUserDetailsHelper.isAuthenticated()) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return "cmm/uat/uia/EgovLoginUsr";
    	}

    LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

	BoardMasterVO master = new BoardMasterVO();
	BoardMasterVO vo = new BoardMasterVO();

	vo.setBbsId(boardVO.getBbsId());
	vo.setUniqId(user.getUniqId());

	master = bbsAttrbService.selectBBSMasterInf(vo);

	model.addAttribute("bdMstr", master);
	model.addAttribute("result", boardVO);

	//----------------------------
	// 기본 BBS template 지정
	//----------------------------
	if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
	    master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
	}

	model.addAttribute("brdMstrVO", master);
	////-----------------------------

	return "cop/bbs/EgovNoticeReply";
    }

    /**
     * 게시물에 대한 답변을 등록한다.
     *
     * @param boardVO
     * @param board
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/replyBoardArticle.do")
    public String replyBoardArticle(final MultipartHttpServletRequest multiRequest, @ModelAttribute("searchVO") BoardVO boardVO,
	    @ModelAttribute("bdMstr") BoardMaster bdMstr, @ModelAttribute("board") Board board, BindingResult bindingResult, ModelMap model,
	    SessionStatus status) throws Exception {

    	// 사용자권한 처리
    	if(!EgovUserDetailsHelper.isAuthenticated()) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return "cmm/uat/uia/EgovLoginUsr";
    	}

	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

	beanValidator.validate(board, bindingResult);
	if (bindingResult.hasErrors()) {
	    BoardMasterVO master = new BoardMasterVO();
	    BoardMasterVO vo = new BoardMasterVO();

	    vo.setBbsId(boardVO.getBbsId());
	    vo.setUniqId(user.getUniqId());

	    master = bbsAttrbService.selectBBSMasterInf(vo);

	    model.addAttribute("bdMstr", master);
	    model.addAttribute("result", boardVO);

	    //----------------------------
	    // 기본 BBS template 지정
	    //----------------------------
	    if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
		master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
	    }

	    model.addAttribute("brdMstrVO", master);
	    ////-----------------------------

	    return "cop/bbs/EgovNoticeReply";
	}

	if (isAuthenticated) {
	    final Map<String, MultipartFile> files = multiRequest.getFileMap();
	    String atchFileId = "";

	    if (!files.isEmpty()) {
		List<FileVO> result = fileUtil.parseFileInf(files, "BBS_", 0, "", "");
		atchFileId = fileMngService.insertFileInfs(result);
	    }

	    board.setAtchFileId(atchFileId);
	    board.setReplyAt("Y");
	    board.setFrstRegisterId(user.getUniqId());
	    board.setBbsId(board.getBbsId());
	    board.setParnts(Long.toString(boardVO.getNttId()));
	    board.setSortOrdr(boardVO.getSortOrdr());
	    board.setReplyLc(Integer.toString(Integer.parseInt(boardVO.getReplyLc()) + 1));

	    board.setNtcrNm("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
	    board.setPassword("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)

	    board.setNttCn(unscript(board.getNttCn()));	// XSS 방지

	    bbsMngService.insertBoardArticle(board);
	}

	return "forward:/cop/bbs/selectBoardList.do";
    }

    /**
     * 게시물 수정을 위한 수정페이지로 이동한다.
     *
     * @param boardVO
     * @param vo
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/forUpdateBoardArticle.do")
    public String selectBoardArticleForUpdt(@ModelAttribute("searchVO") BoardVO boardVO, @ModelAttribute("board") BoardVO vo, ModelMap model)
	    throws Exception {

	// 사용자권한 처리 (자유게시판에 대한 요청이 아닌 경우는 로긴화면으로 이동)
	if(!boardVO.getBbsId().equals("BBSMSTR_BBBBBBBBBBBB") && !EgovUserDetailsHelper.isAuthenticated()) {
		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
    	return "cmm/uat/uia/EgovLoginUsr";
	}

	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

	boardVO.setFrstRegisterId(user.getUniqId());

	BoardMaster master = new BoardMaster();
	BoardMasterVO bmvo = new BoardMasterVO();
	BoardVO bdvo = new BoardVO();

	vo.setBbsId(boardVO.getBbsId());

	master.setBbsId(boardVO.getBbsId());
	master.setUniqId(user.getUniqId());

	if (isAuthenticated) {
	    bmvo = bbsAttrbService.selectBBSMasterInf(master);
	    bdvo = bbsMngService.selectBoardArticle(boardVO);
	}

	model.addAttribute("result", bdvo);
	model.addAttribute("bdMstr", bmvo);

	//----------------------------
	// 기본 BBS template 지정
	//----------------------------
	if (bmvo.getTmplatCours() == null || bmvo.getTmplatCours().equals("")) {
	    bmvo.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
	}

	model.addAttribute("brdMstrVO", bmvo);
	////-----------------------------

	return "cop/bbs/EgovNoticeUpdt";
    }

    /**
     * 게시물에 대한 내용을 수정한다.
     *
     * @param boardVO
     * @param board
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/updateBoardArticle.do")
    public String updateBoardArticle(final MultipartHttpServletRequest multiRequest, @ModelAttribute("searchVO") BoardVO boardVO,
	    @ModelAttribute("bdMstr") BoardMaster bdMstr, @ModelAttribute("board") Board board, BindingResult bindingResult, ModelMap model,
	    SessionStatus status) throws Exception {

    	// 사용자권한 처리
    	if(!EgovUserDetailsHelper.isAuthenticated()) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return "cmm/uat/uia/EgovLoginUsr";
    	}

	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

	String atchFileId = boardVO.getAtchFileId();

	beanValidator.validate(board, bindingResult);
	if (bindingResult.hasErrors()) {

	    boardVO.setFrstRegisterId(user.getUniqId());

	    BoardMaster master = new BoardMaster();
	    BoardMasterVO bmvo = new BoardMasterVO();
	    BoardVO bdvo = new BoardVO();

	    master.setBbsId(boardVO.getBbsId());
	    master.setUniqId(user.getUniqId());

	    bmvo = bbsAttrbService.selectBBSMasterInf(master);
	    bdvo = bbsMngService.selectBoardArticle(boardVO);

	    model.addAttribute("result", bdvo);
	    model.addAttribute("bdMstr", bmvo);

	    return "cop/bbs/EgovNoticeUpdt";
	}

	if (isAuthenticated) {
	    final Map<String, MultipartFile> files = multiRequest.getFileMap();
	    if (!files.isEmpty()) {
		if ("".equals(atchFileId)) {
		    List<FileVO> result = fileUtil.parseFileInf(files, "BBS_", 0, atchFileId, "");
		    atchFileId = fileMngService.insertFileInfs(result);
		    board.setAtchFileId(atchFileId);
		} else {
		    FileVO fvo = new FileVO();
		    fvo.setAtchFileId(atchFileId);
		    int cnt = fileMngService.getMaxFileSN(fvo);
		    List<FileVO> _result = fileUtil.parseFileInf(files, "BBS_", cnt, atchFileId, "");
		    fileMngService.updateFileInfs(_result);
		}
	    }

	    board.setLastUpdusrId(user.getUniqId());

	    board.setNtcrNm("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
	    board.setPassword("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
	    board.setNttCn(unscript(board.getNttCn()));	// XSS 방지

	    bbsMngService.updateBoardArticle(board);
	}

	return "forward:/cop/bbs/selectBoardList.do";
    }

    /**
     * 게시물에 대한 내용을 삭제한다.
     *
     * @param boardVO
     * @param board
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/deleteBoardArticle.do")
    public String deleteBoardArticle(@ModelAttribute("searchVO") BoardVO boardVO, @ModelAttribute("board") Board board,
	    @ModelAttribute("bdMstr") BoardMaster bdMstr, ModelMap model) throws Exception {

    	// 사용자권한 처리
    	if(!EgovUserDetailsHelper.isAuthenticated()) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return "cmm/uat/uia/EgovLoginUsr";
    	}

	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

	if (isAuthenticated) {
	    board.setLastUpdusrId(user.getUniqId());

	    bbsMngService.deleteBoardArticle(board);
	}

	return "forward:/cop/bbs/selectBoardList.do";
    }

    /**
     * 템플릿에 대한 미리보기용 게시물 목록을 조회한다.
     *
     * @param boardVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/previewBoardList.do")
    public String previewBoardArticles(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
	//LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

	String template = boardVO.getSearchWrd();	// 템플릿 URL

	BoardMasterVO master = new BoardMasterVO();

	master.setBbsNm("미리보기 게시판");

	boardVO.setPageUnit(propertyService.getInt("pageUnit"));
	boardVO.setPageSize(propertyService.getInt("pageSize"));

	PaginationInfo paginationInfo = new PaginationInfo();

	paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
	paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
	paginationInfo.setPageSize(boardVO.getPageSize());

	boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
	boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

	BoardVO target = null;
	List<BoardVO> list = new ArrayList<BoardVO>();

	target = new BoardVO();
	target.setNttSj("게시판 기능 설명");
	target.setFrstRegisterId("ID");
	target.setFrstRegisterNm("관리자");
	target.setFrstRegisterPnttm("2009-01-01");
	target.setInqireCo(7);
	target.setParnts("0");
	target.setReplyAt("N");
	target.setReplyLc("0");
	target.setUseAt("Y");

	list.add(target);

	target = new BoardVO();
	target.setNttSj("게시판 부가 기능 설명");
	target.setFrstRegisterId("ID");
	target.setFrstRegisterNm("관리자");
	target.setFrstRegisterPnttm("2009-01-01");
	target.setInqireCo(7);
	target.setParnts("0");
	target.setReplyAt("N");
	target.setReplyLc("0");
	target.setUseAt("Y");

	list.add(target);

	boardVO.setSearchWrd("");

	int totCnt = list.size();

	paginationInfo.setTotalRecordCount(totCnt);

	master.setTmplatCours(template);

	model.addAttribute("resultList", list);
	model.addAttribute("resultCnt", Integer.toString(totCnt));
	model.addAttribute("boardVO", boardVO);
	model.addAttribute("brdMstrVO", master);
	model.addAttribute("paginationInfo", paginationInfo);

	model.addAttribute("preview", "true");

	return "cop/bbs/EgovNoticeList";
    }
}
