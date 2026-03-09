# 문서 자동화 지침

> **이 프로젝트는 구조 참고용 템플릿입니다.**
> AI가 새로운 서비스를 개발하면서 API 문서와 개발 문서를 자동으로 생성/갱신할 때
> 따라야 할 규칙과 절차를 정의합니다.

---

## 1. 문서 유형 및 관리 구조

```
docs/
├── DEVELOPMENT_GUIDE.md             # 개발 가이드 (AI용) - 이 파일
├── DB_DESIGN_GUIDE.md               # DB 설계 지침
├── DOCUMENTATION_AUTOMATION_GUIDE.md # 문서 자동화 지침 (이 파일)
├── api/                             # API 문서
│   ├── API_SPEC.md                  # 전체 API 명세서
│   └── CHANGELOG.md                 # API 변경 이력
├── schema/                          # DB 스키마 문서
│   ├── SCHEMA.md                    # 현재 DB 스키마 명세
│   ├── MIGRATION_LOG.md             # 마이그레이션 이력
│   └── ERD.md                       # ER 다이어그램 (텍스트)
└── dev/                             # 개발 문서
    └── ARCHITECTURE.md              # 아키텍처 결정 기록
```

---

## 2. API 문서 자동화

### 2.1 Swagger/OpenAPI 어노테이션 규칙

모든 컨트롤러에 아래 어노테이션을 반드시 포함합니다.

#### 클래스 레벨

```java
@Tag(name = "EgovXxxApiController", description = "xxx 관리")
```

#### 메서드 레벨

```java
@Operation(
    summary = "xxx 목록 조회",                              // 간결한 요약 (필수)
    description = "xxx에 대한 목록을 조회",                   // 상세 설명 (필수)
    security = {@SecurityRequirement(name = "Authorization")}, // 인증 필요 시 (선택)
    tags = {"EgovXxxApiController"}                           // 태그 그룹 (필수)
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "조회 성공"),
    @ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
    @ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
})
```

#### 파라미터

```java
// Path Variable
@Parameter(name = "xxxId", description = "xxx ID", in = ParameterIn.PATH, example = "XXX_000000000001")
@PathVariable("xxxId") String xxxId

// 인증 사용자 (Swagger에서 숨김)
@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user
```

#### DTO 필드

```java
@Schema(description = "xxx ID", example = "XXX_000000000001")
private String xxxId;

@Schema(description = "페이지 번호", example = "1")
private int pageIndex = 1;
```

#### 응답 예시 (오류 응답)

```java
@ApiResponse(
    responseCode = "403",
    description = "인가된 사용자가 아님",
    content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
            name = "403 응답 예시",
            summary = "Forbidden",
            value = "{\n  \"resultCode\": 403,\n  \"resultMessage\": \"인가된 사용자가 아님\"\n}"
        )
    )
)
```

### 2.2 API 명세서 자동 생성 절차

새 API를 추가하거나 기존 API를 수정할 때, `docs/api/API_SPEC.md`를 아래 형식으로 갱신합니다.

```markdown
### [HTTP_METHOD] /endpoint-path

**설명**: API 설명

**인증**: 필요 / 불필요

**요청 파라미터**:

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| xxxId | String | Y | Path | xxx ID |
| pageIndex | int | N | Query | 페이지 번호 (기본: 1) |

**요청 Body** (POST/PUT):

```json
{
    "field1": "value1",
    "field2": "value2"
}
```

**응답**:

```json
{
    "resultCode": 200,
    "resultMessage": "성공했습니다.",
    "result": {}
}
```

**에러 코드**:

| 코드 | 설명 |
|------|------|
| 403 | 인가된 사용자가 아님 |
| 900 | 입력값 무결성 오류 |
```

### 2.3 API 변경 이력 기록

`docs/api/CHANGELOG.md`에 아래 형식으로 기록합니다:

```markdown
## [날짜] 변경 내용

### 추가
- `[POST] /new-endpoint` - 신규 API 추가

### 수정
- `[GET] /existing-endpoint` - 파라미터 추가 (searchType)

### 삭제
- `[DELETE] /old-endpoint` - API 제거 (대체: PATCH 소프트 삭제)
```

---

## 3. DB 스키마 문서 자동화

### 3.1 스키마 문서 형식

`docs/schema/SCHEMA.md`에 아래 형식으로 관리합니다:

```markdown
## 테이블: LETTN{도메인명}

**설명**: 테이블 목적 설명

| 컬럼명 | 타입 | NULL | 기본값 | 설명 |
|--------|------|------|--------|------|
| PK_ID | varchar(20) | NOT NULL | - | PK, 식별자 |
| FIELD_NM | varchar(60) | NULL | NULL | 필드 설명 |
| USE_AT | char(1) | NOT NULL | 'Y' | 사용여부 |

**PK**: PK_ID
**FK**: PARENT_ID → PARENT_TABLE(PK_COLUMN)
**인덱스**: IDX_FIELD (FIELD_NM)
```

