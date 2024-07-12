package egovframework.com.cmm;

import lombok.Getter;
import lombok.Setter;

/**
 * IncludedInfo annotation을 바탕으로 화면에 표시할 정보를 구성하기 위한 VO 클래스
 * @author 공통컴포넌트 정진오
 * @since 2011.08.26
 * @version 2.0.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *  수정일		수정자		수정내용
 *  -------    	--------    ---------------------------
 *  2011.08.26	정진오 		최초 생성
 *
 * </pre>
 */
@Getter
@Setter
public class IncludedCompInfoVO {
	
	private String name;
	private String listUrl;
	private int order;
	private int gid;
	
	
}