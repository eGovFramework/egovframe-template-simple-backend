package egovframework.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 매퍼는 DB 타입과 무관하게 SqlSessionFactory 생성 단계를 통과한다.
 *
 * MyBatis의 {@code typeHandler} 속성은 {@code org.apache.ibatis.type.TypeHandler}
 * 구현만 받는다. iBATIS 2 계약을 따르는 핸들러를 지정하면 매퍼를 읽는 시점에
 * 팩토리 생성이 실패해 해당 DB 타입으로는 기동하지 못한다.
 *
 * @author 최완택
 * @since 2026-09-01
 * @see egovframework.com.config.EgovConfigAppMapper
 */
@DisplayName("매퍼 타입핸들러")
class EgovMapperTypeHandlerTest {

	private static final Path MAPPER_DIR = Paths.get("src/main/resources/egovframework/mapper/let");

	private static final Pattern DB_TYPE = Pattern.compile("_([a-z]+)\\.xml$");

	@Test
	@DisplayName("모든 DB 타입에서 SqlSessionFactory 가 만들어진다")
	void everyDbTypeBuildsSqlSessionFactory() throws IOException {
		Set<String> dbTypes = dbTypes();
		assertFalse(dbTypes.isEmpty(), "매퍼에서 DB 타입을 찾지 못했다: " + MAPPER_DIR);

		for (String dbType : dbTypes) {
			assertDoesNotThrow(() -> buildSqlSessionFactory(dbType), "Globals.DbType=" + dbType);
		}
	}

	private Set<String> dbTypes() throws IOException {
		Set<String> dbTypes = new TreeSet<>();
		try (Stream<Path> paths = Files.walk(MAPPER_DIR)) {
			for (Path path : paths.filter(Files::isRegularFile).collect(Collectors.toList())) {
				Matcher matcher = DB_TYPE.matcher(path.getFileName().toString());
				if (matcher.find()) {
					dbTypes.add(matcher.group(1));
				}
			}
		}
		return dbTypes;
	}

	/** {@link egovframework.com.config.EgovConfigAppMapper#sqlSession()} 과 같은 순서로 만든다. */
	private void buildSqlSessionFactory(String dbType) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(new UnpooledDataSource());
		sqlSessionFactoryBean.setConfigLocation(
			resolver.getResource("classpath:/egovframework/mapper/config/mapper-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(
			resolver.getResources("classpath:/egovframework/mapper/let/**/*_" + dbType + ".xml"));

		sqlSessionFactoryBean.afterPropertiesSet();
	}

}
