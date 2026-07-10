package egovframework.let.uat.uia.web;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.utl.sim.service.EgovFileScrty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * fileName       : EgovLoginApiContollerTest
 * author         : crlee
 * date           : 2023/06/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/19        crlee       최초 생성
 */
@TestInstance(TestInstance. Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EgovLoginApiControllerTest {

    @Value("${server.servlet.context-path}")
    String CONTEXT_PATH;
    String URL = "http://localhost";

    @LocalServerPort
    int randomServerPort;
    String SERVER_URL;

    @BeforeAll
    void init(){
        this.SERVER_URL = String.format("%s:%s%s", URL,randomServerPort,CONTEXT_PATH);
    }


    @Test
    @DisplayName("인증 성공")
    void hasToken(){
        String token = getToken();
        ResponseEntity<ResultVO> result = callApi(token);
        Assertions.assertThat( result.getStatusCode() ).isEqualTo( HttpStatus.OK );
        Assertions.assertThat( result.getBody().getResultCode() ).isEqualTo( ResponseCode.SUCCESS.getCode() );
        Assertions.assertThat( result.getBody().getResultMessage() ).isEqualTo( ResponseCode.SUCCESS.getMessage() );
    }
    @Test
    @DisplayName("인증 실패 - Token null")
    void noToken(){
        ResponseEntity<ResultVO> result = callApi(null);
        Assertions.assertThat( result.getStatusCode() ).isEqualTo( HttpStatus.UNAUTHORIZED );
        Assertions.assertThat( result.getBody().getResultCode() ).isEqualTo( ResponseCode.AUTH_ERROR.getCode() );
        Assertions.assertThat( result.getBody().getResultMessage() ).isEqualTo( ResponseCode.AUTH_ERROR.getMessage() );
    }
    @Test
    @DisplayName("인증 실패 - Wrong Token")
    void wrongToken(){
        ResponseEntity<ResultVO> result = callApi("123123123123123T&*#$SDF123");
        // 토큰이 존재하나 위조·만료된 경우 JwtAuthenticationFilter 가 직접 401 body 를 내려준다.
        Assertions.assertThat( result.getStatusCode() ).isEqualTo( HttpStatus.UNAUTHORIZED );
        Assertions.assertThat( result.getBody().getResultCode() ).isEqualTo( 401 );
        Assertions.assertThat( result.getBody().getResultMessage() ).isEqualTo( "invalid or expired token" );
    }
    String getToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> params = new HashMap<>();
        params.put("id","admin");
        // 프론트엔드(passwordHash.js)와 동일하게 비번을 1차 SHA-256 해시하여 전송한다.
        // 서버는 encryptPassword 로 2차 해시하여 저장값(encryptPasswordTwice)과 비교한다.
        // (admin/Admin@1234 은 README·시드에 공개된 샘플 계정 — 운영 비밀 아님)
        params.put("password", clientHashedPassword("admin", "Admin@1234"));
        params.put("userSe","USR");

        HttpEntity request = new HttpEntity(params,headers);
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<HashMap> res = rest.exchange(this.SERVER_URL + "/auth/login-jwt", HttpMethod.POST,request , HashMap.class);
        assertThat( res.getStatusCode() ).isEqualTo( HttpStatus.OK );

        HashMap<String,Object> body = (HashMap<String,Object>) res.getBody();
        assertThat( body.get("resultCode") ).isEqualTo("200");
        assertThat( body.get("resultMessage") ).isEqualTo("성공 !!!");

        // JWT 는 응답 본문이 아니라 httpOnly ACCESS_TOKEN 쿠키로 발급된다. Set-Cookie 에서 추출.
        String token = extractAccessToken(res.getHeaders());
        assertThat( token ).isNotNull();
        return token;
    }

    private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";

    private String extractAccessToken(HttpHeaders responseHeaders){
        List<String> setCookies = responseHeaders.get(HttpHeaders.SET_COOKIE);
        if (setCookies == null) {
            return null;
        }
        for (String cookie : setCookies) {
            if (cookie.startsWith(ACCESS_TOKEN_COOKIE + "=")) {
                String value = cookie.substring((ACCESS_TOKEN_COOKIE + "=").length());
                int semi = value.indexOf(';');
                return semi >= 0 ? value.substring(0, semi) : value;
            }
        }
        return null;
    }

    private static String clientHashedPassword(String id, String rawPassword){
        try {
            // 프론트엔드 passwordHash.js 와 동일한 1차 해시 = EgovFileScrty.encryptPassword(raw, id)
            return EgovFileScrty.encryptPassword(rawPassword, id);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    ResponseEntity<ResultVO> callApi(String token){
        HttpHeaders headers = new HttpHeaders();
        // token 이 null 이면 헤더를 설정하지 않는다. (JDK HttpClient 는 null 헤더 값 설정 시 NPE)
        if (token != null) {
            headers.set("Authorization", token);
        }
        HttpEntity request = new HttpEntity(headers);
        TestRestTemplate rest = new TestRestTemplate();

         return rest.exchange(this.SERVER_URL + "/jwtAuthAPI", HttpMethod.POST, request,ResultVO.class);
    }
}
