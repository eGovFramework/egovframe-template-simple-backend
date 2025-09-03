package egovframework.let.cop.bbs.dto.request;

import egovframework.com.cmm.LoginVO;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시물 수정 요청을 위한 DTO 클래스
 * 게시물 정보를 수정할 때 사용되는 요청 데이터를 구조화한 객체입니다.
 *
 * @author 김재섭(nirsa)
 * @since 2025.09.03
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.09.03    김재섭(nirsa)      최초 생성
 *
 * </pre>
 */
@Getter
@Setter
@Schema(description = "게시물 수정 요청")
public class BbsManageUpdateRequestDTO {
    @Schema(description = "게시판 아이디", example = "BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시자 아이디", example = "USRCNFRM_00000000000")
    private String ntcrId;

    @Schema(description = "게시자명", example = "관리자")
    private String ntcrNm;

    @Schema(description = "게시물 아이디", example = "1")
    private long nttId;

    @Schema(description = "게시물 제목", example = "게시물 제목입니다.")
    private String nttSj;

    @Schema(description = "게시물 내용", example = "게시물 내용입니다.")
    private String nttCn;

    @Schema(description = "패스워드", example = "4/hNOJJJTB26aAcPzaEFoGQQ5eTP/cEzxFqbgzqHE30=")
    private String password;

    @Schema(description = "게시시작일(사용 여부 확인되지 않음)", example = "")
    private String ntceBgnde;

    @Schema(description = "게시종료일(사용 여부 확인되지 않음)", example = "")
    private String ntceEndde;

    @Schema(description = "최종수정자 아이디", example = "USRCNFRM_00000000000")
    private String lastUpdusrId;

    @Schema(description = "게시물 첨부파일 아이디(없을 경우 스페이스 20칸)", example = "FILE_000000000000001")
    private String atchFileId;

    /**
     * BbsManageUpdateRequestDTO → BoardMaster 변환 메서드
     *
     * @return BoardMaster 도메인 객체
     */
    public BoardVO toBoardMaster(BbsManageUpdateRequestDTO bbsManageUpdateRequestDTO, LoginVO user) {
        BoardVO vo = new BoardVO();
        vo.setBbsId(bbsManageUpdateRequestDTO.getBbsId());
        vo.setNtcrId(bbsManageUpdateRequestDTO.getNtcrId());
        vo.setNtcrNm(user.getName());
        vo.setNttId(bbsManageUpdateRequestDTO.getNttId());
        vo.setNttSj(bbsManageUpdateRequestDTO.getNttSj());
        vo.setNttCn(bbsManageUpdateRequestDTO.getNttCn());
        vo.setPassword(bbsManageUpdateRequestDTO.getPassword());
        vo.setNtceBgnde(bbsManageUpdateRequestDTO.getNtceBgnde());
        vo.setNtceEndde(bbsManageUpdateRequestDTO.getNtceEndde());
        vo.setLastUpdusrId(user.getUniqId());
        vo.setAtchFileId(bbsManageUpdateRequestDTO.getAtchFileId());

        return vo;
    }
}
