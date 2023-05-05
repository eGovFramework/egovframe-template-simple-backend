package egovframework.let.uat.uia;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.uat.uia.service.EgovLoginService;
import egovframework.let.uat.uia.web.EgovLoginApiController;
import egovframework.let.utl.sim.service.EgovFileScrty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * fileName       : EgovLoginApiControllerTest
 * author         : crlee
 * date           : 2023/05/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/06        crlee       최초 생성
 */
@SpringBootTest
@TestInstance(TestInstance. Lifecycle.PER_CLASS)
public class EgovLoginApiControllerTest {

    @Autowired
    EgovLoginApiController egovLoginApiController;
    @Autowired
    EgovMessageSource egovMessageSource;

    @BeforeAll
    void init(){
        this.egovLoginApiController.setLoginService(  new MockLoginService() );
    }

    @Test
    @DisplayName("actionLoginJWT() 성공")
    void actionLoginJWTSuccessTest() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("GoodPwd");
        loginVO.setUserSe("USR");

        MockHttpServletRequest request = new MockHttpServletRequest();

        HashMap<String, Object> result = egovLoginApiController.actionLoginJWT(loginVO , request,null);
        assertThat(result.get("resultCode")).isEqualTo(HttpStatus.OK);
        assertThat(result.get("resultMessage")).isEqualTo("성공 !!!");
    }
    @Test
    @DisplayName("actionLoginJWT() 실패")
    void actionLoginJWTFailTest() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("badPwd");
        loginVO.setUserSe("USR");

        MockHttpServletRequest request = new MockHttpServletRequest();

        HashMap<String, Object> result = egovLoginApiController.actionLoginJWT(loginVO , request,null);
        assertThat(result.get("resultCode")).isEqualTo(HttpStatus.MULTIPLE_CHOICES);
        assertThat(result.get("resultMessage")).isEqualTo( egovMessageSource.getMessage("fail.common.login"));
    }
    private class MockLoginService implements EgovLoginService {

        @Override
        public LoginVO actionLogin(LoginVO vo) throws Exception {
            if( vo.getPassword().equals("badPwd")){
                return null;
            }
            String enpassword = EgovFileScrty.encryptPassword(vo.getPassword(), vo.getId());
            vo.setPassword(enpassword);
            return vo;
        }

        @Override
        public LoginVO searchId(LoginVO vo) throws Exception {
            return vo;
        }

        @Override
        public boolean searchPassword(LoginVO vo) throws Exception {
            return true;
        }
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logOutTest() throws Exception {
        ResultVO resultVO = egovLoginApiController.actionLogoutJSON();
        Assertions.assertThat(resultVO.getResultCode()).isEqualTo( ResponseCode.SUCCESS.getCode() );
        Assertions.assertThat(resultVO.getResultMessage()).isEqualTo( ResponseCode.SUCCESS.getMessage() );
    }
}
