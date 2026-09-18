package egovframework.com.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.env.MockEnvironment;


class EgovConfigAppDatasourceTest {

    @ParameterizedTest
    @DisplayName("DB 종류가 누락되거나 허용 형식에 맞지 않으면 설정 오류가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "MYSQL", " mysql", "mysql ", "my*sql", "../mysql", "1mysql"})
    void rejectsMissingOrMalformedDbType(String dbType) {
        MockEnvironment env = new MockEnvironment();
        if (dbType != null) {
            env.setProperty("Globals.DbType", dbType);
        }
        assertThatThrownBy(() -> new EgovConfigAppDatasource(env).init())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Globals.DbType");
    }

    @Test
    @DisplayName("DB 종류에 오타가 있으면 드라이버와 URL의 누락된 설정 키를 함께 안내한다")
    void reportsBothMissingKeysForUnknownDbType() {
        MockEnvironment env = new MockEnvironment().withProperty("Globals.DbType", "mysqp");
        assertThatThrownBy(() -> new EgovConfigAppDatasource(env).init())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Globals.mysqp.DriverClassName", "Globals.mysqp.Url", "Globals.DbType");
    }

    @ParameterizedTest
    @DisplayName("드라이버 클래스명이 누락되거나 공백이면 해당 설정 키를 포함한 오류가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void rejectsMissingOrBlankDriver(String driver) {
        MockEnvironment env = externalEnvironment();
        setOrRemove(env, "Globals.mysql.DriverClassName", driver);
        assertThatThrownBy(() -> new EgovConfigAppDatasource(env).init())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Globals.mysql.DriverClassName")
                .hasMessageNotContaining("Globals.mysql.Url");
    }

    @ParameterizedTest
    @DisplayName("JDBC URL이 누락되거나 공백이면 해당 설정 키를 포함한 오류가 발생한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void rejectsMissingOrBlankUrl(String url) {
        MockEnvironment env = externalEnvironment();
        setOrRemove(env, "Globals.mysql.Url", url);
        assertThatThrownBy(() -> new EgovConfigAppDatasource(env).init())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Globals.mysql.Url")
                .hasMessageNotContaining("Globals.mysql.DriverClassName");
    }

    @Test
    @DisplayName("드라이버 로딩 실패 시 원인 예외를 보존하고 접속 정보 없이 설정 키와 확인 방법을 안내한다")
    void identifiesDriverLoadingFailureAndPreservesCause() {
        MockEnvironment env = externalEnvironment()
                .withProperty("Globals.mysql.DriverClassName", "missing.jdbc.Driver")
                .withProperty("Globals.mysql.Url", "jdbc:example:private-host")
                .withProperty("Globals.mysql.UserName", "private-user")
                .withProperty("Globals.mysql.Password", "private-password");
        EgovConfigAppDatasource configuration = new EgovConfigAppDatasource(env);
        configuration.init();
        assertThatThrownBy(configuration::dataSource)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Globals.mysql.DriverClassName", "JDBC 드라이버 의존성")
                .hasMessageNotContaining("private-host")
                .hasMessageNotContaining("private-user")
                .hasMessageNotContaining("private-password")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest
    @DisplayName("선택적 인증값을 그대로 전달하고 최대 풀 크기를 유지하며 연결 풀을 시작하지 않는다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", " value with spaces "})
    void preservesOptionalCredentialsWithoutStartingPool(String credential) {
        MockEnvironment env = externalEnvironment();
        setOrRemove(env, "Globals.mysql.UserName", credential);
        setOrRemove(env, "Globals.mysql.Password", credential);
        EgovConfigAppDatasource configuration = new EgovConfigAppDatasource(env);
        configuration.init();
        try (HikariDataSource dataSource = (HikariDataSource) configuration.dataSource()) {
            assertThat(dataSource.getUsername()).isEqualTo(credential);
            assertThat(dataSource.getPassword()).isEqualTo(credential);
            assertThat(dataSource.getMaximumPoolSize()).isEqualTo(10);
            assertThat(dataSource.getHikariPoolMXBean()).isNull();
        }
    }

    @Test
    @DisplayName("내장 HSQL은 JDBC 설정을 읽지 않고 초기화 SQL을 실행하며 기존 빈 별칭을 유지한다")
    void initializesEmbeddedSchemaWithoutReadingJdbcSettingsAndPreservesAliases() {
        // 해석할 수 없는 플레이스홀더를 넣어 HSQL 경로에서 JDBC 설정을 읽지 않는지 확인한다.
        new ApplicationContextRunner().withUserConfiguration(EgovConfigAppDatasource.class)
                .withPropertyValues("Globals.DbType=hsql", "Globals.hsql.DriverClassName=${missing.driver}",
                        "Globals.hsql.Url=${missing.url}", "Globals.hsql.UserName=${missing.user}",
                        "Globals.hsql.Password=${missing.password}")
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(DataSource.class);
                    DataSource dataSource = context.getBean(DataSource.class);
                    assertThat(context.getBean("egov.dataSource")).isSameAs(dataSource);
                    assertThat(context.getBean("egovDataSource")).isSameAs(dataSource);
                    assertThat(new JdbcTemplate(dataSource).queryForObject("SELECT COUNT(*) FROM IDS", Integer.class))
                            .isPositive();
                });
    }

    @Test
    @DisplayName("필수 JDBC 설정이 없으면 DataSource 생성 전 Spring 초기화가 실패한다")
    void failsDuringSpringInitializationBeforeCreatingDataSource() {
        new ApplicationContextRunner().withUserConfiguration(EgovConfigAppDatasource.class)
                .withPropertyValues("Globals.DbType=mysqp")
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure()).hasRootCauseInstanceOf(IllegalStateException.class)
                            .rootCause().hasMessageContaining("Globals.mysqp.DriverClassName", "Globals.mysqp.Url");
                });
    }

    private MockEnvironment externalEnvironment() {
        return new MockEnvironment().withProperty("Globals.DbType", "mysql")
                .withProperty("Globals.mysql.DriverClassName", "org.hsqldb.jdbc.JDBCDriver")
                .withProperty("Globals.mysql.Url", "jdbc:hsqldb:mem:datasource-config-test");
    }

    private void setOrRemove(MockEnvironment env, String key, String value) {
        if (value == null) {
            ((MapPropertySource) env.getPropertySources().get("mockProperties")).getSource().remove(key);
        } else {
            env.setProperty(key, value);
        }
    }
}
