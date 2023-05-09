package egovframework.let.uat.uia.service;

import egovframework.com.cmm.LoginVO;
import egovframework.let.uat.uia.MockLoginDao;
import egovframework.let.uat.uia.service.impl.EgovLoginServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * fileName       : EgovLoginServiceImplTest
 * author         : crlee
 * date           : 2023/05/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/10        crlee       최초 생성
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EgovLoginServiceImplTest {

    EgovLoginService egovLoginService;

    @BeforeAll
    void init(){
        this.egovLoginService = new EgovLoginServiceImpl(new MockLoginDao());
    }

    @Test
    @DisplayName("로그인 성공")
    public void actionLoginSucTest() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("goodPwd");

        LoginVO result = egovLoginService.actionLogin( loginVO );
        Assertions.assertThat( result.getId() ).isEqualTo("admin");
        Assertions.assertThat( result.getUserSe() ).isEqualTo("USR");
    }
    @Test
    @DisplayName("로그인 실패")
    public void actionLoginFailTest() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("badPwd");

        LoginVO result = egovLoginService.actionLogin( loginVO );
        Assertions.assertThat( result.getId() ).isNull();
        Assertions.assertThat( result.getUserSe() ).isNull();
    }

    @Test
    @DisplayName("아이디 찾기 성공")
    public void searchIdSucTest() throws Exception{
        LoginVO loginVO = new LoginVO();
        loginVO.setName("홍길동");
        loginVO.setEmail("korea@naver.com");

        LoginVO searchLogin = egovLoginService.searchId(loginVO);
        Assertions.assertThat( searchLogin.getId() ).isEqualTo("helloHong123");
    }
    @Test
    @DisplayName("아이디 찾기 실패")
    public void searchIdFailTest() throws Exception{
        LoginVO loginVO = new LoginVO();
        loginVO.setName("홍길동");
        loginVO.setEmail("korea@kakao.com");

        LoginVO searchLogin = egovLoginService.searchId(loginVO);
        Assertions.assertThat( searchLogin.getId() ).isNull();
    }

    @Test
    @DisplayName("비밀번호 찾기 성공")
    public void searchPwdSucTest() throws Exception{
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setEmail("admin@naver.com");
        loginVO.setPasswordHint("인하사대부속고등학교");

        Boolean searchYn = egovLoginService.searchPassword(loginVO);
        Assertions.assertThat( searchYn ).isTrue();
    }

    @Test
    @DisplayName("비밀번호 찾기 실패")
    public void searchPwdFailTest() throws Exception{
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setEmail("admin@naver.com");
        loginVO.setPasswordHint("인하사대부속중학교");

        Boolean searchYn = egovLoginService.searchPassword(loginVO);
        Assertions.assertThat( searchYn ).isFalse();
    }
}
