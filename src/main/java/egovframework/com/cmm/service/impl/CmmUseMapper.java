package egovframework.com.cmm.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.service.CmmnDetailCode;

/**
 * @Class Name : CmmUseMapper.java
 * @Description : 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기위한 매퍼 인터페이스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */
@EgovMapper
public interface CmmUseMapper {

    /**
     * 주어진 조건에 따른 공통코드를 불러온다.
     *
     * @param vo
     * @return
     * @throws Exception
     */
    List<CmmnDetailCode> selectCmmCodeDetail(ComDefaultCodeVO vo) throws Exception;

    /**
     * 공통코드로 사용할 조직정보를 불러온다.
     *
     * @param vo
     * @return
     * @throws Exception
     */
    List<CmmnDetailCode> selectOgrnztIdDetail(ComDefaultCodeVO vo) throws Exception;

    /**
     * 공통코드로 사용할 그룹정보를 불러온다.
     *
     * @param vo
     * @return
     * @throws Exception
     */
    List<CmmnDetailCode> selectGroupIdDetail(ComDefaultCodeVO vo) throws Exception;

}
