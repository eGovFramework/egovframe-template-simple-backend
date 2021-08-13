package egovframework.com.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import egovframework.com.cmm.util.EgovIdGnrBuilder;
import org.egovframe.rte.fdl.idgnr.impl.EgovTableIdGnrServiceImpl;
import org.egovframe.rte.fdl.idgnr.impl.strategy.EgovIdGnrStrategyImpl;

/**
 * @ClassName : EgovConfigAppIdGen.java
 * @Description : IdGeneration 설정
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */
@Configuration
public class EgovConfigAppIdGen {
	@Autowired
	@Qualifier("dataSource")
	DataSource dataSource;

	@Autowired
	@Qualifier("egovDataSource")
	DataSource egovDataSource;

	// 구현 방법 1:

	/**
	 * 첨부파일 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovFileIdGnrService() {
		EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
		egovTableIdGnrServiceImpl.setDataSource(dataSource);
		egovTableIdGnrServiceImpl.setStrategy(fileStrategy());
		egovTableIdGnrServiceImpl.setBlockSize(10);
		egovTableIdGnrServiceImpl.setTable("IDS");
		egovTableIdGnrServiceImpl.setTableName("FILE_ID");
		return egovTableIdGnrServiceImpl;
	}

	/**
	 * 첨부파일 ID Generation  Strategy Config
	 * @return
	 */
	private EgovIdGnrStrategyImpl fileStrategy() {
		EgovIdGnrStrategyImpl egovIdGnrStrategyImpl = new EgovIdGnrStrategyImpl();
		egovIdGnrStrategyImpl.setPrefix("FILE_");
		egovIdGnrStrategyImpl.setCipers(15);
		egovIdGnrStrategyImpl.setFillChar('0');
		return egovIdGnrStrategyImpl;
	}


	// 구현 방법 2: EgovIdGnrStrategyImpl 사용시 사용 가능

