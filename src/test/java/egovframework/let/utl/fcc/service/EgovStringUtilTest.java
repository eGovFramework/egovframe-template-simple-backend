package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EgovStringUtil 문자열 유틸리티 단위 테스트
 */
class EgovStringUtilTest {

    @Test
    void isEmpty_null이면true() {
        assertTrue(EgovStringUtil.isEmpty(null));
    }

    @Test
    void isEmpty_빈문자열이면true() {
        assertTrue(EgovStringUtil.isEmpty(""));
    }

    @Test
    void isEmpty_공백문자는false() {
        assertFalse(EgovStringUtil.isEmpty(" "));
    }

    @Test
    void isEmpty_일반문자열은false() {
        assertFalse(EgovStringUtil.isEmpty("hello"));
    }

    @Test
    void cutString_길이초과시suffix붙여서반환() {
        assertEquals("abcde...", EgovStringUtil.cutString("abcdefghij", "...", 5));
    }

    @Test
    void cutString_길이이하시원본반환() {
        assertEquals("abc", EgovStringUtil.cutString("abc", "...", 10));
    }

    @Test
    void cutString_null이면null반환() {
        assertNull(EgovStringUtil.cutString(null, "...", 5));
    }

    @Test
    void cutString_suffix없이자르기() {
        assertEquals("abc", EgovStringUtil.cutString("abcdef", 3));
    }

    @Test
    void removeCommaChar_콤마제거() {
        assertEquals("asdfgqweqe", EgovStringUtil.removeCommaChar("asdfg,qweqe"));
    }

    @Test
    void removeCommaChar_null이면null반환() {
        assertNull(EgovStringUtil.removeCommaChar(null));
    }

    @Test
    void removeMinusChar_하이픈제거() {
        assertEquals("asdfgqweqe", EgovStringUtil.removeMinusChar("a-sdfg-qweqe"));
    }

    @Test
    void replace_전체치환() {
        assertEquals("xxbxx", EgovStringUtil.replace("aabaa", "a", "x"));
    }

    @Test
    void replaceOnce_첫번째만치환() {
        assertEquals("xabaa", EgovStringUtil.replaceOnce("aabaa", "a", "x"));
    }

    @Test
    void indexOf_null입력이면마이너스1() {
        assertEquals(-1, EgovStringUtil.indexOf(null, "a"));
        assertEquals(-1, EgovStringUtil.indexOf("abc", null));
    }

    @Test
    void indexOf_존재하는위치반환() {
        assertEquals(2, EgovStringUtil.indexOf("aabaabaa", "b"));
    }

    @Test
    void decode_소스와비교가같으면returnStr() {
        assertEquals("foo", EgovStringUtil.decode("하이", "하이", "foo", "bar"));
    }

    @Test
    void decode_둘다null이면returnStr() {
        assertEquals("foo", EgovStringUtil.decode(null, null, "foo", "bar"));
    }

    @Test
    void decode_다르면defaultStr() {
        assertEquals("bar", EgovStringUtil.decode("하이", "바이", "foo", "bar"));
    }

    @Test
    void lowerCase_소문자변환() {
        assertEquals("abc", EgovStringUtil.lowerCase("aBc"));
    }

    @Test
    void lowerCase_null이면null() {
        assertNull(EgovStringUtil.lowerCase(null));
    }

    @Test
    void upperCase_대문자변환() {
        assertEquals("ABC", EgovStringUtil.upperCase("aBc"));
    }

    @Test
    void upperCase_null이면null() {
        assertNull(EgovStringUtil.upperCase(null));
    }

    @Test
    void removeWhitespace_모든공백제거() {
        assertEquals("abc", EgovStringUtil.removeWhitespace("   ab  c  "));
    }

    @Test
    void removeWhitespace_null이면null() {
        assertNull(EgovStringUtil.removeWhitespace(null));
    }

    @Test
    void stripStart_앞공백제거() {
        assertEquals("abc", EgovStringUtil.stripStart("  abc", null));
    }

    @Test
    void stripStart_지정문자앞에서제거() {
        assertEquals("abc  ", EgovStringUtil.stripStart("yxabc  ", "xyz"));
    }

    @Test
    void stripEnd_뒤공백제거() {
        assertEquals("abc", EgovStringUtil.stripEnd("abc  ", null));
    }

    @Test
    void strip_앞뒤공백제거() {
        assertEquals("abc", EgovStringUtil.strip(" abc ", null));
    }

    @Test
    void nullConvert_null이면빈문자열() {
        assertEquals("", EgovStringUtil.nullConvert((String) null));
    }

    @Test
    void nullConvert_null문자열이면빈문자열() {
        assertEquals("", EgovStringUtil.nullConvert("null"));
    }

    @Test
    void nullConvert_정상문자열trim결과반환() {
        assertEquals("hello", EgovStringUtil.nullConvert("  hello  "));
    }

    @Test
    void addMinusChar_8자리날짜에구분자추가() {
        assertEquals("2010-09-01", EgovStringUtil.addMinusChar("20100901"));
    }

    @Test
    void addMinusChar_8자리아니면빈문자열() {
        assertEquals("", EgovStringUtil.addMinusChar("2010090"));
    }

    @Test
    void getSpclStrCnvr_꺽쇠를엔티티로변환() {
        assertEquals("&lt;div&gt;", EgovStringUtil.getSpclStrCnvr("<div>"));
    }

    @Test
    void split_구분자로배열분리() {
        String[] result = EgovStringUtil.split("a,b,c", ",");
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("c", result[2]);
    }
}
