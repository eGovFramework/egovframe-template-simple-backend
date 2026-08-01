package egovframework.let.cop.smt.sim.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 일정관리를 처리하는 Dao Class 구현
 * @since 2009.04.10
 * @see
 * <pre>
 * << 개정이력(Modification Information) >>  수정일      수정자           수정내용 -------    ---
 * -----    --------------------------- 2009.04.10  장동한          최초 생성 2011.05.31
 * JJY           경량환경 커스터마이징버전 생성
 *   2026-08-01  이백행          [2026년 컨트리뷰션] 불필요한 예외 제거
 * </pre>
 * @author 조재영
 * @version 1.0
 * @created 09-6-2011 오전 10:08:07
 */
@Repository("indvdlSchdulManageDao")
public class IndvdlSchdulManageDao extends EgovAbstractMapper {

	/**
	 * 메인페이지/일정관리조회 목록을 Map(map)형식으로 조회한다.
	 * @param Map(map) - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	public List<?> selectIndvdlSchdulManageMainList(Map<?, ?> map) {
		return selectList("IndvdlSchdulManage.selectIndvdlSchdulManageMainList", map);
	}

	/**
	 * 일정 목록을 Map(map)형식으로 조회한다.
	 * @param Map(map) - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	public List<?> selectIndvdlSchdulManageRetrieve(Map<?, ?> map) {
		return selectList("IndvdlSchdulManage.selectIndvdlSchdulManageRetrieve", map);
	}

	/**
	 * 일정 목록을 VO(model)형식으로 조회한다.
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 * @return IndvdlSchdulManageVO
	 * @throws Exception
	 */
	public IndvdlSchdulManageVO selectIndvdlSchdulManageDetailVO(IndvdlSchdulManageVO indvdlSchdulManageVO)
 {
		return (IndvdlSchdulManageVO)selectOne("IndvdlSchdulManage.selectIndvdlSchdulManageDetailVO",
			indvdlSchdulManageVO);
	}

	/**
	 * 일정 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	public List<?> selectIndvdlSchdulManageList(ComDefaultVO searchVO) {
		return selectList("IndvdlSchdulManage.selectIndvdlSchdulManage", searchVO);
	}

	/**
	 * 일정를(을) 상세조회 한다.
	 * @param indvdlSchdulManageVO - 일정 정보 담김 VO
	 * @return List
	 * @throws Exception
	 */
	//	public List<?> selectIndvdlSchdulManageDetail(IndvdlSchdulManageVO indvdlSchdulManageVO){
	//		return list("IndvdlSchdulManage.selectIndvdlSchdulManageDetail", indvdlSchdulManageVO);
	//}
	public IndvdlSchdulManageVO selectIndvdlSchdulManageDetail(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		return selectOne("IndvdlSchdulManage.selectIndvdlSchdulManageDetailVO", indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 목록 전체 건수를(을) 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return int
	 * @throws Exception
	 */
	public int selectIndvdlSchdulManageListCnt(ComDefaultVO searchVO) {
		return (Integer)selectOne("IndvdlSchdulManage.selectIndvdlSchdulManageCnt", searchVO);
	}

	/**
	 * 일정를(을) 등록한다.
	 * @param qindvdlSchdulManageVO - 일정 정보 담김 VO
	 * @throws Exception
	 */
	public void insertIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		insert("IndvdlSchdulManage.insertIndvdlSchdulManage", indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 수정한다.
	 * @param indvdlSchdulManageVO - 일정 정보 담김 VO
	 * @throws Exception
	 */
	public void updateIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		insert("IndvdlSchdulManage.updateIndvdlSchdulManage", indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 삭제한다.
	 * @param indvdlSchdulManageVO - 일정 정보 담김 VO
	 * @throws Exception
	 */
	public void deleteIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		// 일지 삭제
		//delete("IndvdlSchdulManage.deleteDiaryManage", indvdlSchdulManageVO);
		// 일정관리 삭제
		delete("IndvdlSchdulManage.deleteIndvdlSchdulManage", indvdlSchdulManageVO);
	}
}
