package egovframework.com.security.pass;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * fileName       : SecurityPassUtilsTest
 * author         : crlee
 * date           : 2023/08/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/04        crlee       최초 생성
 */
@SpringBootTest(classes = {
    TestController.class,
    Test2Controller.class,
    SecurityPassUtils.class
})
public class SecurityPassUtilsTest {

    @Autowired
    SecurityPassUtils securityPassUtils;

    @Test
    @DisplayName("모든 권한 테스트")
    public void getAllUrlsTest(){
        String[] getAllUrls = securityPassUtils.getUrls();
        String[] expectUrls = {"/test/post", "/test/put", "/test/delete","/test/get"};
        chkArr( getAllUrls , expectUrls );
    }
    @Test
    @DisplayName("ROLE1 권한 테스트")
    public void getRole1UrlsTest(){
        String[] getAllUrls = securityPassUtils.getUrls("ROLE1");
        String[] expectUrls = {"/test/get2", "/test/delete2" ,"/test2/post"};
        chkArr( getAllUrls , expectUrls );
    }
    @Test
    @DisplayName("ROLE2 권한 테스트")
    public void getRole2UrlsTest(){
        String[] getAllUrls = securityPassUtils.getUrls("ROLE2");
        String[] expectUrls = {"/test/get2"};
        chkArr( getAllUrls , expectUrls );
    }
    @Test
    @DisplayName("없는 권한 테스트")
    public void nullTest(){
        String[] getAllUrls = securityPassUtils.getUrls("ROLE33333");
        String[] expectUrls = {};
        chkArr( getAllUrls , expectUrls );
    }
    public void chkArr(String[] arr1,String[] arr2){
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        Assertions.assertArrayEquals( arr1 , arr2 );
    }
}
