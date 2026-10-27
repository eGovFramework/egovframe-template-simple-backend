package egovframework.com.cmm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import egovframework.com.cmm.service.ResultVO;
import lombok.extern.slf4j.Slf4j;

/**
 * API 런타임 예외를 ResultVO로 반환한다. 프로파일과 무관하게 예외 메시지와
 * 스택트레이스는 응답에 포함하지 않고 서버 로그에만 기록한다.
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ResultVO> handleNumberFormatException(NumberFormatException exception) {
		return errorResponse(HttpStatus.BAD_REQUEST, ResponseCode.INPUT_CHECK_ERROR.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResultVO> handleRuntimeException(RuntimeException exception) {
		log.error("API 요청 처리 중 런타임 예외가 발생했습니다.", exception);
		return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "요청 처리 중 오류가 발생했습니다.");
	}

	private ResponseEntity<ResultVO> errorResponse(HttpStatus status, String message) {
		ResultVO result = new ResultVO();
		result.setResultCode(status.value());
		result.setResultMessage(message);
		return ResponseEntity.status(status).body(result);
	}
}
