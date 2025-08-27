package egovframework.let.utl.fcc.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EgovStringUtil 유틸리티 클래스 테스트")
class EgovStringUtilTest {

	@Test
	@DisplayName("isEmpty: null 입력 시 true를 반환해야 한다")
	void isEmpty_withNull_shouldReturnTrue() {
		// Act
		boolean result = EgovStringUtil.isEmpty(null);

		// Assert
		assertTrue(result);
	}

	@Test
	@DisplayName("isEmpty: 빈 문자열 입력 시 true를 반환해야 한다")
	void isEmpty_withEmptyString_shouldReturnTrue() {
		// Act
		boolean result = EgovStringUtil.isEmpty("");

		// Assert
		assertTrue(result);
	}

	@Test
	@DisplayName("isEmpty: 내용이 있는 문자열 입력 시 false를 반환해야 한다")
	void isEmpty_withNonEmptyString_shouldReturnFalse() {
		// Act
		boolean result = EgovStringUtil.isEmpty("Hello");

		// Assert
		assertFalse(result);
	}

	@Test
	@DisplayName("isNotEmpty: null 입력 시 false를 반환해야 한다")
	void isNotEmpty_withNull_shouldReturnFalse() {
		// Act
		boolean result = EgovStringUtil.isNotEmpty(null);

		// Assert
		assertFalse(result);
	}

	@Test
	@DisplayName("isNotEmpty: 빈 문자열 입력 시 false를 반환해야 한다")
	void isNotEmpty_withEmptyString_shouldReturnFalse() {
		// Act
		boolean result = EgovStringUtil.isNotEmpty("");

		// Assert
		assertFalse(result);
	}

	@Test
	@DisplayName("isNotEmpty: 내용이 있는 문자열 입력 시 true를 반환해야 한다")
	void isNotEmpty_withNonEmptyString_shouldReturnTrue() {
		// Act
		boolean result = EgovStringUtil.isNotEmpty("Hello");

		// Assert
		assertTrue(result);
	}

	@Test
	@DisplayName("isBlank: null 입력 시 true를 반환해야 한다")
	void isBlank_withNull_shouldReturnTrue() {
		// Act
		boolean result = EgovStringUtil.isBlank(null);

		// Assert
		assertTrue(result);
	}

	@Test
	@DisplayName("isBlank: 빈 문자열 입력 시 true를 반환해야 한다")
	void isBlank_withEmptyString_shouldReturnTrue() {
		// Act
		boolean result = EgovStringUtil.isBlank("");

		// Assert
		assertTrue(result);
	}

	@Test
	@DisplayName("isBlank: 공백만 있는 문자열 입력 시 true를 반환해야 한다")
	void isBlank_withOnlyWhitespace_shouldReturnTrue() {
		// Act
		boolean result = EgovStringUtil.isBlank("   ");

		// Assert
		assertTrue(result);
	}

	@Test
	@DisplayName("isBlank: 내용이 있는 문자열 입력 시 false를 반환해야 한다")
	void isBlank_withNonBlankString_shouldReturnFalse() {
		// Act
		boolean result = EgovStringUtil.isBlank("Hello");

		// Assert
		assertFalse(result);
	}

	@Test
	@DisplayName("isNotBlank: null 입력 시 false를 반환해야 한다")
	void isNotBlank_withNull_shouldReturnFalse() {
		// Act
		boolean result = EgovStringUtil.isNotBlank(null);

		// Assert
		assertFalse(result);
	}

	@Test
	@DisplayName("isNotBlank: 빈 문자열 입력 시 false를 반환해야 한다")
	void isNotBlank_withEmptyString_shouldReturnFalse() {
		// Act
		boolean result = EgovStringUtil.isNotBlank("");

		// Assert
		assertFalse(result);
	}

	@Test
	@DisplayName("isNotBlank: 공백만 있는 문자열 입력 시 false를 반환해야 한다")
	void isNotBlank_withOnlyWhitespace_shouldReturnFalse() {
		// Act
		boolean result = EgovStringUtil.isNotBlank("   ");

		// Assert
		assertFalse(result);
	}

	@Test
	@DisplayName("isNotBlank: 내용이 있는 문자열 입력 시 true를 반환해야 한다")
	void isNotBlank_withNonBlankString_shouldReturnTrue() {
		// Act
		boolean result = EgovStringUtil.isNotBlank("Hello");

		// Assert
		assertTrue(result);
	}
}
