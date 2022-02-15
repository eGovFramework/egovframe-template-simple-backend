# context-mapper.xml  설정 변환

> mapper 설정 파일을 지정해주는 설정파일



mapper 설정 파일을 등록해 준다.

<context-mapper.xml>

```xml
<bean id="egov.lobHandler" class="org.springframework.jdbc.support.lob.DefaultLobHandler" lazy-init="true" />

<!-- Mybatis setup for Mybatis Database Layer -->
<bean id="sqlSession" class="org.mybatis.spring.SqlSessionFactoryBean">		
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="classpath:/egovframework/mapper/config/mapper-config.xml" />

    <property name="mapperLocations">
        <list>
            <value>classpath:/egovframework/mapper/let/**/*_${Globals.DbType}.xml</value>
        </list>
    </property>
</bean>

<!-- Mybatis Session Template -->
<bean id="egov.sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
    <constructor-arg ref="egov.sqlSession"/>
</bean>

<alias name="sqlSession" alias="egov.sqlSession" />
```

<EgovConfigAppMapper.class>

```java
@Bean
@Lazy
public DefaultLobHandler lobHandler() {
    return new DefaultLobHandler();
}

@Bean(name = {"sqlSession", "egov.sqlSession"})
public SqlSessionFactoryBean sqlSession() {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);

    PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

    sqlSessionFactoryBean.setConfigLocation(
        pathMatchingResourcePatternResolver
        .getResource("classpath:/egovframework/mapper/config/mapper-config.xml"));

    try {
        sqlSessionFactoryBean.setMapperLocations(
            pathMatchingResourcePatternResolver
            .getResources("classpath:/egovframework/mapper/let/**/*_" + dbType + ".xml"));
    } catch (IOException e) {
        // TODO Exception 처리 필요
    }

    return sqlSessionFactoryBean;
}

@Bean
public SqlSessionTemplate egovSqlSessionTemplate(@Qualifier("sqlSession") SqlSessionFactory sqlSession) {
    SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSession);
    return sqlSessionTemplate;
}
```