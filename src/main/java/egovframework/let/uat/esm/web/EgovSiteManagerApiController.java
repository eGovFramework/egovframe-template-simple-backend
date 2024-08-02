package egovframework.let.uat.esm.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.uat.esm.service.EgovSiteManagerService;
import egovframework.let.utl.sim.service.EgovFileScrty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 사이트관리자의 로그인 비밀번호를 변경 처리하는 컨트롤러 클래스
 * @author 공통서비스 개발팀
 * @since 2023.04.15
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일      수정자      수정내용
 *  -------            --------        ---------------------------
 *  2023.04.15  김일국     최초 생성
 *  2023.04.20  김일국     리액트에서 사용할 공통인증메서드 추가
 *	2024.07.17	김일국	 @RequestParam 에서 @RequestBody로 변경
 *  </pre>
 */
@Slf4j
@RestController
@Tag(name="EgovSiteManagerApiController",description = "사용자 관리")
public class EgovSiteManagerApiController {
	/** EgovSiteManagerService */
	@Resource(name = "siteManagerService")
	private EgovSiteManagerService siteManagerService;
	
	
	/**
	 * 리액트에서 사이트관리자에 접근하는 토큰값 위변조 방지용으로 서버에서 비교한다.
	 * @param map데이터: String old_password, new_password
	 * @param request - 토큰값으로 인증된 사용자를 확인하기 위한 HttpServletRequest
	 * @return result - JWT 토큰값 비교결과 코드와 메세지
	 * @exception Exception
	 */
	@Operation(
			summary = "토큰값 검증",
			description = "Headers에서 Authorization 속성값에 발급한 토큰값 검증",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovSiteManagerApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@PostMapping(value = "/jwtAuthAPI")
	public ResultVO jwtAuthentication(HttpServletRequest request) throws Exception {
		ResultVO resultVO = new ResultVO();

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		return resultVO;
	}
	/**
	 * 사이트관리자의 기존 비번과 비교하여 변경된 비밀번호를 저장한다.
	 * @param map데이터: String old_password, new_password
	 * @param request - 토큰값으로 인증된 사용자를 확인하기 위한 HttpServletRequest
	 * @return result - 수정결과
	 * @exception Exception
	 */
	@Operation(
			summary = "비밀번호 변경",
			description = "사이트관리자의 기존 비번과 비교하여 변경된 비밀번호를 저장",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovSiteManagerApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "800", description = "저장시 내부 오류")
	})
	@PatchMapping(value = "/admin/password")
	public ResultVO updateAdminPassword(
			@Parameter(
					schema = @Schema(type = "object",
					additionalProperties = Schema.AdditionalPropertiesValue.TRUE, 
					ref = "#/components/schemas/passwordMap"),
					style = ParameterStyle.FORM,
					explode = Explode.TRUE
					) @RequestBody Map<String, String> param, HttpServletRequest request, 
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception {
		ResultVO resultVO = new ResultVO();

		String old_password = param.get("old_password");
		String new_password = param.get("new_password");
		String login_id = user.getId();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("old_password", EgovFileScrty.encryptPassword(old_password, login_id));
		resultMap.put("new_password", EgovFileScrty.encryptPassword(new_password, login_id));
		resultMap.put("login_id", login_id);
		log.debug("===>>> loginVO.getId() = "+login_id);
		Integer result = siteManagerService.updateAdminPassword(resultMap); //저장성공 시 1, 실패 시 0 반환
		log.debug("===>>> result = "+result);
		if(result > 0) {
			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		}else{
			resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.SAVE_ERROR.getMessage());
		}

		return resultVO;
	}
}