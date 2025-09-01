package egovframework.let.cop.bbs.dto.request;

import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시물 상세 조회 요청 DTO
 *
 * @author 김재섭(nirsa)
 * @since 2025.08.11
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.08.11    김재섭(nirsa)      최초 생성
 */
@Getter
@Builder
@Schema(description = "게시물 상세 조회 요청 DTO")
public class BbsManageDetailBoardRequestDTO {
    @Schema(description = "게시판 아이디", example = "BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시물 아이디", example = "1")
    private long nttId;

    @Setter
    @Schema(description = "조회 수 증가 여부", example = "false")
    private boolean plusCount;

    @Schema(description = "하위 페이지 인덱스 (댓글 및 만족도 조사 여부 확인용)")
    private String subPageIndex;

    @Schema(description = "최종수정자 아이디", example = "관리자")
    private String lastUpdusrId;

    /**
     * BbsAttributeInsertRequestDTO → BoardVO 변환 메서드
     *
     * @return BoardVO 도메인 객체
     */
    public BoardVO toBoardVO() {
        BoardVO vo = new BoardVO();
        vo.setBbsId(bbsId);
        vo.setNttId(nttId);
        vo.setPlusCount(plusCount);
        vo.setSubPageIndex(subPageIndex);
        vo.setLastUpdusrId(lastUpdusrId);
        return vo;
    }
}
