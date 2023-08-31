package egovframework.com.cmm;

import org.egovframe.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.cmm
 * @filename : EgovComOthersExcepHndlr.java
 * @modificationInformation
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일            수정자             수정내용
 *  ----------   ----------   ----------------------
 *  2023. 8. 9.    2023. 8. 9.   주석추가
 * </pre>
 *
 *
 */

@Slf4j
public class EgovComOthersExcepHndlr implements ExceptionHandler {

    public void occur(Exception exception, String packageName) {
    	//log.debug(" EgovServiceExceptionHandler run...............");
    	log.error(packageName, exception);
    }
}
