# context-datasource.xml 설정 변환

기존 `context-datasource.xml`의 설정을 현재 [EgovConfigAppDatasource.java](../src/main/java/egovframework/com/config/EgovConfigAppDatasource.java)의 JavaConfig 구현과 비교합니다. XML 예제는 이전 설정 방식이며, 현재 프로젝트는 `application.properties`의 값을 읽어 DataSource를 구성합니다.

## 현재 DataSource 선택 방식

[application.properties](../src/main/resources/application.properties)의 `Globals.DbType` 값으로 사용할 DataSource를 선택합니다.

| `Globals.DbType` | 현재 구성 방식 |
| --- | --- |
| `hsql` (기본값) | `EmbeddedDatabaseBuilder`로 내장 HSQL DB 생성 |
| `hsql` 이외의 값 | 해당 DB의 접속 설정을 읽어 `HikariDataSource` 구성 |

다음은 `EgovConfigAppDatasource.java`에서 DataSource Bean을 등록하는 부분입니다.

```java
@Bean(name = {"dataSource", "egov.dataSource", "egovDataSource"})
DataSource dataSource() {
    if ("hsql".equals(dbType)) {
        return dataSourceHSQL();
    } else {
        return hikariDataSource();
    }
}
```

## 내장 HSQL DB 설정

### 이전 XML 설정

```xml
<jdbc:embedded-database id="dataSource-hsql" type="HSQL">
    <jdbc:script location= "classpath:/db/shtdb.sql"/>
</jdbc:embedded-database>
```

### 현재 JavaConfig 구현

```java
private DataSource dataSourceHSQL() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.HSQL)
        .setScriptEncoding("UTF8")
        .addScript("classpath:/db/shtdb.sql")
        //			.addScript("classpath:/otherpath/other.sql")
        .build();
}
```

기본값인 `Globals.DbType=hsql`에서는 `classpath:/db/shtdb.sql`을 읽어 내장 DB를 초기화합니다. 이 분기는 `Globals.hsql.Url` 등의 외부 접속 설정을 사용하지 않으므로, 해당 URL만 바꿔 외부 HSQL 서버에 연결되는 구조는 아닙니다.

## 외부 DB 설정

### 이전 XML 설정 (DBCP2)

다음은 기존 `BasicDataSource` 기반 설정입니다. 여기의 `Globals.DriverClassName`, `Globals.Url` 등은 이전 XML 예제의 키이며, 현재 JavaConfig는 아래에 설명한 DB 종류별 키를 사용합니다.

```xml
<!-- mysql -->
<bean id="dataSource-mysql" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${Globals.DriverClassName}"/>
    <property name="url" value="${Globals.Url}" />
    <property name="username" value="${Globals.UserName}"/>
    <property name="password" value="${Globals.Password}"/>
</bean>

<!-- Oracle -->
<bean id="dataSource-oracle" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${Globals.DriverClassName}"/>
    <property name="url" value="${Globals.Url}" />
    <property name="username" value="${Globals.UserName}"/>
    <property name="password" value="${Globals.Password}"/>
</bean>

<!-- Altibase -->
<bean id="dataSource-altibase" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${Globals.DriverClassName}"/>
    <property name="url" value="${Globals.Url}" />
    <property name="username" value="${Globals.UserName}"/>
    <property name="password" value="${Globals.Password}"/>
</bean>

<!-- Tibero -->
<bean id="dataSource-tibero" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${Globals.DriverClassName}"/>
    <property name="url" value="${Globals.Url}" />
    <property name="username" value="${Globals.UserName}"/>
    <property name="password" value="${Globals.Password}"/>
</bean>

<!-- cubrid -->
<bean id="dataSource-cubrid" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${Globals.DriverClassName}"/>
    <property name="url" value="${Globals.Url}" />
    <property name="username" value="${Globals.UserName}"/>
    <property name="password" value="${Globals.Password}"/>
</bean>
```

### 현재 JavaConfig 구현 (HikariCP)

현재 프로젝트는 `pom.xml`에 선언된 HikariCP를 사용합니다. 아래 코드는 `EgovConfigAppDatasource.java`의 일부이며, 필드와 생성자는 원본 코드를 참고합니다.

```java
@PostConstruct
void init() {
    dbType = env.getProperty("Globals.DbType");
    //Exception 처리 필요
    className = env.getProperty("Globals." + dbType + ".DriverClassName");
    url = env.getProperty("Globals." + dbType + ".Url");
    userName = env.getProperty("Globals." + dbType + ".UserName");
    password = env.getProperty("Globals." + dbType + ".Password");
}
```

예를 들어 `Globals.DbType=mysql`이면 다음 설정을 읽습니다.

| 설정 키 | 용도 |
| --- | --- |
| `Globals.mysql.DriverClassName` | JDBC 드라이버 클래스 |
| `Globals.mysql.Url` | JDBC 접속 URL |
| `Globals.mysql.UserName` | DB 사용자명 |
| `Globals.mysql.Password` | DB 비밀번호 |

읽어온 값은 다음과 같이 `HikariDataSource`에 전달합니다.

```java
import com.zaxxer.hikari.HikariDataSource;

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

기존 `BasicDataSource.setUrl()`에 해당하는 JDBC URL은 `HikariDataSource.setJdbcUrl()`로 설정합니다. 현재 코드의 최대 커넥션 풀 크기는 `setMaximumPoolSize(10)`으로 명시되어 있습니다.
