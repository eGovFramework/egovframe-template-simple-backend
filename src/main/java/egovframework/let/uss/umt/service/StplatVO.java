package egovframework.let.uss.umt.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 가입약관VO클래스로서가입약관확인시 비지니스로직 처리용 항목을 구성한다.
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
public class StplatVO implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 약관아이디*/
    private String useStplatId;
	
    /** 사용약관안내*/
    private String useStplatCn;
    
    /** 정보동의안내*/
    private String infoProvdAgeCn;
    
    /**
	 * useStplatId attribute 값을  리턴한다.
	 * @return String
	 */
	public String getUseStplatId() {
		return useStplatId;
	}

	/**
	 * useStplatId attribute 값을 설정한다.
	 * @param useStplatId String
	 */
	public void setUseStplatId(String useStplatId) {
		this.useStplatId = useStplatId;
	}

	/**
	 * useStplatCn attribute 값을  리턴한다.
	 * @return String
	 */
	public String getUseStplatCn() {
		return useStplatCn;
	}

	/**
	 * useStplatCn attribute 값을 설정한다.
	 * @param useStplatCn String
	 */
	public void setUseStplatCn(String useStplatCn) {
		this.useStplatCn = useStplatCn;
	}

	/**
	 * infoProvdAgeCn attribute 값을  리턴한다.
	 * @return String
	 */
	public String getInfoProvdAgeCn() {
		return infoProvdAgeCn;
	}

	/**
	 * infoProvdAgeCn attribute 값을 설정한다.
	 * @param infoProvdAgeCn String
	 */
	public void setInfoProvdAgeCn(String infoProvdAgeCn) {
		this.infoProvdAgeCn = infoProvdAgeCn;
	}

	/**
     * toString 메소드를 대치한다.
     */
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
    
}