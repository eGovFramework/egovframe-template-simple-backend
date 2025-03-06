package egovframework.let.cop.com.service.impl;

import java.util.List;

import egovframework.let.cop.com.service.UserInfVO;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import org.springframework.stereotype.Repository;

/**
 * 협업 활용 사용자 정보 조회를 위한 데이터 접근 클래스
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
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Repository("EgovUserInfManageDAO")
public class EgovUserInfManageDAO extends EgovAbstractMapper {

    /**
     * 사용자 정보에 대한 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
	public List<UserInfVO> selectUserList(UserInfVO userVO) throws Exception {
		return selectList("EgovUserInfManageDAO.selectUserList", userVO);
	}

    /**
     * 사용자 정보에 대한 목록 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public int selectUserListCnt(UserInfVO userVO) throws Exception {
    	return (Integer)selectOne("EgovUserInfManageDAO.selectUserListCnt", userVO);
    }

    /**
     * 커뮤니티 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectCmmntyUserList(UserInfVO userVO) throws Exception {
		return selectList("EgovUserInfManageDAO.selectCmmntyUserList", userVO);
	}

    /**
     * 커뮤니티 사용자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public int selectCmmntyUserListCnt(UserInfVO userVO) throws Exception {
    	return (Integer)selectOne("EgovUserInfManageDAO.selectCmmntyUserListCnt", userVO);
    }

    /**
     * 커뮤니티 관리자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectCmmntyMngrList(UserInfVO userVO) throws Exception {
		return selectList("EgovUserInfManageDAO.selectCmmntyMngrList", userVO);
	}

    /**
     * 커뮤니티 관리자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public int selectCmmntyMngrListCnt(UserInfVO userVO) throws Exception {
    	return (Integer)selectOne("EgovUserInfManageDAO.selectCmmntyMngrListCnt", userVO);
    }

    /**
     * 동호회 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectClubUserList(UserInfVO userVO) throws Exception {
		return selectList("EgovUserInfManageDAO.selectClubUserList", userVO);
	}

    /**
     * 동호회 사용자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public int selectClubUserListCnt(UserInfVO userVO) throws Exception {
    	return (Integer)selectOne("EgovUserInfManageDAO.selectClubUserListCnt", userVO);
    }

    /**
     * 동호회 운영자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectClubOprtrList(UserInfVO userVO) throws Exception {
		return selectList("EgovUserInfManageDAO.selectClubOprtrList", userVO);
	}

    /**
     * 동호회 운영자 목록에 대한 전체 건수를 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public int selectClubOprtrListCnt(UserInfVO userVO) throws Exception {
    	return (Integer)selectOne("EgovUserInfManageDAO.selectClubOprtrListCnt", userVO);
    }

    /**
     * 동호회에 대한 모든 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectAllClubUser(UserInfVO userVO) throws Exception {
		return selectList("EgovUserInfManageDAO.selectAllClubUser", userVO);
	}

    /**
     * 커뮤니티에 대한 모든 사용자 목록을 조회한다.
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectAllCmmntyUser(UserInfVO userVO) throws Exception {
		return selectList("EgovUserInfManageDAO.selectAllCmmntyUser", userVO);
	}
}
