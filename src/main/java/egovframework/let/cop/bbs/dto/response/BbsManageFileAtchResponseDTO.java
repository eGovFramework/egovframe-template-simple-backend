package egovframework.let.cop.bbs.dto.response;

import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시판 마스터 상세내용(파일 첨부 가능 여부) 정보를 반환하는 응답 DTO 클래스 입니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.07.03
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.07.03    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */

@Getter
@Builder
@Schema(description = "게시판 마스터 상세내용(파일 첨부 가능 여부) 응답 DTO")
public class BbsManageFileAtchResponseDTO implements BbsDetailResponse {
	@Schema(description = "파일첨부가능여부", example="Y")
	private String fileAtchPosblAt;
	
    @Schema(description = "첨부파일 허용 개수", example = "3")
    private int posblAtchFileNumber;
    
    @Schema(description = "첨부파일 허용 크기 (/boardFileAtch/{bbsId} 요청 시 사용)", example = "5242880")
    private String posblAtchFileSize;
    
	/**
	 *  BoardMasterVO vo → BbsManageFileAtchResponseDTO 변환 메서드 입니다.
	 * @param BoardMasterVO
	 * @return BbsManageFileAtchResponseDTO
	 */
    public static BbsManageFileAtchResponseDTO from(BoardMasterVO vo) {
        return BbsManageFileAtchResponseDTO.builder()
        		.fileAtchPosblAt(vo.getFileAtchPosblAt())
        		.posblAtchFileNumber(vo.getPosblAtchFileNumber())
        		.posblAtchFileSize(vo.getPosblAtchFileSize())
        		.build();
    }
}
