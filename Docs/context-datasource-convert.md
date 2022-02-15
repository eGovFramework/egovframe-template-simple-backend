# context-datasource.xml  설정 변환

> 데이터 소스관련 설정 사항들 다루고 있음



내장 DB 사용시

<context-datasource.xml>

```xml
<jdbc:embedded-database id="dataSource-hsql" type="HSQL">
    <jdbc:script location= "classpath:/db/shtdb.sql"/>
</jdbc:embedded-database>
```

<EgovConfigAppDatasource.class>

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




다른 DB 사용시

<context-datasource.xml>

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

<EgovConfigAppDatasource.class>

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

......

private DataSource basicDataSource() {
    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setDriverClassName(className);
    basicDataSource.setUrl(url);
    basicDataSource.setUsername(userName);
    basicDataSource.setPassword(password);
    return basicDataSource;
}
```