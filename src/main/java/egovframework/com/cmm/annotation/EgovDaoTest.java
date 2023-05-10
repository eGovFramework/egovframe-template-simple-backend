package egovframework.com.cmm.annotation;

import egovframework.com.config.EgovConfigAppDatasource;
import egovframework.com.config.EgovConfigAppMapper;
import egovframework.com.config.EgovConfigAppTransaction;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * fileName       : EgovDaoTest
 * author         : crlee
 * date           : 2023/05/10
 * description    : Dao 테스트 어노테이션
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/10        crlee       최초 생성
 */
@Target({ElementType.TYPE})
@Retention( RetentionPolicy.RUNTIME )
@SpringJUnitConfig(classes = {
        EgovConfigAppDatasource.class,
        EgovConfigAppMapper.class,
        EgovConfigAppTransaction.class
})
@Transactional
public @interface EgovDaoTest {
}
