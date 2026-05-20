package egovframework.com.cmm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * EgovWebUtil 단위 테스트
 *
 * XSS 방어, 파일 경로 보안, 입력값 정제 메서드를 검증한다.
 */
class EgovWebUtilTest {

    // -----------------------------------------------------------------------
    // clearXSSMinimum
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("null 또는 공백 문자열을 입력하면 빈 문자열을 반환한다")
    void clearXSSMinimum_nullOrBlank_returnsEmpty() {
        assertThat(EgovWebUtil.clearXSSMinimum(null)).isEmpty();
        assertThat(EgovWebUtil.clearXSSMinimum("   ")).isEmpty();
    }

    @Test
    @DisplayName("XSS 위험 문자를 HTML 엔티티로 치환한다")
    void clearXSSMinimum_xssChars_areEscaped() {
        String input = "<script>alert('xss')</script>";
        String result = EgovWebUtil.clearXSSMinimum(input);

        assertThat(result).doesNotContain("<", ">", "'");
        assertThat(result).contains("&lt;", "&gt;", "&#39;");
    }

    @Test
    @DisplayName("& 문자를 &amp; 로 치환한다")
    void clearXSSMinimum_ampersand_isEscaped() {
        assertThat(EgovWebUtil.clearXSSMinimum("a&b")).isEqualTo("a&amp;b");
    }

    @Test
    @DisplayName("큰따옴표를 &#34; 로 치환한다")
    void clearXSSMinimum_doubleQuote_isEscaped() {
        assertThat(EgovWebUtil.clearXSSMinimum("\"value\"")).contains("&#34;");
    }

    // -----------------------------------------------------------------------
    // filePathBlackList
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("null 또는 공백 경로를 입력하면 빈 문자열을 반환한다")
    void filePathBlackList_nullOrBlank_returnsEmpty() {
        assertThat(EgovWebUtil.filePathBlackList(null)).isEmpty();
        assertThat(EgovWebUtil.filePathBlackList("   ")).isEmpty();
    }

    @Test
    @DisplayName("경로 순회 시도(..) 를 제거한다")
    void filePathBlackList_pathTraversal_isRemoved() {
        String result = EgovWebUtil.filePathBlackList("../../etc/passwd");
        assertThat(result).doesNotContain("..");
    }

    @Test
    @DisplayName("basePath 가 null 이면 SecurityException 을 던진다")
    void filePathBlackList_withNullBasePath_throwsSecurityException() {
        assertThatThrownBy(() -> EgovWebUtil.filePathBlackList("file.txt", null))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    @DisplayName("basePath 가 루트 경로(/) 이면 SecurityException 을 던진다")
    void filePathBlackList_withRootBasePath_throwsSecurityException() {
        assertThatThrownBy(() -> EgovWebUtil.filePathBlackList("file.txt", "/"))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    @DisplayName("정상 basePath 와 파일명이면 결합된 경로를 반환한다")
    void filePathBlackList_withValidBasePath_returnsCombinedPath() {
        String result = EgovWebUtil.filePathBlackList("report.pdf", "/upload/");
        assertThat(result).startsWith("/upload/");
    }

    // -----------------------------------------------------------------------
    // isIPAddress
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("유효한 IPv4 주소는 true 를 반환한다")
    void isIPAddress_validIPv4_returnsTrue() {
        assertThat(EgovWebUtil.isIPAddress("192.168.0.1")).isTrue();
        assertThat(EgovWebUtil.isIPAddress("10.0.0.255")).isTrue();
    }

    @Test
    @DisplayName("IP 형식이 아닌 문자열은 false 를 반환한다")
    void isIPAddress_invalidInput_returnsFalse() {
        assertThat(EgovWebUtil.isIPAddress("localhost")).isFalse();
        assertThat(EgovWebUtil.isIPAddress("abc.def.ghi.jkl")).isFalse();
    }

    // -----------------------------------------------------------------------
    // removeCRLF
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("CRLF 문자를 제거한다")
    void removeCRLF_crlfChars_areRemoved() {
        String result = EgovWebUtil.removeCRLF("line1\r\nline2\nline3\r");
        assertThat(result).isEqualTo("line1line2line3");
    }

    // -----------------------------------------------------------------------
    // removeSQLInjectionRisk
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("SQL 인젝션 위험 문자를 제거한다")
    void removeSQLInjectionRisk_dangerousChars_areRemoved() {
        String result = EgovWebUtil.removeSQLInjectionRisk("a; DROP TABLE users--");
        assertThat(result).doesNotContain(";", "-", " ");
    }

    // -----------------------------------------------------------------------
    // removeOSCmdRisk
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("OS 명령어 인젝션 위험 문자를 제거한다")
    void removeOSCmdRisk_dangerousChars_areRemoved() {
        String result = EgovWebUtil.removeOSCmdRisk("ls;|rm *");
        assertThat(result).doesNotContain(";", "|", " ");
    }

    // -----------------------------------------------------------------------
    // handleAuthError
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("handleAuthError 는 전달받은 코드와 메시지를 담은 ResultVO 를 반환한다")
    void handleAuthError_returnsResultVOWithGivenCodeAndMessage() {
        var resultVO = EgovWebUtil.handleAuthError(403, "접근 거부");

        assertThat(resultVO.getResultCode()).isEqualTo(403);
        assertThat(resultVO.getResultMessage()).isEqualTo("접근 거부");
    }
}
