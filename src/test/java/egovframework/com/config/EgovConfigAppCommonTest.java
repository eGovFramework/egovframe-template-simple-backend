package egovframework.com.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;

import egovframework.com.cmm.web.EgovMultipartResolver;

class EgovConfigAppCommonTest {

    @Test
    @DisplayName("Multipart Resolver 빈 생성 시 전달받은 확장자 화이트리스트를 사용한다.")
    void localMultiCommonsMultipartResolverUsesConfiguredWhitelist() {
        EgovConfigAppCommon configuration = new EgovConfigAppCommon();
        EgovMultipartResolver resolver = configuration.localMultiCommonsMultipartResolver(".webp");

        assertThat(resolver.resolveMultipart(multipartRequest("banner.webp")).getFile("file")).isNotNull();
        assertThatThrownBy(() -> resolver.resolveMultipart(multipartRequest("banner.png")))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("File extension not allowed");
    }

    @Test
    @DisplayName("Multipart Resolver 화이트리스트는 기본값 없이 명시된 설정값을 사용한다.")
    void multipartResolverWhitelistRequiresExplicitConfiguration() throws NoSuchMethodException {
        assertThat(whitelistValueOf("localMultiCommonsMultipartResolver").value())
                .isEqualTo("${Globals.fileUpload.Extensions}");
        assertThat(whitelistValueOf("multipartResolver").value())
                .isEqualTo("${Globals.fileUpload.Extensions}");
    }

    private Value whitelistValueOf(String methodName) throws NoSuchMethodException {
        Method factoryMethod = EgovConfigAppCommon.class.getDeclaredMethod(methodName, String.class);
        return factoryMethod.getParameters()[0].getAnnotation(Value.class);
    }

    private MockHttpServletRequest multipartRequest(String filename) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/files");
        request.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        request.addPart(new MockPart("file", filename, "content".getBytes(StandardCharsets.UTF_8)));
        return request;
    }
}
