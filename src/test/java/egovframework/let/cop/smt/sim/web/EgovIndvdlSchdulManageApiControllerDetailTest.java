package egovframework.let.cop.smt.sim.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.com.cmm.web.EgovFileDownloadController;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

@ExtendWith(MockitoExtension.class)
class EgovIndvdlSchdulManageApiControllerDetailTest {

	private static final String SCHEDULE_ID = "SCHDUL_0000000000001";

	@Mock
	private EgovIndvdlSchdulManageService scheduleService;
	@Mock
	private EgovCmmUseService cmmUseService;
	@Mock
	private EgovFileMngService fileMngService;
	@Mock
	private EgovFileMngUtil fileUtil;
	@Mock
	private EgovCryptoService cryptoService;

	private EgovIndvdlSchdulManageApiController controller;

	@BeforeEach
	void setUp() {
		controller = new EgovIndvdlSchdulManageApiController(scheduleService, cmmUseService,
				fileMngService, fileUtil, cryptoService, new ResultVoHelper());
	}

	@Test
	void missingScheduleReturnsNotFoundWithoutLoadingAttachmentsOrCodes() throws Exception {
		ResultVO result = controller.EgovIndvdlSchdulManageDetail(SCHEDULE_ID, new LoginVO());

		assertNotFound(result);
		ArgumentCaptor<IndvdlSchdulManageVO> query = ArgumentCaptor.forClass(IndvdlSchdulManageVO.class);
		verify(scheduleService).selectIndvdlSchdulManageDetail(query.capture());
		assertEquals(SCHEDULE_ID, query.getValue().getSchdulId());
		verifyNoInteractions(fileMngService, fileUtil, cryptoService, cmmUseService);
	}

	@Test
	void scheduleNoLongerReturnedAfterDeletionGetsNotFound() throws Exception {
		IndvdlSchdulManageVO detail = newSchedule("");
		when(scheduleService.selectIndvdlSchdulManageDetail(any())).thenReturn(detail).thenReturn(null);
		assertEquals(200, controller.EgovIndvdlSchdulManageDetail(SCHEDULE_ID, null).getResultCode());

		controller.EgovIndvdlSchdulManageDelete(SCHEDULE_ID);
		assertNotFound(controller.EgovIndvdlSchdulManageDetail(SCHEDULE_ID, null));

		ArgumentCaptor<IndvdlSchdulManageVO> deletion = ArgumentCaptor.forClass(IndvdlSchdulManageVO.class);
		verify(scheduleService).deleteIndvdlSchdulManage(deletion.capture());
		assertEquals(SCHEDULE_ID, deletion.getValue().getSchdulId());
		verifyNoInteractions(fileMngService, fileUtil, cryptoService);
	}

	@ParameterizedTest
	@NullAndEmptySource
	void existingScheduleWithoutAttachmentKeepsDetailAndUser(String attachmentId) throws Exception {
		IndvdlSchdulManageVO detail = newSchedule(attachmentId);
		LoginVO user = new LoginVO();
		when(scheduleService.selectIndvdlSchdulManageDetail(any())).thenReturn(detail);

		ResultVO result = controller.EgovIndvdlSchdulManageDetail(SCHEDULE_ID, user);

		assertEquals(ResponseCode.SUCCESS.getCode(), result.getResultCode());
		assertSame(detail, result.getResult("scheduleDetail"));
		assertSame(user, result.getResult("user"));
		assertTrue(result.getResult().keySet().containsAll(List.of("schdulIpcrCode", "schdulSe", "reptitSeCode")));
		assertFalse(result.getResult().containsKey("resultFiles"));
		verifyNoInteractions(fileMngService, fileUtil, cryptoService);
	}

	@Test
	void existingScheduleReturnsAttachmentsWithEncryptedIds() throws Exception {
		String attachmentId = "FILE_000000000000001";
		IndvdlSchdulManageVO detail = newSchedule(attachmentId);
		FileVO file = new FileVO();
		file.setAtchFileId(attachmentId);
		file.setOrignlFileNm("schedule.xlsx");
		List<FileVO> files = List.of(file);
		byte[] encryptedId = "encrypted-file-id".getBytes(StandardCharsets.UTF_8);
		when(scheduleService.selectIndvdlSchdulManageDetail(any())).thenReturn(detail);
		when(fileMngService.selectFileInfs(any())).thenReturn(files);
		when(cryptoService.encrypt(any(byte[].class), eq(EgovFileDownloadController.ALGORITM_KEY)))
				.thenReturn(encryptedId);

		ResultVO result = controller.EgovIndvdlSchdulManageDetail(SCHEDULE_ID, null);

		assertEquals(ResponseCode.SUCCESS.getCode(), result.getResultCode());
		assertSame(detail, result.getResult("scheduleDetail"));
		assertSame(files, result.getResult("resultFiles"));
		assertEquals("schedule.xlsx", file.getOrignlFileNm());
		assertEquals(Base64.getEncoder().encodeToString(encryptedId), file.getAtchFileId());
		ArgumentCaptor<FileVO> query = ArgumentCaptor.forClass(FileVO.class);
		verify(fileMngService).selectFileInfs(query.capture());
		assertEquals(attachmentId, query.getValue().getAtchFileId());
		verify(cryptoService).encrypt(eq(attachmentId.getBytes(StandardCharsets.UTF_8)),
				eq(EgovFileDownloadController.ALGORITM_KEY));
	}

	private IndvdlSchdulManageVO newSchedule(String attachmentId) {
		IndvdlSchdulManageVO detail = new IndvdlSchdulManageVO();
		detail.setSchdulId(SCHEDULE_ID);
		detail.setAtchFileId(attachmentId);
		return detail;
	}

	private void assertNotFound(ResultVO result) {
		assertEquals(404, result.getResultCode());
		assertEquals("요청한 정보를 찾을 수 없습니다.", result.getResultMessage());
		assertTrue(result.getResult().isEmpty());
	}
}
