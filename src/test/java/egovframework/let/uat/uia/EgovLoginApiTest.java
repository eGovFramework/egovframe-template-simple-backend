package egovframework.let.uat.uia;

import egovframework.com.cmm.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * fileName       : EgovLoginApiTest
 * author         : crlee
 * date           : 2023/05/06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/06        crlee       최초 생성
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EgovLoginApiTest {

    @Value("${server.port}")
    String PORT;

    @Value("${server.servlet.context-path}")
    String CONTEXT_PATH;

    String URL = "http://localhost";

    String SERVER_URL;

    @BeforeEach
    void init(){
        this.SERVER_URL = String.format("%s:%s%s", URL,PORT,CONTEXT_PATH);
    }

    @Test
    @DisplayName("로그인 성공")
    void actionLoginJWTSucTest(){
        // set Header
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        headers.set("headerTest", "headerValue");

        // set Body
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("1");
        loginVO.setUserSe("USR");
        HttpEntity<LoginVO> request = new HttpEntity<>(loginVO, headers);

        // send request
        TestRestTemplate rest = new TestRestTemplate();
        ResponseEntity<Map> response = rest.postForEntity(this.SERVER_URL+"/uat/uia/actionLoginJWT.do", request, Map.class);

        // verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getBody().get("resultMessage")).isEqualTo("성공 !!!");
    }

    @Test
    @DisplayName("로그인 실패")
    void actionLoginJWTFailTest(){
        // set Header
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        headers.set("headerTest", "headerValue");

        // set Body
        LoginVO loginVO = new LoginVO();
        loginVO.setId("admin");
        loginVO.setPassword("123");
        loginVO.setUserSe("USR");
        HttpEntity<LoginVO> request = new HttpEntity<>(loginVO, headers);

        // send request
        TestRestTemplate rest = new TestRestTemplate();
        ResponseEntity<Map> response = rest.postForEntity(this.SERVER_URL+"/uat/uia/actionLoginJWT.do", request, Map.class);

        // verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getBody().get("resultMessage")).isEqualTo(Locale.getDefault().toLanguageTag().equals("ko-KR") ? "로그인 정보가 올바르지 않습니다." : "login information is not correct");
    }

    @Test
    @DisplayName("로그아웃")
    void actionLogoutJSONTest(){
        // send request
        TestRestTemplate rest = new TestRestTemplate();
        ResponseEntity<Map> response = rest.getForEntity(this.SERVER_URL+"/uat/uia/actionLogoutAPI.do", Map.class);

        // verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("resultMessage")).isEqualTo( "성공했습니다.");
    }
}
