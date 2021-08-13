package egovframework.let.cop.com.service;

import java.util.List;
import java.util.Map;

/**
 * 협업 기능에서 사용자 정보를 관리하기 위한 서비스 인터페이스 클래스
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
public interface EgovUserInfManageService {

	/**
     * 사용자 정보에 대한 목록을 조회한다.
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectUserList(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티 사용자 목록을 조회한다.
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectCmmntyUserList(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티 관리자 목록을 조회한다.
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectCmmntyMngrList(UserInfVO userVO) throws Exception;

    /**
     * 동호회 사용자 목록을 조회한다.
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectClubUserList(UserInfVO userVO) throws Exception;

    /**
     * 동호회 운영자 목록을 조회한다.
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectClubOprtrList(UserInfVO userVO) throws Exception;

    /**
     * 동호회에 대한 모든 사용자 목록을 조회한다.
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectAllClubUser(UserInfVO userVO) throws Exception;

    /**
     * 커뮤니티에 대한 모든 사용자 목록을 조회한다.
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    public List<UserInfVO> selectAllCmmntyUser(UserInfVO userVO) throws Exception;
}
