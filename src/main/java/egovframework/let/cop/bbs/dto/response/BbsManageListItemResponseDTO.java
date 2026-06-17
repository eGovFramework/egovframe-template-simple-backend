package egovframework.let.cop.bbs.dto.response;

import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * BbsManage 게시판 리스트 아이템(게시물)을 반환하는 응답 DTO 클래스 입니다.
 * 게시판 리스트 조회 API 호출 시 리스트에 표현될 아이템(게시물)의 정보를 저장합니다.
 *
 * <p>
 * 상위 클래스인 {@link BbsDetailResponseDTO} 에서 게시물의 공통 필드
 * (게시판 ID, 게시물 ID, 제목, 조회수, 최초 등록자명, 최초 등록시점)를 상속받으며,
 * 해당 클래스는 목록 조회에 특화된 필드(답장 위치, 사용 여부 플래그)를 추가로 포함합니다.
 * </p>
 *
 * @author 김재섭(nirsa)
 * @since 2025.08.10
 * @version 1.1
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.08.10    김재섭(nirsa)      최초 생성
 *   2025.08.26    김재섭(nirsa)      공통 필드 추출 및 Docs 보강
 *
 * </pre>
 */
@Getter
@SuperBuilder
@Schema(description = "게시판 리스트 아이템(게시물) 응답 DTO")
public class BbsManageListItemResponseDTO extends BbsDetailResponseDTO {
    @Schema(description = "답장위치", example="0")
    private String replyLc;

    @Schema(description = "사용플래그", example="Y")
    private String bbsUseFlag;

    /**
     *  BoardVO vo → BbsManageListItemResponseDTO 변환 메서드 입니다.
     * @param BoardVO
     * @return BbsManageListItemResponseDTO
     */
    public static BbsManageListItemResponseDTO from(BoardVO vo) {
        return BbsManageListItemResponseDTO.builder()
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
