package egovframework.let.cop.smt.sim.web;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
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
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.ScheduleSearchVO;

/**
 * @Class             EgovIndvdlSchdulManageApiControllerPartialDateParamTest.java
 * @Description     year 만 주고 month·date 를 빼면 파싱 게이트가 통과해 NumberFormatException 이
 *                        나던 일간·주간 조회의 회귀 테스트. 같은 컨트롤러의 월간 조회는 파싱하는 필드를
 *                        전부 null 검사해 오늘 기준으로 폴백한다.
 * @author            wantaek
 * @since             2026. 9. 9.
 *
 * <pre>
 * <개정이력(Modification Information)>
 * 개정일자                 개정자                  개정내용
 * ------------------ ----------- --------------------------
 * 2026. 9. 9.           wantaek           최초생성
 *
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class EgovIndvdlSchdulManageApiControllerPartialDateParamTest {

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

	/** 오늘 기준으로 폴백했을 때 나와야 하는 yyyyMMdd */
	private static String today() {
		return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
	}

	@Test
	@DisplayName("일간 조회에 year 만 주면 month·date 를 오늘로 채워 조회한다")
	void dailyList_withYearOnly_fallsBackToToday() throws Exception {
		when(cmmUseService.selectCmmCodeDetail(any())).thenReturn(Collections.emptyList());
		when(egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(any())).thenReturn(Collections.emptyList());

		ScheduleSearchVO searchVO = new ScheduleSearchVO();
		searchVO.setYear("2026");

		assertDoesNotThrow(() -> controller.EgovIndvdlSchdulManageDailyList(searchVO));

		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(egovIndvdlSchdulManageService).selectIndvdlSchdulManageRetrieve(captor.capture());

		assertEquals(today(), captor.getValue().get("searchDay"));
	}

	@Test
	@DisplayName("주간 조회에 year 만 주면 month·date 를 오늘로 채워 조회한다")
	void weekList_withYearOnly_fallsBackToToday() throws Exception {
		when(cmmUseService.selectCmmCodeDetail(any())).thenReturn(Collections.emptyList());
		when(egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(any())).thenReturn(Collections.emptyList());

		ScheduleSearchVO searchVO = new ScheduleSearchVO();
		searchVO.setYear("2026");

		assertDoesNotThrow(() -> controller.EgovIndvdlSchdulManageWeekList(searchVO));

		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(egovIndvdlSchdulManageService).selectIndvdlSchdulManageRetrieve(captor.capture());

		assertEquals(today(), captor.getValue().get("schdulBgnde"));
	}
}
