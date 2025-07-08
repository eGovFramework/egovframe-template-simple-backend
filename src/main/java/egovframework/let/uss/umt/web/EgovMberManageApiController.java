package egovframework.let.uss.umt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.cop.bbs.dto.request.BbsAttributeSearchRequestDTO;
import egovframework.let.uss.umt.service.EgovMberManageService;
import egovframework.let.uss.umt.service.MberManageVO;
import egovframework.let.uss.umt.service.UserDefaultVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 회원관련 요청을 비지니스 클래스로 전달하고 처리된 결과를 해당 웹 화면으로 전달하는 Controller를 정의한다
 * 
 * @author 공통서비스 개발팀 조재영
 * @since 2009.04.10
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  조재영          최초 생성
 *   2024.07.22  김일국          Boot 템플릿 커스터마이징버전 생성
 *
 *      </pre>
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "EgovMberManageApiController", description = "회원 관리")
public class EgovMberManageApiController {

	private EgovJwtTokenUtil jwtTokenUtil;
	public static final String HEADER_STRING = "Authorization";

	private final EgovMberManageService mberManageService;
	private final EgovCmmUseService cmmUseService;
	private final EgovPropertyService propertiesService;
	private final DefaultBeanValidator beanValidator;
	private final ResultVoHelper resultVoHelper;

	/**
	 * 관리자단에서 회원목록을 조회한다. (paging)
	 * 
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 목록조회화면", description = "관리자단에서 회원에 대한 목록을 조회", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value = "/members")
	public ResultVO selectMberList(
			@ModelAttribute BbsAttributeSearchRequestDTO boardMasterSearchVO,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user)
			throws Exception {

		MberManageVO userSearchVO = new MberManageVO();

		userSearchVO.setPageIndex(boardMasterSearchVO.getPageIndex());
		userSearchVO.setSearchCondition(boardMasterSearchVO.getSearchCnd());
		userSearchVO.setSearchKeyword(boardMasterSearchVO.getSearchWrd());

		/** EgovPropertyService */
		userSearchVO.setPageUnit(propertiesService.getInt("Globals.pageUnit"));
		userSearchVO.setPageSize(propertiesService.getInt("Globals.pageSize"));

		/** paging */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userSearchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(userSearchVO.getPageUnit());
		paginationInfo.setPageSize(userSearchVO.getPageSize());

		userSearchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		userSearchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		userSearchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<MberManageVO> resultList = mberManageService.selectMberList(userSearchVO);

		int totCnt = mberManageService.selectMberListTotCnt(userSearchVO);
		paginationInfo.setTotalRecordCount(totCnt);

		// 회원 상태코드를 코드정보로부터 조회
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM013");

