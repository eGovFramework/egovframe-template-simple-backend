package egovframework.let.uat.esm.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.let.uat.esm.service.EgovSiteManagerService;
import egovframework.let.utl.sim.service.EgovFileScrty;

@ExtendWith(MockitoExtension.class)
class EgovSiteManagerApiControllerTest {

	private static final String LOGIN_ID = "test-admin";

	@Mock
	private EgovSiteManagerService siteManagerService;

	@Captor
	private ArgumentCaptor<Map<String, Object>> passwordMapCaptor;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		EgovSiteManagerApiController controller = new EgovSiteManagerApiController();
		ReflectionTestUtils.setField(controller, "siteManagerService", siteManagerService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
				.build();

		LoginVO user = new LoginVO();
		user.setId(LOGIN_ID);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
				user, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
	}

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"1", "12345"})
	void rejectsNewPasswordShorterThanSixCharacters(String newPassword) throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("old_password", "old-password");
		request.put("new_password", newPassword);

		assertInputError(request);
	}

	@Test
	void rejectsMissingNewPassword() throws Exception {
		assertInputError(Map.of("old_password", "old-password"));
	}

	@ParameterizedTest
	@CsvSource({"old, 123456", "old-password, 1234567", "old-password, ' 1234 '"})
	void acceptsAtLeastSixCharactersAndPreservesPasswordHashing(String oldPassword, String newPassword)
			throws Exception {
		when(siteManagerService.updateAdminPassword(anyMap())).thenReturn(1);

		updatePassword(Map.of("old_password", oldPassword, "new_password", newPassword))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value(ResponseCode.SUCCESS.getCode()));

		verify(siteManagerService).updateAdminPassword(passwordMapCaptor.capture());
		Map<String, Object> passwordMap = passwordMapCaptor.getValue();
		assertEquals(LOGIN_ID, passwordMap.get("login_id"));
		assertEquals(EgovFileScrty.encryptPasswordTwice(oldPassword, LOGIN_ID), passwordMap.get("old_password"));
		assertEquals(EgovFileScrty.encryptPasswordTwice(newPassword, LOGIN_ID), passwordMap.get("new_password"));
	}

	@Test
	void preservesSaveErrorWhenOldPasswordDoesNotMatch() throws Exception {
		when(siteManagerService.updateAdminPassword(anyMap())).thenReturn(0);

		updatePassword(Map.of("old_password", "wrong-password", "new_password", "123456"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value(ResponseCode.SAVE_ERROR.getCode()))
				.andExpect(jsonPath("$.resultMessage").value(ResponseCode.SAVE_ERROR.getMessage()));
		verify(siteManagerService).updateAdminPassword(anyMap());
	}

	private void assertInputError(Map<String, String> request) throws Exception {
		updatePassword(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value(ResponseCode.INPUT_CHECK_ERROR.getCode()))
				.andExpect(jsonPath("$.resultMessage").value("신규 암호는 6자 이상이어야 합니다."));
		verifyNoInteractions(siteManagerService);
	}

	private ResultActions updatePassword(Map<String, String> request) throws Exception {
		return mockMvc.perform(patch("/admin/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));
	}
}
