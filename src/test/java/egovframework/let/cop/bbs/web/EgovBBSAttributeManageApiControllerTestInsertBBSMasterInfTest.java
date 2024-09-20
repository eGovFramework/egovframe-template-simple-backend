package egovframework.let.cop.bbs.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import egovframework.let.cop.bbs.service.BoardMaster;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [게시판생성관리][EgovBBSAttributeManageApiController.insertBBSMasterInf] Controller
 * 단위 테스트
 * 
 * @author 이백행
 * @since 2024-09-20
 *
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor
@Slf4j
class EgovBBSAttributeManageApiControllerTestInsertBBSMasterInfTest {

	/**
	 * 
	 */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * 게시판 속성정보 관리를 위한 데이터 접근 클래스
	 */
	@Autowired
	private EgovBBSAttributeManageService egovBBSAttributeManageService;

	@Test
	void test() throws Exception {
		// testData
		final BoardMaster boardMaster = new BoardMaster();

		final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS"));

		boardMaster.setBbsNm("test 이백행 게시판명 " + now);

		boardMaster.setPosblAtchFileSize("0");

		final String resultBbsId = egovBBSAttributeManageService.insertBBSMastetInf(boardMaster);

		// given

		// when
		mockMvc.perform(

				get("/bbsMaster")

						.param("searchCnd", "0")

						.param("searchWrd", boardMaster.getBbsNm())

		)

				.andDo(print())

				.andExpect(status().isOk())

				// resultCode
				.andExpect(jsonPath("$.resultCode").value(200))

				.andExpect(jsonPath("$.resultCode").value(equalTo(200)))

				// resultMessage
				.andExpect(jsonPath("$.resultMessage").value("성공했습니다."))

				.andExpect(jsonPath("$.resultMessage").value(equalTo("성공했습니다.")))

				// resultCnt
				.andExpect(jsonPath("$.result.resultCnt").value("1"))

				.andExpect(jsonPath("$.result.resultCnt").value(equalTo("1")))

				// bbsNm
				.andExpect(jsonPath("$.result.resultList[0].bbsNm").value(boardMaster.getBbsNm()))

				.andExpect(jsonPath("$.result.resultList[0].bbsNm").value(equalTo(boardMaster.getBbsNm())))

				// bbsId
				.andExpect(jsonPath("$.result.resultList[0].bbsId").value(resultBbsId))

				.andExpect(jsonPath("$.result.resultList[0].bbsId").value(equalTo(resultBbsId)))

		;

		// then
		if (log.isDebugEnabled()) {
			log.debug("resultBbsId={}", resultBbsId);
		}

		assertEquals("", "", "게시판 마스터 목록을 조회한다.");
	}

}
