package egovframework.let.cop.smt.sim.service;

import java.io.Serializable;

/**
 * 일정관리 VO Class 구현
 * @since 2009.04.10
 * @see
 * <pre>
 * << 개정이력(Modification Information) >>  수정일      수정자           수정내용 -------    ---
 * -----    --------------------------- 2009.04.10  장동한          최초 생성 2011.05.31
 * JJY           경량환경 커스터마이징버전 생성
 * </pre>
 * @author 조재영
 * @version 1.0
 * @created 09-6-2011 오전 10:08:07
 */
public class IndvdlSchdulManageVO implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 일정ID */
	private String schdulId;
	
	/** 일정구분(회의/교육/세미나/강의 기타) */
	private String schdulSe;
	
	/** 일정부서ID */
	private String schdulDeptId;
	
	/** 일정종류(부서일정/개인일정) */
	private String schdulKindCode;
	
	/** 일정시작일자 */
	private String schdulBgnde;
	
	/** 일정종료일자 */
	private String schdulEndde;
	
	/** 일정명 */
	private String schdulNm;
	
	/** 일정내용 */
	private String schdulCn;
	
	/** 일정장소 */
	private String schdulPlace;
	
	/** 일정중요도코드 */
	private String schdulIpcrCode;
	
	/** 일정담담자ID */
	private String schdulChargerId;
	
	/** 첨부파일ID */
	private String atchFileId;
	
	/** 반복구분(반복, 연속, 요일반복) */
	private String reptitSeCode;
	
	/** 최초등록시점 */
	private String frstRegisterPnttm = "";
	
	/** 최초등록자ID */
	private String frstRegisterId = "";
	
	/** 최종수정시점 */
	private String lastUpdusrPnttm = "";
	
	/** 최종수정ID */
	private String lastUpdusrId = "";
	
	/** 일정시작일자(시간) */
	private String schdulBgndeHH = "";
	
	/** 일정시작일자(분) */
	private String schdulBgndeMM = "";
	
	/** 일정종료일자(시간) */
	private String schdulEnddeHH = "";
	
	/** 일정종료일자(분) */
	private String schdulEnddeMM = "";
	
	/** 일정시작일자(Year/Month/Day) */
	private String schdulBgndeYYYMMDD = "";
	
	/** 일정종료일자(Year/Month/Day) */
	private String schdulEnddeYYYMMDD = "";
	
	/** 담당부서 */
	private String schdulDeptName = "";
	
	/** 담당자명 */
	private String schdulChargerName = "";

	/**
	 * schdulId attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulId() {
		return schdulId;
	}

	/**
	 * schdulId attribute 값을 설정한다.
	 * @return schdulId String
	 */
	public void setSchdulId(String schdulId) {
		this.schdulId = schdulId;
	}

	/**
	 * schdulSe attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulSe() {
		return schdulSe;
	}

	/**
	 * schdulSe attribute 값을 설정한다.
	 * @return schdulSe String
	 */
	public void setSchdulSe(String schdulSe) {
		this.schdulSe = schdulSe;
	}

	/**
	 * schdulDeptId attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulDeptId() {
		return schdulDeptId;
	}

	/**
	 * schdulDeptId attribute 값을 설정한다.
	 * @return schdulDeptId String
	 */
	public void setSchdulDeptId(String schdulDeptId) {
		this.schdulDeptId = schdulDeptId;
	}

	/**
	 * schdulKindCode attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulKindCode() {
		return schdulKindCode;
	}

	/**
	 * schdulKindCode attribute 값을 설정한다.
	 * @return schdulKindCode String
	 */
	public void setSchdulKindCode(String schdulKindCode) {
		this.schdulKindCode = schdulKindCode;
	}

	/**
	 * schdulBgnde attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulBgnde() {
		return schdulBgnde;
	}

	/**
	 * schdulBgnde attribute 값을 설정한다.
	 * @return schdulBgnde String
	 */
	public void setSchdulBgnde(String schdulBgnde) {
		this.schdulBgnde = schdulBgnde;
	}

	/**
	 * schdulEndde attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulEndde() {
		return schdulEndde;
	}

	/**
	 * schdulEndde attribute 값을 설정한다.
	 * @return schdulEndde String
	 */
	public void setSchdulEndde(String schdulEndde) {
		this.schdulEndde = schdulEndde;
	}

	/**
	 * schdulNm attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulNm() {
		return schdulNm;
	}

	/**
	 * schdulNm attribute 값을 설정한다.
	 * @return schdulNm String
	 */
	public void setSchdulNm(String schdulNm) {
		this.schdulNm = schdulNm;
	}

	/**
	 * schdulCn attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulCn() {
		return schdulCn;
	}

	/**
	 * schdulCn attribute 값을 설정한다.
	 * @return schdulCn String
	 */
	public void setSchdulCn(String schdulCn) {
		this.schdulCn = schdulCn;
	}

	/**
	 * schdulPlace attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulPlace() {
		return schdulPlace;
	}

	/**
	 * schdulPlace attribute 값을 설정한다.
	 * @return schdulPlace String
	 */
	public void setSchdulPlace(String schdulPlace) {
		this.schdulPlace = schdulPlace;
	}

	/**
	 * schdulIpcrCode attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulIpcrCode() {
		return schdulIpcrCode;
	}

	/**
	 * schdulIpcrCode attribute 값을 설정한다.
	 * @return schdulIpcrCode String
	 */
	public void setSchdulIpcrCode(String schdulIpcrCode) {
		this.schdulIpcrCode = schdulIpcrCode;
	}

	/**
	 * schdulChargerId attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulChargerId() {
		return schdulChargerId;
	}

	/**
	 * schdulChargerId attribute 값을 설정한다.
	 * @return schdulChargerId String
	 */
	public void setSchdulChargerId(String schdulChargerId) {
		this.schdulChargerId = schdulChargerId;
	}

	/**
	 * atchFileId attribute 를 리턴한다.
	 * @return the String
	 */
	public String getAtchFileId() {
		return atchFileId;
	}

	/**
	 * atchFileId attribute 값을 설정한다.
	 * @return atchFileId String
	 */
	public void setAtchFileId(String atchFileId) {
		this.atchFileId = atchFileId;
	}

	/**
	 * reptitSeCode attribute 를 리턴한다.
	 * @return the String
	 */
	public String getReptitSeCode() {
		return reptitSeCode;
	}

	/**
	 * reptitSeCode attribute 값을 설정한다.
	 * @return reptitSeCode String
	 */
	public void setReptitSeCode(String reptitSeCode) {
		this.reptitSeCode = reptitSeCode;
	}

	/**
	 * frstRegisterPnttm attribute 를 리턴한다.
	 * @return the String
	 */
	public String getFrstRegisterPnttm() {
		return frstRegisterPnttm;
	}

	/**
	 * frstRegisterPnttm attribute 값을 설정한다.
	 * @return frstRegisterPnttm String
	 */
	public void setFrstRegisterPnttm(String frstRegisterPnttm) {
		this.frstRegisterPnttm = frstRegisterPnttm;
	}

	/**
	 * frstRegisterId attribute 를 리턴한다.
	 * @return the String
	 */
	public String getFrstRegisterId() {
		return frstRegisterId;
	}

	/**
	 * frstRegisterId attribute 값을 설정한다.
	 * @return frstRegisterId String
	 */
	public void setFrstRegisterId(String frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}

	/**
	 * lastUpdusrPnttm attribute 를 리턴한다.
	 * @return the String
	 */
	public String getLastUpdusrPnttm() {
		return lastUpdusrPnttm;
	}

	/**
	 * lastUpdusrPnttm attribute 값을 설정한다.
	 * @return lastUpdusrPnttm String
	 */
	public void setLastUpdusrPnttm(String lastUpdusrPnttm) {
		this.lastUpdusrPnttm = lastUpdusrPnttm;
	}

	/**
	 * lastUpdusrId attribute 를 리턴한다.
	 * @return the String
	 */
	public String getLastUpdusrId() {
		return lastUpdusrId;
	}

	/**
	 * lastUpdusrId attribute 값을 설정한다.
	 * @return lastUpdusrId String
	 */
	public void setLastUpdusrId(String lastUpdusrId) {
		this.lastUpdusrId = lastUpdusrId;
	}

	/**
	 * schdulBgndeHH attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulBgndeHH() {
		return schdulBgndeHH;
	}

	/**
	 * schdulBgndeHH attribute 값을 설정한다.
	 * @return schdulBgndeHH String
	 */
	public void setSchdulBgndeHH(String schdulBgndeHH) {
		this.schdulBgndeHH = schdulBgndeHH;
	}

	/**
	 * schdulBgndeMM attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulBgndeMM() {
		return schdulBgndeMM;
	}

	/**
	 * schdulBgndeMM attribute 값을 설정한다.
	 * @return schdulBgndeMM String
	 */
	public void setSchdulBgndeMM(String schdulBgndeMM) {
		this.schdulBgndeMM = schdulBgndeMM;
	}

	/**
	 * schdulEnddeHH attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulEnddeHH() {
		return schdulEnddeHH;
	}

	/**
	 * schdulEnddeHH attribute 값을 설정한다.
	 * @return schdulEnddeHH String
	 */
	public void setSchdulEnddeHH(String schdulEnddeHH) {
		this.schdulEnddeHH = schdulEnddeHH;
	}

	/**
	 * schdulEnddeMM attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulEnddeMM() {
		return schdulEnddeMM;
	}

	/**
	 * schdulEnddeMM attribute 값을 설정한다.
	 * @return schdulEnddeMM String
	 */
	public void setSchdulEnddeMM(String schdulEnddeMM) {
		this.schdulEnddeMM = schdulEnddeMM;
	}

	/**
	 * schdulBgndeYYYMMDD attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulBgndeYYYMMDD() {
		return schdulBgndeYYYMMDD;
	}

	/**
	 * schdulBgndeYYYMMDD attribute 값을 설정한다.
	 * @return schdulBgndeYYYMMDD String
	 */
	public void setSchdulBgndeYYYMMDD(String schdulBgndeYYYMMDD) {
		this.schdulBgndeYYYMMDD = schdulBgndeYYYMMDD;
	}

	/**
	 * schdulEnddeYYYMMDD attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulEnddeYYYMMDD() {
		return schdulEnddeYYYMMDD;
	}

	/**
	 * schdulEnddeYYYMMDD attribute 값을 설정한다.
	 * @return schdulEnddeYYYMMDD String
	 */
	public void setSchdulEnddeYYYMMDD(String schdulEnddeYYYMMDD) {
		this.schdulEnddeYYYMMDD = schdulEnddeYYYMMDD;
	}

	/**
	 * schdulDeptName attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulDeptName() {
		return schdulDeptName;
	}

	/**
	 * schdulDeptName attribute 값을 설정한다.
	 * @return schdulDeptName String
	 */
	public void setSchdulDeptName(String schdulDeptName) {
		this.schdulDeptName = schdulDeptName;
	}

	/**
	 * schdulChargerName attribute 를 리턴한다.
	 * @return the String
	 */
	public String getSchdulChargerName() {
		return schdulChargerName;
	}

	/**
	 * schdulChargerName attribute 값을 설정한다.
	 * @return schdulChargerName String
	 */
	public void setSchdulChargerName(String schdulChargerName) {
		this.schdulChargerName = schdulChargerName;
	}
	
	
}
