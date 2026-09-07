package egovframework.com.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.net.URLClassLoader;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.mock.env.MockEnvironment;

class EgovConfigAppMapperTest {

	@ParameterizedTest
	@DisplayName("기본 제공하는 6종 DB의 Mapper를 로딩하고 로그인 쿼리를 등록한다")
	@ValueSource(strings = {"hsql", "mysql", "oracle", "altibase", "tibero", "cubrid"})
	void loadsStatementsForEachBundledDb(String dbType) throws Exception {
		EgovConfigAppMapper configuration = new EgovConfigAppMapper(new UnpooledDataSource(),
				new MockEnvironment().withProperty("Globals.DbType", dbType));
		configuration.init();
		SqlSessionFactoryBean factory = configuration.sqlSession();
		factory.afterPropertiesSet();
		assertThat(factory.getObject().getConfiguration().hasStatement("loginDAO.actionLogin")).isTrue();
	}

	@Test
	@DisplayName("JDBC 설정이 있어도 해당 DB의 Mapper가 없으면 Spring 초기화가 실패한다")
	void failsSpringStartupWhenJdbcSettingsExistButMappersAreMissing() {
		new ApplicationContextRunner()
				.withUserConfiguration(EgovConfigAppDatasource.class, EgovConfigAppMapper.class)
				.withPropertyValues("Globals.DbType=missingdb",
						"Globals.missingdb.DriverClassName=org.hsqldb.jdbc.JDBCDriver",
						"Globals.missingdb.Url=jdbc:hsqldb:mem:missing-mapper-test")
				.run(context -> {
					assertThat(context).hasFailed();
					assertThat(context.getStartupFailure()).hasRootCauseInstanceOf(IllegalStateException.class)
							.rootCause().hasMessageContaining("Globals.DbType=missingdb", "Mapper 파일이 없습니다",
									"classpath*:/egovframework/mapper/let/**/*_missingdb.xml");
				});
	}

	@Test
	@DisplayName("새 DB 설정과 추가 클래스패스의 Mapper로 실제 조회를 수행할 수 있다")
	void supportsNewDbWithSettingsAndMappersOnAdditionalClasspath() throws Exception {
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		URL extensionRoot = getClass().getResource("/db-extension/");
		assertThat(extensionRoot).isNotNull();
		try (URLClassLoader extensionLoader = new URLClassLoader(new URL[] {extensionRoot}, original)) {
			// 별도 모듈에 Mapper가 추가된 환경을 재현하기 위해 테스트 리소스를 클래스패스 루트로 등록한다.
			Thread.currentThread().setContextClassLoader(extensionLoader);
			MockEnvironment env = new MockEnvironment().withProperty("Globals.DbType", "testdb")
					.withProperty("Globals.testdb.DriverClassName", "org.hsqldb.jdbc.JDBCDriver")
					.withProperty("Globals.testdb.Url", "jdbc:hsqldb:mem:extension-test;shutdown=true")
					.withProperty("Globals.testdb.UserName", "sa")
					.withProperty("Globals.testdb.Password", "");
			EgovConfigAppDatasource datasourceConfig = new EgovConfigAppDatasource(env);
			datasourceConfig.init();
			try (HikariDataSource dataSource = (HikariDataSource) datasourceConfig.dataSource()) {
				EgovConfigAppMapper mapperConfig = new EgovConfigAppMapper(dataSource, env);
				mapperConfig.init();
				SqlSessionFactoryBean factory = mapperConfig.sqlSession();
				factory.afterPropertiesSet();
				assertThat(dataSource.getHikariPoolMXBean()).isNull();
				try (SqlSession session = factory.getObject().openSession()) {
					assertThat(session.<Integer>selectOne("extensionTest.selectOne")).isEqualTo(1);
				}
			}
		} finally {
			// 다른 테스트의 리소스 검색에 영향을 주지 않도록 원래 클래스 로더를 반드시 복원한다.
			Thread.currentThread().setContextClassLoader(original);
		}
	}
}
