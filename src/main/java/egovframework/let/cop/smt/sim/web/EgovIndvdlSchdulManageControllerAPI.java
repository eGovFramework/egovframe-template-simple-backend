package egovframework.let.cop.smt.sim.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.property.EgovPropertyService;

/**
 * 일정관리를 처리하는 Controller Class 구현
 * @since 2009.04.10
 * @see
 * <pre>
 * << 개정이력(Modification Information) >>
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2009.04.10  장동한          최초 생성
 *  2011.05.31  JJY           경량환경 커스터마이징버전 생성
 * </pre>
 * @author 조재영
 * @version 1.0
 * @created 09-6-2011 오전 10:08:04
 */
@Controller
public class EgovIndvdlSchdulManageControllerAPI {

	@Autowired
	private DefaultBeanValidator beanValidator;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "egovIndvdlSchdulManageService")
	private EgovIndvdlSchdulManageService egovIndvdlSchdulManageService;

	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	// 첨부파일 관련
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileMngService;

	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;

	/**
	 * 일정(월별) 목록을 조회한다.
	 * @param searchVO
	 * @param commandMap
	 * @param indvdlSchdulManageVO
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageMonthList"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageMonthListAPI.do")
	@ResponseBody
	public ResultVO EgovIndvdlSchdulManageMonthList(
		@RequestBody Map<String, Object> commandMap) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!EgovUserDetailsHelper.isAuthenticated()) {
			return handleAuthError(resultVO); // server-side 권한 확인
		}

		//일정구분 검색 유지
		resultMap.put("searchKeyword",
			commandMap.get("searchKeyword") == null ? "" : (String)commandMap.get("searchKeyword"));
		resultMap.put("searchCondition",
			commandMap.get("searchCondition") == null ? "" : (String)commandMap.get("searchCondition"));

		Calendar cal = Calendar.getInstance();

		String sYear = String.valueOf(commandMap.get("year"));
		String sMonth = String.valueOf(commandMap.get("month"));

		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH);
		//int iDate = cal.get(java.util.Calendar.DATE);

		//검색 설정
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

		//공통코드 일정종류
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", cmmUseService.selectCmmCodeDetail(voComCode));

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
	 * 일정를 등록 폼
	 * @param searchVO
	 * @param commandMap
	 * @param indvdlSchdulManageVO
	 * @param bindingResult
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageRegist"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageRegistAPI.do")
	@ResponseBody
	public ResultVO IndvdlSchdulManageRegist(
		@ModelAttribute("indvdlSchdulManageVO") IndvdlSchdulManageVO indvdlSchdulManageVO)
		throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!EgovUserDetailsHelper.isAuthenticated()) {
			return handleAuthError(resultVO); // server-side 권한 확인
		}

		String sLocationUrl = "/cop/smt/sim/EgovIndvdlSchdulManageRegist";

		//공통코드  중요도 조회
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM019");
		resultMap.put("schdulIpcrCode", cmmUseService.selectCmmCodeDetail(voComCode));
		//공통코드  일정구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", cmmUseService.selectCmmCodeDetail(voComCode));
		//공통코드  반복구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM031");
		resultMap.put("reptitSeCode", cmmUseService.selectCmmCodeDetail(voComCode));

		//일정시작일자(시)
		resultMap.put("schdulBgndeHH", getTimeHH());
		//일정시작일자(분)
		resultMap.put("schdulBgndeMM", getTimeMM());
		//일정종료일자(시)
		resultMap.put("schdulEnddeHH", getTimeHH());
		//일정정료일자(분)
		resultMap.put("schdulEnddeMM", getTimeMM());

		//팝업정보창 사용하여 셋팅하지 않고 고정하여 설정함(템플릿에서 기능 축소)
		indvdlSchdulManageVO.setSchdulDeptName("관리자부서");
		indvdlSchdulManageVO.setSchdulDeptId("ORGNZT_0000000000000");
		indvdlSchdulManageVO.setSchdulChargerName("관리자");
		indvdlSchdulManageVO.setSchdulChargerId("USRCNFRM_00000000000");

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정를 등록 처리 한다.
	 * @param multiRequest
	 * @param searchVO
	 * @param commandMap
	 * @param indvdlSchdulManageVO
	 * @param bindingResult
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageRegistActor"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageRegistActorAPI.do")
	@ResponseBody
	public ResultVO IndvdlSchdulManageRegistActor(
		final MultipartHttpServletRequest multiRequest,
		IndvdlSchdulManageVO indvdlSchdulManageVO,
		BindingResult bindingResult
	//		ModelMap model,
	) throws Exception {

		ResultVO resultVO = new ResultVO();

		if (!EgovUserDetailsHelper.isAuthenticated()) {
			return handleAuthError(resultVO); // server-side 권한 확인
		}

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

		//서버  validate 체크
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
			_result = fileUtil.parseFileInf(files, "DSCH_", 0, "", "");
			_atchFileId = fileMngService.insertFileInfs(_result); //파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
		}

		// 리턴받은 첨부파일ID를 셋팅한다..
		indvdlSchdulManageVO.setAtchFileId(_atchFileId); // 첨부파일 ID

		//아이디 설정
		indvdlSchdulManageVO.setFrstRegisterId(user.getUniqId());
		indvdlSchdulManageVO.setLastUpdusrId(user.getUniqId());

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
	 * 일정 목록을 상세조회 조회한다.
	 * @param searchVO
	 * @param indvdlSchdulManageVO
	 * @param commandMap
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageDetail"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageDetailAPI.do")
	@ResponseBody
	public ResultVO EgovIndvdlSchdulManageDetail(
		@RequestBody Map<String, Object> commandMap)
		throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		LoginVO user = new LoginVO();
		if (EgovUserDetailsHelper.isAuthenticated()) {
			user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		}

		IndvdlSchdulManageVO indvdlSchdulManageVO = new IndvdlSchdulManageVO();
		indvdlSchdulManageVO.setSchdulId((String)commandMap.get("schdulId"));

		//일정시작일자(시)
		resultMap.put("schdulBgndeHH", getTimeHH());
		//일정시작일자(분)
		resultMap.put("schdulBgndeMM", getTimeMM());
		//일정종료일자(시)
		resultMap.put("schdulEnddeHH", getTimeHH());
		//일정정료일자(분)
		resultMap.put("schdulEnddeMM", getTimeMM());

		//공통코드  중요도 조회
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM019");
		resultMap.put("schdulIpcrCode", cmmUseService.selectCmmCodeDetail(voComCode));
		//공통코드  일정구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", cmmUseService.selectCmmCodeDetail(voComCode));
		//공통코드  반복구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM031");
		resultMap.put("reptitSeCode", cmmUseService.selectCmmCodeDetail(voComCode));

		IndvdlSchdulManageVO scheduleDetail = egovIndvdlSchdulManageService
			.selectIndvdlSchdulManageDetail(indvdlSchdulManageVO);
		resultMap.put("scheduleDetail", scheduleDetail);
		Object testAtchFiledId = commandMap.get("atchFileId");
		System.out.println(testAtchFiledId);
		// 첨부파일 확인
		if (scheduleDetail.getAtchFileId() != null && !scheduleDetail.getAtchFileId().isEmpty()) {
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(scheduleDetail.getAtchFileId());
			List<FileVO> resultFiles = fileMngService.selectFileInfs(fileVO);
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
		 * @param searchVO
		 * @param indvdlSchdulManageVO
		 * @param commandMap
		 * @param model
		 * @return "/cop/smt/sim/EgovIndvdlSchdulManageDetail"
		 * @throws Exception
		 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageDeleteAPI.do")
	@ResponseBody
	public ResultVO EgovIndvdlSchdulManageDelete(
		@RequestBody Map<String, Object> commandMap)
		throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		IndvdlSchdulManageVO indvdlSchdulManageVO = new IndvdlSchdulManageVO();
		indvdlSchdulManageVO.setSchdulId((String)commandMap.get("schdulId"));

		if (EgovUserDetailsHelper.isAuthenticated()) {
			egovIndvdlSchdulManageService.deleteIndvdlSchdulManage(indvdlSchdulManageVO);//schdulId

			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		} else {
			resultVO.setResultCode(ResponseCode.AUTH_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.AUTH_ERROR.getMessage());
		}

		return resultVO;
	}

	/**
	 * 일정 수정 폼
	 * @param searchVO
	 * @param commandMap
	 * @param indvdlSchdulManageVO
	 * @param bindingResult
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageModify"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageModifyAPI.do")
	@ResponseBody
	public ResultVO IndvdlSchdulManageModify(
		@RequestBody Map<String, Object> commandMap,
		IndvdlSchdulManageVO indvdlSchdulManageVO,
		BindingResult bindingResult) throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!EgovUserDetailsHelper.isAuthenticated()) {
			return handleAuthError(resultVO); // server-side 권한 확인
		}

		//공통코드  중요도 조회
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM019");

		resultMap.put("schdulIpcrCode", cmmUseService.selectCmmCodeDetail(voComCode));
		//공통코드  일정구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");

		resultMap.put("schdulSe", cmmUseService.selectCmmCodeDetail(voComCode));
		//공통코드  반복구분 조회
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM031");

		resultMap.put("reptitSeCode", cmmUseService.selectCmmCodeDetail(voComCode));

		//일정시작일자(시)
		resultMap.put("schdulBgndeHH", getTimeHH());
		//일정시작일자(분)
		resultMap.put("schdulBgndeMM", getTimeMM());
		//일정종료일자(시)
		resultMap.put("schdulEnddeHH", getTimeHH());
		//일정정료일자(분)
		resultMap.put("schdulEnddeMM", getTimeMM());

		IndvdlSchdulManageVO resultIndvdlSchdulManageVOReuslt = egovIndvdlSchdulManageService
			.selectIndvdlSchdulManageDetailVO(indvdlSchdulManageVO);

		String sSchdulBgnde = resultIndvdlSchdulManageVOReuslt.getSchdulBgnde();
		String sSchdulEndde = resultIndvdlSchdulManageVOReuslt.getSchdulEndde();

		resultIndvdlSchdulManageVOReuslt.setSchdulBgndeYYYMMDD(
			sSchdulBgnde.substring(0, 4) + "-" + sSchdulBgnde.substring(4, 6) + "-" + sSchdulBgnde.substring(6, 8));
		resultIndvdlSchdulManageVOReuslt.setSchdulBgndeHH(sSchdulBgnde.substring(8, 10));
		resultIndvdlSchdulManageVOReuslt.setSchdulBgndeMM(sSchdulBgnde.substring(10, 12));

		resultIndvdlSchdulManageVOReuslt.setSchdulEnddeYYYMMDD(
			sSchdulEndde.substring(0, 4) + "-" + sSchdulEndde.substring(4, 6) + "-" + sSchdulEndde.substring(6, 8));
		resultIndvdlSchdulManageVOReuslt.setSchdulEnddeHH(sSchdulEndde.substring(8, 10));
		resultIndvdlSchdulManageVOReuslt.setSchdulEnddeMM(sSchdulEndde.substring(10, 12));

		//팝업정보창 사용하여 셋팅하지 않고 고정하여 설정함(템플릿에서 기능 축소)
		resultIndvdlSchdulManageVOReuslt.setSchdulDeptName("관리자부서");
		resultIndvdlSchdulManageVOReuslt.setSchdulChargerName("관리자");

		resultMap.put("indvdlSchdulManageVO", resultIndvdlSchdulManageVOReuslt);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정를 수정 처리 한다.
	 * @param multiRequest
	 * @param searchVO
	 * @param commandMap
	 * @param indvdlSchdulManageVO
	 * @param bindingResult
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageModifyActor"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageModifyActorAPI.do")
	@ResponseBody
	public ResultVO IndvdlSchdulManageModifyActor(
		final MultipartHttpServletRequest multiRequest,
		IndvdlSchdulManageVO indvdlSchdulManageVO,
		BindingResult bindingResult)
		throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!EgovUserDetailsHelper.isAuthenticated()) {
			return handleAuthError(resultVO); // server-side 권한 확인
		}

		//로그인 객체 선언
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

		//서버  validate 체크
		beanValidator.validate(indvdlSchdulManageVO, bindingResult);
		if (bindingResult.hasErrors()) {

			resultVO.setResult(resultMap);
			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

			return resultVO;
		}

		/* *****************************************************************
		// 아이디 설정
		****************************************************************** */
		indvdlSchdulManageVO.setFrstRegisterId(user.getUniqId());
		indvdlSchdulManageVO.setLastUpdusrId(user.getUniqId());
		/* *****************************************************************
		// 첨부파일 관련 ID 생성 start....
		****************************************************************** */
		String _atchFileId = indvdlSchdulManageVO.getAtchFileId();

		final Map<String, MultipartFile> files = multiRequest.getFileMap();

		if (!files.isEmpty()) {
			String attfileAT = (String)multiRequest.getAttribute("atchFileAt");
			String atchFileAt = multiRequest.getAttribute("atchFileAt") == null ? "" : (String)multiRequest.getAttribute("atchFileAt");
			if ("N".equals(atchFileAt)) {
				List<FileVO> _result = fileUtil.parseFileInf(files, "DSCH_", 0, _atchFileId, "");
				_atchFileId = fileMngService.insertFileInfs(_result);

				// 첨부파일 ID 셋팅
				indvdlSchdulManageVO.setAtchFileId(_atchFileId); // 첨부파일 ID

			} else {
				FileVO fvo = new FileVO();
				fvo.setAtchFileId(_atchFileId);
				int _cnt = fileMngService.getMaxFileSN(fvo);
				List<FileVO> _result = fileUtil.parseFileInf(files, "DSCH_", _cnt, _atchFileId, "");
				fileMngService.updateFileInfs(_result);
			}
		}

		/* *****************************************************************
		// 일정관리정보 업데이트 처리
		****************************************************************** */
		egovIndvdlSchdulManageService.updateIndvdlSchdulManage(indvdlSchdulManageVO);

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 일정(일별) 목록을 조회한다.
	 * @param searchVO
	 * @param commandMap
	 * @param indvdlSchdulManageVO
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageDailyList"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageDailyListAPI.do")
	@ResponseBody
	public ResultVO EgovIndvdlSchdulManageDailyList(
		@RequestBody Map<String, Object> commandMap)
		throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		//일정구분 검색 유지
		resultMap.put("searchKeyword",
			commandMap.get("searchKeyword") == null ? "" : (String)commandMap.get("searchKeyword"));
		resultMap.put("searchCondition",
			commandMap.get("searchCondition") == null ? "" : (String)commandMap.get("searchCondition"));

		//공통코드 일정종류
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", cmmUseService.selectCmmCodeDetail(voComCode));

		/* *****************************************************************
		// 캘런더 설정 로직
		****************************************************************** */
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
	 * @param searchVO
	 * @param commandMap
	 * @param indvdlSchdulManageVO
	 * @param model
	 * @return "/cop/smt/sim/EgovIndvdlSchdulManageWeekList"
	 * @throws Exception
	 */
	@RequestMapping(value = "/cop/smt/sim/egovIndvdlSchdulManageWeekListAPI.do")
	@ResponseBody
	public ResultVO EgovIndvdlSchdulManageWeekList(
		@RequestBody Map<String, Object> commandMap)
		throws Exception {

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		//일정구분 검색 유지
		resultMap.put("searchKeyword",
			commandMap.get("searchKeyword") == null ? "" : (String)commandMap.get("searchKeyword"));
		resultMap.put("searchCondition",
			commandMap.get("searchCondition") == null ? "" : (String)commandMap.get("searchCondition"));

		//공통코드 일정종류
		ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
		voComCode = new ComDefaultCodeVO();
		voComCode.setCodeId("COM030");
		resultMap.put("schdulSe", cmmUseService.selectCmmCodeDetail(voComCode));

		/* *****************************************************************
		// 캘런더 설정 로직
		****************************************************************** */
		Calendar calNow = Calendar.getInstance();
		Calendar calBefore = Calendar.getInstance();
		Calendar calNext = Calendar.getInstance();

		String strYear = String.valueOf(commandMap.get("year"));
		String strMonth = String.valueOf(commandMap.get("month"));
		String strWeek = String.valueOf(commandMap.get("weekOfMonth"));

		int iNowYear = calNow.get(Calendar.YEAR);
		int iNowMonth = calNow.get(Calendar.MONTH);
		int iNowDate = calNow.get(Calendar.DATE);
		int iNowWeek = 0;

		if (strYear != null) {
			iNowYear = Integer.parseInt(strYear);
			iNowMonth = Integer.parseInt(strMonth);
			iNowWeek = Integer.parseInt(strWeek);
		}

		//년도/월 셋팅
		calNow.set(iNowYear, iNowMonth, 1);
		calBefore.set(iNowYear, iNowMonth, 1);
		calNext.set(iNowYear, iNowMonth, 1);

		calBefore.add(Calendar.MONTH, -1);
		calNext.add(Calendar.MONTH, +1);

		//int startDay = calNow.getMinimum(Calendar.DATE);
		int endDay = calNow.getActualMaximum(Calendar.DAY_OF_MONTH);
		int startWeek = calNow.get(Calendar.DAY_OF_WEEK);

		ArrayList<ArrayList<String>> listWeekGrop = new ArrayList<ArrayList<String>>();
		ArrayList<String> listWeekDate = new ArrayList<String>();

		String sUseDate = "";

		calBefore.add(Calendar.DATE, calBefore.getActualMaximum(Calendar.DAY_OF_MONTH) - (startWeek - 1));
		for (int i = 1; i < startWeek; i++) {
			sUseDate = Integer.toString(calBefore.get(Calendar.YEAR));
			sUseDate += DateTypeIntForString(calBefore.get(Calendar.MONTH) + 1);
			sUseDate += DateTypeIntForString(calBefore.get(Calendar.DATE));

			listWeekDate.add(sUseDate);
			calBefore.add(Calendar.DATE, +1);
		}

		int iBetweenCount = startWeek;

		// 주별로 자른다. BETWEEN 구하기
		for (int i = 1; i <= endDay; i++) {
			sUseDate = Integer.toString(iNowYear);
			sUseDate += Integer.toString(iNowMonth + 1).length() == 1 ? "0" + Integer.toString(iNowMonth + 1)
				: Integer.toString(iNowMonth + 1);
			sUseDate += Integer.toString(i).length() == 1 ? "0" + Integer.toString(i) : Integer.toString(i);

			listWeekDate.add(sUseDate);

			if (iBetweenCount % 7 == 0) {
				listWeekGrop.add(listWeekDate);
				listWeekDate = new ArrayList<String>();

				if (strYear == null && i < iNowDate) {
					iNowWeek++;

				}
			}

			//미지막 7일 자동계산
			if (i == endDay) {

				for (int j = listWeekDate.size(); j < 7; j++) {
					String sUseNextDate = Integer.toString(calNext.get(Calendar.YEAR));
					sUseNextDate += DateTypeIntForString(calNext.get(Calendar.MONTH) + 1);
					sUseNextDate += DateTypeIntForString(calNext.get(Calendar.DATE));
					listWeekDate.add(sUseNextDate);
					calNext.add(Calendar.DATE, +1);
				}

				listWeekGrop.add(listWeekDate);
			}

			iBetweenCount++;
		}

		resultMap.put("year", iNowYear);
		resultMap.put("month", iNowMonth);
		resultMap.put("week", iNowWeek);

		resultMap.put("listWeekGrop", listWeekGrop);
		List<?> listWeek = listWeekGrop.get(iNowWeek);
		commandMap.put("searchMode", "WEEK");
		commandMap.put("schdulBgnde", listWeek.get(0));
		commandMap.put("schdulEndde", listWeek.get(listWeek.size() - 1));

		resultMap.put("resultList", egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(commandMap));

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

	/**
	 * 시간을 LIST를 반환한다.
	 * @return  List
	 * @throws
	 */
	private List<ComDefaultCodeVO> getTimeHH() {
		ArrayList<ComDefaultCodeVO> listHH = new ArrayList<ComDefaultCodeVO>();
		//HashMap hmHHMM;
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
	 * @return  List
	 * @throws
	 */
	private List<ComDefaultCodeVO> getTimeMM() {
		ArrayList<ComDefaultCodeVO> listMM = new ArrayList<ComDefaultCodeVO>();
		//HashMap hmHHMM;
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
	 * @return  String
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

	/**
	 * 운영자 권한을 확인한다.(로그인 여부를 확인한다.)
	 *
	 * @param boardMaster
	 * @throws EgovBizException
	 */
	protected boolean checkAuthority(ModelMap model) throws Exception {
		// 사용자권한 처리
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
			return false;
		} else {
			return true;
		}
	}

	private ResultVO handleAuthError(ResultVO resultVO) {
		resultVO.setResultCode(ResponseCode.AUTH_ERROR.getCode());
		resultVO.setResultMessage(ResponseCode.AUTH_ERROR.getMessage());
		return resultVO;
	}

}