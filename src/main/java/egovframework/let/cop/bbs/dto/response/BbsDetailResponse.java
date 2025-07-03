package egovframework.let.cop.bbs.dto.response;

/**
 * 게시판 응답 DTO 공통 인터페이스입니다.
 * 
 * 이 인터페이스는 게시판 마스터 정보 조회 메서드인 
 * {@code selectBBSMasterInf(String bbsId, String uniqId, String requestType)} 에서
 * 요청 유형에 따라 서로 다른 응답 DTO를 반환하기 위한 공통 타입입니다.
 * 
 * <p>
 * 예) {@link BbsAttributeDetailResponseDTO}, {@link BbsManageFileAtchResponseDTO} 등에서 구현되며
 * 클라이언트 요청 유형에 따라 조건 분기 후 해당 DTO를 반환합니다.
 * </p>
 * 
 * @author 김재섭(nirsa)
 * @since 2025.07.03
 * @version 1.0
 * @see BbsAttributeDetailResponseDTO
 * @see BbsManageFileAtchResponseDTO
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일         수정자          수정내용
 *   ----------    ------------    ---------------------------
 *   2025.07.03    김재섭(nirsa)     최초 생성
 *
 * </pre>
 */

public interface BbsDetailResponse {

}
