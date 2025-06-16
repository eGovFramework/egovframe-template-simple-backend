package egovframework.let.cop.bbs.dto.request;

import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 삭제 요청 DTO 클래스
 * 게시글 삭제할 때 사용되는 요청 데이터를 구조화한 객체입니다.
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
 *   2025.06.16    김재섭(nirsa)      최초 생성 
 * </pre>
 */

@Getter
@Setter
@Schema(description = "게시글 마스터 수정 요청 DTO")
public class BbsDeleteRequestDTO {
    @Schema(description = "게시판 Id", example="BBSMSTR_AAAAAAAAAAAA")
    private String bbsId;

    @Schema(description = "게시글 Id", example="1")
    private long nttId;
    
	@Schema(description = "게시물 제목", example="")
	private String nttSj;
    
    /**
     * BBSManageDeleteRequestDTO → BoardVO 변환 메서드
     * 
     * @return BoardVO 도메인 객체
     */
	public BoardVO toBoardVO(String uniqId) {
		BoardVO vo = new BoardVO();
		vo.setBbsId(this.bbsId);
		vo.setNttId(this.nttId);
		vo.setNttSj(this.nttSj);
		vo.setLastUpdusrId(uniqId);
	    return vo;
	}
}