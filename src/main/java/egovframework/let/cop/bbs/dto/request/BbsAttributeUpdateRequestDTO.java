package egovframework.let.cop.bbs.dto.request;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시판 마스터 수정 요청 DTO 클래스
 * 게시판 마스터 정보를 수정할 때 사용되는 요청 데이터를 구조화한 객체입니다.
 * 
 * - 게시판 속성, 템플릿, 첨부파일 옵션 등을 포함한 전체 게시판 정보 수정에 사용됩니다.
 * - Swagger 문서화 및 파라미터 바인딩에 활용됩니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.05.26
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.05.26    김재섭(nirsa)      최초 생성 
 * </pre>
 */
@Getter
@Setter
@Schema(description = "게시판 마스터 수정 요청 DTO")
public class BbsAttributeUpdateRequestDTO {
    @Schema(description = "게시판 ID", example = "BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시판 명", example = "공지사항")
    private String bbsNm;

    @Schema(description = "게시판 소개", example = "소개글입니다.")
    private String bbsIntrcn;

    @Schema(description = "게시판 유형 코드", example = "BBST03")
    private String bbsTyCode;

    @Schema(description = "게시판 유형명", example = "공지게시판")
    private String bbsTyCodeNm;

    @Schema(description = "게시판 속성 코드", example = "BBSA03")
    private String bbsAttrbCode;

    @Schema(description = "게시판 속성명", example = "일반게시판")
    private String bbsAttrbCodeNm;

    @Schema(description = "템플릿 ID", example = "TMPLAT_BOARD_DEFAULT")
    private String tmplatId;

    @Schema(description = "템플릿명", example = "")
    private String tmplatNm;

    @Schema(description = "게시판 사용 여부", example = "Y")
    private String useAt;

    @Schema(description = "게시판 사용 플래그", example = "Y")
    private String bbsUseFlag;

    @Schema(description = "등록자 ID", example = "USRCNFRM_00000000000")
    private String frstRegisterId;

    @Schema(description = "등록자명", example = "관리자")
    private String frstRegisterNm;

    @Schema(description = "등록일시", example = "2011-0831")
    private String frstRegisterPnttm;

    @Schema(description = "첨부파일 가능 여부", example = "N")
    private String fileAtchPosblAt;

    @Schema(description = "첨부파일 가능 개수", example = "3")
    private int posblAtchFileNumber;

    @Schema(description = "답글 가능 여부", example = "Y")
    private String replyPosblAt;

    @Schema(description = "권한 플래그", example = "Y")
    private String authFlag;
    
	@Schema(description = "최종수정자 아이디", example="")
    public String lastUpdusrId = "";
	
	@Schema(description = "첨부가능파일사이즈", example="")
    private String posblAtchFileSize = "";
	
	@Schema(description = "추가 option (댓글-comment, 만족도조사-stsfdg)", example="")
    private String option = "";
	
    /**
     * BbsAttributeUpdateRequestDTO → BoardMaster 변환 메서드
     * 
     * @return BoardMaster 도메인 객체
     */
	public BoardMaster toBoardMaster() {
	    BoardMaster vo = new BoardMaster();
	    vo.setBbsId(this.bbsId);
	    vo.setBbsNm(this.bbsNm); 
	    vo.setBbsIntrcn(this.bbsIntrcn);
	    vo.setBbsTyCode(this.bbsTyCode);
	    vo.setBbsAttrbCode(this.bbsAttrbCode);
	    vo.setTmplatId(this.tmplatId);
	    vo.setTmplatNm(this.tmplatNm);
	    vo.setUseAt(this.useAt);
	    vo.setBbsUseFlag(this.bbsUseFlag);
	    vo.setFrstRegisterId(this.frstRegisterId);
	    vo.setFrstRegisterPnttm(this.frstRegisterPnttm);
	    vo.setFileAtchPosblAt(this.fileAtchPosblAt);
	    vo.setPosblAtchFileNumber(this.posblAtchFileNumber);
	    vo.setReplyPosblAt(this.replyPosblAt);
	    vo.setLastUpdusrId(this.lastUpdusrId);
	    vo.setPosblAtchFileSize(this.posblAtchFileSize);
	    vo.setOption(this.option);
	    return vo;
	}
}