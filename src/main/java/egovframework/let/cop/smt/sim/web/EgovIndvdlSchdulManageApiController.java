package egovframework.let.cop.smt.sim.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.web.EgovFileDownloadController;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 일정관리를 처리하는 Controller Class 구현
 * 
 * @since 2009.04.10
 * @see
 * 
 *      <pre>
 * << 개정이력(Modification Information) >>
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  장동한          최초 생성
 *   2011.05.31  JJY           경량환경 커스터마이징버전 생성
 *   2024.08.27  이백행          컨트리뷰션 롬복 생성자 기반 종속성 주입
 *      </pre>
 * 
 * @author 조재영
 * @version 1.0
 * @created 09-6-2011 오전 10:08:04
 */
@RestController
@Tag(name = "EgovIndvdlSchdulManageApiController", description = "일정관리")
@RequiredArgsConstructor
public class EgovIndvdlSchdulManageApiController {

	/**
	 * 유효성 검사기
	 */
	private final DefaultBeanValidator beanValidator;

	/**
	 * 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기 위한 서비스 인터페이스
	 */
	private final EgovIndvdlSchdulManageService egovIndvdlSchdulManageService;

	/**
	 * 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기 위한 서비스 인터페이스
	 */
	private final EgovCmmUseService egovCmmUseService;

	/**
	 * 파일정보의 관리를 위한 서비스 인터페이스
	 */
	private final EgovFileMngService egovFileMngService;

	/**
	 * 파일정보의 관리를 위한 유틸리티
	 */
	private final EgovFileMngUtil egovFileMngUtil;

	/**
	 * 암호화서비스
	 */
	private final EgovCryptoService egovCryptoService;

