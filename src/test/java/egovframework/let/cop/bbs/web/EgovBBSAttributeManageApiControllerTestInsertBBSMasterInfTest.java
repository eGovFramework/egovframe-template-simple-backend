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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.dto.request.BbsAttributeInsertRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.utl.sim.service.EgovFileScrty;
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
		// 프론트엔드(passwordHash.js)와 동일하게 1차 해시하여 전송 (서버가 2차 해시로 저장값과 비교)
		// (admin/Admin@1234 은 README·시드에 공개된 샘플 계정 — 운영 비밀 아님)
		jwtRequest.setPassword(clientHashedPassword("admin", "Admin@1234"));
		HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);

		// JWT 는 응답 본문이 아니라 httpOnly ACCESS_TOKEN 쿠키로 발급된다. Set-Cookie 에서 추출.
		final ResponseEntity<String> response = restTemplate.postForEntity("/auth/login-jwt", request, String.class);
		final JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setJToken(extractAccessToken(response.getHeaders()));
		return jwtResponse;
	}

	private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";

	private String extractAccessToken(HttpHeaders responseHeaders) {
		final List<String> setCookies = responseHeaders.get(HttpHeaders.SET_COOKIE);
		if (setCookies == null) {
			return null;
		}
		for (String cookie : setCookies) {
			if (cookie.startsWith(ACCESS_TOKEN_COOKIE + "=")) {
				final String value = cookie.substring((ACCESS_TOKEN_COOKIE + "=").length());
				final int semi = value.indexOf(';');
				return semi >= 0 ? value.substring(0, semi) : value;
			}
		}
		return null;
	}

	private static String clientHashedPassword(String id, String rawPassword) {
		try {
			// 프론트엔드 passwordHash.js 와 동일한 1차 해시 = EgovFileScrty.encryptPassword(raw, id)
			return EgovFileScrty.encryptPassword(rawPassword, id);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}