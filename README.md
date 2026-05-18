# 표준프레임워크 심플홈페이지 BackEnd

![java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=JAVA&logoColor=white)
![Spring_boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![swagger](https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)  
![workflow](https://github.com/eGovFramework/egovframe-template-simple-backend/actions/workflows/maven.yml/badge.svg)

※ 본 프로젝트는 기존 JSP 뷰 방식에서 벗어나 BackEnd와 FrontEnd를 분리한 아키텍처를 제공하는 예시 프로젝트입니다. 실제 구현 시 참고용으로 활용하시기 바랍니다.

---

## 환경 설정

| 항목 | 버전 |
| :--- | :--- |
| JDK | 17 |
| Jakarta EE | 10 |
| Servlet | 6.0 |
| Spring Framework | 6.2.11 |
| Spring Boot | 3.5.6 |

---

## 보안 환경변수 설정 (운영 배포 시 권장)

본 프로젝트는 오픈소스 샘플 특성상 `application.properties` 에 localhost 개발용 디폴트 placeholder 가 들어 있어 환경변수 미주입 시에도 기동됩니다. **운영 배포 시에는 아래 환경변수를 반드시 무작위 값으로 교체하여 주입**하세요. 미교체 시 기동 로그에 `[SECURITY] Default ... placeholder detected` 경고가 출력됩니다.

| 환경변수 | 용도 | 최소 길이 |
|:--------|:-----|:--------:|
| `EGOV_JWT_SECRET` | JWT 서명 키 | 32자 이상 |
| `EGOV_CRYPTO_KEY` | 첨부파일 ID ARIA 암호화 키 | 16자 이상 |

### 안전한 키 생성 방법

`EGOV_JWT_SECRET`, `EGOV_CRYPTO_KEY`는 서로 다른 값으로 설정해야 하므로, 아래 명령을 **각 키마다 한 번씩 별도로 실행**합니다.

#### Linux / macOS / Git Bash

```bash
# 48바이트 무작위 값을 Base64 문자열로 생성 — 각 키마다 별도 실행
openssl rand -base64 48
```

#### Windows PowerShell

```powershell
$bytes = New-Object byte[] 48; $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create(); $rng.GetBytes($bytes); [Convert]::ToBase64String($bytes); $rng.Dispose()
```

### 환경별 주입 방법

**개발 환경 (CLI)**
```bash
EGOV_JWT_SECRET="생성한_값" EGOV_CRYPTO_KEY="생성한_값" mvn spring-boot:run
```

**개발 환경 (IDE — Eclipse / IntelliJ)**  
Run Configurations → Environment 탭에서 `EGOV_JWT_SECRET`, `EGOV_CRYPTO_KEY` 추가

**운영 환경 (systemd)**
```ini
[Service]
Environment="EGOV_JWT_SECRET=생성한_값"
Environment="EGOV_CRYPTO_KEY=생성한_값"
```

**운영 환경 (Docker)**
```bash
docker run -e EGOV_JWT_SECRET="생성한_값" -e EGOV_CRYPTO_KEY="생성한_값" <이미지명>
```

**운영 환경 (Jar 직접 실행)**
```bash
export EGOV_JWT_SECRET="생성한_값"
export EGOV_CRYPTO_KEY="생성한_값"
java -jar <jar파일명>
```

> **주의**: 생성한 키 값을 소스코드, `application.properties`, Git 커밋에 포함하지 마세요.  
> `.gitignore`에 `*.env`, `secrets/` 가 포함되어 있는지 확인하세요.

---

## 첨부파일 저장 경로 설정 (`Globals.fileStorePath`)

게시판 첨부파일·이미지가 저장되는 디렉토리입니다. 디폴트 값은 `./files` (상대 경로) 로, JVM 의 `user.dir` 을 기준으로 해석됩니다.

이런 환경에서는 **절대 경로** 또는 **환경변수** 로 명시하기를 권장합니다.

### 프로파일 override 주의

본 프로젝트는 `application.properties` 가 base 이고, `spring.profiles.active=dev` (기본값) 이면 `application-dev.properties` 가, 운영 배포 시 `application-prod.properties` 가 추가로 로딩되어 **같은 키를 override** 합니다.

본 repo 는 dev/prod 파일의 `Globals.fileStorePath` 라인을 **이미 주석 처리**해 두었으므로 base 값이 단일 소스로 적용됩니다. dev/prod 환경별로 별도 경로를 쓰고 싶다면 주석을 해제하세요.

```properties
# application-dev.properties / application-prod.properties  (현재 상태)
# Globals.fileStorePath=./files   ← 주석 해제 시 dev/prod 값이 base 를 override 함
```

### 권장 설정 예시

**환경변수 placeholder**
```properties
# application.properties
Globals.fileStorePath=${EGOV_FILE_STORE_PATH:./files}
```
- 환경변수 주입 시 그 경로 사용, 미주입 시 `./files` fallback

**또는 절대 경로 직접 명시**
```properties
# application.properties
Globals.fileStorePath=/Users/<id>/eGov_files
```

> **체크포인트**: 지정 디렉토리는 사전 생성 + 쓰기 권한을 확인하세요.  
> `mkdir -p <경로>` 후 `ls -ld <경로>` 또는 `dir <경로>` 로 확인

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
  - 게시판·메인 등 일반 조회성 GET 요청(예: `/mainPage`, `/board/**`, `/schedule/**`)은 JWT 토큰 인증 없이 사용 가능합니다.
  - 관리(`/admin/**`, `/members/**`) 및 회원(`/mypage/**`)·게시판 관리(`/inform/**`) 경로는 GET 포함 전체 메서드에 대해 JWT 토큰 인증이 필요합니다.
  - 그 외 POST/PUT/DELETE 변경계 요청도 기본적으로 JWT 토큰 인증이 필요합니다.
  - 상세 규칙은 `SecurityConfig.java` 의 `filterChain()` 정의를 참고합니다.
  - 인증 미통과는 401, 인증은 되었으나 권한이 부족한 경우(예: `ROLE_USER` 가 `/admin/**` 접근)에는 403 응답이 반환됩니다.

#### JWT 토큰 인증 방법

1. 다음 중 한 방법으로 로그인합니다.
   - **React 앱(`localhost:3000`)에서 로그인** — 관리자: `admin` / `Admin@1234`, 일반사용자: `user` / `User@1234`
   - **Swagger 에서 직접 로그인** — `/auth/login-jwt` 의 [Try it out] → `password` 필드에 1차 해싱된 값을 입력합니다. 해싱 방식은 프론트엔드 프로젝트의 `src/utils/passwordHash.js` 를 참고합니다.
2. 로그인 성공 후 같은 브라우저로 Swagger UI 를 열면 자물쇠 표시가 있는 보호 엔드포인트도 별도 [Authorize] 절차 없이 호출할 수 있습니다.
3. 외부 도구(Postman, curl 등)에서 호출하려면 DevTools → Application → Cookies 의 `ACCESS_TOKEN` Value 를 복사해 `Authorization` 헤더로 첨부합니다 (Swagger 상단 [Authorize] Value 에도 동일하게 사용 가능).

---

## FrontEnd 구동 (React)

현재 FrontEnd는 React 기반으로 구성되어 있습니다.
[심플홈페이지FrontEnd](https://github.com/eGovFramework/egovframe-template-simple-react.git) 저장소에서 소스를 받아 구동하시기 바랍니다.

---

## 변경 사항

### 1. [Java Config 변환](./Docs/java-config-convert.md)

#### 1) web.xml을 WebApplicationInitializer 구현체로 변환

#### 2) context-\*.xml 파일을 @Configuration 클래스로 변환

#### 3) properties 파일 변환 (Spring Boot `application.properties` 기반)

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