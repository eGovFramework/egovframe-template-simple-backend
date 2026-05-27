package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EgovStringUtil 단위 테스트
 */
class EgovStringUtilTest {

    @DisplayName("isEmpty: null 입력 시 true 반환")
    @Test
    void testIsEmpty_null() {
        assertTrue(EgovStringUtil.isEmpty(null));
    }

    @DisplayName("isEmpty: 빈 문자열 입력 시 true 반환")
    @Test
    void testIsEmpty_empty() {
        assertTrue(EgovStringUtil.isEmpty(""));
    }

    @DisplayName("isEmpty: 공백 문자열은 false 반환")
    @Test
    void testIsEmpty_whitespace() {
        assertFalse(EgovStringUtil.isEmpty(" "));
    }

    @DisplayName("isEmpty: 일반 문자열은 false 반환")
    @Test
    void testIsEmpty_normalString() {
        assertFalse(EgovStringUtil.isEmpty("abc"));
    }

    @DisplayName("cutString(source, output, length): 길이 초과 시 잘라서 출력 문자열 붙임")
    @Test
    void testCutString_withOutput_exceedsLength() {
        assertEquals("hello...", EgovStringUtil.cutString("hello world", "...", 5));
    }

    @DisplayName("cutString(source, output, length): 길이 이내이면 원본 반환")
    @Test
    void testCutString_withOutput_withinLength() {
        assertEquals("hi", EgovStringUtil.cutString("hi", "...", 10));
    }

    @DisplayName("cutString(source, output, length): null 입력 시 null 반환")
    @Test
    void testCutString_withOutput_null() {
        assertNull(EgovStringUtil.cutString(null, "...", 5));
    }

    @DisplayName("cutString(source, length): 길이 초과 시 잘라낸 문자열 반환")
    @Test
    void testCutString_exceedsLength() {
        assertEquals("hello", EgovStringUtil.cutString("hello world", 5));
    }

    @DisplayName("cutString(source, length): 길이 이내이면 원본 반환")
    @Test
    void testCutString_withinLength() {
        assertEquals("hi", EgovStringUtil.cutString("hi", 10));
    }

    @DisplayName("remove: 지정 문자 제거")
    @Test
    void testRemove_char() {
        assertEquals("qeed", EgovStringUtil.remove("queued", 'u'));
    }

    @DisplayName("remove: 지정 문자 없으면 원본 반환")
    @Test
    void testRemove_charNotFound() {
        assertEquals("queued", EgovStringUtil.remove("queued", 'z'));
    }

    @DisplayName("removeCommaChar: 콤마 제거")
    @Test
    void testRemoveCommaChar() {
        assertEquals("asdfgqweqe", EgovStringUtil.removeCommaChar("asdfg,qweqe"));
    }

    @DisplayName("removeMinusChar: 마이너스 제거")
    @Test
    void testRemoveMinusChar() {
        assertEquals("asdfgqweqe", EgovStringUtil.removeMinusChar("a-sdfg-qweqe"));
    }

    @DisplayName("replace: 모든 대상 문자열 치환")
    @Test
    void testReplace() {
        assertEquals("a-b-c", EgovStringUtil.replace("a.b.c", ".", "-"));
    }

    @DisplayName("replaceOnce: 첫 번째 대상 문자열만 치환")
    @Test
    void testReplaceOnce() {
        assertEquals("a-b.c", EgovStringUtil.replaceOnce("a.b.c", ".", "-"));
    }

    @DisplayName("replaceOnce: 대상 없으면 원본 반환")
    @Test
    void testReplaceOnce_notFound() {
        assertEquals("abc", EgovStringUtil.replaceOnce("abc", ".", "-"));
    }

    @DisplayName("lowerCase: 소문자 변환")
    @Test
    void testLowerCase() {
        assertEquals("abc", EgovStringUtil.lowerCase("aBc"));
    }

    @DisplayName("lowerCase: null 입력 시 null 반환")
    @Test
    void testLowerCase_null() {
        assertNull(EgovStringUtil.lowerCase(null));
    }

    @DisplayName("upperCase: 대문자 변환")
    @Test
    void testUpperCase() {
        assertEquals("ABC", EgovStringUtil.upperCase("aBc"));
    }

    @DisplayName("upperCase: null 입력 시 null 반환")
    @Test
    void testUpperCase_null() {
        assertNull(EgovStringUtil.upperCase(null));
    }

