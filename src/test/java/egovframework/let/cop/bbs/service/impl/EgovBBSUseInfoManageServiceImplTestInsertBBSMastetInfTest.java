package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import egovframework.let.cop.bbs.dto.request.BbsAttributeInsertRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [게시판생성관리][EgovBBSUseInfoManageServiceImpl.insertBBSMastetInf] ServiceImpl 단위
 * 테스트
 * 
 * @author 이백행
 * @since 2024-09-20
 *
 */
@SpringBootTest
@RequiredArgsConstructor
@Slf4j
class EgovBBSUseInfoManageServiceImplTestInsertBBSMastetInfTest {

	/**
	 * 게시판 속성정보 관리를 위한 데이터 접근 클래스
	 */
	@Autowired
	private EgovBBSAttributeManageService egovBBSAttributeManageService;

	@Test
	void test() throws Exception {
		// given
		final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS"));
		
		final BbsAttributeInsertRequestDTO bbsInsertRequestDTO = new BbsAttributeInsertRequestDTO();
		bbsInsertRequestDTO.setBbsNm("test 이백행 게시판명 " + now);
		bbsInsertRequestDTO.setPosblAtchFileSize("0");
		bbsInsertRequestDTO.setBbsAttrbCode("BBSA02");
		bbsInsertRequestDTO.setBbsTyCode("BBST01");
		bbsInsertRequestDTO.setFileAtchPosblAt("Y");
		bbsInsertRequestDTO.setUseAt("Y");
		bbsInsertRequestDTO.setFrstRegisterId("admin");
		
		// selectBBSMasterInf의 코드는 리액트 변경이 필요하여 리펙토링 보류 상태이므로
		// 테스트를 위해 임시로 BoardMaster를 사용합니다.
		final BoardMaster boardMaster = new BoardMaster();
		boardMaster.setBbsNm("test 이백행 게시판명 " + now);
		boardMaster.setPosblAtchFileSize("0");
		
		// when
		final String result = egovBBSAttributeManageService.insertBBSMastetInf(bbsInsertRequestDTO);
		boardMaster.setBbsId(result);

		// then
		final BoardMasterVO resultBoardMasterVO = egovBBSAttributeManageService.selectBBSMasterInf(boardMaster);
		
		if (log.isDebugEnabled()) {
			log.debug("result={}", result);
			log.debug("resultBoardMasterVO={}", resultBoardMasterVO);
			log.debug("getBbsId={}", resultBoardMasterVO.getBbsId());
		}

		assertEquals(result, resultBoardMasterVO.getBbsId(), "신규 게시판 속성정보를 생성한다.");
	}

}