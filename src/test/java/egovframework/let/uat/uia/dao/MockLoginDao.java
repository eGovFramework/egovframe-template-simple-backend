package egovframework.let.uat.uia.dao;

import egovframework.com.cmm.LoginVO;

/**
 * fileName       : MockLoginDao
 * author         : crlee
 * date           : 2023/05/10
 * description    : 로그인 Dao 목
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
        return LoginVO.builder()
                .id("")
                .password("")
                .build();
    }

    @Override
    public LoginVO searchId(LoginVO vo) throws Exception {
        if(
            vo.getName().equals("홍길동") &&
            vo.getEmail().equals("korea@naver.com")
        ){
            return LoginVO.builder()
                    .id("helloHong123")
                    .build();
        }

        return LoginVO.builder()
                .id("")
                .build();
    }

    @Override
    public LoginVO searchPassword(LoginVO vo) throws Exception {
        if(
            vo.getId().equals("admin") &&
            vo.getEmail().equals("admin@naver.com") &&
            vo.getPasswordHint().equals("인하사대부속고등학교")
        ){
            return LoginVO.builder()
                    .password("qwer1234!")
                    .build();
        }
        return LoginVO.builder()
                .password("")
                .build();
    }

    @Override
    public void updatePassword(LoginVO vo) throws Exception {

    }
}
