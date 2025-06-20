package egovframework.let.cop.bbs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시판 검색 조건을 담는 VO 클래스
 * 게시판 목록 조회 시 사용되는 검색 파라미터들을 구조화하여 전달하기 위한 데이터 객체입니다.
 * 
 * - Swagger 문서화 및 파라미터 자동 바인딩 용도로 사용됩니다.
 * - ModelAttribute 또는 RequestParam 변환 시 검색 조건 처리에 활용됩니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.04.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.04.06    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */

@Getter
@Setter
@Schema(description = "게시판 검색 조건")
public class BbsAttributeSearchRequestDTO {

    @Schema(description = "게시판 Id", example = "")
    private String bbsId = "";

    @Schema(description = "페이지 번호", example = "1")
    private int pageIndex = 1;

    @Schema(description = "검색 조건", example = "0")
    private String searchCnd = "0";

    @Schema(description = "검색어", example = "")
    private String searchWrd = "";
}