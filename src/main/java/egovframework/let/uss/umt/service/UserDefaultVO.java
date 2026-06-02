package egovframework.let.uss.umt.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Getter;
import lombok.Setter;

/**
 * 사용자정보 VO클래스로서일반회원, 기업회원, 업무사용자의  비지니스로직 처리시 기타조건성 항목을 구성한다.
 * @author 공통서비스 개발팀 조재영
 * @since 2009.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  조재영          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Getter
@Setter
public class UserDefaultVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 검색조건-회원상태     (0, A, D, P)*/
    private String sbscrbSttus = "0";

	/** 검색조건 */
    private String searchCondition = "";

    /** 검색Keyword */
    private String searchKeyword = "";

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

	/**
     * toString 메소드를 대치한다.
     */
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }

}
