package egovframework.com.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.com.cmm.LoginVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtAuthenticationFilterTest {

    @Autowired
    private JwtAuthenticationFilter filter;

    @MockBean
    private EgovJwtTokenUtil jwtTokenUtil;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("유효한 토큰이 주어지면 인증 객체가 설정된다")
    @Test
    public void testValidTokenSetsAuthentication() throws Exception {
        String fakeToken = "valid.jwt.token";

        LoginVO loginVO = new LoginVO();
        loginVO.setId("user1");
        loginVO.setGroupNm("ROLE_ADMIN");

        request.addHeader("Authorization", fakeToken);
        when(jwtTokenUtil.getLoginVOFromToken(fakeToken)).thenReturn(loginVO);

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user1", ((LoginVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    @DisplayName("유효하지 않은 토큰은 기존 인증을 제거하고 401 응답으로 요청을 차단한다")
    @ParameterizedTest(name = "기존 인증 존재: {0}")
    @ValueSource(booleans = {false, true})
    public void testInvalidTokenRejectsRequest(boolean hasExistingAuthentication) throws Exception {
        String invalidToken = "invalid.jwt.token";
        request.addHeader("Authorization", invalidToken);

        if (hasExistingAuthentication) {
            SecurityContextHolder.getContext().setAuthentication(
                    UsernamePasswordAuthenticationToken.authenticated(
                            "previous-user", null, AuthorityUtils.createAuthorityList("ROLE_USER")));
        }

        when(jwtTokenUtil.getLoginVOFromToken(invalidToken))
                .thenThrow(new InvalidJwtException("Invalid token"));

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        JsonNode body = new ObjectMapper().readTree(response.getContentAsString());
        assertEquals("401", body.path("resultCode").textValue());
        assertEquals("invalid or expired token", body.path("resultMessage").textValue());
        verifyNoInteractions(filterChain);
    }
}

