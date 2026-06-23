package egovframework.let.cop.bbs.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import egovframework.let.cop.bbs.domain.repository.BBSLoneMasterDAO;
import egovframework.let.cop.bbs.service.EgovBBSLoneMasterService;
import egovframework.let.cop.com.service.BoardUseInf;
import egovframework.let.cop.com.service.impl.BBSUseInfoManageDAO;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.cmmn.exception.BaseRuntimeException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * 게시판 속성관리를 위한 서비스 구현 클래스
 * @author 공통 서비스 개발팀 한성곤
 * @since 2009.08.25
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.08.25  한성곤          최초 생성
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성 
 *  2026.06.23  이백행          [2026년 컨트리뷰션] DAO 반환값 추가
 *  
 *  </pre>
 */
@Service("EgovBBSLoneMasterService")
@RequiredArgsConstructor
@Slf4j
public class EgovBBSLoneMasterServiceImpl extends EgovAbstractServiceImpl implements EgovBBSLoneMasterService {

    @Resource(name = "BBSLoneMasterDAO")
    private BBSLoneMasterDAO masterDAO;

    @Resource(name = "BBSUseInfoManageDAO")
    private BBSUseInfoManageDAO bbsUseDAO;

    @Resource(name = "egovBBSMstrIdGnrService")
    private EgovIdGnrService idgenService;

    private final EgovMessageSource egovMessageSource;

    /**
     * 등록된 게시판 속성정보를 삭제한다.
     */
    public void deleteMaster(BoardMaster boardMaster) throws Exception {
		int result = masterDAO.deleteMaster(boardMaster);
		if (log.isDebugEnabled()) {
			log.debug("result={}", result);
		}
		if (result == 0) {
			throw new BaseRuntimeException(egovMessageSource.getMessage("fail.common.update"));
		}
		
		BoardUseInf bdUseInf = new BoardUseInf();
		
		bdUseInf.setBbsId(boardMaster.getBbsId());
		bdUseInf.setLastUpdusrId(boardMaster.getLastUpdusrId());
		
		int result2 = bbsUseDAO.deleteBBSUseInfByBoardId(bdUseInf);
		if (log.isDebugEnabled()) {
			log.debug("result2={}", result2);
		}
		if (result2 == 0) {
			throw new BaseRuntimeException(egovMessageSource.getMessage("fail.common.update"));
		}
    }

    /**
     * 신규 게시판 속성정보를 생성한다.
     */
    public String insertMaster(BoardMaster boardMaster) throws Exception {
		String bbsId = idgenService.getNextStringId();
		
		boardMaster.setBbsId(bbsId);
		
		masterDAO.insertMaster(boardMaster);
	
		//----------------------------------------------
		// 게시판 사용 등록 (시스템)
		//----------------------------------------------
		BoardUseInf bdUseInf = new BoardUseInf();
	
		bdUseInf.setBbsId(bbsId);
		bdUseInf.setTrgetId("SYSTEM_DEFAULT_BOARD");
		bdUseInf.setRegistSeCode("REGC01");
		bdUseInf.setFrstRegisterId(boardMaster.getFrstRegisterId());
		bdUseInf.setUseAt("Y");
	
		int result = bbsUseDAO.insertBBSUseInf(bdUseInf);
		if (log.isDebugEnabled()) {
			log.debug("result={}", result);
		}
		if (result == 0) {
			throw new BaseRuntimeException(egovMessageSource.getMessage("fail.common.insert"));
		}
		    
		return bbsId;
    }

    /**
     * 게시판 속성정보 한 건을 상세조회한다.
     */
    public BoardMasterVO selectMaster(BoardMaster searchVO) throws Exception {
    	return masterDAO.selectMaster(searchVO);
    }

    /**
     * 게시판 속성 정보의 목록을 조회 한다.
     */
    public Map<String, Object> selectMasterList(BoardMasterVO searchVO) throws Exception {
		List<BoardMasterVO> result = masterDAO.selectMasterList(searchVO);
		int cnt = masterDAO.selectMasterListCnt(searchVO);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
	
		return map;
    }

    /**
     * 게시판 속성정보를 수정한다.
     */
    public void updateMaster(BoardMaster boardMaster) throws Exception {
    	int result = masterDAO.updateMaster(boardMaster);
		if (log.isDebugEnabled()) {
			log.debug("result={}", result);
		}
		if (result == 0) {
			throw new BaseRuntimeException(egovMessageSource.getMessage("fail.common.update"));
		}
    }
}
