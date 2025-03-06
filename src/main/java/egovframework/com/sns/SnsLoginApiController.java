package egovframework.com.sns;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.com.sns.SnsVO.NaverProfileVO;
import egovframework.com.sns.SnsVO.NaverResponseVO;
import egovframework.com.sns.SnsVO.NaverTokenVO;
import egovframework.com.sns.SnsVO.KakaoTokenVO;
import egovframework.com.sns.SnsVO.KakaoProfileVO;
import egovframework.com.sns.SnsVO.KakaoResponseVO;
import egovframework.let.uat.uia.service.EgovLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
/**
 * Sns 로그인을 처리하는 컨트롤러 클래스
 * @네이버로그인API명세 : https://developers.naver.com/docs/login/api/api.md
 * @네이버회원프로필조회API명세 : https://developers.naver.com/docs/login/profile/profile.md
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일      수정자      수정내용
 *  -------            --------        ---------------------------
 *  2024.08.15  김일국     최초 생성
 *
 *  </pre>
 */
@Slf4j
@RestController
@Tag(name="SnsLoginApiController",description = "Sns 로그인 관련")
public class SnsLoginApiController {
	
	/** EgovLoginService */
	@Resource(name = "loginService")
	private EgovLoginService loginService;
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	/** JWT */
	@Autowired
    private EgovJwtTokenUtil jwtTokenUtil;
	public static final String HEADER_STRING = "Authorization";
	/** SNS */
	public static final String NAVER_CLIENT_ID = EgovProperties.getProperty("Sns.naver.clientId");
	public static final String NAVER_CLIENT_SECRET = EgovProperties.getProperty("Sns.naver.clientSecret");
	public static final String NAVER_CALLBACK_URL = EgovProperties.getProperty("Sns.naver.callbackUrl");
	public static final String KAKAO_CLIENT_ID = EgovProperties.getProperty("Sns.kakao.clientId");
	public static final String KAKAO_CALLBACK_URL = EgovProperties.getProperty("Sns.kakao.callbackUrl");
	