		// 그룹정보를 조회 - GROUP_ID정보(스프링부트에서는 실제로 이 값만 사용한다.)
		vo.setTableNm("LETTNORGNZTINFO");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("paginationInfo", paginationInfo);
		resultMap.put("user", user);
		resultMap.put("entrprsMberSttus_result", cmmUseService.selectCmmCodeDetail(vo));
		resultMap.put("groupId_result", cmmUseService.selectGroupIdDetail(vo));
		/*
		 * 권한그룹이름 디버그
		 * List<CmmnDetailCode> groupId_result = cmmUseService.selectGroupIdDetail(vo);
		 * for(CmmnDetailCode result : groupId_result) {
		 * log.debug("===>>> getCode = "+result.getCode());
		 * log.debug("===>>> getCodeNm = "+result.getCodeNm());
		 * }
		 */
		resultMap.put("resultList", resultList);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 관리자단에서 회원등록화면으로 이동한다.
	 * 
	 * @param userSearchVO 검색조건정보
	 * @param mberManageVO 회원초기화정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 등록화면", description = "관리자단에서 회원등록화면에 필요한 값 생성", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/members/insert")
	public ResultVO insertMberView(UserDefaultVO userSearchVO, MberManageVO mberManageVO)
			throws Exception {

		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		resultMap.put("passwordHint_result", cmmUseService.selectCmmCodeDetail(vo));
		// 성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		resultMap.put("sexdstnCode_result", cmmUseService.selectCmmCodeDetail(vo));
		// 사용자상태코드를 코드정보로부터 조회
		vo.setCodeId("COM013");
		resultMap.put("mberSttus_result", cmmUseService.selectCmmCodeDetail(vo));
		// 그룹정보를 조회 - GROUP_ID정보(스프링부트에서는 실제로 이 값만 사용한다.)
		vo.setTableNm("LETTNORGNZTINFO");
		resultMap.put("groupId_result", cmmUseService.selectGroupIdDetail(vo));

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 관리자단에서 회원등록처리
	 * 
	 * @param mberManageVO  회원등록정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 등록처리", description = "관리자단에서 회원 등록처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping("/members/insert")
	public ResultVO insertMber(MberManageVO mberManageVO, BindingResult bindingResult) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		beanValidator.validate(mberManageVO, bindingResult);

		if (bindingResult.hasErrors()) {
			ComDefaultCodeVO vo = new ComDefaultCodeVO();

			// 패스워드힌트목록을 코드정보로부터 조회
			vo.setCodeId("COM022");
			resultMap.put("passwordHint_result", cmmUseService.selectCmmCodeDetail(vo));
			// 성별구분코드를 코드정보로부터 조회
			vo.setCodeId("COM014");
			resultMap.put("sexdstnCode_result", cmmUseService.selectCmmCodeDetail(vo));

			// 사용자상태코드를 코드정보로부터 조회
			vo.setCodeId("COM013");
			resultMap.put("mberSttus_result", cmmUseService.selectCmmCodeDetail(vo));

			// 그룹정보를 조회 - GROUP_ID정보
			vo.setTableNm("LETTNORGNZTINFO");
			resultMap.put("groupId_result", cmmUseService.selectGroupIdDetail(vo));

			resultMap.put("resultMsg", "fail.common.insert");
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.SAVE_ERROR);
		}

		mberManageService.insertMber(mberManageVO);

		// Exception 없이 진행시 등록 성공메시지
		resultMap.put("resultMsg", "success.common.insert");

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 관리자단에서 회원정보 수정을 위해 회원정보를 상세조회한다.
	 * 
	 * @param uniqId       상세조회대상 회원아이디
	 * @param userSearchVO 검색조건
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원정보 수정용 상세조회화면", description = "관리자단에서 회원정보 수정을 위해 회원정보를 상세조회", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/members/update/{uniqId}")
	public ResultVO updateMberView(@PathVariable("uniqId") String uniqId, UserDefaultVO userSearchVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		// 패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		resultMap.put("passwordHint_result", cmmUseService.selectCmmCodeDetail(vo));

		// 성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		resultMap.put("sexdstnCode_result", cmmUseService.selectCmmCodeDetail(vo));

		// 사용자상태코드를 코드정보로부터 조회
		vo.setCodeId("COM013");
		resultMap.put("mberSttus_result", cmmUseService.selectCmmCodeDetail(vo));

		// 그룹정보를 조회 - GROUP_ID정보
		vo.setTableNm("LETTNORGNZTINFO");
		resultMap.put("groupId_result", cmmUseService.selectGroupIdDetail(vo));

		MberManageVO mberManageVO = mberManageService.selectMber(uniqId);
		resultMap.put("mberManageVO", mberManageVO);
		resultMap.put("userSearchVO", userSearchVO);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 관리자단에서 회원수정 처리
	 * 
	 * @param mberManageVO  회원수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 수정처리", description = "관리자단에서 회원 수정처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping("/members/update")
	public ResultVO updateMber(@RequestBody MberManageVO mberManageVO, BindingResult bindingResult) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		beanValidator.validate(mberManageVO, bindingResult);

		if (bindingResult.hasErrors()) {
			ComDefaultCodeVO vo = new ComDefaultCodeVO();

			// 패스워드힌트목록을 코드정보로부터 조회
			vo.setCodeId("COM022");
			resultMap.put("passwordHint_result", cmmUseService.selectCmmCodeDetail(vo));

			// 성별구분코드를 코드정보로부터 조회
			vo.setCodeId("COM014");
			resultMap.put("sexdstnCode_result", cmmUseService.selectCmmCodeDetail(vo));

			// 사용자상태코드를 코드정보로부터 조회
			vo.setCodeId("COM013");
			resultMap.put("mberSttus_result", cmmUseService.selectCmmCodeDetail(vo));

			// 그룹정보를 조회 - GROUP_ID정보
			vo.setTableNm("LETTNORGNZTINFO");
			resultMap.put("groupId_result", cmmUseService.selectGroupIdDetail(vo));

			resultMap.put("resultMsg", "fail.common.insert");
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.SAVE_ERROR);
		}

		mberManageService.updateMber(mberManageVO);

		// Exception 없이 진행시 수정성공메시지
		resultMap.put("resultMsg", "success.common.update");
		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 관리자단에서 회원정보삭제.
	 * 
	 * @param checkedIdForDel 삭제대상 아이디 정보
	 * @param userSearchVO    검색조건정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "관리자단에서 회원 삭제처리", description = "관리자단에서 회원 삭제처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "삭제 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@DeleteMapping("/members/delete/{uniqId}")
	public ResultVO deleteMber(@PathVariable("uniqId") String uniqId, UserDefaultVO userSearchVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		mberManageService.deleteMber(uniqId);
		// Exception 없이 진행시 삭제성공메시지
		resultMap.put("resultMsg", "success.common.delete");
		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 사용자단에서 회원정보 수정을 위해 회원정보를 상세조회한다.
	 * 
	 * @param user 인증된 사용자 정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원정보 수정용 상세조회화면", description = "사용자단에서 회원정보 수정을 위해 회원정보를 상세조회", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/mypage")
	public ResultVO selectMypageView(@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (user == null || user.getId() == null) {
			resultMap.put("resultMsg", "회원 정보를 불러올 수 없습니다. 다시 로그인해주세요.");
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.AUTH_ERROR);
		}

		// 현재 로그인한 사용자의 정보를 조회
		MberManageVO result = mberManageService.selectMber(user.getUniqId());

		if (result == null) {
			resultMap.put("resultMsg", "회원 정보를 찾을 수 없습니다.");
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.SAVE_ERROR);
		}

		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		// 패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		resultMap.put("passwordHint_result", cmmUseService.selectCmmCodeDetail(vo));

		// 성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		resultMap.put("sexdstnCode_result", cmmUseService.selectCmmCodeDetail(vo));

		// 회원 정보 추가
		resultMap.put("mberManageVO", result);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 사용자단에서 회원 수정처리
	 * 
	 * @param mberManageVO  회원수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 수정처리", description = "사용자단에서 회원 수정처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping("/mypage/update")
	public ResultVO updateMypage(@RequestBody MberManageVO mberManageVO, BindingResult bindingResult) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		beanValidator.validate(mberManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			resultMap.put("resultMsg", "fail.common.insert");
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.SAVE_ERROR);
		}

		mberManageVO.setMberSttus("P");// 회원상태는 로그인가능상태로
		mberManageVO.setGroupId("GROUP_00000000000001");// 회원 권한그룹은 ROLE_USER상태로
		mberManageService.updateMber(mberManageVO);

		// Exception 없이 진행시 수정성공메시지
		resultMap.put("resultMsg", "success.common.update");

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 사용자단에서 회원탈퇴 처리
	 * 
	 * @param mberManageVO  회원수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 탈퇴처리", description = "사용자단에서 회원 탈퇴처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping("/mypage/delete")
	public ResultVO deleteMypage(@RequestBody MberManageVO mberManageVO, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		beanValidator.validate(mberManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			resultMap.put("resultMsg", "fail.common.insert");
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.SAVE_ERROR);
		}

		mberManageVO.setMberSttus("D");// 회원상태 삭제상태로
		mberManageService.updateMber(mberManageVO);// 회원상태 탈퇴 처리
		new SecurityContextLogoutHandler().logout(request, response, null);// 로그인 토큰값 지우기

		// Exception 없이 진행시 수정성공메시지
		resultMap.put("resultMsg", "success.common.update");
		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 사용자단에서 회원가입신청등록처리.
	 * 
	 * @param mberManageVO 회원가입신청정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 등록처리", description = "사용자단에서 회원 등록처리", tags = { "EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping("/etc/member_insert")
	public ResultVO sbscrbMber(MberManageVO mberManageVO, BindingResult bindingResult) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		beanValidator.validate(mberManageVO, bindingResult);

		if (bindingResult.hasErrors()) {
			resultMap.put("resultMsg", "fail.common.insert");
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.SAVE_ERROR);
		}

		mberManageVO.setMberSttus("P");// 회원상태는 로그인가능상태로
		mberManageVO.setGroupId("GROUP_00000000000001");// 회원 권한그룹은 ROLE_USER상태로
		// 회원가입신청 등록시 회원등록기능을 사용하여 등록한다.
		mberManageService.insertMber(mberManageVO);

		// Exception 없이 진행시 수정성공메시지
		resultMap.put("resultMsg", "success.common.insert");
		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 사용자단에서 회원가입신청(등록화면)으로 이동한다.
	 * 
	 * @param userSearchVO 검색조건
	 * @param mberManageVO 회원가입신청정보
	 * @param commandMap   파라메터전달용 commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 가입화면", description = "사용자단에서 회원가입화면에 필요한 값 생성", tags = {
			"EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
	})
	@GetMapping("/etc/member_insert")
	public ResultVO sbscrbMberView(UserDefaultVO userSearchVO, MberManageVO mberManageVO,
			@RequestParam Map<String, Object> commandMap) throws Exception {

		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		resultMap.put("passwordHint_result", cmmUseService.selectCmmCodeDetail(vo));

		// 성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		resultMap.put("sexdstnCode_result", cmmUseService.selectCmmCodeDetail(vo));

		if (!"".equals(commandMap.get("realname"))) {
			resultMap.put("mberNm", commandMap.get("realname")); // 실명인증된 이름 - 주민번호 인증
			resultMap.put("ihidnum", commandMap.get("ihidnum")); // 실명인증된 주민등록번호 - 주민번호 인증
		}
		if (!"".equals(commandMap.get("realName"))) {
			resultMap.put("mberNm", commandMap.get("realName")); // 실명인증된 이름 - ipin인증
		}

		mberManageVO.setGroupId("DEFAULT");
		mberManageVO.setMberSttus("DEFAULT");

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 회원 약관확인
	 * 
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자단에서 회원 약관확인", description = "사용자단에서 회원 약관확인에 필요한 값 생성", tags = {
			"EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
	})
	@GetMapping("/etc/member_agreement")
	public ResultVO sbscrbEntrprsMber() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 회원용 약관 아이디 설정
		String stplatId = "STPLAT_0000000000001";
		// 회원가입유형 설정-회원
		String sbscrbTy = "USR01";
		// 약관정보 조회
		resultMap.put("stplatList", mberManageService.selectStplat(stplatId));
		resultMap.put("sbscrbTy", sbscrbTy); // 회원가입유형 포함

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

	/**
	 * 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * 
	 * @param commandMap 파라메터전달용 commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "사용자아이디의 중복여부 체크처리", description = "사용자아이디의 중복여부 체크처리", tags = {
			"EgovMberManageApiController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/etc/member_checkid/{checkid}")
	public ResultVO checkIdDplct(@PathVariable("checkid") String checkId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		checkId = new String(checkId.getBytes("ISO-8859-1"), "UTF-8");

		if (checkId == null || checkId.equals("")) {
			return resultVoHelper.buildFromMap(resultMap, ResponseCode.INPUT_CHECK_ERROR);
		}

		int usedCnt = mberManageService.checkIdDplct(checkId);
		resultMap.put("usedCnt", usedCnt);
		resultMap.put("checkId", checkId);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}
}