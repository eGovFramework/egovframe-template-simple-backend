package egovframework.let.cop.bbs.dto.request;

import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 게시글 삭제 요청에 사용되는 DTO 클래스입니다.
 * 게시판 ID, 게시글 ID, 첨부파일 ID 정보를 포함하며 해당 게시글 삭제 요청 시 전달되는 데이터를 구조화합니다.
 * 
 * - Swagger 문서화 및 요청 본문(RequestBody) 자동 매핑 용도로 사용됩니다.
 * - 게시글 삭제 API에서 필요한 식별자 정보를 전달하는 데 활용됩니다.
 * - bbsId, nttId는 경로 파라미터로 전달되어 세터로 주입합니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.04.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.06.19    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */


@Setter
@Getter
@ToString
@Schema(description = "게시글 삭제 요청")
public class BbsDeleteBoardRequestDTO {
    @Schema(description = "게시판 ID", example = "BBSMSTR_AAAAAAAAAAAA", hidden=true)
	private String bbsId;
	 
    @Schema(description = "게시글 ID", example = "1", hidden=true)
    private Long nttId;
    
	@Schema(description = "게시글 제목", example = "이 글은 작성자에 의해서 삭제되었습니다.", hidden=true)
	private String nttSj;
    
    @Schema(description = "첨부파일 ID", example = "FILE_000000000000001")
    private String atchFileId;
    
    /**
     * BbsDeleteBoardRequestDTO → BoardMaster 변환 메서드
     * 
     * @return BoardMaster 도메인 객체
     */
	public BoardVO toBoardMaster(BbsDeleteBoardRequestDTO bbsDeleteBoardRequestDTO, String lastUpdusrId) {
		BoardVO vo = new BoardVO();
	    vo.setBbsId(bbsDeleteBoardRequestDTO.getBbsId());
	    vo.setAtchFileId(bbsDeleteBoardRequestDTO.getAtchFileId());
	    vo.setNttId(bbsDeleteBoardRequestDTO.getNttId());
	    vo.setNttSj(bbsDeleteBoardRequestDTO.getNttSj());
	    vo.setLastUpdusrId(lastUpdusrId);
	    return vo;
	}
}
