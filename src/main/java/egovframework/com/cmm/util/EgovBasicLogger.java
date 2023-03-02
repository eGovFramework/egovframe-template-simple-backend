package egovframework.com.cmm.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class  to support to logging information
 * @author Vincent Han
 * @since 2014.09.18
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일        수정자       수정내용
 *  -------       --------    ---------------------------
 *   2014.09.18	표준프레임워크센터	최초 생성
 *
 * </pre>
 */
public class EgovBasicLogger {
	private static final Level IGNORE_INFO_LEVEL = Level.OFF;
	private static final Level DEBUG_INFO_LEVEL = Level.FINEST;
	private static final Level INFO_INFO_LEVEL = Level.INFO;
	
	private static final Logger ignoreLogger = Logger.getLogger("ignore");
	private static final Logger debugLogger = Logger.getLogger("debug");
	private static final Logger infoLogger = Logger.getLogger("info");
	
	/**
	 * 기록이나 처리가 불필요한 경우 사용.
	 * @param message
	 * @param exception
	 */
	public static void ignore(String message, Exception exception) {
		if (exception == null) {
			ignoreLogger.log(IGNORE_INFO_LEVEL, message);
		} else {
			ignoreLogger.log(IGNORE_INFO_LEVEL, message, exception);
		}
	}
	
	/**
	 * 기록이나 처리가 불필요한 경우 사용.
	 * @param message
	 * @param exception
	 */
	public static void ignore(String message) {
		ignore(message, null);
	}
	
	/**
	 * 디버그 정보를 기록하는 경우 사용.
	 * @param message
	 * @param exception
	 */
	public static void debug(String message, Exception exception) {
		if (exception == null) {
			debugLogger.log(DEBUG_INFO_LEVEL, message);
		} else {
			debugLogger.log(DEBUG_INFO_LEVEL, message, exception);
		}
	}
	
	/**
	 * 디버그 정보를 기록하는 경우 사용.
	 * @param message
	 * @param exception
	 */
	public static void debug(String message) {
		debug(message, null);
	}
	
	/**
	 * 일반적이 정보를 기록하는 경우 사용.
	 * @param message
	 * @param exception
	 */
	public static void info(String message) {
		infoLogger.log(INFO_INFO_LEVEL, message);
	}
}
