# API 명세서

> 이 문서는 프로젝트의 전체 REST API를 정리한 명세서입니다.
> 새 API를 추가할 때 아래 형식을 따라 문서화하세요.
> Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## 인증 방식

- **JWT Token** 기반 인증
- 로그인 API(`POST /auth/login-jwt`)로 토큰 발급
- 인증 필요 API는 `Authorization` 헤더에 JWT 토큰 포함

---

## 1. 인증 (EgovLoginApiController)

### [POST] /auth/login-jwt

**설명**: JWT 기반 로그인 처리

**인증**: 불필요

**요청 Body**:
```json
{
    "id": "admin",
    "password": "1"
}
```

**응답**:
```json
{
    "resultCode": "200",
    "resultMessage": "성공 !!!",
    "resultVO": { "id": "admin", "userSe": "ADM", ... },
    "jToken": "eyJhbGciOi..."
}
```

---

### [GET] /auth/logout

**설명**: JWT 로그아웃 처리 (SecurityContext 초기화)

**인증**: 필요

---

## 새 API 문서화 템플릿

아래 형식을 따라 새 API를 문서화하세요:

```markdown
### [HTTP_METHOD] /endpoint-path

**설명**: API 설명

**인증**: 필요 / 불필요

**요청 파라미터**:

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| id | String | Y | Path | 식별자 |
| pageIndex | int | N | Query | 페이지 번호 (기본: 1) |

**요청 Body** (POST/PUT):

{
    "field1": "value1"
}

**응답**:

{
    "resultCode": 200,
    "resultMessage": "성공했습니다.",
    "result": {}
}

**에러 코드**:

| 코드 | 설명 |
|------|------|
| 403 | 인가된 사용자가 아님 |
| 900 | 입력값 무결성 오류 |
```

---

## 공통 응답 코드

| 코드 | 상수 | 메시지 |
|------|------|--------|
| 200 | SUCCESS | 성공했습니다. |
| 403 | AUTH_ERROR | 인가된 사용자가 아닙니다. |
| 700 | DELETE_ERROR | 삭제 중 내부 오류가 발생했습니다. |
| 800 | SAVE_ERROR | 저장시 내부 오류가 발생했습니다. |
| 900 | INPUT_CHECK_ERROR | 입력값 무결성 오류 입니다. |
