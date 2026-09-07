package egovframework.let.cop.bbs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.validation.BeanPropertyBindingResult;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.IntermediateResultVO;
import egovframework.com.config.EgovConfigAppProperties;
import egovframework.let.cop.bbs.dto.request.BbsAttributeInsertRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsAttributeUpdateRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;

class EgovBBSAttributeManageApiControllerAttachmentSizeTest {

    private EgovBBSAttributeManageService bbsAttrbService;
    private LoginVO loginVO;

    @BeforeEach
    void setUp() {
        bbsAttrbService = mock(EgovBBSAttributeManageService.class);
        loginVO = new LoginVO();
        loginVO.setUniqId("USRCNFRM_00000000001");
    }

    @DisplayName("게시판 등록 시 설정된 첨부파일 크기를 서비스에 전달한다")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "10485760")
    void insertUsesConfiguredAttachmentSize(String overrideSize) throws Exception {
        try (AnnotationConfigApplicationContext context = createPropertiesContext(overrideSize)) {
            EgovBBSAttributeManageApiController controller = createController(context);
            BbsAttributeInsertRequestDTO request = new BbsAttributeInsertRequestDTO();
            request.setBbsNm("테스트 게시판");
            request.setBbsIntrcn("첨부파일 크기 설정 검증");
            request.setBbsTyCode("BBST01");
            request.setBbsAttrbCode("BBSA02");

            IntermediateResultVO<?> result = controller.insertBBSMasterInf(request,
                    new BeanPropertyBindingResult(request, "request"), loginVO);

            ArgumentCaptor<BbsAttributeInsertRequestDTO> captor =
                    ArgumentCaptor.forClass(BbsAttributeInsertRequestDTO.class);
            verify(bbsAttrbService).insertBBSMastetInf(captor.capture());
            assertEquals(context.getEnvironment().getRequiredProperty("Globals.posblAtchFileSize"),
                    captor.getValue().getPosblAtchFileSize());
            assertEquals(200, result.getResultCode());
        }
    }

    @DisplayName("게시판 수정 시 설정된 첨부파일 크기를 서비스에 전달한다")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "10485760")
    void updateUsesConfiguredAttachmentSize(String overrideSize) throws Exception {
        try (AnnotationConfigApplicationContext context = createPropertiesContext(overrideSize)) {
            EgovBBSAttributeManageApiController controller = createController(context);
            BbsAttributeUpdateRequestDTO request = new BbsAttributeUpdateRequestDTO();
            request.setBbsId("BBSMSTR_000000000001");
            request.setBbsNm("테스트 게시판");
            request.setBbsIntrcn("첨부파일 크기 설정 검증");
            request.setBbsTyCode("BBST01");
            request.setBbsAttrbCode("BBSA02");

            IntermediateResultVO<?> result = controller.updateBBSMasterInf(request,
                    new BeanPropertyBindingResult(request, "request"), loginVO);

            ArgumentCaptor<BbsAttributeUpdateRequestDTO> captor =
                    ArgumentCaptor.forClass(BbsAttributeUpdateRequestDTO.class);
            verify(bbsAttrbService).updateBBSMasterInf(captor.capture());
            assertEquals(context.getEnvironment().getRequiredProperty("Globals.posblAtchFileSize"),
                    captor.getValue().getPosblAtchFileSize());
            assertEquals(200, result.getResultCode());
        }
    }

    private AnnotationConfigApplicationContext createPropertiesContext(String overrideSize) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        TestPropertySourceUtils.addPropertiesFilesToEnvironment(context, "classpath:application.properties");
        if (overrideSize != null) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
                    "Globals.posblAtchFileSize=" + overrideSize);
        }
        context.register(EgovConfigAppProperties.class);
        context.refresh();
        return context;
    }

    private EgovBBSAttributeManageApiController createController(AnnotationConfigApplicationContext context) {
        return new EgovBBSAttributeManageApiController(bbsAttrbService, mock(EgovCmmUseService.class),
                context.getBean(EgovPropertyService.class));
    }
}
