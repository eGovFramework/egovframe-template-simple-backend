package egovframework.let.cop.bbs.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import egovframework.com.cmm.web.EgovFileDownloadController;
import egovframework.let.cop.bbs.dto.request.BbsManageDetailBoardRequestDTO;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageDetailResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageListItemResponseDTO;
import egovframework.let.cop.bbs.dto.response.BbsManageListResponseDTO;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.domain.repository.BBSManageDAO;
import egovframework.let.cop.bbs.dto.request.BbsManageDeleteBoardRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.utl.fcc.service.EgovDateUtil;
import lombok.RequiredArgsConstructor;

/**
 * 게시물 관리를 위한 서비스 구현 클래스
 * @author 공통 서비스 개발팀 한성곤
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
 *  2025.06.16  김재섭(nirsa)   서비스 로직 이동 및 생성자 주입 방식 변경
 *
 *  </pre>
 */
@Service("EgovBBSManageService")
@RequiredArgsConstructor
public class EgovBBSManageServiceImpl extends EgovAbstractServiceImpl implements EgovBBSManageService {
	private final BBSManageDAO bbsMngDAO;
	private final EgovFileMngService fileService;
	private final EgovCryptoService cryptoService;


	/**
	 * 게시물 한 건을 삭제 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#deleteBoardArticle(egovframework.let.cop.bbs.domain.model.brd.service.Board)
	 */
	@Override
	public void deleteBoardArticle(BbsManageDeleteBoardRequestDTO bbsDeleteBoardRequestDTO, LoginVO user) throws Exception {
		String atchFileId = bbsDeleteBoardRequestDTO.getAtchFileId();
		BoardVO vo = bbsDeleteBoardRequestDTO.toBoardMaster(bbsDeleteBoardRequestDTO, user.getUniqId());
		bbsMngDAO.deleteBoardArticle(vo);
		
		// 작성자 외 삭제 불가능하도록 기능 개선 필요
		if (atchFileId != null && !atchFileId.trim().isEmpty()) {
	        FileVO fvo = new FileVO();
	        fvo.setAtchFileId(atchFileId);
	        fileService.deleteAllFileInf(fvo);
	    }
	}

	/**
	 * 게시판에 게시물 또는 답변 게시물을 등록 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#insertBoardArticle(egovframework.let.cop.bbs.domain.model.brd.service.Board)
	 */
	@Override
	public void insertBoardArticle(Board board) throws Exception {
		// SORT_ORDR는 부모글의 소트 오더와 같게, NTT_NO는 순서대로 부여

		if ("Y".equals(board.getReplyAt())) {
			// 답글인 경우 1. Parnts를 세팅, 2.Parnts의 sortOrdr을 현재글의 sortOrdr로 가져오도록, 3.nttNo는 현재 게시판의 순서대로
			// replyLc는 부모글의 ReplyLc + 1

			@SuppressWarnings("unused") long tmpNttId = 0L; // 답글 게시물 ID

			tmpNttId = bbsMngDAO.replyBoardArticle(board);

		} else {
			// 답글이 아닌경우 Parnts = 0, replyLc는 = 0, sortOrdr = nttNo(Query에서 처리)
			board.setParnts("0");
			board.setReplyLc("0");
			board.setReplyAt("N");

			bbsMngDAO.insertBoardArticle(board);
		}
	}

