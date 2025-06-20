package egovframework.let.cop.bbs.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.dto.request.BbsAttributeInsertRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * [게시판생성관리][EgovBBSAttributeManageApiController.insertBBSMasterInf] Controller
 * 단위 테스트
 * 
 * @author 이백행
 * @since 2024-09-20
 *
 */
//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor
@Slf4j
class EgovBBSAttributeManageApiControllerTestInsertBBSMasterInfTest {

	/**
	 * 
	 */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * 
	 */
	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * 게시판 속성정보 관리를 위한 데이터 접근 클래스
	 */
	@Autowired
	private EgovBBSAttributeManageService egovBBSAttributeManageService;

	@Test
	void test() throws Exception {
		// testData
		final BbsAttributeInsertRequestDTO bbsInsertRequestDTO = new BbsAttributeInsertRequestDTO();
		
		final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS"));
		bbsInsertRequestDTO.setBbsNm("test 이백행 게시판명 " + now);
		bbsInsertRequestDTO.setPosblAtchFileSize("0");
		bbsInsertRequestDTO.setBbsAttrbCode("BBSA02");
		bbsInsertRequestDTO.setBbsTyCode("BBST01");
		bbsInsertRequestDTO.setFileAtchPosblAt("Y");
		bbsInsertRequestDTO.setUseAt("Y");
		bbsInsertRequestDTO.setFrstRegisterId("admin");

		final String resultBbsId = egovBBSAttributeManageService.insertBBSMastetInf(bbsInsertRequestDTO);

		// given

		JwtResponse jwtResponse = getJwtResponse();

		// when
		mockMvc.perform(

				get("/bbsMaster")

						.param("searchCnd", "0")

						.param("searchWrd", bbsInsertRequestDTO.getBbsNm())

						.header("Authorization", jwtResponse.getJToken())

		)

				.andDo(print())

				.andExpect(status().isOk())

				// resultCode
				.andExpect(jsonPath("$.resultCode").value(200))

				.andExpect(jsonPath("$.resultCode").value(equalTo(200)))

				// resultMessage
				.andExpect(jsonPath("$.resultMessage").value("성공했습니다."))

				.andExpect(jsonPath("$.resultMessage").value(equalTo("성공했습니다.")))

				// resultCnt
				.andExpect(jsonPath("$.result.resultCnt").value(1))

				.andExpect(jsonPath("$.result.resultCnt").value(equalTo(1)))

				// bbsNm
				.andExpect(jsonPath("$.result.resultList[0].bbsNm").value(bbsInsertRequestDTO.getBbsNm()))

				.andExpect(jsonPath("$.result.resultList[0].bbsNm").value(equalTo(bbsInsertRequestDTO.getBbsNm())))

				// bbsId
				.andExpect(jsonPath("$.result.resultList[0].bbsId").value(resultBbsId))

				.andExpect(jsonPath("$.result.resultList[0].bbsId").value(equalTo(resultBbsId)))

		;

		// then
		if (log.isDebugEnabled()) {
			log.debug("resultBbsId={}", resultBbsId);
		}

		assertEquals("", "", "게시판 마스터 목록을 조회한다.");
	}

	@Getter
	@Setter
	static class JwtRequest {
		private String userSe;
		private String id;
		private String password;
	}

	@Getter
	@Setter
	static class JwtResponse {
		private int resultCode;
		private String jToken;
	}

	private JwtResponse getJwtResponse() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final JwtRequest jwtRequest = new JwtRequest();
		jwtRequest.setUserSe("USR");
		jwtRequest.setId("admin");
		jwtRequest.setPassword("1");
		HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);
//		final JwtResponse jwtResponse = restTemplate.postForObject("/auth/login-jwt", request, JwtResponse.class);
		final String content = restTemplate.postForObject("/auth/login-jwt", request, String.class);
		final String content2 = content.substring(content.indexOf("\"jToken\":\"") + 10);
		final String content3 = content2.substring(0, content2.indexOf("\""));
//		final ObjectMapper mapper = new ObjectMapper();
//		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		final JwtResponse jwtResponse = mapper.readValue(content, JwtResponse.class);
		final JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setJToken(content3);
		return jwtResponse;
	}

}