package egovframework.com.cmm;

import egovframework.com.cmm.service.ResultVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EgovWebUtilTest {

    @DisplayName("handleAuthError 호출 시, 코드와 메시지가 담긴 ResultVO를 반환한다.")
    @Test
    void testHandleAuthError() {
        // given
        int code = 401;
        String msg = "인증 실패";

        // when
        ResultVO result = EgovWebUtil.handleAuthError(code, msg);

        // then
        assertEquals(code, result.getResultCode());
        assertEquals(msg, result.getResultMessage());
    }

    @DisplayName("clearXSSMinimum 호출 시, null 또는 빈 문자열이면 빈 문자열을 반환한다.")
    @Test
    void testClearXSSMinimumWithNullOrEmpty() {
        assertEquals("", EgovWebUtil.clearXSSMinimum(null));
        assertEquals("", EgovWebUtil.clearXSSMinimum(""));
        assertEquals("", EgovWebUtil.clearXSSMinimum("  "));
    }

    @DisplayName("clearXSSMinimum 호출 시, 특수문자를 HTML 엔티티로 치환한다.")
    @Test
    void testClearXSSMinimumEscapesSpecialChars() {
        // given
        String value = "<script>alert('x&y\".')</script>";

        // when
        String result = EgovWebUtil.clearXSSMinimum(value);

        // then
        assertEquals("&lt;script&gt;alert(&#39;x&amp;y&#34;&#46;&#39;)&lt;/script&gt;", result);
    }

    @DisplayName("clearXSSMaximum 호출 시, 홑 퍼센트 기호를 엔티티로 치환하고 특수문자를 치환한다.")
    @Test
    void testClearXSSMaximumEscapesPercentAndTags() {
        // given
        String value = "100% off <script>";

        // when
        String result = EgovWebUtil.clearXSSMaximum(value);

        // then
        assertEquals("100&#37; off &lt;script&gt;", result);
    }

    @DisplayName("filePathBlackList(value) 호출 시, null 또는 빈 문자열이면 빈 문자열을 반환한다.")
    @Test
    void testFilePathBlackListWithNullOrEmpty() {
        assertEquals("", EgovWebUtil.filePathBlackList((String) null));
        assertEquals("", EgovWebUtil.filePathBlackList(""));
    }

    @DisplayName("filePathBlackList(value) 호출 시, 상위 디렉토리 이동 패턴(..)을 제거한다.")
    @Test
    void testFilePathBlackListRemovesDotDot() {
        assertEquals("//etc/passwd", EgovWebUtil.filePathBlackList("../../etc/passwd"));
    }

    @DisplayName("filePathBlackList(value, basePath) 호출 시, basePath가 비어있으면 SecurityException이 발생한다.")
    @Test
    void testFilePathBlackListWithEmptyBasePathThrows() {
        assertThrows(SecurityException.class, () -> EgovWebUtil.filePathBlackList("file.txt", ""));
        assertThrows(SecurityException.class, () -> EgovWebUtil.filePathBlackList("file.txt", null));
    }

    @DisplayName("filePathBlackList(value, basePath) 호출 시, basePath가 루트 경로이면 SecurityException이 발생한다.")
    @Test
    void testFilePathBlackListWithRootBasePathThrows() {
        assertThrows(SecurityException.class, () -> EgovWebUtil.filePathBlackList("file.txt", "/"));
    }

    @DisplayName("filePathBlackList(value, basePath) 호출 시, basePath와 결합된 경로에서 상위 디렉토리 이동 패턴을 제거한다.")
    @Test
    void testFilePathBlackListWithBasePath() {
        assertEquals("/data/uploads//file.txt", EgovWebUtil.filePathBlackList("../file.txt", "/data/uploads/"));
    }

    @DisplayName("filePathReplaceAll 호출 시, 경로 구분자와 상위 디렉토리 이동 패턴 및 &을 제거한다.")
    @Test
    void testFilePathReplaceAll() {
        assertEquals("", EgovWebUtil.filePathReplaceAll(null));
        assertEquals("", EgovWebUtil.filePathReplaceAll(""));
        assertEquals("etcpasswd", EgovWebUtil.filePathReplaceAll("../etc/passwd&"));
    }

    @DisplayName("fileInjectPathReplaceAll 호출 시, 경로 구분자와 &을 제거한다.")
    @Test
    void testFileInjectPathReplaceAll() {
        assertEquals("", EgovWebUtil.fileInjectPathReplaceAll(null));
        assertEquals("", EgovWebUtil.fileInjectPathReplaceAll(""));
        assertEquals("etcpasswd", EgovWebUtil.fileInjectPathReplaceAll("etc/passwd&"));
    }

    @DisplayName("filePathWhiteList 호출 시, 입력값을 그대로 반환한다.")
    @Test
    void testFilePathWhiteList() {
        assertEquals("value", EgovWebUtil.filePathWhiteList("value"));
        assertNull(EgovWebUtil.filePathWhiteList(null));
    }

    @DisplayName("isIPAddress 호출 시, IPv4 형식 문자열만 true를 반환한다.")
    @Test
    void testIsIPAddress() {
        assertTrue(EgovWebUtil.isIPAddress("192.168.0.1"));
        assertFalse(EgovWebUtil.isIPAddress("not-an-ip"));
        assertFalse(EgovWebUtil.isIPAddress("localhost"));
    }

    @DisplayName("removeCRLF 호출 시, 캐리지리턴과 개행문자를 제거한다.")
    @Test
    void testRemoveCRLF() {
        assertEquals("abcdef", EgovWebUtil.removeCRLF("ab\rcd\nef"));
    }

    @DisplayName("removeSQLInjectionRisk 호출 시, SQL 인젝션 위험 문자를 제거한다.")
    @Test
    void testRemoveSQLInjectionRisk() {
        assertEquals("abcSELECTFROM", EgovWebUtil.removeSQLInjectionRisk("a b*c; SELECT % FROM- +,"));
    }

    @DisplayName("removeOSCmdRisk 호출 시, OS 명령어 인젝션 위험 문자를 제거한다.")
    @Test
    void testRemoveOSCmdRisk() {
        assertEquals("abcls", EgovWebUtil.removeOSCmdRisk("a b*c; ls |"));
    }
}
