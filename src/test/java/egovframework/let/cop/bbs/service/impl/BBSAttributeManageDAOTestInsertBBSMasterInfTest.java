package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.repository.BBSAttributeManageDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [게시판생성관리][BBSAttributeManageDAO.insertBBSMasterInf] DAO 단위 테스트
 * 
 * @author 이백행
 * @since 2024-09-20
 *
 */
@SpringBootTest
@RequiredArgsConstructor
@Slf4j
class BBSAttributeManageDAOTestInsertBBSMasterInfTest {

	/**
	 * 게시판 속성정보 관리를 위한 데이터 접근 클래스
	 */
	@Autowired
	private BBSAttributeManageDAO bbsAttributeManageDAO;

	/**
	 * 
	 */
	@Autowired
	private EgovIdGnrService egovBBSMstrIdGnrService;

	@Test
	void test() throws Exception {
		// given
		final BoardMaster boardMaster = new BoardMaster();

		boardMaster.setBbsId(egovBBSMstrIdGnrService.getNextStringId());

		final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS"));

		boardMaster.setBbsNm("test 이백행 게시판명 " + now);

		boardMaster.setPosblAtchFileSize("0");

		final int expected = 1;

		// when
		final int result = bbsAttributeManageDAO.insertBBSMasterInf(boardMaster);

		// then
		if (log.isDebugEnabled()) {
			log.debug("result={}, {}", expected, result);
		}

		assertEquals(expected, result, "신규 게시판 속성정보를 등록한다.");
	}

}