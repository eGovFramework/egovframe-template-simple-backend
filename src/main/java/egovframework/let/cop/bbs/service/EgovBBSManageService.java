package egovframework.let.cop.bbs.service;

import java.util.Map;

import egovframework.com.cmm.LoginVO;
import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.dto.request.BbsManageDeleteBoardRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsManageDetailBoardRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailItemResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageListResponseDTO;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 게시물 관리를 위한 서비스 인터페이스  클래스
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
public interface EgovBBSManageService {

	/**
	 * 게시물 한 건을 삭제 한다.
	 * 
	 * @param Board
	 * @exception Exception Exception
	 */
	public void deleteBoardArticle(BbsManageDeleteBoardRequestDTO bbsDeleteBoardRequestDTO, LoginVO user)
	  throws Exception;

	/**
	 * 방명록 내용을 삭제 한다.
	 * 
	 * @param boardVO
	 * @exception Exception Exception
	 */
	public void deleteGuestList(BoardVO boardVO)
	  throws Exception;

	/**
	 * 방명록에 대한 패스워드를 조회 한다.
	 * @return
	 * 
	 * @param Board
	 * @exception Exception Exception
	 */
	public String getPasswordInf(Board Board)
	  throws Exception;

	/**
	 * 게시판에 게시물 또는 답변 게시물을 등록 한다.
	 * 
	 * @param Board
	 * @exception Exception Exception
	 */
	public void insertBoardArticle(Board Board)
	  throws Exception;

	/**
	 * 게시물 대하여 상세 내용을 조회 한다.
	 * @return
	 * 
	 * @param bbsManageDetailBoardRequestDTO
	 * @exception Exception Exception
	 */
	public BbsManageDetailResponseDTO selectBoardArticle(BbsManageDetailBoardRequestDTO bbsManageDetailBoardRequestDTO)
	  throws Exception;

	/**
	 * 조건에 맞는 게시물 목록을 조회 한다.
	 * @return
	 * 
	 * @param bbsSearchRequestDTO
	 * @param paginationInfo
	 * @param attrbFlag
	 * @exception Exception Exception
	 */
	public BbsManageListResponseDTO selectBoardArticles(BbsSearchRequestDTO bbsSearchRequestDTO, PaginationInfo paginationInfo, String attrbFlag)
	  throws Exception;

	/**
	 * 방명록에 대한 목록을 조회 한다.
	 * @return
	 * 
	 * @param boardVO
	 * @exception Exception Exception
	 */
	public Map<String, Object> selectGuestList(BoardVO boardVO)
	  throws Exception;

	/**
	 * 게시물 한 건의 내용을 수정 한다.
	 * 
	 * @param Board
	 * @exception Exception Exception
	 */
	public void updateBoardArticle(Board Board)
	  throws Exception;

}