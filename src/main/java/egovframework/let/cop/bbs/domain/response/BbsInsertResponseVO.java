package egovframework.let.cop.bbs.domain.response;

import java.util.List;

import egovframework.com.cmm.service.CmmnDetailCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시판 마스터 등록을 반환하는 응답 VO 클래스 입니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.05.26
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.05.26    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "게시판 마스터 등록 응답 VO")
public class BbsInsertResponseVO {
	private List<CmmnDetailCode> typeList;
	private List<CmmnDetailCode> attrbList;
	
}
