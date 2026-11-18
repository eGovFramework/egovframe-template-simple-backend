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
 *  2026.07.19   EricSeokgon  성능: 상수 정규식 반복 컴파일 제거 / 정확성: clearXSSMaximum %00 replacement NULL 및 fileInjectPathReplaceAll 경로 제거 결함 수정
 * </pre>
 */

public class EgovWebUtil {

	// 성능: 매 호출마다 Pattern.compile이 반복되지 않도록 정규식은 클래스 로딩 시 1회만 컴파일한다.
	// (정규식 의미가 없는 문자 그대로의 치환은 String.replace 로 대체 — 패턴 컴파일 자체가 불필요)
	private static final Pattern SPACE_PATTERN = Pattern.compile("\\p{Space}");
	private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

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

		returnValue = returnValue.replace("&", "&amp;");
		returnValue = returnValue.replace("<", "&lt;");
		returnValue = returnValue.replace(">", "&gt;");
		returnValue = returnValue.replace("\"", "&#34;");
		returnValue = returnValue.replace("\'", "&#39;");
		returnValue = returnValue.replace(".", "&#46;");
		returnValue = returnValue.replace("%2E", "&#46;");
		returnValue = returnValue.replace("%2F", "&#47;");
		return returnValue;
	}

	public static String clearXSSMaximum(String value) {
		String returnValue = value;
		returnValue = clearXSSMinimum(returnValue);

		// 정확성: 기존 코드는 replaceAll("%00", null) 로 replacement 에 null 을 넘겨
		// 입력에 "%00" 이 포함되면 NullPointerException 이 발생했다("%00" 제거가 의도).
		returnValue = returnValue.replace("%00", "");

		returnValue = returnValue.replace("%", "&#37;");

		// \\. => .

		returnValue = returnValue.replace("../", ""); // ../
		returnValue = returnValue.replace("..\\", ""); // ..\
		returnValue = returnValue.replace("./", ""); // ./
		returnValue = returnValue.replace("%2F", "");

		return returnValue;
	}

	public static String filePathBlackList(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replace("..", "");

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

		returnValue = returnValue.replace("/", "");
		returnValue = returnValue.replace("\\", ""); // \
		returnValue = returnValue.replace("..", ""); // ..
		returnValue = returnValue.replace("&", "");

		return returnValue;
	}
	
	public static String fileInjectPathReplaceAll(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		// 정확성: 기존 정규식 "\\.." 은 주석("// ..")과 달리 '.' + 임의 1문자를 의미해
		// report.txt -> reportxt 처럼 정상 파일명을 훼손했다. 또한 구분자를 나중에 제거하면
		// ".\." 같은 입력이 ".." 로 되살아난다. filePathReplaceAll 과 동일하게
		// 구분자(/,\)를 먼저 제거한 뒤 문자 그대로의 ".." 을 제거한다.
		returnValue = returnValue.replace("/", "");
		returnValue = returnValue.replace("\\", ""); // \
		returnValue = returnValue.replace("..", ""); // ..
		returnValue = returnValue.replace("&", "");

		return returnValue;
	}

	public static String filePathWhiteList(String value) {
		return value;
	}

	public static boolean isIPAddress(String str) {
		return IP_PATTERN.matcher(str).matches();
    }

	public static String removeCRLF(String parameter) {
		return parameter.replace("\r", "").replace("\n", "");
	}

	public static String removeSQLInjectionRisk(String parameter) {
		return SPACE_PATTERN.matcher(parameter).replaceAll("").replace("*", "").replace("%", "").replace(";", "").replace("-", "").replace("+", "").replace(",", "");
	}

	public static String removeOSCmdRisk(String parameter) {
		return SPACE_PATTERN.matcher(parameter).replaceAll("").replace("*", "").replace("|", "").replace(";", "");
	}
}
