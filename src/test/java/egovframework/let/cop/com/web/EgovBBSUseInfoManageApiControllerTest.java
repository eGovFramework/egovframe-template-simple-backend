package egovframework.let.cop.com.web;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.com.service.BoardUseInf;
import egovframework.let.cop.com.service.EgovBBSUseInfoManageService;

@ExtendWith(MockitoExtension.class)
class EgovBBSUseInfoManageApiControllerTest {

    @Mock
    private EgovBBSUseInfoManageService bbsUseService;

    @Mock
    private EgovPropertyService propertyService;

    @Mock
    private EgovBBSAttributeManageService bbsAttrbService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        EgovBBSUseInfoManageApiController controller = new EgovBBSUseInfoManageApiController(
                bbsUseService, propertyService, bbsAttrbService, new ResultVoHelper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();

        LoginVO loginVO = new LoginVO();
        loginVO.setUniqId("CURRENT_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                loginVO, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @ParameterizedTest(name = "[{index}] 요청 수정자={0}, 사용 여부={1}")
    @MethodSource("updateRequests")
    void updateUsesAuthenticatedEditor(String requestedEditor, String useAt) throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("bbsId", "BBS_FROM_BODY");
        request.put("trgetId", "SYSTEM_DEFAULT_BOARD");
        request.put("useAt", useAt);
        if (requestedEditor != null) {
            request.put("lastUpdusrId", requestedEditor);
        }

        mockMvc.perform(put("/bbsUseInf/{bbsId}", "BBS_FROM_PATH")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200));

        ArgumentCaptor<BoardUseInf> captor = ArgumentCaptor.forClass(BoardUseInf.class);
        verify(bbsUseService).updateBBSUseInf(captor.capture());
        BoardUseInf updated = captor.getValue();
        assertAll(
                () -> assertEquals("CURRENT_ADMIN", updated.getLastUpdusrId()),
                () -> assertEquals("BBS_FROM_PATH", updated.getBbsId()),
                () -> assertEquals("SYSTEM_DEFAULT_BOARD", updated.getTrgetId()),
                () -> assertEquals(useAt, updated.getUseAt()));
    }

    private static Stream<Arguments> updateRequests() {
        return Stream.of(null, "", "PREVIOUS_ADMIN")
                .flatMap(editor -> Stream.of("Y", "N").map(useAt -> Arguments.of(editor, useAt)));
    }
}
