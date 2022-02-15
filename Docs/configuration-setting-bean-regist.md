# `@Configuration `설정과 `@Bean `등록

## 기본 규칙

>1. 환경설정 역할을 하는 클래스에 `@Configuration`을 붙여준다.
>2. @Bean 을 붙여서 등록하고자 하는 Spring Bean을 정의한다.
>
> * 메소드 정의시 @Bean 을 붙인다.
>
>3. XML Bean 설정 방법을 따른다.
>
> * 메소드의 Return 타입은 Bean의 Class Type 이다.
> * 메소드의 이름이 Bean의 이름(XML 설정 당시 id 속성)이다.
>
>4. 특정 Bean을 Injection 받아서 Bean을 생성해야 할땐 특정 Bean을 생성하는 메소드를 직접 호출, 메소드의 파라미터, 또는 클래스레벨의 @Autowired로 특정 Bean을 Injection 받을 수 있다

### 1. 환경설정 역할을 하는 클래스에 `@Configuration`을 붙여준다.

`@Configuration` (org.springframework.context.annotation)을 붙이면 Spring은 일반 비지니스 Bean과는 달리 설정과 관련된 Bean이라 인식한다. XML 설정에서 `<Bean>` 과 같다.

<Java Code>

```java
@Configuration
public class ContextApp {
}
```



### 2. `@Bean` 을 붙여서 등록하고자 하는 Spring Bean을 정의한다.

<XML>

```xml
<bean id="egovHandler" class="egovframework.com.cmm.EgovComExcepHndlr" />
```

####  메소드 정의시 @Bean 을 붙인다.

해당 `<bean>`은 Java code의 메소드로 생성할 수 있으며, 해당 메소드에 @Bean을 붙여줌으로 bean 등록한다.

<Java Code>

```java
@Bean
public EgovComExcepHndlr egovHandler() {
	EgovComExcepHndlr egovComExcepHndlr = new EgovComExcepHndlr();
	return egovComExcepHndlr;
}
```



### 3. XML Bean 설정 방법을 따른다.

#### 메소드의 Return 타입은 Bean의 Class Type 이다.

xml 설정에서 `class`는 `<bean>`의 타입이다. `<bean>` 태그의 필수 속성은 class 속성 하나 뿐이다. 해당 `<bean>`의 타입을 객체 생성하여 return 해 준다.

#### 메소드의 이름이 Bean의 이름(XML 설정의 id 속성)이다.

`id`는 `<bean>`의 `id`를 통해 참조할 경우가 있는 경우에만 설정되며 `<bean>`의 고유한 이름이 된다. 이를 Java Code에서는 메소드 이름으로 작성을 한다.

#### 프로퍼티 주입  - ` <property>`는 멤버 변수를 등록할때 사용된다.

`<property>`는 setter로 등록하는 멤버변수의 항목들이다.

#### 프로퍼티 주입  - ` <value>`는 값을 주입 할때 사용 된다.

` <value>`는 값을 주입 할 때 사용되며, `<property>`의 속성으로 사용 될 수 도 있고, 하위 태그로도 사용 될 수 있다. 

#### 프로퍼티 주입  - ` <ref>`는 타입을 참조 할 때 사용된다.

`<ref>` 기존에 설정된 bean을 참조 할 수 있다. 

#### 프로퍼티 주입  -  collection 을 주입 할 수도 있다.`<list>`,`<set>`,`<map>`

<XML>

```xml
<bean id="defaultExceptionHandleManager" class="egovframework.rte.fdl.cmmn.exception.manager.DefaultExceptionHandleManager">
    <property name="reqExpMatcher">
        <ref bean="antPathMatcher"/>
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

Exception 발생에 대한 기본 Exception 처리를 위해 표준프레임워크 실행환경의 ExceptionTransfer를 활용하도록  설정하고 있다. ExceptionHandlerService의 구현체를 대상으로 하고 있으며 여기서는 DefaultExceptionHandleManager을 통해 이루어 진다. 이때  생성하려는 `defaultExceptionHandleManager` bean의 class type 인 `DefaultExceptionHandleManager`을 살펴보면 `AbstractExceptionHandleManager `를 상속하고 `ExceptionHandlerService`를 구현하고 있는 것을 확인 할 수 있다. 

```java
public class DefaultExceptionHandleManager extends AbstractExceptionHandleManager implements ExceptionHandlerService {
	...
}
```

`AbstractExceptionHandleManager ` 를 확인 해 보면 `patterns`, `handlers`

```java
public abstract class AbstractExceptionHandleManager {

