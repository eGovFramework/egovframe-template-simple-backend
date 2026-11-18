package egovframework.let.utl.fcc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EgovFormBasedFileUtilTest {

    @DisplayName("getTodayString 호출 시, 8자리 숫자 날짜 문자열을 반환한다.")
    @Test
    void testGetTodayString() {
        String today = EgovFormBasedFileUtil.getTodayString();
        assertEquals(8, today.length());
        assertTrue(today.matches("\\d{8}"));
    }

    @DisplayName("getPhysicalFileName 호출 시, 하이픈이 없는 32자리 대문자 UUID 문자열을 반환한다.")
    @Test
    void testGetPhysicalFileName() {
        String name = EgovFormBasedFileUtil.getPhysicalFileName();
        assertTrue(name.matches("[0-9A-F]{32}"));
    }

    @DisplayName("saveFile 호출 시, InputStream의 내용을 파일로 저장하고 저장된 크기를 반환한다.")
    @Test
    void testSaveFileWritesContentAndReturnsSize(@TempDir Path tempDir) throws IOException {
        // given
        byte[] content = "hello egovframe".getBytes();
        InputStream is = new ByteArrayInputStream(content);
        File target = tempDir.resolve("sub").resolve("saved.txt").toFile();

        // when
        long size = EgovFormBasedFileUtil.saveFile(is, target);

        // then
        assertEquals(content.length, size);
        assertTrue(target.exists());
        assertArrayEquals(content, Files.readAllBytes(target.toPath()));
    }

    @DisplayName("saveFile 호출 시, 저장할 파일의 상위 디렉토리가 없으면 RuntimeException이 발생한다.")
    @Test
    void testSaveFileWithNoParentDirectoryThrows() {
        // given
        InputStream is = new ByteArrayInputStream("data".getBytes());
        File fileWithoutParent = new File("no-parent-file.tmp");

        // when / then
        assertThrows(RuntimeException.class, () -> EgovFormBasedFileUtil.saveFile(is, fileWithoutParent));
    }
}
