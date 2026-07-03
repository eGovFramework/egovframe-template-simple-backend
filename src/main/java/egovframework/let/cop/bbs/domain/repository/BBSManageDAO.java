package egovframework.let.cop.bbs.domain.repository;
import java.util.Iterator;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import org.springframework.stereotype.Repository;

import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.model.BoardVO;

/**
 * 게시물 관리를 위한 데이터 접근 클래스
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
@Repository("BBSManageDAO")
public class BBSManageDAO extends EgovAbstractMapper {

    /**
     * 게시판에 게시물을 등록 한다.
     *
     * @param board
     */
    public void insertBoardArticle(Board board) {
	long nttId = (Long)selectOne("BBSManageDAO.selectMaxNttId");
	board.setNttId(nttId);

	insert("BBSManageDAO.insertBoardArticle", board);
    }

    /**
     * 게시판에 답변 게시물을 등록 한다.
     *
     * @param board
     */
    public long replyBoardArticle(Board board) {
	long nttId = (Long)selectOne("BBSManageDAO.selectMaxNttId");
	board.setNttId(nttId);

	insert("BBSManageDAO.replyBoardArticle", board);

	//----------------------------------------------------------
	// 현재 글 이후 게시물에 대한 NTT_NO를 증가 (정렬을 추가하기 위해)
	//----------------------------------------------------------
	//String parentId = board.getParnts();
	long nttNo = (Long)selectOne("BBSManageDAO.getParentNttNo", board);

	board.setNttNo(nttNo);
	update("BBSManageDAO.updateOtherNttNo", board);

	board.setNttNo(nttNo + 1);
	update("BBSManageDAO.updateNttNo", board);

	return nttId;
    }

    /**
     * 게시물 한 건에 대하여 상세 내용을 조회 한다.
     *
     * @param boardVO
     * @return
     */
    public BoardVO selectBoardArticle(BoardVO boardVO) {
    	return (BoardVO)selectOne("BBSManageDAO.selectBoardArticle", boardVO);
    }

    /**
     * 조건에 맞는 게시물 목록을 조회 한다.
     *
     * @param boardVO
     * @return
     */
    public List<BoardVO> selectBoardArticleList(BoardVO boardVO) {
		return selectList("BBSManageDAO.selectBoardArticleList", boardVO);
	}

    /**
     * 조건에 맞는 게시물 목록에 대한 전체 건수를 조회 한다.
     *
     * @param boardVO
     * @return
     */
    public int selectBoardArticleListCnt(BoardVO boardVO) {
    	return (Integer)selectOne("BBSManageDAO.selectBoardArticleListCnt", boardVO);
    }

    /**
     * 게시물 한 건의 내용을 수정 한다.
     *
     * @param board
     */
    public void updateBoardArticle(Board board) {
    	update("BBSManageDAO.updateBoardArticle", board);
    }

    /**
     * 게시물 한 건을 삭제 한다.
     *
     * @param board
     */
    public void deleteBoardArticle(Board board) {
    	update("BBSManageDAO.deleteBoardArticle", board);
    }

    /**
     * 게시물에 대한 조회 건수를 수정 한다.
     *
     * @param board
     */
    public void updateInqireCo(BoardVO boardVO) {
    	update("BBSManageDAO.updateInqireCo", boardVO);
    }

    /**
     * 게시물에 대한 현재 조회 건수를 조회 한다.
     *
     * @param boardVO
     * @return
     */
    public int selectMaxInqireCo(BoardVO boardVO) {
    	return (Integer)selectOne("BBSManageDAO.selectMaxInqireCo", boardVO);
    }

    /**
     * 게시판에 대한 목록을 정렬 순서로 조회 한다.
     *
     * @param boardVO
     * @return
     */
    public List<BoardVO> selectNoticeListForSort(Board board) {
		return selectList("BBSManageDAO.selectNoticeListForSort", board);
	}

    /**
     * 게사판에 대한 정렬 순서를 수정 한다.
     *
     * @param sortList
     */
    public void updateSortOrder(List<BoardVO> sortList) {
	BoardVO vo;
	Iterator<BoardVO> iter = sortList.iterator();
	while (iter.hasNext()) {
	    vo = iter.next();
	    update("BBSManageDAO.updateSortOrder", vo);
	}
    }

    /**
     * 게시판에 대한 현재 게시물 번호의 최대값을 구한다.
     *
     * @param boardVO
     * @return
     */
    public long selectNoticeItemForSort(Board board) {
    	return (Long)selectOne("BBSManageDAO.selectNoticeItemForSort", board);
    }

    /**
     * 방명록에 대한 목록을 조회 한다.
     *
     * @param boardVO
     * @return
     */
    public List<BoardVO> selectGuestList(BoardVO boardVO) {
		return selectList("BBSManageDAO.selectGuestList", boardVO);
	}

    /**
     * 방명록에 대한 목록 건수를 조회 한다.
     *
     * @param boardVO
     * @return
     */
    public int selectGuestListCnt(BoardVO boardVO) {
    	return (Integer)selectOne("BBSManageDAO.selectGuestListCnt", boardVO);
    }

    /**
     * 방명록 내용을 삭제 한다.
     *
     * @param boardVO
     */
    public void deleteGuestList(BoardVO boardVO) {
    	update("BBSManageDAO.deleteGuestList", boardVO);
    }

    /**
     * 방명록에 대한 패스워드를 조회 한다.
     *
     * @param board
     * @return
     */
    public String getPasswordInf(Board board) {
    	return (String)selectOne("BBSManageDAO.getPasswordInf", board);
    }
}
