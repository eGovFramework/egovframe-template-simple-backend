package egovframework.let.cop.bbs.dto.response;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.FileVO;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * BbsManage 게시물 정보를 반환하는 응답 DTO 클래스 입니다.
 *
 * @author 김재섭(nirsa)
 * @since 2025.08.11
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자         수정내용
 *   ----------    ------------     -------------------
 *   2025.08.11    김재섭(nirsa)      최초 생성
 *
 * </pre>
 */

@Getter
@Builder(toBuilder = true)
@Schema(description = "BBsManage 게시물 정보 조회 응답 DTO")
public class BbsManageDetailResponseDTO {
    private BbsManageDetailItemResponseDTO boardVO;

    @Setter
    @Schema(
            description = "게시판 마스터 상세",
            implementation = BbsManageDetailItemResponseDTO.class
    )
    private BbsFileAtchResponseDTO brdMstrVO;

    @Setter
    private String sessionUniqId;

    @Setter
    private LoginVO user;

    private List<FileVO> resultFiles;

    /**
     *  BoardVO vo → BbsManageDetailResponseDTO 변환 메서드 입니다.
     * @param boardVO, resultFiles
     * @return BbsManageDetailResponseDTO
     */
    public static BbsManageDetailResponseDTO from(BoardVO boardVO, List<FileVO> resultFiles) {
        return BbsManageDetailResponseDTO.builder()
                .boardVO(BbsManageDetailItemResponseDTO.from(boardVO))
                .resultFiles(resultFiles)
                .build();
    }

    public static BbsManageDetailResponseDTO from(BoardVO boardVO) {
        return BbsManageDetailResponseDTO.builder()
                .boardVO(BbsManageDetailItemResponseDTO.from(boardVO))
                .build();
    }
}