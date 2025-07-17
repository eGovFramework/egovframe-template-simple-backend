package egovframework.let.cop.bbs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 게시판 응답 DTO 공통 베이스 클래스입니다.
 *
 * <p>
 * 게시판 마스터 정보 조회 시, 클라이언트 요청 유형에 따라 서로 다른 응답 DTO가 반환될 수 있으며 
 * 이 클래스는 그들 간의 공통 필드(file attach 관련)를 정의하는 상위 타입입니다.
 * </p>
 *
 * <p>
 * 예: {@link BbsAttributeDetailResponseDTO}, {@link BbsManageFileAtchResponseDTO} 등은 이 클래스를 상속하여 사용합니다.
 * </p>
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
 *
 * </pre>
 */

@Getter
@SuperBuilder
public class BbsDetailResponse {
	@Schema(description = "파일첨부가능여부", example="Y")
	private String fileAtchPosblAt;
	
    @Schema(description = "첨부파일 허용 개수", example = "3")
    private int posblAtchFileNumber;
    
    @Schema(description = "첨부파일 허용 크기 (/boardFileAtch/{bbsId} 요청 시 사용)", example = "5242880")
    private String posblAtchFileSize;
}
