package egovframework.com.cmm;

import org.egovframe.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @Class Name : EgovComExcepHndlr.java
 * @Description : 공통서비스의 exception 처리 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 13.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 13.
 * @version
 * @see
 *
 */
@Slf4j
public class EgovComExcepHndlr implements ExceptionHandler {


    /**
     * 발생된 Exception을 처리한다.
     */
    public void occur(Exception ex, String packageName) {
		log.debug("[HANDLER][PACKAGE]::: {}", packageName);
		log.debug("[HANDLER][Exception]:::", ex);
    }
}
