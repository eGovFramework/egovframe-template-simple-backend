package egovframework.let.cop.bbs.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 게시판 속성 정보를 관리하기 위한 VO  클래스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.12  이삼섭          최초 생성
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
public class BoardMasterVO extends BoardMaster implements Serializable {

    /**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -8070768280461816170L;

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

    /** firstIndex */
    private int firstIndex = 1;

    /** lastIndex */
    private int lastIndex = 1;

    /** recordCountPerPage */
    private int recordCountPerPage = 10;

    /** rowNo */
    private int rowNo = 0;

    /** 최초 등록자명 */
    private String frstRegisterNm = "";

    /** 게시판유형 코드명 */
    private String bbsTyCodeNm = "";

    /** 게시판속성 코드명 */
    private String bbsAttrbCodeNm = "";

    /** 템플릿 명 */
    private String tmplatNm = "";

    /** 최종 수정자명 */
    private String lastUpdusrNm = "";

    /** 권한지정 여부 */
    private String authFlag = "";

    /** 템플릿경로 */
    private String tmplatCours = "";

    /**
     * searchBgnDe attribute를 리턴한다.
     *
     * @return the searchBgnDe
     */
    public String getSearchBgnDe() {
	return searchBgnDe;
    }

    /**
     * searchBgnDe attribute 값을 설정한다.
     *
     * @param searchBgnDe
     *            the searchBgnDe to set
     */
    public void setSearchBgnDe(String searchBgnDe) {
	this.searchBgnDe = searchBgnDe;
    }

    /**
     * searchCnd attribute를 리턴한다.
     *
     * @return the searchCnd
     */
    public String getSearchCnd() {
	return searchCnd;
    }

    /**
     * searchCnd attribute 값을 설정한다.
     *
     * @param searchCnd
     *            the searchCnd to set
     */
    public void setSearchCnd(String searchCnd) {
	this.searchCnd = searchCnd;
    }

    /**
     * searchEndDe attribute를 리턴한다.
     *
     * @return the searchEndDe
     */
    public String getSearchEndDe() {
	return searchEndDe;
    }

    /**
     * searchEndDe attribute 값을 설정한다.
     *
     * @param searchEndDe
     *            the searchEndDe to set
     */
    public void setSearchEndDe(String searchEndDe) {
	this.searchEndDe = searchEndDe;
    }

    /**
     * searchWrd attribute를 리턴한다.
     *
     * @return the searchWrd
     */
    public String getSearchWrd() {
	return searchWrd;
    }

    /**
     * searchWrd attribute 값을 설정한다.
     *
     * @param searchWrd
     *            the searchWrd to set
     */
    public void setSearchWrd(String searchWrd) {
	this.searchWrd = searchWrd;
    }

    /**
     * sortOrdr attribute를 리턴한다.
     *
     * @return the sortOrdr
     */
    public String getSortOrdr() {
	return sortOrdr;
    }

    /**
     * sortOrdr attribute 값을 설정한다.
     *
     * @param sortOrdr
     *            the sortOrdr to set
     */
    public void setSortOrdr(String sortOrdr) {
	this.sortOrdr = sortOrdr;
    }

    /**
     * searchUseYn attribute를 리턴한다.
     *
     * @return the searchUseYn
     */
    public String getSearchUseYn() {
	return searchUseYn;
    }

    /**
     * searchUseYn attribute 값을 설정한다.
     *
     * @param searchUseYn
     *            the searchUseYn to set
     */
    public void setSearchUseYn(String searchUseYn) {
	this.searchUseYn = searchUseYn;
    }

    /**
     * pageIndex attribute를 리턴한다.
     *
     * @return the pageIndex
     */
    public int getPageIndex() {
	return pageIndex;
    }

    /**
     * pageIndex attribute 값을 설정한다.
     *
     * @param pageIndex
     *            the pageIndex to set
     */
    public void setPageIndex(int pageIndex) {
	this.pageIndex = pageIndex;
    }

    /**
     * pageUnit attribute를 리턴한다.
     *
     * @return the pageUnit
     */
    public int getPageUnit() {
	return pageUnit;
    }

    /**
     * pageUnit attribute 값을 설정한다.
     *
     * @param pageUnit
     *            the pageUnit to set
     */
    public void setPageUnit(int pageUnit) {
	this.pageUnit = pageUnit;
    }

    /**
     * pageSize attribute를 리턴한다.
     *
     * @return the pageSize
     */
    public int getPageSize() {
	return pageSize;
    }

    /**
     * pageSize attribute 값을 설정한다.
     *
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
    }

    /**
     * firstIndex attribute를 리턴한다.
     *
     * @return the firstIndex
     */
    public int getFirstIndex() {
	return firstIndex;
    }

