package egovframework.let.sym.cal.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 휴일 모델 클래스
 * @author 공통서비스 개발팀 이중호
 * @since 2009.04.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.01  이중호          최초 생성
 *
 * </pre>
 */
@Getter
@Setter
public class Restde implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 휴일번호
	 */
    private int    restdeNo       = 0;
    
    /*
     * 휴일일자
     */
    private String restdeDe       = "";
    
    /*
     * 휴일명
     */
    private String restdeNm       = "";
    
    /*
     * 휴일설명
     */
    private String restdeDc       = "";
    
    /*
     * 휴일구분
     */
    private String restdeSe       = "";
    
    /*
     * 휴일구분코드
     */
    private String restdeSeCode   = "";
    
    /*
     * 최초등록자ID
     */
    private String frstRegisterId = "";
    
    /*
     * 최종수정자ID
     */
    private String lastUpdusrId   = "";

    /*
     * 년
     */
    private String year           = "";
    
    /*
     * 월
     */
    private String month          = "";
    
    /*
     * 일
     */
    private String day            = "";
    
    /*
     * 휴일여부
     */
    private String restdeAt       = "";

    /*
     * 달력셀
     */
	private int    cellNum        = 0;
	
	/*
	 * 월별 주순위
	 */
    private int    weeks          = 0;
    
    /*
     * 월 주수
     */
    private int maxWeeks = 0;
    
    /*
     * 요일
     */
    private int    week           = 0;
    
    /*
     * 시작요일 
     */
    private int    startWeekMonth = 0;
    
    /*
     * 마지막 일자
     */
    private int    lastDayMonth   = 0;
    
}
