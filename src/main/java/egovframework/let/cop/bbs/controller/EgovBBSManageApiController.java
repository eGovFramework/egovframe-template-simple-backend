package egovframework.let.cop.bbs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import egovframework.com.cmm.util.XssUtil;
import egovframework.let.cop.bbs.dto.request.BbsManageDetailBoardRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsManageUpdateRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageListResponseDTO;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.IntermediateResultVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsManageDeleteBoardRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsFileAtchResponseDTO;
import egovframework.let.cop.bbs.enums.BbsDetailRequestType;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.utl.sim.service.EgovFileScrty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 게시물 관리를 위한 컨트롤러 클래스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자             수정내용
 *  -------    --------        ---------------------------
 *  2009.03.19  이삼섭            최초 생성
 *  2009.06.29  한성곤	         2단계 기능 추가 (댓글관리, 만족도조사)
 *  2011.08.31  JJY              경량환경 템플릿 커스터마이징버전 생성
 *  2024.04.06  김재섭(nirsa)     생성자 주입 전환, 불필요한 필드 제거, ResultVoHelper 적용 및 1차 코드 리팩토링
 *
 *  </pre>
 */
@RestController
@RequiredArgsConstructor
@Tag(name="EgovBBSManageApiController",description = "게시물 관리")
public class EgovBBSManageApiController {
	private final XssUtil xssUtil; // 리펙토링 전 임시 사용
    private final EgovJwtTokenUtil jwtTokenUtil;
    private final EgovFileMngUtil fileUtil;
    private final ResultVoHelper resultVoHelper;
    private final EgovBBSManageService bbsMngService;
    private final EgovFileMngService fileMngService;
    private final EgovPropertyService propertyService;
    private final EgovBBSAttributeManageService bbsAttrbService;
    private final DefaultBeanValidator beanValidator;
	
