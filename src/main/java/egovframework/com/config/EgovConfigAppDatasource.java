package egovframework.com.config;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.util.StringUtils;

/**
 * @ClassName : EgovConfigAppDatasource.java
 * @Description : DataSource 설정
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
public class EgovConfigAppDatasource {

    private final Environment env;

    public EgovConfigAppDatasource(Environment env) {
        this.env = env;
    }

	private String dbType;

	private String className;

	private String url;

	private String userName;

	private String password;

	@PostConstruct
	void init() {

		dbType = env.getProperty("Globals.DbType");

		// 우선 dbType이 null, 빈값, 공백인지 확인하고, dbType의 첫 글자는 영문 소문자, 이후는 소문자·숫자·밑줄·하이픈만 허용
		// DB 종류가 Mapper 검색 패턴에 포함되므로 경로 구분자나 와일드카드가 들어가지 않도록 제한
		// EgovConfigAppMapper.class의 Mapper 검색 패턴은 "**/mapper/**/*Mapper.xml"로 설정되어 있음
		if (!StringUtils.hasText(dbType) || !dbType.matches("[a-z][a-z0-9_-]*")) {
			String errMsg = "DB 설정 오류: Globals.DbType은 필수이며 소문자로 시작하는 [a-z][a-z0-9_-]* 형식이어야 합니다.";
			throw new IllegalStateException(errMsg);
		}

		// 내장 HSQL은 초기화 SQL로 생성하므로 외부 접속용 JDBC 설정을 읽을 필요가 없음
		if ("hsql".equals(dbType)) {
			return;
		}

		String prefix = String.format("Globals.%s.", dbType);

		className = env.getProperty(prefix + "DriverClassName");
		url = env.getProperty(prefix + "Url");

		// 한 번에 수정할 수 있도록 누락되거나 공백뿐인 필수 설정 키를 모두 수집
		List<String> missingKeys = new ArrayList<>();
		if (!StringUtils.hasText(className)) {
			missingKeys.add(prefix + "DriverClassName");
		}

		if (!StringUtils.hasText(url)) {
			missingKeys.add(prefix + "Url");
		}

		if (!missingKeys.isEmpty()) {
			String message = String.format(
					"DB 설정 오류: %s이 없거나 비어 있습니다. Globals.DbType과 해당 DB 설정을 확인하세요.",
					String.join(", ", missingKeys));
			throw new IllegalStateException(message);
		}

		// 인증과 관련된 username과 password는 공백 제거 없이 그대로 반환
		userName = env.getProperty(prefix + "UserName");
		password = env.getProperty(prefix + "Password");
	}

	/**
	 * @return [dataSource 설정] HSQL 설정
	 */
	private DataSource dataSourceHSQL() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.setScriptEncoding("UTF8")
			.addScript("classpath:/db/shtdb.sql")
			//			.addScript("classpath:/otherpath/other.sql")
			.build();
	}

	/**
	 * @return [dataSource 설정] HikariDataSource 설정
	 */
	private DataSource hikariDataSource() {
		HikariDataSource hikariDataSource = new HikariDataSource();

		try {
			hikariDataSource.setDriverClassName(className);
		} catch (RuntimeException | LinkageError e) {
			// 드라이버 로딩, 드라이버 클래스 누락, 버전 불일치 포함 등의 예외에 대해서만 예외처리
			hikariDataSource.close();
			String message = String.format(
					"DB 설정 오류: Globals.%s.DriverClassName의 드라이버를 로딩하거나 생성할 수 없습니다. 클래스명과 JDBC 드라이버 의존성을 확인하세요.",
					dbType);
			throw new IllegalStateException(message, e);
		}

		hikariDataSource.setJdbcUrl(url);
		hikariDataSource.setUsername(userName);
		hikariDataSource.setPassword(password);
		hikariDataSource.setMaximumPoolSize(10);
		return hikariDataSource;
	}

	/**
	 * @return [DataSource 설정]
	 */
	@Bean(name = {"dataSource", "egov.dataSource", "egovDataSource"})
	DataSource dataSource() {
		if ("hsql".equals(dbType)) {
			return dataSourceHSQL();
		} else {
			return hikariDataSource();
		}
	}
}
