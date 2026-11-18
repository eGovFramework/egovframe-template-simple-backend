package egovframework.let.main.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 템플릿 메인화면 작업 List 항목 VO(Sample 소스)
 * @author 실행환경 개발팀 JJY
 * @since 2011.08.31
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.08.31  JJY            최초 생성
 *   2025.06.05  dasomell       Lombok @Getter/@Setter 적용
 *
 * </pre>
 */
@Getter
@Setter
public class EgovMainContentsVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2202175699511921484L;
	/**
	 * 작업항목 이름
	 */
	private String workItemName;
	/**
	 * To-Do List 항목 별 업무화면 URL
	 */
	private String workItemURL;

	/**
	 * 작업항목 개수 (기본값 0)
	 * @return 항목 개수
	 */
	public int getItemCount() {
		return 0;
	}

}
