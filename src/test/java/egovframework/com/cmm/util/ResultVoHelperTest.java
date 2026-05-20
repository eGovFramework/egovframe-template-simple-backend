package egovframework.com.cmm.util;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ResultVoHelper 단위 테스트
 *
 * Map 기반 ResultVO 생성 및 ResponseCode 매핑 로직을 검증한다.
 */
class ResultVoHelperTest {

    private ResultVoHelper helper;

    @BeforeEach
    void setUp() {
        helper = new ResultVoHelper();
    }

    // -----------------------------------------------------------------------
    // buildFromMap
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("buildFromMap 은 Map 데이터와 ResponseCode 를 ResultVO 에 담아 반환한다")
    void buildFromMap_withSuccessCode_returnsPopulatedResultVO() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", "testUser");
        data.put("age", 30);

        ResultVO result = helper.buildFromMap(data, ResponseCode.SUCCESS);

        assertThat(result.getResultCode()).isEqualTo(ResponseCode.SUCCESS.getCode());
        assertThat(result.getResultMessage()).isEqualTo(ResponseCode.SUCCESS.getMessage());
        assertThat(result.getResult()).containsEntry("userId", "testUser");
        assertThat(result.getResult()).containsEntry("age", 30);
    }

    @Test
    @DisplayName("buildFromMap 은 빈 Map 이어도 코드/메시지를 정상 설정한다")
    void buildFromMap_withEmptyMap_setsCodeAndMessage() {
        ResultVO result = helper.buildFromMap(new HashMap<>(), ResponseCode.AUTH_ERROR);

        assertThat(result.getResultCode()).isEqualTo(ResponseCode.AUTH_ERROR.getCode());
        assertThat(result.getResultMessage()).isEqualTo(ResponseCode.AUTH_ERROR.getMessage());
        assertThat(result.getResult()).isEmpty();
    }

    @Test
    @DisplayName("buildFromMap 은 SAVE_ERROR 코드를 올바르게 설정한다")
    void buildFromMap_withSaveErrorCode_setsCorrectCodeAndMessage() {
        ResultVO result = helper.buildFromMap(new HashMap<>(), ResponseCode.SAVE_ERROR);

        assertThat(result.getResultCode()).isEqualTo(800);
        assertThat(result.getResultMessage()).isEqualTo(ResponseCode.SAVE_ERROR.getMessage());
    }

    // -----------------------------------------------------------------------
    // buildFromResultVO
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("buildFromResultVO 는 기존 ResultVO 에 응답 코드와 메시지를 덮어쓴다")
    void buildFromResultVO_overridesCodeAndMessage() {
        ResultVO original = new ResultVO();
        original.putResult("key", "value");

        ResultVO updated = helper.buildFromResultVO(original, ResponseCode.INPUT_CHECK_ERROR);

        assertThat(updated.getResultCode()).isEqualTo(ResponseCode.INPUT_CHECK_ERROR.getCode());
        assertThat(updated.getResultMessage()).isEqualTo(ResponseCode.INPUT_CHECK_ERROR.getMessage());
        // 기존 result 데이터는 유지되어야 한다
        assertThat(updated.getResult()).containsKey("key");
    }

    @Test
    @DisplayName("buildFromResultVO 는 동일한 ResultVO 인스턴스를 반환한다")
    void buildFromResultVO_returnsSameInstance() {
        ResultVO original = new ResultVO();

        ResultVO returned = helper.buildFromResultVO(original, ResponseCode.SUCCESS);

        assertThat(returned).isSameAs(original);
    }

    @Test
    @DisplayName("buildFromResultVO 는 DELETE_ERROR 코드를 올바르게 설정한다")
    void buildFromResultVO_withDeleteError_setsCorrectCode() {
        ResultVO resultVO = new ResultVO();

        helper.buildFromResultVO(resultVO, ResponseCode.DELETE_ERROR);

        assertThat(resultVO.getResultCode()).isEqualTo(700);
    }
}
