package egovframework.com.config;

import javax.sql.DataSource;

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

    private final DataSource dataSource;

    private final DataSource egovDataSource;

    public EgovConfigAppIdGen(
        @Qualifier("dataSource") DataSource dataSource,
        @Qualifier("egovDataSource") DataSource egovDataSource
    ) {
        this.dataSource = dataSource;
        this.egovDataSource = egovDataSource;
    }

	// ========================================
	// 인프라 ID 생성기 (파일 첨부)
	// ========================================

	/**
	 * 첨부파일 ID Generation Config
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
	 * 첨부파일 ID Generation Strategy Config
	 * @return
	 */
	private EgovIdGnrStrategyImpl fileStrategy() {
		EgovIdGnrStrategyImpl egovIdGnrStrategyImpl = new EgovIdGnrStrategyImpl();
		egovIdGnrStrategyImpl.setPrefix("FILE_");
		egovIdGnrStrategyImpl.setCipers(15);
		egovIdGnrStrategyImpl.setFillChar('0');
		return egovIdGnrStrategyImpl;
	}

	// ========================================
	// 새 도메인 ID 생성기 등록 예시 (EgovIdGnrBuilder 사용)
	// ========================================
	//
	// 새 비즈니스 도메인의 ID 생성기가 필요하면 아래 패턴을 따라 추가하세요:
	//
	// @Bean(destroyMethod = "destroy")
	// public EgovTableIdGnrServiceImpl egovXxxIdGnrService() {
	//     return new EgovIdGnrBuilder()
	//         .setDataSource(dataSource)
	//         .setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
	//         .setBlockSize(10)
	//         .setTable("IDS")
	//         .setTableName("XXX_ID")       // IDS 테이블에 등록한 TABLE_NAME
	//         .setPreFix("XXX_")            // 생성될 ID의 접두어
	//         .setCipers(12)                // 접두어 제외 자릿수
	//         .setFillChar('0')             // 빈자리 채움 문자
	//         .build();
	// }
	//
	// IDS 테이블에도 시퀀스를 등록해야 합니다:
	// INSERT INTO IDS (TABLE_NAME, NEXT_ID) VALUES ('XXX_ID', 1);

}
