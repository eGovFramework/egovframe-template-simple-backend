package egovframework.let.cop.smt.sim.web;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.ScheduleSearchVO;

/**
 * @Class             EgovIndvdlSchdulManageApiControllerWeekListTest.java
 * @Description     year/month/date 파라미터 없이 "이번 주" 조회 시 strDate가 null인 채로 쓰여
 *                        NullPointerException이 발생하던 EgovIndvdlSchdulManageWeekList()의
 *                        버그 회귀 테스트
 * @author            content_j
 * @since             2026. 8. 27.
 *
 * <pre>
 * <개정이력(Modification Information)>
 * 개정일자                 개정자                  개정내용
 * ------------------ ----------- --------------------------
 * 2026. 8. 27.          content_j         최초생성
 *
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class EgovIndvdlSchdulManageApiControllerWeekListTest {

	@Mock
	private EgovIndvdlSchdulManageService egovIndvdlSchdulManageService;

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
		controller = new EgovIndvdlSchdulManageApiController(
				egovIndvdlSchdulManageService, cmmUseService, fileMngService, fileUtil, cryptoService, new ResultVoHelper());
	}

	@Test
	@DisplayName("year/month/date 파라미터 없이 호출해도 NPE 없이 오늘 날짜 기준 이번 주 범위(오늘~+6일)로 조회한다")
	void weekList_withoutParams_doesNotThrowAndUsesTodayAsBaseDate() throws Exception {
		// given: year/month/date 모두 null인 상태 (쿼리 파라미터 없이 호출한 상황)
		when(cmmUseService.selectCmmCodeDetail(any())).thenReturn(Collections.emptyList());
		when(egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(any())).thenReturn(Collections.emptyList());

		ScheduleSearchVO searchVO = new ScheduleSearchVO();

		// when & then: 수정 전에는 strDate.length()에서 NullPointerException 발생
		ResultVO result = assertDoesNotThrow(() -> controller.EgovIndvdlSchdulManageWeekList(searchVO));
		assertNotNull(result);

		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> paramCaptor = ArgumentCaptor.forClass(Map.class);
		verify(egovIndvdlSchdulManageService).selectIndvdlSchdulManageRetrieve(paramCaptor.capture());

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String expectedBgnde = fmt.format(Calendar.getInstance().getTime());

		Calendar expectedEndCal = Calendar.getInstance();
		expectedEndCal.add(Calendar.DATE, 6);
		String expectedEndde = fmt.format(expectedEndCal.getTime());

		Map<String, Object> capturedParams = paramCaptor.getValue();

		System.out.println("======================================");
		System.out.println("searchMode  = " + capturedParams.get("searchMode"));
		System.out.println("schdulBgnde = " + capturedParams.get("schdulBgnde") + " (expected " + expectedBgnde + ")");
		System.out.println("schdulEndde = " + capturedParams.get("schdulEndde") + " (expected " + expectedEndde + ")");
		System.out.println("======================================");

		assertEquals("WEEK", capturedParams.get("searchMode"));
		assertEquals(expectedBgnde, capturedParams.get("schdulBgnde"));
		assertEquals(expectedEndde, capturedParams.get("schdulEndde"));
	}
}
