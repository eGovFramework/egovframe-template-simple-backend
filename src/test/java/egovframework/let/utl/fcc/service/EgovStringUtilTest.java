package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * EgovStringUtil 결함 수정에 대한 회귀 테스트
 */
class EgovStringUtilTest {

	@Test
	void replaceChar_subject가여러문자여도결과가누적되지않는다() {
		assertEquals("heXlX world", EgovStringUtil.replaceChar("hello world", "lo", "X"));
	}

}
