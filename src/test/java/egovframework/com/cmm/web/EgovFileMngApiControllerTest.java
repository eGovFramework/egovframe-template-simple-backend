package egovframework.com.cmm.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileDeleteRequest;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.cop.bbs.dto.request.BbsManageDetailBoardRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailItemResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailResponseDTO;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

@ExtendWith(MockitoExtension.class)
class EgovFileMngApiControllerTest {

    private static final String FILE_ID = "FILE_0001";
    private static final String OWNER_ID = "USER_0001";
    private static final byte[] ENCRYPTED_FILE_ID = "encrypted-file-id".getBytes(StandardCharsets.UTF_8);

    enum OwnerContext {
        BOARD, SCHEDULE
    }

    @Mock
    private EgovFileMngService fileService;

    @Mock
    private EgovCryptoService cryptoService;

    @Mock
    private EgovBBSManageService bbsMngService;

    @Mock
    private EgovIndvdlSchdulManageService scheduleService;

    private EgovFileMngApiController controller;

    @BeforeEach
    void setUp() {
        controller = new EgovFileMngApiController();
        ReflectionTestUtils.setField(controller, "fileService", fileService);
        ReflectionTestUtils.setField(controller, "cryptoService", cryptoService);
        ReflectionTestUtils.setField(controller, "bbsMngService", bbsMngService);
        ReflectionTestUtils.setField(controller, "egovIndvdlSchdulManageService", scheduleService);
        when(cryptoService.decrypt(eq(ENCRYPTED_FILE_ID), eq(EgovFileDownloadController.ALGORITM_KEY)))
                .thenReturn(FILE_ID.getBytes(StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("소유자와 관리자는 소유 정보가 일치하는 첨부파일을 삭제할 수 있다")
    @ParameterizedTest
    @CsvSource({
            "BOARD, ROLE_USER, USER_0001",
            "SCHEDULE, ROLE_USER, USER_0001",
            "BOARD, ROLE_ADMIN, ADMIN_0001",
            "SCHEDULE, ROLE_ADMIN, ADMIN_0001"
    })
    void deleteFileInfAllowsOwnerOrAdmin(OwnerContext context, String role, String userId) throws Exception {
        authenticate(userId, role);
        stubOwner(context, FILE_ID);

        ResultVO result = controller.deleteFileInf(requestWithContext(context));

        assertEquals(200, result.getResultCode());
        ArgumentCaptor<FileVO> fileCaptor = ArgumentCaptor.forClass(FileVO.class);
        verify(fileService).deleteFileInf(fileCaptor.capture());
        assertEquals(FILE_ID, fileCaptor.getValue().getAtchFileId());
        assertEquals("2", fileCaptor.getValue().getFileSn());
        verifyOwnerLookup(context);
    }

    @DisplayName("일반 사용자는 다른 사용자의 첨부파일을 삭제할 수 없다")
    @ParameterizedTest
    @EnumSource(OwnerContext.class)
    void deleteFileInfRejectsOtherUser(OwnerContext context) throws Exception {
        authenticate("OTHER_USER", "ROLE_USER");
        stubOwner(context, FILE_ID);

        assertDeletionDenied(controller.deleteFileInf(requestWithContext(context)));
    }

    @DisplayName("소유자와 관리자도 소유 엔티티의 첨부파일 ID가 다르면 삭제할 수 없다")
    @ParameterizedTest
    @CsvSource({
            "BOARD, ROLE_USER", "SCHEDULE, ROLE_USER",
            "BOARD, ROLE_ADMIN", "SCHEDULE, ROLE_ADMIN"
    })
    void deleteFileInfRejectsMismatchedAttachment(OwnerContext context, String role) throws Exception {
        authenticate(OWNER_ID, role);
        stubOwner(context, "OTHER_FILE");

        assertDeletionDenied(controller.deleteFileInf(requestWithContext(context)));
    }

    @DisplayName("소유 엔티티를 찾을 수 없으면 첨부파일을 삭제하지 않는다")
    @ParameterizedTest
    @EnumSource(OwnerContext.class)
    void deleteFileInfRejectsMissingEntity(OwnerContext context) throws Exception {
        authenticate(OWNER_ID, "ROLE_USER");
        // 조회 서비스의 기본 응답(null)은 존재하지 않는 게시글 또는 일정을 나타낸다.

        assertDeletionDenied(controller.deleteFileInf(requestWithContext(context)));
        verifyOwnerLookup(context);
    }

    @DisplayName("소유 컨텍스트가 없으면 사용자 권한과 관계없이 첨부파일을 삭제하지 않는다")
    @ParameterizedTest
    @ValueSource(strings = {"ROLE_USER", "ROLE_ADMIN"})
    void deleteFileInfRejectsMissingContext(String role) throws Exception {
        authenticate(OWNER_ID, role);

        assertDeletionDenied(controller.deleteFileInf(fileRequest()));
        verifyNoInteractions(bbsMngService, scheduleService);
    }

    private void authenticate(String userId, String role) {
        LoginVO user = new LoginVO();
        user.setUniqId(userId);
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority(role))));
        SecurityContextHolder.setContext(context);
    }

    private FileDeleteRequest fileRequest() {
        FileDeleteRequest request = new FileDeleteRequest();
        request.setAtchFileId(Base64.getEncoder().encodeToString(ENCRYPTED_FILE_ID));
        request.setFileSn("2");
        return request;
    }

    private FileDeleteRequest requestWithContext(OwnerContext context) {
        FileDeleteRequest request = fileRequest();
        if (context == OwnerContext.BOARD) {
            request.setBbsId("BBS_0001");
            request.setNttId("17");
        } else {
            request.setSchdulId("SCHDUL_0001");
        }
        return request;
    }

    private void stubOwner(OwnerContext context, String fileId) throws Exception {
        if (context == OwnerContext.BOARD) {
            var board = BbsManageDetailItemResponseDTO.builder()
                    .frstRegisterId(OWNER_ID).atchFileId(fileId).build();
            when(bbsMngService.selectBoardArticle(any(BbsManageDetailBoardRequestDTO.class)))
                    .thenReturn(BbsManageDetailResponseDTO.builder().boardVO(board).build());
        } else {
            IndvdlSchdulManageVO schedule = new IndvdlSchdulManageVO();
            schedule.setFrstRegisterId(OWNER_ID);
            schedule.setAtchFileId(fileId);
            when(scheduleService.selectIndvdlSchdulManageDetail(any(IndvdlSchdulManageVO.class)))
                    .thenReturn(schedule);
        }
    }

    private void assertDeletionDenied(ResultVO result) {
        assertEquals(403, result.getResultCode());
        verifyNoInteractions(fileService);
    }

    private void verifyOwnerLookup(OwnerContext context) throws Exception {
        if (context == OwnerContext.BOARD) {
            var captor = ArgumentCaptor.forClass(BbsManageDetailBoardRequestDTO.class);
            verify(bbsMngService).selectBoardArticle(captor.capture());
            assertEquals("BBS_0001", captor.getValue().getBbsId());
            assertEquals(17L, captor.getValue().getNttId());
            assertFalse(captor.getValue().isPlusCount());
            verifyNoInteractions(scheduleService);
        } else {
            var captor = ArgumentCaptor.forClass(IndvdlSchdulManageVO.class);
            verify(scheduleService).selectIndvdlSchdulManageDetail(captor.capture());
            assertEquals("SCHDUL_0001", captor.getValue().getSchdulId());
            verifyNoInteractions(bbsMngService);
        }
    }
}
