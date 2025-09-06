package egovframework.let.uss.umt.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.uss.umt.service.MberManageVO;
import egovframework.let.uss.umt.service.UserDefaultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * 회원관련 요청을 비지니스 클래스로 전달하고 처리된 결과를 해당 웹 화면으로 전달하는 Controller 에 대한 API 를 분리합니다.
 *
 * @author
 * @since 2025.09.06
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2025.09.06  박준규		   최초 생성
 *
 *      </pre>
 */

@Tag(name = "EgovMberManageApi", description = "회원 관리")
public interface EgovMberManageApi {

	/**
	 * 관리자단에서 회원목록을 조회한다. (paging)
	 *
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 목록조회화면", description = "관리자단에서 회원에 대한 목록을 조회", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value = "/members")
	ResultVO selectMberList(@ModelAttribute BbsSearchRequestDTO boardMasterSearchVO,
		@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception;


	/**
	 * 관리자단에서 회원등록화면으로 이동한다.
	 *
	 * @param userSearchVO 검색조건정보
	 * @param mberManageVO 회원초기화정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 등록화면", description = "관리자단에서 회원등록화면에 필요한 값 생성", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/members/insert")
	ResultVO insertMberView(UserDefaultVO userSearchVO, MberManageVO mberManageVO) throws Exception;


	/**
	 * 관리자단에서 회원등록처리
	 *
	 * @param mberManageVO  회원등록정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 등록처리", description = "관리자단에서 회원 등록처리", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "등록 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
		@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping("/members/insert")
	ResultVO insertMber(MberManageVO mberManageVO, BindingResult bindingResult) throws Exception;

	/**
	 * 관리자단에서 회원정보 수정을 위해 회원정보를 상세조회한다.
	 *
	 * @param uniqId       상세조회대상 회원아이디
	 * @param userSearchVO 검색조건
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원정보 수정용 상세조회화면", description = "관리자단에서 회원정보 수정을 위해 회원정보를 상세조회", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/members/update/{uniqId}")
	ResultVO updateMberView(@PathVariable("uniqId") String uniqId, UserDefaultVO userSearchVO) throws Exception;

	/**
	 * 관리자단에서 회원수정 처리
	 *
	 * @param mberManageVO  회원수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 수정처리", description = "관리자단에서 회원 수정처리", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "등록 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
		@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping("/members/update")
	ResultVO updateMber(@RequestBody MberManageVO mberManageVO, BindingResult bindingResult) throws Exception;

	/**
	 * 관리자단에서 회원정보삭제.
	 *
	 * @param checkedIdForDel 삭제대상 아이디 정보
	 * @param userSearchVO    검색조건정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 삭제처리", description = "관리자단에서 회원 삭제처리", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "삭제 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
		@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@DeleteMapping("/members/delete/{uniqId}")
	ResultVO deleteMber(@PathVariable("uniqId") String uniqId, UserDefaultVO userSearchVO) throws Exception;

	/**
	 * 사용자단에서 회원정보 수정을 위해 회원정보를 상세조회한다.
	 *
	 * @param user 인증된 사용자 정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원정보 수정용 상세조회화면", description = "사용자단에서 회원정보 수정을 위해 회원정보를 상세조회", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/mypage")
	ResultVO selectMypageView(@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception;

	/**
	 * 사용자단에서 회원 수정처리
	 *
	 * @param mberManageVO  회원수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 수정처리", description = "사용자단에서 회원 수정처리", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "등록 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
		@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping("/mypage/update")
	ResultVO updateMypage(@RequestBody MberManageVO mberManageVO, BindingResult bindingResult) throws Exception;

	/**
	 * 사용자단에서 회원탈퇴 처리
	 *
	 * @param mberManageVO  회원수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 탈퇴처리", description = "사용자단에서 회원 탈퇴처리", security = {
		@SecurityRequirement(name = "Authorization") })
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "등록 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
		@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping("/mypage/delete")
	ResultVO deleteMypage(@RequestBody MberManageVO mberManageVO, BindingResult bindingResult,
		HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 사용자단에서 회원가입신청등록처리.
	 *
	 * @param mberManageVO 회원가입신청정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 등록처리", description = "사용자단에서 회원 등록처리")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "등록 성공"),
		@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping("/etc/member_insert")
	ResultVO sbscrbMber(MberManageVO mberManageVO, BindingResult bindingResult) throws Exception;

	/**
	 * 사용자단에서 회원가입신청(등록화면)으로 이동한다.
	 *
	 * @param userSearchVO 검색조건
	 * @param mberManageVO 회원가입신청정보
	 * @param commandMap   파라메터전달용 commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 가입화면", description = "사용자단에서 회원가입화면에 필요한 값 생성")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
	})
	@GetMapping("/etc/member_insert")
	public ResultVO sbscrbMberView(UserDefaultVO userSearchVO, MberManageVO mberManageVO,
		@RequestParam Map<String, Object> commandMap) throws Exception;

	/**
	 * 회원 약관확인
	 *
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 약관확인", description = "사용자단에서 회원 약관확인에 필요한 값 생성")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
	})
	@GetMapping("/etc/member_agreement")
	ResultVO sbscrbEntrprsMber() throws Exception;

	/**
	 * 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 *
	 * @param commandMap 파라메터전달용 commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자아이디의 중복여부 체크처리", description = "사용자아이디의 중복여부 체크처리")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/etc/member_checkid/{checkid}")
	ResultVO checkIdDplct(@PathVariable("checkid") String checkId) throws Exception;
}
