package egovframework.com.cmm.service;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class Name : FileDeleteRequest.java
 * @Description : POST /file 첨부파일 삭제 요청 DTO
 *
 * 삭제 대상(atchFileId, fileSn)과 함께, 소유권 검증을 위한 소유 엔티티 컨텍스트
 * (게시글 bbsId+nttId / 일정 schdulId)를 전달받는다. 공통 파일정보 VO(FileVO)와
 * 관심사를 분리하기 위해 이 엔드포인트 전용 요청 DTO로 둔다.
 *
 * @Modification Information
 *    수정일         수정자      수정내용
 *    -------       ------     -------------------
 *    2026.07       보안대응     최초 생성 (소유권 검증)
 */
@Schema(description = "파일 삭제 요청 (소유권 검증용 컨텍스트 포함)")
@Getter
@Setter
public class FileDeleteRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "첨부파일 아이디(암호화)", example = "oO3rGEfD8twsMG5pYVeQOpaMm04hc+uWqx2u6s44KzA=")
	private String atchFileId = "";

	@Schema(description = "파일연번", example = "0")
	private String fileSn = "";

	// 소유권 검증용 — 삭제 대상 파일이 속한 소유 엔티티 식별(게시글 or 일정 중 하나)
	@Schema(description = "(소유권 검증용) 게시판ID", example = "BBSMSTR_AAAAAAAAAAAA")
	private String bbsId = "";

	@Schema(description = "(소유권 검증용) 게시글 번호", example = "1")
	private String nttId = "";

	@Schema(description = "(소유권 검증용) 일정ID", example = "SCHDUL_0000000000000001")
	private String schdulId = "";
}
