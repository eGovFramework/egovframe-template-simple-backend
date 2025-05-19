package egovframework.let.cop.bbs.service;

import org.junit.Test;

import egovframework.let.cop.bbs.domain.model.Board;

/**
 * fileName       : BoardTest
 * author         : crlee
 * date           : 2023/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/26        crlee       최초 생성
 */

public class BoardTest{

    @Test
    public void Board() {
        Board board = new Board();
        board.setBbsId("TestId");
        board.setFrstRegisterId("FrstId");
        board.setNttNo(3L);

        org.junit.Assert.assertEquals( board.getBbsId() , "TestId" );
        org.junit.Assert.assertEquals( board.getFrstRegisterId() , "FrstId" );
        org.junit.Assert.assertEquals( board.getNttNo() , 3L );
    }

}