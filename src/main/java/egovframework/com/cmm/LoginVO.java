package egovframework.com.cmm;

import java.io.Serializable;

import javax.validation.constraints.Email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class Name : LoginVO.java
 * @Description : Login VO class
 * @Modification Information
 * @
 * @  수정일         수정자                   수정내용
 * @ -------    --------    ---------------------------
 * @ 2009.03.03    박지욱          최초 생성
 *
 *  @author 공통서비스 개발팀 박지욱
 *  @since 2009.03.03
 *  @version 1.0
 *  @see
 *  
 */
@Schema(description = "사용자 정보 VO")
@Getter
@Setter
public class LoginVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8274004534207618049L;
	
	@Schema(description = "아이디")
	private String id;
	
	@Schema(description = "이름")
	private String name;
	
	@Schema(description = "주민등록번호")
	private String ihidNum;
	
	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	@Schema(description = "이메일주소")
	private String email;
	
	@Schema(description = "비밀번호")
	private String password;
	
	@Schema(description = "비밀번호 힌트")
	private String passwordHint;
	
	@Schema(description = "비밀번호 정답")
	private String passwordCnsr;
	
	@Schema(description = "사용자 구분", allowableValues = {"GNR", "ENT", "USR", "ADM"}, defaultValue = "USR")
	private String userSe; //사용자 구분에 ADM 추가
	
	@Schema(description = "조직(부서)ID")
	private String orgnztId;
	
	@Schema(description = "조직(부서)명")
	private String orgnztNm;
	
	@Schema(description = "고유아이디")
	private String uniqId;
	
	@Schema(description = "로그인 후 이동할 페이지")
	private String url;
	
	@Schema(description = "사용자 IP정보")
	private String ip;
	
	@Schema(description = "GPKI인증 DN")
	private String dn;
	
	@Schema(description = "그룹ID") //권한 그룹ID 추가
	private String groupId;
	
	@Schema(description = "그룹명") //권한 그룹명 추가
	private String groupNm;
	
	
}
