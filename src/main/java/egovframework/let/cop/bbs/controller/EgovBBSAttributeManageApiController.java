package egovframework.let.cop.bbs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.IntermediateResultVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import egovframework.let.cop.bbs.dto.request.BbsInsertRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsUpdateRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsInsertResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsListResponseDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 게시판 속성관리를 위한 컨트롤러  클래스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자            수정내용
 *  -------    --------        ---------------------------
 *  2009.03.12  이삼섭           최초 생성
 *  2009.06.26	한성곤		    2단계 기능 추가 (댓글관리, 만족도조사)
 *  2011.08.31  JJY             경량환경 템플릿 커스터마이징버전 생성
 *  2024.04.06  김재섭(nirsa)     생성자 주입 전환, 불필요한 필드 제거, ResultVoHelper 적용 및 1차 코드 리팩토링
 *
 *  </pre>
 */
@RestController
@RequiredArgsConstructor
@Tag(name="EgovBBSAttributeManageApiController",description = "게시판 속성관리")
public class EgovBBSAttributeManageApiController {
	private final EgovBBSAttributeManageService bbsAttrbService;
	private final EgovCmmUseService cmmUseService;
	private final EgovPropertyService propertyService;
	private final DefaultBeanValidator beanValidator;
	private final ResultVoHelper resultVoHelper;

