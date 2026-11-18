package egovframework.let.cop.bbs.dto.response;

import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 게시판 파일 첨부 정보를 반환하는 응답 DTO 클래스 입니다.
 *
 * @author 김재섭(nirsa)
 * @since 2025.07.03
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일         수정자          수정내용
 *   ----------    ------------    ---------------------------
 *   2025.07.03    김재섭(nirsa)     최초 생성
 *   2025.08.11    김재섭(nirsa)     네이밍 변경(BbsDetailResponse -> BbsFileAtchResponseDTO)
 *
 * </pre>
 */

@Getter
@SuperBuilder
public class BbsFileAtchResponseDTO {
	@Schema(description = "파일첨부가능여부", example="Y")
	private String fileAtchPosblAt;
	
    @Schema(description = "첨부파일 허용 개수", example = "3")
    private int posblAtchFileNumber;
    
    @Schema(description = "첨부파일 허용 크기 (/boardFileAtch/{bbsId} 요청 시 사용)", example = "5242880")
    private String posblAtchFileSize;

    /**
     *  BoardMasterVO vo → BbsFileAtchResponseDTO 변환 메서드 입니다.
     * @param BoardMasterVO
     * @return BbsFileAtchResponseDTO
     */
    public static BbsFileAtchResponseDTO from(BoardMasterVO vo) {
        return BbsFileAtchResponseDTO.builder()
                .fileAtchPosblAt(vo.getFileAtchPosblAt())
                .posblAtchFileNumber(vo.getPosblAtchFileNumber())
                .posblAtchFileSize(vo.getPosblAtchFileSize())
                .build();
    }
}
