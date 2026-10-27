package egovframework.let.cop.smt.sim.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.stream.Stream;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import egovframework.com.cmm.ApiExceptionHandler;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;

/** DB 없이 dev 프로파일에서 실제 MVC 응답의 상태 코드와 전체 JSON 구조를 검증한다. */
@SpringJUnitConfig(EgovScheduleQueryValidationTest.MvcConfiguration.class)
@WebAppConfiguration
@ActiveProfiles("dev")
@TestPropertySource(properties = {
		"server.error.include-stacktrace=always",
		"server.error.include-message=always"
})
class EgovScheduleQueryValidationTest {

	private static final String BAD_REQUEST_JSON = """
			{"resultCode":400,"resultMessage":"입력값 무결성 오류 입니다.","result":{}}
			""";

	@Autowired
	private WebApplicationContext context;
	@Autowired
	private Environment environment;
	@Autowired
	private EgovIndvdlSchdulManageService scheduleService;
	@Autowired
	private EgovCmmUseService codeService;

	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		reset(scheduleService, codeService);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	static Stream<Arguments> invalidDates() {
		return Stream.of("month", "daily", "week").flatMap(endpoint ->
				Stream.of("year", "month", "date").flatMap(parameter ->
						Stream.of("abc", "", "1.5", "-1", "+1", " 1", "1e2", "１２", "2147483648")
								.map(value -> Arguments.of(endpoint, parameter, value))));
	}

	@ParameterizedTest(name = "{0}: {1}={2} → ResultVO 400")
	@MethodSource("invalidDates")
	void invalidProvidedDateReturnsBadRequest(String endpoint, String parameter, String value) throws Exception {
		MockHttpServletRequestBuilder request = get("/schedule/" + endpoint);
		request.param("year", parameter.equals("year") ? value : "2026");
		request.param("month", parameter.equals("month") ? value : "0");
		request.param("date", parameter.equals("date") ? value : "1");

		mvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().json(BAD_REQUEST_JSON, true));
		verifyNoInteractions(scheduleService, codeService);
	}

	static Stream<Arguments> partialInvalidDates() {
		return Stream.of("month", "daily", "week").flatMap(endpoint ->
				Stream.of("year", "month", "date").map(parameter -> Arguments.of(endpoint, parameter)));
	}

	@ParameterizedTest(name = "{0}: {1}만 비숫자로 제공해도 ResultVO 400")
	@MethodSource("partialInvalidDates")
	void invalidPartialDateDoesNotSilentlyFallBack(String endpoint, String parameter) throws Exception {
		mvc.perform(get("/schedule/" + endpoint).param(parameter, "abc"))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(BAD_REQUEST_JSON, true));
		verifyNoInteractions(scheduleService, codeService);
	}

	@ParameterizedTest
	@ValueSource(strings = {"month", "daily", "week"})
	void numericDatesAndExistingDefaultBehaviorRemainSuccessful(String endpoint) throws Exception {
		when(codeService.selectCmmCodeDetail(any())).thenReturn(Collections.emptyList());
		when(scheduleService.selectIndvdlSchdulManageRetrieve(any())).thenReturn(Collections.emptyList());

		mvc.perform(get("/schedule/" + endpoint).param("year", "2026").param("month", "0").param("date", "01"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value(200));
		mvc.perform(get("/schedule/" + endpoint))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value(200));
		mvc.perform(get("/schedule/" + endpoint).param("year", "2026"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value(200));
	}

	@Test
	void globalNumberFormatHandlerDoesNotExposeTraceInDev() throws Exception {
		assertDevProfile();
		mvc.perform(get("/test/number-format").param("trace", "true"))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(BAD_REQUEST_JSON, true));
	}

	@Test
	void globalRuntimeHandlerDoesNotExposeTraceOrExceptionMessageInDev() throws Exception {
		assertDevProfile();
		mvc.perform(get("/test/runtime").param("trace", "true"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().json("""
						{"resultCode":500,"resultMessage":"요청 처리 중 오류가 발생했습니다.","result":{}}
						""", true));
	}

	private void assertDevProfile() {
		assertEquals("dev", environment.getActiveProfiles()[0]);
		assertEquals("always", environment.getProperty("server.error.include-stacktrace"));
	}

	@Configuration(proxyBeanMethods = false)
	@EnableWebMvc
	static class MvcConfiguration {
		@Bean
		EgovIndvdlSchdulManageService scheduleService() {
			return mock(EgovIndvdlSchdulManageService.class);
		}

		@Bean
		EgovCmmUseService codeService() {
			return mock(EgovCmmUseService.class);
		}

		@Bean
		EgovIndvdlSchdulManageApiController scheduleController(
				EgovIndvdlSchdulManageService scheduleService, EgovCmmUseService codeService) {
			return new EgovIndvdlSchdulManageApiController(scheduleService, codeService,
					mock(EgovFileMngService.class), mock(EgovFileMngUtil.class), mock(EgovCryptoService.class),
					new ResultVoHelper());
		}

		@Bean
		ApiExceptionHandler apiExceptionHandler() {
			return new ApiExceptionHandler();
		}

		@Bean
		FailureController failureController() {
			return new FailureController();
		}
	}

	@RestController
	static class FailureController {
		@GetMapping("/test/number-format")
		String numberFormat() {
			throw new NumberFormatException("sensitive-number-format-detail");
		}

		@GetMapping("/test/runtime")
		String runtime() {
			throw new IllegalStateException("sensitive-runtime-detail");
		}
	}
}
