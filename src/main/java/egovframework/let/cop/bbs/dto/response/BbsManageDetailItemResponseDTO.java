package egovframework.let.cop.bbs.dto.response;

import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * BbsManage 게시물 정보를 반환하는 응답 DTO 클래스 입니다.
 * 게시물 상세보기 조회 API 호출 시 단일 게시물의 필요한 정보를 표현합니다.
 *
 * <p>
 * 상위 클래스인 {@link BbsDetailResponseDTO}를 상속받아
 * 공통 필드(게시판 ID, 게시물 ID, 제목, 조회수, 최초 등록자명, 최초 등록시점)를 포함하며
 * 해당 클래스는 상세 조회 시에만 필요한 추가 필드(게시판 명, 답장 가능 여부, 본문 내용, 등록자 ID, 부모글 번호, 첨부파일 ID)를 정의합니다.
 * </p>

 *
 * @author 김재섭(nirsa)
 * @since 2025.08.11
 * @version 1.1
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.08.11    김재섭(nirsa)      최초 생성
 *   2025.08.26    김재섭(nirsa)      공통 필드 추출 및 Docs 보강
 *
 * </pre>
 */

@Getter
@SuperBuilder
@Schema(description = "게시물 정보 응답 DTO")
public class BbsManageDetailItemResponseDTO extends BbsDetailResponseDTO {
    @Schema(description = "게시판 명", example = "공지사항")
    private String bbsNm;

    @Schema(description = "답장가능여부", example = "Y")
    private String replyPosblAt;

    @Schema(description = "게시물 내용", example = "게시물 내용입니다.")
    private String nttCn;

    @Schema(description = "최초 등록자 아이디", example = "USRCNFRM_00000000000")
    private String frstRegisterId;

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
