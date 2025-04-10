package egovframework.com.jwt;

import egovframework.com.cmm.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
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

    @DisplayName("유효하지 않은 토큰이 주어지면 인증 객체가 설정되지 않는다")
    @Test
    public void testInvalidTokenDoesNotSetAuthentication() throws Exception {
        String invalidToken = "invalid.jwt.token";
        request.addHeader("Authorization", invalidToken);

        when(jwtTokenUtil.getLoginVOFromToken(invalidToken))
                .thenThrow(new InvalidJwtException("Invalid token"));

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}

