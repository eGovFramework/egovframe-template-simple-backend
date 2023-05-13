package egovframework.let.uat.uia.service;

import egovframework.com.cmm.LoginVO;
import egovframework.let.uat.uia.service.impl.EgovLoginDao;
import egovframework.let.uat.uia.service.impl.EgovLoginServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
/**
 * fileName       : EgovLoginServiceImplTest
 * author         : crlee
 * date           : 2023/05/10
 * description    : 로그인 서비스 테스트
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/10        crlee       최초 생성
 */
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
public class EgovLoginServiceImplTest {


    EgovLoginService egovLoginService;
    EgovLoginDao egovLoginDao;
    @BeforeAll
    void init(){
        this.egovLoginDao = Mockito.mock(EgovLoginDao.class);
        this.egovLoginService = new EgovLoginServiceImpl( egovLoginDao );
    }

    @Test
    @DisplayName("로그인 성공")
    public void actionLoginSucTest() throws Exception {
        //given
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("goodPwd");

        LoginVO expected = new LoginVO();
        expected.setId("admin");
        expected.setPassword("goodPwd");
        expected.setUserSe("USR");
        when(egovLoginDao.actionLogin(loginVO)).thenReturn(expected);

        //when
        LoginVO result = egovLoginService.actionLogin( loginVO );

        //then
        Assertions.assertThat( result.getId() ).isEqualTo("admin");
        Assertions.assertThat( result.getUserSe() ).isEqualTo("USR");
    }
    @Test
    @DisplayName("로그인 실패")
    public void actionLoginFailTest() throws Exception {
        //given
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("badPwd");
        when(egovLoginDao.actionLogin(loginVO)).thenReturn( LoginVO.builder()
                .id("")
                .password("")
                .build() );

        //when
        LoginVO result = egovLoginService.actionLogin( loginVO );

        //then
        Assertions.assertThat( result.getId() ).isNull();
        Assertions.assertThat( result.getUserSe() ).isNull();
    }

    @Test
    @DisplayName("아이디 찾기 성공")
    public void searchIdSucTest() throws Exception{
        LoginVO loginVO = new LoginVO();
        loginVO.setName("홍길동");
        loginVO.setEmail("korea@naver.com");
        when(egovLoginDao.searchId(loginVO)).thenReturn( LoginVO.builder()
                .id("helloHong123")
                .build() );
        LoginVO searchLogin = egovLoginService.searchId(loginVO);
        Assertions.assertThat( searchLogin.getId() ).isEqualTo("helloHong123");
    }
    @Test
    @DisplayName("아이디 찾기 실패")
    public void searchIdFailTest() throws Exception{

        //Given
        LoginVO loginVO = new LoginVO();
        loginVO.setName("홍길동");
        loginVO.setEmail("korea@kakao.com");
        when(egovLoginDao.searchId(loginVO)).thenReturn( LoginVO.builder()
                .id("")
                .build() );

        //When
        LoginVO searchLogin = egovLoginService.searchId(loginVO);

        //Then
        Assertions.assertThat( searchLogin.getId() ).isNull();
    }

    @Test
    @DisplayName("비밀번호 찾기 성공")
    public void searchPwdSucTest() throws Exception{
        //Given
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setEmail("admin@naver.com");
        loginVO.setPasswordHint("인하사대부속고등학교");

        when(egovLoginDao.searchPassword(loginVO)).thenReturn( LoginVO.builder()
                .password("qwer1234!")
                .build() );

        //When
        Boolean searchYn = egovLoginService.searchPassword(loginVO);

        //Then
        Assertions.assertThat( searchYn ).isTrue();
    }

    @Test
    @DisplayName("비밀번호 찾기 실패")
    public void searchPwdFailTest() throws Exception{

        //Given
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setEmail("admin@naver.com");
        loginVO.setPasswordHint("인하사대부속중학교");
        when(egovLoginDao.searchPassword(loginVO)).thenReturn( new LoginVO() );

        //When
        Boolean searchYn = egovLoginService.searchPassword(loginVO);

        //then
        Assertions.assertThat( searchYn ).isFalse();
    }
}
