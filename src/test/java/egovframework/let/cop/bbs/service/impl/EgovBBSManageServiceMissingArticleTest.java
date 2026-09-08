package egovframework.let.cop.bbs.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.domain.repository.BBSManageDAO;
import egovframework.let.cop.bbs.dto.request.BbsManageDetailBoardRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailResponseDTO;

/**
 * 존재하지 않는 게시물을 조회할 때 서비스가 null 을 돌려주는지 검증한다.
 *
 * <p>호출부의 소유권 검증은 {@code article == null || article.getBoardVO() == null} 로
 * 작성돼 있는데, 그 앞에서 DTO 변환이 NullPointerException 을 내 그 분기에 도달하지
 * 못했다. 그래서 삭제된 게시물을 수정·삭제·조회하면 500 이 반환됐다.</p>
 */
@ExtendWith(MockitoExtension.class)
class EgovBBSManageServiceMissingArticleTest {

    @Mock
    private BBSManageDAO bbsMngDAO;

    @Mock
    private EgovFileMngService fileService;

    @Mock
    private EgovCryptoService cryptoService;

    @Mock
    private EgovIdGnrService egovNttIdGnrService;

    @InjectMocks
    private EgovBBSManageServiceImpl egovBBSManageService;

    private BbsManageDetailBoardRequestDTO request() {
        return BbsManageDetailBoardRequestDTO.builder()
                .bbsId("BBSMSTR_AAAAAAAAAAAA")
                .nttId(999999L)
                .plusCount(false)
                .build();
    }

    @Test
    @DisplayName("게시물이 없으면 예외 대신 null 을 돌려준다")
    void returnsNullWhenArticleDoesNotExist() throws Exception {
        when(bbsMngDAO.selectBoardArticle(any(BoardVO.class))).thenReturn(null);

        BbsManageDetailResponseDTO result = egovBBSManageService.selectBoardArticle(request());

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("게시물이 없으면 첨부파일 조회로 넘어가지 않는다")
    void doesNotLookUpAttachmentsWhenArticleDoesNotExist() throws Exception {
        when(bbsMngDAO.selectBoardArticle(any(BoardVO.class))).thenReturn(null);

        assertThatCode(() -> egovBBSManageService.selectBoardArticle(request()))
                .doesNotThrowAnyException();

        verify(fileService, never()).selectFileInfs(any());
    }

    @Test
    @DisplayName("게시물이 있으면 기존과 같이 내용을 담아 돌려준다")
    void returnsArticleWhenItExists() throws Exception {
        BoardVO vo = new BoardVO();
        vo.setBbsId("BBSMSTR_AAAAAAAAAAAA");
        vo.setNttId(1L);
        vo.setNttSj("제목");
        vo.setFrstRegisterId("USER000000000000001");
        when(bbsMngDAO.selectBoardArticle(any(BoardVO.class))).thenReturn(vo);

        BbsManageDetailResponseDTO result = egovBBSManageService.selectBoardArticle(request());

        assertThat(result).isNotNull();
        assertThat(result.getBoardVO()).isNotNull();
        assertThat(result.getBoardVO().getNttSj()).isEqualTo("제목");
        assertThat(result.getBoardVO().getFrstRegisterId()).isEqualTo("USER000000000000001");
    }
}
