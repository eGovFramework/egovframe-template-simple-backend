package egovframework.let.cop.smt.sim.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.cop.smt.sim.service.IndvdlSchdulManageVO;

/**
 * [일정관리][IndvdlSchdulManageDao.updateIndvdlSchdulManage] DAO 단위 테스트
 *
 * 등록은 SCHDUL_PLACE 를 저장하고 상세조회는 그 값을 돌려준다.
 * 수정도 같은 컬럼을 저장하는지 확인한다.
 */
@SpringBootTest
@Transactional
class IndvdlSchdulManageDaoUpdateTest {

	@Autowired
	private IndvdlSchdulManageDao indvdlSchdulManageDao;

	private IndvdlSchdulManageVO newSchdul(String schdulId, String schdulPlace) {
		IndvdlSchdulManageVO vo = new IndvdlSchdulManageVO();
		vo.setSchdulId(schdulId);
		vo.setSchdulSe("1");
		vo.setSchdulDeptId("ORGNZT_0000000000000");
		vo.setSchdulKindCode("01");
		vo.setSchdulBgnde("202609010900");
		vo.setSchdulEndde("202609011000");
		vo.setSchdulNm("일정장소 저장 확인");
		vo.setSchdulCn("내용");
		vo.setSchdulPlace(schdulPlace);
		vo.setSchdulIpcrCode("1");
		vo.setSchdulChargerId("USRCNFRM_00000000000");
		vo.setAtchFileId("");
		vo.setReptitSeCode("0");
		vo.setFrstRegisterId("USRCNFRM_00000000000");
		vo.setLastUpdusrId("USRCNFRM_00000000000");
		return vo;
	}

	@DisplayName("일정을 수정하면 일정장소도 함께 저장된다.")
	@Test
	void updateIndvdlSchdulManageSavesSchdulPlace() throws Exception {
		// given
		String schdulId = "SCHDUL_TEST000000001";
		indvdlSchdulManageDao.insertIndvdlSchdulManage(newSchdul(schdulId, "등록한 장소"));

		// when
		indvdlSchdulManageDao.updateIndvdlSchdulManage(newSchdul(schdulId, "수정한 장소"));

		// then
		IndvdlSchdulManageVO saved = indvdlSchdulManageDao.selectIndvdlSchdulManageDetail(newSchdul(schdulId, null));
		assertEquals("수정한 장소", saved.getSchdulPlace());
	}

}
