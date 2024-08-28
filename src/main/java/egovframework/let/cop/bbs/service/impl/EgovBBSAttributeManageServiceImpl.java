package egovframework.let.cop.bbs.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.stereotype.Service;

import egovframework.let.cop.bbs.service.BoardMaster;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.com.service.BoardUseInf;
import egovframework.let.cop.com.service.EgovUserInfManageService;
import egovframework.let.cop.com.service.UserInfVO;
import egovframework.let.cop.com.service.impl.BBSUseInfoManageDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 게시판 속성관리를 위한 서비스 구현 클래스
 * 
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.24
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *   2009.03.24  이삼섭          최초 생성
 *   2009.06.26  한성곤          2단계 기능 추가 (댓글관리, 만족도조사)
 *   2011.08.31  JJY          경량환경 템플릿 커스터마이징버전 생성
 *   2024.08.28  이백행          컨트리뷰션 롬복 생성자 기반 종속성 주입
 *
 *      </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EgovBBSAttributeManageServiceImpl extends EgovAbstractServiceImpl
		implements EgovBBSAttributeManageService {

	private final BBSAttributeManageDAO bbsAttributeManageDAO;

	private final BBSUseInfoManageDAO bbsUseInfoManageDAO;

	private final EgovUserInfManageService egovUserInfManageService;

	private final EgovIdGnrService egovBBSMstrIdGnrService;

	private final EgovPropertyService egovPropertyService;

	/**
	 * <pre>
	 * 2단계 기능 추가 (댓글관리, 만족도조사) 관리를 위한 데이터 접근 클래스
	 * 2009.06.26 : 2단계 기능 추가
	 * </pre>
	 */
	private final BBSAddedOptionsDAO addedOptionsDAO;

	/**
	 * 등록된 게시판 속성정보를 삭제한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSAttributeManageService#deleteBBSMasterInf(egovframework.let.cop.bbs.brd.service.BoardMaster)
	 */
	@Override
	public void deleteBBSMasterInf(BoardMaster boardMaster) throws Exception {
		bbsAttributeManageDAO.deleteBBSMasterInf(boardMaster);

		BoardUseInf bdUseInf = new BoardUseInf();

		bdUseInf.setBbsId(boardMaster.getBbsId());
		bdUseInf.setLastUpdusrId(boardMaster.getLastUpdusrId());

		bbsUseInfoManageDAO.deleteBBSUseInfByBoardId(bdUseInf);
	}

	/**
	 * 신규 게시판 속성정보를 생성한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSAttributeManageService#insertBBSMastetInf(egovframework.let.cop.bbs.brd.service.BoardMaster)
	 */
	@Override
	public String insertBBSMastetInf(BoardMaster boardMaster) throws Exception {
		String bbsId = egovBBSMstrIdGnrService.getNextStringId();

		boardMaster.setBbsId(bbsId);

		bbsAttributeManageDAO.insertBBSMasterInf(boardMaster);

		// ---------------------------------
		// 2009.06.26 : 2단계 기능 추가
		// ---------------------------------
		if (boardMaster.getOption().equals("comment") || boardMaster.getOption().equals("stsfdg")) {
			addedOptionsDAO.insertAddedOptionsInf(boardMaster);
		}
		//// -------------------------------

		if ("Y".equals(boardMaster.getBbsUseFlag())) {
			BoardUseInf bdUseInf = new BoardUseInf();

			bdUseInf.setBbsId(bbsId);
			bdUseInf.setTrgetId(boardMaster.getTrgetId());
			bdUseInf.setRegistSeCode(boardMaster.getRegistSeCode());
			bdUseInf.setFrstRegisterId(boardMaster.getFrstRegisterId());
			bdUseInf.setUseAt("Y");

			bbsUseInfoManageDAO.insertBBSUseInf(bdUseInf);

			UserInfVO userVO = new UserInfVO();
			userVO.setTrgetId(boardMaster.getTrgetId());

			List<UserInfVO> tmpList = null;
			Iterator<UserInfVO> iter = null;

			if ("REGC05".equals(boardMaster.getRegistSeCode())) {
				tmpList = egovUserInfManageService.selectAllClubUser(userVO);
				iter = tmpList.iterator();
				while (iter.hasNext()) {
					bdUseInf = new BoardUseInf();

					bdUseInf.setBbsId(bbsId);
					bdUseInf.setTrgetId(iter.next().getUniqId());
					bdUseInf.setRegistSeCode("REGC07");
					bdUseInf.setUseAt("Y");
					bdUseInf.setFrstRegisterId(boardMaster.getFrstRegisterId());

					bbsUseInfoManageDAO.insertBBSUseInf(bdUseInf);
				}
			} else if ("REGC06".equals(boardMaster.getRegistSeCode())) {
				tmpList = egovUserInfManageService.selectAllCmmntyUser(userVO);
				iter = tmpList.iterator();
				while (iter.hasNext()) {
					bdUseInf = new BoardUseInf();

					bdUseInf.setBbsId(bbsId);
					bdUseInf.setTrgetId(iter.next().getUniqId());
					bdUseInf.setRegistSeCode("REGC07");
					bdUseInf.setUseAt("Y");
					bdUseInf.setFrstRegisterId(boardMaster.getFrstRegisterId());

					bbsUseInfoManageDAO.insertBBSUseInf(bdUseInf);
				}
			}
		}
		return bbsId;
	}

	/**
	 * 게시판 속성 정보의 목록을 조회 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSAttributeManageService#selectAllBBSMasteInf(egovframework.let.cop.bbs.brd.service.BoardMasterVO)
	 */
	@Override
	public List<BoardMasterVO> selectAllBBSMasteInf(BoardMasterVO vo) throws Exception {
		return bbsAttributeManageDAO.selectAllBBSMasteInf(vo);
	}

	/**
	 * 게시판 속성정보 한 건을 상세조회한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSAttributeManageService#selectBBSMasterInf(egovframework.let.cop.bbs.brd.service.BoardMasterVO)
	 */
	@Override
	public BoardMasterVO selectBBSMasterInf(BoardMaster searchVO) throws Exception {
		// ---------------------------------
		// 2009.06.26 : 2단계 기능 추가
		// ---------------------------------
		// return attrbMngDAO.selectBBSMasterInf(searchVO);

		BoardMasterVO result = bbsAttributeManageDAO.selectBBSMasterInf(searchVO);

		String flag = egovPropertyService.getString("Globals.addedOptions");
		if (flag != null && flag.trim().equalsIgnoreCase("true")) {
			BoardMasterVO options = addedOptionsDAO.selectAddedOptionsInf(searchVO);

			if (options != null) {
				if (options.getCommentAt().equals("Y")) {
					result.setOption("comment");
				}

				if (options.getStsfdgAt().equals("Y")) {
					result.setOption("stsfdg");
				}
			} else {
				result.setOption("na"); // 미지정 상태로 수정 가능 (이미 지정된 경우는 수정 불가로 처리)
			}
		}

		return result;
		//// -------------------------------

	}

	/**
	 * 게시판 속성 정보의 목록을 조회 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSAttributeManageService#selectBBSMasterInfs(egovframework.let.cop.bbs.brd.service.BoardMasterVO)
	 */
	@Override
	public Map<String, Object> selectBBSMasterInfs(BoardMasterVO searchVO) throws Exception {
		List<BoardMasterVO> result = bbsAttributeManageDAO.selectBBSMasterInfs(searchVO);
		int cnt = bbsAttributeManageDAO.selectBBSMasterInfsCnt(searchVO);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}

	/**
	 * 게시판 속성정보를 수정한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSAttributeManageService#updateBBSMasterInf(egovframework.let.cop.bbs.brd.service.BoardMaster)
	 */
	@Override
	public void updateBBSMasterInf(BoardMaster boardMaster) throws Exception {
		bbsAttributeManageDAO.updateBBSMasterInf(boardMaster);

		// ---------------------------------
		// 2009.06.26 : 2단계 기능 추가
		// ---------------------------------
		String flag = egovPropertyService.getString("Globals.addedOptions");
		if (flag != null && flag.trim().equalsIgnoreCase("true")) {
			if (boardMaster.getOption().equals("na")) {
				return;
			}
			BoardMasterVO options = addedOptionsDAO.selectAddedOptionsInf(boardMaster);

			if (options == null) {
				boardMaster.setFrstRegisterId(boardMaster.getLastUpdusrId());
				addedOptionsDAO.insertAddedOptionsInf(boardMaster);
			} else {
				// 수정 기능 제외 (새롭게 선택사항을 지정한 insert만 처리함)
				// addedOptionsDAO.updateAddedOptionsInf(boardMaster);
				log.debug("BBS Master update ignored...");
			}
		}
		//// -------------------------------
	}

	/**
	 * 템플릿의 유효여부를 점검한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSAttributeManageService#validateTemplate(egovframework.let.cop.bbs.brd.service.BoardMasterVO)
	 */
	@Override
	public void validateTemplate(BoardMasterVO searchVO) throws Exception {
		log.debug("validateTemplate method ignored...");
	}

	/**
	 * 사용중인 게시판 속성 정보의 목록을 조회 한다.
	 */
	@Override
	public Map<String, Object> selectBdMstrListByTrget(BoardMasterVO vo) throws Exception {
		List<BoardMasterVO> result = bbsAttributeManageDAO.selectBdMstrListByTrget(vo);
		int cnt = bbsAttributeManageDAO.selectBdMstrListCntByTrget(vo);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}

	/**
	 * 커뮤니티, 동호회에서 사용중인 게시판 속성 정보의 목록을 전체조회 한다.
	 */
	@Override
	public List<BoardMasterVO> selectAllBdMstrByTrget(BoardMasterVO vo) throws Exception {
		return bbsAttributeManageDAO.selectAllBdMstrByTrget(vo);
	}

	/**
	 * 사용중이지 않은 게시판 속성 정보의 목록을 조회 한다.
	 */
	@Override
	public Map<String, Object> selectNotUsedBdMstrList(BoardMasterVO searchVO) throws Exception {
		List<BoardMasterVO> result = bbsAttributeManageDAO.selectNotUsedBdMstrList(searchVO);
		int cnt = bbsAttributeManageDAO.selectNotUsedBdMstrListCnt(searchVO);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}
}
