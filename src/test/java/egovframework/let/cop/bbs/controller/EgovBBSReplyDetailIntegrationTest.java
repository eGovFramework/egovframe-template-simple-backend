package egovframework.let.cop.bbs.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import egovframework.com.cmm.LoginVO;
import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.domain.repository.BBSAttributeManageDAO;
import egovframework.let.cop.bbs.domain.repository.BBSManageDAO;
import egovframework.let.cop.bbs.dto.request.BbsManageDetailBoardRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSManageService;

@SpringBootTest
@Transactional
class EgovBBSReplyDetailIntegrationTest {

    @Autowired
    private EgovBBSManageApiController controller;

    @Autowired
    private EgovBBSManageService service;

    @Autowired
    private BBSManageDAO boardDAO;

    @Autowired
    private BBSAttributeManageDAO masterDAO;

    @Autowired
    @Qualifier("egovBBSMstrIdGnrService")
    private EgovIdGnrService masterIdService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        LoginVO user = new LoginVO();
        user.setUniqId("REPLY_TEST_ADMIN");
        user.setName("답글 테스트 관리자");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @ParameterizedTest(name = "상세 응답으로 답글 등록: 부모 깊이 {0}")
    @ValueSource(ints = {0, 1})
    void replyFromDetailPreservesThreadOrderAndIncrementsDepth(int parentDepth) throws Exception {
        String bbsId = createBoardMaster();
        Board older = insertArticle(bbsId, "이전 글");
        Board root = insertArticle(bbsId, "원글");
        BoardVO parent = selectStoredArticle(bbsId, root.getNttId());
        if (parentDepth == 1) {
            Board existingReply = new Board();
            existingReply.setBbsId(bbsId);
            existingReply.setNttSj("기존 답글");
            existingReply.setNttCn("기존 답글 내용");
            existingReply.setReplyAt("Y");
            existingReply.setReplyLc("1");
            existingReply.setParnts(Long.toString(root.getNttId()));
            existingReply.setSortOrdr(parent.getSortOrdr());
            service.insertBoardArticle(existingReply);
            parent = selectStoredArticle(bbsId, existingReply.getNttId());
        }

        JsonNode detail = objectMapper.readTree(objectMapper.writeValueAsString(
                service.selectBoardArticle(BbsManageDetailBoardRequestDTO.builder()
                        .bbsId(bbsId).nttId(parent.getNttId()).plusCount(false).build())))
                .get("boardVO");

        // React 답글 화면처럼 상세 응답을 복사하고 FormData 문자열로 전송한다.
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        detail.fields().forEachRemaining(field -> form.add(field.getKey(), field.getValue().asText()));
        form.set("nttSj", "새 답글");
        form.set("nttCn", "새 답글 내용");
        form.set("inqireCo", "0");
        form.set("atchFileId", "");

        mockMvc.perform(multipart("/boardReply").params(form))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200));

        BoardVO search = new BoardVO();
        search.setBbsId(bbsId);
        search.setSearchCnd("0");
        search.setSearchWrd("");
        search.setFirstIndex(0);
        search.setRecordCountPerPage(10);
        List<BoardVO> articles = boardDAO.selectBoardArticleList(search);
        long replyId = articles.stream().filter(article -> "새 답글".equals(article.getNttSj()))
                .findFirst().orElseThrow().getNttId();
        BoardVO savedReply = selectStoredArticle(bbsId, replyId);
        long parentId = parent.getNttId();
        long parentSortOrder = parent.getSortOrdr();
        List<Long> expectedOrder = parentDepth == 0
                ? List.of(root.getNttId(), replyId, older.getNttId())
                : List.of(root.getNttId(), parentId, replyId, older.getNttId());

        assertAll(
                () -> assertEquals(Long.toString(parentId), savedReply.getParnts()),
                () -> assertEquals(parentSortOrder, savedReply.getSortOrdr()),
                () -> assertEquals(Integer.toString(parentDepth + 1), savedReply.getReplyLc()),
                () -> assertEquals(expectedOrder, articles.stream().map(BoardVO::getNttId).toList()));
    }

    private String createBoardMaster() throws Exception {
        BoardMaster master = new BoardMaster();
        master.setBbsId(masterIdService.getNextStringId());
        master.setBbsNm("답글 상세 응답 테스트");
        master.setPosblAtchFileSize("0");
        masterDAO.insertBBSMasterInf(master);
        return master.getBbsId();
    }

    private Board insertArticle(String bbsId, String title) throws Exception {
        Board article = new Board();
        article.setBbsId(bbsId);
        article.setNttSj(title);
        article.setNttCn(title + " 내용");
        service.insertBoardArticle(article);
        return article;
    }

    private BoardVO selectStoredArticle(String bbsId, long nttId) throws Exception {
        BoardVO lookup = new BoardVO();
        lookup.setBbsId(bbsId);
        lookup.setNttId(nttId);
        return boardDAO.selectBoardArticle(lookup);
    }
}
