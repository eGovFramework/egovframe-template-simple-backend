package egovframework.com.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.util.unit.DataSize;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.MultipartFilter;

import egovframework.com.cmm.filter.HTMLTagFilter;
import egovframework.com.jwt.JwtAuthenticationEntryPoint;
import egovframework.com.jwt.JwtAuthenticationFilter;
import jakarta.servlet.MultipartConfigElement;

/**
 * fileName : SecurityConfig
 * author : crlee
 * date : 2023/06/10
 * description :
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2023/06/10 crlee 최초 생성
 * 2026/05/13 보안취약점 대응
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Http Methpd : Get 인증예외 List
    private String[] AUTH_GET_WHITELIST = {
            "/mainPage", // 메인 화면 리스트 조회
            "/board", // 게시판 목록조회
            "/board/{bbsId}/{nttId}", // 게시물 상세조회
            "/boardFileAtch/{bbsId}", // 게시판 파일 첨부가능 여부 조회
            "/schedule/daily", // 일별 일정 조회
            "/schedule/week", // 주간 일정 조회
            "/schedule/{schdulId}", // 일정 상세조회
            "/image", // 갤러리 이미지보기
            "/file", // 파일 다운로드 — GET만 공개 (2026/06/26 보안취약점 대응: POST(삭제)는 인증 요구로 전환)
    };

    // 인증 예외 List
    private String[] AUTH_WHITELIST = {
            "/",
            "/error", // 에러 페이지
            "/login/**",
            "/auth/login-jwt", // JWT 로그인
            "/auth/oauth-state", // OAuth state 발급 (SNS 로그인 CSRF 방어용 double-submit 쿠키)
            "/auth/logout", // 로그아웃
            "/auth/me", // 현재 사용자 조회 — 익명 호출 시 컨트롤러가 401 응답을 직접 반환 (라우트 가드/메뉴 분기용)
            "/etc/**", // 사용자단의 회원약관,회원가입,사용자아이디 중복여부체크 URL허용

            /* 정적 리소스 */
            "/css/**",
            "/js/**",
            "/images/**",
            "/static/**",
            "/favicon.ico",
            "/index.html",

            /* swagger */
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**", // Swagger UI 정적 리소스

            /* actuator 헬스 프로브 — k8s readiness/liveness 및 Docker HEALTHCHECK 전용 */
            "/actuator/health",
            "/actuator/health/readiness",
            "/actuator/health/liveness",

    };
    // application.properties의 Globals.Allow.Origin 값을 사용하며,
    // 환경별로 콤마로 구분된 복수 Origin 지정 가능 (예: "https://a.com,https://b.com")
    @Value("${Globals.Allow.Origin:http://localhost:3000}")
    private String allowedOrigins;

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 명시적 Origin 목록만 허용 — setAllowedOriginPatterns("*") + credentials 동시 사용 금지
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public HTMLTagFilter htmlTagFilter() {
        return new HTMLTagFilter();
    }

    // 멀티파트 필터 빈
    @Bean
    public MultipartFilter multipartFilter() {
        return new MultipartFilter();
    }

    // 서블릿 컨테이너에 멀티파트 구성을 제공하기 위한 설정
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(DataSize.ofMegabytes(100L));
        factory.setMaxFileSize(DataSize.ofMegabytes(100L));
        return factory.createMultipartConfig();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 페이지는 ADMIN만 접근
                        .requestMatchers("/members/**").hasRole("ADMIN") // 회원 관리는 ADMIN만 접근
                        .requestMatchers("/mypage/**").hasAnyRole("ADMIN", "USER") // 마이페이지는 ADMIN, USER 모두 접근
                        .requestMatchers("/inform/**").hasAnyRole("ADMIN", "USER") // 게시판은 ADMIN, USER 모두 접근
                        // 2026/06/26 보안취약점 대응 — 관리자 전용 사이트관리 API 서버측 역할검사 강제(프론트 라우트가드 의존 제거)
                        // 일정: 공개 조회(GET /schedule/daily,/week,/{id})는 AUTH_GET_WHITELIST 로 유지, 쓰기 및 admin 전용 GET(month)만 ADMIN
                        .requestMatchers(HttpMethod.GET,    "/schedule/month").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/schedule").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/schedule/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/schedule/**").hasRole("ADMIN")
                        // 게시판 속성(bbsMaster)·사용설정(bbsUseInf)은 공개 읽기가 없는 관리자 전용 → 경로 전체 ADMIN
                        .requestMatchers("/bbsMaster/**").hasRole("ADMIN")
                        .requestMatchers("/bbsUseInf/**").hasRole("ADMIN")
                        .requestMatchers("/notUsedBbsMaster").hasRole("ADMIN")
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, AUTH_GET_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(new NullSecurityContextRepository()))
                .requestCache(requestCache -> requestCache.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(characterEncodingFilter(), DisableEncodeUrlFilter.class)
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(multipartFilter(), CsrfFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .build();
    }

}