package egovframework.com.security;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import egovframework.com.cmm.LoginVO;
import egovframework.com.jwt.EgovJwtTokenUtil;

/**
 * 운영 SecurityConfig의 필터 체인을 검증하며 컨트롤러의 업무 인가는 검증하지 않는다.
 * MVC와 실제 JWT 필터만 기동하므로 외부 DB나 요청 본문이 필요하지 않다.
 * 허용된 요청의 404 등은 정상이다. 업무 응답의 resultCode와 HTTP 401/403을 혼동하지 않는다.
 */
@SpringBootTest(classes = SecurityConfigAuthorizationTest.SecurityTestConfiguration.class)
@AutoConfigureMockMvc
class SecurityConfigAuthorizationTest {

    @Configuration(proxyBeanMethods = false)
    @TestComponent
    @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
    @Import({SecurityConfig.class, EgovJwtTokenUtil.class})
    static class SecurityTestConfiguration {
    }

    private enum Outcome {
        UNAUTHORIZED, FORBIDDEN, PASSED
    }

    private enum Caller {
        ANONYMOUS(null), USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

        private final String groupNm;

        Caller(String groupNm) {
            this.groupNm = groupNm;
        }
    }

    private record Rule(HttpMethod method, String path, Outcome anonymous, Outcome user, Outcome admin) {
        Outcome expectedFor(Caller caller) {
            return switch (caller) {
                case ANONYMOUS -> anonymous;
                case USER -> user;
                case ADMIN -> admin;
            };
        }
    }