	/**
	 * 일정(월별) 목록을 조회한다.
	 * 
	 * @param request
	 * @param commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "월별 일정 조회", description = "일정(월별) 목록을 조회", tags = { "EgovIndvdlSchdulManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@GetMapping(value = "/schedule/month")
	public ResultVO EgovIndvdlSchdulManageMonthList(HttpServletRequest request,
			@Parameter(in = ParameterIn.QUERY, schema = @Schema(type = "object", additionalProperties = Schema.AdditionalPropertiesValue.TRUE, ref = "#/components/schemas/searchSchdulMap"), style = ParameterStyle.FORM, explode = Explode.TRUE) @RequestParam Map<String, Object> commandMap,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 일정구분 검색 유지
		resultMap.put("searchKeyword",
				commandMap.get("searchKeyword") == null ? "" : (String) commandMap.get("searchKeyword"));
		resultMap.put("searchCondition",
				commandMap.get("searchCondition") == null ? "" : (String) commandMap.get("searchCondition"));

		Calendar cal = Calendar.getInstance();

		String sYear = String.valueOf(commandMap.get("year"));
		String sMonth = String.valueOf(commandMap.get("month"));

		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH);
		// int iDate = cal.get(java.util.Calendar.DATE);

		// 검색 설정
		String sSearchDate = "";
		if (sYear == null || sMonth == null) {
			sSearchDate += Integer.toString(iYear);
			sSearchDate += Integer.toString(iMonth + 1).length() == 1 ? "0" + Integer.toString(iMonth + 1)
					: Integer.toString(iMonth + 1);
		} else {
			iYear = Integer.parseInt(sYear);
			iMonth = Integer.parseInt(sMonth);
			sSearchDate += sYear;
			sSearchDate += Integer.toString(iMonth + 1).length() == 1 ? "0" + Integer.toString(iMonth + 1)
					: Integer.toString(iMonth + 1);
		}

		// 공통코드 일정종류
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", egovCmmUseService.selectCmmCodeDetail(voComCode));

		commandMap.put("searchMonth", sSearchDate);
		commandMap.put("searchMode", "MONTH");
		resultMap.put("resultList", egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(commandMap));

		resultMap.put("prevRequest", commandMap);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정를 등록 처리 한다.
	 * 
	 * @param request
	 * @param multiRequest
	 * @param indvdlSchdulManageVO
	 * @param bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "일정 등록", description = "일정을 등록 처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovIndvdlSchdulManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류") })
	@PostMapping(value = "/schedule")
	public ResultVO IndvdlSchdulManageRegistActor(HttpServletRequest request,
			final MultipartHttpServletRequest multiRequest, IndvdlSchdulManageVO indvdlSchdulManageVO,
			BindingResult bindingResult, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO)
			throws Exception {

		ResultVO resultVO = new ResultVO();

		// 서버 validate 체크
		beanValidator.validate(indvdlSchdulManageVO, bindingResult);
		if (bindingResult.hasErrors()) {

			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());
			return resultVO;
		}

		// 첨부파일 관련 첨부파일ID 생성
		List<FileVO> _result = null;
		String _atchFileId = "";

		final Map<String, MultipartFile> files = multiRequest.getFileMap();

		if (!files.isEmpty()) {
			_result = egovFileMngUtil.parseFileInf(files, "DSCH_", 0, "", "");
			_atchFileId = egovFileMngService.insertFileInfs(_result); // 파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
		}

		// 리턴받은 첨부파일ID를 셋팅한다..
		indvdlSchdulManageVO.setAtchFileId(_atchFileId); // 첨부파일 ID

		// 아이디 설정
		indvdlSchdulManageVO.setFrstRegisterId(loginVO.getUniqId());
		indvdlSchdulManageVO.setLastUpdusrId(loginVO.getUniqId());

		indvdlSchdulManageVO.setSchdulDeptName("관리자부서");
		indvdlSchdulManageVO.setSchdulDeptId("ORGNZT_0000000000000");
		indvdlSchdulManageVO.setSchdulChargerName("관리자");
		indvdlSchdulManageVO.setSchdulChargerId("USRCNFRM_00000000000");
		egovIndvdlSchdulManageService.insertIndvdlSchdulManage(indvdlSchdulManageVO);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;

	}

	/**
	 * 일정 목록을 상세조회한다.
	 * 
	 * @param commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "일정 상세조회", description = "일정 목록을 상세조회", tags = { "EgovIndvdlSchdulManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공") })
	@GetMapping(value = "/schedule/{schdulId}")
	public ResultVO EgovIndvdlSchdulManageDetail(
			@Parameter(name = "schdulId", description = "일정 Id", in = ParameterIn.PATH, example = "SCHDUL_0000000000001") @PathVariable("schdulId") String schdulId,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		IndvdlSchdulManageVO indvdlSchdulManageVO = new IndvdlSchdulManageVO();
		indvdlSchdulManageVO.setSchdulId(schdulId);

		// 일정시작일자(시)
		resultMap.put("schdulBgndeHH", getTimeHH());
		// 일정시작일자(분)
		resultMap.put("schdulBgndeMM", getTimeMM());
		// 일정종료일자(시)
		resultMap.put("schdulEnddeHH", getTimeHH());
		// 일정정료일자(분)
		resultMap.put("schdulEnddeMM", getTimeMM());

		// 공통코드 중요도 조회
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM019");
		resultMap.put("schdulIpcrCode", egovCmmUseService.selectCmmCodeDetail(voComCode));
		// 공통코드 일정구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", egovCmmUseService.selectCmmCodeDetail(voComCode));
		// 공통코드 반복구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM031");
		resultMap.put("reptitSeCode", egovCmmUseService.selectCmmCodeDetail(voComCode));

		IndvdlSchdulManageVO scheduleDetail = egovIndvdlSchdulManageService
				.selectIndvdlSchdulManageDetail(indvdlSchdulManageVO);
		resultMap.put("scheduleDetail", scheduleDetail);

		// 첨부파일 확인
		if (scheduleDetail.getAtchFileId() != null && !scheduleDetail.getAtchFileId().isEmpty()) {
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(scheduleDetail.getAtchFileId());
			List<FileVO> resultFiles = egovFileMngService.selectFileInfs(fileVO);

			// FileId를 유추하지 못하도록 암호화하여 표시한다. (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
			for (FileVO file : resultFiles) {
				String toEncrypt = file.atchFileId;
				file.setAtchFileId(Base64.getEncoder().encodeToString(
						egovCryptoService.encrypt(toEncrypt.getBytes(), EgovFileDownloadController.ALGORITM_KEY)));
			}

			resultMap.put("resultFiles", resultFiles);
		}
		resultMap.put("user", user);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정을 삭제한다..
	 * 
	 * @param request
	 * @param schdulId
	 * @return ResultVO
	 * @throws Exception
	 */
	@Operation(summary = "일정 삭제", description = "일정을 삭제 처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovIndvdlSchdulManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@DeleteMapping(value = "/schedule/{schdulId}")
	public ResultVO EgovIndvdlSchdulManageDelete(
			@Parameter(name = "schdulId", description = "일정 Id", in = ParameterIn.PATH, example = "SCHDUL_0000000000001") @PathVariable("schdulId") String schdulId)
			throws Exception {

		ResultVO resultVO = new ResultVO();

		IndvdlSchdulManageVO indvdlSchdulManageVO = new IndvdlSchdulManageVO();
		indvdlSchdulManageVO.setSchdulId(schdulId);

		egovIndvdlSchdulManageService.deleteIndvdlSchdulManage(indvdlSchdulManageVO);// schdulId

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정를 수정 처리 한다.
	 * 
	 * @param multiRequest
	 * @param indvdlSchdulManageVO
	 * @param bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "일정 수정", description = "일정을 수정 처리", security = {
			@SecurityRequirement(name = "Authorization") }, tags = { "EgovIndvdlSchdulManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류") })
	@PutMapping(value = "/schedule/{schdulId}")
	public ResultVO IndvdlSchdulManageModifyActor(final MultipartHttpServletRequest multiRequest,
			IndvdlSchdulManageVO indvdlSchdulManageVO, BindingResult bindingResult,
			@Parameter(name = "schdulId", description = "일정 Id", in = ParameterIn.PATH, example = "SCHDUL_0000000000001") @PathVariable("schdulId") String schdulId,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 서버 validate 체크
		indvdlSchdulManageVO.setSchdulId(schdulId);
		beanValidator.validate(indvdlSchdulManageVO, bindingResult);
		if (bindingResult.hasErrors()) {

			resultVO.setResult(resultMap);
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());

			return resultVO;
		}

