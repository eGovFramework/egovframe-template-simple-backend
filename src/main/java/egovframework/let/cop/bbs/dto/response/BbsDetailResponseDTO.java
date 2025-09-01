package egovframework.let.cop.bbs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 게시물 공통 아이템 메타 DTO 클래스입니다.
 * 게시물 상세/목록 조회 시 공통적으로 필요한 필드(게시판 ID, 게시물 ID, 제목, 조회수, 최초 등록자명, 최초 등록시점)를 정의합니다.
 *
 * <p>
 * 이 클래스는 {@code abstract}로 선언되어 직접 인스턴스화되지 않으며,
 * 상세 전용 DTO({@link BbsManageDetailItemResponseDTO})와 목록 전용 DTO({@link BbsManageListItemResponseDTO})
 * 에 의해 상속되어 사용됩니다.
 * </p>
 *
 * @author 김재섭(nirsa)
 * @since 2025.08.26
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자           수정내용
 *   ----------    ------------     -------------------
 *   2025.08.26    김재섭(nirsa)      최초 생성
 *
 * </pre>
 */

@Getter
@SuperBuilder
@Schema(description = "게시물 공통 아이템 메타(목록/상세 공통)")
// TODO : 현재는 상속을 통해 첨부파일 설정이 항상 노출됩니다.
//        첨부파일 설정이 필요한 API에서만 노출되도록 추후 Composition 방식으로 전환이 필요합니다.
public abstract class BbsDetailResponseDTO extends BbsFileAtchResponseDTO {
    @Schema(description = "게시판 아이디", example = "BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시물 아이디", example = "1")
    private long nttId;

    @Schema(description = "게시물 제목", example = "게시물 제목입니다.")
    private String nttSj;

    @Schema(description = "조회수", example = "1")
    private int inqireCo;

    @Schema(description = "최초 등록자명", example = "관리자")
    private String frstRegisterNm;

    @Schema(description = "최초등록시점", example = "2025-08-11")
    private String frstRegisterPnttm;
}
