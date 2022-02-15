# Aspect 설정

## `@EnableAspectJAutoProxy`

 `@EnableAspectJAutoProxy`는 AspectJ 설정을 위해 쓰인다. AspectJ의 자동 프록시 기능이 켜지도록 설정하는 것이고, xml에 `<aop:aspectj-autoproxy/>`를 설정했던 것과 같다.

`<aop:aspectj-autoproxy/>` 는 Spring에서 AOP 구현시 AspectJ 프레임워크 (스타일) 로 구현하도록 설정하는 것이며, 여기서 실제 AspectJ를 사용하는 것이 아닌 AspectJ 스타일을 사용하는 것임을 기억해 두자.

> AspectJ이 구현하는 AOP는 **ByteCode 조작**해 구현
> Spring이 구현하는 AOP는 **Proxy패턴을 이용**해 구현 

Spring AspectJ 프레임워크는  AspectJ스타일(문법)을 사용할 뿐 내부적으로는 **Proxy 방식** 을 사용하며 이를 가능하게 해 주는것이 `@EnableAspectJAutoProxy` 이다.

### proxyTargetClass 속성  

false 설정 (아무 값도 주지 않을때 ) : 
(AOP 대상이 되는 클래스가)
	interface를 구현 O => JDK Dynamic Proxy를 만들어서
	interface를 구현 X => CGLIB를 이용해서 구현

true 설정 : CG-LIB로만 구현
(AOP 대상이 되는 클래스가)
	interface를 구현 O => CGLIB를 이용해서 구현
	interface를 구현 X => CGLIB를 이용해서 구현

> CGLIB(Code Generator Library)란
> 런타임에 동적으로 자바 클래스의 프록시를 생성해주는 기능



## Aspect 설정시 알아두면 좋을 것들

### AOP  관련 용어

> **AOP**
>
> 낮은 결합도, 높은 응집도를 위한것.

우리가 작성하는 코드 혹은 프로그램을 **핵심 로직** + **공통 로직(반복적으로 쓰이는 로직, 부가 기능)** 으로 나눠 볼 수 있다.

핵심 로직(비지니스 로직, 서비스 로직)과 공통 관심사(로깅,트랜잭션, security)를 분리하기 위해..

관심을 분리 함으로써 부가적인 공통 관심사를 AOP를 통해 효율적으로 관리, **소스상 결합은 발생하지 않는다.**



#### Separation of Concerns 

비지니스 로직을 Core Concerns + Cross-cutting Concerns 로 나눌 수 있다.

#### Core Concerns
업무의 주된 flow.
핵심 비지니스 로직.

#### Cross-cutting Concerns 
공통, 반복되는 코드(로직)
로깅, 예외, 트랜잭션 처리 같은 코드들.






#### **Advice** 
Cross-cutting Concerns을 구현한 객체. 공통로직을 담은 클래스.

#### JoinPoint (???)
모든 비지니스 메소드. 공통 관심사를 적용할 수 있는 대상이 되는 메소드.
Spring AOP에서는 각 객체의 메소드.

#### **PointCut** 
필터링된 JoinPoint. (JoinPoint의 부분집합). 
Advice가 어디에 적용될지 결정하는 역할. 다른 여러 메소드중 실제 Advice가 절용될 대상 메소드

#### **Aspect** 또는 Advisor
PointCut과 Advice의 결합. 어떤 PointCut메소드에 어떤 Advice메소드를 실행할지 결정.




#### Target 
대상 메소드를 가지는 객체

#### Proxy 
Advice가 적용 될 때 만들어 지는 객체

#### Introducing
target에 없는 새로운 메소드나 인스턴스 변수를 추가하는 기능

#### Weaving
Advice와 Target이 결합되어서 Proxy를 만드는 과정



``` ```

```<aop:config>```

스프링 설정 내 여러번 사용 가능

```
<beans xmlns="http://www.springframework.org/schema/beans" ...>
    <aop:config>
    <aop:pointcut …/>
    </aop:aspect …></aop:aspect>
    </aop:config>
</beans>
```

```<aop:pointcut>```
	PointCut 을 지정하기 위해 사용.
	```<aop:config>``` 나 ```<aop:aspect>``` 의 자식 엘리멘트로 사용
	```<aop:aspect>```하위에 설정된 포인트컷은 해당 ```<aop:aspect>```하위에서만 사용 가능
	여러개 정의 가능.

```<aop:aspect>```
	해당 관심에 해당하는 PointCut 메소드와 횡단 관심에 해당하는 Advice 메소드를 결합하기위해 사용
	설정에 따라 Weaving결과가 달라지므로 AOP에서 가장 중요한 설정

```<aop:advisor>```
	aspect와 같은 기능
	트랜잭션 설정 같은 특수한 경우는 advisor를 사용.
	AOP에서 aspect 사용시 advice의 아이디와 메소드 이름을 알아야 하지만 이를 모를때는 사용할 수 없다.
	이럴때 advisor를 사용한다.
	스프링 컨테이너는 ```<tx:advice>```엘리먼트를 해석하여 트랜잭션 관리 기능의 advice를 생성한다.
	txAdvice 설정에 따라 동작이 달라진다.
	문제는 advice 아이디는 확인되지만 메소드 이름은 확인 할 방법이 없다.


advice의 동작 시점
	* Before : 비지니스 메소드 실행 전 동작 ; <aop:before>
	* After
		* After Returning : 비지니스 메소드가 성공적으로 리턴되면 동작 ; <aop:after-returning>
		* After Throwing : 비지니스 메소드 실행 중 예외가 발생하면 동작(try~catch 블록에서 catch 블록에 해당) ; <aop:after-throwing>
		* After : 실행 된 후 무조건 실행(try~catch~finally 블록에서 finally 블록에 해당) ; <aop:after>
	* Around : 비지니스 메소드 실행 전후에 처리할 로직을 삽입할 수 있음 <aop::after-around>

``` ```
``` ```
``` ```
``` ```
``` ```
``` ```



*PointCut*

표현식 :
	excution : 

	whitin :



예제 :
<aop:after-throwing throwing="exception" pointcut-ref="serviceMethod" method="transfer" />





















참조

----

https://www.hanumoka.net/2018/09/01/spring-20180901-spring-AOP-summary/
https://icarus8050.tistory.com/8
http://closer27.github.io/backend/2017/08/03/spring-aop/

선언적 Transaction : https://freehoon.tistory.com/110
Spring AOP config 변환 : https://m.blog.naver.com/PostView.nhn?blogId=zzxx4949&logNo=221697782544&proxyReferer=https:%2F%2Fwww.google.com%2F
https://dymn.tistory.com/49
https://moonsiri.tistory.com/53

* tx변환 : https://linked2ev.github.io/gitlog/2019/10/02/springboot-mvc-15-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-MVC-Transaction-%EC%84%A4%EC%A0%95/