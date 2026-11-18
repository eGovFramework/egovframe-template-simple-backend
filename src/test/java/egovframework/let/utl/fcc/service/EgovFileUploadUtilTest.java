package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EgovFileUploadUtilTest {

    @DisplayName("getFileExtension 호출 시, 파일명에서 확장자를 추출하며 null이면 빈 문자열을 반환한다.")
    @Test
    void testGetFileExtension() {
        assertEquals("", EgovFileUploadUtil.getFileExtension(null));
        assertEquals("txt", EgovFileUploadUtil.getFileExtension("document.txt"));
        assertEquals("gz", EgovFileUploadUtil.getFileExtension("archive.tar.gz"));
    }

    @DisplayName("checkFileExtension 호출 시, 화이트리스트에 포함된 확장자만 허용한다.")
    @Test
    void testCheckFileExtension() {
        assertTrue(EgovFileUploadUtil.checkFileExtension("image.png", ".png.pdf.txt"));
        assertFalse(EgovFileUploadUtil.checkFileExtension("image.exe", ".png.pdf.txt"));
        assertFalse(EgovFileUploadUtil.checkFileExtension("noext", ".png.pdf.txt"));
        assertFalse(EgovFileUploadUtil.checkFileExtension("image.png", null));
        assertFalse(EgovFileUploadUtil.checkFileExtension("image.png", ""));
    }

    @DisplayName("checkFileMaxSize 호출 시, 허용 크기 이하이면 true를 반환한다.")
    @Test
    void testCheckFileMaxSize() {
        assertFalse(EgovFileUploadUtil.checkFileMaxSize(null, 1024));

        MultipartFile smallFile = mock(MultipartFile.class);
        when(smallFile.getSize()).thenReturn(512L);
        assertTrue(EgovFileUploadUtil.checkFileMaxSize(smallFile, 1024));

        MultipartFile bigFile = mock(MultipartFile.class);
        when(bigFile.getSize()).thenReturn(2048L);
        assertFalse(EgovFileUploadUtil.checkFileMaxSize(bigFile, 1024));
    }
}
