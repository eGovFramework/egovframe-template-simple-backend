package egovframework.let.cop.bbs.service;

import java.util.Map;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.model.BoardMasterVO;

/**
 * 게시판 속성관리를 위한 서비스 인터페이스 클래스
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
 *  
 *  </pre>
 */
public interface EgovBBSLoneMasterService {

	/**
	 * 등록된 게시판 속성정보를 삭제한다.
	 * @param BoardMaster
	 * 
	 * @param boardMaster
	 * @exception Exception Exception
	 */
	public void deleteMaster(BoardMaster boardMaster)
	  throws Exception;

	/**
	 * 신규 게시판 속성정보를 생성한다.
	 * @param BoardMaster
	 * 
	 * @param boardMaster
	 * @exception Exception Exception
	 */
	public String insertMaster(BoardMaster boardMaster)
	  throws Exception;

	/**
	 * 게시판 속성정보 한 건을 상세조회한다.
	 * @param BoardMasterVO
	 * 
	 * @param searchVO
	 * @exception Exception Exception
	 */
	public BoardMasterVO selectMaster(BoardMaster searchVO)
	  throws Exception;

	/**
	 * 게시판 속성 정보의 목록을 조회 한다.
	 * @param BoardMasterVO
	 * 
	 * @param searchVO
	 * @exception Exception Exception
	 */
	public Map<String, Object> selectMasterList(BoardMasterVO searchVO)
	  throws Exception;

	/**
	 * 게시판 속성정보를 수정한다.
	 * @param BoardMaster
	 * 
	 * @param boardMaster
	 * @exception Exception Exception
	 */
	public void updateMaster(BoardMaster boardMaster)
	  throws Exception;

}