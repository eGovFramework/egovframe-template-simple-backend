# DB 설계 지침

> 이 문서는 egovframe-template-simple-backend 프로젝트의 기존 DB 스키마 패턴을 기반으로,
> AI가 새로운 테이블을 설계하고 DB에 반영할 때 따라야 할 규칙을 정의합니다.

---

## 1. 기존 스키마 개요

### 1.1 테이블 목록 및 용도

| 테이블명 | 용도 | 비고 |
|----------|------|------|
| `IDS` | ID 시퀀스 관리 | eGovFrame 고유 |
| `COMTECOPSEQ` | 공통 시퀀스 관리 | - |
| `LETTCCMMNCLCODE` | 공통 분류 코드 | 코드 그룹 |
| `LETTCCMMNCODE` | 공통 코드 | 코드 마스터 |
| `LETTCCMMNDETAILCODE` | 공통 상세 코드 | 코드 상세 |
| `LETTNEMPLYRINFO` | 직원(업무사용자) 정보 | 로그인 사용 |
| `LETTHEMPLYRINFOCHANGEDTLS` | 직원 정보 변경이력 | - |
| `LETTNGNRLMBER` | 일반회원 정보 | - |
| `LETTNENTRPRSMBER` | 기업회원 정보 | - |
| `LETTNORGNZTINFO` | 조직 정보 | - |
| `LETTNAUTHORGROUPINFO` | 권한 그룹 정보 | - |
| `LETTNAUTHORINFO` | 권한 정보 | - |
| `LETTNEMPLYRSCRTYESTBS` | 사용자 보안 설정 | - |
| `LETTNBBSMASTER` | 게시판 마스터 | 게시판 설정 |
| `LETTNBBSMASTEROPTN` | 게시판 마스터 옵션 | - |
| `LETTNBBSUSE` | 게시판 사용 정보 | - |
| `LETTNBBS` | 게시물 | 실제 게시글 |
| `LETTNFILE` | 파일 마스터 | 첨부파일 그룹 |
| `LETTNFILEDETAIL` | 파일 상세 | 실제 파일 정보 |
| `LETTNSCHDULINFO` | 일정 정보 | 일정 관리 |
| `COMVNUSERMASTER` | 통합 사용자 뷰 | VIEW (UNION ALL) |

### 1.2 ER 관계 요약

```
LETTCCMMNCLCODE (분류코드)
  └── LETTCCMMNCODE (코드)
        └── LETTCCMMNDETAILCODE (상세코드)

LETTNORGNZTINFO (조직)
  └── LETTNEMPLYRINFO (직원)
        └── LETTHEMPLYRINFOCHANGEDTLS (변경이력)

LETTNAUTHORGROUPINFO (권한그룹)
  ├── LETTNEMPLYRINFO (직원)
  ├── LETTNGNRLMBER (일반회원)
  └── LETTNENTRPRSMBER (기업회원)

LETTNBBSMASTER (게시판마스터)
  ├── LETTNBBSUSE (게시판사용정보)
  └── LETTNBBS (게시물)

LETTNFILE (파일마스터)
  └── LETTNFILEDETAIL (파일상세)
```

---

## 2. 테이블 설계 규칙

### 2.1 테이블명 규칙

| 접두어 | 의미 | 사용처 |
|--------|------|--------|
| `LETTN` | 비즈니스 엔티티 테이블 | 대부분의 테이블 |
| `LETTC` | 공통 코드 테이블 | 코드 관련 |
| `LETTH` | 이력(History) 테이블 | 변경 이력 |
| `COMVN` | 뷰(View) | VIEW 객체 |
| `COMTE` | 공통 테이블 | 시퀀스 등 |

**규칙**:
- 테이블명은 **대문자** 사용
- 약어 기반 네이밍 (예: `MBER` = Member, `EMPLYR` = Employee, `ORGNZT` = Organization)
- 언더스코어 없이 연결 (예: `LETTNEMPLYRINFO`)

### 2.2 컬럼명 규칙

- **대문자 + 언더스코어** 구분
- 약어 기반 네이밍

| 약어 | 의미 | 예시 |
|------|------|------|
| `_ID` | 식별자 | `EMPLYR_ID`, `BBS_ID` |
| `_NM` | 이름 | `USER_NM`, `BBS_NM` |
| `_DC` | 설명(Description) | `CODE_DC`, `ORGNZT_DC` |
| `_CODE` | 코드 | `AUTHOR_CODE`, `EMPLYR_STTUS_CODE` |
| `_AT` | 여부(Y/N) | `USE_AT`, `REPLY_POSBL_AT` |
| `_DE` | 날짜(Date) | `CHANGE_DE`, `SBSCRB_DE` |
| `_NO` | 번호 | `NTT_NO`, `EMPL_NO` |
| `_SN` | 순번(Serial Number) | `FILE_SN` |
| `_CN` | 내용(Content) | `NTT_CN`, `SCHDUL_CN` |
| `_SJ` | 제목(Subject) | `NTT_SJ` |
| `_COURS` | 경로(Course/Path) | `FILE_STRE_COURS` |

