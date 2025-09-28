package egovframework.com.cmm.service.impl;

import egovframework.com.cmm.service.FileVO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FileManageDAOBulkPerformanceTest {

	@Resource(name = "FileManageDAO")
	private FileManageDAO fileManageDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 10만건은 시간이 오래 걸릴 수 있으므로, 필요 시 배열에 추가하여 테스트하세요.
	private final int[] dataSizes = {100, 1000, 10000, 100000};

	@Test
	@DisplayName("다중 파일 등록/수정/삭제 성능 비교")
	void compareCUDPerformance() throws Exception {
		System.out.println("\n--- [성능 비교] 다중 파일 등록 (Insert) ---");
		System.out.printf("%-12s | %-20s | %-20s%n", "데이터 수", "Loop 방식 (ms)", "Bulk 방식 (ms)");
		System.out.println("------------------------------------------------------------");
		for (int size : dataSizes) {
			performInsertTest(size);
		}

		System.out.println("\n--- [성능 비교] 다중 파일 수정 (Update) ---");
		System.out.printf("%-12s | %-20s | %-20s%n", "데이터 수", "Loop 방식 (ms)", "Bulk 방식 (ms)");
		System.out.println("------------------------------------------------------------");
		for (int size : dataSizes) {
			performUpdateTest(size);
		}

		System.out.println("\n--- [성능 비교] 다중 파일 삭제 (Delete) ---");
		System.out.printf("%-12s | %-20s | %-20s%n", "데이터 수", "Loop 방식 (ms)", "Bulk 방식 (ms)");
		System.out.println("------------------------------------------------------------");
		for (int size : dataSizes) {
			performDeleteTest(size);
		}
	}

	private void performInsertTest(int size) throws Exception {
		String atchFileId = "INSERT_TEST_" + size;
		List<FileVO> fileList = createTestFileList(atchFileId, size);

		// --- 1. Loop 방식 성능 측정 ---
		long loopStartTime = System.currentTimeMillis();
		insertFileInfsWithLoop(fileList);
		long loopDuration = System.currentTimeMillis() - loopStartTime;

		// --- 2. Bulk 방식 측정을 위해 데이터 초기화 ---
		jdbcTemplate.update("DELETE FROM LETTNFILEDETAIL WHERE ATCH_FILE_ID = ?", atchFileId);
		jdbcTemplate.update("DELETE FROM LETTNFILE WHERE ATCH_FILE_ID = ?", atchFileId);

		// --- 3. Bulk 방식 성능 측정 ---
		long bulkStartTime = System.currentTimeMillis();
		fileManageDAO.insertFileInfs(fileList);
		long bulkDuration = System.currentTimeMillis() - bulkStartTime;

		System.out.printf("%-12s | %-20s | %-20s%n", size + "건", loopDuration, bulkDuration);
	}

	private void performUpdateTest(int size) throws Exception {
		String atchFileId = "UPDATE_TEST_" + size;
		List<FileVO> initialData = createTestFileList(atchFileId, size);
		List<FileVO> updatedData = createTestFileList(atchFileId, size);
		updatedData.forEach(vo -> vo.setOrignlFileNm("updated_file.txt"));

		// --- 1. Loop 방식 성능 측정 (N번 Delete + N번 Insert) ---
		// 테스트 데이터 준비
		insertFileInfsWithLoop(initialData);

		long loopStartTime = System.currentTimeMillis();
		// [중요] 기존 방식은 '수정' 기능이 없었으므로, '전체 삭제 후 전체 삽입'으로 시뮬레이션
		deleteFileInfsWithLoop(initialData);
		insertFileInfsWithLoop(updatedData);
		long loopDuration = System.currentTimeMillis() - loopStartTime;

		// --- 2. Bulk 방식 측정을 위해 데이터 초기화 및 재입력 ---
		jdbcTemplate.update("DELETE FROM LETTNFILEDETAIL WHERE ATCH_FILE_ID = ?", atchFileId);
		jdbcTemplate.update("DELETE FROM LETTNFILE WHERE ATCH_FILE_ID = ?", atchFileId);
		fileManageDAO.insertFileInfs(initialData); // 테스트 데이터 다시 준비

		// --- 3. Bulk 방식 성능 측정 (단일 Upsert 쿼리) ---
		long bulkStartTime = System.currentTimeMillis();
		fileManageDAO.updateFileInfs(updatedData);
		long bulkDuration = System.currentTimeMillis() - bulkStartTime;

		System.out.printf("%-12s | %-20s | %-20s%n", size + "건", loopDuration, bulkDuration);
	}

	private void performDeleteTest(int size) throws Exception {
		String atchFileId = "DELETE_TEST_" + size;
		List<FileVO> fileList = createTestFileList(atchFileId, size);

		// --- 1. Loop 방식 성능 측정 ---
		insertFileInfsWithLoop(fileList); // 삭제할 데이터 준비
		long loopStartTime = System.currentTimeMillis();
		deleteFileInfsWithLoop(fileList);
		long loopDuration = System.currentTimeMillis() - loopStartTime;

		// --- 2. Bulk 방식 성능 측정 ---
		insertFileInfsWithLoop(fileList); // 삭제할 데이터 다시 준비
		long bulkStartTime = System.currentTimeMillis();
		fileManageDAO.deleteFileInfs(fileList);
		long bulkDuration = System.currentTimeMillis() - bulkStartTime;

		System.out.printf("%-12s | %-20s | %-20s%n", size + "건", loopDuration, bulkDuration);
	}

	// ===== 기존 Loop 로직을 테스트 클래스 내에서 직접 구현 =====

	private void insertFileInfsWithLoop(List<FileVO> fileList) throws Exception {
		if (fileList.isEmpty()) return;
		try {
			// 마스터 정보는 한 번만 INSERT 시도
			fileManageDAO.insert("FileManageDAO.insertFileMaster", fileList.get(0));
		} catch (Exception e) {
			// update 테스트 등에서 master key가 이미 있는 경우 예외가 발생할 수 있으므로 무시
		}
		for (FileVO vo : fileList) {
			fileManageDAO.insert("FileManageDAO.insertFileDetail", vo);
		}
	}

	private void deleteFileInfsWithLoop(List<FileVO> fileList) throws Exception {
		for (FileVO vo : fileList) {
			fileManageDAO.delete("FileManageDAO.deleteFileDetail", vo);
		}
	}

	private List<FileVO> createTestFileList(String atchFileId, int size) {
		List<FileVO> fileList = new ArrayList<>();
		IntStream.range(0, size).forEach(i -> {
			FileVO vo = new FileVO();
			vo.setAtchFileId(atchFileId);
			vo.setFileSn(String.valueOf(i));
			vo.setFileStreCours("/test/path");
			vo.setStreFileNm("test_file.txt");
			vo.setOrignlFileNm("original.txt");
			vo.setFileExtsn("txt");
			vo.setFileMg("1024");
			vo.setFileCn("test content");
			fileList.add(vo);
		});
		return fileList;
	}
}
