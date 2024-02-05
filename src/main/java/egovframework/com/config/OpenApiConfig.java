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
	private static final String API_VERSION = "4.2.0";
	private static final String API_DESCRIPTION = "심플홈페이지 프로젝트 명세서";

	@Bean
	public OpenAPI api() {
		
		Schema<?> searchBbsMap = new Schema<Map<String, String>>()
				.addProperty("bbsId", new StringSchema().example("BBSMSTR_AAAAAAAAAAAA"))
				.addProperty("pageIndex", new StringSchema().example("1"))
				.addProperty("searchCnd", new StringSchema().example("0"))
				.addProperty("searchWrd", new StringSchema().example(""));
		
		Schema<?> searchMap = new Schema<Map<String, String>>()
				.addProperty("pageIndex", new StringSchema().example("1"))
				.addProperty("searchCnd", new StringSchema().example("0"))
				.addProperty("searchWrd", new StringSchema().example(""));
		
		Schema<?> fileMap = new Schema<Map<String, String>>()
				.addProperty("atchFileId", new StringSchema().example(""))
				.addProperty("fileSn", new StringSchema().example("0"));
		
		Schema<?> searchSchdulMap = new Schema<Map<String, String>>()
				.addProperty("schdulSe", new StringSchema().example(""))
				.addProperty("year", new StringSchema().example("2023"))
				.addProperty("month", new StringSchema().example("0"))
				.addProperty("date", new StringSchema().example("1"));
		
		Schema<?> searchSchdulWeekMap = new Schema<Map<String, String>>()
				.addProperty("schdulSe", new StringSchema().example(""))
				.addProperty("year", new StringSchema().example("2023"))
				.addProperty("month", new StringSchema().example("0"))
				.addProperty("date", new StringSchema().example("1"))
				.addProperty("weekDay", new StringSchema().example("0"))
				.addProperty("weekOfMonth", new StringSchema().example("1"));
		
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
						.addSchemas("searchBbsMap", searchBbsMap)
						.addSchemas("searchMap", searchMap)
						.addSchemas("fileMap", fileMap)
						.addSchemas("searchSchdulMap", searchSchdulMap)
						.addSchemas("searchSchdulWeekMap", searchSchdulWeekMap)
						.addSchemas("passwordMap", passwordMap))
				.externalDocs(new ExternalDocumentation()
				.description("Wiki Documentation")
			    .url("https://github.com/eGovFramework/egovframe-template-simple-backend/wiki"));
	}
}
