package egovframework.com.security;

import egovframework.com.cmm.annotation.EgovSecurity;
import egovframework.com.jwt.JwtAuthenticationEntryPoint;
import egovframework.com.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // 인증 예외 List
    private String[] AUTH_WHITELIST = {
            "/",
            "/login/**",
            "/uat/uia/**.do", // 로그인
            "/cmm/main/**.do", // 메인페이지
            "/cop/smt/sim/egovIndvdlSchdulManageDailyListAPI.do", // 일별 일정 조회
            "/cop/smt/sim/egovIndvdlSchdulManageWeekListAPI.do", //주별 일정 조회
            "/cop/bbs/selectBoardArticleAPI.do", //게시판 상세조회
            "/cop/bbs/selectBoardListAPI.do" //게시판 조회
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
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
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
                        .antMatchers(AUTH_WHITELIST).permitAll()
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