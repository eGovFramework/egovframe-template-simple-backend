package egovframework.com.cmm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ResponseCode enum 단위 테스트
 *
 * 각 코드 값과 메시지가 설계대로 정의되어 있는지 검증한다.
 */
class ResponseCodeTest {

    @Test
    @DisplayName("SUCCESS 코드는 200 이고 메시지가 비어 있지 않다")
    void success_hasCode200AndNonEmptyMessage() {
        assertThat(ResponseCode.SUCCESS.getCode()).isEqualTo(200);
        assertThat(ResponseCode.SUCCESS.getMessage()).isNotBlank();
    }

    @Test
    @DisplayName("AUTH_ERROR 코드는 403 이다")
    void authError_hasCode403() {
        assertThat(ResponseCode.AUTH_ERROR.getCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("DELETE_ERROR 코드는 700 이다")
    void deleteError_hasCode700() {
        assertThat(ResponseCode.DELETE_ERROR.getCode()).isEqualTo(700);
    }

    @Test
    @DisplayName("SAVE_ERROR 코드는 800 이다")
    void saveError_hasCode800() {
        assertThat(ResponseCode.SAVE_ERROR.getCode()).isEqualTo(800);
    }

    @Test
    @DisplayName("INPUT_CHECK_ERROR 코드는 900 이다")
    void inputCheckError_hasCode900() {
        assertThat(ResponseCode.INPUT_CHECK_ERROR.getCode()).isEqualTo(900);
    }

    @ParameterizedTest
    @EnumSource(ResponseCode.class)
    @DisplayName("모든 ResponseCode 는 양수 코드와 비어있지 않은 메시지를 가진다")
    void allCodes_havePositiveCodeAndNonEmptyMessage(ResponseCode code) {
        assertThat(code.getCode()).isPositive();
        assertThat(code.getMessage()).isNotBlank();
    }

    @Test
    @DisplayName("ResponseCode 는 총 5개 항목으로 구성된다")
    void responseCode_hasFiveEntries() {
        assertThat(ResponseCode.values()).hasSize(5);
    }

    @Test
    @DisplayName("코드 값은 모두 서로 다르다 (중복 없음)")
    void allCodes_areUnique() {
        long distinctCount = java.util.Arrays.stream(ResponseCode.values())
                .mapToInt(ResponseCode::getCode)
                .distinct()
                .count();
        assertThat(distinctCount).isEqualTo(ResponseCode.values().length);
    }
}
