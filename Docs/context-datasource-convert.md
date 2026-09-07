# 데이터소스 설정 (JavaConfig)

데이터소스는 [EgovConfigAppDatasource.java](../src/main/java/egovframework/com/config/EgovConfigAppDatasource.java)에서 구성합니다. [application.properties](../src/main/resources/application.properties)의 `Globals.DbType`으로 사용할 DB를 선택합니다.

| `Globals.DbType` | 구성 방식 |
| --- | --- |
| `hsql` (기본값) | 내장 HSQL DB |
| `hsql` 이외의 값 | DB별 접속 설정을 사용하는 HikariCP |

## 내장 HSQL DB

`EmbeddedDatabaseBuilder`로 DB를 생성하고 `classpath:/db/shtdb.sql`로 초기화합니다. 이때 `Globals.hsql.Url` 등의 외부 접속 설정은 사용하지 않습니다.

## 외부 DB (HikariCP)

`Globals.DbType`에 지정한 DB의 접속 정보를 읽어 `HikariDataSource`를 구성합니다. 예를 들어 `mysql`을 지정하면 다음 설정을 사용합니다.

| 설정 키 | 용도 |
| --- | --- |
| `Globals.mysql.DriverClassName` | JDBC 드라이버 클래스 |
| `Globals.mysql.Url` | JDBC 접속 URL |
| `Globals.mysql.UserName` | DB 사용자명 |
| `Globals.mysql.Password` | DB 비밀번호 |

다음은 접속 정보를 적용하는 메서드입니다. 최대 커넥션 풀 크기는 `10`으로 설정되어 있습니다.

```java
private DataSource hikariDataSource() {
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setDriverClassName(className);
    hikariDataSource.setJdbcUrl(url);
    hikariDataSource.setUsername(userName);
    hikariDataSource.setPassword(password);
    hikariDataSource.setMaximumPoolSize(10);
    return hikariDataSource;
}
```
