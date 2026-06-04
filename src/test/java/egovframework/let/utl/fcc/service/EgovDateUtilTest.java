package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EgovDateUtil 날짜 유틸리티 단위 테스트
 */
class EgovDateUtilTest {

    @Test
    void checkDate_유효한날짜true() {
        assertTrue(EgovDateUtil.checkDate("20060228"));
    }

    @Test
    void checkDate_구분자형식도true() {
        assertTrue(EgovDateUtil.checkDate("2006-02-28"));
    }

    @Test
    void checkDate_존재하지않는날짜false() {
        assertFalse(EgovDateUtil.checkDate("20061131"));
    }

    @Test
    void checkDate_잘못된월false() {
        assertFalse(EgovDateUtil.checkDate("20061331"));
    }

    @Test
    void addDay_일수더하기() {
        assertEquals("20050903", EgovDateUtil.addDay("20050831", 3));
    }

    @Test
    void addDay_일수빼기() {
        assertEquals("19991201", EgovDateUtil.addDay("20000201", -62));
    }

    @Test
    void addMonth_월더하기() {
        assertEquals("20020201", EgovDateUtil.addMonth("20010201", 12));
    }

    @Test
    void addMonth_월빼기() {
        assertEquals("20060128", EgovDateUtil.addMonth("20060228", -1));
    }

    @Test
    void addYear_년더하기() {
        assertEquals("20620201", EgovDateUtil.addYear("20000201", 62));
    }

    @Test
    void addYear_년빼기() {
        assertEquals("20000201", EgovDateUtil.addYear("20620201", -62));
    }

    @Test
    void getDaysDiff_날짜차이양수() {
        assertEquals(10, EgovDateUtil.getDaysDiff("20060228", "20060310"));
    }

    @Test
    void getDaysDiff_동일날짜0() {
        assertEquals(0, EgovDateUtil.getDaysDiff("20060801", "20060801"));
    }

    @Test
    void getDaysDiff_날짜차이음수() {
        assertEquals(-28, EgovDateUtil.getDaysDiff("19990228", "19990131"));
    }

    @Test
    void formatDate_8자리점구분자() {
        assertEquals("2003.04.05", EgovDateUtil.formatDate("20030405", "."));
    }

    @Test
    void formatDate_8자리슬래시구분자() {
        assertEquals("2004/01/01", EgovDateUtil.formatDate("20040101", "/"));
    }

    @Test
    void formatDate_6자리는validChkDate불통과로예외() {
        // validChkDate는 8자리 또는 10자리만 허용
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.formatDate("200304", "."));
    }

    @Test
    void formatDate_4자리는validChkDate불통과로예외() {
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.formatDate("2003", "."));
    }

    @Test
    void validChkTime_콜론제거후4자리반환() {
        // validChkTime: HH:mm(5자리) 입력 시 콜론 제거 후 4자리 반환
        assertEquals("1512", EgovDateUtil.validChkTime("15:12"));
    }

    @Test
    void validChkTime_4자리그대로반환() {
        assertEquals("1512", EgovDateUtil.validChkTime("1512"));
    }

    @Test
    void validChkDate_null이면예외() {
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.validChkDate(null));
    }

    @Test
    void validChkDate_잘못된길이예외() {
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.validChkDate("200601"));
    }

    @Test
    void validChkDate_구분자제거후8자리반환() {
        assertEquals("20060228", EgovDateUtil.validChkDate("2006-02-28"));
    }

    @Test
    void isLeapYear_윤년이면false() {
        // 메서드 반환값이 반전되어 있음: 윤년(4의배수)이면 false 반환
        assertFalse(EgovDateUtil.isLeapYear(2004));
    }

    @Test
    void isLeapYear_평년이면true() {
        assertTrue(EgovDateUtil.isLeapYear(2005));
    }

    @Test
    void convertWeek_영문요일을한글로변환() {
        assertEquals("월요일", EgovDateUtil.convertWeek("MON"));
        assertEquals("일요일", EgovDateUtil.convertWeek("SUN"));
        assertEquals("금요일", EgovDateUtil.convertWeek("FRI"));
    }

    @Test
    void addYearMonthDay_복합가감() {
        assertEquals("19810916", EgovDateUtil.addYearMonthDay("19810828", 0, 0, 19));
    }

    @Test
    void convertDate_날짜포맷변환() {
        assertEquals("2006-02-28", EgovDateUtil.convertDate("20060228", "yyyyMMdd", "yyyy-MM-dd", ""));
    }
}