### 3.2 마이그레이션 이력

`docs/schema/MIGRATION_LOG.md`에 아래 형식으로 기록합니다:

```markdown
## [날짜] 변경 설명

### 변경 유형: CREATE TABLE / ALTER TABLE / CREATE INDEX

```sql
-- 실행 SQL
CREATE TABLE LETTN... (...);
```

### 영향받는 도메인
- 도메인명: 영향 설명

### 롤백 SQL
```sql
DROP TABLE LETTN...;
```
```

### 3.3 ERD 텍스트 문서

`docs/schema/ERD.md`에 텍스트 기반 ER 다이어그램을 관리합니다:

```markdown
## 도메인: {도메인명}

LETTN{부모테이블} ({설명})
  │ PK: {PK_ID}
  │
  ├──< LETTN{자식테이블1} ({설명})
  │     PK: {PK_ID} + {PK_ID2}
  │     FK: {FK_ID} → LETTN{부모테이블}.{PK_ID}
  │
  └──< LETTN{자식테이블2} ({설명})
        PK: {PK_ID}
        FK: {FK_ID} → LETTN{부모테이블}.{PK_ID}
```

---

## 4. 개발 문서 자동화

### 4.1 아키텍처 결정 기록 (ADR)

새로운 아키텍처 결정이 있을 때 `docs/dev/ARCHITECTURE.md`에 기록합니다:

```markdown
## ADR-{번호}: 결정 제목

**날짜**: YYYY-MM-DD
**상태**: 제안 / 승인 / 반려 / 대체됨

### 맥락
결정이 필요한 배경

### 결정
선택한 방법과 이유

### 결과
이 결정으로 인한 영향
```

---

## 5. 문서 갱신 트리거

AI가 아래 작업을 수행할 때 자동으로 해당 문서를 갱신해야 합니다:

| 작업 | 갱신 대상 문서 |
|------|----------------|
| 새 Controller 추가 | `API_SPEC.md`, `CHANGELOG.md` |
| API 엔드포인트 수정 | `API_SPEC.md`, `CHANGELOG.md` |
| 새 테이블 생성 | `SCHEMA.md`, `ERD.md`, `MIGRATION_LOG.md` |
| 테이블 구조 변경 | `SCHEMA.md`, `MIGRATION_LOG.md` |
| 새 도메인 모듈 추가 | `ARCHITECTURE.md`, `DEVELOPMENT_GUIDE.md` (패키지 구조 갱신) |

---

## 6. 문서 작성 원칙

1. **한국어 우선**: 모든 문서는 한국어로 작성 (코드 예시는 영어)
2. **마크다운 형식**: GitHub Flavored Markdown 사용
3. **테이블 활용**: 구조화된 정보는 마크다운 테이블로 표현
4. **코드 블록**: SQL, Java 코드는 적절한 언어 태그 포함
5. **변경 이력**: 날짜 기반 역순 정렬 (최신이 위)
6. **간결함**: 불필요한 설명 없이 핵심 정보만 포함
7. **실행 가능한 예시**: SQL, API 호출 예시는 실제 실행 가능한 형태로 작성

---

## 7. Swagger UI 연동

### 7.1 접속 정보

```
URL: http://localhost:8080/swagger-ui/index.html
API Docs: http://localhost:8080/v3/api-docs
```

### 7.2 설정 (application.properties)

```properties
springdoc.version=v4.2.0
springdoc.packages-to-scan=egovframework
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.operations-sorter=alpha
springdoc.swagger-ui.doc-expansion=none
springdoc.api-docs.path=/v3/api-docs
springdoc.api-docs.groups.enabled=true
springdoc.cache.disabled=true
```

### 7.3 OpenAPI 설정 클래스

`EgovConfigApp` 또는 `OpenApiConfig`에서 Swagger 기본 설정을 관리합니다:

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API 제목")
                .version("버전")
                .description("설명"))
            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
            .components(new Components()
                .addSecuritySchemes("Authorization",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")));
    }
}
```

---

## 8. 문서 갱신 체크리스트

코드 변경 후 아래를 확인합니다:

- [ ] Swagger 어노테이션이 모든 새 API에 추가되었는가?
- [ ] `@Schema` 어노테이션이 모든 새 DTO 필드에 추가되었는가?
- [ ] `API_SPEC.md`가 새/수정된 API를 반영하는가?
- [ ] `CHANGELOG.md`에 변경 이력이 기록되었는가?
- [ ] DB 변경 시 `SCHEMA.md`가 갱신되었는가?
- [ ] DB 변경 시 `MIGRATION_LOG.md`에 SQL이 기록되었는가?
- [ ] 새 모듈 추가 시 `ARCHITECTURE.md`가 갱신되었는가?
