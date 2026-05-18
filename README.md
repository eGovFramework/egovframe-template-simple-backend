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

개발환경에서 프로젝트 우클릭 > Run As > Spring Boot App을 통해 구동한다.

### BackEnd 구동 후 확인

구동 후, 브라우저에서 `http://localhost:포트번호/` 로 확인이 가능하다.  
초기 포트번호는 8080이며 `/src/main/resources/application.properties` 파일의 `server.port` 항목에서 변경 가능하다.  
또한, 스웨거(Swagger)에서 테스트할 때는 아래처럼 사용한다.
- 스웨거3.x에서는 `http://localhost:포트번호/swagger-ui/index.html` 로 애플리케이션의 엔드포인트를 확인 가능하다.
- 참고로, 예전 스웨거2.x에서는 `http://localhost:포트번호/swagger-ui.html` 로 애플리케이션의 엔드포인트 확인이 가능했다.
- API 인증 정보:
  - 게시판·메인 등 일반 조회성 GET 요청(예: `/mainPage`, `/board/**`, `/schedule/**`)은 JWT 토큰 인증 없이 사용 가능하다.
  - 관리(`/admin/**`, `/members/**`) 및 회원(`/mypage/**`)·게시판 관리(`/inform/**`) 경로는 GET 포함 전체 메서드에 대해 JWT 토큰 인증이 필요하다.
  - 그 외 POST/PUT/DELETE 변경계 요청도 기본적으로 JWT 토큰 인증이 필요하다.
  - 상세 규칙은 `SecurityConfig.java` 의 `filterChain()` 정의를 참고한다.
  - 인증 미통과는 401, 인증은 되었으나 권한이 부족한 경우(예: `ROLE_USER` 가 `/admin/**` 접근)에는 403 응답이 반환된다.

#### JWT 토큰 인증 방법

1. 다음 중 한 방법으로 로그인한다.
   - **React 앱(`localhost:3000`)에서 로그인** — 관리자: `admin` / `Admin@1234`, 일반사용자: `user` / `User@1234`
   - **Swagger 에서 직접 로그인** — `/auth/login-jwt` 의 [Try it out] → `password` 필드에 1차 해싱된 값을 입력한다. 해싱 방식은 프론트엔드 프로젝트의 `src/utils/passwordHash.js` 를 참고한다.
2. 로그인 성공 후 같은 브라우저로 Swagger UI 를 열면 자물쇠 표시가 있는 보호 엔드포인트도 별도 [Authorize] 절차 없이 호출할 수 있다 (JWT 가 httpOnly 쿠키 `ACCESS_TOKEN` 으로 자동 전송).
3. 외부 도구(Postman, curl 등)에서 호출하려면 DevTools → Application → Cookies 의 `ACCESS_TOKEN` Value 를 복사해 `Authorization` 헤더로 첨부한다 (Swagger 상단 [Authorize] Value 에도 동일하게 사용 가능).

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