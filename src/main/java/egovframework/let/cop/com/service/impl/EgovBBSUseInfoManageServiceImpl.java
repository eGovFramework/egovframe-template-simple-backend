package egovframework.let.cop.com.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.let.cop.com.service.BoardUseInf;
import egovframework.let.cop.com.service.BoardUseInfVO;
import egovframework.let.cop.com.service.EgovBBSUseInfoManageService;
import lombok.RequiredArgsConstructor;

/**
 * 게시판 이용정보를 관리하기 위한 서비스 구현 클래스
 * 
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.02
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.02  이삼섭          최초 생성
 *   2011.08.31  JJY           경량환경 템플릿 커스터마이징버전 생성
 *   2024.08.29  이백행          컨트리뷰션 롬복 생성자 기반 종속성 주입
 *
 *      </pre>
 */
@Service
@RequiredArgsConstructor
public class EgovBBSUseInfoManageServiceImpl extends EgovAbstractServiceImpl implements EgovBBSUseInfoManageService {

//	@Resource(name = "BBSUseInfoManageDAO")
//	private BBSUseInfoManageDAO bbsUseDAO;
	private final BBSUseInfoManageDAO bbsUseInfoManageDAO;

	/**
	 * 게시판 사용 정보를 삭제한다.
	 * 
	 * @see egovframework.let.cop.bbs.com.service.EgovBBSUseInfoManageService#deleteBBSUseInf(egovframework.let.cop.bbs.com.service.BoardUseInf)
	 */
	@Override
	public void deleteBBSUseInf(BoardUseInf bdUseInf) throws Exception {
		bbsUseInfoManageDAO.deleteBBSUseInf(bdUseInf);
	}

	/**
	 * 게시판 사용정보를 등록한다.
	 * 
	 * @see egovframework.let.cop.bbs.com.service.EgovBBSUseInfoManageService#insertBBSUseInf(egovframework.let.cop.bbs.com.service.BoardUseInf)
	 */
	@Override
	public void insertBBSUseInf(BoardUseInf bdUseInf) throws Exception {
		bbsUseInfoManageDAO.insertBBSUseInf(bdUseInf);
	}

	/**
	 * 게시판 사용정보 목록을 조회한다.
	 * 
	 * @see egovframework.let.cop.bbs.com.service.EgovBBSUseInfoManageService#selectBBSUseInfs(egovframework.let.cop.bbs.com.service.BoardUseInfVO)
	 */
	@Override
	public Map<String, Object> selectBBSUseInfs(BoardUseInfVO bdUseVO) throws Exception {

		List<BoardUseInfVO> result = bbsUseInfoManageDAO.selectBBSUseInfs(bdUseVO);
		int cnt = bbsUseInfoManageDAO.selectBBSUseInfsCnt(bdUseVO);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}

	/**
	 * 게시판 사용정보를 수정한다.
	 * 
	 * @see egovframework.let.cop.bbs.com.service.EgovBBSUseInfoManageService#updateBBSUseInf(egovframework.let.cop.bbs.com.service.BoardUseInf)
	 */
	@Override
	public void updateBBSUseInf(BoardUseInf bdUseInf) throws Exception {
		bbsUseInfoManageDAO.updateBBSUseInf(bdUseInf);
	}

	/**
	 * 게시판 사용정보에 대한 상세정보를 조회한다.
	 * 
	 * @see egovframework.let.cop.bbs.com.service.EgovBBSUseInfoManageService#selectBBSUseInf(egovframework.let.cop.bbs.com.service.BoardUseInfVO)
	 */
	@Override
	public BoardUseInfVO selectBBSUseInf(BoardUseInfVO bdUseVO) throws Exception {
		return bbsUseInfoManageDAO.selectBBSUseInf(bdUseVO);
	}

