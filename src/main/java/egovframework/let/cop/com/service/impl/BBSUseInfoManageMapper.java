package egovframework.let.cop.com.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.let.cop.com.service.BoardUseInf;
import egovframework.let.cop.com.service.BoardUseInfVO;

/**
 * 게시판 이용정보를 관리하기 위한 매퍼 인터페이스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *   2009.04.02  이삼섭          최초 생성
 *
 * </pre>
 */
@EgovMapper
public interface BBSUseInfoManageMapper {

    /**
     * 게시판 사용 정보를 삭제한다.
     *
     * @param bdUseInf
     * @throws Exception
     */
    void deleteBBSUseInf(BoardUseInf bdUseInf) throws Exception;

    /**
     * 커뮤니티에 사용되는 게시판 사용정보 목록을 조회한다.
     *
     * @param bdUseVO
     * @return
     * @throws Exception
     */
    List<BoardUseInf> selectBBSUseInfByCmmnty(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 동호회에 사용되는 게시판 사용정보 목록을 조회한다.
     *
     * @param bdUseVO
     * @return
     * @throws Exception
     */
    List<BoardUseInf> selectBBSUseInfByClub(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 커뮤니티에 사용되는 모든 게시판 사용정보를 삭제한다.
     *
     * @param bdUseVO
     * @throws Exception
     */
    void deleteAllBBSUseInfByCmmnty(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 동호회에 사용되는 모든 게시판 사용정보를 삭제한다.
     *
     * @param bdUseVO
     * @throws Exception
     */
    void deleteAllBBSUseInfByClub(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 게시판 사용정보를 등록한다.
     *
     * @param bdUseInf
     * @throws Exception
     */
    void insertBBSUseInf(BoardUseInf bdUseInf) throws Exception;

    /**
     * 게시판 사용정보 목록을 조회한다.
     *
     * @param bdUseVO
     * @return
     * @throws Exception
     */
    List<BoardUseInfVO> selectBBSUseInfs(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 게시판 사용정보 목록 전체 건수를 조회한다.
     *
     * @param bdUseVO
     * @return
     * @throws Exception
     */
    int selectBBSUseInfsCnt(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 게시판 사용정보에 대한 상세정보를 조회한다.
     *
     * @param bdUseVO
     * @return
     * @throws Exception
     */
    BoardUseInfVO selectBBSUseInf(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 게시판 사용정보를 수정한다.
     *
     * @param bdUseInf
     * @throws Exception
     */
    void updateBBSUseInf(BoardUseInf bdUseInf) throws Exception;

    /**
     * 게시판에 대한 사용정보를 삭제한다.
     *
     * @param bdUseInf
     * @throws Exception
     */
    void deleteBBSUseInfByBoardId(BoardUseInf bdUseInf) throws Exception;

    /**
     * 커뮤니티, 동호회에 사용되는 게시판 사용정보에 대한 목록을 조회한다.
     *
     * @param bdUseVO
     * @return
     * @throws Exception
     */
    List<BoardUseInfVO> selectBBSUseInfsByTrget(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 커뮤니티, 동호회에 사용되는 게시판 사용정보에 대한 전체 건수를 조회한다.
     *
     * @param bdUseVO
     * @return
     * @throws Exception
     */
    int selectBBSUseInfsCntByTrget(BoardUseInfVO bdUseVO) throws Exception;

    /**
     * 커뮤니티, 동호회에 사용되는 게시판 사용정보를 수정한다.
     *
     * @param bdUseInf
     * @throws Exception
     */
    void updateBBSUseInfByTrget(BoardUseInf bdUseInf) throws Exception;
}
