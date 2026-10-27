package egovframework.let.cop.bbs.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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


        Assertions.assertEquals( boardVO.getBbsId() , "TestId" );
        Assertions.assertEquals( boardVO.getFrstRegisterId() , "FrstId" );
        Assertions.assertEquals( boardVO.getNttNo() , 3L );
        Assertions.assertEquals( boardVO.getBbsNm() , "게시판nm" );
    }
}