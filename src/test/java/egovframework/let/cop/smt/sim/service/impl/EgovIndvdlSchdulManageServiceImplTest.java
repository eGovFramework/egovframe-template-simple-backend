package egovframework.let.cop.smt.sim.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;

/**
 * 개인 일정관리 서비스 단위 테스트
 * @author 공통서비스 개발팀
 * @since 2026.05.28
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class EgovIndvdlSchdulManageServiceImplTest {

    @InjectMocks
    private EgovIndvdlSchdulManageServiceImpl service;

    @Mock
    private IndvdlSchdulManageDao dao;

    @Mock
    private EgovIdGnrService idgenService;

    private IndvdlSchdulManageVO sampleVO;
    private ComDefaultVO searchVO;

    @BeforeEach
    void setUp() {
        sampleVO = new IndvdlSchdulManageVO();
        sampleVO.setSchdulId("SCHDUL_0001");
        sampleVO.setSchdulNm("팀 회의");
        sampleVO.setSchdulSe("01");
        sampleVO.setSchdulBgnde("20260528");
        sampleVO.setSchdulEndde("20260528");
        sampleVO.setSchdulChargerId("testUser");

        searchVO = new ComDefaultVO();
        searchVO.setSearchKeyword("");
        searchVO.setPageIndex(1);
    }

    @Test
    @DisplayName("일정 목록 전체 건수 조회 - DAO 위임 결과를 그대로 반환한다")
    void selectIndvdlSchdulManageListCnt_건수반환() throws Exception {
        // given
        given(dao.selectIndvdlSchdulManageListCnt(any(ComDefaultVO.class))).willReturn(3);

        // when
        int cnt = service.selectIndvdlSchdulManageListCnt(searchVO);

        // then
        assertThat(cnt).isEqualTo(3);
        then(dao).should(times(1)).selectIndvdlSchdulManageListCnt(searchVO);
    }

    @Test
    @DisplayName("일정 목록 조회 - DAO가 반환한 리스트를 그대로 반환한다")
    void selectIndvdlSchdulManageList_목록반환() throws Exception {
        // given
        List<Object> mockList = Arrays.asList(sampleVO);
        doReturn(mockList).when(dao).selectIndvdlSchdulManageList(any(ComDefaultVO.class));

        // when
        List<?> result = service.selectIndvdlSchdulManageList(searchVO);

        // then
        assertThat(result).hasSize(1);
        then(dao).should(times(1)).selectIndvdlSchdulManageList(searchVO);
    }

    @Test
    @DisplayName("일정 상세 조회 - DAO가 반환한 VO를 그대로 반환한다")
    void selectIndvdlSchdulManageDetail_상세반환() throws Exception {
        // given
        given(dao.selectIndvdlSchdulManageDetail(any(IndvdlSchdulManageVO.class))).willReturn(sampleVO);

        // when
        IndvdlSchdulManageVO result = service.selectIndvdlSchdulManageDetail(sampleVO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getSchdulId()).isEqualTo("SCHDUL_0001");
        assertThat(result.getSchdulNm()).isEqualTo("팀 회의");
        then(dao).should(times(1)).selectIndvdlSchdulManageDetail(sampleVO);
    }

    @Test
    @DisplayName("일정 등록 - ID 생성 후 schdulId를 VO에 세팅하고 DAO를 호출한다")
    void insertIndvdlSchdulManage_ID생성후DAO호출() throws Exception {
        // given
        String generatedId = "SCHDUL_NEW_001";
        given(idgenService.getNextStringId()).willReturn(generatedId);
        willDoNothing().given(dao).insertIndvdlSchdulManage(any(IndvdlSchdulManageVO.class));

        IndvdlSchdulManageVO newVO = new IndvdlSchdulManageVO();
        newVO.setSchdulNm("신규 일정");

        // when
        service.insertIndvdlSchdulManage(newVO);

        // then
        assertThat(newVO.getSchdulId()).isEqualTo(generatedId);
        then(idgenService).should(times(1)).getNextStringId();
        then(dao).should(times(1)).insertIndvdlSchdulManage(newVO);
    }

    @Test
    @DisplayName("일정 수정 - DAO 수정 메서드를 1회 호출한다")
    void updateIndvdlSchdulManage_DAO호출() throws Exception {
        // given
        willDoNothing().given(dao).updateIndvdlSchdulManage(any(IndvdlSchdulManageVO.class));

        // when
        service.updateIndvdlSchdulManage(sampleVO);

        // then
        then(dao).should(times(1)).updateIndvdlSchdulManage(sampleVO);
    }
}