	/**
	 * 동호회에 사용되는 게시판 사용정보를 삭제한다.
	 * 
	 * @see egovframework.let.cop.com.service.EgovBBSUseInfoManageService#deleteBBSUseInfByClub(egovframework.let.cop.com.service.BoardUseInf)
	 */
	@Override
	public void deleteBBSUseInfByClub(BoardUseInfVO bdUseVO) throws Exception {
		List<BoardUseInf> result = bbsUseInfoManageDAO.selectBBSUseInfByClub(bdUseVO);

		BoardUseInf bdUseInf = null;
		Iterator<BoardUseInf> iter = result.iterator();
		while (iter.hasNext()) {
			bdUseInf = iter.next();

			bdUseInf.setLastUpdusrId(bdUseVO.getLastUpdusrId());
			// bdUseInf.setTrgetId(bdUseVO.getClbId()); // 사용자 ID를 넘겨야 함..
			bdUseInf.setTrgetId(bdUseVO.getTrgetId());

			bbsUseInfoManageDAO.deleteBBSUseInf(bdUseInf);
		}
	}

	/**
	 * 커뮤니티에 사용되는 게시판 사용정보를 삭제한다.
	 * 
	 * @see egovframework.let.cop.com.service.EgovBBSUseInfoManageService#deleteBBSUseInfByCmmnty(egovframework.let.cop.com.service.BoardUseInf)
	 */
	@Override
	public void deleteBBSUseInfByCmmnty(BoardUseInfVO bdUseVO) throws Exception {
		List<BoardUseInf> result = bbsUseInfoManageDAO.selectBBSUseInfByCmmnty(bdUseVO);

		BoardUseInf bdUseInf = null;
		Iterator<BoardUseInf> iter = result.iterator();

		while (iter.hasNext()) {
			bdUseInf = iter.next();

			bdUseInf.setLastUpdusrId(bdUseVO.getLastUpdusrId());
			// bdUseInf.setTrgetId(bdUseVO.getCmmntyId()); // 사용자 ID를 넘겨야 함..
			bdUseInf.setTrgetId(bdUseVO.getTrgetId());

			bbsUseInfoManageDAO.deleteBBSUseInf(bdUseInf);
		}
	}

	/**
	 * 동호회에 사용되는 모든 게시판 사용정보를 삭제한다.
	 * 
	 * @see egovframework.let.cop.com.service.EgovBBSUseInfoManageService#deleteAllBBSUseInfByClub(egovframework.let.cop.com.service.BoardUseInfVO)
	 */
	@Override
	public void deleteAllBBSUseInfByClub(BoardUseInfVO bdUseVO) throws Exception {
		bbsUseInfoManageDAO.deleteAllBBSUseInfByClub(bdUseVO);
	}

	/**
	 * 커뮤니티에 사용되는 모든 게시판 사용정보를 삭제한다.
	 * 
	 * @see egovframework.let.cop.com.service.EgovBBSUseInfoManageService#deleteAllBBSUseInfByCmmnty(egovframework.let.cop.com.service.BoardUseInfVO)
	 */
	@Override
	public void deleteAllBBSUseInfByCmmnty(BoardUseInfVO bdUseVO) throws Exception {
		bbsUseInfoManageDAO.deleteAllBBSUseInfByCmmnty(bdUseVO);
	}

	/**
	 * 게시판에 대한 사용정보를 삭제한다.
	 * 
	 * @see egovframework.let.cop.com.service.EgovBBSUseInfoManageService#deleteBBSUseInfByBoardId(egovframework.let.cop.com.service.BoardUseInf)
	 */
	@Override
	public void deleteBBSUseInfByBoardId(BoardUseInf bdUseInf) throws Exception {
		bbsUseInfoManageDAO.deleteBBSUseInfByBoardId(bdUseInf);
	}

	/**
	 * 커뮤니티, 동호회에 사용되는 게시판 사용정보에 대한 목록을 조회한다.
	 * 
	 * @see egovframework.let.cop.com.service.EgovBBSUseInfoManageService#selectBBSUseInfsByTrget(egovframework.let.cop.com.service.BoardUseInfVO)
	 */
	@Override
	public Map<String, Object> selectBBSUseInfsByTrget(BoardUseInfVO bdUseVO) throws Exception {
		List<BoardUseInfVO> result = bbsUseInfoManageDAO.selectBBSUseInfsByTrget(bdUseVO);
		int cnt = bbsUseInfoManageDAO.selectBBSUseInfsCntByTrget(bdUseVO);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}

	/**
	 * 커뮤니티, 동호회에 사용되는 게시판 사용정보를 수정한다.
	 */
	@Override
	public void updateBBSUseInfByTrget(BoardUseInf bdUseInf) throws Exception {
		bbsUseInfoManageDAO.updateBBSUseInfByTrget(bdUseInf);
	}
}
