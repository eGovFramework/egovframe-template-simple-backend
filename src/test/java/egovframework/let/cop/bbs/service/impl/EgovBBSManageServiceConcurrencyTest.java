package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 게시물 ID 채번 동시성 테스트
 */
@Slf4j
@SpringBootTest
class EgovBBSManageServiceConcurrencyTest {

    @Autowired
    @Qualifier("egovNttIdGnrService")
    private EgovIdGnrService egovNttIdGnrService;

    @Test
    @DisplayName("멀티 스레드 환경에서 ID 채번 시 중복 없이 고유한 ID가 발급된다")
    void testConcurrentIdGeneration() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Set<Long> generatedIds = Collections.synchronizedSet(new HashSet<>());

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    long id = egovNttIdGnrService.getNextLongId();
                    generatedIds.add(id);
                } catch (Exception e) {
                    log.error("동시성 ID 발급 중 오류 발생", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        
        // 중복 없이 정확히 threadCount(100) 만큼의 고유 ID가 생성되었는지 검증
        assertEquals(threadCount, generatedIds.size(), "동시성 환경에서 중복된 ID가 발급되었습니다.");
    }
}
