package egovframework.let.cop.bbs.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 존재하지 않는 게시물을 조회했을 때 서버 오류가 나지 않는지 검증한다.
 *
 * <p>서비스가 조회 결과 없음을 null 로 돌려주지 않으면 DTO 변환에서
 * NullPointerException 이 나 HTTP 500 이 반환된다. 상세 화면에서 새로고침을 하거나
 * 삭제된 게시물을 다시 열면 재현된다.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EgovBBSManageApiControllerMissingArticleTest {

    /** 시드에 존재하는 공지사항 게시판. */
    private static final String SEEDED_BBS_ID = "BBSMSTR_AAAAAAAAAAAA";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("존재하지 않는 게시물 상세 조회가 서버 오류로 끝나지 않는다")
    void missingArticleDetailDoesNotReturnServerError() throws Exception {
        mockMvc.perform(get("/board/{bbsId}/{nttId}", SEEDED_BBS_ID, "99999999"))
                .andExpect(status().is(org.springframework.http.HttpStatus.OK.value()));
    }
}
