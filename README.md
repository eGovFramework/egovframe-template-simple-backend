# eGovFrame 백엔드 템플릿

![java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=JAVA&logoColor=white)
![Spring_boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![swagger](https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

이 프로젝트는 **eGovFrame 기반 백엔드 템플릿**입니다.
새로운 서비스를 개발하기 위한 구조 참고용으로, 기존 샘플 코드(게시판, 일정, 회원관리)는 삭제되었습니다.

---

## 기술 스택

| 항목 | 버전 |
|:---|:---|
| JDK | 17 |
| Spring Boot (eGovFrame Boot) | 3.5.6 |
| Spring Framework | 6.2.11 |
| Jakarta EE | 10 |
| ORM/SQL | MyBatis |
| 인증 | JWT (jjwt 0.12.6) |
| API 문서 | SpringDoc OpenAPI (Swagger) 2.6.0 |
| Build | Maven |

---

## 사전 준비

- **JDK 17** 설치
- **Maven 3.8+** 설치

---

## 실행 방법

### 개발 환경 (HSQL 내장 DB)

```bash
mvn spring-boot:run
```

기본 프로파일 `dev`로 실행됩니다. HSQL 내장 DB를 사용하므로 별도 DB 설치가 불필요합니다.

### 프로덕션 환경

```bash
java -jar target/egovframe-boot-simple-backend-*.jar --spring.profiles.active=prod
```

프로덕션 환경에서는 `application-prod.properties`에서 MySQL 등 외부 DB를 설정하세요.

### IDE에서 실행

프로젝트 우클릭 > Run As > Spring Boot App

---

## 실행 확인

| 항목 | URL |
|------|-----|
| 서비스 | `http://localhost:8080/` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| API Docs (JSON) | `http://localhost:8080/v3/api-docs` |

### JWT 인증 테스트

1. Swagger UI에서 `POST /auth/login-jwt` 실행
2. `{ "id": "admin", "password": "1" }` 입력
3. 응답의 `jToken` 값 복사
4. Swagger 상단 [Authorize] 버튼 클릭 → 토큰 입력
5. 인증이 필요한 API 테스트 가능

---

## 프로파일

| 프로파일 | 설정 파일 | DB | 용도 |
|----------|-----------|-----|------|
| dev (기본) | `application-dev.properties` | HSQL (내장) | 개발 |
| prod | `application-prod.properties` | MySQL 등 외부 | 운영 |

---

## 프로젝트 구조

```
src/main/java/egovframework/
├── com/                    # [인프라] 공통 모듈 (수정 불필요)
│   ├── config/             # Spring 설정 클래스
│   ├── jwt/                # JWT 인증
│   ├── security/           # Spring Security
│   ├── cmm/                # 공통 유틸, VO, 필터
│   └── sns/                # SNS 로그인
│
└── let/                    # [개발] 비즈니스 모듈
    ├── uat/uia/            # [인프라] 로그인/인증
    ├── utl/                # [인프라] 유틸리티
    └── {새 도메인}/         # ← 여기에 새 모듈 추가
```

---

## 새 기능 개발 시작하기

1. `let/` 하위에 새 도메인 패키지 생성
2. Controller, Service, DAO, DTO, Model 작성 (패턴은 `docs/DEVELOPMENT_GUIDE.md` 참고)
3. MyBatis SQL Mapper XML 작성 (`src/main/resources/egovframework/mapper/let/{도메인}/`)
4. DB 테이블 DDL 작성 (규칙은 `docs/DB_DESIGN_GUIDE.md` 참고)
5. `SecurityConfig.java`에 새 API 경로 등록
6. `EgovConfigAppIdGen.java`에 ID 생성기 등록 (필요 시)

---

## 문서 안내

### AI 개발 가이드 (`docs/`)

| 문서 | 설명 |
|------|------|
| `docs/DEVELOPMENT_GUIDE.md` | 코드 패턴, 네이밍 규칙, 새 기능 추가 체크리스트 |
| `docs/DB_DESIGN_GUIDE.md` | DB 설계 규칙, 테이블 생성 절차 |
| `docs/DOCUMENTATION_AUTOMATION_GUIDE.md` | 문서 자동화 규칙 |
| `docs/api/API_SPEC.md` | API 명세서 |
| `docs/schema/SCHEMA.md` | DB 스키마 명세 |
| `docs/dev/ARCHITECTURE.md` | 아키텍처 결정 기록 |

### Java Config 변환 가이드 (`Docs/`)

| 문서 | 설명 |
|------|------|
| [java-config-convert.md](Docs/java-config-convert.md) | 전체 변환 가이드 |
| [context-datasource-convert.md](Docs/context-datasource-convert.md) | DataSource 설정 변환 |
| [context-mapper-convert.md](Docs/context-mapper-convert.md) | MyBatis 설정 변환 |
| [context-transaction-convert.md](Docs/context-transaction-convert.md) | 트랜잭션 설정 변환 |
| [context-idgen-convert.md](Docs/context-idgen-convert.md) | ID 생성기 설정 변환 |
| [context-aspect-convert.md](Docs/context-aspect-convert.md) | AOP 설정 변환 |
| [context-common-convert.md](Docs/context-common-convert.md) | 공통 Bean 설정 변환 |
| [context-properties-convert.md](Docs/context-properties-convert.md) | Property 설정 변환 |
| [context-validator-convert.md](Docs/context-validator-convert.md) | Validator 설정 변환 |
| [db-schema-guide.md](Docs/db-schema-guide.md) | DB 스키마 가이드 |
| [configuration-setting-bean-regist.md](Docs/configuration-setting-bean-regist.md) | Bean 등록 규칙 |
