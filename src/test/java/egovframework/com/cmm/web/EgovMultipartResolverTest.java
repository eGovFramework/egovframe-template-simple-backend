package egovframework.com.cmm.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.web.multipart.MultipartHttpServletRequest;

class EgovMultipartResolverTest {

    @Test
    @DisplayName("주입된 화이트리스트에 포함된 확장자는 멀티파트 요청으로 해석한다.")
    void resolveMultipartAllowsExtensionInInjectedWhitelist() {
        EgovMultipartResolver resolver = new EgovMultipartResolver(".png");

        MultipartHttpServletRequest result = resolver.resolveMultipart(multipartRequest("profile.png"));

        assertThat(result.getFile("file")).isNotNull();
    }

    @Test
    @DisplayName("주입된 화이트리스트에 없는 확장자는 거부한다.")
    void resolveMultipartRejectsExtensionOutsideInjectedWhitelist() {
        EgovMultipartResolver resolver = new EgovMultipartResolver(".png");

        assertThatThrownBy(() -> resolver.resolveMultipart(multipartRequest("payload.exe")))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("File extension not allowed");
    }

    private MockHttpServletRequest multipartRequest(String filename) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/files");
        request.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        request.addPart(new MockPart("file", filename, "content".getBytes(StandardCharsets.UTF_8)));
        return request;
    }
}
