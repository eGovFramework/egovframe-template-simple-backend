package egovframework.com.jwt;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import egovframework.com.cmm.LoginVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * fileName       : JwtAuthenticationFilter
 * author         : crlee
 * date           : 2023/06/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/11        crlee       최초 생성
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;
    public static final String HEADER_STRING = "Authorization";

    @Override //로그인 이후 HttpServletRequest 요청할 때마다 실행(스프링의 AOP기능)
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        boolean verificationFlag = true;

        // step 1. request header에서 토큰을 가져온다.
        String jwtToken = EgovStringUtil.isNullToString(req.getHeader(HEADER_STRING));


        // step 2. 토큰에 내용이 있는지 확인해서 id값을 가져옴
        // Exception 핸들링 추가처리 (토큰 유효성, 토큰 변조 여부, 토큰 만료여부)
        // 내부적으로 parse하는 과정에서 해당 여부들이 검증됨
        String id = null;

        try {

            id = jwtTokenUtil.getUserIdFromToken(jwtToken);
            if (id == null) {
                logger.debug("jwtToken not validate");
                verificationFlag =  false;
            }
            logger.debug("===>>> id = " + id);
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            logger.debug("Unable to verify JWT Token: " + e.getMessage());
            verificationFlag = false;
        }

        LoginVO loginVO = new LoginVO();
        if( verificationFlag ){
            logger.debug("jwtToken validated");
            loginVO.setId(id);
            loginVO.setUserSe( jwtTokenUtil.getUserSeFromToken(jwtToken) );
            loginVO.setUniqId( jwtTokenUtil.getInfoFromToken("uniqId",jwtToken) );
            loginVO.setOrgnztId( jwtTokenUtil.getInfoFromToken("orgnztId",jwtToken) );
            loginVO.setName( jwtTokenUtil.getInfoFromToken("name",jwtToken) );
            loginVO.setGroupNm(jwtTokenUtil.getInfoFromToken("groupNm", jwtToken));//토큰에서 가져온 스프링시큐리티용 그룹명값 부여
            logger.debug("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
            if(loginVO.getGroupNm().equals("ROLE_ADMIN")) { //스프링시큐리티 관리자 권한은 getGroupNm값으로 구분
            	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginVO, null,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            	logger.debug("authentication1 ===>>> " + authentication);
            	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
            	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginVO, null,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            	logger.debug("authentication2 ===>>> " + authentication);
            	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }


        chain.doFilter(req, res);

    }
}
