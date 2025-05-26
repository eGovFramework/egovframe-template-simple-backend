package egovframework.let.cop.bbs.domain.response;

import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * 게시글 정보를 반환하는 응답 VO 클래스 입니다.
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
@ToString
@Schema(description = "게시판 정보 응답 VO")
public class BbsDetailResponseVO {
    @Schema(description = "게시판 ID", example = "BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시판 이름", example = "공지사항")
    private String bbsNm;

    @Schema(description = "게시판 유형 코드", example = "BBST01")
    private String bbsTyCode;

    @Schema(description = "게시판 유형 이름", example = "공지게시판")
    private String bbsTyCodeNm;

    @Schema(description = "게시판 속성 코드", example = "BBSA03")
    private String bbsAttrbCode;

    @Schema(description = "게시판 속성 이름", example = "일반게시판")
    private String bbsAttrbCodeNm;

    @Schema(description = "첨부파일 허용 개수", example = "3")
    private int posblAtchFileNumber;

    @Schema(description = "템플릿 ID", example = "TMPLAT_BOARD_DEFAULT")
    private String tmplatId;

    @Schema(description = "사용 여부", example = "Y")
    private String useAt;

    @Schema(description = "등록일", example = "2011-08-31")
    private String frstRegisterPnttm;
    
    @Builder.Default
	@Schema(description = "추가 option (댓글-comment, 만족도조사-stsfdg)", example="")
    private String option = "";
    
	/**
	 *  BoardMasterVO → BoardMasterResponse 변환 메서드 입니다.
	 * @param vo
	 * @return BoardMasterResponse
	 */
    public static BbsDetailResponseVO from(BoardMasterVO vo) {
        return BbsDetailResponseVO.builder()
            .bbsId(vo.getBbsId())
            .bbsNm(vo.getBbsNm())
            .bbsTyCode(vo.getBbsTyCode())
            .bbsTyCodeNm(vo.getBbsTyCodeNm())
            .bbsAttrbCode(vo.getBbsAttrbCode())
            .bbsAttrbCodeNm(vo.getBbsAttrbCodeNm())
            .posblAtchFileNumber(vo.getPosblAtchFileNumber())
            .tmplatId(vo.getTmplatId())
            .useAt(vo.getUseAt())
            .frstRegisterPnttm(vo.getFrstRegisterPnttm())
            .option(vo.getOption())
            .build();
    }
}