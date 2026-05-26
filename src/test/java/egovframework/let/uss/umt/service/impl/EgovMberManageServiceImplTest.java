package egovframework.let.uss.umt.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.Arrays;
import java.util.List;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import egovframework.let.uss.umt.service.MberManageVO;
import egovframework.let.uss.umt.service.UserDefaultVO;

/**
 * 일반회원관리 서비스 단위 테스트
 * @author 테스트
 * @since 2026.05.27
 */
@ExtendWith(MockitoExtension.class)
class EgovMberManageServiceImplTest {

    @InjectMocks
    private EgovMberManageServiceImpl mberManageService;

    @Mock
    private MberManageDAO mberManageDAO;

    @Mock
    private EgovIdGnrService idgenService;

    private MberManageVO sampleVO;

    @BeforeEach
    void setUp() {
        sampleVO = new MberManageVO();
        sampleVO.setMberId("testUser");
        sampleVO.setMberNm("홍길동");
        sampleVO.setPassword("password123");
        sampleVO.setMberSttus("P");
    }

    @Test
    @DisplayName("회원 등록 시 고유아이디가 설정되고 비밀번호가 암호화된다")
    void insertMber_고유아이디설정_비밀번호암호화() throws Exception {
        // given
        given(idgenService.getNextStringId()).willReturn("USR_0000000001");
        given(mberManageDAO.insertMber(any(MberManageVO.class))).willReturn(1);

        // when
        int result = mberManageService.insertMber(sampleVO);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(sampleVO.getUniqId()).isEqualTo("USR_0000000001");
        assertThat(sampleVO.getPassword()).isNotEqualTo("password123");
        assertThat(sampleVO.getPassword()).isNotBlank();
    }

    @Test
    @DisplayName("회원 단건 조회 시 DAO 결과를 그대로 반환한다")
    void selectMber_단건조회_DAO결과반환() {
        // given
        given(mberManageDAO.selectMber("USR_0000000001")).willReturn(sampleVO);

        // when
        MberManageVO result = mberManageService.selectMber("USR_0000000001");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMberId()).isEqualTo("testUser");
        assertThat(result.getMberNm()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("회원 목록 조회 시 DAO가 반환한 목록을 그대로 반환한다")
    void selectMberList_목록조회_목록반환() {
        // given
        UserDefaultVO searchVO = new UserDefaultVO();
        List<MberManageVO> expected = Arrays.asList(sampleVO);
        given(mberManageDAO.selectMberList(searchVO)).willReturn(expected);

        // when
        List<MberManageVO> result = mberManageService.selectMberList(searchVO);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMberId()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("회원 수정 시 비밀번호가 있으면 암호화 후 저장한다")
    void updateMber_비밀번호있음_암호화후저장() throws Exception {
        // given
        sampleVO.setPassword("newpassword");

        // when
        mberManageService.updateMber(sampleVO);

        // then
        assertThat(sampleVO.getPassword()).isNotEqualTo("newpassword");
        assertThat(sampleVO.getPassword()).isNotBlank();
        then(mberManageDAO).should().updateMber(sampleVO);
    }

    @Test
    @DisplayName("회원 수정 시 비밀번호가 비어있으면 암호화 없이 저장한다")
    void updateMber_비밀번호없음_암호화건너뜀() throws Exception {
        // given
        sampleVO.setPassword("");

        // when
        mberManageService.updateMber(sampleVO);

        // then
        assertThat(sampleVO.getPassword()).isEmpty();
        then(mberManageDAO).should().updateMber(sampleVO);
    }

    @Test
    @DisplayName("아이디 중복확인 시 DAO 반환값을 그대로 반환한다")
    void checkIdDplct_중복확인_DAO결과반환() {
        // given
        given(mberManageDAO.checkIdDplct("duplicateId")).willReturn(1);

        // when
        int count = mberManageService.checkIdDplct("duplicateId");

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 삭제 시 DAO 삭제 메서드를 호출한다")
    void deleteMber_회원삭제_DAO호출() {
        // when
        mberManageService.deleteMber("USR_0000000001");

        // then
        then(mberManageDAO).should().deleteMber("USR_0000000001");
    }
}
