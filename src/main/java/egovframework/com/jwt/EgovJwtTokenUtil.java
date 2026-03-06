package egovframework.com.jwt;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

//security 관련 제외한 jwt util 클래스
@Slf4j
@Component
public class EgovJwtTokenUtil implements Serializable{

	private static final long serialVersionUID = -5180902194184255251L;

	// JWT 토큰 유효시간 (초 단위)
	// public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60; // 24시간 (86400초)
	public static final long JWT_TOKEN_VALIDITY = 60 * 60; // 1시간 (3600초)
	
	public static final String SECRET_KEY_STRING = EgovProperties.getProperty("Globals.jwt.secret");
	
	/**
	 * SecretKey를 생성합니다. (io.jsonwebtoken 0.12.6 버전 호환)
	 */
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
	}
  
	// retrieve username from jwt token
	public String getUserIdFromToken(String token) {
		return getInfoFromToken("id", token);
	}

	public String getUserSeFromToken(String token) {
		return getInfoFromToken("userSe", token);
	}

	public String getInfoFromToken(String type, String token) {
		Claims claims = getClaimFromToken(token);
	    Object info = claims.get(type);

	    return info != null ? info.toString() : null;
	}

	public Claims getClaimFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims;
	}

	//for retrieveing any information from token we will need the secret key
	public Claims getAllClaimsFromToken(String token) {
		log.debug("===>>> secret = "+SECRET_KEY_STRING);
		SecretKey key = getSecretKey();
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	//generate token for user
    public String generateToken(LoginVO loginVO) {
        return doGenerateToken(loginVO, "Authorization");
    }

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string
	private String doGenerateToken(LoginVO loginVO, String subject) {
		
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginVO.getId() );
        claims.put("name", loginVO.getName() );
        claims.put("userSe", loginVO.getUserSe() );
        claims.put("orgnztId", loginVO.getOrgnztId() );
        claims.put("uniqId", loginVO.getUniqId() );
        claims.put("type", subject);
        claims.put("groupNm", loginVO.getGroupNm());//권한그룹으로 시프링시큐리티 사용

    	log.debug("===>>> secret = "+SECRET_KEY_STRING);
    	SecretKey key = getSecretKey();
        return Jwts.builder()
        		.claims(claims)
        		.subject(subject)
        		.issuedAt(new Date(System.currentTimeMillis()))
        		.expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        		.signWith(key)
        		.compact();
    }

	public LoginVO getLoginVOFromToken(String token) throws InvalidJwtException{
		LoginVO loginVO = new LoginVO();

        try {
		    loginVO.setId(getUserIdFromToken(token));
			loginVO.setName(getInfoFromToken("name", token));
			loginVO.setUserSe(getUserSeFromToken(token));
			loginVO.setOrgnztId(getInfoFromToken("orgnztId", token));
			loginVO.setUniqId(getInfoFromToken("uniqId", token));
            loginVO.setGroupNm(getInfoFromToken("groupNm", token));

            if(loginVO.getId() == null) throw new InvalidJwtException("Missing id in token");
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException("Unable to verify JWT Token: " + e.getMessage());
        } catch (JwtException e) {
            throw new InvalidJwtException("Unable to verify JWT Token: " + e.getMessage());
        }

		return loginVO;
	}
}
