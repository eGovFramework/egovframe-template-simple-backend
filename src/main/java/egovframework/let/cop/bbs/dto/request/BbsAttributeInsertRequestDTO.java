package egovframework.let.cop.bbs.dto.request;

import javax.validation.constraints.NotBlank;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 신규 게시판 마스터 등록 요청 DTO 클래스
 * 신규 게시판 마스터 등록 요청 시 사용되는 파라미터들을 구조화하여 전달하기 위한 데이터 객체입니다.
 * 
 * 
 * @author 김재섭(nirsa)
 * @since 2025.05.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.05.20    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */

@Getter
@Setter
@Schema(description = "신규 게시판 마스터 등록 요청 DTO")
@ToString
public class BbsAttributeInsertRequestDTO {
    @Schema(description = "게시판명", example = "asdf")
    @NotBlank
    private String bbsNm;

    @Schema(description = "게시판 소개", example = "asdf")
    @NotBlank
    private String bbsIntrcn;

    @Schema(description = "게시판 유형", example = "BBST01")
    @NotBlank
    private String bbsTyCode;

    @Schema(description = "게시판 속성", example = "BBSA02")
    @NotBlank
    private String bbsAttrbCode;

    @Schema(description = "템플릿 ID", example = "TMPLAT_BOARD_DEFAULT")
    private String tmplatId;

    @Schema(description = "첨부파일 가능 여부", example = "Y")
    private String fileAtchPosblAt;

    @Schema(description = "첨부파일 가능한 파일 숫자", example = "1", type = "int")
    private int posblAtchFileNumber;

    @Schema(description = "답장 기능 여부", example = "N")
    private String replyPosblAt;
    
	@Schema(description = "최초등록자 아이디", example="")
    private String frstRegisterId;
	
	@Schema(description = "사용여부", example="")
    private String useAt;
	
	@Schema(description = "대상 아이디", example="")
    private String trgetId;
	
	@Schema(description = "첨부가능파일사이즈", example="")
    private String posblAtchFileSize;
	
    /**
     * BbsAttributeInsertRequestDTO → BoardMaster 변환 메서드
     * 
     * @return BoardMaster 도메인 객체
     */
	public BoardMaster toBoardMaster(String bbsId) {
	    BoardMaster vo = new BoardMaster();
	    vo.setBbsId(bbsId);
	    vo.setBbsNm(this.bbsNm);
	    vo.setBbsIntrcn(this.bbsIntrcn);
	    vo.setBbsTyCode(this.bbsTyCode);
	    vo.setBbsAttrbCode(this.bbsAttrbCode);
	    vo.setTmplatId(this.tmplatId);
	    vo.setFileAtchPosblAt(this.fileAtchPosblAt);
	    vo.setPosblAtchFileNumber(this.posblAtchFileNumber);
	    vo.setReplyPosblAt(this.replyPosblAt);
	    vo.setFrstRegisterId(this.frstRegisterId);
	    vo.setUseAt(this.useAt);
	    vo.setTrgetId(this.trgetId);
	    vo.setPosblAtchFileSize(this.posblAtchFileSize);
	    return vo;
	}
}