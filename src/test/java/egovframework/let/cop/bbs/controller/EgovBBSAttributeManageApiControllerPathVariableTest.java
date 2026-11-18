package egovframework.let.cop.bbs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.validation.BeanPropertyBindingResult;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.config.EgovConfigAppProperties;
import egovframework.let.cop.bbs.dto.request.BbsAttributeUpdateRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;

/**
 * PUT /bbsMaster/{bbsId} 가 경로변수를 수정 대상으로 쓰는지 검증한다.
 *
 * <p>같은 컨트롤러의 조회·삭제는 경로변수를 읽는데 이 핸들러만 매핑에 {bbsId} 를 선언해 놓고
 * 본문 DTO 의 값으로 갱신했다. URL 과 본문이 다른 게시판을 가리키면 본문 쪽이 수정된다.
 */
class EgovBBSAttributeManageApiControllerPathVariableTest {

    private static final String PATH_BBS_ID = "BBSMSTR_000000000001";
    private static final String BODY_BBS_ID = "BBSMSTR_999999999999";

    private EgovBBSAttributeManageService bbsAttrbService;
    private LoginVO loginVO;

    @BeforeEach
    void setUp() {
        bbsAttrbService = mock(EgovBBSAttributeManageService.class);
        loginVO = new LoginVO();
        loginVO.setUniqId("USRCNFRM_00000000001");
    }

    @Test
    @DisplayName("URL 과 본문이 다른 게시판을 가리키면 URL 쪽이 수정 대상이 된다")
    void updateUsesPathVariableAsTarget() throws Exception {
        try (AnnotationConfigApplicationContext context = createPropertiesContext()) {
            EgovBBSAttributeManageApiController controller = createController(context);

            BbsAttributeUpdateRequestDTO request = new BbsAttributeUpdateRequestDTO();
            request.setBbsId(BODY_BBS_ID);
            request.setBbsNm("테스트 게시판");
            request.setBbsIntrcn("경로변수 바인딩 검증");
            request.setBbsTyCode("BBST01");
            request.setBbsAttrbCode("BBSA02");

            controller.updateBBSMasterInf(PATH_BBS_ID, request,
                    new BeanPropertyBindingResult(request, "request"), loginVO);

            ArgumentCaptor<BbsAttributeUpdateRequestDTO> captor =
                    ArgumentCaptor.forClass(BbsAttributeUpdateRequestDTO.class);
            verify(bbsAttrbService).updateBBSMasterInf(captor.capture());

            assertEquals(PATH_BBS_ID, captor.getValue().getBbsId());
        }
    }

    private AnnotationConfigApplicationContext createPropertiesContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        TestPropertySourceUtils.addPropertiesFilesToEnvironment(context, "classpath:application.properties");
        context.register(EgovConfigAppProperties.class);
        context.refresh();
        return context;
    }

    private EgovBBSAttributeManageApiController createController(AnnotationConfigApplicationContext context) {
        return new EgovBBSAttributeManageApiController(bbsAttrbService, mock(EgovCmmUseService.class),
                context.getBean(EgovPropertyService.class));
    }
}
