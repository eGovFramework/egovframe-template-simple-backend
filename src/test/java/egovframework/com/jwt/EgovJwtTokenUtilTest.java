package egovframework.com.jwt;

import egovframework.com.cmm.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EgovJwtTokenUtilTest {

    private final EgovJwtTokenUtil jwtTokenUtil = new EgovJwtTokenUtil();

    // secretKeyString 은 @Value("${Globals.jwt.secret}") 로 주입되나, 본 단위 테스트는
    // 스프링 컨텍스트 없이 인스턴스를 직접 생성한다. 하드코딩 시크릿을 남기지 않도록
    // 매 실행 시 SecureRandom 으로 32바이트 이상 임시 서명키를 생성해 주입한다.
    @BeforeEach
    void setUp() {
        byte[] randomKey = new byte[48];
        new SecureRandom().nextBytes(randomKey);
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKeyString",
                Base64.getEncoder().encodeToString(randomKey));
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
}
