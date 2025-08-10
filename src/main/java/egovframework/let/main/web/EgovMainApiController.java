package egovframework.let.main.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageListResponseDTO;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 템플릿 메인 페이지 컨트롤러 클래스(Sample 소스)
 * @author 실행환경 개발팀 JJY
 * @since 2011.08.31
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.08.31  JJY            최초 생성
 *
 * </pre>
 */
@RestController
@SessionAttributes(types = ComDefaultVO.class)
@Tag(name="EgovMainApiController",description = "메인 페이지")
public class EgovMainApiController {

	/**
	 * EgovBBSManageService
	 */
	@Resource(name = "EgovBBSManageService")
    private EgovBBSManageService bbsMngService;

	/**
	 * 템플릿 메인 페이지 조회
	 * @return 메인페이지 정보 Map [key : 항목명]
	 *
	 * @throws Exception
	 */
	@Operation(
			summary = "메인 페이지",
			description = "템플릿 메인 페이지 조회",
			tags = {"EgovMainApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@GetMapping(value = "/mainPage")
	public ResultVO getMgtMainPage()
	  throws Exception{

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 공지사항 메인 컨텐츠 조회 시작 ---------------------------------
		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(1);
		paginationInfo.setRecordCountPerPage(5);
		paginationInfo.setPageSize(10);

		BbsSearchRequestDTO bbsSearchRequestDTO = new BbsSearchRequestDTO();
		bbsSearchRequestDTO.setBbsId("BBSMSTR_AAAAAAAAAAAA");

		BbsManageListResponseDTO notiList = bbsMngService.selectBoardArticles(bbsSearchRequestDTO, paginationInfo,"BBSA02");
		resultMap.put("notiList", notiList.getResultList());

		bbsSearchRequestDTO.setBbsId("BBSMSTR_BBBBBBBBBBBB");
		BbsManageListResponseDTO galList = bbsMngService.selectBoardArticles(bbsSearchRequestDTO, paginationInfo,"BBSA02");
		resultMap.put("galList", galList.getResultList());

		resultVO.setResult(resultMap);
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return resultVO;
	}

}