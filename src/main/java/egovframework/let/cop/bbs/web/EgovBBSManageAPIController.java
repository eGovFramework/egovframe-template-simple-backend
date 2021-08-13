package egovframework.let.cop.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.bbs.service.Board;
import egovframework.let.cop.bbs.service.BoardMaster;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

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
public class EgovBBSManageAPIController {

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

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;

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
	 * 게시물에 대한 목록을 조회한다.
	 *
	 * @param boardVO
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cop/bbs/selectBoardListAPI.do")
	public @ResponseBody Map<String, Object> selectBoardArticles(@RequestBody BoardVO boardVO, ModelMap model)
		throws Exception {
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
		/*if (master.getBbsTyCode().equals("BBST04")) {
		return "forward:/cop/bbs/selectGuestList.do";
		}*/
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

		Map<String, Object> resultMap = bbsMngService.selectBoardArticles(boardVO, vo.getBbsAttrbCode());
		int totCnt = Integer.parseInt((String)resultMap.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		//-------------------------------
		// 기본 BBS template 지정
		//-------------------------------
		/*if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
		master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}*/
		////-----------------------------

		/*
		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("brdMstrVO", master);
		model.addAttribute("paginationInfo", paginationInfo);
		*/
		resultMap.put("boardVO", boardVO);
		resultMap.put("brdMstrVO", master);
		resultMap.put("paginationInfo", paginationInfo);

		return resultMap;
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
	@RequestMapping("/cop/bbs/selectBoardArticleAPI.do")
	public @ResponseBody Map<String, Object> selectBoardArticle(@RequestBody BoardVO boardVO, ModelMap model)
		throws Exception {
		LoginVO user = new LoginVO();
		if (EgovUserDetailsHelper.isAuthenticated()) {
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

		//model.addAttribute("result", vo);
		//model.addAttribute("sessionUniqId", user.getUniqId());

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
		//model.addAttribute("brdMstrVO", masterVo);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", vo);
		resultMap.put("sessionUniqId", user.getUniqId());
		resultMap.put("brdMstrVO", masterVo);

		// 2021-06-01 신용호 추가
		// 첨부파일 확인
		if (vo == null || vo.getAtchFileId() == null || vo.getAtchFileId().isEmpty()) {

		} else {
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(vo.getAtchFileId());
			List<FileVO> resultFiles = fileService.selectFileInfs(fileVO);
			resultMap.put("resultFiles", resultFiles);
		}

		return resultMap;
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
	@RequestMapping("/cop/bbs/updateBoardArticleAPI.do")
	public @ResponseBody ResultVO updateBoardArticle(final MultipartHttpServletRequest multiRequest,
		@ModelAttribute("searchVO") BoardVO boardVO, @ModelAttribute("bdMstr") BoardMaster bdMstr,
		@ModelAttribute("board") Board board, BindingResult bindingResult, ModelMap model, SessionStatus status)
		throws Exception {

		// 사용자권한 처리
		/*
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
			return "cmm/uat/uia/EgovLoginUsr";
		}*/

		//LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		//Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		LoginVO user = new LoginVO();
		user.setUniqId("USRCNFRM_00000000000");
		Boolean isAuthenticated = true;

		String atchFileId = boardVO.getAtchFileId();
		ResultVO resultVO = new ResultVO();

		beanValidator.validate(board, bindingResult);
		if (bindingResult.hasErrors()) {

			/*
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

			resultVO.setResultCode(900);
			resultVO.setResultMessage(resultMessage);
			*/
			resultVO.setResultCode(ResultVO.INPUT_CHECK_ERROR);
			resultVO.setResultMessage(ResultVO.INPUT_CHECK_ERROR_MESSAGE);

			return resultVO;
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

			board.setNtcrNm(""); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
			board.setPassword(""); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
			board.setNttCn(unscript(board.getNttCn())); // XSS 방지

			bbsMngService.updateBoardArticle(board);
		}

		//return "forward:/cop/bbs/selectBoardList.do";
		return resultVO;
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
	@RequestMapping("/cop/bbs/insertBoardArticleAPI.do")
	public @ResponseBody ResultVO insertBoardArticle(final MultipartHttpServletRequest multiRequest,
		@ModelAttribute("searchVO") BoardVO boardVO, @ModelAttribute("bdMstr") BoardMaster bdMstr,
		@ModelAttribute("board") Board board, BindingResult bindingResult, SessionStatus status, ModelMap model)
		throws Exception {
		// 사용자권한 처리
		/*
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
			return "cmm/uat/uia/EgovLoginUsr";
		}
		*/

		//LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		//Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		LoginVO user = new LoginVO();
		user.setUniqId("USRCNFRM_00000000000");
		Boolean isAuthenticated = true;
		ResultVO resultVO = new ResultVO();

		beanValidator.validate(board, bindingResult);
		if (bindingResult.hasErrors()) {
			/*
			BoardMasterVO master = new BoardMasterVO();
			BoardMasterVO vo = new BoardMasterVO();

			vo.setBbsId(boardVO.getBbsId());
			vo.setUniqId(user.getUniqId());

			master = bbsAttrbService.selectBBSMasterInf(vo);

			model.addAttribute("bdMstr", master);

			// ----------------------------
			// 기본 BBS template 지정
			// ----------------------------
			if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
				master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
			}

			model.addAttribute("brdMstrVO", master);
			//// -----------------------------

			return "cop/bbs/EgovNoticeRegist";
			*/
			resultVO.setResultCode(ResultVO.INPUT_CHECK_ERROR);
			resultVO.setResultMessage(ResultVO.INPUT_CHECK_ERROR_MESSAGE);

			return resultVO;
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

			board.setNtcrNm(""); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
			board.setPassword(""); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
			// board.setNttCn(unscript(board.getNttCn())); // XSS 방지

			bbsMngService.insertBoardArticle(board);
		}
		// status.setComplete();
		//return "forward:/cop/bbs/selectBoardList.do";
		return resultVO;
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
	@RequestMapping("/cop/bbs/replyBoardArticleAPI.do")
	public @ResponseBody ResultVO replyBoardArticle(final MultipartHttpServletRequest multiRequest,
		@ModelAttribute("searchVO") BoardVO boardVO, @ModelAttribute("bdMstr") BoardMaster bdMstr,
		@ModelAttribute("board") Board board, BindingResult bindingResult, ModelMap model, SessionStatus status)
		throws Exception {

		// 사용자권한 처리
		/*
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
			return "cmm/uat/uia/EgovLoginUsr";
		}
		*/

		//LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		//Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		LoginVO user = new LoginVO();
		user.setUniqId("USRCNFRM_00000000000");
		Boolean isAuthenticated = true;
		ResultVO resultVO = new ResultVO();

		beanValidator.validate(board, bindingResult);
		if (bindingResult.hasErrors()) {
			/*
			BoardMasterVO master = new BoardMasterVO();
			BoardMasterVO vo = new BoardMasterVO();

			vo.setBbsId(boardVO.getBbsId());
			vo.setUniqId(user.getUniqId());

			master = bbsAttrbService.selectBBSMasterInf(vo);

			model.addAttribute("bdMstr", master);
			model.addAttribute("result", boardVO);

			// ----------------------------
			// 기본 BBS template 지정
			// ----------------------------
			if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
				master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
			}

			model.addAttribute("brdMstrVO", master);
			//// -----------------------------

			return "cop/bbs/EgovNoticeReply";
			*/
			resultVO.setResultCode(ResultVO.INPUT_CHECK_ERROR);
			resultVO.setResultMessage(ResultVO.INPUT_CHECK_ERROR_MESSAGE);

			return resultVO;
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

			board.setNtcrNm(""); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
			board.setPassword(""); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)

			board.setNttCn(unscript(board.getNttCn())); // XSS 방지

			bbsMngService.insertBoardArticle(board);
		}

		//return "forward:/cop/bbs/selectBoardList.do";
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