    /**
     * firstIndex attribute 값을 설정한다.
     *
     * @param firstIndex
     *            the firstIndex to set
     */
    public void setFirstIndex(int firstIndex) {
	this.firstIndex = firstIndex;
    }

    /**
     * lastIndex attribute를 리턴한다.
     *
     * @return the lastIndex
     */
    public int getLastIndex() {
	return lastIndex;
    }

    /**
     * lastIndex attribute 값을 설정한다.
     *
     * @param lastIndex
     *            the lastIndex to set
     */
    public void setLastIndex(int lastIndex) {
	this.lastIndex = lastIndex;
    }

    /**
     * recordCountPerPage attribute를 리턴한다.
     *
     * @return the recordCountPerPage
     */
    public int getRecordCountPerPage() {
	return recordCountPerPage;
    }

    /**
     * recordCountPerPage attribute 값을 설정한다.
     *
     * @param recordCountPerPage
     *            the recordCountPerPage to set
     */
    public void setRecordCountPerPage(int recordCountPerPage) {
	this.recordCountPerPage = recordCountPerPage;
    }

    /**
     * rowNo attribute를 리턴한다.
     *
     * @return the rowNo
     */
    public int getRowNo() {
	return rowNo;
    }

    /**
     * rowNo attribute 값을 설정한다.
     *
     * @param rowNo
     *            the rowNo to set
     */
    public void setRowNo(int rowNo) {
	this.rowNo = rowNo;
    }

    /**
     * frstRegisterNm attribute를 리턴한다.
     *
     * @return the frstRegisterNm
     */
    public String getFrstRegisterNm() {
	return frstRegisterNm;
    }

    /**
     * frstRegisterNm attribute 값을 설정한다.
     *
     * @param frstRegisterNm
     *            the frstRegisterNm to set
     */
    public void setFrstRegisterNm(String frstRegisterNm) {
	this.frstRegisterNm = frstRegisterNm;
    }

    /**
     * bbsTyCodeNm attribute를 리턴한다.
     *
     * @return the bbsTyCodeNm
     */
    public String getBbsTyCodeNm() {
	return bbsTyCodeNm;
    }

    /**
     * bbsTyCodeNm attribute 값을 설정한다.
     *
     * @param bbsTyCodeNm
     *            the bbsTyCodeNm to set
     */
    public void setBbsTyCodeNm(String bbsTyCodeNm) {
	this.bbsTyCodeNm = bbsTyCodeNm;
    }

    /**
     * bbsAttrbCodeNm attribute를 리턴한다.
     *
     * @return the bbsAttrbCodeNm
     */
    public String getBbsAttrbCodeNm() {
	return bbsAttrbCodeNm;
    }

    /**
     * bbsAttrbCodeNm attribute 값을 설정한다.
     *
     * @param bbsAttrbCodeNm
     *            the bbsAttrbCodeNm to set
     */
    public void setBbsAttrbCodeNm(String bbsAttrbCodeNm) {
	this.bbsAttrbCodeNm = bbsAttrbCodeNm;
    }

    /**
     * tmplatNm attribute를 리턴한다.
     *
     * @return the tmplatNm
     */
    public String getTmplatNm() {
	return tmplatNm;
    }

    /**
     * tmplatNm attribute 값을 설정한다.
     *
     * @param tmplatNm
     *            the tmplatNm to set
     */
    public void setTmplatNm(String tmplatNm) {
	this.tmplatNm = tmplatNm;
    }

    /**
     * lastUpdusrNm attribute를 리턴한다.
     *
     * @return the lastUpdusrNm
     */
    public String getLastUpdusrNm() {
	return lastUpdusrNm;
    }

    /**
     * lastUpdusrNm attribute 값을 설정한다.
     *
     * @param lastUpdusrNm
     *            the lastUpdusrNm to set
     */
    public void setLastUpdusrNm(String lastUpdusrNm) {
	this.lastUpdusrNm = lastUpdusrNm;
    }

    /**
     * authFlag attribute를 리턴한다.
     *
     * @return the authFlag
     */
    public String getAuthFlag() {
	return authFlag;
    }

    /**
     * authFlag attribute 값을 설정한다.
     *
     * @param authFlag
     *            the authFlag to set
     */
    public void setAuthFlag(String authFlag) {
	this.authFlag = authFlag;
    }

    /**
     * tmplatCours attribute를 리턴한다.
     *
     * @return the tmplatCours
     */
    public String getTmplatCours() {
	return tmplatCours;
    }

    /**
     * tmplatCours attribute 값을 설정한다.
     *
     * @param tmplatCours
     *            the tmplatCours to set
     */
    public void setTmplatCours(String tmplatCours) {
	this.tmplatCours = tmplatCours;
    }

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