    // SecurityConfig 160-176행의 순서와 AUTH_*_WHITELIST를 한 표에서 대조한다.
    // /auth/me(70행)는 컨트롤러가 익명에게 직접 401을 반환하므로 공개 HTTP 상태 검사에서 제외한다.
    private static final List<Rule> RULES = List.of(
            adminOnly(HttpMethod.GET, "/admin/authorization-matrix"),
            adminOnly(HttpMethod.GET, "/members"),
            adminOnly(HttpMethod.GET, "/members/insert"),
            authenticated(HttpMethod.GET, "/mypage"),
            authenticated(HttpMethod.PUT, "/mypage/update"),
            authenticated(HttpMethod.GET, "/inform/authorization-matrix"),
            adminOnly(HttpMethod.GET, "/schedule/month"),
            adminOnly(HttpMethod.POST, "/schedule"),
            adminOnly(HttpMethod.PUT, "/schedule/SCHDUL_AUTH_TEST"),
            adminOnly(HttpMethod.DELETE, "/schedule/SCHDUL_AUTH_TEST"),
            adminOnly(HttpMethod.GET, "/bbsMaster"),
            adminOnly(HttpMethod.GET, "/bbsMaster/BBSMSTR_AUTH_TEST"),
            adminOnly(HttpMethod.GET, "/bbsUseInf"),
            adminOnly(HttpMethod.GET, "/bbsUseInf/SYSTEM_AUTH_TEST/BBSMSTR_AUTH_TEST"),
            adminOnly(HttpMethod.GET, "/notUsedBbsMaster"),

            // AUTH_WHITELIST 63-94행: 모든 경로 패턴의 대표 요청. 메서드 제한이 없다.
            publicRoute(HttpMethod.GET, "/"),
            publicRoute(HttpMethod.GET, "/error"),
            publicRoute(HttpMethod.POST, "/login/authorization-matrix"),
            publicRoute(HttpMethod.POST, "/auth/login-jwt"),
            publicRoute(HttpMethod.GET, "/auth/oauth-state"),
            publicRoute(HttpMethod.GET, "/auth/logout"),
            publicRoute(HttpMethod.POST, "/etc/member_insert"),
            publicRoute(HttpMethod.GET, "/css/authorization-matrix.css"),
            publicRoute(HttpMethod.GET, "/js/authorization-matrix.js"),
            publicRoute(HttpMethod.GET, "/images/authorization-matrix.png"),
            publicRoute(HttpMethod.GET, "/static/authorization-matrix.txt"),
            publicRoute(HttpMethod.GET, "/favicon.ico"),
            publicRoute(HttpMethod.GET, "/index.html"),
            publicRoute(HttpMethod.GET, "/v3/api-docs/authorization-matrix"),
            publicRoute(HttpMethod.GET, "/swagger-resources"),
            publicRoute(HttpMethod.GET, "/swagger-resources/authorization-matrix"),
            publicRoute(HttpMethod.GET, "/swagger-ui.html"),
            publicRoute(HttpMethod.GET, "/swagger-ui/index.html"),
            publicRoute(HttpMethod.GET, "/webjars/authorization-matrix.js"),
            publicRoute(HttpMethod.GET, "/actuator/health"),
            publicRoute(HttpMethod.GET, "/actuator/health/readiness"),
            publicRoute(HttpMethod.GET, "/actuator/health/liveness"),

            // AUTH_GET_WHITELIST 50-60행: 9개 패턴 전체. /schedule/month의 ADMIN 규칙이 우선한다.
            publicRoute(HttpMethod.GET, "/mainPage"),
            publicRoute(HttpMethod.GET, "/board"),
            publicRoute(HttpMethod.GET, "/board/BBSMSTR_AUTH_TEST/1"),
            publicRoute(HttpMethod.GET, "/boardFileAtch/BBSMSTR_AUTH_TEST"),
            publicRoute(HttpMethod.GET, "/schedule/daily"),
            publicRoute(HttpMethod.GET, "/schedule/week"),
            publicRoute(HttpMethod.GET, "/schedule/SCHDUL_AUTH_TEST"),
            publicRoute(HttpMethod.GET, "/image"),
            publicRoute(HttpMethod.GET, "/file"),

            // anyRequest().authenticated(): 공개 GET의 POST 및 공개하지 않은 actuator 경로.
            authenticated(HttpMethod.POST, "/file"),
            authenticated(HttpMethod.POST, "/board"),
            authenticated(HttpMethod.GET, "/actuator/env"),
            authenticated(HttpMethod.GET, "/authorization-matrix/unmapped"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;

    static Stream<Arguments> authorizationMatrix() {
        return RULES.stream().flatMap(rule -> Stream.of(Caller.values())
                .map(caller -> Arguments.of(rule.method(), rule.path(), caller, rule.expectedFor(caller))));
    }

    @ParameterizedTest(name = "{0} {1} / {2} -> {3}")
    @MethodSource("authorizationMatrix")
    void appliesProductionAuthorizationRules(HttpMethod method, String path, Caller caller, Outcome expected)
            throws Exception {
        MockHttpServletRequestBuilder request = request(method, path);
        if (caller != Caller.ANONYMOUS) {
            LoginVO loginVO = new LoginVO();
            loginVO.setId("authorization-matrix");
            loginVO.setUniqId("AUTHORIZATION_MATRIX");
            loginVO.setGroupNm(caller.groupNm);
            request.header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginVO));
        }

        ResultActions result = mockMvc.perform(request);
        switch (expected) {
            // EntryPoint의 HTTP 상태는 401, 본문 AUTH_ERROR 코드는 403이다. 여기서는 HTTP만 검증한다.
            case UNAUTHORIZED -> result.andExpect(status().isUnauthorized());
            case FORBIDDEN -> result.andExpect(status().isForbidden());
            case PASSED -> result.andExpect(response -> {
                int httpStatus = response.getResponse().getStatus();
                assertNotEquals(401, httpStatus, "보안 필터가 인증된/공개 요청을 거절했다");
                assertNotEquals(403, httpStatus, "보안 필터가 허용된 역할/공개 요청을 거절했다");
            });
        }
    }

    private static Rule adminOnly(HttpMethod method, String path) {
        return new Rule(method, path, Outcome.UNAUTHORIZED, Outcome.FORBIDDEN, Outcome.PASSED);
    }

    private static Rule authenticated(HttpMethod method, String path) {
        return new Rule(method, path, Outcome.UNAUTHORIZED, Outcome.PASSED, Outcome.PASSED);
    }

    private static Rule publicRoute(HttpMethod method, String path) {
        return new Rule(method, path, Outcome.PASSED, Outcome.PASSED, Outcome.PASSED);
    }
}
