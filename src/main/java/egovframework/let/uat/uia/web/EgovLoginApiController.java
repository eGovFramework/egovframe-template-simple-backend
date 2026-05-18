package egovframework.let.uat.uia.web;

import java.time.Duration;
import java.util.HashMap;

import org.egovframe.rte.fdl.cmmn.trace.LeaveaTrace;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.uat.uia.service.EgovLoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 기반 로그인을 처리하는 컨트롤러 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 2.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일      수정자      수정내용
 *  -------            --------        ---------------------------
 *  2009.03.06  박지욱     최초 생성
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *  2025.11.10             JWT 기반 로그인으로 전환, 세션 기반 로그인 제거
 *  2026.05.13             보안취약점 대응
 *
 *  </pre>
 */
@Slf4j
@RestController
@Tag(name="EgovLoginApiController",description = "로그인 관련")
public class EgovLoginApiController {

	/** EgovLoginService */
	@Resource(name = "loginService")
	private EgovLoginService loginService;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	/** JWT */
	@Autowired
    private EgovJwtTokenUtil jwtTokenUtil;

	@Value("${Globals.jwt.cookieSecure:false}")
	private boolean cookieSecure;

	private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";

	/**
	 * JWT 기반 로그인을 처리한다
	 * @param loginVO - 아이디, 비밀번호가 담긴 LoginVO
	 * @param request - HttpServletRequest
	 * @return HashMap - 로그인 결과(JWT 토큰, 사용자 정보)
	 * @exception Exception
	 */
	@Operation(
			summary = "JWT 로그인",
			description = "JWT 로그인 처리",
			tags = {"EgovLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "300", description = "로그인 실패")
	})
	@PostMapping(value = "/auth/login-jwt")
	public HashMap<String, Object> actionLoginJWT(@RequestBody LoginVO loginVO,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		LoginVO loginResultVO = loginService.actionLogin(loginVO);

		if (loginResultVO != null && loginResultVO.getId() != null && !loginResultVO.getId().equals("")) {
			if (loginResultVO.getGroupNm().equals("ROLE_ADMIN")) {
				loginResultVO.setUserSe("ADM");
			}
			String jwtToken = jwtTokenUtil.generateToken(loginResultVO);

			// JWT를 httpOnly Secure 쿠키로 발급 — JS에서 접근 불가
			ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, jwtToken)
					.httpOnly(true)
					.secure(cookieSecure)
					.sameSite("Strict")
					.path("/")
					.maxAge(Duration.ofSeconds(EgovJwtTokenUtil.JWT_TOKEN_VALIDITY))
					.build();
			response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

			// 응답 본문에는 표시·UI 분기용 최소 정보만 포함 (토큰 미포함)
			HashMap<String, Object> userInfo = new HashMap<>();
			userInfo.put("id",     loginResultVO.getId());
			userInfo.put("name",   loginResultVO.getName());
			userInfo.put("userSe", loginResultVO.getUserSe());
			userInfo.put("uniqId", loginResultVO.getUniqId()); // 게시물 작성자 본인 확인용 (비밀 정보 아님)

			resultMap.put("resultVO", userInfo);
			resultMap.put("resultCode", "200");
			resultMap.put("resultMessage", "성공 !!!");

		} else {
			resultMap.put("resultVO", loginResultVO);
			resultMap.put("resultCode", "300");
			resultMap.put("resultMessage", egovMessageSource.getMessage("fail.common.login"));
		}

		return resultMap;
	}

	/**
	 * 로그아웃한다. SecurityContext를 초기화한다.
	 * @return resultVO - 로그아웃 결과
	 * @exception Exception
	 */
	@Operation(
			summary = "로그아웃",
			description = "JWT 로그아웃 처리 (SecurityContext 초기화)",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
	})
	@GetMapping(value = "/auth/logout")
	public ResultVO actionLogoutJSON(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ResultVO resultVO = new ResultVO();

		// SecurityContext 초기화
		new SecurityContextLogoutHandler().logout(request, response, null);

		// ACCESS_TOKEN 쿠키 삭제
		ResponseCookie expiredCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
				.httpOnly(true)
				.secure(cookieSecure)
				.sameSite("Strict")
				.path("/")
				.maxAge(Duration.ZERO)
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 현재 인증된 사용자 정보 + 권한 목록을 반환한다.
	 * 프론트엔드 AuthContext가 앱 시작 시 호출하여 라우트 가드·메뉴 분기에 사용.
	 * 인증되지 않은 호출은 SecurityConfig에 의해 401 응답된다.
	 */
	@Operation(
			summary = "현재 사용자 정보",
			description = "인증된 사용자의 ID/이름/권한을 반환 (라우트 가드용)",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않음")
	})
	@GetMapping(value = "/auth/me")
	public HashMap<String, Object> getCurrentUser() {
		HashMap<String, Object> resultMap = new HashMap<>();

		// permitAll 로 익명 호출도 도달함 — anonymous 인 경우 principal 은 "anonymousUser" String.
		// LoginVO 가 아닌 케이스를 401 로 분기.
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof LoginVO user)
				|| user.getId() == null || user.getId().isEmpty()) {
			resultMap.put("resultCode", "401");
			resultMap.put("resultMessage", "not authenticated");
			return resultMap;
		}

		List<String> roles = auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		resultMap.put("resultVO", Map.of(
				"id",     user.getId(),
				"name",   user.getName() == null ? "" : user.getName(),
				"userSe", user.getUserSe() == null ? "" : user.getUserSe(),
				"uniqId", user.getUniqId() == null ? "" : user.getUniqId(),
				"roles",  roles
		));
		resultMap.put("resultCode", "200");
		resultMap.put("resultMessage", "성공");
		return resultMap;
	}
}