package egovframework.com.cmm.service;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EgovFileMngUtilTest {

    private EgovFileMngUtil egovFileMngUtil;
    private EgovPropertyService propertyService;
    private EgovIdGnrService idgenService;

    @BeforeEach
    void setUp() {
        egovFileMngUtil = new EgovFileMngUtil();
        propertyService = mock(EgovPropertyService.class);
        idgenService = mock(EgovIdGnrService.class);

        ReflectionTestUtils.setField(egovFileMngUtil, "allowedExtensionsRaw", ".gif.jpg.jpeg.png.xls.xlsx");
        ReflectionTestUtils.setField(egovFileMngUtil, "propertyService", propertyService);
        ReflectionTestUtils.setField(egovFileMngUtil, "idgenService", idgenService);
    }

    @DisplayName("parseFileInf 호출 시, 허용된 확장자의 파일이면 FileVO 목록을 반환한다.")
    @Test
    void testParseFileInfWithAllowedExtension(@TempDir Path tempDir) throws Exception {
        // given
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());
        when(idgenService.getNextStringId()).thenReturn("ATCH0001");

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("photo.png");
        when(multipartFile.getSize()).thenReturn(100L);

        Map<String, MultipartFile> files = new LinkedHashMap<>();
        files.put("file", multipartFile);

        // when
        List<FileVO> result = egovFileMngUtil.parseFileInf(files, "KEY", 1, "", "");

        // then
        assertEquals(1, result.size());
        FileVO fileVO = result.get(0);
        assertEquals("png", fileVO.getFileExtsn());
        assertEquals(tempDir.toString(), fileVO.getFileStreCours());
        assertEquals("100", fileVO.getFileMg());
        assertEquals("photo.png", fileVO.getOrignlFileNm());
        assertEquals("ATCH0001", fileVO.getAtchFileId());
        assertEquals("1", fileVO.getFileSn());
        verify(multipartFile, times(1)).transferTo(any(java.io.File.class));
    }

    @DisplayName("parseFileInf 호출 시, 허용되지 않은 확장자면 EgovBizException이 발생한다.")
    @Test
    void testParseFileInfWithDisallowedExtensionThrows(@TempDir Path tempDir) {
        // given
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("malware.exe");

        Map<String, MultipartFile> files = new LinkedHashMap<>();
        files.put("file", multipartFile);

        // when / then
        assertThrows(EgovBizException.class, () -> egovFileMngUtil.parseFileInf(files, "KEY", 1, "", ""));
    }

    @DisplayName("parseFileInf 호출 시, 최대 허용 크기를 초과하면 EgovBizException이 발생한다.")
    @Test
    void testParseFileInfWithOversizedFileThrows(@TempDir Path tempDir) {
        // given
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("big.png");
        when(multipartFile.getSize()).thenReturn(11L * 1024 * 1024);

        Map<String, MultipartFile> files = new LinkedHashMap<>();
        files.put("file", multipartFile);

        // when / then
        assertThrows(EgovBizException.class, () -> egovFileMngUtil.parseFileInf(files, "KEY", 1, "", ""));
    }

    @DisplayName("parseFileInf 호출 시, 확장자가 없는 파일이면 EgovBizException이 발생한다.")
    @Test
    void testParseFileInfWithNoExtensionThrows(@TempDir Path tempDir) {
        // given
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("noextension");

        Map<String, MultipartFile> files = new LinkedHashMap<>();
        files.put("file", multipartFile);

        // when / then
        assertThrows(EgovBizException.class, () -> egovFileMngUtil.parseFileInf(files, "KEY", 1, "", ""));
    }

    @DisplayName("parseFileInf 호출 시, 원 파일명이 빈 파일은 건너뛴다.")
    @Test
    void testParseFileInfSkipsFileWithoutOriginalName(@TempDir Path tempDir) throws Exception {
        // given
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("");

        Map<String, MultipartFile> files = new LinkedHashMap<>();
        files.put("file", multipartFile);

        // when
        List<FileVO> result = egovFileMngUtil.parseFileInf(files, "KEY", 1, "", "");

        // then
        assertTrue(result.isEmpty());
    }
}
