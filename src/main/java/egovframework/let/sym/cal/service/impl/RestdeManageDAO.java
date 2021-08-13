package egovframework.let.sym.cal.service.impl;

import java.util.List;

import egovframework.let.sym.cal.service.Restde;
import egovframework.let.sym.cal.service.RestdeVO;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import org.springframework.stereotype.Repository;

/**
 *
 * 휴일에 대한 데이터 접근 클래스를 정의한다
 * @author 공통서비스 개발팀 이중호
 * @since 2009.04.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.01  이중호          최초 생성
 *
 * </pre>
 */
@Repository("RestdeManageDAO")
public class RestdeManageDAO extends EgovAbstractMapper {

	/**
	 * 일반달력 팝업 정보를 조회한다.
	 * @param restde
	 * @return List(일반달력 팝업 날짜정보)
	 * @throws Exception
	 */
	public List<?> selectNormalRestdePopup(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectNormalRestdePopup", restde);
	}

	/**
	 * 행정달력 팝업 정보를 조회한다.
	 * @param restde
	 * @return List(행정달력 팝업 날짜정보)
	 * @throws Exception
	 */
	public List<?> selectAdministRestdePopup(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectAdministRestdePopup", restde);
	}

	/**
	 * 일반달력 일간 정보를 조회한다.
	 * @param restde
	 * @return List(일반달력 일간 날짜정보)
	 * @throws Exception
	 */
	public List<?> selectNormalDayCal(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectNormalDayCal", restde);
	}

	/**
	 * 일반달력 일간 휴일을 조회한다.
	 * @param restde
	 * @return List(일반달력 일간 휴일정보)
	 * @throws Exception
	 */
	public List<?> selectNormalDayRestde(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectNormalDayRestde", restde);
	}

	/**
	 * 일반달력 월간 휴일을 조회한다.
	 * @param restde
	 * @return List(일반달력 월간 휴일정보)
	 * @throws Exception
	 */
	public List<?> selectNormalMonthRestde(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectNormalMonthRestde", restde);
	}

	/**
	 * 행정달력 일간 정보를 조회한다.
	 * @param restde
	 * @return List(행정달력 일간 날짜정보)
	 * @throws Exception
	 */
	public List<?> selectAdministDayCal(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectAdministDayCal", restde);
	}

	/**
	 * 행정달력 일간 휴일을 조회한다.
	 * @param restde
	 * @return List(행정달력 일간 휴일정보)
	 * @throws Exception
	 */
	public List<?> selectAdministDayRestde(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectAdministDayRestde", restde);
	}

	/**
	 * 행정달력 월간 휴일을 조회한다.
	 * @param restde
	 * @return List(행정달력 월간 휴일정보)
	 * @throws Exception
	 */
	public List<?> selectAdministMonthRestde(Restde restde) throws Exception {
        return list("RestdeManageDAO.selectAdministMonthRestde", restde);
	}

	/**
	 * 휴일을 삭제한다.
	 * @param restde
	 * @throws Exception
	 */
	public void deleteRestde(Restde restde) throws Exception {
		delete("RestdeManageDAO.deleteRestde", restde);
	}


	/**
	 * 휴일을 등록한다.
	 * @param restde
	 * @throws Exception
	 */
	public void insertRestde(Restde restde) throws Exception {
        insert("RestdeManageDAO.insertRestde", restde);
	}

	/**
	 * 휴일 상세항목을 조회한다.
	 * @param restde
	 * @return Restde(휴일)
	 * @throws Exception
	 */
	public Restde selectRestdeDetail(Restde restde) throws Exception {
		return (Restde) selectOne("RestdeManageDAO.selectRestdeDetail", restde);
	}


    /**
     * 휴일 목록을 조회한다.
     * @param searchVO
	 * @return List(휴일 목록)
     * @throws Exception
     */
	public List<?> selectRestdeList(RestdeVO searchVO) throws Exception {
        return list("RestdeManageDAO.selectRestdeList", searchVO);
    }

    /**
     * 글 총 갯수를 조회한다.
     * @param searchVO
     * @return int(휴일 총 갯수)
     * @throws Exception
     */
    public int selectRestdeListTotCnt(RestdeVO searchVO) throws Exception {
        return (Integer)selectOne("RestdeManageDAO.selectRestdeListTotCnt", searchVO);
    }

	/**
	 * 휴일을 수정한다.
	 * @param restde
	 * @throws Exception
	 */
	public void updateRestde(Restde restde) throws Exception {
		update("RestdeManageDAO.updateRestde", restde);
	}

}
