package egovframework.let.cop.com.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 사용자 정보 조회를 위한 VO  클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.06  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Getter
@Setter
public class UserInfVO implements Serializable {

    /**
	 * serialVersion UID
	 */
	private static final long serialVersionUID = -6156707290504312279L;

	/** 유일 아이디 */
    private String uniqId = "";

    /** 사용자 아이디 */
    private String userId = "";

    /** 사용자 명 */
    private String userNm = "";

    /** 사용자 우편번호 */
    private String userZip = "";

    /** 사용자 주소 */
    private String userAdres = "";

    /** 사용자 이메일 */
    private String userEmail = "";

    /** 검색시작일 */
    private String searchBgnDe = "";

    /** 검색조건 */
    private String searchCnd = "";

    /** 검색종료일 */
    private String searchEndDe = "";

    /** 검색단어 */
    private String searchWrd = "";

    /** 정렬순서(DESC,ASC) */
    private String sortOrdr = "";

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

    /** 대상 아이디 */
    private String trgetId = "";

    /** 사용여부 */
    private String useAt = "Y";

    /** 커뮤니티 아이디 */
    private String cmmntyId = "";

    /** 동호회 아이디 */
    private String clubId = "";

    /** 대상 중지 여부 (커뮤니티 또는 동호회) */
    private String deletedAt = "N";

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}