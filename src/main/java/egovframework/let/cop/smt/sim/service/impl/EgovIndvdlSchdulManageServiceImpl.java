package egovframework.let.cop.smt.sim.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.let.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;
import lombok.RequiredArgsConstructor;

/**
 * 일정관리를 처리하는 ServiceImpl Class 구현
 * 
 * @since 2009.04.10
 * @see
 * 
 *      <pre>
 * << 개정이력(Modification Information) >>
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  장동한          최초 생성
 *   2011.05.31  JJY           경량환경 커스터마이징버전 생성
 *   2024.08.27  이백행          컨트리뷰션 롬복 생성자 기반 종속성 주입
 *      </pre>
 * 
 * @author 조재영
 * @version 1.0
 * @created 09-6-2011 오전 10:08:05
 */
@Service
@RequiredArgsConstructor
public class EgovIndvdlSchdulManageServiceImpl extends EgovAbstractServiceImpl
		implements EgovIndvdlSchdulManageService {

	/**
	 * 일정관리를 처리하는 Dao Class 구현
	 */
	private final IndvdlSchdulManageDao indvdlSchdulManageDao;

	/**
	 * Id Generation 서비스의 인터페이스 클래스
	 */
	private final EgovIdGnrService deptSchdulManageIdGnrService;

	/**
	 * 메인페이지/일정관리조회
	 * 
	 * @param map - 조회할 정보가 담긴 map
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<?> selectIndvdlSchdulManageMainList(Map<?, ?> map) throws Exception {
		return indvdlSchdulManageDao.selectIndvdlSchdulManageMainList(map);
	}

	/**
	 * 일정 목록을 Map(map)형식으로 조회한다.
	 * 
	 * @param Map(map) - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<?> selectIndvdlSchdulManageRetrieve(Map<?, ?> map) throws Exception {
		return indvdlSchdulManageDao.selectIndvdlSchdulManageRetrieve(map);
	}

	/**
	 * 일정 목록을 VO(model)형식으로 조회한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public IndvdlSchdulManageVO selectIndvdlSchdulManageDetailVO(IndvdlSchdulManageVO indvdlSchdulManageVO)
			throws Exception {
		return indvdlSchdulManageDao.selectIndvdlSchdulManageDetailVO(indvdlSchdulManageVO);
	}

	/**
	 * 일정 목록을 조회한다.
	 * 
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<?> selectIndvdlSchdulManageList(ComDefaultVO searchVO) throws Exception {
		return indvdlSchdulManageDao.selectIndvdlSchdulManageList(searchVO);
	}

	/**
	 * 일정를(을) 상세조회 한다.
	 * 
	 * @param IndvdlSchdulManage - 회정정보가 담김 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public IndvdlSchdulManageVO selectIndvdlSchdulManageDetail(IndvdlSchdulManageVO indvdlSchdulManageVO)
			throws Exception {
		return indvdlSchdulManageDao.selectIndvdlSchdulManageDetail(indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 목록 전체 건수를(을) 조회한다.
	 * 
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int selectIndvdlSchdulManageListCnt(ComDefaultVO searchVO) throws Exception {
		return indvdlSchdulManageDao.selectIndvdlSchdulManageListCnt(searchVO);
	}

	/**
	 * 일정를(을) 등록한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 * @throws Exception
	 */
	@Override
	public void insertIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception {
		String sMakeId = deptSchdulManageIdGnrService.getNextStringId();
		indvdlSchdulManageVO.setSchdulId(sMakeId);

		indvdlSchdulManageDao.insertIndvdlSchdulManage(indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 수정한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 * @throws Exception
	 */
	@Override
	public void updateIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception {
		indvdlSchdulManageDao.updateIndvdlSchdulManage(indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 삭제한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 * @throws Exception
	 */
	@Override
	public void deleteIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception {
		indvdlSchdulManageDao.deleteIndvdlSchdulManage(indvdlSchdulManageVO);
	}
}
