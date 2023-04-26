package egovframework.com.cmm.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Class Name : FileVO.java
 * @Description : 파일정보 처리를 위한 VO 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 25.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
@Getter
@Setter
public class FileVO implements Serializable {

    /**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -287950405903719128L;
	/**
     * 첨부파일 아이디
     */
    public String atchFileId = "";
    /**
     * 생성일자
     */
    public String creatDt = "";
    /**
     * 파일내용
     */
    public String fileCn = "";
    /**
     * 파일확장자
     */
    public String fileExtsn = "";
    /**
     * 파일크기
     */
    public String fileMg = "";
    /**
     * 파일연번
     */
    public String fileSn = "";
    /**
     * 파일저장경로
     */
    public String fileStreCours = "";
    /**
     * 원파일명
     */
    public String orignlFileNm = "";
    /**
     * 저장파일명
     */
    public String streFileNm = "";

    /**
     * toString 메소드를 대치한다.
     */
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }

}
