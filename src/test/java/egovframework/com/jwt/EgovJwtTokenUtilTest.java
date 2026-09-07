package egovframework.com.jwt;

import egovframework.com.cmm.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EgovJwtTokenUtilTest {

    private final EgovJwtTokenUtil jwtTokenUtil = new EgovJwtTokenUtil();
    private String secretKeyString;

    // secretKeyString 은 @Value("${Globals.jwt.secret}") 로 주입되나, 본 단위 테스트는
    // 스프링 컨텍스트 없이 인스턴스를 직접 생성한다. 하드코딩 시크릿을 남기지 않도록
    // 매 실행 시 SecureRandom 으로 32바이트 이상 임시 서명키를 생성해 주입한다.
    @BeforeEach
    void setUp() {
        byte[] randomKey = new byte[48];
        new SecureRandom().nextBytes(randomKey);
        secretKeyString = Base64.getEncoder().encodeToString(randomKey);
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKeyString", secretKeyString);
    }

    @DisplayName("올바른 토큰을 입력했을 때, LoginVO 객체를 반환한다.")
    @Test
    void testValidTokenReturnsLoginVO() {
        // given
        LoginVO loginVO = new LoginVO();
        loginVO.setId("testUser");
        loginVO.setName("Test User");
        loginVO.setUserSe("USER");
        loginVO.setOrgnztId("testOrg");
        loginVO.setUniqId("testUniqId");
        loginVO.setGroupNm("ROLE_USER");

        String token = jwtTokenUtil.generateToken(loginVO);

        // when
        LoginVO result = jwtTokenUtil.getLoginVOFromToken(token);

        // then
        assertNotNull(result);
        assertEquals("testUser", result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("USER", result.getUserSe());
        assertEquals("testOrg", result.getOrgnztId());
        assertEquals("testUniqId", result.getUniqId());
        assertEquals("ROLE_USER", result.getGroupNm());
    }

    @DisplayName("토큰에서 LoginVO를 생성할 때, JWT를 한 번만 파싱한다.")
    @Test
    void testGetLoginVOFromTokenParsesTokenOnlyOnce() {
        // given
        CountingJwtTokenUtil countingJwtTokenUtil = new CountingJwtTokenUtil();
        ReflectionTestUtils.setField(countingJwtTokenUtil, "secretKeyString",
                ReflectionTestUtils.getField(jwtTokenUtil, "secretKeyString"));

        LoginVO loginVO = new LoginVO();
        loginVO.setId("testUser");
        loginVO.setName("Test User");
        loginVO.setUserSe("USER");
        loginVO.setOrgnztId("testOrg");
        loginVO.setUniqId("testUniqId");
        loginVO.setGroupNm("ROLE_USER");

        String token = countingJwtTokenUtil.generateToken(loginVO);

        // when
        LoginVO result = countingJwtTokenUtil.getLoginVOFromToken(token);

        // then
        assertEquals("testUser", result.getId());
        assertEquals(1, countingJwtTokenUtil.getParseCount());
    }

    @DisplayName("잘못된 토큰을 입력했을 때, InvalidJwtException 예외가 발생한다.")
    @Test
    void testInvalidTokenReturnsThrowException() {
        // given
        String token = "invalidToken";

        // when
        // then
        assertThrows(InvalidJwtException.class, () -> {
            jwtTokenUtil.getLoginVOFromToken(token);
        });
    }

    @DisplayName("서명은 유효하지만 만료된 토큰은 InvalidJwtException 예외가 발생한다.")
    @Test
    void testExpiredTokenThrowsInvalidJwtException() {
        // given
        SecretKey signingKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claim("id", "testUser")
                .expiration(Date.from(Instant.now().minusSeconds(3600)))
                .signWith(signingKey)
                .compact();

        // 서명이나 ID 누락이 아닌 만료 때문에 거부되는 토큰인지 확인한다.
        ExpiredJwtException expired = assertThrows(ExpiredJwtException.class,
                () -> jwtTokenUtil.getAllClaimsFromToken(token));
        assertEquals("testUser", expired.getClaims().get("id", String.class));

        // when / then
        assertThrows(InvalidJwtException.class, () -> jwtTokenUtil.getLoginVOFromToken(token));
    }

    @DisplayName("다른 키로 서명된 토큰은 InvalidJwtException 예외가 발생한다.")
    @Test
    void testTokenSignedWithDifferentKeyThrowsInvalidJwtException() {
        // given
        byte[] otherKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        otherKeyBytes[0] ^= 1;
        SecretKey otherSigningKey = Keys.hmacShaKeyFor(otherKeyBytes);
        String token = Jwts.builder()
                .claim("id", "testUser")
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(otherSigningKey)
                .compact();

        // 서명에 사용한 키로는 정상 검증되는 토큰인지 확인한다.
        Claims claims = Jwts.parser().verifyWith(otherSigningKey).build()
                .parseSignedClaims(token).getPayload();
        assertEquals("testUser", claims.get("id", String.class));

        // when / then
        assertThrows(InvalidJwtException.class, () -> jwtTokenUtil.getLoginVOFromToken(token));
    }

    @DisplayName("Id가 포함되지 않은 토큰을 입력했을 때, InvalidJwtException 예외가 발생한다.")
    @Test
    void testTokenWithoutIdReturnsThrowException() {
        // given
        LoginVO loginVO = new LoginVO();
        String token = jwtTokenUtil.generateToken(loginVO);

        // when
        // then
        assertThrows(InvalidJwtException.class, () -> {
            jwtTokenUtil.getLoginVOFromToken(token);
        });
    }

    private static class CountingJwtTokenUtil extends EgovJwtTokenUtil {
        private int parseCount;

        @Override
        public Claims getAllClaimsFromToken(String token) {
            parseCount++;
            return super.getAllClaimsFromToken(token);
        }

        int getParseCount() {
            return parseCount;
        }
    }
}
