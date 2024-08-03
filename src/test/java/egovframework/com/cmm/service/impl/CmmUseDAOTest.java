package egovframework.com.cmm.service.impl;

import static org.mockito.Mockito.*;
import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.config.EgovConfigAppDatasource;
import egovframework.com.config.EgovConfigAppMapper;
import egovframework.com.config.EgovConfigAppTransaction;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest(
        classes = {
                EgovConfigAppDatasource.class,
                EgovConfigAppMapper.class,
                EgovConfigAppTransaction.class,
                CmmUseDAO.class
        }
)
public class CmmUseDAOTest {

    @Spy
    @InjectMocks
    private CmmUseDAO cmmUseDAO;

    private ComDefaultCodeVO vo;
    private List<CmmnDetailCode> mockList;
    private DataAccessException dataAccessException;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        vo = new ComDefaultCodeVO();
        CmmnDetailCode code1 = new CmmnDetailCode();
        CmmnDetailCode code2 = new CmmnDetailCode();
        mockList = Arrays.asList(code1, code2);
        dataAccessException = new DataAccessException("Mock DataAccessException") {};
        System.setOut(new PrintStream(outContent));
    }
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("공통코드 조회 - 성공")
    public void selectCmmCodeDetail_Success() {
        // Given
        vo.setCodeId("COM031");

        // Stub
        doReturn(mockList).when(cmmUseDAO).selectList("CmmUseDAO.selectCmmCodeDetail", vo);

        // When
        List<CmmnDetailCode> result = cmmUseDAO.selectCmmCodeDetail(vo);

        // Then
        Assertions.assertThat(result).hasSize(2).containsExactlyElementsOf(mockList);
    }

    @Test
    @DisplayName("공통코드 조회 - 실패")
    public void selectCmmCodeDetail_Fail() {
        // Given
        vo.setCodeId("");

        // Stub
        doThrow(dataAccessException).when(cmmUseDAO).selectList("CmmUseDAO.selectCmmCodeDetail", vo);

        // When
        List<CmmnDetailCode> result = cmmUseDAO.selectCmmCodeDetail(vo);

        // Then
        Assertions.assertThat(result.size()).isEqualTo(0);
        Assertions.assertThat( (outContent.toString()).contains("Mock DataAccessException") ).isTrue();
    }

    @Test
    @DisplayName("조직정보 조회 - 성공")
    public void selectOgrnztIdDetail_Success() {
        // Given
        vo.setCodeId("ORGNZT_0000000000000");

        // Stub
        doReturn(mockList).when(cmmUseDAO).selectList("CmmUseDAO.selectOgrnztIdDetail", vo);

        // When
        List<CmmnDetailCode> result = cmmUseDAO.selectOgrnztIdDetail(vo);

        // Then
        Assertions.assertThat(result).hasSize(2).containsExactlyElementsOf(mockList);
    }

    @Test
    @DisplayName("조직정보 조회 - 실패")
    public void selectOgrnztIdDetail_Fail() {
        // Given
        vo.setCodeId("");

        // Stub
        doThrow(dataAccessException).when(cmmUseDAO).selectList("CmmUseDAO.selectOgrnztIdDetail", vo);

        // When
        List<CmmnDetailCode> result = cmmUseDAO.selectOgrnztIdDetail(vo);

        // Then
        Assertions.assertThat(result.size()).isEqualTo(0);
        Assertions.assertThat( (outContent.toString()).contains("Mock DataAccessException") ).isTrue();
    }

    @Test
    @DisplayName("사용할그룹정보 조회 - 성공")
    public void selectGroupIdDetail_Success() {
        // Given
        vo.setCodeId("GROUP_00000000000000");

        // Stub
        doReturn(mockList).when(cmmUseDAO).selectList("CmmUseDAO.selectGroupIdDetail", vo);

        // When
        List<CmmnDetailCode> result = cmmUseDAO.selectGroupIdDetail(vo);

        // Then
        Assertions.assertThat(result).hasSize(2).containsExactlyElementsOf(mockList);
    }

    @Test
    @DisplayName("사용할그룹정보 조회 - 실패")
    public void selectGroupIdDetail_Fail() {
        // Given
        vo.setCodeId("");

        // Stub
        doThrow(dataAccessException).when(cmmUseDAO).selectList("CmmUseDAO.selectGroupIdDetail", vo);

        // When
        List<CmmnDetailCode> result = cmmUseDAO.selectGroupIdDetail(vo);

        // Then
        Assertions.assertThat(result.size()).isEqualTo(0);
        Assertions.assertThat( (outContent.toString()).contains("Mock DataAccessException") ).isTrue();

    }
}
