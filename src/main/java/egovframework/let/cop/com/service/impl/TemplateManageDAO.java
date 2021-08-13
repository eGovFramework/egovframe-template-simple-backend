package egovframework.let.cop.com.service.impl;
import java.util.List;

import egovframework.let.cop.com.service.TemplateInf;
import egovframework.let.cop.com.service.TemplateInfVO;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import org.springframework.stereotype.Repository;

/**
 * 템플릿 정보관리를 위한 데이터 접근 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.03.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.17  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Repository("TemplateManageDAO")
public class TemplateManageDAO extends EgovAbstractMapper {

    /**
     * 템플릿 정보를 삭제한다.
     *
     * @param tmplatInf
     * @throws Exception
     */
    public void deleteTemplateInf(TemplateInf tmplatInf) throws Exception {
	update("TemplateManageDAO.deleteTemplateInf", tmplatInf);
    }

    /**
     * 템플릿 정보를 등록한다.
     *
     * @param tmplatInf
     * @throws Exception
     */
    public void insertTemplateInf(TemplateInf tmplatInf) throws Exception {
	insert("TemplateManageDAO.insertTemplateInf", tmplatInf);
    }

    /**
     * 템플릿 정보를 수정한다.
     *
     * @param tmplatInf
     * @throws Exception
     */
    public void updateTemplateInf(TemplateInf tmplatInf) throws Exception {
	update("TemplateManageDAO.updateTemplateInf", tmplatInf);
    }

    /**
     * 템플릿에 대한 목록를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TemplateInfVO> selectTemplateInfs(TemplateInfVO tmplatInfVO) throws Exception {
	return (List<TemplateInfVO>) list("TemplateManageDAO.selectTemplateInfs", tmplatInfVO);
    }

    /**
     * 템플릿에 대한 목록 전체 건수를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    public int selectTemplateInfsCnt(TemplateInfVO tmplatInfVO) throws Exception {
	return (Integer)selectOne("TemplateManageDAO.selectTemplateInfsCnt", tmplatInfVO);
    }

    /**
     * 템플릿에 대한 상세정보를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    public TemplateInfVO selectTemplateInf(TemplateInfVO tmplatInfVO) throws Exception {
	return (TemplateInfVO)selectOne("TemplateManageDAO.selectTemplateInf", tmplatInfVO);

    }

    /**
     * 템플릿에 대한 미리보기 정보를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    public TemplateInfVO selectTemplatePreview(TemplateInfVO tmplatInfVO) throws Exception {
	return null;
    }

    /**
     * 템플릿 구분에 따른 목록을 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TemplateInfVO> selectTemplateInfsByCode(TemplateInfVO tmplatInfVO) throws Exception {
	return (List<TemplateInfVO>) list("TemplateManageDAO.selectTemplateInfsByCode", tmplatInfVO);
    }

}
