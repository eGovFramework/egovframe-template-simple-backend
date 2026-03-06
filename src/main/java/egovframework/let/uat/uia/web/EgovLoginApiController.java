package egovframework.let.uat.uia.web;

import java.util.HashMap;

import org.egovframe.rte.fdl.cmmn.trace.LeaveaTrace;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
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
	public HashMap<String, Object> actionLoginJWT(@RequestBody LoginVO loginVO, HttpServletRequest request) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 1. JWT 로그인 처리
		LoginVO loginResultVO = loginService.actionLogin(loginVO);
		
		if (loginResultVO != null && loginResultVO.getId() != null && !loginResultVO.getId().equals("")) {
			if(loginResultVO.getGroupNm().equals("ROLE_ADMIN")) {//로그인 결과에서 스프링시큐리티용 그룹명값에 따른 권한부여
				loginResultVO.setUserSe("ADM");
	        }
			log.debug("===>>> loginResultVO.getUserSe() = "+loginResultVO.getUserSe());
			log.debug("===>>> loginResultVO.getId() = "+loginResultVO.getId());
			log.debug("===>>> loginResultVO.getPassword() = "+loginResultVO.getPassword());
			log.debug("===>>> loginResultVO.getGroupNm() = "+loginResultVO.getGroupNm());//로그인 결과에서 스프링시큐리티용 그룹명값 출력
			
			String jwtToken = jwtTokenUtil.generateToken(loginResultVO);
			
			String username = jwtTokenUtil.getUserSeFromToken(jwtToken);
	    	log.debug("Dec jwtToken username = "+username);
	    	String groupnm = jwtTokenUtil.getInfoFromToken("groupNm", jwtToken);
	    	log.debug("Dec jwtToken groupnm = "+groupnm);//생성한 토큰에서 스프링시큐리티용 그룹명값 출력
	    	// JWT 토큰을 클라이언트에 반환
	    	// 클라이언트는 이후 모든 요청의 Authorization 헤더에 JWT 토큰을 포함하여 전송
	    	// JwtAuthenticationFilter가 토큰을 검증하고 SecurityContext에 인증 정보를 설정
	    	
			resultMap.put("resultVO", loginResultVO);
			resultMap.put("jToken", jwtToken);
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

		new SecurityContextLogoutHandler().logout(request, response, null);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}
}