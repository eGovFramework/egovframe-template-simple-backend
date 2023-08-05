package egovframework.com.security.pass;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fileName       : SecurityPassConfig
 * author         : crlee
 * date           : 2023/08/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/04        crlee       최초 생성
 */
@Configuration
public class SecurityPassConfig {

    @Bean
    protected SecurityPassUtils securityUtils(){
        return new SecurityPassUtils();
    }
}