	/**
	 * 카카오API 로그인
	 * @param HttpServletResponse
	 * @return void 
	 */
	@Operation(
			summary = "카카오API 로그인",
			description = "카카오API 로그인 처리",
			tags = {"SnsLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "인증 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@GetMapping("/login/kakao")
    public void getKakaoAuthUrl(HttpServletResponse response) throws IOException {
		String clientId = KAKAO_CLIENT_ID;//카카오 애플리케이션 클라이언트 아이디값";
		String redirectURI = URLEncoder.encode(KAKAO_CALLBACK_URL, "UTF-8");
	    String apiURL = "https://kauth.kakao.com/oauth/authorize?response_type=code";
	    apiURL += "&client_id=" + clientId;
	    apiURL += "&redirect_uri=" + redirectURI;
	    log.debug("apiURL="+ apiURL);
		response.sendRedirect(apiURL);
	}
	
	/**
	 * 카카오API 로그인 콜백
	 * @param HttpServletResponse, HttpServletRequest
	 * @return HashMap<String, Object> 
	 */
	@Operation(
			summary = "카카오API 로그인 콜백",
			description = "카카오API 로그인 콜백처리",
			tags = {"SnsLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "인증 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@GetMapping("/login/kakao/callback")
    public HashMap<String, Object> getKakaoAuthCallback(HttpServletResponse response, HttpServletRequest request) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//카카오 로그인 인증 시작
		String clientId = KAKAO_CLIENT_ID;//애플리케이션 클라이언트 아이디값";
	    String code = request.getParameter("code");
	    String redirectURI = URLEncoder.encode(KAKAO_CALLBACK_URL, "UTF-8");
	    String json_string=""; //3번사용
	    String responseBody="";//2번사용
	    Map<String, String> requestHeaders = new HashMap<>();
	    String apiURL;
	    apiURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&";
	    apiURL += "client_id=" + clientId;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&code=" + code;
	    log.debug("apiURL="+ apiURL);
        requestHeaders.put("Authorization", null);
        requestHeaders.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        requestHeaders.put("X-Http-Method-Override", "GET");
	    responseBody = SnsUtils.getOpenApi(apiURL,requestHeaders);
	    log.debug("responseBody="+ responseBody);
	    json_string = responseBody;//토큰 값 변수에 저장
	    //카카오 로그인 인증 끝
	    //카카오 프로필 정보 가져오기 시작 
	    ObjectMapper objectMapper = new ObjectMapper();
	    KakaoTokenVO jsonToken = objectMapper.readValue(json_string, KakaoTokenVO.class);
	    String token = jsonToken.getAccess_token(); // 카카오 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        String openApiURL = "https://kapi.kakao.com/v2/user/me";
        requestHeaders.put("Authorization", header);
        requestHeaders.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        requestHeaders.put("Authorization", header);
        requestHeaders.put("X-Http-Method-Override", "POST");
        responseBody = SnsUtils.getOpenApi(openApiURL,requestHeaders);
        log.debug("responseBody="+ responseBody);
        json_string = responseBody;//프로필 값 변수에 저장
        KakaoResponseVO jsonResponse = objectMapper.readValue(json_string, KakaoResponseVO.class);
        log.debug("jsonProfile="+ jsonResponse.getId() + jsonResponse.getProperties());
        //카카오 프로필 정보 가져오기 끝
        //로그인 권한 부여 시작
        if(jsonResponse.getId() != null && !jsonResponse.getId().isEmpty()) {
        	ObjectMapper mapper = new ObjectMapper();
        	KakaoProfileVO jsonProfile = mapper.convertValue(jsonResponse.getProperties(), KakaoProfileVO.class);
        	log.debug("jsonProfile="+ jsonProfile);
        	log.debug("jsonProfile.getName()="+ jsonProfile.getNickname());
        	LoginVO loginVO = new LoginVO();
            loginVO.setName(jsonProfile.getNickname());
            loginVO.setId(jsonResponse.getId());
            loginVO.setUniqId(jsonResponse.getId());
            loginVO.setUserSe("SNS");
            loginVO.setGroupNm("ROLE_USER");
            loginVO.setOrgnztId(jsonResponse.getId());
	        String jwtToken = jwtTokenUtil.generateToken(loginVO);
			String username = jwtTokenUtil.getUserSeFromToken(jwtToken);
	    	log.debug("Dec jwtToken username = "+username);
	    	String groupnm = jwtTokenUtil.getInfoFromToken("groupNm", jwtToken);
	    	log.debug("Dec jwtToken groupnm = "+groupnm);//생성한 토큰에서 스프링시큐리티용 그룹명값 출력
	    	//서버사이드 권한 체크 통과를 위해 삽입
	    	request.getSession().setAttribute("LoginVO", loginVO);
			resultMap.put("resultVO", loginVO);
			resultMap.put("jToken", jwtToken);
			resultMap.put("resultCode", "200");
			resultMap.put("resultMessage", "성공 !!!");
        }else {
        	resultMap.put("resultVO", null);
			resultMap.put("resultCode", "300");
			resultMap.put("resultMessage", egovMessageSource.getMessage("fail.common.login"));
        }
        //로그인 권한 부여 끝
		return resultMap;
	}
	
	/**
	 * 네이버API 로그인
	 * @param HttpServletResponse
	 * @return void 
	 */
	@Operation(
			summary = "네이버API 로그인",
			description = "네이버API 로그인 처리",
			tags = {"SnsLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "인증 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@GetMapping("/login/naver")
    public void getNaverAuthUrl(HttpServletResponse response) throws IOException {
		String clientId = NAVER_CLIENT_ID;//네이버 애플리케이션 클라이언트 아이디값";
		String redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL, "UTF-8");
	    SecureRandom random = new SecureRandom();
	    String state = new BigInteger(130, random).toString();
	    String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
	    apiURL += "&client_id=" + clientId;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&state=" + state;
	    log.debug("apiURL="+ apiURL);
		response.sendRedirect(apiURL);
	}
	
	/**
	 * 네이버API 로그인 콜백
	 * @param HttpServletResponse, HttpServletRequest
	 * @return HashMap<String, Object> 
	 */
	@Operation(
			summary = "네이버API 로그인 콜백",
			description = "네이버API 로그인 콜백처리",
			tags = {"SnsLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "인증 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@GetMapping("/login/naver/callback")
    public HashMap<String, Object> getNaverAuthCallback(HttpServletResponse response, HttpServletRequest request) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//네이버 로그인 인증 시작
		String clientId = NAVER_CLIENT_ID;//애플리케이션 클라이언트 아이디값";
	    String clientSecret = NAVER_CLIENT_SECRET;//애플리케이션 클라이언트 시크릿값";
	    String code = request.getParameter("code");
	    String state = request.getParameter("state");
	    String redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL, "UTF-8");
	    String json_string=""; //3번사용
	    String responseBody="";//2번사용
	    Map<String, String> requestHeaders = new HashMap<>();
	    String apiURL;
	    apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
	    apiURL += "client_id=" + clientId;
	    apiURL += "&client_secret=" + clientSecret;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&code=" + code;
	    apiURL += "&state=" + state;
	    log.debug("apiURL="+ apiURL);
        requestHeaders.put("Authorization", null);
        requestHeaders.put("X-Http-Method-Override", "GET");
	    responseBody = SnsUtils.getOpenApi(apiURL,requestHeaders);
	    log.debug("responseBody="+ responseBody);
	    json_string = responseBody;//토큰 값 변수에 저장
        //네이버 로그인 인증 끝
	    //네이버 프로필 정보 가져오기 시작 
	    ObjectMapper objectMapper = new ObjectMapper();
	    NaverTokenVO jsonToken = objectMapper.readValue(json_string, NaverTokenVO.class);
	    String token = jsonToken.getAccess_token(); // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        String openApiURL = "https://openapi.naver.com/v1/nid/me";
        requestHeaders.put("Authorization", header);
        requestHeaders.put("X-Http-Method-Override", "GET");
        responseBody = SnsUtils.getOpenApi(openApiURL,requestHeaders);
        log.debug("responseBody="+ responseBody);
        json_string = responseBody;//프로필 값 변수에 저장
        NaverResponseVO jsonResponse = objectMapper.readValue(json_string, NaverResponseVO.class);
        log.debug("jsonProfile="+ jsonResponse.getResultcode() + jsonResponse.getMessage());
        //네이버 프로필 정보 가져오기 끝
        //로그인 권한 부여 시작
        if(jsonResponse.getResultcode().equals("00")) {
        	ObjectMapper mapper = new ObjectMapper();
        	NaverProfileVO jsonProfile = mapper.convertValue(jsonResponse.getResponse(), NaverProfileVO.class);
        	log.debug("jsonProfile="+ jsonProfile);
        	log.debug("jsonProfile.getName()="+ jsonProfile.getName());
        	LoginVO loginVO = new LoginVO();
            loginVO.setName(jsonProfile.getName());
            loginVO.setId(jsonProfile.getEmail());
            loginVO.setUniqId(jsonProfile.getEmail());
            loginVO.setUserSe("SNS");
            loginVO.setGroupNm("ROLE_USER");
            loginVO.setOrgnztId(jsonProfile.getEmail());
	        String jwtToken = jwtTokenUtil.generateToken(loginVO);
			String username = jwtTokenUtil.getUserSeFromToken(jwtToken);
	    	log.debug("Dec jwtToken username = "+username);
	    	String groupnm = jwtTokenUtil.getInfoFromToken("groupNm", jwtToken);
	    	log.debug("Dec jwtToken groupnm = "+groupnm);//생성한 토큰에서 스프링시큐리티용 그룹명값 출력
	    	//서버사이드 권한 체크 통과를 위해 삽입
	    	request.getSession().setAttribute("LoginVO", loginVO);
			resultMap.put("resultVO", loginVO);
			resultMap.put("jToken", jwtToken);
			resultMap.put("resultCode", "200");
			resultMap.put("resultMessage", "성공 !!!");
        }else {
        	resultMap.put("resultVO", null);
			resultMap.put("resultCode", "300");
			resultMap.put("resultMessage", egovMessageSource.getMessage("fail.common.login"));
        }
        //로그인 권한 부여 끝
		return resultMap;
	}
}