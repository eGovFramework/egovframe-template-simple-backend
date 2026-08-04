package egovframework.com;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CI 검증용 프로브.
 *
 * 컴파일은 정상적으로 통과하고 실행 단계에서만 실패한다.
 * 외부 서비스나 스프링 컨텍스트에 의존하지 않으므로, 이 테스트가 통과했다면
 * 테스트가 아예 실행되지 않았다는 뜻이다.
 */
class CiProbeFailingTest {

    @DisplayName("의도적으로 실패하는 단정 - 테스트가 실행되면 반드시 실패한다")
    @Test
    void deliberatelyFails() {
        assertEquals(1, 2, "이 테스트는 CI가 테스트를 실행하는지 확인하기 위해 의도적으로 실패한다");
    }
}
