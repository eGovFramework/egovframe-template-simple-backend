package egovframework.com;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.config.EgovConfigAppMsg;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


/**
 * fileName       : EgovMessageSourceTest
 * author         : crlee
 * date           : 2023/05/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/05        crlee       최초 생성
 */
@SpringBootTest(classes = {EgovConfigAppMsg.class})
public class EgovMessageSourceTest {

    @Autowired
    EgovMessageSource egovMessageSource;

    @TestFactory
    Stream<DynamicTest> messageTests() {
        List<Locale> locales = Arrays.asList(
                Locale.KOREA,
                Locale.US
        );

        return locales.stream().map(locale -> DynamicTest.dynamicTest(
                "Message test with " + locale.getDisplayName(),
                () -> {
                    String msgType = "fail.common.login";
                    String expectedMessage = getMessageForLocale(locale);

                    Assertions.assertThat(
                            egovMessageSource.getMessage(msgType, locale)
                    ).isEqualTo(expectedMessage);
                }
        ));
    }

    private String getMessageForLocale(Locale locale) {
        if (locale == Locale.KOREA) {
            return "로그인 정보가 올바르지 않습니다.";
        } else if (locale == Locale.US) {
            return "login information is not correct";
        } else {
            return "";
        }
    }

    @Test
    void enMessageTest(){
        String msgType = "fail.common.login";
        String expectedMessage = "login information is not correct";
        Assertions.assertThat( egovMessageSource.getMessage(msgType, Locale.US) ).isEqualTo(expectedMessage);
        Assertions.assertThat( egovMessageSource.getMessage(msgType, Locale.UK) ).isEqualTo(expectedMessage);
        Assertions.assertThat( egovMessageSource.getMessage(msgType, Locale.ENGLISH) ).isEqualTo(expectedMessage);
    }
}
