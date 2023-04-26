package egovframework.let.cop.com.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;


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
@Getter
@Setter
public class BoardUseInfVO extends BoardUseInf implements Serializable {

    /**
	 * serialVersion UID
	 */
	private static final long serialVersionUID = -2688781320530443850L;

	/** 검색시작일 */
    private String searchBgnDe = "";

    /** 검색조건 */
    private String searchCnd = "";

    /** 검색종료일 */
    private String searchEndDe = "";

    /** 검색단어 */
    private String searchWrd = "";

    /** 정렬순서(DESC,ASC) */
    private long sortOrdr = 0L;

    /** 검색사용여부 */
    private String searchUseYn = "";

    /** 현재페이지 */
    private int pageIndex = 1;

    /** 페이지갯수 */
    private int pageUnit = 10;

    /** 페이지사이즈 */
    private int pageSize = 10;

    /** 첫페이지 인덱스 */
    private int firstIndex = 1;

    /** 마지막페이지 인덱스 */
    private int lastIndex = 1;

    /** 페이지당 레코드 개수 */
    private int recordCountPerPage = 10;

    /** 레코드 번호 */
    private int rowNo = 0;

    /** 최초 등록자명 */
    private String frstRegisterNm = "";

    /** 최종 수정자명 */
    private String lastUpdusrNm = "";

    /** 등록구분 코드명 */
    private String registSeCodeNm = "";

    /** 커뮤니티 아이디 */
    private String cmmntyId = "";

    /** 커뮤니티 명 */
    private String cmmntyNm = "";

    /** 동호회 아이디 */
    private String clbId = "";

    /** 동호회 명 */
    private String clbNm = "";

    /** 게시판 명 */
    private String bbsNm = "";

    /** 사용자 명 */
    private String userNm = "";

    /** 제공 URL */
    private String provdUrl = "";

    /** 게시판 유형코드 */
    private String bbsTyCode = "";

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
