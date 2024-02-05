package egovframework.let.utl.fcc.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @Class Name  : EgovFormBasedFileVo.java
 * @Description : Form-based File Upload VO
 * @Modification Information
 * 
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.08.26       한성곤                  최초 생성
 *
 * @author 공통컴포넌트 개발팀 한성곤
 * @since 2009.08.26
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) 2008 by MOPAS  All right reserved.
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class EgovFormBasedFileVo implements Serializable {
    /** 파일명 */
    private String fileName = "";
    /** ContextType */
    private String contentType = "";
    /** 하위 디렉토리 지정 */
    private String serverSubPath = "";
    /** 물리적 파일명 */
    private String physicalName = "";
    /** 파일 사이즈 */
    private long size = 0L;
    
}