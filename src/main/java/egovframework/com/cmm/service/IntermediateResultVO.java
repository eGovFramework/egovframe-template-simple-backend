package egovframework.com.cmm.service;

import com.fasterxml.jackson.annotation.JsonInclude;

import egovframework.com.cmm.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
/**
 * 제네릭으로 응답 객체를 받아 동적으로 처리하기 위한 VO 클래스 입니다.
 * 추후 응답 객체로 모두 변환이 완료되면 ResultVO를 대체하여 사용합니다.
 * 
 * result 반환 문제로 인해 사용되는 임시 클래스 입니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.04.10    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */


@Schema(description = "응답 객체 VO (최종 구조로 가는 중간 단계 VO)")
@Getter
@Setter
public class IntermediateResultVO<T> {

	@Schema(description = "응답 코드", example = "0")
	private int resultCode;
	
	@Schema(description = "응답 메시지", example = "OK")
	private String resultMessage;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Schema(description = "응답 객체")
	private T result;
	
	public static <T> IntermediateResultVO<T> success(T data) {
		IntermediateResultVO<T> result = new IntermediateResultVO<>();
		result.setResultCode(ResponseCode.SUCCESS.getCode());
		result.setResultMessage(ResponseCode.SUCCESS.getMessage());
		result.setResult(data);
		return result;
	}
	
	public static <T> IntermediateResultVO<T> inputCheckError(T data) {
		IntermediateResultVO<T> result = new IntermediateResultVO<>();
		result.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
		result.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());
		result.setResult(data);
		return result;
	}
}