	/**
	 * 게시판 마스터 상세내용을 조회한다.
	 * 파일 첨부 가능 여부 조회용
	 *
	 * @param bbsId
	 * @return BbsDetailResponse
	 * @throws Exception
	 */
	@Operation(
			summary = "게시판 파일 첨부 관련 정보 조회",
			description = "게시판의 파일 첨부가능 여부 및 첨부가능 파일 수 조회",
			tags = {"EgovBBSManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(
				    schema = @Schema(oneOf = {
							BbsFileAtchResponseDTO.class
				        })
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
	})
	@GetMapping(value = "/boardFileAtch/{bbsId}")
	public IntermediateResultVO<BbsFileAtchResponseDTO> selectUserBBSMasterInf(
			@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")
			@PathVariable("bbsId") String bbsId)
		throws Exception {
		
		BbsFileAtchResponseDTO response = bbsAttrbService.selectBBSMasterInf(bbsId, null, BbsDetailRequestType.FILE_ATCH);

		return IntermediateResultVO.success(response);
	}
	/**
	 * 게시물에 대한 목록을 조회한다.
	 *
	 * @param boardVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시물 목록 조회",
			description = "게시물에 대한 목록을 조회",
			tags = {"EgovBBSManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@GetMapping(value = "/board")
	public IntermediateResultVO<BbsManageListResponseDTO> selectBoardArticles(@ModelAttribute BbsSearchRequestDTO bbsSearchRequestDTO,
																				 @Parameter(hidden = true) @AuthenticationPrincipal LoginVO user)
		throws Exception {
		BbsFileAtchResponseDTO attributeDetailResponse = bbsAttrbService.selectBBSMasterInf(bbsSearchRequestDTO.getBbsId(), user.getUniqId(), BbsDetailRequestType.DETAIL);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(bbsSearchRequestDTO.getPageIndex());
		paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
		paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

		BbsManageListResponseDTO response = bbsMngService.selectBoardArticles(bbsSearchRequestDTO, paginationInfo, "");
		paginationInfo.setTotalRecordCount(response.getResultCnt());
		response.setPaginationInfo(paginationInfo);
		response.setUser(user);
		response.setBrdMstrVO(attributeDetailResponse);

		return IntermediateResultVO.success(response);
	}

	/**
	 * 게시물에 대한 상세 정보를 조회한다.
	 *
	 * @param boardVO
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시물 상세 조회",
			description = "게시물에 대한 상세 정보를 조회",
			tags = {"EgovBBSManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@GetMapping(value = "/board/{bbsId}/{nttId}")
	public IntermediateResultVO<BbsManageDetailResponseDTO> selectBoardArticle(
			@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")
			@PathVariable("bbsId") String bbsId,
			@Parameter(name = "nttId", description = "게시글 Id", in = ParameterIn.PATH, example="1")
			@PathVariable("nttId") String nttId,
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user)
		throws Exception {
		BbsManageDetailBoardRequestDTO bbsManageDetailBoardRequestDTO = BbsManageDetailBoardRequestDTO.builder()
				.bbsId(bbsId)
				.nttId(Long.parseLong(nttId))
				.plusCount(true)
				.lastUpdusrId(user.getUniqId())
				.build();
		
		//---------------------------------
		// 2009.06.29 : 2단계 기능 추가
		//---------------------------------
		if (bbsManageDetailBoardRequestDTO.getSubPageIndex() != null && !bbsManageDetailBoardRequestDTO.getSubPageIndex().isEmpty()) {
			bbsManageDetailBoardRequestDTO.setPlusCount(false);
		}
		////-------------------------------

		//----------------------------
		// template 처리 (기본 BBS template 지정  포함)
		//----------------------------
		BbsFileAtchResponseDTO bbsFileAtchResponseDTO = bbsAttrbService.selectBBSMasterInf(bbsManageDetailBoardRequestDTO.getBbsId(), user.getUniqId(), BbsDetailRequestType.LIST);

		BbsManageDetailResponseDTO bbsManageDetailResponseDTO = bbsMngService.selectBoardArticle(bbsManageDetailBoardRequestDTO)
				.toBuilder()
				.brdMstrVO(bbsFileAtchResponseDTO)
				.sessionUniqId(user.getUniqId())
				.user(user)
				.build();

		return IntermediateResultVO.success(bbsManageDetailResponseDTO);
	}

	/**
	 * 게시물에 대한 내용을 수정한다.
	 *
	 * @param boardVO
	 * @param multiRequest
	 * @param bindingResult
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시물 수정",
			description = "게시물에 대한 내용을 수정",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSManageApiController"}
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
	@PutMapping(value = "/board/{nttId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public IntermediateResultVO<Object> updateBoardArticle(
			@Parameter(name = "nttId", description = "게시글 Id", in = ParameterIn.PATH, example="1")
			@PathVariable Long nttId,
			@ModelAttribute BbsManageUpdateRequestDTO bbsManageUpdateRequestDTO,
			BindingResult bindingResult,
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
			HttpServletRequest request)
		throws Exception {
		bbsManageUpdateRequestDTO.setNttId(nttId);
		LoginVO user = jwtTokenUtil.extractUserFromJwt(request);

		beanValidator.validate(bbsManageUpdateRequestDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			return IntermediateResultVO.inputCheckError(null);
		}

		bbsMngService.updateBoardArticle(bbsManageUpdateRequestDTO, user, files);

		return IntermediateResultVO.success(null);
	}

	/**
	 * 게시물을 등록한다.
	 *
	 * @param multiRequest
	 * @param boardVO
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Operation(
			summary = "게시물 등록",
			description = "게시물을 등록",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping(value ="/board")
	public ResultVO insertBoardArticle(final MultipartHttpServletRequest multiRequest,
		@ModelAttribute BoardVO boardVO,
		BindingResult bindingResult,
		HttpServletRequest request)
		throws Exception {
		LoginVO user = jwtTokenUtil.extractUserFromJwt(request);
		
		beanValidator.validate(boardVO, bindingResult);
		if (bindingResult.hasErrors()) {

			return resultVoHelper.buildFromResultVO(new ResultVO(), ResponseCode.INPUT_CHECK_ERROR);
		}
	
		List<FileVO> result = null;
		String atchFileId = "";

		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		if (!files.isEmpty()) {
			result = fileUtil.parseFileInf(files, "BBS_", 0, "", "");
			atchFileId = fileMngService.insertFileInfs(result);
		}
		boardVO.setAtchFileId(atchFileId);
		boardVO.setFrstRegisterId(user.getUniqId());
		boardVO.setBbsId(boardVO.getBbsId());
		boardVO.setNtcrNm(user.getName()); //jwt토큰값으로 추가. dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		boardVO.setPassword(EgovFileScrty.encryptPassword("", user.getUniqId())); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		// board.setNttCn(unscript(board.getNttCn())); // XSS 방지

		bbsMngService.insertBoardArticle(boardVO);
		
		return resultVoHelper.buildFromMap(new HashMap<String, Object>(), ResponseCode.SUCCESS);
	}

	/**
	 * 게시물에 대한 답변을 등록한다.
	 *
	 * @param multiRequest
	 * @param boardVO
	 * @param bindingResult
	 * @param request 
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시물 답변 등록",
			description = "게시물에 대한 답변을 등록",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSManageApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping(value ="/boardReply")
	public ResultVO replyBoardArticle(final MultipartHttpServletRequest multiRequest,
		@ModelAttribute BoardVO boardVO,
		BindingResult bindingResult,
		HttpServletRequest request)
		throws Exception {
		ResultVO resultVO = new ResultVO();
		LoginVO user = jwtTokenUtil.extractUserFromJwt(request);

		beanValidator.validate(boardVO, bindingResult);
		if (bindingResult.hasErrors()) {
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());

			return resultVoHelper.buildFromResultVO(resultVO, ResponseCode.INPUT_CHECK_ERROR);
		}
		
		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		String atchFileId = "";

		if (!files.isEmpty()) {
			List<FileVO> result = fileUtil.parseFileInf(files, "BBS_", 0, "", "");
			atchFileId = fileMngService.insertFileInfs(result);
		}

		boardVO.setAtchFileId(atchFileId);
		boardVO.setReplyAt("Y");
		boardVO.setFrstRegisterId(user.getUniqId());
		boardVO.setBbsId(boardVO.getBbsId());
		boardVO.setParnts(Long.toString(boardVO.getNttId()));
		boardVO.setSortOrdr(boardVO.getSortOrdr());
		boardVO.setReplyLc(Integer.toString(Integer.parseInt(boardVO.getReplyLc()) + 1));

		boardVO.setNtcrNm(user.getName()); //jwt토큰값으로 추가. dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		boardVO.setPassword(EgovFileScrty.encryptPassword("", user.getUniqId())); // dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)

		boardVO.setNttCn(xssUtil.unscript(boardVO.getNttCn())); // XSS 방지

		bbsMngService.insertBoardArticle(boardVO);

		return resultVoHelper.buildFromMap(new HashMap<String, Object>(), ResponseCode.SUCCESS);
	}

	/**
	 * 게시물에 대한 내용을 삭제한다.
	 *
	 * @param boardVO
	 * @param nttId
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "게시물 삭제",
			description = "게시물에 대한 내용을 삭제",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"EgovBBSManageApiController"}
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
	@PatchMapping(value = "/board/{bbsId}/{nttId}")
	public IntermediateResultVO<Object> deleteBoardArticle(
		@Parameter(name = "bbsId", description = "게시판 Id", in = ParameterIn.PATH, example="BBSMSTR_AAAAAAAAAAAA")	
		@PathVariable("bbsId") String bbsId,
		@Parameter(name = "nttId", description = "게시글 Id", in = ParameterIn.PATH, example="1")
		@PathVariable("nttId") String nttId,
		@RequestBody BbsManageDeleteBoardRequestDTO bbsDeleteBoardRequestDTO,
		@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user)
		throws Exception {
		bbsDeleteBoardRequestDTO.setBbsId(bbsId);
		bbsDeleteBoardRequestDTO.setNttId(Long.parseLong(nttId));
		bbsDeleteBoardRequestDTO.setNttSj("이 글은 작성자에 의해서 삭제되었습니다.");
		
		bbsMngService.deleteBoardArticle(bbsDeleteBoardRequestDTO, user);

		return IntermediateResultVO.success(null);
	}
}