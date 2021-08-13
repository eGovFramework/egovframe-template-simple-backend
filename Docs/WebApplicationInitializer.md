# [참고] 순수 자바 클래스를 이용한 시동이 가능한 이유

## WebApplicationInitializer

서블릿은 3.0 이후부터 `web.xml` 없이도 서블릿 컨텍스트를 구현 가능하게 합니다.

>**서블릿 컨텍스트 초기화**
>서블릿 등록과 매핑, 리스너 등록, 필터 등록 을 담당한다.



## 스프링 프레임워크의 시동

스프링 프레임워크의 시동 방법 중 순수 자바 클래스만을 이용하여 시동 할 수 있는 방법은 아래와 같다.

1. `javax.servlet.ServletContainerInitializer` 인터페이스를 구현한 클래스 만들고 
2. 구현체의 클래스 이름을 `/META-INF/services/javax.servlet.ServletContainerInitializer` 에 준다.
3. 이 구현 클래스에 WAS 시작 시 실행 될 클래스를 `@HandlesTypes` 어노테이션으로 달아준다.



스프링의 경우 이를 spring-web 모듈 내에서 확인 가능하다.

```tex
#/META-INF/services/javax.servlet.ServletContainerInitializer

javax.servlet.ServletContainerInitializer
```



```java
package org.springframework.web;

...

@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {
    
    @Override
	public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
        
		List<WebApplicationInitializer> initializers = new inkedList<WebApplicationInitializer>();
		...
		for (WebApplicationInitializer initializer : initializers) {
			initializer.onStartup(servletContext);
		}
	}
}

```

이 `SpringServletContainerInitializer` 클래스는 `@HandlesTypes` 통해 `WebApplicationInitializer.class` 를 지정하고 있다. 이때 `onStartUp()` 메소드의 인자로, 지정된 클래스 set과 서블릿 컨텍스트 객체를 파라미터로 넣어준다.

WAS가 시작될 때 `org.springframework.web.WebApplicationInitializer` 인터페이스를 구현한 각 클래스들의 `onStartup()` 메소드가 실행되어 초기화 작업이 진행된다.

이 인터페이스를 구현한 클래스를 만들어두면 웹 어플리케이션이 시작할 때 자동으로 `onStartup()` 메서드가 실행된다.



##  `AnnotationConfigWebApplicationContext` 설정

Spring 3.1 부터는 `WebApplicationInitializer` 사용 시 직접 초기화 하고 `DispatcherServlet`이나 `ContextLoaderListener` 를 직접 주입 할 수 있다.

```java
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class EgovWebApplicationInitializer implements WebApplicationInitializer {
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
	
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(ContextApp.class);	
		servletContext.addListener(new ContextLoaderListener(rootContext));	
	}
}
```



## Context 설정

### `@Configuration` 과  `@Bean` 

설정에 관련된 클래스는 클래스 상단에 `@Configuration` 을 달아주면 된다.

Bean의 생성은 메소드를 통해 생성되며 메소드에 @Bean 을 달아주면 된다.

```java
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextApp {
    @Bean
    public BeanClass beanName(){
        return new BeanClass();
    }
}
```



### Context 설정의 모듈화

스프링 XML 파일에서 설정 모듈화에 <import/> 요소를 사용하지만 `@Import` 은 다른 설정 클래스에서 @Bean 설정을 로딩한다. 만약 필요한 XML을 불러와야 하는 상황이면 `@ImportResource`을 통해 불러 올 수 있다.

```java
import org.springframework.context.annotation.Configuration;

@Configuration
@Import({ContextAppCommon.class, ContextAppDatasource.class}) 
@ImportResource("classpath*:egovframework/spring/com/context-*.xml")
public class ContextApp {
   
}
```





참고

-------

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/support/AnnotationConfigWebApplicationContext.html

https://joont92.github.io/spring/WebApplicationInitializer/

https://offbyone.tistory.com/215

https://blog.outsider.ne.kr/785