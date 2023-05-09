package egovframework.let.uat.uia.service;

import egovframework.com.cmm.LoginVO;

/**
 * fileName       : EgovLoginDao
 * author         : crlee
 * date           : 2023/05/09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/09        crlee       최초 생성
 */
public interface EgovLoginDao {

    /**
     * 일반 로그인을 처리한다
     * @param vo LoginVO
     * @return LoginVO
     * @exception Exception
     */
    public LoginVO actionLogin(LoginVO vo) throws Exception;

    /**
     * 아이디를 찾는다.
     * @param vo LoginVO
     * @return LoginVO
     * @exception Exception
     */
    public LoginVO searchId(LoginVO vo) throws Exception;

    /**
     * 비밀번호를 찾는다.
     * @param vo LoginVO
     * @return LoginVO
     * @exception Exception
     */
    public LoginVO searchPassword(LoginVO vo) throws Exception;

    /**
     * 변경된 비밀번호를 저장한다.
     * @param vo LoginVO
     * @exception Exception
     */
    public void updatePassword(LoginVO vo) throws Exception;
}
