package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EgovNumberUtil 숫자 유틸리티 단위 테스트
 */
class EgovNumberUtilTest {

    @Test
    void getNumSearchCheck_포함된숫자true() {
        assertTrue(EgovNumberUtil.getNumSearchCheck(12345678, 7));
    }

    @Test
    void getNumSearchCheck_포함안된숫자false() {
        assertFalse(EgovNumberUtil.getNumSearchCheck(12345678, 9));
    }

    @Test
    void getNumToStrCnvr_숫자를문자열로() {
        assertEquals("20081212", EgovNumberUtil.getNumToStrCnvr(20081212));
    }

    @Test
    void getNumToStrCnvr_0변환() {
        assertEquals("0", EgovNumberUtil.getNumToStrCnvr(0));
    }

    @Test
    void getNumToDateCnvr_8자리숫자날짜변환() {
        assertEquals("2008-12-12", EgovNumberUtil.getNumToDateCnvr(20081212));
    }

    @Test
    void getNumToDateCnvr_잘못된자릿수예외() {
        assertThrows(IllegalArgumentException.class, () -> EgovNumberUtil.getNumToDateCnvr(2008121));
    }

    @Test
    void getNumberValidCheck_숫자문자열true() {
        assertTrue(EgovNumberUtil.getNumberValidCheck("12345"));
    }

    @Test
    void getNumberValidCheck_문자포함false() {
        assertFalse(EgovNumberUtil.getNumberValidCheck("123a5"));
    }

    @Test
    void getNumberValidCheck_공백포함false() {
        assertFalse(EgovNumberUtil.getNumberValidCheck("123 5"));
    }

    @Test
    void getNumberCnvr_숫자치환() {
        assertEquals(99945678, EgovNumberUtil.getNumberCnvr(12345678, 123, 999));
    }

    @Test
    void checkRlnoInteger_음수이면마이너스1() {
        assertEquals(-1, EgovNumberUtil.checkRlnoInteger(-1.5));
    }

    @Test
    void checkRlnoInteger_double3_0은소수점있으므로1() {
        // String.valueOf(3.0) = "3.0" → indexOf('.') != -1 → 1(실수)
        assertEquals(1, EgovNumberUtil.checkRlnoInteger(3.0));
    }

    @Test
    void getRandomNum_범위내값반환() {
        int result = EgovNumberUtil.getRandomNum(1, 10);
        assertTrue(result >= 1 && result <= 10);
    }
}
