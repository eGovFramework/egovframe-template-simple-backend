package egovframework.com.cmm.service;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
        when(propertyService.getLong("Globals.posblAtchFileSize")).thenReturn(5L * 1024 * 1024);
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
        when(multipartFile.getSize()).thenReturn(5L * 1024 * 1024 + 1);

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

    @DisplayName("정상 파일 다음에 크기 제한을 초과한 파일이 있으면 아무 파일도 저장하지 않는다.")
    @Test
    void rejectsOversizedLastFileBeforeSaving(@TempDir Path tempDir) throws Exception {
        assertRejectedWithoutNewFiles(tempDir, upload("first.png", new byte[] {1, 2}),
                upload("large.png", new byte[5]));
    }

    @DisplayName("첫 파일이 크기 제한을 초과하면 뒤의 정상 파일도 저장하지 않는다.")
    @Test
    void rejectsOversizedFirstFileBeforeSaving(@TempDir Path tempDir) throws Exception {
        assertRejectedWithoutNewFiles(tempDir, upload("large.png", new byte[5]),
                upload("second.png", new byte[] {1, 2}));
    }

    @DisplayName("정상 파일 다음에 금지 확장자 파일이 있으면 아무 파일도 저장하지 않는다.")
    @Test
    void rejectsDisallowedLastFileBeforeSaving(@TempDir Path tempDir) throws Exception {
        assertRejectedWithoutNewFiles(tempDir, upload("first.png", new byte[] {1, 2}),
                upload("second.exe", new byte[] {3}));
    }

    @DisplayName("정상 파일 다음에 확장자 없는 파일이 있으면 아무 파일도 저장하지 않는다.")
    @Test
    void rejectsExtensionlessLastFileBeforeSaving(@TempDir Path tempDir) throws Exception {
        assertRejectedWithoutNewFiles(tempDir, upload("first.png", new byte[] {1, 2}),
                upload("second", new byte[] {3}));
    }

    @DisplayName("모든 파일이 정상이면 원본 내용, 파일명 정규화, 순서와 파일 번호를 유지한다.")
    @Test
    void savesValidFilesInOrder(@TempDir Path tempDir) throws Exception {
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());
        when(idgenService.getNextStringId()).thenReturn("ATCH0002");
        byte[] firstContent = {1, 2, 3};
        byte[] secondContent = {4, 5};

        List<FileVO> result = egovFileMngUtil.parseFileInf(uploads(
                upload("photo:one.PNG", firstContent), upload("second.xlsx", secondContent)), "KEY", 7, "", "");

        assertEquals(2, result.size());
        FileVO first = result.get(0);
        FileVO second = result.get(1);
        assertEquals("photo_one.PNG", first.getOrignlFileNm());
        assertEquals("second.xlsx", second.getOrignlFileNm());
        assertEquals("png", first.getFileExtsn());
        assertEquals("xlsx", second.getFileExtsn());
        assertEquals("3", first.getFileMg());
        assertEquals("2", second.getFileMg());
        assertEquals("7", first.getFileSn());
        assertEquals("8", second.getFileSn());
        assertEquals("ATCH0002", first.getAtchFileId());
        assertEquals("ATCH0002", second.getAtchFileId());
        assertEquals(tempDir.toString(), first.getFileStreCours());
        assertEquals(tempDir.toString(), second.getFileStreCours());
        assertTrue(first.getStreFileNm().startsWith("KEY"));
        assertTrue(second.getStreFileNm().startsWith("KEY"));
        assertNotEquals(first.getStreFileNm(), second.getStreFileNm());
        assertArrayEquals(firstContent, Files.readAllBytes(tempDir.resolve(first.getStreFileNm())));
        assertArrayEquals(secondContent, Files.readAllBytes(tempDir.resolve(second.getStreFileNm())));
        assertFileCount(tempDir, 2);
        verify(idgenService).getNextStringId();
    }

    @DisplayName("파일명 없는 입력은 건너뛰고 이름이 있는 빈 파일은 저장한다.")
    @Test
    void skipsUnnamedInputsButSavesNamedEmptyFile(@TempDir Path tempDir) throws Exception {
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());
        MultipartFile nullNameFile = mock(MultipartFile.class);
        when(nullNameFile.getOriginalFilename()).thenReturn(null);

        List<FileVO> result = egovFileMngUtil.parseFileInf(uploads(
                upload("", new byte[0]), nullNameFile, upload("empty.xlsx", new byte[0])),
                "KEY", 3, " ATCH 0003 ", "");

        assertEquals(1, result.size());
        FileVO file = result.get(0);
        assertEquals("empty.xlsx", file.getOrignlFileNm());
        assertEquals("xlsx", file.getFileExtsn());
        assertEquals("0", file.getFileMg());
        assertEquals("3", file.getFileSn());
        assertEquals("ATCH0003", file.getAtchFileId());
        Path savedFile = tempDir.resolve(file.getStreFileNm());
        assertTrue(Files.isRegularFile(savedFile));
        assertEquals(0, Files.size(savedFile));
        assertFileCount(tempDir, 1);
        verify(idgenService, times(0)).getNextStringId();
        verify(nullNameFile, times(0)).transferTo(any(java.io.File.class));
    }

    @DisplayName("허용 한도와 크기가 같은 파일은 저장한다.")
    @Test
    void savesFileAtSizeLimit(@TempDir Path tempDir) throws Exception {
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());
        when(propertyService.getLong("Globals.posblAtchFileSize")).thenReturn(4L);
        byte[] content = {1, 2, 3, 4};

        List<FileVO> result = egovFileMngUtil.parseFileInf(
                uploads(upload("limit.png", content)), "KEY", 0, "ATCH0004", "");

        assertEquals(1, result.size());
        assertEquals("4", result.get(0).getFileMg());
        assertArrayEquals(content, Files.readAllBytes(tempDir.resolve(result.get(0).getStreFileNm())));
        assertFileCount(tempDir, 1);
    }

    private void assertRejectedWithoutNewFiles(Path tempDir, MultipartFile... files) throws Exception {
        when(propertyService.getString("Globals.fileStorePath")).thenReturn(tempDir.toString());
        when(propertyService.getLong("Globals.posblAtchFileSize")).thenReturn(4L);
        Path existingFile = tempDir.resolve("existing.png");
        byte[] existingContent = {9, 8, 7};
        Files.write(existingFile, existingContent);

        assertThrows(EgovBizException.class,
                () -> egovFileMngUtil.parseFileInf(uploads(files), "KEY", 1, "ATCH0001", ""));

        assertArrayEquals(existingContent, Files.readAllBytes(existingFile));
        assertFileCount(tempDir, 1);
    }

    private MockMultipartFile upload(String originalName, byte[] content) {
        return new MockMultipartFile("file", originalName, "application/octet-stream", content);
    }

    private Map<String, MultipartFile> uploads(MultipartFile... files) {
        Map<String, MultipartFile> result = new LinkedHashMap<>();
        for (int index = 0; index < files.length; index++) {
            result.put("file_" + index, files[index]);
        }
        return result;
    }

    private void assertFileCount(Path directory, long expectedCount) throws Exception {
        try (Stream<Path> savedFiles = Files.list(directory)) {
            assertEquals(expectedCount, savedFiles.count());
        }
    }
}
