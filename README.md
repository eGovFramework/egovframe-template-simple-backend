# 표준프레임워크 심플홈페이지 BackEnd

![java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=JAVA&logoColor=white)
![Spring_boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![swagger](https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)  
![workflow](https://github.com/eGovFramework/egovframe-template-simple-backend/actions/workflows/maven.yml/badge.svg)

※ 본 프로젝트는 기존 JSP 뷰 방식에서 벗어나 BackEnd와 FrontEnd를 분리한 아키텍처를 제공하는 예시 프로젝트입니다. 실제 구현 시 참고용으로 활용하시기 바랍니다.

---

## 환경 설정

프로젝트에서 사용된 환경 프로그램 정보는 다음과 같습니다.
| 프로그램 명 | 버전 명 |
| :--------- | :------ |
| java | 1.8 이상 |
| maven | 3.8.4 |

---

## BackEnd 구동

### CLI 구동 방법

```bash
mvn spring-boot:run
```

### IDE에서 BackEnd 구동 방법

개발환경에서 프로젝트 우클릭 > Run As > Spring Boot App을 선택하여 애플리케이션을 구동합니다.

### BackEnd 구동 후 확인

구동 후, 브라우저에서 `http://localhost:포트번호/`로 접속하여 서비스를 확인할 수 있습니다.  
초기 포트번호는 8080이며, `/src/main/resources/application.properties` 파일의 `server.port` 항목에서 변경할 수 있습니다.  

#### Swagger API 문서 확인 및 테스트 방법
- Swagger 3.x: `http://localhost:포트번호/swagger-ui/index.html`에서 API 엔드포인트를 확인할 수 있습니다.
- Swagger 2.x(참고): `http://localhost:포트번호/swagger-ui.html`에서 API 엔드포인트를 확인할 수 있었습니다.
- API 인증 정보:
  - GET 요청은 JWT 토큰 인증 없이 사용 가능합니다.
  - POST, PUT, DELETE 요청은 JWT 토큰을 통한 인증이 필요합니다.
  - 인증되지 않은 요청은 401(403) "인가된 사용자가 아닙니다."와 같은 오류 메시지가 표시됩니다.

#### JWT 토큰 인증 방법
1. Swagger에서 `/auth/login-jwt` 엔드포인트의 [Try it out] 기능을 사용합니다.
2. 아이디/암호(admin/1)를 입력하고 [Execute]를 클릭합니다.
3. [Response body]에서 "jToken": "토큰 값"의 토큰 값을 복사합니다.
4. Swagger 상단의 [Authorize] 버튼을 클릭하고, 복사한 토큰을 Value 필드에 입력한 후 [Authorize]를 클릭합니다.
5. 인증이 완료되면 보안이 필요한 모든 엔드포인트를 사용할 수 있습니다.

---

## FrontEnd 구동 (React)

현재 FrontEnd는 React 기반으로 구성되어 있습니다.
[심플홈페이지FrontEnd](https://github.com/eGovFramework/egovframe-template-simple-react.git) 저장소에서 소스를 받아 구동하시기 바랍니다.

---

## 변경 사항

### 1. [Java Config 변환](./Docs/java-config-convert.md)

#### 1) web.xml을 WebApplicationInitializer 구현체로 변환

#### 2) context-\*.xml 파일을 @Configuration 클래스로 변환

#### 3) properties 파일 변환 (Spring Boot 지원 예정)

### 2. API 변환

기존 직접 View와 연결하던 방식에서 API 형식으로 변환하여 다양한 프론트엔드 프레임워크에서 활용할 수 있도록 예제를 제공합니다.

※ API 컨트롤러는 파일명이 `~ApiController.java` 형식으로 되어 있어 쉽게 식별할 수 있습니다.

---

## Jar 파일 실행 방법
```bash
java -jar <jar파일명> --spring.profiles.active=<profile명>
```

위 명령어를 사용하여 특정 프로필로 애플리케이션을 실행할 수 있습니다.

---

## 참고 문서 리스트

| 문서                             | 경로                                                                                     | 한줄 설명                                                                          |
|--------------------------------|----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| Configuration 설정과 Bean 등록      | [Docs/configuration-setting-bean-regist.md](Docs/configuration-setting-bean-regist.md) | 자바 기반 @Configuration/@Bean 규칙과 컴포넌트 스캔·메시지소스 등 Bean 등록 요령                      |
| Aspect 설정                      | [Docs/context-aspect.md](Docs/context-aspect.md)                                       | @EnableAspectJAutoProxy로 프록시 기반 AOP 활성화 및 AOP 개념·옵션 요약                         |
| conext-aspect 설정 변환            | [Docs/context-aspect-convert.md](Docs/context-aspect-convert.md)                       | 예외 처리 AOP(context-aspect.xml) 를 JavaConfig로 변환하는 방법(Handler/패턴/매니저 등록)         |
| context-common-convert 설정 변환   | [Docs/context-common-convert.md](Docs/context-common-convert.md)                       | 컴포넌트 스캔·메시지소스 등 공통 Bean을 context-common.xml에서 JavaConfig로 이전                   |
| context-datasource.xml 설정 변환   | [Docs/context-datasource-convert.md](Docs/context-datasource-convert.md)               | HSQL 내장 DB·DBCP BasicDataSource 등 데이터소스 설정을 JavaConfig로 변환                     |
| [참고] Context Hierarchy(확인 필요)  | [Docs/context-hierarchy.md](Docs/context-hierarchy.md)                                 | Root/Servlet WebApplicationContext 계층 구조와 역할·로딩 방식 정리                          |
| context-idgen.xml 설정 변환        | [Docs/context-idgen-convert.md](Docs/context-idgen-convert.md)                         | 테이블 기반 ID 생성기(EgovTableIdGnrServiceImpl) 전략·blockSize·관리 테이블 설정 변환             |
| context-mapper.xml 설정 변환       | [Docs/context-mapper-convert.md](Docs/context-mapper-convert.md)                       | MyBatis SqlSessionFactory/매퍼·LobHandler 설정을 XML→JavaConfig로 이전                 |
| context-properties.xml  설정 변환  | [Docs/context-properties-convert.md](Docs/context-properties-convert.md)               | EgovPropertyService에 전역 프로퍼티(pageUnit·fileStorePath 등) 등록(JavaConfig)          |
| context-transaction.xml  설정 변환 | [Docs/context-transaction-convert.md](Docs/context-transaction-convert.md)             | DataSourceTransactionManager·TransactionInterceptor 기반 트랜잭션 AOP 설정(JavaConfig) |
| context-validator.xml  설정 변환   | [Docs/context-validator-convert.md](Docs/context-validator-convert.md)                 | Commons Validator 룰 파일 로딩과 BeanValidator/Factory 설정을 JavaConfig로 변환            |
| context-whitelist.xml  설정 변환   | [Docs/context-whitelist-convert.md](Docs/context-whitelist-convert.md)                 | 페이지 링크 화이트리스트를 util:list → List<String> Bean(JavaConfig)으로 전환                  |
| [컨트리뷰터 참고 권장] DB 스키마 가이드       | [Docs/db-schema-guide.md](Docs/db-schema-guide.md)                                     | shtdb.sql 기반 테이블 용도·컬럼(한글)·제약·기본값을 정리한 DB 스키마 가이드                              |
| JavaConfig 변환                  | [Docs/java-config-convert.md](Docs/java-config-convert.md)                             | web.xml 및 context-*.xml 전반을 JavaConfig로 옮기는 전체 변환 가이드(연결 문서 포함)                |
| Servlet 개념                     | [Docs/servlet.md](Docs/servlet.md)                                                     | 서블릿/CGI 개념과 요청→서비스→응답 라이프사이클, 매핑 원리 요약                                         |
| [참고] 순수 자바 클래스를 이용한 시동이 가능한 이유 | [Docs/WebApplicationInitializer.md](Docs/WebApplicationInitializer.md)                 | Servlet 3.x WebApplicationInitializer와 Spring의 순수 자바 부트스트랩 원리                  |
| WebApplicationInitializer 변환   | [Docs/WebApplicationInitializer-convert.md](Docs/WebApplicationInitializer-convert.md) | web.xml의 리스너·컨텍스트·디스패처 설정을 EgovWebApplicationInitializer(JavaConfig)로 변환       |