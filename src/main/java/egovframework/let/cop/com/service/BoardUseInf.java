package egovframework.let.cop.com.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 게시판의 이용정보를 관리하기 위한 모델 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.02  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 커스터마이징버전 생성
 *
 * </pre>
 */
@Schema(description = "게시판 이용정보 모델")
@Getter
@Setter
public class BoardUseInf implements Serializable {

    /**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -8164785314697750055L;

	@Schema(description = "게시판 아이디")
    private String bbsId = "";

	@Schema(description = "대상시스템 아이디")
    private String trgetId = "";

	@Schema(description = "대상 구분 (커뮤니티, 동호회)")
    private String trgetType = "";

	@Schema(description = "최초 등록자 아이디")
    private String frstRegisterId = "";

	@Schema(description = "최초등록시점")
    private String frstRegisterPnttm = "";

	@Schema(description = "최종수정자 아이디")
    private String lastUpdusrId = "";

	@Schema(description = "최종수정시점")
    private String lastUpdusrPnttm = "";

	@Schema(description = "등록구분코드")
    private String registSeCode = "";

	@Schema(description = "사용여부", allowableValues = {"Y", "N"})
    private String useAt = "";


    
    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