### 2.3 공통 감사(Audit) 컬럼

모든 테이블에 아래 컬럼을 포함합니다:

```sql
FRST_REGIST_PNTTM datetime DEFAULT NULL,    -- 최초 등록 시점
FRST_REGISTER_ID varchar(20) DEFAULT NULL,   -- 최초 등록자 ID
LAST_UPDT_PNTTM datetime DEFAULT NULL,       -- 최종 수정 시점
LAST_UPDUSR_ID varchar(20) DEFAULT NULL      -- 최종 수정자 ID
```

### 2.4 소프트 삭제

```sql
USE_AT char(1) NOT NULL DEFAULT 'Y'    -- 사용여부 (Y: 사용, N: 삭제)
```

- 실제 `DELETE`를 수행하지 않고 `USE_AT = 'N'`으로 업데이트
- 조회 시 항상 `WHERE USE_AT = 'Y'` 조건 포함

### 2.5 데이터 타입 규칙

| 용도 | MySQL 타입 | 길이 | 비고 |
|------|-----------|------|------|
| ID/코드 | `char` / `varchar` | 6~20 | 고정길이는 char |
| 이름/제목 | `varchar` | 60~255 | - |
| 설명/내용 | `varchar` / `mediumtext` | 200~2500 | 긴 내용은 mediumtext |
| 여부(Flag) | `char(1)` | 1 | Y/N |
| 날짜 | `datetime` | - | - |
| 숫자 | `decimal` | 가변 | 정밀도 필요 시 |
| 파일크기 | `decimal(8,0)` | - | - |
| 비밀번호 | `varchar(200)` | 200 | 암호화 저장 |

### 2.6 PK 설계

```sql
-- 단일 PK
PRIMARY KEY (EMPLYR_ID)

-- 복합 PK
PRIMARY KEY (BBS_ID, NTT_ID)
PRIMARY KEY (CODE_ID, CODE)
PRIMARY KEY (ATCH_FILE_ID, FILE_SN)
```

### 2.7 FK 설계

```sql
-- CASCADE 삭제
CONSTRAINT FK명 FOREIGN KEY (컬럼) REFERENCES 부모테이블(PK컬럼) ON DELETE CASCADE

-- 일반 FK
CONSTRAINT FK명 FOREIGN KEY (컬럼) REFERENCES 부모테이블(PK컬럼)
```

- FK 제약조건명: `{테이블명}_ibfk_{순번}`
- 인덱스 키: `KEY {제약조건명} (컬럼명)`

---

## 3. 새 테이블 생성 절차

### Step 1: DDL 작성

```sql
CREATE TABLE LETTN{도메인명} (
    {PK_ID} varchar(20) NOT NULL,          -- PK
    {필드들}                                -- 비즈니스 필드
    USE_AT char(1) NOT NULL DEFAULT 'Y',   -- 사용여부
    FRST_REGIST_PNTTM datetime DEFAULT NULL,
    FRST_REGISTER_ID varchar(20) DEFAULT NULL,
    LAST_UPDT_PNTTM datetime DEFAULT NULL,
    LAST_UPDUSR_ID varchar(20) DEFAULT NULL,
    PRIMARY KEY ({PK_ID})
) ;
```

### Step 2: IDS 테이블에 시퀀스 등록

```sql
INSERT INTO IDS (TABLE_NAME, NEXT_ID) VALUES ('{TABLE_NAME}', 1);
```

### Step 3: 초기 데이터 (DML)

```sql
INSERT INTO LETTN{도메인명} (PK_ID, ..., USE_AT, FRST_REGIST_PNTTM, FRST_REGISTER_ID)
VALUES ('초기ID', ..., 'Y', NOW(), 'SYSTEM');
```

### Step 4: DB 타입별 SQL 파일 생성

`DATABASE/` 디렉토리에 DB 타입별 DDL/DML 파일을 생성합니다:

| 파일명 패턴 | 내용 |
|-------------|------|
| `all_sht_ddl_{dbtype}.sql` | DDL (CREATE TABLE) |
| `all_sht_data_{dbtype}.sql` | DML (초기 데이터) |

지원 DB 타입: `mysql`, `oracle`, `hsql`, `altibase`, `tibero`, `cubrid`

**DB별 문법 차이 주의사항**:

