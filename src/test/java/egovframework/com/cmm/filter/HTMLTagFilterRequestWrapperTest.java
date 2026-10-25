package egovframework.com.cmm.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class HTMLTagFilterRequestWrapperTest {

    @Test
    void scalarAndArrayEscapeAllFiveCharacters() {
        for (String[] example : new String[][] {
                {"<", "&lt;"}, {">", "&gt;"}, {"&", "&amp;"},
                {"\"", "&quot;"}, {"'", "&apos;"},
                {"<a title=\"A&B\">'한글'</a>", "&lt;a title=&quot;A&amp;B&quot;&gt;&apos;한글&apos;&lt;/a&gt;"},
                {"&lt;&#38;", "&amp;lt;&amp;#38;"}, {"", ""}, {"plain 한글", "plain 한글"}}) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter("value", example[0]);
            HTMLTagFilterRequestWrapper wrapper = new HTMLTagFilterRequestWrapper(request);
            assertThat(wrapper.getParameter("value")).isEqualTo(example[1]);
            assertThat(wrapper.getParameterValues("value")).containsExactly(example[1]);
        }
    }

    @Test
    void repeatedReadsPreserveOrderAndUnderlyingRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("value", "A&B", "<second>", "");
        HTMLTagFilterRequestWrapper wrapper = new HTMLTagFilterRequestWrapper(request);
        assertThat(wrapper.getParameterValues("value")).containsExactly("A&amp;B", "&lt;second&gt;", "");
        assertThat(wrapper.getParameter("value")).isEqualTo("A&amp;B");
        assertThat(wrapper.getParameterValues("value")).containsExactly("A&amp;B", "&lt;second&gt;", "");
        assertThat(request.getParameterValues("value")).containsExactly("A&B", "<second>", "");
    }

    @Test
    void callerCannotChangeSubsequentReadsThroughReturnedArray() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("value", "A&B");
        HTMLTagFilterRequestWrapper wrapper = new HTMLTagFilterRequestWrapper(request);
        wrapper.getParameterValues("value")[0] = "changed";
        assertThat(wrapper.getParameter("value")).isEqualTo("A&amp;B");
        assertThat(wrapper.getParameterValues("value")).containsExactly("A&amp;B");
    }

    @Test
    void missingEmptyAndNullValuesRetainTheirShape() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HTMLTagFilterRequestWrapper wrapper = new HTMLTagFilterRequestWrapper(request);
        assertThat(wrapper.getParameter("missing")).isNull();
        assertThat(wrapper.getParameterValues("missing")).isNull();
        request.setParameter("empty", new String[0]);
        assertThat(wrapper.getParameterValues("empty")).isEmpty();
        request.setParameter("nullable", new String[] {null, "", "&"});
        assertThat(wrapper.getParameter("nullable")).isNull();
        assertThat(wrapper.getParameterValues("nullable")).containsExactly(null, "", "&amp;");
    }

    @Test
    void parameterMapStillDelegatesToTheUnderlyingRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("value", "A&B", "<second>");
        HTMLTagFilterRequestWrapper wrapper = new HTMLTagFilterRequestWrapper(request);
        wrapper.getParameterValues("value");
        assertThat(wrapper.getParameterMap().get("value"))
                .isSameAs(request.getParameterMap().get("value"))
                .containsExactly("A&B", "<second>");
    }

    @Test
    void httpFormReadsRemainStableThroughEmbeddedTomcat(@TempDir Path baseDirectory) throws Exception {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(0);
        factory.setAddress(java.net.InetAddress.getLoopbackAddress());
        factory.setBaseDirectory(baseDirectory.toFile());
        WebServer server = factory.getWebServer(context -> {
            context.addFilter("htmlTagFilter", new HTMLTagFilter())
                    .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
            context.addServlet("form", new HttpServlet() {
                @Override
                protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
                    request.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setContentType("text/plain;charset=UTF-8");
                    String first = request.getParameter("value");
                    String[] values = request.getParameterValues("value");
                    values[0] = "caller mutation";
                    response.getWriter().print(first + "\n"
                            + String.join("|", request.getParameterValues("value")) + "\n"
                            + request.getParameter("value") + "\n"
                            + request.getParameter("missing"));
                }
            }).addMapping("/form");
        });
        try {
            server.start();
            String raw = "<>&\"' 한글😀 &lt;";
            String escaped = "&lt;&gt;&amp;&quot;&apos; 한글😀 &amp;lt;";
            String form = "value=" + URLEncoder.encode(raw, StandardCharsets.UTF_8)
                    + "&value=" + URLEncoder.encode("A&B", StandardCharsets.UTF_8) + "&value=";
            HttpRequest request = HttpRequest.newBuilder(
                    new URI("http", null, java.net.InetAddress.getLoopbackAddress().getHostAddress(),
                            server.getPort(), "/form", null, null))
                    .timeout(java.time.Duration.ofSeconds(10))
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(form, StandardCharsets.UTF_8)).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.body()).isEqualTo(escaped + "\n" + escaped + "|A&amp;B|\n" + escaped + "\nnull");
        } finally {
            server.stop();
        }
    }

    @Test
    void filterDeliversEscapedValuesToTheServletChain() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/form");
        request.addParameter("value", "A&B", "<second>");
        new HTMLTagFilter().doFilter(request, new MockHttpServletResponse(), (filtered, response) -> {
            HttpServletRequest downstream = (HttpServletRequest) filtered;
            assertThat(downstream.getParameter("value")).isEqualTo("A&amp;B");
            assertThat(downstream.getParameterValues("value")).containsExactly("A&amp;B", "&lt;second&gt;");
            assertThat(downstream.getParameter("value")).isEqualTo("A&amp;B");
        });
    }
}
