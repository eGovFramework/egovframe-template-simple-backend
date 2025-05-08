package egovframework.com.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
	
	private static final String API_NAME = "Simple Homepage Project API";
	private static final String API_VERSION = "4.3.0";
	private static final String API_DESCRIPTION = "심플홈페이지 프로젝트 명세서";

	@Bean
	public OpenAPI api() {
		
		Schema<?> fileMap = new Schema<Map<String, String>>()
				.addProperty("atchFileId", new StringSchema().example(""))
				.addProperty("fileSn", new StringSchema().example("0"));
		
		Schema<?> passwordMap = new Schema<Map<String, String>>()
				.addProperty("old_password", new StringSchema().example(""))
				.addProperty("new_password", new StringSchema().example(""));
		
		return new OpenAPI()
				.info(new Info().title(API_NAME)
				.description(API_DESCRIPTION)
				.version(API_VERSION)
				.contact(new Contact().name("eGovFrame").url("https://www.egovframe.go.kr/").email("egovframesupport@gmail.com"))
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
				.components(new Components()
						.addSecuritySchemes("Authorization", new SecurityScheme()
								.name("Authorization")
								.type(SecurityScheme.Type.APIKEY)
								.in(SecurityScheme.In.HEADER))
						.addSchemas("fileMap", fileMap)
						.addSchemas("passwordMap", passwordMap))
				.externalDocs(new ExternalDocumentation()
				.description("Wiki Documentation")
			    .url("https://github.com/eGovFramework/egovframe-template-simple-backend/wiki"));
	}
}