| 기능 | MySQL | Oracle | HSQLDB |
|------|-------|--------|--------|
| 자동 증가 | `AUTO_INCREMENT` | `SEQUENCE` | `IDENTITY` |
| 현재 시간 | `SYSDATE()` / `NOW()` | `SYSDATE` | `CURRENT_TIMESTAMP` |
| 문자열 연결 | `CONCAT()` | `\|\|` | `\|\|` |
| LIMIT | `LIMIT n OFFSET m` | `ROWNUM` | `LIMIT n OFFSET m` |
| IFNULL | `IFNULL()` | `NVL()` | `IFNULL()` / `NVL()` |
| mediumtext | `mediumtext` | `CLOB` | `CLOB` |

---

## 4. MyBatis SQL Mapper 작성 규칙

### 4.1 파일 위치

```
src/main/resources/egovframework/mapper/
├── config/
│   └── mapper-config.xml           # MyBatis 공통 설정
└── let/{모듈경로}/
    ├── Egov{도메인}_SQL_mysql.xml
    ├── Egov{도메인}_SQL_oracle.xml
    ├── Egov{도메인}_SQL_hsql.xml
    ├── Egov{도메인}_SQL_altibase.xml
    ├── Egov{도메인}_SQL_tibero.xml
    └── Egov{도메인}_SQL_cubrid.xml
```

### 4.2 필수 SQL 목록

새 도메인에 대해 다음 SQL을 작성합니다:

| SQL ID | 설명 | 타입 |
|--------|------|------|
| `selectMaxXxxId` | 최대 ID 조회 | SELECT |
| `selectXxxList` | 목록 조회 (페이징) | SELECT |
| `selectXxxListCnt` | 목록 건수 조회 | SELECT |
| `selectXxx` | 상세 조회 | SELECT |
| `insertXxx` | 등록 | INSERT |
| `updateXxx` | 수정 | UPDATE |
| `deleteXxx` | 삭제 (소프트) | UPDATE |

### 4.3 resultMap 정의

```xml
<resultMap id="xxxList" type="egovframework.let.xxx.domain.model.XxxVO">
    <result property="자바필드명" column="DB_COLUMN_NAME"/>
</resultMap>
```

- `property`: Java 필드명 (camelCase)
- `column`: DB 컬럼명 (UPPER_SNAKE_CASE)

### 4.4 동적 쿼리 패턴

```xml
<!-- 검색 조건 -->
<if test="searchCnd == 0">
    AND COLUMN LIKE CONCAT('%', #{searchWrd}, '%')
</if>

<!-- NULL 체크 -->
<if test="fieldName != null and fieldName != ''">
    AND COLUMN = #{fieldName}
</if>
```

---

## 5. 코드 테이블 활용

### 5.1 공통 코드 체계

```
분류코드(LETTCCMMNCLCODE) → 코드(LETTCCMMNCODE) → 상세코드(LETTCCMMNDETAILCODE)
```

새로운 코드를 추가할 때:

```sql
-- 1. 분류코드 등록 (3자리)
INSERT INTO LETTCCMMNCLCODE (CL_CODE, CL_CODE_NM, USE_AT)
VALUES ('NEW', '새분류명', 'Y');

-- 2. 코드 등록 (6자리 이내)
INSERT INTO LETTCCMMNCODE (CODE_ID, CODE_ID_NM, CL_CODE, USE_AT)
VALUES ('NEW001', '새코드명', 'NEW', 'Y');

-- 3. 상세코드 등록
INSERT INTO LETTCCMMNDETAILCODE (CODE_ID, CODE, CODE_NM, USE_AT)
VALUES ('NEW001', '01', '상세값1', 'Y');
```

---

## 6. 뷰(VIEW) 작성

사용자 통합 조회 뷰 패턴:

```sql
CREATE OR REPLACE VIEW COMVNUSERMASTER AS
    SELECT ESNTL_ID, MBER_ID AS USER_ID, PASSWORD, MBER_NM AS USER_NM, ...
    FROM LETTNGNRLMBER
  UNION ALL
    SELECT ESNTL_ID, EMPLYR_ID, PASSWORD, USER_NM, ...
    FROM LETTNEMPLYRINFO
  UNION ALL
    SELECT ESNTL_ID, ENTRPRS_MBER_ID, ENTRPRS_MBER_PASSWORD, CMPNY_NM, ...
    FROM LETTNENTRPRSMBER;
```

---

## 7. DB 반영 체크리스트

새 테이블을 추가할 때:

- [ ] DDL 작성 (공통 감사 컬럼, USE_AT 포함)
- [ ] PK/FK 제약조건 정의
- [ ] IDS 시퀀스 등록
- [ ] 초기 데이터(DML) 작성
- [ ] DB 타입별 SQL 파일 생성 (최소 MySQL, HSQLDB)
- [ ] MyBatis SQL Mapper 작성 (DB 타입별)
- [ ] resultMap 정의
- [ ] DAO 클래스 작성
- [ ] ID 생성기 Bean 등록 (`EgovConfigAppIdGen.java`)
