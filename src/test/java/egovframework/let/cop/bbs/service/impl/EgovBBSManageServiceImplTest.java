package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.repository.BBSManageDAO;

@ExtendWith(MockitoExtension.class)
class EgovBBSManageServiceImplTest {

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

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("일반 게시물 등록 시 정상적으로 ID를 채번하여 DAO에 전달한다")
    void testInsertBoardArticle_NormalPost() throws Exception {
        // given
        Board board = new Board();
        board.setReplyAt("N");
        long generatedId = 15L;
        
        when(egovNttIdGnrService.getNextLongId()).thenReturn(generatedId);

        // when
        egovBBSManageService.insertBoardArticle(board);

        // then
        ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(bbsMngDAO).insertBoardArticle(boardCaptor.capture());
        
        Board savedBoard = boardCaptor.getValue();
        assertEquals(generatedId, savedBoard.getNttId());
        assertEquals("0", savedBoard.getParnts());
        assertEquals("0", savedBoard.getReplyLc());
        assertEquals("N", savedBoard.getReplyAt());
        
        verify(bbsMngDAO, never()).replyBoardArticle(any());
    }

    @Test
    @DisplayName("답글 게시물 등록 시 정상적으로 ID를 채번하여 DAO에 전달한다")
    void testInsertBoardArticle_ReplyPost() throws Exception {
        // given
        Board board = new Board();
        board.setReplyAt("Y");
        long generatedId = 20L;
        
        when(egovNttIdGnrService.getNextLongId()).thenReturn(generatedId);

        // when
        egovBBSManageService.insertBoardArticle(board);

        // then
        ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(bbsMngDAO).replyBoardArticle(boardCaptor.capture());
        
        Board savedBoard = boardCaptor.getValue();
        assertEquals(generatedId, savedBoard.getNttId());
        
        verify(bbsMngDAO, never()).insertBoardArticle(any());
    }

    @Test
    @DisplayName("ID 채번 중 예외 발생 시 DAO를 호출하지 않고 예외를 던진다")
    void testInsertBoardArticle_IdGenerationException() throws Exception {
        // given
        Board board = new Board();
        
        when(egovNttIdGnrService.getNextLongId()).thenThrow(new RuntimeException("ID Generation Failed"));

        // when & then
        assertThrows(RuntimeException.class, () -> {
            egovBBSManageService.insertBoardArticle(board);
        });

        // DAO 불리지 않는지 확인
        verify(bbsMngDAO, never()).insertBoardArticle(any());
        verify(bbsMngDAO, never()).replyBoardArticle(any());
    }
}
