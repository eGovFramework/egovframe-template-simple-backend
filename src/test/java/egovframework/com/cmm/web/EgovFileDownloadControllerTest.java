package egovframework.com.cmm.web;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;

class EgovFileDownloadControllerTest {

    @TempDir
    Path temporaryDirectory;

    private EgovFileDownloadController controller;
    private EgovFileMngService fileService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Map<String, Object> parameters;

    @BeforeEach
    void setUp() throws Exception {
        fileService = mock(EgovFileMngService.class);
        EgovCryptoService cryptoService = mock(EgovCryptoService.class);
        controller = new EgovFileDownloadController();
        ReflectionTestUtils.setField(controller, "fileService", fileService);
        ReflectionTestUtils.setField(controller, "cryptoService", cryptoService);

        byte[] encryptedFileId = "encrypted-file-id".getBytes(StandardCharsets.UTF_8);
        when(cryptoService.decrypt(eq(encryptedFileId), eq(EgovFileDownloadController.ALGORITM_KEY)))
                .thenReturn("file-id".getBytes(StandardCharsets.UTF_8));
        parameters = Map.of("atchFileId", Base64.getEncoder().encodeToString(encryptedFileId), "fileSn", "0");
        request = new MockHttpServletRequest();
        request.addHeader("User-Agent", "Chrome");
        response = new MockHttpServletResponse();
    }

    @DisplayName("0바이트 첨부파일도 원본 파일명과 빈 본문으로 다운로드된다.")
    @Test
    void downloadsEmptyFile() throws Exception {
        Files.createFile(temporaryDirectory.resolve("stored-file"));
        givenFileInfo("stored-file", "empty.xlsx");

        controller.cvplFileDownload(parameters, request, response);

        assertDownload("empty.xlsx", new byte[0]);
    }

    @DisplayName("일반 첨부파일은 원본 파일명과 파일 바이트를 응답한다.")
    @Test
    void downloadsFileWithContent() throws Exception {
        byte[] fileBytes = new byte[] {0, 1, 2, (byte) 255};
        Files.write(temporaryDirectory.resolve("stored-file"), fileBytes);
        givenFileInfo("stored-file", "sample.xlsx");

        controller.cvplFileDownload(parameters, request, response);

        assertDownload("sample.xlsx", fileBytes);
    }

    @DisplayName("저장된 실제 파일이 없으면 다운로드를 거부한다.")
    @Test
    void rejectsMissingFile() throws Exception {
        givenFileInfo("missing-file", "sample.xlsx");

        assertDownloadRejected();
    }

    @DisplayName("저장 경로가 디렉터리이면 다운로드를 거부한다.")
    @Test
    void rejectsDirectory() throws Exception {
        Files.createDirectory(temporaryDirectory.resolve("directory"));
        givenFileInfo("directory", "sample.xlsx");

        assertDownloadRejected();
    }

    @DisplayName("삭제 등으로 첨부파일 정보가 조회되지 않으면 다운로드를 거부한다.")
    @Test
    void rejectsUnavailableFileInfo() throws Exception {
        when(fileService.selectFileInf(any(FileVO.class))).thenReturn(null);

        assertDownloadRejected();
    }

    private void givenFileInfo(String storedFileName, String originalFileName) throws Exception {
        FileVO fileInfo = new FileVO();
        fileInfo.setFileStreCours(temporaryDirectory.toString());
        fileInfo.setStreFileNm(storedFileName);
        fileInfo.setOrignlFileNm(originalFileName);
        when(fileService.selectFileInf(any(FileVO.class))).thenReturn(fileInfo);
    }

    private void assertDownload(String originalFileName, byte[] expectedBytes) {
        assertEquals(200, response.getStatus());
        assertEquals("application/x-stuff", response.getContentType());
        assertEquals("attachment; filename=" + originalFileName, response.getHeader("Content-Disposition"));
        assertArrayEquals(expectedBytes, response.getContentAsByteArray());
    }

    private void assertDownloadRejected() {
        assertThrows(EgovBizException.class, () -> controller.cvplFileDownload(parameters, request, response));
        assertNull(response.getHeader("Content-Disposition"));
        assertEquals(0, response.getContentAsByteArray().length);
    }
}
