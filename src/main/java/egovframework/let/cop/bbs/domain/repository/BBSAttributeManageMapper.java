package egovframework.let.cop.bbs.domain.repository;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import egovframework.let.cop.bbs.dto.request.BbsAttributeUpdateRequestDTO;

/**
 * 게시판 속성정보 관리를 위한 Mapper 인터페이스
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
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@EgovMapper
public interface BBSAttributeManageMapper {

    void deleteBBSMasterInf(BoardMaster boardMaster);

    int insertBBSMasterInf(BoardMaster boardMaster);

    BoardMasterVO selectBBSMasterInf(BoardMaster searchVO);

    List<BoardMasterVO> selectBBSMasterInfs(BoardMasterVO vo);

    Integer selectBBSMasterInfsCnt(BoardMasterVO vo);

    void updateBBSMasterInf(BoardMaster boardMaster);

    List<BoardMasterVO> selectAllBBSMaster(BoardMasterVO vo);

    List<BoardMasterVO> selectAllBBSMaster(BbsAttributeUpdateRequestDTO dto);

    List<BoardMasterVO> selectBdMstrListByTrget(BoardMasterVO vo);

    Integer selectBdMstrListCntByTrget(BoardMasterVO vo);

    List<BoardMasterVO> selectAllBdMstrByTrget(BoardMasterVO vo);

    List<BoardMasterVO> selectNotUsedBdMstrList(BoardMasterVO vo);

    Integer selectNotUsedBdMstrListCnt(BoardMasterVO vo);
}
