package egovframework.let.cop.smt.sim.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "일정 검색 조건 VO")
public class ScheduleSearchVO {

    @Schema(description = "조회 모드 (DAILY, WEEK, MONTH)", example = "DAILY")
    private String searchMode;

    @Schema(description = "조회 연도", example = "2025")
    private String year;

    @Schema(description = "조회 월 (1~12)", example = "5")
    private String month;

    @Schema(description = "조회 일 (1~31)", example = "7")
    private String date;

    @Schema(description = "검색 조건", example = "title")
    private String searchCondition;

    @Schema(description = "검색어", example = "회의")
    private String searchKeyword;

    // 일별 검색 시 사용할 전체일자 (yyyyMMdd)
    @Schema(description = "일별 조회용 검색일자 (yyyyMMdd)", example = "20250507")
    private String searchDay;

    // 주간 조회 시 사용할 시작일자
    @Schema(description = "주간 조회 시작일자 (yyyyMMdd)", example = "20250506")
    private String schdulBgnde;

    // 주간 조회 시 사용할 종료일자
    @Schema(description = "주간 조회 종료일자 (yyyyMMdd)", example = "20250512")
    private String schdulEndde;
}