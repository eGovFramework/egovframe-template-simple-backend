package egovframework.let.cop.bbs.domain.repository;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.model.BoardVO;

/**
 * 게시물 관리를 위한 Mapper 인터페이스
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009.03.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.19  이삼섭          최초 생성
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@EgovMapper
public interface BBSManageMapper {

    Long selectMaxNttId();

    void insertBoardArticle(Board board);

    void replyBoardArticle(Board board);

    Long getParentNttNo(Board board);

    void updateOtherNttNo(Board board);

    void updateNttNo(Board board);

    BoardVO selectBoardArticle(BoardVO boardVO);

    List<BoardVO> selectBoardArticleList(BoardVO boardVO);

    Integer selectBoardArticleListCnt(BoardVO boardVO);

    void updateBoardArticle(Board board);

    void deleteBoardArticle(Board board);

    void updateInqireCo(BoardVO boardVO);

    Integer selectMaxInqireCo(BoardVO boardVO);

    List<BoardVO> selectNoticeListForSort(Board board);

    void updateSortOrder(BoardVO boardVO);

    Long selectNoticeItemForSort(Board board);

    List<BoardVO> selectGuestList(BoardVO boardVO);

    Integer selectGuestListCnt(BoardVO boardVO);

    void deleteGuestList(BoardVO boardVO);

    String getPasswordInf(Board board);
}
