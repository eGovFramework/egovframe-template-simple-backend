# WebApplicationInitializer 변환

## Root WebApplication 등록

### 리스너 등록

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



### 설정파일 위치 변경

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



### `@Configuration` 사용

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



## Servlet Application 등록

Servlet Web Application Context는 Servlet 안에서 초기화 되고 Servlet 이 종료될 때 같이 종료 된다.

이때 사용되는 Servlet 이 DispatcherServlet이다.

기본의 DispatcherServlet 등록은 아래와 같이 작성한다.

### 설정파일 위치 변경

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



### `@Configuration` 사용

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





출처

---------

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/ContextLoader.html

https://blog.outsider.ne.kr/785

https://joont92.github.io/spring/WebApplicationInitializer/