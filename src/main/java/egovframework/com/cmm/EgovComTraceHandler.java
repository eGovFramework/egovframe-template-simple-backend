package egovframework.com.cmm;

import org.egovframe.rte.fdl.cmmn.trace.handler.TraceHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @Class Name : EgovComTraceHandler.java
 * @Description : 공통서비스의 trace 처리 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2011. 09. 30.     JJY
 *
 * @author JJY
 * @since 2011. 9. 30.
 *
 */
@Slf4j
public class EgovComTraceHandler implements TraceHandler {

    /**
     * 발생된 메시지를 출력한다.
     */
    public void todo(Class<?> clazz, String message) {
    	//log.debug("log ==> DefaultTraceHandler run...............");
    	log.debug("[TRACE]CLASS::: {}", clazz.getName());
    	log.debug("[TRACE]MESSAGE::: {}", message);
    	//이곳에서 후속처리로 필요한 액션을 취할 수 있다.
    }
}
