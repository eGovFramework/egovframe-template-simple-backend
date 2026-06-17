package egovframework.com.jwt;

import egovframework.com.cmm.LoginVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

class EgovJwtTokenUtilTest {

    private final EgovJwtTokenUtil jwtTokenUtil = new EgovJwtTokenUtil();

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
