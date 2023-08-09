package egovframework.com.cmm.service;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.cmm.service
 * @filename : ResultVO.java
 * @modificationInformation
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일            수정자             수정내용
 *  ----------   ----------   ----------------------
 *  2023. 8. 9.    정완배             주석추가
 * </pre>
 *
 *
 */

@Schema(description = "응답 객체 VO")
@Getter
@Setter
public class ResultVO {

	@Schema(description = "응답 코드")
	private int resultCode = 0;
	
	@Schema(description = "응답 메시지")
	private String resultMessage = "OK";
	private Map<String, Object> result = new HashMap<String, Object>();

	public void putResult(String key, Object value) {
		result.put(key, value);
	}

	public Object getResult(String key) {
		return this.result.get(key);
	}


}
