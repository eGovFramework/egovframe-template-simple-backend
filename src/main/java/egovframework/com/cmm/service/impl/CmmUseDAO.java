package egovframework.com.cmm.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.service.CmmnDetailCode;

/**
 * @Class Name : CmmUseDAO.java
 * @Description : 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기위한 데이터 접근 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.   이삼섭       최초 생성
 *    2026. 05. 20   dasomel     인터페이스 및 @EgovMapper 적용
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */
@EgovMapper("cmmUseDAO")
public interface CmmUseDAO {

	/**
	 * 주어진 조건에 따른 공통코드를 불러온다.
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Results(id = "CmmCodeDetail", value = {
		@Result(property = "codeId", column = "CODE_ID"),
		@Result(property = "code", column = "CODE"),
		@Result(property = "codeNm", column = "CODE_NM"),
		@Result(property = "codeDc", column = "CODE_DC")
	})
	@Select("SELECT CODE_ID, CODE, CODE_NM, CODE_DC "
			+ "FROM LETTCCMMNDETAILCODE "
			+ "WHERE USE_AT = 'Y' "
			+ "AND CODE_ID = #{codeId}")
	List<CmmnDetailCode> selectCmmCodeDetail(ComDefaultCodeVO vo) throws Exception;

	/**
	 * 공통코드로 사용할 조직정보를 불러온다.
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Select("<script>"
			+ "SELECT #{tableNm} CODE_ID, ORGNZT_ID CODE, ORGNZT_NM CODE_NM, ORGNZT_DC CODE_DC "
			+ "FROM LETTNORGNZTINFO "
			+ "WHERE 1=1 "
			+ "<if test=\"haveDetailCondition == 'Y'\">"
			+ "AND ORGNZT_ID LIKE #{detailConditionValue} "
			+ "</if>"
			+ "</script>")
	@ResultMap("CmmCodeDetail")
	List<CmmnDetailCode> selectOgrnztIdDetail(ComDefaultCodeVO vo) throws Exception;

	/**
	 * 공통코드로 사용할그룹정보를 불러온다.
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Select("<script>"
			+ "SELECT #{tableNm} CODE_ID, GROUP_ID CODE, GROUP_NM CODE_NM, GROUP_DC CODE_DC "
			+ "FROM LETTNAUTHORGROUPINFO "
			+ "WHERE 1=1 "
			+ "<if test=\"haveDetailCondition == 'Y'\">"
			+ "AND GROUP_ID LIKE #{detailConditionValue} "
			+ "</if>"
			+ "</script>")
	@ResultMap("CmmCodeDetail")
	List<CmmnDetailCode> selectGroupIdDetail(ComDefaultCodeVO vo) throws Exception;
}
