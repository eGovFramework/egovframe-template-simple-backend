# conext-aspect 설정 변환

> AOP 관련 설정을 하는 곳이다.
>
> Exception의 처리를 Exception Handler를 AOP를 통해서 처리하고 있다. 



각각의 Exception Handler에 대해서 Java Config로 변경은 쉽다.

<conext-aspect.xml>

```xml
<bean id="egovHandler" class="egovframework.com.cmm.EgovComExcepHndlr" />
<bean id="otherHandler" class="egovframework.com.cmm.EgovComOthersExcepHndlr" /> 
```

<ContextAppAspect.class>

```java
@Bean
public EgovComExcepHndlr egovHandler() {
	EgovComExcepHndlr egovComExcepHndlr = new EgovComExcepHndlr();
	return egovComExcepHndlr;
}

@Bean
public EgovComOthersExcepHndlr otherHandler() {
	EgovComOthersExcepHndlr egovComOthersExcepHndlr = new EgovComOthersExcepHndlr();
	return egovComOthersExcepHndlr;
}
```



각각의 `ExceptionHandleManager` 설정을 진행한다. `defaultExceptionHandleManager`, `otherExceptionHandleManager`설정을 통해 복수개의 Handler를 등록 할 수 있음을 보여준다.

<conext-aspect.xml>

```xml
<bean id="defaultExceptionHandleManager" class="egovframework.rte.fdl.cmmn.exception.manager.DefaultExceptionHandleManager">
    <property name="reqExpMatcher">
    	<ref bean="antPathMater"/>
    </property>
    <property name="patterns">
        <list>
        	<value>**service.impl.*</value>
        </list>
    </property>
    <property name="handlers">
        <list>
        	<ref bean="egovHandler" />
        </list>
    </property>
</bean>
```

<ContextAppAspect.class>

```java
@Bean
public DefaultExceptionHandleManager defaultExceptionHandleManager(ExceptionHandler egovHandler) {
    DefaultExceptionHandleManager defaultExceptionHandleManager = new DefaultExceptionHandleManager();
    defaultExceptionHandleManager.setReqExpMatcher(antPathMatcher);
    defaultExceptionHandleManager.setPatterns(new String[] {"**service.impl.*"});
    defaultExceptionHandleManager.setHandlers(new ExceptionHandler[] {egovHandler});
    return defaultExceptionHandleManager;
}
```



템플릿 내에서 Exception 발생시 실제 처리를 위한 클래스 설정

<conext-aspect.xml>

```xml
<bean id="exceptionTransfer" class="egovframework.rte.fdl.cmmn.aspect.ExceptionTransfer">
    <property name="exceptionHandlerService">
        <list>
            <ref bean="defaultExceptionHandleManager" />
            <ref bean="otherExceptionHandleManager" />
        </list>
    </property>
</bean>
```



<ContextAppAspect.class>

```java
@Bean
public ExceptionTransfer exceptionTransfer(
    @Qualifier("defaultExceptionHandleManager") DefaultExceptionHandleManager defaultExceptionHandleManager,
    @Qualifier("otherExceptionHandleManager") DefaultExceptionHandleManager otherExceptionHandleManager) {
    
    ExceptionTransfer exceptionTransfer = new ExceptionTransfer();
    exceptionTransfer.setExceptionHandlerService(new ExceptionHandlerService[] {
    	defaultExceptionHandleManager, otherExceptionHandleManager
    });
    
    return exceptionTransfer;
}
```







