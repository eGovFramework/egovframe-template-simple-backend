package egovframework.com.cmm.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import egovframework.com.cmm.service.FileVO;

@SpringBootTest
@Transactional
class FileManageDAOTest {

	@Resource(name = "FileManageDAO")
	private FileManageDAO fileManageDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	@DisplayName("N+1 문제 해결에 따른 다중 파일 삭제 성능 비교 테스트")
	void compareBulkDeletePerformance() {
		System.out.println("\n=======================================================================");
		System.out.println("  파일 다중 삭제 성능 비교 테스트 (While Loop vs Bulk)");
		System.out.println("=======================================================================");
		System.out.printf("%-10s | %-20s | %-20s%n", "데이터 수", "기존 방식 (ms)", "개선 방식 (ms)");
		System.out.println("-----------------------------------------------------------------------");

		performTestFor(100);
		performTestFor(1000);
		performTestFor(10000);
		performTestFor(100000);

		System.out.println("=======================================================================\n");
	}

	private void performTestFor(int dataSize) {
		// 1. 테스트 데이터 준비
		List<FileVO> fileList = new ArrayList<>();
		String atchFileId = "PERF_TEST_" + dataSize;

		// 마스터 테이블 데이터 삽입
		jdbcTemplate.update("INSERT INTO LETTNFILE (ATCH_FILE_ID, USE_AT) VALUES (?, 'Y')", atchFileId);

		// 상세 데이터 Bulk Insert
		String insertSql = "INSERT INTO LETTNFILEDETAIL (ATCH_FILE_ID, FILE_SN, FILE_STRE_COURS, STRE_FILE_NM, FILE_EXTSN) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, atchFileId);
				ps.setString(2, String.valueOf(i));
				ps.setString(3, "/test");
				ps.setString(4, "test.txt");
				ps.setString(5, "txt");
				FileVO vo = new FileVO();
				vo.setAtchFileId(atchFileId);
				vo.setFileSn(String.valueOf(i));
				fileList.add(vo);
			}
			public int getBatchSize() {
				return dataSize;
			}
		});

		// 2. 기존 방식(While) 성능 측정
		long loopStartTime = System.currentTimeMillis();
		try {
			fileManageDAO.deleteFileInfs(fileList);
		} catch (Exception e) {
			// 예외 처리
		}
		long loopDuration = System.currentTimeMillis() - loopStartTime;

		// 3. 개선 방식(Bulk) 성능 측정을 위해 데이터 다시 삽입
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, atchFileId);
				ps.setString(2, String.valueOf(i));
				ps.setString(3, "/test");
				ps.setString(4, "test.txt");
				ps.setString(5, "txt");
			}
			public int getBatchSize() {
				return dataSize;
			}
		});

		// 4. 개선 방식(Bulk) 성능 측정
		long bulkStartTime = System.currentTimeMillis();
		try {
			fileManageDAO.deleteFileInfs_bulk(fileList);
		} catch (Exception e) {
			// 예외 처리
		}
		long bulkDuration = System.currentTimeMillis() - bulkStartTime;

		// 5. 결과 출력
		System.out.printf("%-10s | %-20s | %-20s%n", dataSize + "건", loopDuration + "ms", bulkDuration + "ms");
	}
}
