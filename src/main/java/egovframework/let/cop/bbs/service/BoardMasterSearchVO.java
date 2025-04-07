package egovframework.let.cop.bbs.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시판 검색 조건")
public class BoardMasterSearchVO {

    @Schema(description = "게시판 Id", example = "")
    private String bbsId = "";

    @Schema(description = "페이지 번호", example = "1")
    private int pageIndex = 1;

    @Schema(description = "검색 조건", example = "0")
    private String searchCnd = "0";

    @Schema(description = "검색어", example = "")
    private String searchWrd = "";
}