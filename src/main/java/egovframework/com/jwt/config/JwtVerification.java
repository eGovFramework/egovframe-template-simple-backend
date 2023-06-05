package egovframework.com.jwt.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtVerification {
	
	@Autowired
	private EgovJwtTokenUtil jwtTokenUtil;
	
	public boolean isVerification(HttpServletRequest request) {
		
		boolean verificationFlag = true;
		
		// step 1. request header에서 토큰을 가져온다.
		String jwtToken = EgovStringUtil.isNullToString(request.getHeader("authorization"));
		
		
		// step 2. 토큰에 내용이 있는지 확인해서 username값을 가져옴
		// Exception 핸들링 추가처리 (토큰 유효성, 토큰 변조 여부, 토큰 만료여부)
		// 내부적으로 parse하는 과정에서 해당 여부들이 검증됨
		String username = null;
		
		try {
		    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		} catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException e) {
		    log.debug("Unable to verify JWT Token: " + e.getMessage());
		    verificationFlag = false;
		    return verificationFlag;
		}
		
		log.debug("===>>> username = " + username);
		
		// step 3. 가져온 username 유무 체크
		if (username == null) {
			log.debug("jwtToken not validate");
			verificationFlag =  false;
			return verificationFlag;
		}
		
		log.debug("jwtToken validated");
		
		return verificationFlag;
	}

}
