package egovframework.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Import({
	EgovConfigAppAspect.class,
	EgovConfigAppCommon.class,
	EgovConfigAppDatasource.class,
	EgovConfigAppIdGen.class,
	EgovConfigAppProperties.class,
	EgovConfigAppMapper.class,
	EgovConfigAppTransaction.class,
	EgovConfigAppValidator.class,
	EgovConfigAppWhitelist.class
})
@PropertySources({
	@PropertySource("classpath:/application.properties")
}) //CAUTION: min JDK 8
public class EgovConfigApp {

}
