package egovframework.let.cop.bbs.service;

import org.junit.Test;

import egovframework.let.cop.bbs.domain.model.BoardVO;

/**
 * fileName       : BoardVOTest
 * author         : crlee
 * date           : 2023/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/26        crlee       최초 생성
 */

public class BoardVOTest{

    @Test
    public void BoardVo() {
        BoardVO boardVO = new BoardVO();
        boardVO.setBbsId("TestId");
        boardVO.setBbsNm("게시판nm");
        boardVO.setFrstRegisterId("FrstId");
        boardVO.setNttNo(3L);


        org.junit.Assert.assertEquals( boardVO.getBbsId() , "TestId" );
        org.junit.Assert.assertEquals( boardVO.getFrstRegisterId() , "FrstId" );
        org.junit.Assert.assertEquals( boardVO.getNttNo() , 3L );
        org.junit.Assert.assertEquals( boardVO.getBbsNm() , "게시판nm" );
    }
}