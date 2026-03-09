# 아키텍처 결정 기록 (ADR)

> **이 프로젝트는 구조 참고용 템플릿입니다.**
> 새 서비스 개발 시 아래 아키텍처 결정을 따르세요.

---

## ADR-001: 레이어 아키텍처

**날짜**: 2026-03-06 (기존 프로젝트 분석 기반)
**상태**: 승인

### 맥락
eGovFrame 기반 백엔드 프로젝트의 표준 아키텍처를 정의해야 함

### 결정
4-Layer 아키텍처 채택:
1. **Controller**: REST API 엔드포인트 (`@RestController`)
2. **Service**: 비즈니스 로직 (인터페이스 + 구현체 분리)
3. **DAO/Repository**: 데이터 접근 (`EgovAbstractMapper` 상속)
4. **MyBatis SQL Mapper**: XML 기반 SQL 관리

### 결과
- 각 레이어의 역할이 명확히 분리됨
- 서비스 인터페이스를 통한 느슨한 결합
- DB 타입별 SQL 분리로 멀티 DB 지원 가능

---

## ADR-002: 응답 객체 표준화

**날짜**: 2026-03-06
**상태**: 승인

### 맥락
`ResultVO`의 `Map<String, Object>` 기반 응답은 타입 안정성이 부족함

### 결정
- **표준: `IntermediateResultVO<T>` 사용** (제네릭 기반)
- `ResultVO`는 레거시이므로 새 API에서 사용하지 않음

### 결과
- 타입 안정성 확보
- Swagger 문서에서 응답 스키마 자동 생성 가능

---

## ADR-003: DTO 패턴

**날짜**: 2026-03-06
**상태**: 승인

### 맥락
Controller에서 도메인 모델(VO)을 직접 노출하면 API 변경에 취약함

### 결정
- **표준: 모든 API에 Request DTO / Response DTO 사용**
- `dto/request/`: 요청 바인딩용
- `dto/response/`: 응답 전용 (`@Builder`, `from()` 팩토리 메서드)
- 도메인 모델(XxxVO)은 내부 레이어에서만 사용

### 결과
- API 계약과 내부 모델의 분리
- Swagger 문서 자동 생성 시 깔끔한 스키마

---

## ADR-004: 인증 방식

**날짜**: 2026-03-06
**상태**: 승인

### 맥락
프론트엔드(React)와의 API 통신에 적합한 인증 방식 필요

### 결정
- JWT 토큰 기반 인증 (`Authorization` 헤더)
- Spring Security + `JwtAuthenticationFilter` 조합
- `@AuthenticationPrincipal LoginVO` 또는 JWT에서 직접 추출

### 결과
- Stateless 인증으로 확장성 확보
- 프론트엔드에서 토큰 기반 API 호출 용이

---

## ADR-005: 소프트 삭제

**날짜**: 2026-03-06
**상태**: 승인

### 맥락
데이터 보존 및 감사 추적이 필요함

### 결정
- 모든 테이블에 `USE_AT` 컬럼 추가 (Y/N)
- DELETE 대신 `UPDATE SET USE_AT = 'N'` 수행
- 조회 시 `WHERE USE_AT = 'Y'` 조건 필수
- HTTP 메서드: `PATCH` 사용 (부분 수정)

### 결과
- 데이터 복구 가능
- 감사 추적 용이
- 조회 쿼리에 항상 USE_AT 조건 필요
