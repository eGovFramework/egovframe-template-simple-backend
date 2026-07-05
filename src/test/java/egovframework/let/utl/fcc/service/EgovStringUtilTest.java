package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EgovStringUtilTest {

    @DisplayName("cutString(source, output, length) 호출 시, 길이를 초과하면 자르고 접미어를 붙인다.")
    @Test
    void testCutStringWithSuffix() {
        assertEquals("hel...", EgovStringUtil.cutString("hello world", "...", 3));
        assertEquals("hi", EgovStringUtil.cutString("hi", "...", 5));
        assertNull(EgovStringUtil.cutString(null, "...", 5));
    }

    @DisplayName("cutString(source, length) 호출 시, 길이를 초과하면 자른다.")
    @Test
    void testCutStringWithoutSuffix() {
        assertEquals("hel", EgovStringUtil.cutString("hello world", 3));
        assertEquals("hi", EgovStringUtil.cutString("hi", 5));
        assertNull(EgovStringUtil.cutString(null, 5));
    }

    @DisplayName("isEmpty 호출 시, null 또는 빈 문자열이면 true를 반환한다.")
    @Test
    void testIsEmpty() {
        assertTrue(EgovStringUtil.isEmpty(null));
        assertTrue(EgovStringUtil.isEmpty(""));
        assertFalse(EgovStringUtil.isEmpty(" "));
        assertFalse(EgovStringUtil.isEmpty("bob"));
    }

    @DisplayName("remove 호출 시, 지정한 문자를 모두 제거한다.")
    @Test
    void testRemove() {
        assertNull(EgovStringUtil.remove(null, 'u'));
        assertEquals("", EgovStringUtil.remove("", 'u'));
        assertEquals("qeed", EgovStringUtil.remove("queued", 'u'));
        assertEquals("queued", EgovStringUtil.remove("queued", 'z'));
    }

    @DisplayName("removeCommaChar 호출 시, 콤마를 모두 제거한다.")
    @Test
    void testRemoveCommaChar() {
        assertNull(EgovStringUtil.removeCommaChar(null));
        assertEquals("", EgovStringUtil.removeCommaChar(""));
        assertEquals("asdfgqweqe", EgovStringUtil.removeCommaChar("asdfg,qweqe"));
    }

    @DisplayName("removeMinusChar 호출 시, 하이픈을 모두 제거한다.")
    @Test
    void testRemoveMinusChar() {
        assertNull(EgovStringUtil.removeMinusChar(null));
        assertEquals("", EgovStringUtil.removeMinusChar(""));
        assertEquals("asdfgqweqe", EgovStringUtil.removeMinusChar("a-sdfg-qweqe"));
    }

    @DisplayName("replace 호출 시, 대상 문자열을 모두 치환한다.")
    @Test
    void testReplace() {
        assertEquals("hellx wxrld", EgovStringUtil.replace("hello world", "o", "x"));
        assertEquals("hello", EgovStringUtil.replace("hello", "z", "x"));
    }

    @DisplayName("replaceOnce 호출 시, 대상 문자열을 첫 번째 것만 치환한다.")
    @Test
    void testReplaceOnce() {
        assertEquals("hxllo world", EgovStringUtil.replaceOnce("hello world", "e", "x"));
        assertEquals("hello", EgovStringUtil.replaceOnce("hello", "z", "x"));
    }

    @DisplayName("replaceChar 호출 시, subject에 포함된 문자를 object로 치환한다.")
    @Test
    void testReplaceChar() {
        assertEquals("hellx world", EgovStringUtil.replaceChar("hello world", "o", "x"));
    }

    @DisplayName("indexOf 호출 시, 검색 문자열의 시작 위치를 반환하며 null이면 -1을 반환한다.")
    @Test
    void testIndexOf() {
        assertEquals(-1, EgovStringUtil.indexOf(null, "a"));
        assertEquals(-1, EgovStringUtil.indexOf("a", null));
        assertEquals(0, EgovStringUtil.indexOf("", ""));
        assertEquals(0, EgovStringUtil.indexOf("aabaabaa", "a"));
        assertEquals(2, EgovStringUtil.indexOf("aabaabaa", "b"));
        assertEquals(1, EgovStringUtil.indexOf("aabaabaa", "ab"));
    }

    @DisplayName("decode(4-arg) 호출 시, 소스와 비교값이 같으면 returnStr을, 다르면 defaultStr을 반환한다.")
    @Test
    void testDecodeFourArgs() {
        assertEquals("foo", EgovStringUtil.decode(null, null, "foo", "bar"));
        assertEquals("bar", EgovStringUtil.decode("", null, "foo", "bar"));
        assertEquals("bar", EgovStringUtil.decode(null, "", "foo", "bar"));
        assertEquals("foo", EgovStringUtil.decode("하이", "하이", "foo", "bar"));
        assertEquals("bar", EgovStringUtil.decode("하이", "하이  ", "foo", "bar"));
    }

    @DisplayName("decode(3-arg) 호출 시, 소스와 비교값이 같으면 returnStr을, 다르면 sourceStr을 반환한다.")
    @Test
    void testDecodeThreeArgs() {
        assertEquals("foo", EgovStringUtil.decode(null, null, "foo"));
        assertEquals("", EgovStringUtil.decode("", null, "foo"));
        assertNull(EgovStringUtil.decode(null, "", "foo"));
        assertEquals("foo", EgovStringUtil.decode("하이", "하이", "foo"));
        assertEquals("하이", EgovStringUtil.decode("하이", "하이 ", "foo"));
        assertEquals("하이", EgovStringUtil.decode("하이", "바이", "foo"));
    }

    @DisplayName("isNullToString 호출 시, null이면 빈 문자열을, 아니면 trim된 문자열을 반환한다.")
    @Test
    void testIsNullToString() {
        assertEquals("", EgovStringUtil.isNullToString(null));
        assertEquals("abc", EgovStringUtil.isNullToString("  abc  "));
        assertEquals("123", EgovStringUtil.isNullToString(123));
    }

    @DisplayName("nullConvert(Object) 호출 시, BigDecimal은 문자열로, null 또는 \"null\"은 빈 문자열로 변환한다.")
    @Test
    void testNullConvertObject() {
        assertEquals("", EgovStringUtil.nullConvert((Object) null));
        assertEquals("", EgovStringUtil.nullConvert((Object) "null"));
        assertEquals("abc", EgovStringUtil.nullConvert((Object) "  abc  "));
        assertEquals("3.14", EgovStringUtil.nullConvert((Object) new java.math.BigDecimal("3.14")));
    }

    @DisplayName("nullConvert(String) 호출 시, null/\"null\"/빈값/공백이면 빈 문자열을 반환한다.")
    @Test
    void testNullConvertString() {
        assertEquals("", EgovStringUtil.nullConvert((String) null));
        assertEquals("", EgovStringUtil.nullConvert("null"));
        assertEquals("", EgovStringUtil.nullConvert(""));
        assertEquals("", EgovStringUtil.nullConvert(" "));
        assertEquals("abc", EgovStringUtil.nullConvert("  abc  "));
    }

    @DisplayName("zeroConvert(Object) 호출 시, null 또는 \"null\"이면 0을, 아니면 정수로 변환한다.")
    @Test
    void testZeroConvertObject() {
        assertEquals(0, EgovStringUtil.zeroConvert((Object) null));
        assertEquals(0, EgovStringUtil.zeroConvert((Object) "null"));
        assertEquals(42, EgovStringUtil.zeroConvert((Object) " 42 "));
    }

    @DisplayName("zeroConvert(String) 호출 시, null/\"null\"/빈값/공백이면 0을, 아니면 정수로 변환한다.")
    @Test
    void testZeroConvertString() {
        assertEquals(0, EgovStringUtil.zeroConvert((String) null));
        assertEquals(0, EgovStringUtil.zeroConvert("null"));
        assertEquals(0, EgovStringUtil.zeroConvert(""));
        assertEquals(0, EgovStringUtil.zeroConvert(" "));
        assertEquals(42, EgovStringUtil.zeroConvert(" 42 "));
    }

    @DisplayName("removeWhitespace 호출 시, 모든 공백문자를 제거한다.")
    @Test
    void testRemoveWhitespace() {
        assertNull(EgovStringUtil.removeWhitespace(null));
        assertEquals("", EgovStringUtil.removeWhitespace(""));
        assertEquals("abc", EgovStringUtil.removeWhitespace("abc"));
        assertEquals("abc", EgovStringUtil.removeWhitespace("   ab  c  "));
    }

    @DisplayName("checkHtmlView 호출 시, HTML 특수문자를 엔티티로 치환한다.")
    @Test
    void testCheckHtmlView() {
        assertEquals("&lt;b&gt;&quot;hi&quot;&nbsp;there&lt;/b&gt;", EgovStringUtil.checkHtmlView("<b>\"hi\" there</b>"));
    }

    @DisplayName("split(source, separator) 호출 시, 구분자로 분리된 배열을 반환한다.")
    @Test
    void testSplit() {
        assertArrayEquals(new String[] {"a", "b", "c"}, EgovStringUtil.split("a,b,c", ","));
        assertArrayEquals(new String[] {"a"}, EgovStringUtil.split("a", ","));
    }

    @DisplayName("lowerCase / upperCase 호출 시, 대소문자를 변환하며 null은 그대로 반환한다.")
    @Test
    void testLowerUpperCase() {
        assertNull(EgovStringUtil.lowerCase(null));
        assertEquals("", EgovStringUtil.lowerCase(""));
        assertEquals("abc", EgovStringUtil.lowerCase("aBc"));

        assertNull(EgovStringUtil.upperCase(null));
        assertEquals("", EgovStringUtil.upperCase(""));
        assertEquals("ABC", EgovStringUtil.upperCase("aBc"));
    }

    @DisplayName("stripStart 호출 시, 앞쪽의 지정 문자를 제거한다.")
    @Test
    void testStripStart() {
        assertNull(EgovStringUtil.stripStart(null, "*"));
        assertEquals("", EgovStringUtil.stripStart("", "*"));
        assertEquals("abc", EgovStringUtil.stripStart("abc", ""));
        assertEquals("abc", EgovStringUtil.stripStart("abc", null));
        assertEquals("abc", EgovStringUtil.stripStart("  abc", null));
        assertEquals("abc  ", EgovStringUtil.stripStart("abc  ", null));
        assertEquals("abc ", EgovStringUtil.stripStart(" abc ", null));
        assertEquals("abc  ", EgovStringUtil.stripStart("yxabc  ", "xyz"));
    }

    @DisplayName("stripEnd 호출 시, 뒤쪽의 지정 문자를 제거한다.")
    @Test
    void testStripEnd() {
        assertNull(EgovStringUtil.stripEnd(null, "*"));
        assertEquals("", EgovStringUtil.stripEnd("", "*"));
        assertEquals("abc", EgovStringUtil.stripEnd("abc", ""));
        assertEquals("abc", EgovStringUtil.stripEnd("abc", null));
        assertEquals("  abc", EgovStringUtil.stripEnd("  abc", null));
        assertEquals("abc", EgovStringUtil.stripEnd("abc  ", null));
        assertEquals(" abc", EgovStringUtil.stripEnd(" abc ", null));
        assertEquals("  abc", EgovStringUtil.stripEnd("  abcyx", "xyz"));
    }

    @DisplayName("strip 호출 시, 앞뒤의 지정 문자를 모두 제거한다.")
    @Test
    void testStrip() {
        assertNull(EgovStringUtil.strip(null, "*"));
        assertEquals("", EgovStringUtil.strip("", "*"));
        assertEquals("abc", EgovStringUtil.strip("abc", null));
        assertEquals("abc", EgovStringUtil.strip("  abc", null));
        assertEquals("abc", EgovStringUtil.strip("abc  ", null));
        assertEquals("abc", EgovStringUtil.strip(" abc ", null));
        assertEquals("  abc", EgovStringUtil.strip("  abcyx", "xyz"));
    }

    @DisplayName("split(source, separator, length) 호출 시, 지정된 길이의 배열로 분리하고 남는 칸은 빈 문자열로 채운다.")
    @Test
    void testSplitWithArrayLength() {
        assertArrayEquals(new String[] {"a", "b,c"}, EgovStringUtil.split("a,b,c", ",", 2));
        assertArrayEquals(new String[] {"a", "b", ""}, EgovStringUtil.split("a,b", ",", 3));
    }

    @DisplayName("getRandomStr 호출 시, 시작문자와 종료문자 사이의 문자를 반환하며, 시작문자가 종료문자보다 크면 예외가 발생한다.")
    @Test
    void testGetRandomStr() {
        for (int i = 0; i < 50; i++) {
            String result = EgovStringUtil.getRandomStr('A', 'Z');
            assertEquals(1, result.length());
            assertTrue(result.charAt(0) >= 'A' && result.charAt(0) <= 'Z');
        }
        assertThrows(IllegalArgumentException.class, () -> EgovStringUtil.getRandomStr('Z', 'A'));
    }

    @DisplayName("getEncdDcd 호출 시, 지정한 캐릭터셋으로 인/디코딩하며 null 입력 시 null을 반환한다.")
    @Test
    void testGetEncdDcd() throws Exception {
        assertNull(EgovStringUtil.getEncdDcd(null, "UTF-8", "UTF-8"));
        assertEquals("hello", EgovStringUtil.getEncdDcd("hello", "UTF-8", "UTF-8"));
        assertNull(EgovStringUtil.getEncdDcd("hello", "INVALID-CHARSET", "UTF-8"));
    }

    @DisplayName("getSpclStrCnvr 호출 시, <, >, &을 HTML 엔티티로 치환한다.")
    @Test
    void testGetSpclStrCnvr() {
        assertEquals("&lt;a&gt;&amp;&lt;/a&gt;", EgovStringUtil.getSpclStrCnvr("<a>&</a>"));
    }

    @DisplayName("getTimeStamp 호출 시, 17자리 숫자 타임스탬프 문자열을 반환한다.")
    @Test
    void testGetTimeStamp() {
        String timeStamp = EgovStringUtil.getTimeStamp();
        assertEquals(17, timeStamp.length());
        assertTrue(timeStamp.matches("\\d{17}"));
    }

    @DisplayName("getHtmlStrCnvr 호출 시, HTML 엔티티를 원래 문자로 복원한다.")
    @Test
    void testGetHtmlStrCnvr() {
        assertEquals("<a>&\"'</a> ", EgovStringUtil.getHtmlStrCnvr("&lt;a&gt;&amp;&quot;&apos;&lt;/a&gt;&nbsp;"));
    }

    @DisplayName("addMinusChar 호출 시, 8자리 날짜 문자열에 하이픈을 추가하며 그 외 길이는 빈 문자열을 반환한다.")
    @Test
    void testAddMinusChar() {
        assertEquals("2010-09-01", EgovStringUtil.addMinusChar("20100901"));
        assertEquals("", EgovStringUtil.addMinusChar("2010"));
    }
}
