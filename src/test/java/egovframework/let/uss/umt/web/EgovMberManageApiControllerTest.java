package egovframework.let.uss.umt.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.uss.umt.service.EgovMberManageService;

@ExtendWith(MockitoExtension.class)
class EgovMberManageApiControllerTest {

    @Mock
    private EgovMberManageService mberManageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        EgovMberManageApiController controller = new EgovMberManageApiController(
                mberManageService, mock(EgovCmmUseService.class), mock(EgovPropertyService.class),
                new ResultVoHelper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @DisplayName("아이디 중복 확인 시 요청한 문자열과 중복 결과를 유지한다")
    @ParameterizedTest(name = "ID={0}, 중복 개수={1}")
    @CsvSource({
            "reviewuser, 0", "reviewuser, 1",
            "홍길동, 0", "홍길동, 1",
            "café, 0", "café, 1"
    })
    void checkIdDplctPreservesRequestedId(String checkId, int usedCnt) throws Exception {
        when(mberManageService.checkIdDplct(anyString())).thenReturn(usedCnt);

        mockMvc.perform(get("/etc/member_checkid/{checkid}", checkId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.result.checkId").value(checkId))
                .andExpect(jsonPath("$.result.usedCnt").value(usedCnt));

        verify(mberManageService).checkIdDplct(checkId);
    }
}
