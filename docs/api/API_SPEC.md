# API 명세서

> 이 문서는 egovframe-template-simple-backend 프로젝트의 전체 REST API를 정리한 명세서입니다.
> Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## 인증 방식

- **JWT Token** 기반 인증
- 로그인 API(`POST /auth/login-jwt`)로 토큰 발급
- 인증 필요 API는 `Authorization` 헤더에 JWT 토큰 포함
- GET 요청은 일반적으로 인증 없이 접근 가능

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

| 응답 코드 | 설명 |
|-----------|------|
| 200 | 로그인 성공 |
| 300 | 로그인 실패 |

---

### [GET] /auth/logout

**설명**: JWT 로그아웃 처리 (SecurityContext 초기화)

**인증**: 필요

**응답**:
```json
{
    "resultCode": 200,
    "resultMessage": "성공했습니다."
}
```

---

## 2. 메인 페이지 (EgovMainApiController)

### [GET] /mainPage

**설명**: 메인 페이지 데이터 조회 (공지사항 + 갤러리 최신 5건)

**인증**: 불필요

**응답**:
```json
{
    "resultCode": 200,
    "resultMessage": "성공했습니다.",
    "result": {
        "notiList": [...],
        "galList": [...]
    }
}
```

---

## 3. 게시물 관리 (EgovBBSManageApiController)

### [GET] /boardFileAtch/{bbsId}

**설명**: 게시판 파일 첨부 가능 여부 및 첨부 가능 파일 수 조회

**인증**: 불필요

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| bbsId | String | Y | Path | 게시판 ID (예: BBSMSTR_AAAAAAAAAAAA) |

---

### [GET] /board

**설명**: 게시물 목록 조회

**인증**: 필요 (LoginVO)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| bbsId | String | Y | Query | 게시판 ID |
| pageIndex | int | N | Query | 페이지 번호 (기본: 1) |
| searchCnd | String | N | Query | 검색 조건 (0: 제목, 1: 내용, 2: 작성자) |
| searchWrd | String | N | Query | 검색어 |

---

### [GET] /board/{bbsId}/{nttId}

**설명**: 게시물 상세 조회 (조회수 증가)

**인증**: 필요 (LoginVO)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| bbsId | String | Y | Path | 게시판 ID |
| nttId | String | Y | Path | 게시물 ID |

---

### [POST] /board

**설명**: 게시물 등록 (Multipart 지원)

**인증**: 필요 (JWT)

**요청**: `multipart/form-data` (BoardVO 필드 + 첨부파일)

---

### [PUT] /board/{nttId}

**설명**: 게시물 수정 (Multipart 지원)

**인증**: 필요 (JWT)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| nttId | String | Y | Path | 게시물 ID |

---

### [PATCH] /board/{bbsId}/{nttId}

**설명**: 게시물 삭제 (소프트 삭제)

**인증**: 필요 (JWT)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| bbsId | String | Y | Path | 게시판 ID |
| nttId | String | Y | Path | 게시물 ID |

**요청 Body**:
```json
{
    "atchFileId": "FILE_0000000000001"
}
```

---

### [POST] /boardReply

**설명**: 게시물 답변 등록

**인증**: 필요 (JWT)

---

## 4. 게시판 속성 관리 (EgovBBSAttributeManageApiController)

### [GET] /bbsMaster

**설명**: 게시판 마스터 목록 조회

**인증**: 필요 (LoginVO)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| pageIndex | int | N | Query | 페이지 번호 |
| searchCnd | String | N | Query | 검색 조건 |
| searchWrd | String | N | Query | 검색어 |

---

### [GET] /bbsMaster/{bbsId}

**설명**: 게시판 마스터 상세 조회

**인증**: 필요 (LoginVO)

---

### [POST] /bbsMaster

**설명**: 게시판 마스터 등록

**인증**: 필요 (JWT)

---

### [PUT] /bbsMaster/{bbsId}

**설명**: 게시판 마스터 수정

**인증**: 필요 (JWT)

---

### [PATCH] /bbsMaster/{bbsId}

**설명**: 게시판 마스터 삭제 (소프트 삭제)

**인증**: 필요 (JWT)

---

## 5. 일정 관리 (EgovIndvdlSchdulManageApiController)

### [GET] /schedule/month

**설명**: 월별 일정 목록 조회

**인증**: 필요 (LoginVO)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| year | String | N | Query | 연도 |
| month | String | N | Query | 월 (0-based) |
| schdulSe | String | N | Query | 일정 구분 |

