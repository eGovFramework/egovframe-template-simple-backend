package egovframework.com.cmm.web;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import jakarta.servlet.http.HttpServletResponse;

import org.egovframe.rte.fdl.crypto.EgovCryptoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EgovImageProcessControllerTest {

    @TempDir
    Path temporaryDirectory;

    @DisplayName("JPG 미리보기는 image/jpeg MIME 타입과 원본 바이트를 응답한다.")
    @Test
    void getImageInfReturnsJpegContentTypeAndFileBytes() throws Exception {
        // given
        byte[] imageBytes = "test-image-bytes".getBytes(StandardCharsets.UTF_8);
        String storedFileName = "preview.jpg";
        Files.write(temporaryDirectory.resolve(storedFileName), imageBytes);

        EgovFileMngService fileService = mock(EgovFileMngService.class);
        EgovCryptoService cryptoService = mock(EgovCryptoService.class);
        EgovImageProcessController controller = new EgovImageProcessController();
        ReflectionTestUtils.setField(controller, "fileService", fileService);
        ReflectionTestUtils.setField(controller, "cryptoService", cryptoService);

        FileVO fileInfo = new FileVO();
        fileInfo.setFileStreCours(temporaryDirectory.toString());
        fileInfo.setStreFileNm(storedFileName);
        fileInfo.setFileExtsn("JPG");

        byte[] encryptedFileId = "encrypted-file-id".getBytes(StandardCharsets.UTF_8);
        String encodedFileId = Base64.getEncoder().encodeToString(encryptedFileId);
        when(cryptoService.decrypt(eq(encryptedFileId), eq(EgovFileDownloadController.ALGORITM_KEY)))
                .thenReturn("file-id".getBytes(StandardCharsets.UTF_8));
        when(fileService.selectFileInf(any(FileVO.class))).thenReturn(fileInfo);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        controller.getImageInf(null, new ModelMap(), Map.of("atchFileId", encodedFileId, "fileSn", "0"), response);

        // then
        assertEquals("image/jpeg", response.getContentType());
        assertEquals(imageBytes.length, response.getContentLength());
        assertArrayEquals(imageBytes, response.getContentAsByteArray());
    }

    @DisplayName("삭제 처리되어 조회되지 않는 첨부는 404 를 응답한다.")
    @Test
    void getImageInfReturnsNotFoundWhenFileIsNotAvailable() throws Exception {
        // given
        EgovFileMngService fileService = mock(EgovFileMngService.class);
        EgovCryptoService cryptoService = mock(EgovCryptoService.class);
        EgovImageProcessController controller = new EgovImageProcessController();
        ReflectionTestUtils.setField(controller, "fileService", fileService);
        ReflectionTestUtils.setField(controller, "cryptoService", cryptoService);

        byte[] encryptedFileId = "encrypted-file-id".getBytes(StandardCharsets.UTF_8);
        String encodedFileId = Base64.getEncoder().encodeToString(encryptedFileId);
        when(cryptoService.decrypt(eq(encryptedFileId), eq(EgovFileDownloadController.ALGORITM_KEY)))
                .thenReturn("file-id".getBytes(StandardCharsets.UTF_8));
        when(fileService.selectFileInf(any(FileVO.class))).thenReturn(null);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        controller.getImageInf(null, new ModelMap(), Map.of("atchFileId", encodedFileId, "fileSn", "0"), response);

        // then
        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
        assertEquals(0, response.getContentAsByteArray().length);
    }

    @DisplayName("첨부 정보가 있어도 실제 이미지 파일이 없으면 404를 응답한다.")
    @Test
    void getImageInfReturnsNotFoundWhenPhysicalFileIsMissing() throws Exception {
        assertPhysicalFileNotFound("missing.jpg");
    }

    @DisplayName("이미지 저장 경로가 디렉터리이면 404를 응답한다.")
    @Test
    void getImageInfReturnsNotFoundWhenPathIsDirectory() throws Exception {
        Files.createDirectory(temporaryDirectory.resolve("directory.jpg"));

        assertPhysicalFileNotFound("directory.jpg");
    }

    private void assertPhysicalFileNotFound(String storedFileName) throws Exception {
        EgovFileMngService fileService = mock(EgovFileMngService.class);
        EgovCryptoService cryptoService = mock(EgovCryptoService.class);
        EgovImageProcessController controller = new EgovImageProcessController();
        ReflectionTestUtils.setField(controller, "fileService", fileService);
        ReflectionTestUtils.setField(controller, "cryptoService", cryptoService);

        FileVO fileInfo = new FileVO();
        fileInfo.setFileStreCours(temporaryDirectory.toString());
        fileInfo.setStreFileNm(storedFileName);
        fileInfo.setFileExtsn("JPG");
        when(fileService.selectFileInf(any(FileVO.class))).thenReturn(fileInfo);

        byte[] encryptedFileId = "encrypted-file-id".getBytes(StandardCharsets.UTF_8);
        when(cryptoService.decrypt(eq(encryptedFileId), eq(EgovFileDownloadController.ALGORITM_KEY)))
                .thenReturn("file-id".getBytes(StandardCharsets.UTF_8));
        Map<String, Object> parameters = Map.of(
                "atchFileId", Base64.getEncoder().encodeToString(encryptedFileId), "fileSn", "0");
        MockHttpServletResponse response = new MockHttpServletResponse();

        controller.getImageInf(null, new ModelMap(), parameters, response);

        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
        assertNull(response.getContentType());
        assertEquals(0, response.getContentAsByteArray().length);
    }
}
