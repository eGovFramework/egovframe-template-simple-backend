package egovframework.let.cop.com.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.let.cop.com.service.EgovTemplateManageService;
import egovframework.let.cop.com.service.TemplateInf;
import egovframework.let.cop.com.service.TemplateInfVO;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 템플릿 정보관리를 위한 서비스 구현 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.3.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.3.17  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성 
 *
 * </pre>
 */
@Service("EgovTemplateManageService")
public class EgovTemplateManageServiceImpl extends EgovAbstractServiceImpl implements EgovTemplateManageService {

    @Resource(name = "TemplateManageDAO")
    private TemplateManageDAO tmplatDAO;

    @Resource(name = "egovTmplatIdGnrService")
    private EgovIdGnrService idgenService;

    /**
     * 템플릿 정보를 삭제한다.
     * 
     * @see egovframework.let.cop.bbs.com.service.EgovTemplateManageService#deleteTemplateInf(egovframework.let.cop.bbs.com.service.TemplateInf)
     */
    public void deleteTemplateInf(TemplateInf tmplatInf) throws Exception {
	tmplatDAO.deleteTemplateInf(tmplatInf);
    }

    /**
     * 템플릿 정보를 등록한다.
     * 
     * @see egovframework.let.cop.bbs.com.service.EgovTemplateManageService#insertTemplateInf(egovframework.let.cop.bbs.com.service.TemplateInf)
     */
    public void insertTemplateInf(TemplateInf tmplatInf) throws Exception {

	tmplatInf.setTmplatId(idgenService.getNextStringId());

	tmplatDAO.insertTemplateInf(tmplatInf);
    }

    /**
     * 템플릿에 대한 상세정보를 조회한다.
     * 
     * @see egovframework.let.cop.bbs.com.service.EgovTemplateManageService#selectTemplateInf(egovframework.let.cop.bbs.com.service.TemplateInfVO)
     */
    public TemplateInfVO selectTemplateInf(TemplateInfVO tmplatInfVO) throws Exception {
	TemplateInfVO vo = new TemplateInfVO();
	vo = tmplatDAO.selectTemplateInf(tmplatInfVO);
	return vo;
    }

    /**
     * 템플릿에 대한 목록를 조회한다.
     * 
     * @see egovframework.let.cop.bbs.com.service.EgovTemplateManageService#selectTemplateInfs(egovframework.let.cop.bbs.com.service.TemplateInfVO)
     */
    public Map<String, Object> selectTemplateInfs(TemplateInfVO tmplatInfVO) throws Exception {
	List<TemplateInfVO> result = tmplatDAO.selectTemplateInfs(tmplatInfVO);
	int cnt = tmplatDAO.selectTemplateInfsCnt(tmplatInfVO);
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	map.put("resultList", result);
	map.put("resultCnt", Integer.toString(cnt));

	return map;
    }

    /**
     * 템플릿에 대한 미리보기 정보를 조회한다.
     * 
     * @see egovframework.let.cop.bbs.com.service.EgovTemplateManageService#selectTemplatePreview(egovframework.let.cop.bbs.com.service.TemplateInfVO)
     */
    public TemplateInfVO selectTemplatePreview(TemplateInfVO tmplatInfVO) throws Exception {
	TemplateInfVO vo = new TemplateInfVO();
	
	vo = tmplatDAO.selectTemplatePreview(tmplatInfVO);
	
	return vo;
    }

    /**
     * 템플릿 정보를 수정한다.
     * 
     * @see egovframework.let.cop.bbs.com.service.EgovTemplateManageService#updateTemplateInf(egovframework.let.cop.bbs.com.service.TemplateInf)
     */
    public void updateTemplateInf(TemplateInf tmplatInf) throws Exception {
	tmplatDAO.updateTemplateInf(tmplatInf);
    }

    /**
     * 템플릿 구분에 따른 목록을 조회한다.
     * 
     * @see egovframework.let.cop.bbs.com.service.EgovTemplateManageService#selectAllTemplateInfs(egovframework.let.cop.bbs.com.service.TemplateInfVO)
     */
    public List<TemplateInfVO> selectTemplateInfsByCode(TemplateInfVO tmplatInfVO) throws Exception {
	return tmplatDAO.selectTemplateInfsByCode(tmplatInfVO);
    }
}
