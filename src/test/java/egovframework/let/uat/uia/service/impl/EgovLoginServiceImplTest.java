package egovframework.let.uat.uia.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import egovframework.com.cmm.LoginVO;

/**
 * 일반 로그인 서비스 단위 테스트
 * @author 공통서비스 개발팀
 * @since 2026.05.28
 */
@ExtendWith(MockitoExtension.class)
class EgovLoginServiceImplTest {

    @InjectMocks
    private EgovLoginServiceImpl loginService;

    @Mock
    private LoginDAO loginDAO;

    private LoginVO sampleLoginVO;

    @BeforeEach
    void setUp() {
        sampleLoginVO = new LoginVO();
        sampleLoginVO.setId("testUser");
        sampleLoginVO.setPassword("plainPassword");
        sampleLoginVO.setUserSe("USR");
        sampleLoginVO.setName("홍길동");
        sampleLoginVO.setEmail("test@example.com");
    }

    @Test
    @DisplayName("로그인 성공 - DB 조회 결과가 있으면 해당 VO를 반환한다")
    void actionLogin_성공_로그인결과반환() throws Exception {
        // given
        LoginVO dbResult = new LoginVO();
        dbResult.setId("testUser");
        dbResult.setPassword("encryptedPassword");
        given(loginDAO.actionLogin(any(LoginVO.class))).willReturn(dbResult);

        // when
        LoginVO result = loginService.actionLogin(sampleLoginVO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("testUser");
        assertThat(result.getPassword()).isEqualTo("encryptedPassword");
    }

    @Test
    @DisplayName("로그인 실패 - DB 조회 결과가 없으면 빈 VO를 반환한다")
    void actionLogin_실패_빈VO반환() throws Exception {
        // given
        given(loginDAO.actionLogin(any(LoginVO.class))).willReturn(null);

        // when
        LoginVO result = loginService.actionLogin(sampleLoginVO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getPassword()).isNull();
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 암호화되어 DAO에 전달된다")
    void actionLogin_비밀번호암호화_DAO호출() throws Exception {
        // given
        given(loginDAO.actionLogin(any(LoginVO.class))).willReturn(null);
        String originalPassword = sampleLoginVO.getPassword();

        // when
        loginService.actionLogin(sampleLoginVO);

        // then
        assertThat(sampleLoginVO.getPassword()).isNotEqualTo(originalPassword);
        assertThat(sampleLoginVO.getPassword()).isNotBlank();
    }

    @Test
    @DisplayName("아이디 찾기 - DB 조회 결과가 있으면 해당 VO를 반환한다")
    void searchId_성공_ID반환() throws Exception {
        // given
        LoginVO dbResult = new LoginVO();
        dbResult.setId("testUser");
        given(loginDAO.searchId(any(LoginVO.class))).willReturn(dbResult);

        // when
        LoginVO result = loginService.searchId(sampleLoginVO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("아이디 찾기 - DB 조회 결과가 없으면 빈 VO를 반환한다")
    void searchId_실패_빈VO반환() throws Exception {
        // given
        given(loginDAO.searchId(any(LoginVO.class))).willReturn(null);

        // when
        LoginVO result = loginService.searchId(sampleLoginVO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
    }

    @Test
    @DisplayName("비밀번호 찾기 - 사용자 정보가 일치하면 임시 비밀번호를 발급하고 true를 반환한다")
    void searchPassword_성공_임시비밀번호발급() throws Exception {
        // given
        LoginVO dbResult = new LoginVO();
        dbResult.setId("testUser");
        dbResult.setPassword("storedPassword");
        given(loginDAO.searchPassword(any(LoginVO.class))).willReturn(dbResult);

        // when
        boolean result = loginService.searchPassword(sampleLoginVO);

        // then
        assertThat(result).isTrue();
        then(loginDAO).should().updatePassword(any(LoginVO.class));
    }

    @Test
    @DisplayName("비밀번호 찾기 - 사용자 정보가 일치하지 않으면 false를 반환하고 DB를 갱신하지 않는다")
    void searchPassword_실패_false반환() throws Exception {
        // given
        given(loginDAO.searchPassword(any(LoginVO.class))).willReturn(null);

        // when
        boolean result = loginService.searchPassword(sampleLoginVO);

        // then
        assertThat(result).isFalse();
        then(loginDAO).should(never()).updatePassword(any(LoginVO.class));
    }
}