	/**
	 * 게시판마스터 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovBBSMstrIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("IDS")
			.setTableName("BBS_ID")
			.setPreFix("BBSMSTR_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	//
	/** 게시판템플릿 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovTmplatIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("IDS")
			.setTableName("TMPLAT_ID")
			.setPreFix("TMPLAT_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 부서일정 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl deptSchdulManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("IDS")
			.setTableName("SCHDUL_ID")
			.setPreFix("SCHDUL_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 위키북마크 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovWikiBookmarkIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("WIKI_ID")
			.setPreFix("WIKI")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 지식정보제공/지식정보요청 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovRequestOfferIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("KNO_ID2")
			.setPreFix("KNO_ID2")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** RSS관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovRssTagManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("RSS_ID")
			.setPreFix("RSS_ID")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 쪽지관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNoteManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NOTE_ID")
			.setPreFix("NOTE_ID")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 보낸쪽지함관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNoteTrnsmitIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NOTE_TRNSMIT_ID")
			.setPreFix("NOTE_TR")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 받은쪽지함관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNoteRecptnIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NOTE_RECPTN_ID")
			.setPreFix("NOTE_RE")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 시스템연계 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovSystemCntcIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CNTC_ID")
			.setPreFix("CNTC")
			.setCipers(4)
			.setFillChar('0')
			.build();
	}

	/** 연계기관 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCntcInsttIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("INSTT_ID")
			.setPreFix("INS")
			.setCipers(5)
			.setFillChar('0')
			.build();
	}

	/** 연계시스템 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCntcSystemIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SYS_ID")
			.setPreFix("SYS")
			.setCipers(5)
			.setFillChar('0')
			.build();
	}

	/** 연계서비스 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCntcServiceIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SVC_ID")
			.setPreFix("SVC")
			.setCipers(5)
			.setFillChar('0')
			.build();
	}

	/** 연계메시지 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCntcMessageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CNTC_MESSAGE_ID")
			.setPreFix("MSG")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** 연계메시지항목 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCntcMessageItemIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ITEM_ID")
			.setPreFix("ITM")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** 기관코드 수신 작업 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovInsttCodeRecptnIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("INSTT_CODE_OPERT")
			.build();
	}

	/** 행정코드 수신 작업 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovAdministCodeRecptnIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ADMIN_CODE_OPERT")
			.build();
	}

	/** 팝업창관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovPopupManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("POPUP_ID")
			.setPreFix("POPUP_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 최근검색어관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovSrchwrdManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SRCHWRD_MANAGEID")
			.setPreFix("SRCMGR_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 최근검색어관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovSrchwrdIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SRCHWRD_ID")
			.setPreFix("SRC_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 행정전문용어사전 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovAdministrationWordIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ADMINIST_WORD_ID")
			.setPreFix("ADMINIST_")
			.setCipers(11)
			.setFillChar('0')
			.build();
	}

	/** 개정정보보호정책확인 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovIndvdlInfoPolicyIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("INDVDL_INFO_ID")
			.setPreFix("INDVDL_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 통합링크 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovUnityLinkIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("UNITY_LINK_ID")
			.setPreFix("ULINK_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 온라인메뉴얼 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovOnlineMenualIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ONLINE_MUL_ID")
			.setPreFix("OMUL_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** 온라인POLL관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovOnlinePollManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("POLL_MGR_ID")
			.setPreFix("POLLMGR_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 온라인POLL항목 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovOnlinePollItemIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("POLL_IEM_ID")
			.setPreFix("POLLIEM_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 온라인POLL결과 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovOnlinePollResultIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("POLL_RUT_ID")
			.setPreFix("POLLRUT_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 삭제예정 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("ids")
			.setTableName("COMTNWORDDICARYINFO")
			.setPreFix("SAMPLE-")
			.setCipers(5)
			.setFillChar('0')
			.build();
	}

	/** 게시판용 NTT_ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNttIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NTT_ID")
			//.setPreFix("SAMPLE-") //TODO : 입력하지 않았을때 처리 필요
			.setCipers(20)
			.setFillChar('0')
			.build();
	}

	/** Clb ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovClbIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CLB_ID")
			.setPreFix("CLB_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 커뮤니티 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCmmntyIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CMMNTY_ID")
			.setPreFix("CMMNTY_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 커뮤니티 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovUsrCnfrmIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("USRCNFRM_ID")
			.setPreFix("USRCNFRM_")
			.setCipers(11)
			.setFillChar('0')
			.build();
	}

	/** 메일 메세지 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovMailMsgIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("MAILMSG_ID")
			.setPreFix("MAILMSG_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** RestDe NTT_ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovRestDeIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("RESTDE_ID")
			.build();
	}

	/** WordDicary ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovWordDicaryIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("WORD_ID")
			.setPreFix("WORDDICARY_")
			.setCipers(9)
			.setFillChar('0')
			.build();
	}

	/** 회의관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovMgtIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("MTG_ID")
			.setPreFix("MTG_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 행사/이벤트/켐페인 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovEventInfoIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("EVENTINFO_ID")
			.setPreFix("EVENT_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 외부인사정보 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovExtrlhrInfoIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("EXTRLHRINFO_ID")
			.setPreFix("EXTRLHR_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 설문템플릿 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovQustnrTmplatManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("QUSTNRTMPLA_ID")
			.setPreFix("QTMPLA_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 설문관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovQustnrManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("QUSTNRTMPLA_ID")
			.setPreFix("QMANAGE_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 설문문항 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovQustnrQestnManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("QUSTNRQESTN_ID")
			.setPreFix("QQESTN_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 설문항목 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovQustnrItemManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("QESTNR_QESITM_ID")
			.setPreFix("QESITM_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 설문응답자정보 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl qustnrRespondManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("QESTNR_RPD_ID")
			.setPreFix("QRPD_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** 설문조사 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl qustnrRespondInfoIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("QESRSPNS_ID")
			.setPreFix("QRSPNS_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 일정관리 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl diaryManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("DIARY_ID")
			.setPreFix("DIARY_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** SiteManage ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovSiteManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SITE_ID")
			.setPreFix("SITE_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** RecomendSiteManage ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovRecomendSiteManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("RECOMEND_SITE_ID")
			.setPreFix("RECOMEND_")
			.setCipers(11)
			.setFillChar('0')
			.build();
	}

	/** HPCMManage ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovHpcmManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("HPCM_ID")
			.setPreFix("HPCM_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** NewsManage ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovFaqManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("FAQ_ID")
			.setPreFix("FAQ_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** FaqManage ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNewsManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NEWS_ID")
			.setPreFix("NEWS_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** 명함 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNcrdIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NCRD_ID")
			.setPreFix("NCRD_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** 주소록 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovAdbkIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ADBK_ID")
			.setPreFix("ADBK_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** AdbkUser ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovAdbkUserIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ADBKUSER_ID")
			.setPreFix("ADBKUSER_")
			.setCipers(11)
			.setFillChar('0')
			.build();
	}

	/** 그룹 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovGroupIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("GROUP_ID")
			.setPreFix("GROUP_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 롤 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovRoleIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ROLE_ID")
			.setPreFix("")
			.setCipers(6)
			.setFillChar('0')
			.build();
	}

	/** StplatManage ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovStplatManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("USE_STPLAT_ID")
			.setPreFix("STPLAT_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** CpyrhtPrtcPolicy ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCpyrhtPrtcPolicyIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CPYRHT_ID")
			.setPreFix("CPYRHT_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** Qna ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovQnaManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("QA_ID")
			.setPreFix("QA_")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** Cnslt ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCnsltManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CNSLT_ID")
			.setPreFix("CNSLT_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** Login ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovLoginLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("LOGINLOG_ID")
			.setPreFix("LOGIN_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** System Log ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovSysLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SYSLOG_ID")
			.setPreFix("SYSLOG_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** Web Log. ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovWebLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("WEBLOG_ID")
			.setPreFix("WEBLOG_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** Trsmrcv. ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovTrsmrcvLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("TRSMRCVLOG_ID")
			.setPreFix("TRSMRCV_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 배너. ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovBannerIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("BANNER_ID")
			.setPreFix("BANNER_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 로그인화면이미지. ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovLoginScrinImageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("LSI_ID")
			.setPreFix("LSI_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 메인화면이미지. ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovMainImageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("MSI_ID")
			.setPreFix("MSI_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 인터넷서비스안내 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovIntnetSvcGuidanceIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ISG_ID")
			.setPreFix("ISG_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 마이페이지 컨텐츠 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovIndvdlPgeIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CNTNTS_ID")
			.setPreFix("C")
			.setCipers(19)
			.setFillChar('0')
			.build();
	}

	/** 보고서통계 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovReprtStatsIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("RS_ID")
			.setPreFix("RS_")
			.setCipers(3)
			.setFillChar('0')
			.build();
	}

	/** 자료이용현황통계 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovDtaUseStatsIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("DUS_ID")
			.setPreFix("DUS_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 자료이용현황통계 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovSmsIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SMS_ID")
			.setPreFix("SMS_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** Scrap ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovScrapIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SCRAP_ID")
			.setPreFix("SCRIP_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 부서 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovDeptManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ORGNZT_ID")
			.setPreFix("ORGNZT_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** 네트워크 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNtwrkIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NTWRK_ID")
			.setPreFix("NID_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 서버장비 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovServerEqpmnIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SEVEQ_ID")
			.setPreFix("SVE_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 서버 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovServerIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SERVER_ID")
			.setPreFix("SRV_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 장애 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovTroblIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("TROBL_ID")
			.setPreFix("TBM_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 동기화대상 서버 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovSynchrnServerIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SYNCHRNSERVER_ID")
			.setPreFix("SSY_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 회의실관리 서버 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovMtgPlaceManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("MTG_PLACE_ID")
			.setPreFix("MTGP_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** 회의실예약 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovMtgPlaceResveIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("RESVE_ID")
			.setPreFix("RESVE_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 행사 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovEventManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("EVENT_ID")
			.setPreFix("EVENT_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 행사접수 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovEventAtdrnIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("APPLCNT_ID")
			.setPreFix("APPLCNT_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 포상 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovRwardManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("RWARD_ID")
			.setPreFix("RWARD_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 경조사 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovCtsnnManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("CTSNN_ID")
			.setPreFix("RWARD_")
			.setCipers(14)
			.setFillChar('0')
			.build();
	}

	/** 기념일 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovAnnvrsryManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("ANN_ID")
			.setPreFix("ANN_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 간부일정 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovLeaderSchdulIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("LEADER_SCHDUL_ID")
			.setPreFix("LDSCHDUL_")
			.setCipers(11)
			.setFillChar('0')
			.build();
	}

	/** 부서업무함 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovDeptJobBxIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("DEPT_JOB_BX_ID")
			.setPreFix("DX_")
			.setCipers(3)
			.setFillChar('0')
			.build();
	}

	/** 부서업무 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovDeptJobIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("DEPT_JOB_ID")
			.setPreFix("DEPTJOB_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 주간/월간 보고  ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovWikMnthngReprtIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("WIKMNTHNG_REPRT")
			.setPreFix("WR")
			.setCipers(4)
			.setFillChar('0')
			.build();
	}

	/** 메모 할일  ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovMemoTodoIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("MEMO_TODO_ID")
			.setPreFix("MEMOTODO_")
			.setCipers(11)
			.setFillChar('0')
			.build();
	}

	/** 메모 보고  ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovMemoReprtIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("MEMO_REPRT")
			.setPreFix("MR")
			.setCipers(4)
			.setFillChar('0')
			.build();
	}

	/** 약식 결재   ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovInfrmlSanctnIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("INFRML_SANCTN")
			.setPreFix("SANCTN_")
			.setCipers(13)
			.setFillChar('0')
			.build();
	}

	/** DB서비스모니터링   ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovDbMntrngLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("DB_MNTRNG_LOG_ID")
			.setPreFix("")
			.setCipers(20)
			.setFillChar('0')
			.build();
	}

	/** DB송수신모니터링   ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovTrsmrcvMntrngLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("TR_MNTRNG_LOG_ID")
			.setPreFix("")
			.setCipers(20)
			.setFillChar('0')
			.build();
	}

	/** 배치작업 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovBatchOpertIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("BATCH_OPERT_ID")
			.setPreFix("BAT")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** 배치스케줄  ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovBatchSchdulIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("BATCH_SCHDUL_ID")
			.setPreFix("BSC")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** 배치결과 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovBatchResultIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("BATCH_RESULT_ID")
			.setPreFix("BRT")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** 파일시스템모니터링 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovFileSysMntrngIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("FILESYS_MNTRNG")
			.setPreFix("FILESYS_")
			.setCipers(12)
			.setFillChar('0')
			.build();
	}

	/** 네트워크서비스모니터링 로그 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovNtwrkSvcMntrngLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("NTWRKSVC_LOGID")
			.setPreFix("")
			.setCipers(20)
			.setFillChar('0')
			.build();
	}

	/** 파일시스템모니터링 로그 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovFileSysMntrngLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("FILESYS_LOGID")
			.setPreFix("")
			.setCipers(20)
			.setFillChar('0')
			.build();
	}

	/** 프록시서비스 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovProxySvcIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("PROXYSVC_ID")
			.setPreFix("PXY_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** 프록시Log ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovProxyLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("PROXYLOG_ID")
			.setPreFix("PLG_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** knoManage ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovDamManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("DAM_ID")
			.setPreFix("DMID_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** 백업작업 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovBackupOpertIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("BACKUP_OPERT_ID")
			.setPreFix("BAK")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** 백업결과 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovBackupResultIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("BACKUP_RESULT_ID")
			.setPreFix("BRT")
			.setCipers(17)
			.setFillChar('0')
			.build();
	}

	/** 서버자원 모니터링 ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovServerResrceMntrngLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("SVCRESMONTLOG_ID")
			.setPreFix("LOG_")
			.setCipers(16)
			.setFillChar('0')
			.build();
	}

	/** HttpMon ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovHttpManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("HTTP_ID")
			.setPreFix("HTTP_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** HttpMonLog ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovHttpLogManageIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("HTTL_ID")
			.setPreFix("HTTL_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** HttpMonLog ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovProcessMonIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("PROC_ID")
			.setPreFix("PROC_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

	/** ProcessMonLog ID Generation  Config
	 * @return
	 */
	@Bean(destroyMethod = "destroy")
	public EgovTableIdGnrServiceImpl egovProcessMonLogIdGnrService() {
		return new EgovIdGnrBuilder().setDataSource(egovDataSource)
			.setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
			.setBlockSize(10)
			.setTable("COMTECOPSEQ")
			.setTableName("PROL_ID")
			.setPreFix("PROL_")
			.setCipers(15)
			.setFillChar('0')
			.build();
	}

}
