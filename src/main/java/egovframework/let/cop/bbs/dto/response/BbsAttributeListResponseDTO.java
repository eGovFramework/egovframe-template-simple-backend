package egovframework.let.cop.bbs.dto.response;

import java.util.List;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시판 리스트를 반환하는 응답 DTO 클래스 입니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.04.10    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */

@Getter
@Builder
@Schema(description = "게시판 리스트 조회 응답 DTO")
public class BbsAttributeListResponseDTO {
    private List<BbsAttributeDetailResponseDTO> resultList;
    private int resultCnt;
    
    @Setter
    private PaginationInfo paginationInfo;
    
    @Builder.Default
	@Schema(description = "응답 코드", example="")
	private int resultCode = 0;
	
    @Builder.Default
	@Schema(description = "응답 메시지", example="")
	private String resultMessage = "OK";
}
