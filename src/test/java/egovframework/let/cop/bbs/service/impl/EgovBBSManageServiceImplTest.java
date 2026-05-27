package egovframework.let.cop.bbs.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.domain.repository.BBSManageDAO;
import egovframework.let.cop.bbs.dto.request.BbsManageDeleteBoardRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsManageDetailBoardRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageListResponseDTO;
import org.egovframe.rte.fdl.crypto.EgovCryptoService;

/**
 * 게시물 관리 서비스 단위 테스트
 * @author 공통서비스 개발팀
 * @since 2026.05.28
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class EgovBBSManageServiceImplTest {

    @InjectMocks
    private EgovBBSManageServiceImpl service;

    @Mock
    private BBSManageDAO bbsMngDAO;

    @Mock
    private EgovFileMngService fileService;

    @Mock
    private EgovCryptoService cryptoService;

    private Board sampleBoard;
    private BoardVO sampleBoardVO;
    private LoginVO loginVO;

    @BeforeEach
    void setUp() {
        sampleBoard = new Board();
        sampleBoard.setBbsId("BBSMSTR_AAAAAAAAAAAA");
        sampleBoard.setNttId(1L);
        sampleBoard.setNttSj("테스트 제목");
        sampleBoard.setNttCn("테스트 내용");
        sampleBoard.setReplyAt("N");

        sampleBoardVO = new BoardVO();
        sampleBoardVO.setBbsId("BBSMSTR_AAAAAAAAAAAA");
        sampleBoardVO.setNttId(1L);
        sampleBoardVO.setNttSj("테스트 제목");
        sampleBoardVO.setNttCn("테스트 내용");

        loginVO = new LoginVO();
        loginVO.setId("testUser");
        loginVO.setUniqId("USRCNFRM_00000000000");
    }

    @Test
    @DisplayName("게시물 등록(답글 아님) - parnts/replyLc를 초기화하고 DAO를 호출한다")
    void insertBoardArticle_일반게시물_DAO호출() throws Exception {
        // given
        willDoNothing().given(bbsMngDAO).insertBoardArticle(any(Board.class));

        // when
        service.insertBoardArticle(sampleBoard);

        // then
        assertThat(sampleBoard.getParnts()).isEqualTo("0");
        assertThat(sampleBoard.getReplyLc()).isEqualTo("0");
        assertThat(sampleBoard.getReplyAt()).isEqualTo("N");
        then(bbsMngDAO).should(times(1)).insertBoardArticle(sampleBoard);
    }

    @Test
    @DisplayName("게시물 수정 - DAO 수정 메서드를 1회 호출한다")
    void updateBoardArticle_DAO호출() throws Exception {
        // given
        willDoNothing().given(bbsMngDAO).updateBoardArticle(any(Board.class));

        // when
        service.updateBoardArticle(sampleBoard);

        // then
        then(bbsMngDAO).should(times(1)).updateBoardArticle(sampleBoard);
    }

    @Test
    @DisplayName("게시물 삭제(첨부파일 없음) - 파일 서비스를 호출하지 않고 DAO만 호출한다")
    void deleteBoardArticle_첨부파일없음_파일서비스미호출() throws Exception {
        // given
        BbsManageDeleteBoardRequestDTO deleteDTO = new BbsManageDeleteBoardRequestDTO();
        deleteDTO.setBbsId("BBSMSTR_AAAAAAAAAAAA");
        deleteDTO.setNttId(1L);
        deleteDTO.setAtchFileId(null);

        willDoNothing().given(bbsMngDAO).deleteBoardArticle(any(BoardVO.class));

        // when
        service.deleteBoardArticle(deleteDTO, loginVO);

        // then
        then(bbsMngDAO).should(times(1)).deleteBoardArticle(any(BoardVO.class));
        then(fileService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("게시물 상세 조회(조회수 미증가) - plusCount가 false이면 조회수 업데이트를 호출하지 않는다")
    void selectBoardArticle_조회수미증가_정상반환() throws Exception {
        // given
        BbsManageDetailBoardRequestDTO detailDTO = BbsManageDetailBoardRequestDTO.builder()
                .bbsId("BBSMSTR_AAAAAAAAAAAA")
                .nttId(1L)
                .plusCount(false)
                .build();

        given(bbsMngDAO.selectBoardArticle(any(BoardVO.class))).willReturn(sampleBoardVO);

        // when
        BbsManageDetailResponseDTO result = service.selectBoardArticle(detailDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBoardVO()).isNotNull();
        then(bbsMngDAO).should(times(0)).updateInqireCo(any(BoardVO.class));
        then(bbsMngDAO).should(times(1)).selectBoardArticle(any(BoardVO.class));
    }

    @Test
    @DisplayName("게시물 목록 조회 - DAO 목록과 건수를 묶어 DTO로 반환한다")
    void selectBoardArticles_목록과건수반환() throws Exception {
        // given
        BbsSearchRequestDTO searchDTO = new BbsSearchRequestDTO();
        searchDTO.setBbsId("BBSMSTR_AAAAAAAAAAAA");
        searchDTO.setPageIndex(1);

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(1);
        paginationInfo.setRecordCountPerPage(10);
        paginationInfo.setPageSize(10);

        List<BoardVO> mockList = Arrays.asList(sampleBoardVO);
        given(bbsMngDAO.selectBoardArticleList(any(BoardVO.class))).willReturn(mockList);
        given(bbsMngDAO.selectBoardArticleListCnt(any(BoardVO.class))).willReturn(1);

        // when
        BbsManageListResponseDTO result = service.selectBoardArticles(searchDTO, paginationInfo, "BBSA02");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getResultList()).hasSize(1);
        assertThat(result.getResultCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("방명록 목록 조회 - 목록과 건수를 Map으로 반환한다")
    void selectGuestList_목록건수Map반환() throws Exception {
        // given
        List<BoardVO> mockList = Arrays.asList(sampleBoardVO);
        given(bbsMngDAO.selectGuestList(any(BoardVO.class))).willReturn(mockList);
        given(bbsMngDAO.selectGuestListCnt(any(BoardVO.class))).willReturn(1);

        // when
        Map<String, Object> result = service.selectGuestList(sampleBoardVO);

        // then
        assertThat(result).containsKey("resultList");
        assertThat(result).containsKey("resultCnt");
        assertThat(result.get("resultCnt")).isEqualTo("1");
    }

    @Test
    @DisplayName("방명록 삭제 - DAO 삭제 메서드를 1회 호출한다")
    void deleteGuestList_DAO호출() throws Exception {
        // given
        willDoNothing().given(bbsMngDAO).deleteGuestList(any(BoardVO.class));

        // when
        service.deleteGuestList(sampleBoardVO);

        // then
        then(bbsMngDAO).should(times(1)).deleteGuestList(sampleBoardVO);
    }
}
