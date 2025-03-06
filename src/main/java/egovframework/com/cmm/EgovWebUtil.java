package egovframework.com.cmm;

import egovframework.com.cmm.service.ResultVO;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 교차접속 스크립트 공격 취약성 방지(파라미터 문자열 교체)
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일         수정자      수정내용
 *  ----------   --------   ---------------------------
 *  2011.10.10   한성곤       최초 생성
 *	2017-02-07   이정은       시큐어코딩(ES) - 시큐어코딩 경로 조작 및 자원 삽입[CWE-22, CWE-23, CWE-95, CWE-99]
 *  2018.08.17   신용호       filePathBlackList 수정
 *  2018.10.10   신용호       . => \\.으로 수정
 *  2024.12.04   신용호       filePathBlackList() basePath 파라미터 추가
 * </pre>
 */

public class EgovWebUtil {


	public static ResultVO handleAuthError(int code, String msg) {
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode(code);
		resultVO.setResultMessage(msg);
		return resultVO;
	}
	public static String clearXSSMinimum(String value) {
		if (value == null || value.trim().equals("")) {
			return "";
		}

		String returnValue = value;

		returnValue = returnValue.replaceAll("&", "&amp;");
		returnValue = returnValue.replaceAll("<", "&lt;");
		returnValue = returnValue.replaceAll(">", "&gt;");
		returnValue = returnValue.replaceAll("\"", "&#34;");
		returnValue = returnValue.replaceAll("\'", "&#39;");
		returnValue = returnValue.replaceAll("\\.", "&#46;");
		returnValue = returnValue.replaceAll("%2E", "&#46;");
		returnValue = returnValue.replaceAll("%2F", "&#47;");
		return returnValue;
	}

	public static String clearXSSMaximum(String value) {
		String returnValue = value;
		returnValue = clearXSSMinimum(returnValue);

		returnValue = returnValue.replaceAll("%00", null);

		returnValue = returnValue.replaceAll("%", "&#37;");

		// \\. => .

		returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
		returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\
		returnValue = returnValue.replaceAll("\\./", ""); // ./
		returnValue = returnValue.replaceAll("%2F", "");

		return returnValue;
	}

	public static String filePathBlackList(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("\\.\\.", "");

		return returnValue;
	}
	
	/**
	 * 파일경로 보안취약점 조치
	 * # 주의사항
	 * 1. basePath는 반드시 지정해야 한다.
	 * 2. basePath는 ROOT Path "/" 사용 금지 한다.
	 * 3. basePath 하위 디렉토리는 업로드한 파일이 존재하도록 구성하며 중요파일이 존재하지 않도록 관리한다.
	 *
	 * @param value 파일명
	 * @param basePath 기본 경로
	 * @return
	 */
	public static String filePathBlackList(String value, String basePath) {
		if ( basePath == null || "".equals(basePath) )
			throw new SecurityException("base path is empty.");
		if ( File.separator.equals(basePath) || "/".equals(basePath) )
			throw new SecurityException("base path does not allow Root.");
		return filePathBlackList(basePath + value);
	}

	/**
	 * 행안부 보안취약점 점검 조치 방안.
	 *
	 * @param value
	 * @return
	 */
	public static String filePathReplaceAll(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("/", "");
		returnValue = returnValue.replaceAll("\\\\", ""); // \
		returnValue = returnValue.replaceAll("\\.\\.", ""); // ..
		returnValue = returnValue.replaceAll("&", "");

		return returnValue;
	}
	
	public static String fileInjectPathReplaceAll(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		
		returnValue = returnValue.replaceAll("/", "");
		returnValue = returnValue.replaceAll("\\..", ""); // ..
		returnValue = returnValue.replaceAll("\\\\", "");// \
		returnValue = returnValue.replaceAll("&", "");

		return returnValue;
	}

	public static String filePathWhiteList(String value) {
		return value;
	}

	public static boolean isIPAddress(String str) {
		Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

		return ipPattern.matcher(str).matches();
    }

	public static String removeCRLF(String parameter) {
		return parameter.replaceAll("\r", "").replaceAll("\n", "");
	}

	public static String removeSQLInjectionRisk(String parameter) {
		return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("%", "").replaceAll(";", "").replaceAll("-", "").replaceAll("\\+", "").replaceAll(",", "");
	}

	public static String removeOSCmdRisk(String parameter) {
		return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("\\|", "").replaceAll(";", "");
	}

}