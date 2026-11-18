# WebApplicationInitializer 변환

> 이 문서는 `web.xml` 기반 설정을 JavaConfig 로 옮기는 방법을 다룬다.
> `WebApplicationInitializer`을 이용하는 방식과 Spring Boot 기반에서의 방식으로 나누어 정리한다.

## 1. `web.xml` → `WebApplicationInitializer`
### Root WebApplication 등록

#### 리스너 등록

> **리스너의 역할**
> Listener는 Servlet Context가 생성하는 이벤트를 전달받는 역할을 한다.
> Servlet Context가 생성하는 이벤트는 컨텍스트 초기화 이벤트와 종료 이벤트이다.
> 즉 웹 어플리케이션이 시작과 종료 시점에 이벤트가 발생하고, 리스너를 등록해두면 이를 받을 수 있는 것이다.

<web.xml>

```xml
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>    
</listener>
```



<EgovWebApplicationInitializer.class>

```java
ServletContextListener listener = new ContextLoaderListener();
servletContext.addListener(listener);
```

여기서 한가지 궁금한 점이 생길 수 도 있을 것이다.

`WebApplicationInitializer`의 `onStartup()`은 Servlet Context 초기화 시점에 실행되는데 굳이 리스너를 등록 해야 하나 생각 할 수 있다. 

앞서 리스너는 Servlet Context가 생성하는 이벤트를 전달받는다고 했다. `onStartup()`은 초기화 시점은 정해졌지만, 종료 시점은 캐치할 수 없으므로 리스너를 등록해 놓은 것이다.



#### 설정파일 위치 변경

<web.xml>

```xml
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>
			classpath*:egovframework/spring/com/context-*.xml
	</param-value>
</context-param>
```

<EgovWebApplicationInitializer.class>

```java
servletContext.setInitParameter("contextConfigLoaction", "classpath*:egovframework/spring/com/context-*.xml");
```

이때 기본 Root WebApplication의 contextClass는 [`XmlWebApplicationContext`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/support/XmlWebApplicationContext.html)이고 기본 설정 파일 위치인 `contextConfigLoaction`은 `/WEB-INF/applicationContext.xml` 이다.



#### `@Configuration` 사용

`AnnotationConfigWebApplicationContext` 를 이용하면 Java Config 를 이용한 설정으로 사용할 수 있다.

<EgovWebApplicationInitializer.class>

```java
AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
rootContext.register(ContextApp.class);

ServletContextListener listener = new ContextLoaderListener(rootContext);
servletContext.addListener(listener);
```



`AnnotationConfigApplicationContext`와  `AnnotationConfigWebApplicationContext`는 `AnnotationConfigRegistry`를 구현하고 있으므로 인스턴스화 할때 `@Configuration`클래스들을 input으로 사용 할 수 있다.

> `@Configuration` 클래스들은 `@Component`로 메타 어노테이션이 붙은 클래스라는 것을 기억해라. 그래서 이 클래스들은 컴포넌트 스캔의 후보들이 된다. scan("[스캔하고자 하는 패키지]")을 호출하는 동안 선택될 것이고 클래스의 모든 @Bean 메서드들을 refresh() 할 때 컨테이너내 빈 정의로 처리되고 등록될 것이다.

<ContextApp.java>

```java
@Configuration
@ImportResource(value= {"classpath*:egovframework/spring/com/context-*.xml"	})
public class ContextApp {

}
```

기존의 xml 기반 context 설정 파일들은 변경하는 동안 @ImportResource 를 통해 불러오자.



### Servlet Application 등록

Servlet Web Application Context는 Servlet 안에서 초기화 되고 Servlet 이 종료될 때 같이 종료 된다.

이때 사용되는 Servlet 이 DispatcherServlet이다.

기본의 DispatcherServlet 등록은 아래와 같이 작성한다.

#### 설정파일 위치 변경

<web.xml>

```xml
<servlet>
	<servlet-name>action</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/config/egovframework/springmvc/*.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
	<servlet-name>action</servlet-name>
	<url-pattern>*.do</url-pattern>
</servlet-mapping>
```

<EgovWebApplicationInitializer.class>

```java
ServletRegistration.Dynamic dispatcher = servletContext.addServlet("action", new DispatcherServlet());
dispatcher.setInitParameter("contextConfigLocation", "/WEB-INF/config/egovframework/springmvc/*.xml");
dispatcher.setLoadOnStartup(1);

dispatcher.addMapping("*.do");
```



#### `@Configuration` 사용

`AnnotationConfigWebApplicationContext` 를 이용하면 Java Config 를 이용한 설정으로 사용할 수 있다.

<EgovWebApplicationInitializer.class>

```java
AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
webApplicationContext.register(ContextWebDispatcherServlet.class);

ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(webApplicationContext));
dispatcher.setLoadOnStartup(1);

dispatcher.addMapping("*.do");

```

<ContextWebDispatcherServlet.class>

```java
@Configuration
@ImportResource(value= { "/WEB-INF/config/egovframework/springmvc/*.xml" })
public class ContextWebDispatcherServlet {

}
```





## 2. Spring Boot 자동 구성

해당 프로젝트는 위 `WebApplicationInitializer` / `web.xml` 을 직접 작성하지 않는다.
Spring Boot 가 내장 톰캣과 `DispatcherServlet` 을 자동 구성하므로, 다음 클래스들만 둔다.

애플리케이션 진입점 — `egovframework.EgovBootApplication`

`@SpringBootApplication` 의 `main()` 에서 `SpringApplication.run(...)` 으로 기동한다(내장 톰캣·DispatcherServlet 자동 구성). `web.xml` 도, `WebApplicationInitializer` 구현도 없다.

```java
@ServletComponentScan
@SpringBootApplication
public class EgovBootApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(EgovBootApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
```

루트 설정 — `egovframework.com.config.EgovConfigApp`
`@Import` 로 `EgovConfigApp*` 설정 클래스들을 모으고 `@PropertySource` 로 프로퍼티를 로딩한다. 
(`ContextApp` + `@ImportResource` 역할을 대체)

```java
@Configuration
@Import({ EgovConfigAppAspect.class, EgovConfigAppCommon.class, EgovConfigAppDatasource.class,
          EgovConfigAppIdGen.class, EgovConfigAppProperties.class, EgovConfigAppMapper.class,
          EgovConfigAppTransaction.class, EgovConfigAppWhitelist.class })
@PropertySources({ @PropertySource("classpath:/application.properties") })
public class EgovConfigApp {
}
```

MVC 설정 — `egovframework.com.config.EgovConfigWebDispatcherServlet`
`WebMvcConfigurer` 를 구현해 Boot 가 제공하는 DispatcherServlet 을 커스터마이즈한다(인터셉터·뷰컨트롤러 등록). DispatcherServlet 을 직접 등록하지 않는다. (`ContextWebDispatcherServlet` 역할을 대체)

실행환경 리스너 — `egovframework.com.config.EgovWebServletContextListener`
`ServletContextListener` 로 `@ServletComponentScan` 을 통해 등록되며, `Globals.DbType` · `Globals.Auth` 값으로 `spring.profiles.active` 를 설정한다. (`ContextLoaderListener` 와는 목적이 다른, 프로파일 설정용 리스너)

출처

---------

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/ContextLoader.html

https://blog.outsider.ne.kr/785

https://joont92.github.io/spring/WebApplicationInitializer/