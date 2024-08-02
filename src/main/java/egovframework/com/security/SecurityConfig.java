package egovframework.com.security;

import egovframework.com.jwt.JwtAuthenticationEntryPoint;
import egovframework.com.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * fileName       : SecurityConfig
 * author         : crlee
 * date           : 2023/06/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/10        crlee       최초 생성
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	 //Http Methpd : Get 인증예외 List
    private String[] AUTH_GET_WHITELIST = {
    		"/mainPage", //메인 화면 리스트 조회
    		"/board", // 게시판 목록조회
    		"/board/{bbsId}/{nttId}", // 게시물 상세조회
    		"/boardFileAtch/{bbsId}", //게시판 파일 첨부가능 여부 조회
            "/schedule/daily", //일별 일정 조회
            "/schedule/week", //주간 일정 조회
            "/schedule/{schdulId}", //일정 상세조회
            "/image", //갤러리 이미지보기
    };

    // 인증 예외 List
    private String[] AUTH_WHITELIST = {
    		"/",
            "/login/**",
            "/auth/login-jwt",//JWT 로그인
            "/auth/login",//일반 로그인
            "/file", //파일 다운로드
            "/etc/**",//사용자단의 회원약관,회원가입,사용자아이디 중복여부체크 URL허용
            /* swagger*/
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            
    };
    private static final String[] ORIGINS_WHITELIST = {
            "http://localhost:3000",
    };

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }


    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT","PATCH"));
        configuration.setAllowedOrigins(Arrays.asList(ORIGINS_WHITELIST));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                		.antMatchers("/members/**").hasRole("ADMIN") //ROLE_생략=자동으로 입력됨
                        .antMatchers(AUTH_WHITELIST).permitAll()
                        .antMatchers(HttpMethod.GET,AUTH_GET_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement((sessionManagement) ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors().and()
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
                .build();
    }

}