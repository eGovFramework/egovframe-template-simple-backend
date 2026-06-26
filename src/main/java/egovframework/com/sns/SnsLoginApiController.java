package egovframework.com.sns;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
 *  2026.05.14            보안취약점 대응 (4.3.x)
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
	private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";

	@Value("${Globals.jwt.cookieSecure:false}")
	private boolean cookieSecure;

	// OAuth state: double-submit 쿠키로 "흐름을 시작한 브라우저"에 바인딩 (STATELESS 환경, TTL 10분)
	// 단순 서명/저장은 "발급 사실"만 증명할 뿐 브라우저를 묶지 못하므로, URL state == 쿠키 state 대조로 검증한다.
	private static final String OAUTH_STATE_COOKIE = "OAUTH_STATE";
	private static final long STATE_TTL_SEC = 10 * 60L;

	// state 생성 + 동일 값을 브라우저 쿠키로 발급 (double-submit). 백엔드/프론트 시작 흐름 공통.
	private String issueOAuthState(HttpServletResponse response) {
		String state = new BigInteger(130, new SecureRandom()).toString();
		ResponseCookie cookie = ResponseCookie.from(OAUTH_STATE_COOKIE, state)
				.httpOnly(true).secure(cookieSecure).sameSite("Lax")
				.path("/").maxAge(Duration.ofSeconds(STATE_TTL_SEC)).build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return state;
	}

	// 콜백 검증: URL state == 쿠키 state (상수시간 비교), 확인 즉시 쿠키 폐기(1회용)
	private boolean verifyAndConsumeOAuthState(HttpServletRequest request, HttpServletResponse response, String queryState) {
		String cookieState = null;
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (OAUTH_STATE_COOKIE.equals(ck.getName())) {
					cookieState = ck.getValue();
				}
			}
		}
		// 재사용 방지: 검증 즉시 쿠키 만료
		ResponseCookie del = ResponseCookie.from(OAUTH_STATE_COOKIE, "")
				.httpOnly(true).secure(cookieSecure).sameSite("Lax")
				.path("/").maxAge(0).build();
		response.addHeader(HttpHeaders.SET_COOKIE, del.toString());
		return queryState != null && cookieState != null
				&& MessageDigest.isEqual(queryState.getBytes(StandardCharsets.UTF_8),
						cookieState.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * OAuth state 발급 (SPA 직접 로그인 흐름용)
	 * state 값을 반환하면서 동일 값을 OAUTH_STATE 쿠키로 발급한다.
	 * @return HashMap (resultCode, state)
	 */
	@GetMapping("/auth/oauth-state")
	public HashMap<String, Object> getOAuthState(HttpServletResponse response) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", "200");
		resultMap.put("state", issueOAuthState(response));
		return resultMap;
	}

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
		String clientId = KAKAO_CLIENT_ID;
		String redirectURI = URLEncoder.encode(KAKAO_CALLBACK_URL, "UTF-8");
		String state = issueOAuthState(response);
	    String apiURL = "https://kauth.kakao.com/oauth/authorize?response_type=code";
	    apiURL += "&client_id=" + clientId;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&state=" + state;
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

		String code  = request.getParameter("code");
		String state = request.getParameter("state");

		// state 무조건 검증 — double-submit 쿠키(OAUTH_STATE)와 URL state 대조
		if (!verifyAndConsumeOAuthState(request, response, state)) {
			resultMap.put("resultCode", "401");
			resultMap.put("resultMessage", "OAuth state 검증 실패");
			return resultMap;
		}

		//카카오 로그인 인증 시작
		String clientId = KAKAO_CLIENT_ID;
	    String redirectURI = URLEncoder.encode(KAKAO_CALLBACK_URL, "UTF-8");
	    String json_string=""; //3번사용
	    String responseBody="";//2번사용
	    Map<String, String> requestHeaders = new HashMap<>();
	    String apiURL;
	    apiURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&";
	    apiURL += "client_id=" + clientId;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&code=" + code;
        requestHeaders.put("Authorization", null);
        requestHeaders.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        requestHeaders.put("X-Http-Method-Override", "GET");
	    responseBody = SnsUtils.getOpenApi(apiURL,requestHeaders);
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
        requestHeaders.put("X-Http-Method-Override", "POST");
        responseBody = SnsUtils.getOpenApi(openApiURL,requestHeaders);
        json_string = responseBody;//프로필 값 변수에 저장
        KakaoResponseVO jsonResponse = objectMapper.readValue(json_string, KakaoResponseVO.class);
        //카카오 프로필 정보 가져오기 끝
        //로그인 권한 부여 시작
        if(jsonResponse.getId() != null && !jsonResponse.getId().isEmpty()) {
        	ObjectMapper mapper = new ObjectMapper();
        	KakaoProfileVO jsonProfile = mapper.convertValue(jsonResponse.getProperties(), KakaoProfileVO.class);
        	LoginVO loginVO = new LoginVO();
            loginVO.setName(jsonProfile.getNickname());
            loginVO.setId(jsonResponse.getId());
            loginVO.setUniqId(jsonResponse.getId());
            loginVO.setUserSe("SNS");
            loginVO.setGroupNm("ROLE_USER");
            loginVO.setOrgnztId(jsonResponse.getId());
	        String jwtToken = jwtTokenUtil.generateToken(loginVO);
	        issueTokenCookie(response, jwtToken);

			HashMap<String, Object> userInfo = new HashMap<>();
			userInfo.put("id",     loginVO.getId());
			userInfo.put("name",   loginVO.getName());
			userInfo.put("userSe", loginVO.getUserSe());
			userInfo.put("uniqId", loginVO.getUniqId());
			resultMap.put("resultVO", userInfo);
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
		String clientId = NAVER_CLIENT_ID;
		String redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL, "UTF-8");
	    String state = issueOAuthState(response);
	    String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
	    apiURL += "&client_id=" + clientId;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&state=" + state;
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

		String code  = request.getParameter("code");
		String state = request.getParameter("state");

		// state 무조건 검증 — double-submit 쿠키(OAUTH_STATE)와 URL state 대조
		if (!verifyAndConsumeOAuthState(request, response, state)) {
			resultMap.put("resultCode", "401");
			resultMap.put("resultMessage", "OAuth state 검증 실패");
			return resultMap;
		}

		//네이버 로그인 인증 시작
		String clientId = NAVER_CLIENT_ID;
	    String clientSecret = NAVER_CLIENT_SECRET;
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
        requestHeaders.put("Authorization", null);
        requestHeaders.put("X-Http-Method-Override", "GET");
	    responseBody = SnsUtils.getOpenApi(apiURL,requestHeaders);
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
        json_string = responseBody;//프로필 값 변수에 저장
        NaverResponseVO jsonResponse = objectMapper.readValue(json_string, NaverResponseVO.class);
        //네이버 프로필 정보 가져오기 끝
        //로그인 권한 부여 시작
        if(jsonResponse.getResultcode().equals("00")) {
        	ObjectMapper mapper = new ObjectMapper();
        	NaverProfileVO jsonProfile = mapper.convertValue(jsonResponse.getResponse(), NaverProfileVO.class);
        	LoginVO loginVO = new LoginVO();
            loginVO.setName(jsonProfile.getName());
            loginVO.setId(jsonProfile.getEmail());
            loginVO.setUniqId(jsonProfile.getEmail());
            loginVO.setUserSe("SNS");
            loginVO.setGroupNm("ROLE_USER");
            loginVO.setOrgnztId(jsonProfile.getEmail());
	        String jwtToken = jwtTokenUtil.generateToken(loginVO);
	        issueTokenCookie(response, jwtToken);

			HashMap<String, Object> userInfo = new HashMap<>();
			userInfo.put("id",     loginVO.getId());
			userInfo.put("name",   loginVO.getName());
			userInfo.put("userSe", loginVO.getUserSe());
			userInfo.put("uniqId", loginVO.getUniqId());
			resultMap.put("resultVO", userInfo);
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

	private void issueTokenCookie(HttpServletResponse response, String jwtToken) {
		ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, jwtToken)
				.httpOnly(true)
				.secure(cookieSecure)
				.sameSite("Strict")
				.path("/")
				.maxAge(Duration.ofSeconds(EgovJwtTokenUtil.JWT_TOKEN_VALIDITY))
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
