package egovframework.let.cop.com.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 게시판의 이용정보를 관리하기 위한 VO 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.02  이삼섭          최초 생성
 *   2011.05.31  JJY           경량환경 커스터마이징버전 생성
 *
 * </pre>
 */
@Schema(description = "게시판 이용정보 VO")
@Getter
@Setter
public class BoardUseInfVO extends BoardUseInf implements Serializable {

    /**
	 * serialVersion UID
	 */
	private static final long serialVersionUID = -2688781320530443850L;

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

	@Schema(description = "등록구분 코드명")
    private String registSeCodeNm = "";

	@Schema(description = "커뮤니티 아이디")
    private String cmmntyId = "";

	@Schema(description = "커뮤니티 명")
    private String cmmntyNm = "";

	@Schema(description = "동호회 아이디")
    private String clbId = "";

	@Schema(description = "동호회 명")
    private String clbNm = "";

	@Schema(description = "게시판 명")
    private String bbsNm = "";

	@Schema(description = "사용자 명")
    private String userNm = "";

	@Schema(description = "제공 URL")
    private String provdUrl = "";

    /** 게시판 유형코드 */
	@Schema(description = "게시판 유형코드")
    private String bbsTyCode = "";

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
