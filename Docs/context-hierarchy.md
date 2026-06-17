# [참고] Context Hierarchy(확인 필요)

>*Context 란*
> 필요한 정보를 포함하고 있는 설정의 집합 정도로 생각하자

스프링에서 Context의 계층관계는 부모 자식 관계로 표현 할 수 있다.

아래 처럼 Servlet Context 가 Root Context를 참조한다고 생각하면 되겠다.

![Context Hierarchy](https://docs.spring.io/spring-framework/reference/_images/mvc-context-hierarchy.png)

## Root WebApplicationContext

Middle-tier service, Datasources 등을 포함하고 있다.

View 자원 이외의 공통적으로 이용하는 자원등을 구성할때 주로 사용된다. 우리가 흔히 `@Service`, `@Repository` 등으로 작성하는 영역이 되겠다.

<web.xml>

```xml
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>
		classpath*:egovframework/spring/com/context-*.xml
	</param-value>
</context-param> 

<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

ContextLoaderListener를 통해 Root WebApplicationContext를 생성하는데,
`context-param` 엘리먼트를 통해 선언했기 때문에 
**Application의 전역**에서 사용 가능한 WebApplicationContext가 되는 것이다.



## Servlet WebbApplicationContext

Controller, view resolvers등 Web과 관련된 빈들이 모두 여기에 해당한다. 

주로 Servlet에서 사용하는 View 자원을 구성할때  사용. 우리가 흔히 `@Controller` 로 작성하는 영역이다. 

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
```

DispatcherServlet을 통해 Servlet WebApplicationContext를 생성하는데,
`servlet` 엘리먼트를 통해 선언했기 때문에 
**해당 Servlet**에서만 사용 가능한 WebApplicationContext가 되는 것이다.



## 계층 관계 컨텍스트

위의 두 context 모두  `param-name`이 `contextConfigLocation` 이라는 것을 볼 수 있다.



참조

-----

https://jayviii.tistory.com/9

https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#spring-web

