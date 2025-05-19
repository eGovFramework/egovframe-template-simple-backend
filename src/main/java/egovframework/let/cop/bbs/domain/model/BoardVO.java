package egovframework.let.cop.bbs.domain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 게시물 관리를 위한 VO 클래스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.19  이삼섭          최초 생성
 *  2009.06.29  한성곤		   2단계 기능 추가 (댓글관리, 만족도조사)
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@Schema(description = "게시물 VO")
@Getter
@Setter
public class BoardVO extends Board implements Serializable {

	/**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -3779821913760046011L;

	@Schema(description = "검색시작일")
	private String searchBgnDe = "";

	@Schema(description = "검색조건")
	private String searchCnd = "";

	@Schema(description = "검색종료일")
	private String searchEndDe = "";

	@Schema(description = "검색단어")
	private String searchWrd = "";

	@Schema(description = "정렬순서(DESC,ASC)")
	private long sortOrdr = 0L;

	@Schema(description = "검색사용여부")
	private String searchUseYn = "";

	@Schema(description = "현재페이지")
	private int pageIndex = 1;

	@Schema(description = "페이지갯수")
	private int pageUnit = 10;

	@Schema(description = "페이지사이즈")
	private int pageSize = 10;

	@Schema(description = "첫페이지 인덱스")
	private int firstIndex = 1;

	@Schema(description = "마지막페이지 인덱스")
	private int lastIndex = 1;

	@Schema(description = "페이지당 레코드 개수")
	private int recordCountPerPage = 10;

	@Schema(description = "레코드 번호")
	private int rowNo = 0;

	@Schema(description = "최초 등록자명")
	private String frstRegisterNm = "";

	@Schema(description = "최종 수정자명")
	private String lastUpdusrNm = "";

	@Schema(description = "유효여부")
	private String isExpired = "N";

	@Schema(description = "상위 정렬 순서")
	private String parntsSortOrdr = "";

	@Schema(description = "상위 답변 위치")
	private String parntsReplyLc = "";

	@Schema(description = "게시판 유형코드")
	private String bbsTyCode = "";

	@Schema(description = "게시판 속성코드")
	private String bbsAttrbCode = "";

	@Schema(description = "게시판 명")
	private String bbsNm = "";

	@Schema(description = "파일첨부가능여부")
	private String fileAtchPosblAt = "";

	@Schema(description = "첨부가능파일숫자")
	private int posblAtchFileNumber = 0;

	@Schema(description = "답장가능여부")
	private String replyPosblAt = "";

	@Schema(description = "조회 수 증가 여부")
	private boolean plusCount = false;

	//---------------------------------
	// 2009.06.29 : 2단계 기능 추가
	//---------------------------------
	@Schema(description = "하위 페이지 인덱스 (댓글 및 만족도 조사 여부 확인용)")
	private String subPageIndex = "";
	////-------------------------------

	/**
	 * toString 메소드를 대치한다.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}