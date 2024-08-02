package egovframework.let.uss.umt.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 일반회원VO클래스로서 일반회원관리 비지니스로직 처리용 항목을 구성한다.
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
 *   2009.04.10  JJY            최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성 
 *	 2024.07.24	 김일국			스프링부트 롬복사용으로 변경
 * </pre>
 */
@Schema(description = "회원 VO")
@Getter
@Setter
public class MberManageVO extends UserDefaultVO implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Schema(description = "이전비밀번호")
    private String oldPassword = "";
    
	@Schema(description = "사용자고유아이디")
	private String uniqId="";

	@Schema(description = "사용자 유형")
	private String userTy="";

	@Schema(description = "주소")
	private String adres="";

	@Schema(description = "상세주소")
	private String detailAdres="";

	@Schema(description = "끝전화번호")
	private String endTelno="";

	@Schema(description = "팩스번호")
	private String mberFxnum="";

	@Schema(description = "조직 ID")
	private String orgnztId="";
	
	@Schema(description = "그룹 ID")
	private String groupId="";

	@Schema(description = "주민등록번호")
	private String ihidnum="";

	@Schema(description = "성별코드")
	private String sexdstnCode="";

	@Schema(description = "회원 ID")
	private String mberId;

	@Schema(description = "회원명")
	private String mberNm;

	@Schema(description = "회원상태")
	private String mberSttus;

	@Schema(description = "지역번호")
	private String areaNo="";

	@Schema(description = "중간전화번호")
	private String middleTelno="";

	@Schema(description = "핸드폰번호")
	private String moblphonNo="";

	@Schema(description = "비밀번호")
	private String password;

	@Schema(description = "비밀번호 정답")
	private String passwordCnsr="";

	@Schema(description = "비밀번호 힌트")
	private String passwordHint="";

	@Schema(description = "가입 일자")
	private String sbscrbDe;

	@Schema(description = "우편번호")
	private String zip="";

	@Schema(description = "이메일주소")
	private String mberEmailAdres="";
	
	/**
	 * toString 메소드를 대치한다.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}