package egovframework.let.cop.bbs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageListResponseDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;

class EgovBBSManageApiControllerPaginationTest {

    private MockMvc mockMvc;
    private EgovBBSManageService bbsMngService;
    private EgovBBSAttributeManageService bbsAttrbService;
    private EgovPropertyService propertyService;

    @BeforeEach
    void setUp() {
        bbsMngService = mock(EgovBBSManageService.class);
        bbsAttrbService = mock(EgovBBSAttributeManageService.class);
        propertyService = mock(EgovPropertyService.class);
        when(propertyService.getInt("Globals.pageUnit")).thenReturn(10);
        when(propertyService.getInt("Globals.pageSize")).thenReturn(10);
        EgovBBSManageApiController controller = new EgovBBSManageApiController(
                mock(EgovFileMngUtil.class), mock(ResultVoHelper.class), bbsMngService,
                mock(EgovFileMngService.class), propertyService, bbsAttrbService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver()).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "2.5", "2junk", "214748365", "2147483647", "2147483648"})
    void invalidPageIsRejectedBeforeLookup(String pageIndex) throws Exception {
        mockMvc.perform(get("/board").param("pageIndex", pageIndex))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(900));
        verifyNoInteractions(bbsMngService, bbsAttrbService);
    }

    @ParameterizedTest
    @CsvSource({", 1, 0, 10", "1, 1, 0, 10", "3, 3, 20, 30",
            "214748364, 214748364, 2147483630, 2147483640"})
    void validPageKeepsPaginationAndSearch(String page, int expectedPage, int first, int last) throws Exception {
        when(bbsMngService.selectBoardArticles(any(), any(), eq("")))
                .thenReturn(BbsManageListResponseDTO.builder()
                        .resultList(Collections.emptyList()).resultCnt(0).build());
        MockHttpServletRequestBuilder request = get("/board")
                .param("bbsId", "board").param("searchCnd", "1").param("searchWrd", "공지");
        if (page != null) request.param("pageIndex", page);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.result.resultList").isEmpty());

        ArgumentCaptor<BbsSearchRequestDTO> search = ArgumentCaptor.forClass(BbsSearchRequestDTO.class);
        ArgumentCaptor<PaginationInfo> pagination = ArgumentCaptor.forClass(PaginationInfo.class);
        verify(bbsMngService).selectBoardArticles(search.capture(), pagination.capture(), eq(""));
        assertEquals(expectedPage, search.getValue().getPageIndex());
        assertEquals("board", search.getValue().getBbsId());
        assertEquals("1", search.getValue().getSearchCnd());
        assertEquals("공지", search.getValue().getSearchWrd());
        assertEquals(first, pagination.getValue().getFirstRecordIndex());
        assertEquals(last, pagination.getValue().getLastRecordIndex());
        assertEquals(10, pagination.getValue().getRecordCountPerPage());
    }

    @Test
    void overflowLimitUsesConfiguredPageUnit() throws Exception {
        when(propertyService.getInt("Globals.pageUnit")).thenReturn(20);
        mockMvc.perform(get("/board").param("pageIndex", "107374183"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(900));
        verifyNoInteractions(bbsMngService, bbsAttrbService);
    }
}
