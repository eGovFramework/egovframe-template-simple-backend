package egovframework.com.config;

import java.io.IOException;

import egovframework.com.cmm.util.EgovBasicLogger;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

/**
 * @ClassName : EgovConfigAppMapper.java
 * @Description : Mapper 설정
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
 *   2026. 6. 26    이백행               [2026년 컨트리뷰션] @Bean 메서드의 불필요한 public 접근제어자 제거
 * </pre>
 *
 */
@Configuration
@PropertySources({
	@PropertySource("classpath:/application.properties")
})
public class EgovConfigAppMapper {

    private final DataSource dataSource;

    private final Environment env;

    public EgovConfigAppMapper(DataSource dataSource, Environment env) {
        this.dataSource = dataSource;
        this.env = env;
    }

	private String dbType;

	@PostConstruct
	void init() {
		dbType = env.getProperty("Globals.DbType");
	}

	@Bean
	@Lazy
	DefaultLobHandler lobHandler() {
		return new DefaultLobHandler();
	}

	@Bean(name = {"sqlSession", "egov.sqlSession"})
	SqlSessionFactoryBean sqlSession() {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);

		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

		sqlSessionFactoryBean.setConfigLocation(
			pathMatchingResourcePatternResolver
				.getResource("classpath:/egovframework/mapper/config/mapper-config.xml"));

		// "classpath*:"는 추가 JAR 등 모든 클래스패스 위치를 파악하고, "**"는 하위 디렉터리 전체를 검색한다.
		String mapperPattern = String.format("classpath*:/egovframework/mapper/let/**/*_%s.xml", dbType);

		try {
			Resource[] mapperLocations = pathMatchingResourcePatternResolver.getResources(mapperPattern);

			// Globals.DbType에 해당하는 Mapper 검색 결과가 없을 경우의 예외처리
			if (mapperLocations.length == 0) {
				String message = String.format(
						"DB 설정 오류: Globals.DbType=%s에 해당하는 Mapper 파일이 없습니다. 검색 경로: %s",
						dbType, mapperPattern
				);
				throw new IllegalStateException(message);
			}

			sqlSessionFactoryBean.setMapperLocations(mapperLocations);
		} catch (IOException e) {
			// 26.03.04 KISA 보안취약점 조치
			// 구체적인 Exception 명시
			String errMsg = String.format(
					"Mapper 파일 로딩 오류: Globals.DbType=%s, 검색 경로: %s",
					dbType, mapperPattern
			);

			EgovBasicLogger.debug(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}

		return sqlSessionFactoryBean;
	}

	@Bean
	SqlSessionTemplate egovSqlSessionTemplate(@Qualifier("sqlSession") SqlSessionFactory sqlSession) {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSession);
		return sqlSessionTemplate;
	}
}