		/*
		 * ***************************************************************** // 아이디 설정
		 */
		indvdlSchdulManageVO.setFrstRegisterId(user.getUniqId());
		indvdlSchdulManageVO.setLastUpdusrId(user.getUniqId());
		/*
		 * ***************************************************************** // 첨부파일 관련
		 * ID 생성 start....
		 */
		String _atchFileId = indvdlSchdulManageVO.getAtchFileId();

		final Map<String, MultipartFile> files = multiRequest.getFileMap();

		if (!files.isEmpty()) {
			String atchFileAt = multiRequest.getAttribute("atchFileAt") == null ? ""
					: (String) multiRequest.getAttribute("atchFileAt");
			if ("N".equals(atchFileAt) || _atchFileId.equals("") || _atchFileId.equals("undefined")) {
				// 기존 첨부 파일이 존재하지 않는 경우
				List<FileVO> _result = egovFileMngUtil.parseFileInf(files, "DSCH_", 0, _atchFileId, "");
				_atchFileId = egovFileMngService.insertFileInfs(_result);

				// 첨부파일 ID 셋팅
				indvdlSchdulManageVO.setAtchFileId(_atchFileId); // 첨부파일 ID

			} else {
				// 기존 첨부 파일이 하나라도 존재하는 경우
				FileVO fvo = new FileVO();
				fvo.setAtchFileId(_atchFileId);
				int _cnt = egovFileMngService.getMaxFileSN(fvo);
				List<FileVO> _result = egovFileMngUtil.parseFileInf(files, "DSCH_", _cnt, _atchFileId, "");
				egovFileMngService.updateFileInfs(_result);
			}
		}

