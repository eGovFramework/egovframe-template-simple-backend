package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
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
import egovframework.let.cop.bbs.dto.request.BbsManageDeleteBoardRequestDTO;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.LoginVO;
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

    @Test
    @DisplayName("게시물 삭제 시 첨부파일 그룹은 클라이언트가 보낸 값이 아니라 저장된 게시물에서 읽는다")
    void testDeleteBoardArticle_UsesStoredAtchFileId() throws Exception {
        // given: 저장된 게시물의 첨부는 FILE_STORED 인데 요청은 FILE_FROM_CLIENT 를 보낸다.
        BoardVO stored = new BoardVO();
        stored.setAtchFileId("FILE_STORED");
        when(bbsMngDAO.selectBoardArticle(any(BoardVO.class))).thenReturn(stored);

        BbsManageDeleteBoardRequestDTO request = new BbsManageDeleteBoardRequestDTO();
        request.setBbsId("BBSMSTR_000000000001");
        request.setNttId(1L);
        request.setAtchFileId("FILE_FROM_CLIENT");

        LoginVO user = new LoginVO();
        user.setUniqId("USRCNFRM_00000000001");

        // when
        egovBBSManageService.deleteBoardArticle(request, user);

        // then
        ArgumentCaptor<FileVO> captor = ArgumentCaptor.forClass(FileVO.class);
        verify(fileService).deleteAllFileInf(captor.capture());
        assertEquals("FILE_STORED", captor.getValue().getAtchFileId());
    }

    @Test
    @DisplayName("요청에 첨부파일 그룹이 비어 있어도 저장된 첨부는 정리된다")
    void testDeleteBoardArticle_CleansUpWhenRequestOmitsAtchFileId() throws Exception {
        // given: 요청에는 atchFileId 가 없지만 저장된 게시물에는 첨부가 있다.
        BoardVO stored = new BoardVO();
        stored.setAtchFileId("FILE_STORED");
        when(bbsMngDAO.selectBoardArticle(any(BoardVO.class))).thenReturn(stored);

        BbsManageDeleteBoardRequestDTO request = new BbsManageDeleteBoardRequestDTO();
        request.setBbsId("BBSMSTR_000000000001");
        request.setNttId(1L);

        LoginVO user = new LoginVO();
        user.setUniqId("USRCNFRM_00000000001");

        // when
        egovBBSManageService.deleteBoardArticle(request, user);

        // then
        ArgumentCaptor<FileVO> captor = ArgumentCaptor.forClass(FileVO.class);
        verify(fileService).deleteAllFileInf(captor.capture());
        assertEquals("FILE_STORED", captor.getValue().getAtchFileId());
    }
}
