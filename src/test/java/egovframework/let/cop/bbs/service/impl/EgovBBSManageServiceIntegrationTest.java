package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EgovBBSManageServiceIntegrationTest {

    @Autowired
    @Qualifier("egovNttIdGnrService")
    private EgovIdGnrService egovNttIdGnrService;

    @Test
    @DisplayName("ID 채번 서비스(egovNttIdGnrService) 빈 주입 및 초기 데이터(11) 발급 확인")
    void testEgovNttIdGnrService_Initialization() throws Exception {
        assertNotNull(egovNttIdGnrService);
        
        // HSQL 초기 데이터에 11로 세팅되어 있으므로 첫 발급 ID는 11이어야 함.
        long firstId = egovNttIdGnrService.getNextLongId();
        
        // 테스트 환경에 따라 다른 테스트가 먼저 실행되었을 수 있으므로 값이 11 이상이어야 함.
        // HSQL 초기 데이터 연결이 잘 되었다면 최소 11로 시작함.
        assertEquals(true, firstId >= 11, "첫 번째 발급된 ID는 초기 데이터 설정값인 11(이상)이어야 합니다.");
    }
}
