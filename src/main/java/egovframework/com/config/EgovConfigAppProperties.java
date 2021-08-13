package egovframework.com.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.egovframe.rte.fdl.property.impl.EgovPropertyServiceImpl;

/**
 * @ClassName : EgovConfigAppProperties.java
 * @Description : Properties 설정
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */

@Configuration
public class EgovConfigAppProperties {
	@Bean(destroyMethod = "destroy")
	public EgovPropertyServiceImpl propertiesService() {
		EgovPropertyServiceImpl egovPropertyServiceImpl = new EgovPropertyServiceImpl();

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("pageUnit", "10");
		properties.put("pageSize", "10");
		properties.put("posblAtchFileSize", "5242880");
		properties.put("Globals.fileStorePath", "/user/file/sht/");
		properties.put("Globals.addedOptions", "false");

		egovPropertyServiceImpl.setProperties(properties);
		return egovPropertyServiceImpl;
	}
}
