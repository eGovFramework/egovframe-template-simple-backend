package egovframework.let.cop.bbs.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 게시판 마스터 상세내용 조회 요청 DTO 클래스
 * 게시판 마스터 상세내용 조회 요청 시 사용되는 파라미터들을 구조화하여 전달하기 위한 데이터 객체입니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.06.30
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.06.30    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */

@Getter
@Builder
@Schema(description = "게시판 마스터 상세내용 조회 요청 DTO")
@ToString
public class BbsAttributeDetailRequestDTO {
	@Schema(description = "게시판 Id", example="BBSMSTR_AAAAAAAAAAAA")
	private String bbsId;
	
	@Schema(description = "유일 아이디", example="")
    private String uniqId;
}
