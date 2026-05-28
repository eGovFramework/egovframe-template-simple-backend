package egovframework.let.uss.umt.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.let.uss.umt.service.MberManageVO;
import egovframework.let.uss.umt.service.UserDefaultVO;

/**
 * 일반회원관리에 관한 매퍼 인터페이스를 정의한다.
 * @author 공통서비스 개발팀 조재영
 * @since 2009.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  JJY            최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *   2024.07.23  김일국          스프링부트용으로 커스터마이징
 * </pre>
 */
@EgovMapper
public interface MberManageMapper {

	/**
	 * 기 등록된 특정 일반회원의 정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param userSearchVO 검색조건
	 * @return List<MberManageVO> 일반회원 목록정보
	 */
	List<MberManageVO> selectMberList(UserDefaultVO userSearchVO);

	/**
	 * 일반회원 총 갯수를 조회한다.
	 * @param userSearchVO 검색조건
	 * @return int 일반회원총갯수
	 */
	int selectMberListTotCnt(UserDefaultVO userSearchVO);

	/**
	 * 화면에 조회된 일반회원의 정보를 데이터베이스에서 삭제
	 * @param delId 삭제 대상 일반회원아이디
	 */
	void deleteMber_S(String delId);

	/**
	 * 일반회원의 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param mberManageVO 일반회원 등록정보
	 * @return int 등록결과
	 */
	int insertMber_S(MberManageVO mberManageVO);

	/**
	 * 기 등록된 사용자 중 검색조건에 맞는 일반회원의 정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param mberId 상세조회대상 일반회원아이디
	 * @return MberManageVO 일반회원 상세정보
	 */
	MberManageVO selectMber_S(String mberId);

	/**
	 * 화면에 조회된 일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param mberManageVO 일반회원수정정보
	 */
	void updateMber_S(MberManageVO mberManageVO);

	/**
	 * 일반회원 약관확인
	 * @param stplatId 일반회원약관아이디
	 * @return List 일반회원약관정보
	 */
	List<?> selectStplat_S(String stplatId);

	/**
	 * 일반회원 암호수정
	 * @param passVO 일반회원수정정보(비밀번호)
	 */
	void updatePassword_S(MberManageVO passVO);

	/**
	 * 일반회원이 비밀번호를 기억하지 못할 때 비밀번호를 찾을 수 있도록 함
	 * @param mberManageVO 일반회원암호 조회조건정보
	 * @return MberManageVO 일반회원 암호정보
	 */
	MberManageVO selectPassword_S(MberManageVO mberManageVO);

	/**
	 * 입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param checkId 중복체크대상 아이디
	 * @return int 사용가능여부(아이디 사용회수)
	 */
	int checkIdDplct_S(String checkId);

}
