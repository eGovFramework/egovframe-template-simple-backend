package egovframework.let.uat.uia.dao;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.annotation.EgovDaoTest;
import egovframework.let.uat.uia.service.impl.EgovLoginDao;
import egovframework.let.utl.sim.service.EgovFileScrty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

/**
 * fileName       : EgovLoginDaoImplTest
 * author         : crlee
 * date           : 2023/05/10
 * description    : 로그인 DAO Test
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/10        crlee       최초 생성
 */
@SpringBootTest(classes = {
        EgovLoginDao.class
})
@EgovDaoTest
public class EgovLoginDaoTest {

    @Autowired
    EgovLoginDao loginDao;
    @Autowired
    DataSource dataSource;

    @Test
    @DisplayName("dataSource가 정상적으로 get 했니?")
    void checkTestDataSource(){
        Assertions.assertThat( dataSource ).isNotNull();
    }

    @Test
    void actionLoginTest() throws Exception {
        LoginVO loginVO = LoginVO.builder()
                .id("admin")
                .password( EgovFileScrty.encryptPassword("1", "admin" ) )
                .userSe("USR")
                .build();
        LoginVO findLogin = loginDao.actionLogin(loginVO);
        Assertions.assertThat( findLogin.getId() ).isEqualTo("admin");
        Assertions.assertThat( findLogin.getName() ).isEqualTo("관리자");
    }

}
