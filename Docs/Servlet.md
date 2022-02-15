# Servlet

> **Servlet 이란**
>
> 클라이언트의 요청을 처리하고, 그 결과를 반환하는 
> Servlet 클래스의 구현 규칙을 지킨 자바 웹 프로그래밍 기술

자바를 사용하여 웹을 만들기 위해 필요한 기술. 클라이언트가 어떠한 요청을 하면 그에 대한 결과를 다시 전송하기 위한 프로그램. 자바로 구현된 CGI .

> **CGI(Common Gateway Interface)란?**
>
> 웹 서버와 프로그램간의 교환방식. (특별한 라이브러리나 도구를 의미하는 것 X)
> 어떠한 프로그래밍언어로도 구현이가능.
> 클라이언트의 HTTP요청에 대해 특정 기능을 수행하고, HTML 문서등 프로그램의 표준 출력 결과를 클라이언트에게 전송하는 것입니다.
> 즉, 자바 어플리케이션 코딩을 하듯 웹 브라우저용 출력 화면을 만드는 방법입니다.

##  Servlet Container 역할

> **Servlet Container**
>
> Servlet을 관리해주는 Container.
>
> 서버에 Servlet을 만들었다고 해서 스스로 작동하는 것이 아님. Servlet의 동작을 관리해주는 역할을 하는 것이 바로 Servlet Container. Servlet Container는 클라이언트의 요청(Request)을 받아주고 응답(Response)할 수 있게, 웹서버와 소켓으로 통신. 
>
> ex) 톰캣(Tomcat)
>
> 톰캣은 실제로 웹 서버와 통신하여 JSP와 Servlet이 작동하는 환경을 제공해줍니다.

### 웹 서버와의 통신 지원

일반적인 통신은 소켓을 만들고, 특정 port를 Listening 하고, 연결 요청이 들어오면 스트림을 생성해서 요청을 받는다. Servlet Container는 이런 통신 과정을 API 로 제공하고 있기 때문에 우리가 쉽게 사용할 수 있다.

### 생명주기(Life Cycle) 관리
Servlet Container가 기동 시 Servlet Class를 로딩해서 인스턴스화하고, 초기화 메서드를 호출.
요청이 들어오면 적절한 Servlet 메소드를 찾아서 호출한다. 
만약 서블릿의 생명이 다하는 순간 가비지 컬렉션을 진행한다.

### 멀티스레드 지원 및 관리
Servlet Container는 해당 Servlet의 요청이 들어오면 스레드를 생성해서 작업을 수행한다. 즉 동시의 여러 요청이 들어온다면 멀티스레딩 환경으로 동시다발적인 작업을 관리한다.

### 선언적 보안관리
Servlet Container는 보안 관련된 기능을 지원한다. 따라서 서블릿 코드 안에 보안 관련된 메소드를 구현하지 않아도 된다.



## Servlet 동작 방식

1. client가 URL을 입력하면 HTTP Request가 Servlet Container로 전송합니다.![img](https://miro.medium.com/max/711/1*p3bdLuk7wjHFS0n8YJXQ_A.png)

   

2. 요청을 전송받은 Servlet Container는 HttpServletRequest, HttpServletResponse 객체를 생성합니다.
   ![img](https://miro.medium.com/max/821/1*Q4tv8s-_NYuHuE3tYbWcfg.png)

3. web.xml을 기반으로 사용자가 요청한 URL이 어느 Servlet에 대한 요청인지 찾습니다.

4. 해당 Servlet에서 service메소드를 호출한 후 클리아언트의 HTTP 프로토콜에 따라 해당 메소를 호출합니다.
   ![img](https://miro.medium.com/max/761/1*RoDdyWhZxiZ5ODWoK9XnGw.png)

5. 해당 메소드는 동적 페이지를 생성한 후 HttpServletResponse객체에 응답을 보냅니다.

6. 응답이 끝나면 HttpServletRequest, HttpServletResponse 두 객체를 소멸시킵니다.



기존 

1. 기존 방식
	1) Servlet Container가 먼저 뜨고
	2) ServletContainer 안에 등록되는 Servlet Application에다가 Spring을 연동하는 방식이다.
		ContextLoaderListener 등록 OR DispatcherServlet 등록
2. boot 방식
  1) Spring Boot Application 이 Java Application으로 먼저 뜨고, 
  2) 그 안에 tomcat이 내장 서버로 뜬다. 
  3) Servlet(ex, DispatcherServlet)을 내장 톰켓 안에다가 코드로 등록한다.



출처

------

https://mangkyu.tistory.com/14 [MangKyu's Diary]

https://jsonsang2.tistory.com/52 [리루]

https://codeburst.io/understanding-java-servlet-architecture-b74f5ea64bf4

https://jusungpark.tistory.com/15 [정리정리정리]