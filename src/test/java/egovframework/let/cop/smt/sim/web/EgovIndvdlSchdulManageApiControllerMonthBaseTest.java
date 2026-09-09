package egovframework.let.cop.smt.sim.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
 * ScheduleSearchVO.month 가 0-base(1월=0)로 해석된다는 계약을 고정한다.
 *
 * <p>@Schema 는 이 값을 1~12 로 적어 두었으나 조회 경로는 받은 값에 1 을 더해 검색월을 만든다.
 * 문서대로 12 를 넣으면 검색월이 13 이 되어 12월 안에서 시작하고 끝나는 일정이 빠진다.
 */
@ExtendWith(MockitoExtension.class)
class EgovIndvdlSchdulManageApiControllerMonthBaseTest {

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

	@DisplayName("월간 조회의 month 는 0-base 로 해석된다 (0=1월, 11=12월, 문서대로 12 를 주면 13월이 된다)")
	@ParameterizedTest(name = "month={0} → searchMonth={1}")
	@CsvSource({"0,202601", "4,202605", "11,202612", "12,202613"})
	void monthListInterpretsMonthAsZeroBased(String month, String expectedSearchMonth) throws Exception {
		when(cmmUseService.selectCmmCodeDetail(any())).thenReturn(Collections.emptyList());
		when(egovIndvdlSchdulManageService.selectIndvdlSchdulManageRetrieve(any())).thenReturn(Collections.emptyList());

		ScheduleSearchVO searchVO = new ScheduleSearchVO();
		searchVO.setYear("2026");
		searchVO.setMonth(month);

		controller.EgovIndvdlSchdulManageMonthList(searchVO, null);

		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(egovIndvdlSchdulManageService).selectIndvdlSchdulManageRetrieve(captor.capture());

		assertEquals(expectedSearchMonth, captor.getValue().get("searchMonth"));
	}
}
