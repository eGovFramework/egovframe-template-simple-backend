# Swagger 사용방법 가이드

## 개요

Swagger는 REST API를 문서화하고 테스트할 수 있는 도구입니다. 이 가이드는 egovframework Simple Backend 템플릿에서 Swagger를 사용하는 방법을 설명합니다.

## 1. Swagger UI 접속

### 기본 접속 정보
- **Swagger 3.x**: `http://localhost:8080/swagger-ui/index.html`
- **기본 포트**: 8080 (application.properties에서 변경 가능)

### 접속 확인
1. Spring Boot 애플리케이션이 정상적으로 구동되었는지 확인
2. 브라우저에서 Swagger UI URL로 접속
3. API 엔드포인트 목록이 표시되는지 확인

## 2. API 인증 설정

### 인증 방식
- **GET 요청**: JWT 토큰 인증 없이 사용 가능
- **POST/PUT/DELETE 요청**: JWT 토큰을 통한 인증 필요

### JWT 토큰 획득 과정

#### 단계 1: 로그인 API 호출
1. Swagger UI에서 `/auth/login-jwt` 엔드포인트 찾기
2. **[Try it out]** 버튼 클릭
3. 다음 정보 입력:
   - **아이디**: `admin`
   - **비밀번호**: `1`
4. **[Execute]** 버튼 클릭

#### 단계 2: 토큰 복사
1. **Response body** 섹션에서 응답 확인
2. `"jToken": "토큰값"` 형태로 반환된 토큰값 복사
   ```json
   {
     "jToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   ```

#### 단계 3: 토큰 설정
1. Swagger UI 상단의 **[Authorize]** 버튼 클릭
2. **Value** 필드에 복사한 토큰값 입력
3. **[Authorize]** 버튼 클릭
4. 인증 완료 확인

## 3. API 테스트 방법

### GET 요청 테스트
1. 원하는 GET 엔드포인트 선택
2. **[Try it out]** 버튼 클릭
3. 필요한 파라미터 입력 (있는 경우)
4. **[Execute]** 버튼 클릭
5. 응답 결과 확인

### POST/PUT/DELETE 요청 테스트
1. JWT 토큰 인증이 완료되었는지 확인
2. 원하는 엔드포인트 선택
3. **[Try it out]** 버튼 클릭
4. Request body에 필요한 JSON 데이터 입력
5. **[Execute]** 버튼 클릭
6. 응답 결과 확인

## 4. 응답 코드 이해

### 성공 응답
- **200 OK**: 요청 성공
- **201 Created**: 리소스 생성 성공
- **204 No Content**: 요청 성공, 응답 본문 없음

### 오류 응답
- **401 Unauthorized**: 인증 실패 ("인가된 사용자가 아닙니다.")
- **403 Forbidden**: 접근 권한 없음
- **404 Not Found**: 리소스를 찾을 수 없음
- **500 Internal Server Error**: 서버 내부 오류

## 5. API 컨트롤러 식별

### 컨트롤러 파일 명명 규칙
- API 컨트롤러는 `~ApiController.java` 형식으로 명명
- 예시: `BoardApiController.java`, `UserApiController.java`

### Swagger UI에서 확인
- 각 컨트롤러별로 API 그룹이 구분되어 표시
- 컨트롤러 이름을 클릭하여 해당 API 목록 확인

## 6. 실제 사용 예시

### 예시 1: 게시판 목록 조회 (GET)
```
GET /api/board/list
Parameters: 
- page: 1
- size: 10
```

### 예시 2: 게시글 작성 (POST)
```
POST /api/board
Headers: Authorization: Bearer {JWT_TOKEN}
Body:
{
  "title": "게시글 제목",
  "content": "게시글 내용",
  "author": "작성자"
}
```

---

## 7. 문제 해결

### 인증 오류 시

**문제**: `401 Unauthorized` 또는 "인가된 사용자가 아닙니다."

**해결방법**:
- [ ] JWT 토큰이 올바르게 설정되었는지 확인
- [ ] 토큰이 만료되지 않았는지 확인  
- [ ] 필요시 다시 로그인하여 새로운 토큰 획득

### 접속 불가 시

**문제**: Swagger UI 페이지에 접속할 수 없음

**해결방법**:
- [ ] Spring Boot 애플리케이션이 정상 구동되었는지 확인
- [ ] 포트 번호가 올바른지 확인 (기본값: 8080)
- [ ] 방화벽 설정 확인
- [ ] 브라우저 캐시 삭제

### API 응답 오류 시

**문제**: API 호출 시 오류 발생

**해결방법**:
- [ ] 요청 데이터 형식이 올바른지 확인
- [ ] 필수 파라미터가 누락되지 않았는지 확인
- [ ] Content-Type 헤더 확인 (`application/json`)
- [ ] 서버 로그 확인

---

## 8. 추가 정보

### 관련 저장소

| 구분 | 저장소 | 설명 |
|------|--------|------|
| **Backend** | [egovframe-template-simple-backend](https://github.com/eGovFramework/egovframe-template-simple-backend) | 현재 프로젝트 |
| **Frontend** | [egovframe-template-simple-react](https://github.com/eGovFramework/egovframe-template-simple-react.git) | React 기반 프론트엔드 |

### 개발 환경 요구사항

| 항목 | 버전 | 비고 |
|------|------|------|
| **Java** | 1.8 이상 | JDK |
| **Maven** | 3.8.4 | 빌드 도구 |
| **Spring Boot** | - | 프레임워크 |

### 실행 명령어

```bash
# 개발 모드 실행
mvn spring-boot:run

# 특정 프로필로 실행
java -jar <jar파일명> --spring.profiles.active=<profile명>
```

---

## 요약 체크리스트

### 초기 설정
- [ ] Spring Boot 애플리케이션 구동
- [ ] Swagger UI 접속 확인 (`http://localhost:8080/swagger-ui/index.html`)
- [ ] API 엔드포인트 목록 확인

### JWT 인증 설정  
- [ ] `/auth/login-jwt`로 로그인 (admin/1)
- [ ] JWT 토큰 복사
- [ ] Swagger에서 [Authorize] 버튼으로 토큰 설정

### API 테스트
- [ ] GET 요청 테스트 (인증 불필요)
- [ ] POST/PUT/DELETE 요청 테스트 (인증 필요)
- [ ] 응답 코드 및 결과 확인
