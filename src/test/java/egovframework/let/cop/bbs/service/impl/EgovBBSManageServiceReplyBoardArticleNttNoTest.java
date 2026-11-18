package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import egovframework.let.cop.bbs.domain.model.Board;
import egovframework.let.cop.bbs.domain.model.BoardMaster;
import egovframework.let.cop.bbs.domain.repository.BBSAttributeManageDAO;
import egovframework.let.cop.bbs.domain.repository.BBSManageDAO;

/**
 * 
 * description    : 답글 등록(replyBoardArticle) 쿼리의 NTT_NO 하드코딩 결함 검증 테스트
 * 
 * packageName    : egovframework.let.cop.bbs.service.impl
 * fileName       : EgovBBSManageServiceReplyBoardArticleNttNoTest
 * author         : kimminsol
 * date           : 2026.08.26
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * ————————————————————————————————————————————————————————————
 * 2026.08.26       kimminsol          최초 생성
 */

/**
 * 답글 등록 시 게시글 번호(NTT_NO) 바인딩 결함 검증
 *
 * selectKey(order="BEFORE")를 통해 산출된 nttNo가 VALUES 절에 바인딩되지 않고
 * 리터럴 '1'로 하드코딩되어 있던 결함을 검증한다.
 * 
 * 서비스 로직(BBSManageDAO#replyBoardArticle)에서는 INSERT 직후 부모글 기준
 * NTT_NO 재설정 및 UPDATE 작업(getParentNttNo/updateOtherNttNo/updateNttNo)을 수행하므로,
 * 전체 프로세스 수행 후에는 결함 유무와 관계없이 최종 데이터가 동일해집니다.
 * 따라서 후속 보정 로직을 거치지 않고 INSERT 단독 실행 시 DB에 실제로 저장되는
 * NTT_NO 값을 직접 조회하여 결함 여부를 검증합니다.
 * 
 */
@SpringBootTest
class EgovBBSManageServiceReplyBoardArticleNttNoTest {

    @Autowired
    private BBSAttributeManageDAO bbsAttributeManageDAO;

    @Autowired
    private BBSManageDAO bbsMngDAO;

    @Autowired
    @Qualifier("egovBBSMstrIdGnrService")
    private EgovIdGnrService egovBBSMstrIdGnrService;

    @Autowired
    @Qualifier("egovNttIdGnrService")
    private EgovIdGnrService egovNttIdGnrService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Test
    @DisplayName("replyBoardArticle INSERT문은 selectKey로 계산한 nttNo를 그대로 저장한다 (하드코딩된 1이 아님)")
    void insertedNttNo_followsSelectKeyValue_notHardcodedOne() throws Exception {
        // given: 새 게시판과 원글(부모글) 하나 준비
        String bbsId = egovBBSMstrIdGnrService.getNextStringId();

        BoardMaster boardMaster = new BoardMaster();
        boardMaster.setBbsId(bbsId);
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS"));
        boardMaster.setBbsNm("test nttNo 게시판 " + now);
        boardMaster.setPosblAtchFileSize("0");
        bbsAttributeManageDAO.insertBBSMasterInf(boardMaster);

        Board parent = new Board();
        parent.setBbsId(bbsId);
        parent.setNttId(egovNttIdGnrService.getNextLongId());
        parent.setNttSj("부모글");
        parent.setNttCn("부모글 내용");
        parent.setParnts("0");
        parent.setReplyLc("0");
        parent.setReplyAt("N");
        bbsMngDAO.insertBoardArticle(parent); // NTT_NO=1, SORT_ORDR=1로 등록됨

        // when: replyBoardArticle의 INSERT문만 직접 실행 (뒤따르는 보정 UPDATE 전 상태를 확인하기 위함)
        Board firstReply = newReply(bbsId, parent);
        sqlSessionTemplate.insert("BBSManageDAO.replyBoardArticle", firstReply);

        Board secondReply = newReply(bbsId, parent);
        sqlSessionTemplate.insert("BBSManageDAO.replyBoardArticle", secondReply);

        // then: 같은 스레드(BBS_ID+SORT_ORDR) 안에서 MAX(NTT_NO)+1이 순차적으로 계산되어
        // 부모글(1) 다음으로 2, 3이 저장되어야 한다. 수정 전에는 두 답글 모두 1이 저장되었다.
        Long firstReplyNttNo = fetchNttNo(bbsId, firstReply.getNttId());
        Long secondReplyNttNo = fetchNttNo(bbsId, secondReply.getNttId());

        System.out.println("======================================");
        System.out.println("bbsId               = " + bbsId);
        System.out.println("parent  nttId/nttNo = " + parent.getNttId() + " / " + parent.getNttNo());
        System.out.println("reply1  nttId/nttNo = " + firstReply.getNttId() + " / " + firstReplyNttNo);
        System.out.println("reply2  nttId/nttNo = " + secondReply.getNttId() + " / " + secondReplyNttNo);
        System.out.println("======================================");

        assertNotEquals(1L, firstReplyNttNo, "selectKey로 계산한 값 대신 하드코딩된 1이 저장되면 안 된다.");
        assertEquals(2L, firstReplyNttNo);
        assertEquals(3L, secondReplyNttNo);
    }

    private Board newReply(String bbsId, Board parent) throws Exception {
        Board reply = new Board();
        reply.setBbsId(bbsId);
        reply.setNttId(egovNttIdGnrService.getNextLongId());
        reply.setNttSj("답글");
        reply.setNttCn("답글 내용");
        reply.setParnts(String.valueOf(parent.getNttId()));
        reply.setReplyLc("1");
        reply.setReplyAt("Y");
        reply.setSortOrdr(parent.getNttNo());
        return reply;
    }

    private Long fetchNttNo(String bbsId, long nttId) {
        Board lookup = new Board();
        lookup.setBbsId(bbsId);
        lookup.setParnts(String.valueOf(nttId));
        return sqlSessionTemplate.selectOne("BBSManageDAO.getParentNttNo", lookup);
    }
}