	@Resource(name = "messageSource")
	protected MessageSource messageSource;
	...
	protected String[] patterns;
	protected ExceptionHandler[] handlers;
    protected PathMatcher pm;
	...
	public void setPatterns(String[] patterns) {
		this.patterns = patterns;
	}  
    public void setHandlers(ExceptionHandler[] handlers) {
		this.handlers = handlers;
	}    
}
```

`ExceptionHandlerService`에서 `setReqExpMatcher` 를 확인 할 수 있다.

```java
public interface ExceptionHandlerService {
	...
	public void setReqExpMatcher(PathMatcher pm);
    ...
}
```

해당 클래스 타입을 참고하여 아래와 같이 Java Code로 변환해 준다.

<Java Code>

```java
@Bean
public DefaultExceptionHandleManager defaultExceptionHandleManager(AntPathMatcher antPathMatcher, ExceptionHandler egovHandler) {
    DefaultExceptionHandleManager defaultExceptionHandleManager = new DefaultExceptionHandleManager();
    defaultExceptionHandleManager.setReqExpMatcher(antPathMatcher);
    defaultExceptionHandleManager.setPatterns(new String[] {"**service.impl.*"} );
    defaultExceptionHandleManager.setHandlers(new ExceptionHandler[] {egovHandler});
    return defaultExceptionHandleManager;
}
```

`setReqExpMatcher`에 주입하는 `antPathMater` 은 context-common.xml에서 등록한 Bean으로 여기서는 파라미터로 주입받고 있다. 

`setPatterns`에는 `String`타입으로 `egovHandler` 를 수행할 패턴을 지정해 주고 있다.

`setHandlers`에는 처리할 `ExceptionHandler`구현체를 리스트 형태로 넣어 줄 수 있다.



### 4. 특정 Bean을 Injection 받아서 Bean을 생성해야 할땐 특정 Bean을 생성하는 메소드를 직접 호출, 메소드의 파라미터, 또는 클래스레벨의 @Autowired로 특정 Bean을 Injection 받을 수 있다.

위의 예제에서 `antPathMatcher`와  `otherHandler`은 다른곳에서 등록된 Bean을 **메소드의 파라미터** 로 주입받고 있다. 이는 **@Autowired** 를 통해서 주입받는 형태로 바꿀 수도 있다.

```java
@AutowiredAntPathMatcher antPathMatcher;...
@Beanpublic DefaultExceptionHandleManager defaultExceptionHandleManager(ExceptionHandler egovHandler){
	DefaultExceptionHandleManager defaultExceptionHandleManager = new DefaultExceptionHandleManager();
	defaultExceptionHandleManager.setReqExpMatcher(antPathMatcher);
	defaultExceptionHandleManager.setPatterns(new String[] {"**service.impl.*"} );
	defaultExceptionHandleManager.setHandlers(new ExceptionHandler[] {egovHandler});
	return defaultExceptionHandleManager;}
```

이는 Bean을 주입 받는 방법이 다양하므로 다양한 형태의 코드로 구현이 가능함을 기억해 두면 좋을 것이다.

앞선 `defaultExceptionHandleManager`가 메소드의 파라미터 형태로 `egovHandler` 을 주입 받았다면 `otherExceptionHandleManager`은 **Bean을 생성하는 메소드를 직접 호출**하여 `otherHandler`을 주입 받을 수 있는 것을 확인 할 수 있을 것이다.

```java
@Beanpublic DefaultExceptionHandleManager otherExceptionHandleManager() {
	DefaultExceptionHandleManager defaultExceptionHandleManager = new DefaultExceptionHandleManager();
	defaultExceptionHandleManager.setReqExpMatcher(antPathMatcher);
	defaultExceptionHandleManager.setPatterns(new String[] {"**service.impl.*"} );
	defaultExceptionHandleManager.setHandlers(new ExceptionHandler[] {otherHandler()});
	return 	defaultExceptionHandleManager;}
```

참조

-----

https://zgundam.tistory.com/ : 

https://atoz-develop.tistory.com/entry/Spring-%EC%8A%A4%ED%94%84%EB%A7%81-XML-%EC%84%A4%EC%A0%95-%ED%8C%8C%EC%9D%BC-%EC%9E%91%EC%84%B1-%EB%B0%A9%EB%B2%95-%EC%A0%95%EB%A6%AC

