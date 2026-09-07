package egovframework.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

import egovframework.let.cop.bbs.dto.request.BbsAttributeInsertRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class Jsr303ValidationSmokeTest {
	
	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Test
	void notBlankConstraintsFire() {
		
		BbsAttributeInsertRequestDTO dto = new BbsAttributeInsertRequestDTO(); // @NotBlank 4필드 전부 null
		Set<ConstraintViolation<BbsAttributeInsertRequestDTO>> violations = validator.validate(dto);
		assertEquals(4, violations.size()); // bbsNm, bbsIntrcn, bbsTyCode, bbsAttrbCode
	}

}