		/*
		 * ***************************************************************** // 일정관리정보
		 * 업데이트 처리
		 */
		egovIndvdlSchdulManageService.updateIndvdlSchdulManage(indvdlSchdulManageVO);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정(일별) 목록을 조회한다.
	 * 
	 * @param commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "일별 일정 조회", description = "일정(일별) 목록을 조회", tags = { "EgovIndvdlSchdulManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@GetMapping(value = "/schedule/daily")
	public ResultVO EgovIndvdlSchdulManageDailyList(
			@Parameter(in = ParameterIn.QUERY, schema = @Schema(type = "object", additionalProperties = Schema.AdditionalPropertiesValue.TRUE, ref = "#/components/schemas/searchSchdulMap"), style = ParameterStyle.FORM, explode = Explode.TRUE) @RequestParam Map<String, Object> commandMap)
			throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 일정구분 검색 유지
		resultMap.put("searchKeyword",
				commandMap.get("searchKeyword") == null ? "" : (String) commandMap.get("searchKeyword"));
		resultMap.put("searchCondition",
				commandMap.get("searchCondition") == null ? "" : (String) commandMap.get("searchCondition"));

		// 공통코드 일정종류
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", egovCmmUseService.selectCmmCodeDetail(voComCode));

		/*
		 * ***************************************************************** // 캘런더 설정
		 * 로직
		 */
		Calendar calNow = Calendar.getInstance();

		String strYear = String.valueOf(commandMap.get("year"));
		String strMonth = String.valueOf(commandMap.get("month"));
		String strDay = String.valueOf(commandMap.get("date"));
		String strSearchDay = "";
		int iNowYear = calNow.get(Calendar.YEAR);
		int iNowMonth = calNow.get(Calendar.MONTH);
		int iNowDay = calNow.get(Calendar.DATE);

		if (strYear != null) {
			iNowYear = Integer.parseInt(strYear);
			iNowMonth = Integer.parseInt(strMonth);
			iNowDay = Integer.parseInt(strDay);
		}

		strSearchDay = Integer.toString(iNowYear);
		strSearchDay += DateTypeIntForString(iNowMonth + 1);
		strSearchDay += DateTypeIntForString(iNowDay);

		commandMap.put("searchMode", "DAILY");
		commandMap.put("searchDay", strSearchDay);

		resultMap.put("year", iNowYear);
		resultMap.put("month", iNowMonth);
		resultMap.put("day", iNowDay);

		resultMap.put("resultList", egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(commandMap));

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정(주간별) 목록을 조회한다.
	 * 
	 * @param commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(summary = "주간별 일정 조회", description = "일정(주간별) 목록을 조회", tags = { "EgovIndvdlSchdulManageApiController" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님") })
	@GetMapping(value = "/schedule/week")
	public ResultVO EgovIndvdlSchdulManageWeekList(
			@Parameter(in = ParameterIn.QUERY, schema = @Schema(type = "object", additionalProperties = Schema.AdditionalPropertiesValue.TRUE, ref = "#/components/schemas/searchSchdulWeekMap"), style = ParameterStyle.FORM, explode = Explode.TRUE) @RequestParam Map<String, Object> commandMap)
			throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 일정구분 검색 유지
		resultMap.put("searchKeyword",
				commandMap.get("searchKeyword") == null ? "" : (String) commandMap.get("searchKeyword"));
		resultMap.put("searchCondition",
				commandMap.get("searchCondition") == null ? "" : (String) commandMap.get("searchCondition"));

		// 공통코드 일정종류
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", egovCmmUseService.selectCmmCodeDetail(voComCode));

		/*
		 * ***************************************************************** // 캘런더 설정
		 * 로직
		 */
		Calendar calNow = Calendar.getInstance();

		String strYear = String.valueOf(commandMap.get("year"));
		String strMonth = String.valueOf(commandMap.get("month"));
		String strDate = String.valueOf(commandMap.get("date"));

		int iNowMonth = calNow.get(Calendar.MONTH);

		if (strYear != null) {
			iNowMonth = Integer.parseInt(strMonth);
		}

		// 프론트에서 넘어온 값은 1월을 0으로 간주하므로 1달 더해 줌
		int realMonth = iNowMonth + 1;
		strMonth = String.valueOf(realMonth);

		// 자릿수 보정
		strMonth = (strMonth.length() == 1) ? "0" + strMonth : strMonth;
		strDate = (strDate.length() == 1) ? "0" + strDate : strDate;

		// 시작일자
		String schdulBgnde = strYear + strMonth + strDate;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calNext = Calendar.getInstance();

		calNext.set(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1, Integer.parseInt(strDate));

		calNext.add(Calendar.DATE, 6);

		// 종료일자
		String schdulEndde = dateFormat.format(calNext.getTime());

		commandMap.put("searchMode", "WEEK");

		commandMap.put("schdulBgnde", schdulBgnde);
		commandMap.put("schdulEndde", schdulEndde);

		resultMap.put("resultList", egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(commandMap));

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 시간을 LIST를 반환한다.
	 * 
	 * @return List
	 * @throws
	 */
	private List<ComDefaultCodeVO> getTimeHH() {
		ArrayList<ComDefaultCodeVO> listHH = new ArrayList<ComDefaultCodeVO>();
		// HashMap hmHHMM;
		for (int i = 0; i < 24; i++) {
			String sHH = "";
			String strI = String.valueOf(i);
			if (i < 10) {
				sHH = "0" + strI;
			} else {
				sHH = strI;
			}

			ComDefaultCodeVO codeVO = new ComDefaultCodeVO();
			codeVO.setCode(sHH);
			codeVO.setCodeNm(sHH);

			listHH.add(codeVO);
		}

		return listHH;
	}

	/**
	 * 분을 LIST를 반환한다.
	 * 
	 * @return List
	 * @throws
	 */
	private List<ComDefaultCodeVO> getTimeMM() {
		ArrayList<ComDefaultCodeVO> listMM = new ArrayList<ComDefaultCodeVO>();
		// HashMap hmHHMM;
		for (int i = 0; i < 60; i++) {

			String sMM = "";
			String strI = String.valueOf(i);
			if (i < 10) {
				sMM = "0" + strI;
			} else {
				sMM = strI;
			}

			ComDefaultCodeVO codeVO = new ComDefaultCodeVO();
			codeVO.setCode(sMM);
			codeVO.setCodeNm(sMM);

			listMM.add(codeVO);
		}
		return listMM;
	}

	/**
	 * 0을 붙여 반환
	 * 
	 * @return String
	 * @throws
	 */
	public String DateTypeIntForString(int iInput) {
		String sOutput = "";
		if (Integer.toString(iInput).length() == 1) {
			sOutput = "0" + Integer.toString(iInput);
		} else {
			sOutput = Integer.toString(iInput);
		}

		return sOutput;
	}

}