    @DisplayName("indexOf: 검색 문자열의 시작 인덱스 반환")
    @Test
    void testIndexOf_found() {
        assertEquals(2, EgovStringUtil.indexOf("aabaabaa", "b"));
    }

    @DisplayName("indexOf: null 입력 시 -1 반환")
    @Test
    void testIndexOf_null() {
        assertEquals(-1, EgovStringUtil.indexOf(null, "b"));
        assertEquals(-1, EgovStringUtil.indexOf("abc", null));
    }

    @DisplayName("decode(4인자): 두 문자열이 같으면 returnStr 반환")
    @Test
    void testDecode4_equal() {
        assertEquals("foo", EgovStringUtil.decode("하이", "하이", "foo", "bar"));
    }

    @DisplayName("decode(4인자): 두 문자열이 다르면 defaultStr 반환")
    @Test
    void testDecode4_notEqual() {
        assertEquals("bar", EgovStringUtil.decode("하이", "바이", "foo", "bar"));
    }

    @DisplayName("decode(4인자): 둘 다 null이면 returnStr 반환")
    @Test
    void testDecode4_bothNull() {
        assertEquals("foo", EgovStringUtil.decode(null, null, "foo", "bar"));
    }

    @DisplayName("decode(3인자): 같으면 returnStr 반환")
    @Test
    void testDecode3_equal() {
        assertEquals("foo", EgovStringUtil.decode("하이", "하이", "foo"));
    }

    @DisplayName("decode(3인자): 다르면 sourceStr 반환")
    @Test
    void testDecode3_notEqual() {
        assertEquals("하이", EgovStringUtil.decode("하이", "바이", "foo"));
    }

    @DisplayName("isNullToString: null 입력 시 빈 문자열 반환")
    @Test
    void testIsNullToString_null() {
        assertEquals("", EgovStringUtil.isNullToString(null));
    }

    @DisplayName("isNullToString: 일반 객체는 trim한 문자열 반환")
    @Test
    void testIsNullToString_normal() {
        assertEquals("abc", EgovStringUtil.isNullToString("  abc  "));
    }

    @DisplayName("nullConvert(Object): null 또는 'null' 문자열은 빈 문자열 반환")
    @Test
    void testNullConvert_objectNull() {
        assertEquals("", EgovStringUtil.nullConvert((Object) null));
    }

    @DisplayName("nullConvert(String): null 입력 시 빈 문자열 반환")
    @Test
    void testNullConvert_stringNull() {
        assertEquals("", EgovStringUtil.nullConvert((String) null));
    }

    @DisplayName("nullConvert(String): 빈 문자열 입력 시 빈 문자열 반환")
    @Test
    void testNullConvert_empty() {
        assertEquals("", EgovStringUtil.nullConvert(""));
    }

    @DisplayName("nullConvert(String): 일반 문자열은 trim하여 반환")
    @Test
    void testNullConvert_normal() {
        assertEquals("hello", EgovStringUtil.nullConvert("  hello  "));
    }

    @DisplayName("zeroConvert(Object): null 입력 시 0 반환")
    @Test
    void testZeroConvert_objectNull() {
        assertEquals(0, EgovStringUtil.zeroConvert((Object) null));
    }

    @DisplayName("zeroConvert(String): null 입력 시 0 반환")
    @Test
    void testZeroConvert_stringNull() {
        assertEquals(0, EgovStringUtil.zeroConvert((String) null));
    }

    @DisplayName("zeroConvert(String): 숫자 문자열은 정수로 변환")
    @Test
    void testZeroConvert_numeric() {
        assertEquals(42, EgovStringUtil.zeroConvert("42"));
    }

    @DisplayName("removeWhitespace: 공백 문자 모두 제거")
    @Test
    void testRemoveWhitespace() {
        assertEquals("abc", EgovStringUtil.removeWhitespace("   ab  c  "));
    }

    @DisplayName("removeWhitespace: null 입력 시 null 반환")
    @Test
    void testRemoveWhitespace_null() {
        assertNull(EgovStringUtil.removeWhitespace(null));
    }

    @DisplayName("stripStart: 앞쪽 지정 문자 제거")
    @Test
    void testStripStart() {
        assertEquals("abc  ", EgovStringUtil.stripStart("yxabc  ", "xyz"));
    }

