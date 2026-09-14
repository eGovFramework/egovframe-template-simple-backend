package egovframework.let.uss.umt.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.stream.Stream;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.utl.sim.service.EgovFileScrty;
import egovframework.let.uss.umt.web.EgovMberManageApiController;

/** 실제 컨트롤러·서비스·DAO·HSQL 매퍼를 연결한다. 인증 필터는 포함하지 않는다. */
class EgovMberOptionalPasswordTest {

	private EmbeddedDatabase database;
	private JdbcTemplate jdbc;
	private MockMvc mvc;
	private String originalHash;

	@BeforeEach
	void setUp() throws Exception {
		database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
				.setName("member-" + UUID.randomUUID()).build();
		jdbc = new JdbcTemplate(database);
		jdbc.execute("CREATE TABLE LETTNEMPLYRINFO (ESNTL_ID VARCHAR(30) PRIMARY KEY, "
				+ "EMPLYR_ID VARCHAR(30), USER_NM VARCHAR(60), PASSWORD VARCHAR(200), "
				+ "ORGNZT_ID VARCHAR(30), PASSWORD_HINT VARCHAR(30), PASSWORD_CNSR VARCHAR(100), "
				+ "IHIDNUM VARCHAR(30), SEXDSTN_CODE VARCHAR(10), ZIP VARCHAR(10), HOUSE_ADRES VARCHAR(100), "
				+ "AREA_NO VARCHAR(10), EMPLYR_STTUS_CODE VARCHAR(10), DETAIL_ADRES VARCHAR(100), "
				+ "HOUSE_END_TELNO VARCHAR(10), MBTLNUM VARCHAR(30), GROUP_ID VARCHAR(30), "
				+ "FXNUM VARCHAR(30), EMAIL_ADRES VARCHAR(100), HOUSE_MIDDLE_TELNO VARCHAR(10), SBSCRB_DE TIMESTAMP)");
		originalHash = EgovFileScrty.encryptPasswordTwice("Original123", "reviewuser");
		jdbc.update("INSERT INTO LETTNEMPLYRINFO (ESNTL_ID, EMPLYR_ID, USER_NM, PASSWORD, EMPLYR_STTUS_CODE) "
				+ "VALUES (?, ?, ?, ?, ?)", "MEMBER_REVIEW", "reviewuser", "기존 이름", originalHash, "P");
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(database);
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.getTypeAliasRegistry().registerAlias("mberVO", egovframework.let.uss.umt.service.MberManageVO.class);
		configuration.getTypeAliasRegistry().registerAlias("userSearchVO", egovframework.let.uss.umt.service.UserDefaultVO.class);
		factory.setConfiguration(configuration);
		factory.setMapperLocations(new ClassPathResource("egovframework/mapper/let/uss/umt/EgovMberManage_SQL_hsql.xml"));
		MberManageDAO dao = new MberManageDAO();
		dao.setSqlSessionTemplate(new SqlSessionTemplate(factory.getObject()));
		EgovMberManageServiceImpl service = new EgovMberManageServiceImpl();
		ReflectionTestUtils.setField(service, "mberManageDAO", dao);
		EgovMberManageApiController controller = new EgovMberManageApiController(service,
				mock(EgovCmmUseService.class), mock(EgovPropertyService.class), new ResultVoHelper());
		mvc = MockMvcBuilders.standaloneSetup(controller).setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return parameter.getParameterType() == LoginVO.class;
			}
			@Override
			public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
					NativeWebRequest request, WebDataBinderFactory binderFactory) {
				LoginVO user = new LoginVO();
				user.setUniqId("MEMBER_REVIEW");
				return user;
			}
		}).build();
	}

	@AfterEach
	void tearDown() {
		if (database != null) database.shutdown();
	}

	@ParameterizedTest(name = "{0}: password {1}")
	@MethodSource("requests")
	void updatesOtherFieldsAndOnlyChangesPasswordWhenProvided(String endpoint, String label,
			String passwordJson, boolean changed) throws Exception {
		String body = "{\"uniqId\":\"MEMBER_REVIEW\",\"mberId\":\"reviewuser\",\"mberNm\":\"수정 이름\","
				+ "\"mberSttus\":\"P\",\"groupId\":\"GROUP_00000000000001\"" + passwordJson + "}";
		mvc.perform(put(endpoint).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk()).andExpect(jsonPath("$.resultCode").value(200));
		assertEquals("수정 이름", jdbc.queryForObject("SELECT USER_NM FROM LETTNEMPLYRINFO", String.class));
		assertEquals(changed ? EgovFileScrty.encryptPasswordTwice("Changed123", "reviewuser") : originalHash,
				jdbc.queryForObject("SELECT PASSWORD FROM LETTNEMPLYRINFO", String.class));
	}

	static Stream<Arguments> requests() {
		return Stream.of("/members/update", "/mypage/update").flatMap(endpoint -> Stream.of(
				Arguments.of(endpoint, "생략", "", false),
				Arguments.of(endpoint, "null", ",\"password\":null", false),
				Arguments.of(endpoint, "빈 문자열", ",\"password\":\"\"", false),
				Arguments.of(endpoint, "새 비밀번호", ",\"password\":\"Changed123\"", true)));
	}
}
