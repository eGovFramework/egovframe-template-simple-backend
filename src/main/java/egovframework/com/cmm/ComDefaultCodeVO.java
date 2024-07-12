package egovframework.com.cmm;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *  클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------       --------    ---------------------------
 *   2009.3.11   이삼섭          최초 생성
 *
 * </pre>
 */
@Getter
@Setter
public class ComDefaultCodeVO implements Serializable {
    /**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -2020648489890016404L;

	/** 코드 ID */
    private String codeId = "";

    /** 상세코드 */
    private String code = "";

    /** 코드명 */
    private String codeNm = "";

    /** 코드설명 */
    private String codeDc = "";

    /** 특정테이블명 */
    private String tableNm = "";	//특정테이블에서 코드정보를추출시 사용

    /** 상세 조건 여부 */
    private String haveDetailCondition = "N";

    /** 상세 조건 */
    private String detailCondition = "";

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}