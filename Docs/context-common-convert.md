# context-common-convert  설정 변환

> 공통 설정 관련된 사항들



컴포넌트 자동 스캔 설정 변환방법

<context-common.xml>

```xml
<context:component-scan base-package="egovframework">
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Service"/>
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Repository"/>
    <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```

<EgovConfigAppCommon.class>

```java
@ComponentScan(basePackages = "egovframework", includeFilters = {
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class),
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class)
}, excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
})
```

메시지소스빈 설정

<context-common.xml>
```xml
<bean id="egovMessageSource" class="egovframework.com.cmm.EgovMessageSource">
    <property name="reloadableResourceBundleMessageSource">
        <ref bean="messageSource" />
    </property> 
</bean>
```

<EgovConfigAppMsg.class>
```java
@Bean
public EgovMessageSource egovMessageSource() {
    EgovMessageSource egovMessageSource = new EgovMessageSource();
        egovMessageSource.setReloadableResourceBundleMessageSource(messageSource());
    return egovMessageSource;
}
```


프로퍼티 파일 위치 등록

<context-common.xml>

```xml
<bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
    <property name="basenames">
        <list>
            <value>classpath:/egovframework/message/com/message-common</value>
            <value>classpath:/egovframework/rte/fdl/idgnr/messages/idgnr</value>
            <value>classpath:/egovframework/rte/fdl/property/messages/properties</value>
        </list>
    </property>
    <property name="cacheSeconds">
        <value>60</value>
    </property>
</bean>
```

<EgovConfigAppMsg.class>

```java
@Bean
public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
    String classpath = System.getProperty("java.class.path");
    reloadableResourceBundleMessageSource.setBasenames(
        "classpath:/egovframework/message/com/message-common",
        "classpath:/org/egovframe/rte/fdl/idgnr/messages/idgnr",
        "classpath:/org/egovframe/rte/fdl/property/messages/properties");
    reloadableResourceBundleMessageSource.setCacheSeconds(60);
    return reloadableResourceBundleMessageSource;
}
```



Exception 발생시 후처리용 별도작업을 위해 실행환경의 DefaultTrace Handle Manager 를 활용하도록 설정


<context-common.xml>

```xml
<bean id="traceHandlerService" class="egovframework.rte.fdl.cmmn.trace.manager.DefaultTraceHandleManager">
    <property name="reqExpMatcher">
        <ref bean="antPathMater" />
    </property>
    <property name="patterns">
        <list>
            <value>*</value>
        </list>
    </property>
    <property name="handlers">
        <list>
            <ref bean="defaultTraceHandler" />
        </list>
    </property>
</bean>
```

<EgovConfigAppCommon.class>

```java
@Bean
public DefaultTraceHandleManager traceHandlerService() {
    DefaultTraceHandleManager defaultTraceHandleManager = new DefaultTraceHandleManager();
    defaultTraceHandleManager.setReqExpMatcher(antPathMatcher());
    defaultTraceHandleManager.setPatterns(new String[] {"*"});
    defaultTraceHandleManager.setHandlers(new TraceHandler[] {defaultTraceHandler()});
    return defaultTraceHandleManager;
}

```



multipart Resolver 설정


<context-common.xml>

```xml
<bean id="spring.RegularCommonsMultipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <property name="maxUploadSize" value="100000000" />
    <property name="maxInMemorySize" value="100000000" />
</bean>
```

<EgovConfigAppCommon.class>

```java
@Bean
public CommonsMultipartResolver springRegularCommonsMultipartResolver() {
    CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
    commonsMultipartResolver.setMaxUploadSize(100000000);
    commonsMultipartResolver.setMaxInMemorySize(100000000);
    return commonsMultipartResolver;
}
```
