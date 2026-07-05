package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EgovDateUtilTest {

    @DisplayName("addYearMonthDay 호출 시, 년/월/일을 가감한 날짜를 반환한다.")
    @Test
    void testAddYearMonthDay() {
        assertEquals("19810916", EgovDateUtil.addYearMonthDay("19810828", 0, 0, 19));
        assertEquals("20060218", EgovDateUtil.addYearMonthDay("20060228", 0, 0, -10));
        assertEquals("20060310", EgovDateUtil.addYearMonthDay("20060228", 0, 0, 10));
        assertEquals("20060401", EgovDateUtil.addYearMonthDay("20060228", 0, 0, 32));
        assertEquals("20050228", EgovDateUtil.addYearMonthDay("20050331", 0, -1, 0));
        assertEquals("20050531", EgovDateUtil.addYearMonthDay("20050301", 0, 2, 30));
        assertEquals("20060531", EgovDateUtil.addYearMonthDay("20050301", 1, 2, 30));
    }

    @DisplayName("addYearMonthDay 호출 시, 잘못된 날짜 포맷이면 IllegalArgumentException이 발생한다.")
    @Test
    void testAddYearMonthDayWithInvalidFormatThrows() {
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.addYearMonthDay("2006", 0, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.addYearMonthDay(null, 0, 0, 1));
    }

    @DisplayName("addYear 호출 시, 년을 가감한 날짜를 반환한다.")
    @Test
    void testAddYear() {
        assertEquals("20620201", EgovDateUtil.addYear("20000201", 62));
        assertEquals("20000201", EgovDateUtil.addYear("20620201", -62));
        assertEquals("20060228", EgovDateUtil.addYear("20040229", 2));
    }

    @DisplayName("addMonth 호출 시, 월을 가감한 날짜를 반환한다.")
    @Test
    void testAddMonth() {
        assertEquals("20020201", EgovDateUtil.addMonth("20010201", 12));
        assertEquals("19810228", EgovDateUtil.addMonth("19800229", 12));
        assertEquals("20060228", EgovDateUtil.addMonth("20060131", 1));
    }

    @DisplayName("addDay 호출 시, 일을 가감한 날짜를 반환한다.")
    @Test
    void testAddDay() {
        assertEquals("20000201", EgovDateUtil.addDay("19991201", 62));
        assertEquals("19991201", EgovDateUtil.addDay("20000201", -62));
        assertEquals("20050903", EgovDateUtil.addDay("20050831", 3));
    }

    @DisplayName("getDaysDiff 호출 시, 두 날짜 사이의 일수 차이를 반환한다.")
    @Test
    void testGetDaysDiff() {
        assertEquals(10, EgovDateUtil.getDaysDiff("20060228", "20060310"));
        assertEquals(365, EgovDateUtil.getDaysDiff("20060101", "20070101"));
        assertEquals(-28, EgovDateUtil.getDaysDiff("19990228", "19990131"));
        assertEquals(0, EgovDateUtil.getDaysDiff("20060801", "20060801"));
    }

    @DisplayName("checkDate(String) 호출 시, 유효한 8자리 날짜 문자열이면 true를 반환한다.")
    @Test
    void testCheckDateString() {
        assertTrue(EgovDateUtil.checkDate("20060228"));
        assertFalse(EgovDateUtil.checkDate("20061131"));
    }

    @DisplayName("checkDate(year, month, day) 호출 시, 유효한 날짜인지 확인한다.")
    @Test
    void testCheckDateYearMonthDay() {
        assertTrue(EgovDateUtil.checkDate("2006", "02", "28"));
        assertFalse(EgovDateUtil.checkDate("2006", "13", "31"));
        assertFalse(EgovDateUtil.checkDate("2006", "11", "31"));
    }

    @DisplayName("convertDate(source, from, to, tz) 호출 시, 날짜 포맷을 변환한다.")
    @Test
    void testConvertDateFormat() {
        assertEquals("2021-01-02 03:04:05", EgovDateUtil.convertDate("20210102030405", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", ""));
        assertEquals("", EgovDateUtil.convertDate("", "yyyyMMdd", "yyyy-MM-dd", ""));
    }

    @DisplayName("formatDate 호출 시, 지정한 구분자로 날짜 문자열을 나눈다.")
    @Test
    void testFormatDate() {
        assertEquals("2003.04.05", EgovDateUtil.formatDate("20030405", "."));
        assertEquals("2004/01/01", EgovDateUtil.formatDate("20040101", "/"));
        assertEquals("", EgovDateUtil.formatDate("00000101", "/"));
    }

    @DisplayName("formatDate 호출 시, 8자리 또는 10자리가 아닌 날짜 문자열이면 IllegalArgumentException이 발생한다.")
    @Test
    void testFormatDateWithInvalidLengthThrows() {
        // validChkDate가 선행 검증하므로 6자리 등 그 외 길이는 내부 분기와 무관하게 예외가 발생한다.
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.formatDate("200304", "."));
    }

    @DisplayName("formatTime 호출 시, validChkTime을 통과하지 못하는 길이의 입력은 예외가 발생한다.")
    @Test
    void testFormatTimeWithInvalidLengthThrows() {
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.formatTime("151241", "/"));
    }

    @DisplayName("isLeapYear 호출 시, 윤년이면 false, 평년이면 true를 반환한다(구현 그대로 검증).")
    @Test
    void testIsLeapYear() {
        // 구현상 이름과 반대로 윤년일 때 false, 평년일 때 true를 반환한다.
        assertFalse(EgovDateUtil.isLeapYear(2004));
        assertFalse(EgovDateUtil.isLeapYear(2000));
        assertTrue(EgovDateUtil.isLeapYear(2005));
        assertTrue(EgovDateUtil.isLeapYear(2006));
        assertTrue(EgovDateUtil.isLeapYear(1900));
    }

    @DisplayName("getToday / getCurrentDate(\"\") 호출 시, 8자리 숫자 날짜 문자열을 반환한다.")
    @Test
    void testGetTodayAndCurrentDate() {
        String today = EgovDateUtil.getToday();
        assertEquals(8, today.length());
        assertTrue(today.matches("\\d{8}"));

        String currentDate = EgovDateUtil.getCurrentDate("");
        assertEquals(today.length(), currentDate.length());
        assertTrue(currentDate.matches("\\d{8}"));
    }

    @DisplayName("getCurrentDate(dateType) 호출 시, dateType이 비어있지 않으면 내부 convertDate 오버로드 혼선으로 예외가 발생한다.")
    @Test
    void testGetCurrentDateWithFormatThrows() {
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.getCurrentDate("yyyy-MM-dd"));
    }

    @DisplayName("convertDate(date, time, formatStr) 호출 시, 지정한 포맷으로 조합된 날짜/시간 문자열을 반환한다.")
    @Test
    void testConvertDateTimeFormat() {
        assertEquals("2021-01-02 03:04", EgovDateUtil.convertDate("20210102", "0304", "yyyy-MM-dd HH:mm"));
    }

    @DisplayName("getRandomDate 호출 시, 지정한 두 날짜 사이의 임의 날짜를 반환한다.")
    @Test
    void testGetRandomDate() {
        for (int i = 0; i < 20; i++) {
            String random = EgovDateUtil.getRandomDate("20200101", "20200110");
            assertEquals(8, random.length());
            assertTrue(random.compareTo("20200101") >= 0 && random.compareTo("20200110") <= 0);
        }
    }

    @DisplayName("getRandomDate 호출 시, 종료일이 시작일보다 과거이면 IllegalArgumentException이 발생한다.")
    @Test
    void testGetRandomDateWithInvalidRangeThrows() {
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.getRandomDate("20200110", "20200101"));
    }

    @DisplayName("toLunar 호출 시, 8자리 음력 날짜와 윤달 여부를 반환한다.")
    @Test
    void testToLunar() {
        Map<String, String> result = EgovDateUtil.toLunar("20230101");
        assertEquals(8, result.get("day").length());
        assertTrue(result.get("leap").equals("0") || result.get("leap").equals("1"));
    }

    @DisplayName("toLunar 호출 시, 8자리가 아닌 날짜 문자열이면 빈 결과를 반환한다.")
    @Test
    void testToLunarWithShortInput() {
        Map<String, String> result = EgovDateUtil.toLunar("2023010112");
        assertEquals("", result.get("day"));
        assertEquals("0", result.get("leap"));
    }

    @DisplayName("toSolar 호출 시, 음력 날짜를 양력 날짜로 변환한다.")
    @Test
    void testToSolar() {
        String lunar = EgovDateUtil.toLunar("20230101").get("day");
        String solar = EgovDateUtil.toSolar(lunar, 0);
        assertEquals("20230101", solar);
    }

    @DisplayName("convertWeek 호출 시, 영문 요일명을 국문 요일명으로 변환한다.")
    @Test
    void testConvertWeek() {
        assertEquals("일요일", EgovDateUtil.convertWeek("SUN"));
        assertEquals("월요일", EgovDateUtil.convertWeek("MON"));
        assertEquals("토요일", EgovDateUtil.convertWeek("SAT"));
        assertNull(EgovDateUtil.convertWeek("XXX"));
    }

    @DisplayName("validDate(sDate) 호출 시, 유효한 날짜인지 확인한다.")
    @Test
    void testValidDate() {
        assertTrue(EgovDateUtil.validDate("20230101"));
        assertFalse(EgovDateUtil.validDate("20230230"));
    }

    @DisplayName("validTime 호출 시, 유효한 시간인지 확인한다.")
    @Test
    void testValidTime() {
        assertTrue(EgovDateUtil.validTime("1230"));
        assertFalse(EgovDateUtil.validTime("2530"));
    }

    @DisplayName("addYMDtoWeek 호출 시, 가감된 날짜의 요일(영문 약어)을 반환한다.")
    @Test
    void testAddYMDtoWeek() {
        // 2023-01-01은 일요일(Sun) 이었으므로 1일을 더하면 월요일(Mon)이 된다.
        assertEquals("Mon", EgovDateUtil.addYMDtoWeek("20230101", 0, 0, 1));
    }

    @DisplayName("datetoInt / timetoInt 호출 시, 날짜/시간을 int 형으로 변환한다.")
    @Test
    void testDatetoIntAndTimetoInt() {
        assertEquals(20230101, EgovDateUtil.datetoInt("20230101"));
        assertEquals(1230, EgovDateUtil.timetoInt("1230"));
    }

    @DisplayName("validChkDate 호출 시, 8자리 문자열은 그대로, 10자리(하이픈 포함) 문자열은 하이픈을 제거하여 반환한다.")
    @Test
    void testValidChkDate() {
        assertEquals("20230101", EgovDateUtil.validChkDate("20230101"));
        assertEquals("20230101", EgovDateUtil.validChkDate("2023-01-01"));
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.validChkDate("202301"));
    }

    @DisplayName("validChkTime 호출 시, 4자리 문자열은 그대로, 콜론 포함 5자리 문자열은 콜론을 제거하여 반환한다.")
    @Test
    void testValidChkTime() {
        assertEquals("1230", EgovDateUtil.validChkTime("1230"));
        assertEquals("1230", EgovDateUtil.validChkTime("12:30"));
        assertThrows(IllegalArgumentException.class, () -> EgovDateUtil.validChkTime("123"));
    }
}
