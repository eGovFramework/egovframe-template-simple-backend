package egovframework.let.cop.com.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 템플릿 정보를 관리하기 위한 모델 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.03.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.17  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
public class TemplateInf implements Serializable {

    /**
	 * serialVersion UID
	 */
	private static final long serialVersionUID = 600640077510261030L;

	/** 최초등록자 아이디 */
    private String frstRegisterId = "";

    /** 최초등록시점 */
    private String frstRegisterPnttm = "";

    /** 최종수정자 아이디 */
    private String lastUpdusrId = "";

    /** 최종수정시점 */
    private String lastUpdusrPnttm = "";

    /** 템플릿 경로 */
    private String tmplatCours = "";

    /** 템플릿 아이디 */
    private String tmplatId = "";

    /** 템플릿 명 */
    private String tmplatNm = "";

    /** 탬플릿 구분코드 */
    private String tmplatSeCode = "";

    /** 사용여부 */
    private String useAt = "";

    /**
     * frstRegisterId attribute를 리턴한다.
     *
     * @return the frstRegisterId
     */
    public String getFrstRegisterId() {
	return frstRegisterId;
    }

    /**
     * frstRegisterId attribute 값을 설정한다.
     *
     * @param frstRegisterId
     *            the frstRegisterId to set
     */
    public void setFrstRegisterId(String frstRegisterId) {
	this.frstRegisterId = frstRegisterId;
    }

    /**
     * frstRegisterPnttm attribute를 리턴한다.
     *
     * @return the frstRegisterPnttm
     */
    public String getFrstRegisterPnttm() {
	return frstRegisterPnttm;
    }

    /**
     * frstRegisterPnttm attribute 값을 설정한다.
     *
     * @param frstRegisterPnttm
     *            the frstRegisterPnttm to set
     */
    public void setFrstRegisterPnttm(String frstRegisterPnttm) {
	this.frstRegisterPnttm = frstRegisterPnttm;
    }

    /**
     * lastUpdusrId attribute를 리턴한다.
     *
     * @return the lastUpdusrId
     */
    public String getLastUpdusrId() {
	return lastUpdusrId;
    }

    /**
     * lastUpdusrId attribute 값을 설정한다.
     *
     * @param lastUpdusrId
     *            the lastUpdusrId to set
     */
    public void setLastUpdusrId(String lastUpdusrId) {
	this.lastUpdusrId = lastUpdusrId;
    }

    /**
     * lastUpdusrPnttm attribute를 리턴한다.
     *
     * @return the lastUpdusrPnttm
     */
    public String getLastUpdusrPnttm() {
	return lastUpdusrPnttm;
    }

    /**
     * lastUpdusrPnttm attribute 값을 설정한다.
     *
     * @param lastUpdusrPnttm
     *            the lastUpdusrPnttm to set
     */
    public void setLastUpdusrPnttm(String lastUpdusrPnttm) {
	this.lastUpdusrPnttm = lastUpdusrPnttm;
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
     * tmplatId attribute를 리턴한다.
     *
     * @return the tmplatId
     */
    public String getTmplatId() {
	return tmplatId;
    }

    /**
     * tmplatId attribute 값을 설정한다.
     *
     * @param tmplatId
     *            the tmplatId to set
     */
    public void setTmplatId(String tmplatId) {
	this.tmplatId = tmplatId;
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
     * tmplatSeCode attribute를 리턴한다.
     *
     * @return the tmplatSeCode
     */
    public String getTmplatSeCode() {
	return tmplatSeCode;
    }

    /**
     * tmplatSeCode attribute 값을 설정한다.
     *
     * @param tmplatSeCode
     *            the tmplatSeCode to set
     */
    public void setTmplatSeCode(String tmplatSeCode) {
	this.tmplatSeCode = tmplatSeCode;
    }

    /**
     * useAt attribute를 리턴한다.
     *
     * @return the useAt
     */
    public String getUseAt() {
	return useAt;
    }

    /**
     * useAt attribute 값을 설정한다.
     *
     * @param useAt
     *            the useAt to set
     */
    public void setUseAt(String useAt) {
	this.useAt = useAt;
    }

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