	/**
	 * 게시물 대하여 상세 내용을 조회 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#selectBoardArticle(egovframework.let.cop.bbs.domain.model.brd.service.BoardVO)
	 */
	@Override
	public BbsManageDetailResponseDTO selectBoardArticle(BbsManageDetailBoardRequestDTO bbsManageDetailBoardRequestDTO) throws Exception {
		BoardVO boardVO = bbsManageDetailBoardRequestDTO.toBoardVO();

		if (boardVO.isPlusCount()) {
			int iniqireCo = bbsMngDAO.selectMaxInqireCo(boardVO);

			boardVO.setInqireCo(iniqireCo);
			bbsMngDAO.updateInqireCo(boardVO);
		}

		BoardVO vo = bbsMngDAO.selectBoardArticle(boardVO);
		BbsManageDetailResponseDTO bbsManageDetailResponseDTO;

		// 2021-06-01 신용호 추가
		// 첨부파일 확인
		if (vo != null && vo.getAtchFileId() != null && !vo.getAtchFileId().isEmpty()) {
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(vo.getAtchFileId());
			List<FileVO> resultFiles = fileService.selectFileInfs(fileVO);

			// FileId를 유추하지 못하도록 암호화하여 표시한다. (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
			for (FileVO file : resultFiles) {
				String toEncrypt = file.atchFileId;
				file.setAtchFileId(Base64.getEncoder().encodeToString(cryptoService.encrypt(toEncrypt.getBytes(), EgovFileDownloadController.ALGORITM_KEY)));
			}
			bbsManageDetailResponseDTO = BbsManageDetailResponseDTO.from(vo, resultFiles);
		} else {
			bbsManageDetailResponseDTO = BbsManageDetailResponseDTO.from(vo);
		}

		return bbsManageDetailResponseDTO;
	}

	/**
	 * 조건에 맞는 게시물 목록을 조회 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#selectBoardArticles(egovframework.let.cop.bbs.domain.model.brd.service.BoardVO)
	 */
	@Override
	public BbsManageListResponseDTO selectBoardArticles(BbsSearchRequestDTO bbsSearchRequestDTO, PaginationInfo paginationInfo, String attrbFlag) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setPageIndex(bbsSearchRequestDTO.getPageIndex());
		boardVO.setBbsId(bbsSearchRequestDTO.getBbsId());
		boardVO.setSearchCnd(bbsSearchRequestDTO.getSearchCnd());
		boardVO.setSearchWrd(bbsSearchRequestDTO.getSearchWrd());

		boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<BoardVO> list = bbsMngDAO.selectBoardArticleList(boardVO);
		List<BoardVO> result = new ArrayList<BoardVO>();
		if ("BBSA01".equals(attrbFlag)) {
			// 유효게시판 임
			String today = EgovDateUtil.getToday();

			BoardVO vo;
			Iterator<BoardVO> iter = list.iterator();
			while (iter.hasNext()) {
				vo = iter.next();

				if (!"".equals(vo.getNtceBgnde()) || !"".equals(vo.getNtceEndde())) {
					if (EgovDateUtil.getDaysDiff(today, vo.getNtceBgnde()) > 0
						|| EgovDateUtil.getDaysDiff(today, vo.getNtceEndde()) < 0) {
						// 시작일이 오늘날짜보다 크거나, 종료일이 오늘 날짜보다 작은 경우
						vo.setIsExpired("Y");
					}
				}
				result.add(vo);
			}
		} else {
			result = list;
		}

		int cnt = bbsMngDAO.selectBoardArticleListCnt(boardVO);

		List<BbsManageListItemResponseDTO> dtoList = result.stream()
				.map(BbsManageListItemResponseDTO::from)
				.collect(Collectors.toList());

		return BbsManageListResponseDTO.builder()
				.resultList(dtoList)
				.resultCnt(cnt)
				.build();
	}

	/**
	 * 게시물 한 건의 내용을 수정 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#updateBoardArticle(egovframework.let.cop.bbs.domain.model.brd.service.Board)
	 */
	@Override
	public void updateBoardArticle(Board board) throws Exception {
		bbsMngDAO.updateBoardArticle(board);
	}

	/**
	 * 방명록 내용을 삭제 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#deleteGuestList(egovframework.let.cop.bbs.domain.model.brd.service.BoardVO)
	 */
	@Override
	public void deleteGuestList(BoardVO boardVO) throws Exception {
		bbsMngDAO.deleteGuestList(boardVO);
	}

	/**
	 * 방명록에 대한 목록을 조회 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#selectGuestList(egovframework.let.cop.bbs.domain.model.brd.service.BoardVO)
	 */
	@Override
	public Map<String, Object> selectGuestList(BoardVO boardVO) throws Exception {
		List<BoardVO> result = bbsMngDAO.selectGuestList(boardVO);
		int cnt = bbsMngDAO.selectGuestListCnt(boardVO);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}

	/**
	 * 방명록에 대한 패스워드를 조회 한다.
	 *
	 * @param board
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getPasswordInf(Board board) throws Exception {
		return bbsMngDAO.getPasswordInf(board);
	}
}
