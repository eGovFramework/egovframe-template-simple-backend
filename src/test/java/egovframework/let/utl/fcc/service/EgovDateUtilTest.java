package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EgovDateUtil 결함 수정에 대한 회귀 테스트
 */
class EgovDateUtilTest {

	@Test
	void isLeapYear_윤년이면true를반환한다() {
		assertTrue(EgovDateUtil.isLeapYear(2004));
		assertTrue(EgovDateUtil.isLeapYear(2000));
	}

	@Test
	void formatTime_유효한4자리시간문자열을구분자로쪼갠다() {
		assertEquals("15:12", EgovDateUtil.formatTime("1512", ":"));
	}

	@Test
	void getCurrentDate_포맷지정시예외없이변환된날짜를반환한다() {
		String result = EgovDateUtil.getCurrentDate("yyyy-MM-dd");
		assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
	}

}
