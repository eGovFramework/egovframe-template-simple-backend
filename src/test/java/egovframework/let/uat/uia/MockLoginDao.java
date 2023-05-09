package egovframework.let.uat.uia;

import egovframework.com.cmm.LoginVO;
import egovframework.let.uat.uia.service.EgovLoginDao;

/**
 * fileName       : MockLoginDao
 * author         : crlee
 * date           : 2023/05/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/10        crlee       최초 생성
 */
public class MockLoginDao implements EgovLoginDao {

    @Override
    public LoginVO actionLogin(LoginVO vo) throws Exception {
        // goodPwd
        if( vo.getPassword().equals("e1x480pwjij10xqsVPptTdRE1+I7olz8IhleZ9vf7bU=")){
            vo.setUserSe("USR");
            return vo;
        }
        LoginVO failLogin = new LoginVO();
        failLogin.setId("");
        failLogin.setPassword("");
        return failLogin;
    }

    @Override
    public LoginVO searchId(LoginVO vo) throws Exception {
        if(
            vo.getName().equals("홍길동") &&
            vo.getEmail().equals("korea@naver.com")
        ){
            LoginVO sucSearch = new LoginVO();
            sucSearch.setId("helloHong123");
            return sucSearch;
        }

        LoginVO failSearch = new LoginVO();
        failSearch.setId("");
        return failSearch;
    }

    @Override
    public LoginVO searchPassword(LoginVO vo) throws Exception {
        if(
            vo.getId().equals("admin") &&
            vo.getEmail().equals("admin@naver.com") &&
            vo.getPasswordHint().equals("인하사대부속고등학교")
        ){
            LoginVO sucSearch = new LoginVO();
            sucSearch.setPassword("qwer1234!");
            return sucSearch;
        }

        LoginVO failSearch = new LoginVO();
        failSearch.setPassword("");
        return failSearch;
    }

    @Override
    public void updatePassword(LoginVO vo) throws Exception {

    }
}
