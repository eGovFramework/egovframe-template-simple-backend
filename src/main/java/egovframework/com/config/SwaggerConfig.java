package egovframework.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//accessToken 입력 화면과 처리 라이브러리 추가(아래4줄)
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.service.*;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.config
 * @filename : SwaggerConfig.java
 * @modificationInformation
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일            수정자             수정내용
 *  ----------   ----------   ----------------------
 *  2023. 8. 9.    정완배              주석추가
 * </pre>
 *
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private static final String API_NAME = "Simple Homepage Project API";
	private static final String API_VERSION = "4.1.0";
	private static final String API_DESCRIPTION = "심플홈페이지 프로젝트 명세서";
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("egovframework"))
				.paths(PathSelectors.any())
				.build()
				.securityContexts(Arrays.asList(securityContext())) // 스웨그에서 컨텐츠 url 접근 시 인증처리를 위한 보안 규칙 호출
                .securitySchemes(Arrays.asList(apiKey())); // 스웨그 화면상단에 토큰값 입력하는 창 구조 호출, 여기에 배열로 추가 apiKey메서드를 입력가능	
	}
	
	// Authorization창에 Token값 입력 화면 구조 
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
    
    // 스웨그에서 컨텐츠 url 접근 시 인증처리를 위한 보안 규칙 추가(아래)
    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth()).operationSelector(operationContext -> true).build();
    }
    
    // 토큰 인증영역 배열리스트을 반환하는 매서드
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); // 인증영역 객체 생성
        AuthorizationScope[] authorizationScopeArray = new AuthorizationScope[1]; // 빈 배열 인증영역 객체 생성
        authorizationScopeArray[0] = authorizationScope; // 배열변수에 인증영역 객체 등록
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopeArray)); // 여기에 배열로 추가 SecurityReference객체를 입력가능
    }
	
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(API_NAME)
				.version(API_VERSION)
				.description(API_DESCRIPTION)
				.contact(new Contact("eGovFrame", "https://www.egovframe.go.kr/", "egovframesupport@gmail.com"))
				.build();
	}

}
