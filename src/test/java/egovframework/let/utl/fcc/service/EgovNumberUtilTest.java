package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EgovNumberUtil 단위 테스트
 */
class EgovNumberUtilTest {

    @DisplayName("getRandomNum: 시작~종료 범위 내의 랜덤 숫자를 반환한다")
    @Test
    void testGetRandomNum_withinRange() {
        for (int i = 0; i < 20; i++) {
            int result = EgovNumberUtil.getRandomNum(1, 9);
            assertTrue(result >= 1 && result <= 9,
                    "결과값이 범위를 벗어났습니다: " + result);
        }
    }

    @DisplayName("getNumSearchCheck: 숫자집합에 검색숫자가 포함되면 true를 반환한다")
    @Test
    void testGetNumSearchCheck_found() {
        assertTrue(EgovNumberUtil.getNumSearchCheck(12345678, 7));
    }

    @DisplayName("getNumSearchCheck: 숫자집합에 검색숫자가 없으면 false를 반환한다")
    @Test
    void testGetNumSearchCheck_notFound() {
        assertFalse(EgovNumberUtil.getNumSearchCheck(12345678, 9));
    }

    @DisplayName("getNumToStrCnvr: 숫자를 문자열로 변환한다")
    @Test
    void testGetNumToStrCnvr() {
        assertEquals("20081212", EgovNumberUtil.getNumToStrCnvr(20081212));
    }

    @DisplayName("getNumToDateCnvr: 8자리 숫자를 날짜 형식으로 변환한다")
    @Test
    void testGetNumToDateCnvr_8digits() {
        assertEquals("2008-12-12", EgovNumberUtil.getNumToDateCnvr(20081212));
    }

    @DisplayName("getNumToDateCnvr: 8자리/14자리 이외 숫자 입력 시 예외가 발생한다")
    @Test
    void testGetNumToDateCnvr_invalidLength() {
        assertThrows(IllegalArgumentException.class,
                () -> EgovNumberUtil.getNumToDateCnvr(2008));
    }

    @DisplayName("getNumberValidCheck: 숫자 문자열이면 true를 반환한다")
    @Test
    void testGetNumberValidCheck_numeric() {
        assertTrue(EgovNumberUtil.getNumberValidCheck("12345"));
    }

    @DisplayName("getNumberValidCheck: 숫자가 아닌 문자가 포함되면 false를 반환한다")
    @Test
    void testGetNumberValidCheck_nonNumeric() {
        assertFalse(EgovNumberUtil.getNumberValidCheck("123a5"));
    }

    @DisplayName("getNumberCnvr: 숫자집합에서 원래숫자를 치환숫자로 교체한다")
    @Test
    void testGetNumberCnvr() {
        assertEquals(99945678, EgovNumberUtil.getNumberCnvr(12345678, 123, 999));
    }

    @DisplayName("checkRlnoInteger: 음수이면 -1을 반환한다")
    @Test
    void testCheckRlnoInteger_negative() {
        assertEquals(-1, EgovNumberUtil.checkRlnoInteger(-1.5));
    }

    @DisplayName("checkRlnoInteger: double 타입의 양수는 소수점이 표현되므로 1(실수)을 반환한다")
    @Test
    void testCheckRlnoInteger_positiveDouble() {
        // double 5.0 -> String.valueOf -> "5.0" (소수점 포함) -> 실수(1)
        assertEquals(1, EgovNumberUtil.checkRlnoInteger(5.0));
    }

    @DisplayName("checkRlnoInteger: 실수이면 1을 반환한다")
    @Test
    void testCheckRlnoInteger_real() {
        assertEquals(1, EgovNumberUtil.checkRlnoInteger(3.14));
    }
}
