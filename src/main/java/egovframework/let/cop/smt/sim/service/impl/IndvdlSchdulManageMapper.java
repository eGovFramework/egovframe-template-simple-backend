package egovframework.let.cop.smt.sim.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

/**
 * 일정관리를 처리하는 매퍼 인터페이스
 * @since 2009.04.10
 * @see
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  장동한          최초 생성
 *
 * </pre>
 * @author 조재영
 * @version 1.0
 */
@EgovMapper
public interface IndvdlSchdulManageMapper {

    /**
     * 메인페이지/일정관리조회 목록을 Map 형식으로 조회한다.
     *
     * @param map - 조회할 정보가 담긴 Map
     * @return List
     * @throws Exception
     */
    List<?> selectIndvdlSchdulManageMainList(Map<?, ?> map) throws Exception;

    /**
     * 일정 목록을 Map 형식으로 조회한다.
     *
     * @param map - 조회할 정보가 담긴 Map
     * @return List
     * @throws Exception
     */
    List<?> selectIndvdlSchdulManageRetrieve(Map<?, ?> map) throws Exception;

    /**
     * 일정 목록을 VO 형식으로 조회한다.
     *
     * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
     * @return IndvdlSchdulManageVO
     * @throws Exception
     */
    IndvdlSchdulManageVO selectIndvdlSchdulManageDetailVO(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception;

    /**
     * 일정 목록을 조회한다.
     *
     * @param searchVO - 조회할 정보가 담긴 VO
     * @return List
     * @throws Exception
     */
    List<?> selectIndvdlSchdulManage(ComDefaultVO searchVO) throws Exception;

    /**
     * 일정 목록 전체 건수를 조회한다.
     *
     * @param searchVO - 조회할 정보가 담긴 VO
     * @return int
     * @throws Exception
     */
    int selectIndvdlSchdulManageCnt(ComDefaultVO searchVO) throws Exception;

    /**
     * 일정를(을) 등록한다.
     *
     * @param indvdlSchdulManageVO - 일정 정보 담김 VO
     * @throws Exception
     */
    void insertIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception;

    /**
     * 일정를(을) 수정한다.
     *
     * @param indvdlSchdulManageVO - 일정 정보 담김 VO
     * @throws Exception
     */
    void updateIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception;

    /**
     * 일정를(을) 삭제한다.
     *
     * @param indvdlSchdulManageVO - 일정 정보 담김 VO
     * @throws Exception
     */
    void deleteIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception;
}
