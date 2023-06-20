package egovframework.let.uat.uia.web;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EgovLoginApiContollerTest {

    @Value("${server.servlet.context-path}")
    String CONTEXT_PATH;
    String URL = "http://localhost";
    String SERVER_URL;

    @LocalServerPort
    int randomServerPort;

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
        Assertions.assertThat( result.getStatusCode() ).isEqualTo( HttpStatus.UNAUTHORIZED );
        Assertions.assertThat( result.getBody().getResultCode() ).isEqualTo( ResponseCode.AUTH_ERROR.getCode() );
        Assertions.assertThat( result.getBody().getResultMessage() ).isEqualTo( ResponseCode.AUTH_ERROR.getMessage() );
    }
    String getToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> params = new HashMap<>();
        params.put("id","admin");
        params.put("password","1");
        params.put("userSe","USR");

        HttpEntity request = new HttpEntity(params,headers);
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<HashMap> res = rest.exchange(this.SERVER_URL + "/uat/uia/actionLoginJWT.do", HttpMethod.POST,request , HashMap.class);
        assertThat( res.getStatusCode() ).isEqualTo( HttpStatus.OK );

        HashMap<String,Object> body = (HashMap<String,Object>) res.getBody();
        assertThat( body.get("jToken") ).isNotNull();
        assertThat( body.get("resultCode") ).isEqualTo("200");
        assertThat( body.get("resultMessage") ).isEqualTo("성공 !!!");
        String token = body.get("jToken").toString();
        return token;
    }
    ResponseEntity<ResultVO> callApi(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity request = new HttpEntity(headers);
        TestRestTemplate rest = new TestRestTemplate();
         return rest.exchange(this.SERVER_URL + "/uat/esm/jwtAuthAPI.do", HttpMethod.POST, request,ResultVO.class);
    }
}
