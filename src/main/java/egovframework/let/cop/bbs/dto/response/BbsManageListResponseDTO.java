package egovframework.let.cop.bbs.dto.response;

import egovframework.com.cmm.LoginVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import java.util.List;

/**
 * BbsManage 게시판 리스트를 반환하는 응답 DTO 클래스 입니다.
 *
 * @author 김재섭(nirsa)
 * @since 2025.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.08.10    김재섭(nirsa)      최초 생성
 *
 * </pre>
 */

@Getter
@Builder
@Schema(description = "BBsManage 게시판 리스트 조회 응답 DTO")
public class BbsManageListResponseDTO {
    private List<BbsManageListItemResponseDTO> resultList;
    private int resultCnt;

    @Schema(
            description = "게시판 마스터 상세",
            implementation = BbsAttributeDetailResponseDTO.class
    )
    @Setter
    private BbsFileAtchResponseDTO brdMstrVO;

    @Setter
    private PaginationInfo paginationInfo;

    @Setter
    private LoginVO user;
}
