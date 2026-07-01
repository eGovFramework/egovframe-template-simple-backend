package egovframework.com.config;

import egovframework.com.cmm.EgovMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * fileName       : EgovConfigAppMsg
 * author         : crlee
 * date           : 2023/05/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/05        crlee       최초 생성
 * 2026/06/26        이백행       [2026년 컨트리뷰션] @Bean 메서드의 불필요한 public 접근제어자 제거
 */
@Configuration
public class EgovConfigAppMsg {

    /**
     * @return [Resource 설정] 메세지 Properties 경로 설정
     */
    @Bean
    ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasenames(
                "classpath:/egovframework/message/com/message-common",
                "classpath:/org/egovframe/rte/fdl/idgnr/messages/idgnr",
                "classpath:/org/egovframe/rte/fdl/property/messages/properties");
        reloadableResourceBundleMessageSource.setCacheSeconds(60);
        return reloadableResourceBundleMessageSource;
    }

    /**
     * @return [Resource 설정] 메세지 소스 등록
     */
    @Bean
    EgovMessageSource egovMessageSource() {
        EgovMessageSource egovMessageSource = new EgovMessageSource();
        egovMessageSource.setReloadableResourceBundleMessageSource(messageSource());
        return egovMessageSource;
    }

}
