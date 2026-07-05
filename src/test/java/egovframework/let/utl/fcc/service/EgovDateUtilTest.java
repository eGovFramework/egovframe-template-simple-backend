package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.Test;

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

}
