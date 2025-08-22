package egovframework.let.cop.bbs.dto.response;

import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * BbsManage 게시물 정보를 반환하는 응답 DTO 클래스 입니다.
 *
 * @author 김재섭(nirsa)
 * @since 2025.08.11
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.08.11    김재섭(nirsa)      최초 생성
 *
 * </pre>
 */

@Getter
@SuperBuilder
@Schema(description = "게시물 정보 응답 DTO")
public class BbsManageDetailItemResponseDTO extends BbsFileAtchResponseDTO {
    @Schema(description = "게시판 명", example = "공지사항")
    private String bbsNm;

    @Schema(description = "답장가능여부", example = "Y")
    private String replyPosblAt;

    @Schema(description = "게시판 아이디", example = "BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시물 내용", example = "게시물 내용입니다.")
    private String nttCn;

    @Schema(description = "게시물 아이디", example = "1")
    private long nttId;

    @Schema(description = "게시물 제목", example = "게시물 제목입니다.")
    private String nttSj;

    @Schema(description = "최초 등록자 아이디", example = "USRCNFRM_00000000000")
    private String frstRegisterId;

    @Schema(description = "최초 등록자명", example = "관리자")
    private String frstRegisterNm;

    @Schema(description = "최초등록시점", example = "2025-08-11")
    private String frstRegisterPnttm;

    @Schema(description = "조회수", example = "1")
    private int inqireCo;

    @Schema(description = "부모글번호", example = "0")
    private String parnts;

    @Schema(description = "게시물 첨부파일 아이디", example = "FILE_000000000000001")
    private String atchFileId;

    /**
     *  BoardVO vo → BbsManageDetailResponseDTO 변환 메서드 입니다.
     * @param BoardVO
     * @return BbsManageDetailResponseDTO
     */
    public static BbsManageDetailItemResponseDTO from(BoardVO vo) {
        return BbsManageDetailItemResponseDTO.builder()
                .bbsNm(vo.getBbsNm())
                .replyPosblAt(vo.getReplyPosblAt())
                .bbsId(vo.getBbsId())
                .nttCn(vo.getNttCn())
                .nttId(vo.getNttId())
                .nttSj(vo.getNttSj())
                .frstRegisterId(vo.getFrstRegisterId())
                .frstRegisterNm(vo.getFrstRegisterNm())
                .frstRegisterPnttm(vo.getFrstRegisterPnttm())
                .inqireCo(vo.getInqireCo())
                .parnts(vo.getParnts())
                .atchFileId(vo.getAtchFileId())
                .build();
    }
}
