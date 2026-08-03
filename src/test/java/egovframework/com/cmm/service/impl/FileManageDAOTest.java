package egovframework.com.cmm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.cmm.service.FileVO;

/**
 * [파일관리][FileManageDAO.selectFileInf] DAO 단위 테스트
 *
 * 첨부파일 마스터(LETTNFILE)는 USE_AT 플래그로 논리 삭제된다.
 * 삭제 처리된 첨부가 상세 조회에서 걸러지는지 확인한다.
 */
@SpringBootTest
@Transactional
class FileManageDAOTest {

	@Autowired
	private FileManageDAO fileManageDAO;

	private FileVO newFile(String atchFileId) {
		FileVO vo = new FileVO();
		vo.setAtchFileId(atchFileId);
		vo.setFileSn("0");
		vo.setFileStreCours("./files");
		vo.setStreFileNm("TEST_20260803000000000");
		vo.setOrignlFileNm("sample.png");
		vo.setFileExtsn("png");
		vo.setFileMg("1024");
		vo.setFileCn("");
		return vo;
	}

	@DisplayName("사용 중인 첨부파일은 상세 정보가 조회된다.")
	@Test
	void selectFileInfReturnsFileInUse() throws Exception {
		// given
		FileVO vo = newFile("FILE_TEST0000000001");
		fileManageDAO.insertFileInf(vo);

		// when
		FileVO result = fileManageDAO.selectFileInf(vo);

		// then
		assertNotNull(result, "USE_AT='Y'인 첨부파일은 조회되어야 한다.");
		assertEquals("sample.png", result.getOrignlFileNm());
	}

	@DisplayName("삭제 처리된 첨부파일은 상세 정보가 조회되지 않는다.")
	@Test
	void selectFileInfExcludesDeletedFile() throws Exception {
		// given
		FileVO vo = newFile("FILE_TEST0000000002");
		fileManageDAO.insertFileInf(vo);

		// when
		fileManageDAO.deleteAllFileInf(vo);
		FileVO result = fileManageDAO.selectFileInf(vo);

		// then
		assertNull(result, "USE_AT='N'으로 삭제된 첨부파일은 조회되지 않아야 한다.");
	}
}
