package egovframework.let.cop.bbs.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 게시판 속성정보를 담기위한 엔티티 클래스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.12  이삼섭          최초 생성
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@Getter
@Setter
public class BoardMaster implements Serializable {

    /**
	 * serialVersion UID
	 */
	private static final long serialVersionUID = 2821358749509367821L;

	/** 게시판 속성코드 */
    private String bbsAttrbCode = "";

    /** 게시판 아이디 */
    private String bbsId = "";

    /** 게시판 소개 */
    private String bbsIntrcn = "";

    /** 게시판 명 */
    private String bbsNm = "";

    /** 게시판 유형코드 */
    private String bbsTyCode = "";

    /** 파일첨부가능여부 */
    private String fileAtchPosblAt = "";

    /** 최초등록자 아이디 */
    private String frstRegisterId = "";

    /** 최초등록시점 */
    private String frstRegisterPnttm = "";

    /** 최종수정자 아이디 */
    public String lastUpdusrId = "";

    /** 최종수정시점 */
    private String lastUpdusrPnttm = "";

    /** 첨부가능파일숫자 */
    private int posblAtchFileNumber = 0;

    /** 첨부가능파일사이즈 */
    private String posblAtchFileSize = "";

    /** 답장가능여부 */
    private String replyPosblAt = "";

    /** 템플릿 아이디 */
    private String tmplatId = "";

    /** 사용여부 */
    private String useAt = "";

    /** 사용플래그 */
    private String bbsUseFlag = "";

    /** 대상 아이디 */
    private String trgetId = "";

    /** 등록구분코드 */
    private String registSeCode = "";

    /** 유일 아이디 */
    private String uniqId = "";

    /** 템플릿 명 */
    private String tmplatNm = "";

    //---------------------------------
    // 2009.06.26 : 2단계 기능 추가
    //---------------------------------
    /** 추가 option (댓글-comment, 만족도조사-stsfdg) */
    private String option = "";

    /** 댓글 여부 */
    private String commentAt = "";

    /** 만족도조사 */
    private String stsfdgAt = "";
    ////-------------------------------

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}