    @DisplayName("stripStart: stripChars가 null이면 앞쪽 공백 제거")
    @Test
    void testStripStart_nullStripChars() {
        assertEquals("abc", EgovStringUtil.stripStart("  abc", null));
    }

    @DisplayName("stripEnd: 뒤쪽 지정 문자 제거")
    @Test
    void testStripEnd() {
        assertEquals("  abc", EgovStringUtil.stripEnd("  abcyx", "xyz"));
    }

    @DisplayName("stripEnd: stripChars가 null이면 뒤쪽 공백 제거")
    @Test
    void testStripEnd_nullStripChars() {
        assertEquals("abc", EgovStringUtil.stripEnd("abc  ", null));
    }

    @DisplayName("strip: 앞뒤 지정 문자 모두 제거")
    @Test
    void testStrip() {
        assertEquals("  abc", EgovStringUtil.strip("  abcyx", "xyz"));
    }

    @DisplayName("split(source, separator): 분리자로 문자열 배열 반환")
    @Test
    void testSplit_basic() {
        String[] result = EgovStringUtil.split("a,b,c", ",");
        assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    @DisplayName("split(source, separator, length): 지정 길이 배열로 반환")
    @Test
    void testSplit_withLength() {
        String[] result = EgovStringUtil.split("a,b,c", ",", 2);
        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertEquals("b,c", result[1]);
    }

    @DisplayName("getRandomStr: 범위 내 랜덤 문자 반환")
    @Test
    void testGetRandomStr_withinRange() {
        for (int i = 0; i < 20; i++) {
            String result = EgovStringUtil.getRandomStr('A', 'Z');
            assertTrue(result.charAt(0) >= 'A' && result.charAt(0) <= 'Z',
                    "범위를 벗어난 문자: " + result);
        }
    }

    @DisplayName("getRandomStr: 시작 문자가 종료 문자보다 크면 IllegalArgumentException 발생")
    @Test
    void testGetRandomStr_invalidRange() {
        assertThrows(IllegalArgumentException.class,
                () -> EgovStringUtil.getRandomStr('Z', 'A'));
    }

    @DisplayName("getEncdDcd: 동일 인코딩으로 변환 시 원본과 동일")
    @Test
    void testGetEncdDcd_sameCharset() {
        String result = EgovStringUtil.getEncdDcd("hello", "UTF-8", "UTF-8");
        assertEquals("hello", result);
    }

    @DisplayName("getEncdDcd: null 입력 시 null 반환")
    @Test
    void testGetEncdDcd_null() {
        assertNull(EgovStringUtil.getEncdDcd(null, "UTF-8", "UTF-8"));
    }

    @DisplayName("getSpclStrCnvr: 특수문자 HTML 엔티티 변환")
    @Test
    void testGetSpclStrCnvr() {
        assertEquals("&lt;div&gt;&amp;", EgovStringUtil.getSpclStrCnvr("<div>&"));
    }

    @DisplayName("getTimeStamp: 17자리 타임스탬프 반환")
    @Test
    void testGetTimeStamp() {
        String ts = EgovStringUtil.getTimeStamp();
        assertNotNull(ts);
        assertEquals(17, ts.length());
    }

    @DisplayName("getHtmlStrCnvr: HTML 엔티티를 원래 문자로 복원")
    @Test
    void testGetHtmlStrCnvr() {
        assertEquals("<div> & 'text'", EgovStringUtil.getHtmlStrCnvr("&lt;div&gt;&nbsp;&amp;&nbsp;&apos;text&apos;"));
    }

    @DisplayName("checkHtmlView: HTML 태그 특수문자를 엔티티로 변환")
    @Test
    void testCheckHtmlView() {
        String result = EgovStringUtil.checkHtmlView("<b>hi</b>");
        assertTrue(result.contains("&lt;"));
        assertTrue(result.contains("&gt;"));
    }

    @DisplayName("addMinusChar: 8자리 날짜 문자열에 하이픈 추가")
    @Test
    void testAddMinusChar_valid() {
        assertEquals("2010-09-01", EgovStringUtil.addMinusChar("20100901"));
    }

    @DisplayName("addMinusChar: 8자리 아닌 입력은 빈 문자열 반환")
    @Test
    void testAddMinusChar_invalid() {
        assertEquals("", EgovStringUtil.addMinusChar("2010090"));
    }
}
