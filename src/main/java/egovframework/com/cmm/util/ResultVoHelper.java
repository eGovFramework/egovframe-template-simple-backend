package egovframework.com.cmm.util;

import java.util.Map;

import org.springframework.stereotype.Component;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
/**
 * ResultVO 생성을 돕는 헬퍼 유틸리티 클래스
 * 공통 응답 객체(ResultVO)를 표준 구조로 생성할 수 있도록 사용하는 클래스 입니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.04.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.04.06    김재섭(nirsa)      최초 생성 
 *
 * </pre>
 */

@Component
public class ResultVoHelper {
	/**
	 * Map 기반 결과 데이터를 ResultVO로 생성한다.
	 *
	 * @param resultMap 결과 데이터 Map
	 * @param code 응답 코드 (ResponseCode)
	 * @return 생성된 ResultVO 객체
	 */
	public ResultVO buildFromMap(Map<String, Object> resultMap, ResponseCode code) {
	    ResultVO resultVO = new ResultVO();
	    resultVO.setResult(resultMap);
	    resultVO.setResultCode(code.getCode());
	    resultVO.setResultMessage(code.getMessage());
	    return resultVO;
	}
	
	/**
	 * 이미 생성된 ResultVO 객체에 응답 코드 및 메시지를 설정한다.
	 *
	 * @param resultVO 결과 VO 객체
	 * @param code 응답 코드 (ResponseCode)
	 * @return 코드가 적용된 ResultVO 객체
	 */
	public ResultVO buildFromResultVO(ResultVO resultVO, ResponseCode code) {
	    resultVO.setResultCode(code.getCode());
	    resultVO.setResultMessage(code.getMessage());
	    return resultVO;
	}
}
