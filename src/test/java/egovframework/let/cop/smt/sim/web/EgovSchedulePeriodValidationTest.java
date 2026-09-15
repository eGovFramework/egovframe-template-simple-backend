package egovframework.let.cop.smt.sim.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

class EgovSchedulePeriodValidationTest {

	private EgovIndvdlSchdulManageService service;
	private EgovFileMngService fileService;
	private EgovFileMngUtil fileUtil;
	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		service = mock(EgovIndvdlSchdulManageService.class);
		fileService = mock(EgovFileMngService.class);
		fileUtil = mock(EgovFileMngUtil.class);
		EgovIndvdlSchdulManageApiController controller = new EgovIndvdlSchdulManageApiController(service,
				mock(EgovCmmUseService.class), fileService, fileUtil, mock(EgovCryptoService.class), new ResultVoHelper());
		mvc = MockMvcBuilders.standaloneSetup(controller).setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return parameter.getParameterType() == LoginVO.class;
			}
			@Override
			public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
					NativeWebRequest request, WebDataBinderFactory binderFactory) {
				LoginVO user = new LoginVO();
				user.setUniqId("SCHEDULE_REVIEW_USER");
				return user;
			}
		}).build();
	}

	@ParameterizedTest(name = "{0}: {1} ~ {2}")
	@MethodSource("invalidPeriods")
	void rejectsInvalidPeriodBeforeSavingScheduleOrAttachment(HttpMethod method, String begin, String end) throws Exception {
		mvc.perform(request(method, begin, end).file(attachment()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value(900));
		verifyNoInteractions(service, fileService, fileUtil);
	}

	@ParameterizedTest(name = "{0}: {1} ~ {2}, attachment={3}")
	@MethodSource("validPeriods")
	void acceptsValidPeriodWithoutChangingItsValues(HttpMethod method, String begin, String end, boolean attachment) throws Exception {
		MockMultipartHttpServletRequestBuilder request = request(method, begin, end);
		if (attachment) {
			request.file(attachment());
			when(fileUtil.parseFileInf(any(), any(), any(Integer.class), any(), any())).thenReturn(List.of(new FileVO()));
			when(fileService.insertFileInfs(any())).thenReturn("FILE_REVIEW");
		}
		mvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value(200));
		ArgumentCaptor<IndvdlSchdulManageVO> saved = ArgumentCaptor.forClass(IndvdlSchdulManageVO.class);
		if (method == HttpMethod.POST) verify(service).insertIndvdlSchdulManage(saved.capture());
		else verify(service).updateIndvdlSchdulManage(saved.capture());
		assertEquals(begin, saved.getValue().getSchdulBgnde());
		assertEquals(end, saved.getValue().getSchdulEndde());
		assertEquals("SCHEDULE_REVIEW_USER", saved.getValue().getLastUpdusrId());
		if (attachment) {
			verify(fileService).insertFileInfs(any());
			assertEquals("FILE_REVIEW", saved.getValue().getAtchFileId());
		} else verifyNoInteractions(fileService, fileUtil);
	}

	private static MockMultipartHttpServletRequestBuilder request(HttpMethod method, String begin, String end) {
		MockMultipartHttpServletRequestBuilder request = multipart(method,
				method == HttpMethod.POST ? "/schedule" : "/schedule/SCHDUL_REVIEW");
		if (begin != null) request.param("schdulBgnde", begin);
		if (end != null) request.param("schdulEndde", end);
		request.param("schdulNm", "기간 검증").param("schdulCn", "검증 내용");
		return request;
	}

	private static MockMultipartFile attachment() {
		return new MockMultipartFile("file_0", "schedule.txt", "text/plain", new byte[] { 1 });
	}

	static Stream<Arguments> invalidPeriods() {
		String normal = "20260913090000";
		return Stream.of(HttpMethod.POST, HttpMethod.PUT).flatMap(method -> Stream.concat(
				Stream.of(Arguments.of(method, "20260913100000", normal)),
				Stream.of(null, "", "bad-date", "202609130900", "202609130900000", " 20260913090000", "20260913090000x",
						"20260230090000", "20260229090000", "20261313090000", "20260013090000", "20260900090000",
						"20260913240000", "20260913096000", "20260913090060", "00000913090000")
						.flatMap(invalid -> Stream.of(Arguments.of(method, invalid, normal), Arguments.of(method, normal, invalid)))));
	}

	static Stream<Arguments> validPeriods() {
		return Stream.of(HttpMethod.POST, HttpMethod.PUT).flatMap(method -> Stream.of(false, true).flatMap(attachment -> Stream.of(
				Arguments.of(method, "20260913090000", "20260913100000", attachment),
				Arguments.of(method, "20260913000000", "20260913000000", attachment),
				Arguments.of(method, "20240229090000", "20240301090000", attachment),
				Arguments.of(method, "20261231235959", "20270101000000", attachment))));
	}
}