---

### [GET] /schedule/week

**설명**: 주간별 일정 목록 조회

**인증**: 불필요

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| year | String | N | Query | 연도 |
| month | String | N | Query | 월 |
| date | String | N | Query | 일 |
| schdulSe | String | N | Query | 일정 구분 |

---

### [GET] /schedule/daily

**설명**: 일별 일정 목록 조회

**인증**: 불필요

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| year | String | N | Query | 연도 |
| month | String | N | Query | 월 |
| date | String | N | Query | 일 |
| schdulSe | String | N | Query | 일정 구분 |

---

### [GET] /schedule/{schdulId}

**설명**: 일정 상세 조회

**인증**: 필요 (LoginVO)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| schdulId | String | Y | Path | 일정 ID (예: SCHDUL_0000000000001) |

---

### [POST] /schedule

**설명**: 일정 등록 (Multipart 지원)

**인증**: 필요 (JWT)

---

### [PUT] /schedule/{schdulId}

**설명**: 일정 수정 (Multipart 지원)

**인증**: 필요 (JWT)

---

### [DELETE] /schedule/{schdulId}

**설명**: 일정 삭제

**인증**: 필요 (JWT)

---

## 6. 회원 관리 (EgovMberManageApiController)

### [GET] /members

**설명**: 관리자단 회원 목록 조회

**인증**: 필요 (JWT)

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| pageIndex | int | N | Query | 페이지 번호 |
| searchCnd | String | N | Query | 검색 조건 |
| searchWrd | String | N | Query | 검색어 |

---

### [GET] /members/insert

**설명**: 회원 등록 화면 초기 데이터 (코드 목록)

**인증**: 필요 (JWT)

---

### [POST] /members/insert

**설명**: 관리자단 회원 등록 처리

**인증**: 필요 (JWT)

---

### [GET] /members/update/{uniqId}

**설명**: 회원 수정용 상세 조회

**인증**: 필요 (JWT)

---

### [PUT] /members/update

**설명**: 관리자단 회원 수정 처리

**인증**: 필요 (JWT)

---

### [DELETE] /members/delete/{uniqId}

**설명**: 관리자단 회원 삭제 처리

**인증**: 필요 (JWT)

---

### [GET] /mypage

**설명**: 사용자단 내 정보 조회

**인증**: 필요 (JWT)

---

### [PUT] /mypage/update

**설명**: 사용자단 내 정보 수정

**인증**: 필요 (JWT)

---

### [PUT] /mypage/delete

**설명**: 사용자단 회원 탈퇴 (상태를 'D'로 변경)

**인증**: 필요 (JWT)

---

### [GET] /etc/member_agreement

**설명**: 회원 약관 조회

**인증**: 불필요

---

### [GET] /etc/member_insert

**설명**: 회원 가입 화면 초기 데이터

**인증**: 불필요

---

### [POST] /etc/member_insert

**설명**: 사용자단 회원 가입 처리

**인증**: 불필요

---

### [GET] /etc/member_checkid/{checkid}

**설명**: 사용자 ID 중복 체크

**인증**: 불필요

| 파라미터 | 타입 | 필수 | 위치 | 설명 |
|----------|------|------|------|------|
| checkid | String | Y | Path | 확인할 사용자 ID |

---

## 7. 사용자 관리 (EgovSiteManagerApiController)

### [POST] /jwtAuthAPI

**설명**: JWT 토큰값 검증

**인증**: 필요 (JWT)

---

### [PATCH] /admin/password

**설명**: 관리자 비밀번호 변경

**인증**: 필요 (JWT)

**요청 Body**:
```json
{
    "old_password": "기존비밀번호",
    "new_password": "새비밀번호"
}
```

| 응답 코드 | 설명 |
|-----------|------|
| 200 | 변경 성공 |
| 800 | 저장 오류 |

---

## 8. 파일 관리 (EgovFileMngApiController)

### [POST] /file/delete

**설명**: 파일 삭제

**인증**: 필요

---

## 공통 응답 코드

| 코드 | 상수 | 메시지 |
|------|------|--------|
| 200 | SUCCESS | 성공했습니다. |
| 403 | AUTH_ERROR | 인가된 사용자가 아닙니다. |
| 700 | DELETE_ERROR | 삭제 중 내부 오류가 발생했습니다. |
| 800 | SAVE_ERROR | 저장시 내부 오류가 발생했습니다. |
| 900 | INPUT_CHECK_ERROR | 입력값 무결성 오류 입니다. |
