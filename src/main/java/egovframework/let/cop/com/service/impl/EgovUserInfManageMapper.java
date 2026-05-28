package egovframework.let.cop.com.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.let.cop.com.service.UserInfVO;

/**
 * 협업 활용 사용자 정보 조회를 위한 매퍼 인터페이스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.06  이삼섭          최초 생성
 *
 * </pre>
 */
@EgovMapper
public interface EgovUserInfManageMapper {

    /**
     * 사용자 정보에 대한 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    List<UserInfVO> selectUserList(UserInfVO userVO) throws Exception;

    /**
     * 사용자 정보에 대한 목록 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    int selectUserListCnt(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    List<UserInfVO> selectCmmntyUserList(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티 사용자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    int selectCmmntyUserListCnt(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티 관리자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    List<UserInfVO> selectCmmntyMngrList(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티 관리자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    int selectCmmntyMngrListCnt(UserInfVO userVO) throws Exception;

    /**
     * 동호회 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    List<UserInfVO> selectClubUserList(UserInfVO userVO) throws Exception;

    /**
     * 동호회 사용자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    int selectClubUserListCnt(UserInfVO userVO) throws Exception;

    /**
     * 동호회 운영자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    List<UserInfVO> selectClubOprtrList(UserInfVO userVO) throws Exception;

    /**
     * 동호회 운영자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    int selectClubOprtrListCnt(UserInfVO userVO) throws Exception;

    /**
     * 동호회에 대한 모든 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    List<UserInfVO> selectAllClubUser(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티에 대한 모든 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    List<UserInfVO> selectAllCmmntyUser(UserInfVO userVO) throws Exception;
}
