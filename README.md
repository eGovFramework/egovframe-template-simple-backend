# 표준프레임워크 심플홈페이지 BackEnd

![java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=JAVA&logoColor=white)
![Spring_boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![swagger](https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)  
![workflow](https://github.com/eGovFramework/egovframe-template-simple-backend/actions/workflows/maven.yml/badge.svg)

※ 본 프로젝트는 기존 JSP 뷰 방식에서 벗어나 BackEnd와 FrontEnd를 분리하기 위한 예시 파일로 참고만 하시길 바랍니다.  

## 환경 설정

프로젝트에서 사용된 환경 프로그램 정보는 다음과 같다.
| 프로그램 명 | 버전 명 |
| :--------- | :------ |
| java | 1.8 이상 |
| maven | 3.8.4 |

## BackEnd 구동

### CLI 구동 방법

```bash
mvn spring-boot:run
```

### IDE에서 BackEnd 구동 방법

개발환경에서 프로젝트 우클릭 > Run As > Spring Boot App을 통해 구동한다.

### BackEnd 구동 후 확인

구동 후, 브라우저에서 `http://localhost:포트번호/` 로 확인이 가능하다.  
초기 포트번호는 8080이며 `/src/main/resources/application.properties` 파일의 `server.port` 항목에서 변경 가능하다.  
또한, 스웨거(Swagger)에서 테스트할 때는 아래처럼 사용한다.
- 스웨거3.x에서는 `http://localhost:포트번호/swagger-ui/index.html` 로 애플리케이션의 엔드포인트를 확인 가능하다.
- 참고로, 예전 스웨거2.x에서는 `http://localhost:포트번호/swagger-ui.html` 로 애플리케이션의 엔드포인트 확인이 가능했다.
- 스웨거에서 GET방식으로 테스트할 때는 jwt(토큰) 인증 없이 사용 가능하다.
  단, POST,PUT,DELETE 엔드포인트를 사용 하기위해서는 jwt(토큰)을 사용해 인가된 사용자로 사용해야 한다.
  인가 받지 않고 사용하면, 401(403) "인가된 사용자가 아닙니다." 와 같은 에러 메세지를 확인하게 된다.
- [POST] 엔드포인트 사용 예), 스웨거에서 /auth/login-jwt 엔드포인트 [Try it out]에서 아이디/암호(admin/1)을 입력 및 [Execute]실행 후
  [Response body] 항목의 "jToken": "토큰 값" 에서 토큰 값을 복사하여
  [Authorize] 팝업창에서 "토큰 값"을 Value 란에 입력 후 [Authorize] 버튼을 클릭하면, 인증이 되고
  이후 [POST]와 같은 보안 인가(인증)이 필요한 엔드포인트 사용이 가능해 진다.

## FrontEnd 구동 (React)

현재 FrontEnd는 React 관련 예제로 구성되어 있다.
[심플홈페이지FrontEnd](https://github.com/eGovFramework/egovframe-template-simple-react.git) 소스를 받아 구동한다.

## 변경 사항

### 1. [Java Config 변환](./Docs/java-config-convert.md)

#### 1) Web.xml -> WebApplicationInitializer 구현체로 변환

#### 2) context-\*.xml -> @Configuration 변환

#### 3) properties 변환(예정) boot 지원

### 2. API 변환

직접 View와 연결하던 방법에서 API 형식으로 변환 -> 다양한 프론트에서 적용 가능 하도록 예제 제공\
※ API를 사용한 Controller들은 ~ApiController.java에서 확인 가능합니다.

## Jar 실행시
```bash
java -jar <jar파일명> --spring.profiles.active=<profile명>
```