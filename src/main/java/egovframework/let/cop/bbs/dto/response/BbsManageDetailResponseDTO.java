package egovframework.let.cop.bbs.dto.response;

import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 게시물 정보를 반환하는 응답 DTO 클래스 입니다.
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
@SuperBuilder
@Schema(description = "게시물 정보 응답 DTO")
public class BbsManageDetailResponseDTO {
    @Schema(description = "게시판 아이디", example="BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시물 아이디", example="1")
    private long nttId;

    @Schema(description = "게시물 제목", example="게시물 제목입니다.")
    private String nttSj;

    @Schema(description = "조회수", example="0")
    private int inqireCo;

    @Schema(description = "답장위치", example="0")
    private String replyLc;

    @Schema(description = "사용플래그", example="Y")
    private String bbsUseFlag;

    @Schema(description = "최초 등록자명", example="관리자")
    private String frstRegisterNm;

    @Schema(description = "최초등록시점", example="2025-08-10")
    private String frstRegisterPnttm;

    /**
     *  BoardVO vo → BbsManageDetailResponseDTO 변환 메서드 입니다.
     * @param BoardVO
     * @return BbsManageDetailResponseDTO
     */
    public static BbsManageDetailResponseDTO from(BoardVO vo) {
        return BbsManageDetailResponseDTO.builder()
                .bbsId(vo.getBbsId())
                .nttId(vo.getNttId())
                .nttSj(vo.getNttSj())
                .inqireCo(vo.getInqireCo())
                .replyLc(vo.getReplyLc())
                .bbsUseFlag(vo.getUseAt())
                .frstRegisterNm(vo.getFrstRegisterNm())
                .frstRegisterPnttm(vo.getFrstRegisterPnttm())
                .build();
    }
}
