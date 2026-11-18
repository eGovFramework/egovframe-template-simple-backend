package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EgovNumberUtilTest {

    @DisplayName("getRandomNum 호출 시, 시작숫자와 종료숫자 사이의 값을 반환한다.")
    @Test
    void testGetRandomNum() {
        for (int i = 0; i < 100; i++) {
            int result = EgovNumberUtil.getRandomNum(10, 20);
            assertTrue(result >= 10 && result <= 20);
        }
    }

    @DisplayName("getNumSearchCheck 호출 시, 특정 숫자가 포함되어 있는지 확인한다.")
    @Test
    void testGetNumSearchCheck() {
        assertTrue(EgovNumberUtil.getNumSearchCheck(12345678, 567));
        assertFalse(EgovNumberUtil.getNumSearchCheck(12345678, 9));
    }

    @DisplayName("getNumToStrCnvr 호출 시, 숫자를 문자열로 변환한다.")
    @Test
    void testGetNumToStrCnvr() {
        assertEquals("20081212", EgovNumberUtil.getNumToStrCnvr(20081212));
    }

    @DisplayName("getNumToDateCnvr 호출 시, 8자리 숫자를 yyyy-MM-dd 형식으로 변환한다.")
    @Test
    void testGetNumToDateCnvrWithEightDigits() {
        assertEquals("2008-12-12", EgovNumberUtil.getNumToDateCnvr(20081212));
    }

    @DisplayName("getNumToDateCnvr 호출 시, 8자리 또는 14자리가 아니면 IllegalArgumentException이 발생한다.")
    @Test
    void testGetNumToDateCnvrWithInvalidLengthThrows() {
        assertThrows(IllegalArgumentException.class, () -> EgovNumberUtil.getNumToDateCnvr(2008));
    }

    @DisplayName("getNumberValidCheck 호출 시, 모든 문자가 숫자이면 true를 반환한다.")
    @Test
    void testGetNumberValidCheck() {
        assertTrue(EgovNumberUtil.getNumberValidCheck("1234567890"));
        assertFalse(EgovNumberUtil.getNumberValidCheck("123a567"));
    }

    @DisplayName("getNumberCnvr 호출 시, 특정 숫자를 다른 숫자로 치환한다.")
    @Test
    void testGetNumberCnvr() {
        assertEquals(99945678, EgovNumberUtil.getNumberCnvr(12345678, 123, 999));
    }

    @DisplayName("checkRlnoInteger 호출 시, 음수이면 -1을 반환하고 그 외에는 소수점 포함 여부에 따라 1을 반환한다.")
    @Test
    void testCheckRlnoInteger() {
        assertEquals(-1, EgovNumberUtil.checkRlnoInteger(-3.14));
        assertEquals(1, EgovNumberUtil.checkRlnoInteger(3.14));
        // double 표현은 항상 소수점을 포함하므로 정수 값이라도 실수(1)로 판정된다.
        assertEquals(1, EgovNumberUtil.checkRlnoInteger(5.0));
    }
}
