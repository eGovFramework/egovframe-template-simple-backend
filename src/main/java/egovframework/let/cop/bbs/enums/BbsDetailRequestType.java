package egovframework.let.cop.bbs.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 게시판 상세 조회 요청 타입을 정의한 열거형입니다.
 * 
 * <p>
 * 게시판 마스터 정보를 조회할 때, 요청 목적에 따라 다음과 같은 유형으로 구분됩니다.
 * </p>
 * <ul>
 *   <li>{@code DETAIL} - 게시판 상세 설정 정보 조회</li>
 *   <li>{@code LIST} - 게시판 목록 조회 시 필요한 메타 정보</li>
 *   <li>{@code FILE_ATCH} - 첨부파일 관련 설정 정보 조회</li>
 * </ul>
 * 
 * 이 enum은 컨트롤러에서 요청 파라미터로 사용되며, {@code selectBBSMasterInf} 메서드의 동작 분기를 위해 활용됩니다.
 * 
 * @author 김재섭(nirsa)
 * @since 2025.07.03
 * @version 1.0
 *
 * @see egovframework.let.cop.bbs.service.EgovBBSAttributeManageService#selectBBSMasterInf
 */

@Schema(description = "요청 타입", example = "DETAIL", allowableValues = {"DETAIL", "LIST", "FILE_ATCH"})
public enum BbsDetailRequestType {
    DETAIL,
    LIST,
    FILE_ATCH
}
