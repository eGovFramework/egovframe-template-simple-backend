package egovframework.com.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : EgovConfigAppWhitelist.java
 * @Description : whiteList 설정
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
public class EgovConfigAppWhitelist {

	@Bean
	public List<String> egovPageLinkWhitelist() {
		List<String> whiteList = new ArrayList<String>();
		whiteList.add("main/inc/EgovIncHeader");
		whiteList.add("main/inc/EgovIncTopnav");
		whiteList.add("main/inc/EgovIncLeftmenu");
		whiteList.add("main/inc/EgovIncFooter");
		whiteList.add("main/sample_menu/Intro");
		whiteList.add("main/sample_menu/EgovDownloadDetail");
		whiteList.add("main/sample_menu/EgovDownloadModify");
		whiteList.add("main/sample_menu/EgovQADetail");
		whiteList.add("main/sample_menu/EgovAboutSite");
		whiteList.add("main/sample_menu/EgovHistory");
		whiteList.add("main/sample_menu/EgovOrganization");
		whiteList.add("main/sample_menu/EgovLocation");
		whiteList.add("main/sample_menu/EgovProductInfo");
		whiteList.add("main/sample_menu/EgovServiceInfo");
		whiteList.add("main/sample_menu/EgovDownload");
		whiteList.add("main/sample_menu/EgovQA");
		whiteList.add("main/sample_menu/EgovService");
		return whiteList;
	}
}
