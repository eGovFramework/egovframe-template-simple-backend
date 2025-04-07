package egovframework.let.cop.bbs.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "게시판 검색 조건")
public class BoardMasterSearchVO {

    @Schema(description = "게시판 Id", example = " ")
    private final String bbsId = " ";
    
    @Schema(description = "페이지 번호", example = "1")
    private final int pageIndex = 1;

    @Schema(description = "검색 조건", example = "0")
    private final String searchCnd = "0";

    @Schema(description = "검색어", example = " ", defaultValue = " ")
    private final String searchWrd = " ";
}