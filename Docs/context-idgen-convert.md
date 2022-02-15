# context-idgen.xml  설정 변환

> Id Generation을 위한 설정이다.



Table별 채번을 위한 Id Generation을 위한 설정으로 한번에 생성될 ID의 blockSize , ID 관리 테이블, ID 사용 테이블, ID의 머릿글자, ID의 자리수, 자리 채움 문자를 설정 할 수 있다. 

<context-idgen.xml>

```xml
<bean name="egovFileIdGnrService"
      class="egovframework.rte.fdl.idgnr.impl.EgovTableIdGnrServiceImpl"
      destroy-method="destroy">
    <property name="dataSource" ref="dataSource-${Globals.DbType}" />
    <property name="strategy" ref="fileStrategy" />
    <property name="blockSize"  value="10"/>
    <property name="table"      value="IDS"/>
    <property name="tableName"  value="FILE_ID"/>
</bean>

<bean name="fileStrategy"
      class="egovframework.rte.fdl.idgnr.impl.strategy.EgovIdGnrStrategyImpl">
    <property name="prefix" value="FILE_" />
    <property name="cipers" value="15" />
    <property name="fillChar" value="0" />
</bean>
```


<EgovConfigAppIdGen.class>

```java
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

private EgovIdGnrStrategyImpl fileStrategy() {
    EgovIdGnrStrategyImpl egovIdGnrStrategyImpl = new EgovIdGnrStrategyImpl();
    egovIdGnrStrategyImpl.setPrefix("FILE_");
    egovIdGnrStrategyImpl.setCipers(15);
    egovIdGnrStrategyImpl.setFillChar('0');
    return egovIdGnrStrategyImpl;
}

```



위의 형식이 반복되므로 이를 간단하게 builder 형태로 설정 할 수 있다.

<EgovConfigAppIdGen.class>

```java
@Bean(destroyMethod = "destroy")
public EgovTableIdGnrServiceImpl egovBBSMstrIdGnrService() {
    return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
        .setBlockSize(10)
        .setTable("IDS")
        .setTableName("BBS_ID")
        .setPreFix("BBSMSTR_")
        .setCipers(12)
        .setFillChar('0')
        .build();
}

```