	/**
	 * 게시판 마스터 목록을 조회한다.
	 *
	 * @param request
	 * @param boardMasterVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 조회",
			description = "게시판 마스터 목록을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value = "/bbsMaster")
	public IntermediateResultVO<BbsListResponseDTO> selectBBSMasterInfs(@ModelAttribute BbsSearchRequestDTO bbsSearchRequestDTO)
		throws Exception {
		// 1. 페이지 정보 구성
		int pageUnit = propertyService.getInt("Globals.pageUnit");
		int pageSize = propertyService.getInt("Globals.pageSize");
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(bbsSearchRequestDTO.getPageIndex());
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		// 2. 서비스 호출 및 응답 객체 구성
		BbsListResponseDTO response = bbsAttrbService.selectBBSMasterInfs(bbsSearchRequestDTO, paginationInfo);
		paginationInfo.setTotalRecordCount(response.getResultCnt());
		response.setPaginationInfo(paginationInfo); 
		return IntermediateResultVO.success(response);
	}

	/**
	 * 게시판 마스터 상세내용을 조회한다.
	 *
	 * @param request
	 * @param searchVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 상세 조회",
			description = "게시판 마스터 상세내용을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value ="/bbsMaster/{bbsId}")
	public ResultVO  selectBBSMasterInf(@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA") 
			@PathVariable("bbsId") String bbsId)
		throws Exception {
		BoardMasterVO searchVO = new BoardMasterVO();
		searchVO.setBbsId(bbsId);
		searchVO.setUseAt("Y");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(searchVO);
		resultMap.put("boardMasterVO", vo);

		return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
		
//		* HashMap으로 인해 result 안에 boardMasterVO key가 하나 더 생겨서 들어가는 구조로써
//		  아래와 같이 변환 작업 시 리엑트에서 수정이 필요하므로 보류합니다.
		
//		BbsDetailResponseVO response = bbsAttrbService.selectBBSMasterInf(bbsId);
//
//		System.out.println(response.toString());
//		return IntermediateResultVO.success(response);
	}

	/**
	 * 신규 게시판 마스터 정보를 등록한다.
	 *
	 * @param request
	 * @param boardMasterVO
	 * @param bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 등록",
			description = "신규 게시판 마스터 정보를 등록",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping(value ="/bbsMaster")
	public IntermediateResultVO<BbsInsertResponseDTO> insertBBSMasterInf(@Valid @ParameterObject BbsInsertRequestDTO bbsInsertRequestDTO,
									   BindingResult bindingResult,
									   @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO
	)
		throws Exception {

		beanValidator.validate(bbsInsertRequestDTO, bindingResult);
		
		BbsInsertResponseDTO response = new BbsInsertResponseDTO();
		System.out.println(bbsInsertRequestDTO.getFrstRegisterId());
		
		if (bindingResult.hasErrors()) {
			ComDefaultCodeVO vo = new ComDefaultCodeVO();
			
			vo.setCodeId("COM004");
			List<CmmnDetailCode> codeResult = cmmUseService.selectCmmCodeDetail(vo);
			response.setTypeList(codeResult);
			
			vo.setCodeId("COM009");
			codeResult = cmmUseService.selectCmmCodeDetail(vo);
			response.setAttrbList(codeResult);
			
			return IntermediateResultVO.inputCheckError(response);
		}

		bbsInsertRequestDTO.setFrstRegisterId(loginVO.getUniqId());
		bbsInsertRequestDTO.setUseAt("Y");
		bbsInsertRequestDTO.setTrgetId("SYSTEMDEFAULT_REGIST");
		bbsInsertRequestDTO.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));

		bbsAttrbService.insertBBSMastetInf(bbsInsertRequestDTO);

		return IntermediateResultVO.success(response);
	}

	/**
	 * 게시판 마스터 정보를 수정한다.
	 *
	 * @param request
	 * @param bbsId
	 * @param boardMasterVO
	 * @param bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 수정",
			description = "게시판 마스터 정보를 수정",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "수정 성공",
			        content = @Content(
				            mediaType = "application/json",
				            examples = @ExampleObject(
				                name = "200 응답 예시",
				                summary = "Forbidden",
				                value = "{\n" +
				                        "  \"resultCode\": 200,\n" +
				                        "  \"resultMessage\": \"성공했습니다.\"\n" +
				                        "}"
				            )
				        )),
			@ApiResponse(
					responseCode = "403",
					description = "인가된 사용자가 아님",
			        content = @Content(
				            mediaType = "application/json",
				            examples = @ExampleObject(
				                name = "403 응답 예시",
				                summary = "Forbidden",
				                value = "{\n" +
				                        "  \"resultCode\": 403,\n" +
				                        "  \"resultMessage\": \"인가된 사용자가 아님\"\n" +
				                        "}"
				            )
				        )),
			@ApiResponse(
					responseCode = "900",
					description = "입력값 무결성 오류",
					content = @Content(
				            mediaType = "application/json",
				            examples = @ExampleObject(
				                name = "900 응답 예시",
				                summary = "Forbidden",
				                value = "{\n" +
				                        "  \"resultCode\": 900,\n" +
				                        "  \"resultMessage\": \"입력값 무결성 오류\"\n" +
				                        "}"
				            )
				        )),
				})
	@PutMapping(value ="/bbsMaster/{bbsId}")
	public IntermediateResultVO<Object> updateBBSMasterInf(@RequestBody BbsUpdateRequestDTO bbsUpdateRequestDTO,
										BindingResult bindingResult,
										@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO
										) throws Exception {
		beanValidator.validate(bbsUpdateRequestDTO, bindingResult);

		if (bindingResult.hasErrors()) {
			// selectBBSMasterInf 메서드는 리액트에서의 코드 수정이 필요하여 보류하였습니다.
			// 이로 인해 해당 라인의 코드 또한 보류되었습니다.
			BoardMaster boardMaster = bbsUpdateRequestDTO.toBoardMaster();
			BoardMasterVO vo = bbsAttrbService.selectBBSMasterInf(boardMaster);

			return IntermediateResultVO.inputCheckError(vo);
		}

		bbsUpdateRequestDTO.setLastUpdusrId(loginVO.getUniqId());
		bbsUpdateRequestDTO.setPosblAtchFileSize(propertyService.getString("posblAtchFileSize"));
		bbsAttrbService.updateBBSMasterInf(bbsUpdateRequestDTO);

		return IntermediateResultVO.success(null);
	}

	/**
	 * 게시판 마스터 정보를 삭제한다.
	 *
	 * @param request
	 * @param bbsId
	 * @param boardMasterVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 마스터 삭제",
			description = "게시판 마스터 정보를 삭제",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSAttributeManageApiController"}
	)
	@ApiResponses(value = {
		    @ApiResponse(
		        responseCode = "200",
		        description = "삭제 성공",
		        content = @Content(
			            mediaType = "application/json",
			            examples = @ExampleObject(
			                name = "200 응답 예시",
			                summary = "Forbidden",
			                value = "{\n" +
			                        "  \"resultCode\": 200,\n" +
			                        "  \"resultMessage\": \"성공했습니다.\"\n" +
			                        "}"
			            )
			        )),
		    @ApiResponse(
		        responseCode = "403",
		        description = "인가된 사용자가 아님",
		        content = @Content(
		            mediaType = "application/json",
		            examples = @ExampleObject(
		                name = "403 응답 예시",
		                summary = "Forbidden",
		                value = "{\n" +
		                        "  \"resultCode\": 403,\n" +
		                        "  \"resultMessage\": \"인가된 사용자가 아님\"\n" +
		                        "}"
		            )
		        ))
			}) 
	@PatchMapping(value ="/bbsMaster/{bbsId}")
	public IntermediateResultVO<Object> deleteBBSMasterInf(@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO,
		@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")
		@PathVariable("bbsId") String bbsId
		) throws Exception {
			bbsAttrbService.deleteBBSMasterInf(loginVO.getUniqId(), bbsId);

			return IntermediateResultVO.success(null);
	}

}
