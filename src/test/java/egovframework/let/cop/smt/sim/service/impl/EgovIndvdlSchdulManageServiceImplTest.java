package egovframework.let.cop.smt.sim.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

/**
 * [일정관리][EgovIndvdlSchdulManageServiceImpl.deleteIndvdlSchdulManage] 단위 테스트
 *
 * 일정을 삭제할 때 해당 일정에 달린 첨부파일도 함께 정리되는지 확인한다.
 */
class EgovIndvdlSchdulManageServiceImplTest {

	private EgovIndvdlSchdulManageServiceImpl newService(IndvdlSchdulManageDao dao, EgovFileMngService fileService) {
		EgovIndvdlSchdulManageServiceImpl service = new EgovIndvdlSchdulManageServiceImpl();
		ReflectionTestUtils.setField(service, "dao", dao);
		ReflectionTestUtils.setField(service, "fileService", fileService);
		return service;
	}

	@DisplayName("첨부파일이 있는 일정을 삭제하면 첨부파일도 함께 삭제된다.")
	@Test
	void deleteIndvdlSchdulManageRemovesAttachedFile() throws Exception {
		// given
		IndvdlSchdulManageDao dao = mock(IndvdlSchdulManageDao.class);
		EgovFileMngService fileService = mock(EgovFileMngService.class);

		IndvdlSchdulManageVO schdulDetail = new IndvdlSchdulManageVO();
		schdulDetail.setAtchFileId("FILE_000000000000001");
		when(dao.selectIndvdlSchdulManageDetail(any(IndvdlSchdulManageVO.class))).thenReturn(schdulDetail);

		IndvdlSchdulManageVO param = new IndvdlSchdulManageVO();
		param.setSchdulId("SCHDUL_00000000000001");

		// when
		newService(dao, fileService).deleteIndvdlSchdulManage(param);

		// then
		verify(dao).deleteIndvdlSchdulManage(param);

		ArgumentCaptor<FileVO> captor = ArgumentCaptor.forClass(FileVO.class);
		verify(fileService).deleteAllFileInf(captor.capture());
		org.junit.jupiter.api.Assertions.assertEquals("FILE_000000000000001", captor.getValue().getAtchFileId());
	}

	@DisplayName("첨부파일이 없는 일정을 삭제하면 파일 삭제는 호출되지 않는다.")
	@Test
	void deleteIndvdlSchdulManageSkipsFileRemovalWhenNoAttachment() throws Exception {
		// given
		IndvdlSchdulManageDao dao = mock(IndvdlSchdulManageDao.class);
		EgovFileMngService fileService = mock(EgovFileMngService.class);

		IndvdlSchdulManageVO schdulDetail = new IndvdlSchdulManageVO();
		schdulDetail.setAtchFileId("");
		when(dao.selectIndvdlSchdulManageDetail(any(IndvdlSchdulManageVO.class))).thenReturn(schdulDetail);

		IndvdlSchdulManageVO param = new IndvdlSchdulManageVO();
		param.setSchdulId("SCHDUL_00000000000002");

		// when
		newService(dao, fileService).deleteIndvdlSchdulManage(param);

		// then
		verify(dao).deleteIndvdlSchdulManage(param);
		verify(fileService, never()).deleteAllFileInf(any(FileVO.class));
	}
}
