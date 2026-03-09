# DB 스키마 명세

> 현재 프로젝트에서 사용하는 전체 테이블 스키마를 정리합니다.
> 새 테이블을 추가할 때 `DB_DESIGN_GUIDE.md`의 규칙을 따르고, 이 문서에 스키마를 기록하세요.

---

## 인프라 테이블

아래 테이블은 프레임워크 인프라로 유지됩니다. 새 비즈니스 테이블은 이 아래에 추가하세요.

---

### IDS (ID 시퀀스 관리)

| 컬럼명 | 타입 | NULL | 기본값 | 설명 |
|--------|------|------|--------|------|
| TABLE_NAME | varchar(20) | NOT NULL | - | PK, 테이블명 |
| NEXT_ID | decimal(30,0) | NOT NULL | 0 | 다음 ID 값 |

---

### LETTCCMMNCLCODE (공통 분류 코드)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| CL_CODE | char(3) | NOT NULL | PK, 분류코드 |
| CL_CODE_NM | varchar(60) | NULL | 분류코드명 |
| CL_CODE_DC | varchar(200) | NULL | 분류코드 설명 |
| USE_AT | char(1) | NULL | 사용여부 |

### LETTCCMMNCODE (공통 코드)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| CODE_ID | varchar(6) | NOT NULL | PK, 코드ID |
| CODE_ID_NM | varchar(60) | NULL | 코드명 |
| CODE_ID_DC | varchar(200) | NULL | 코드 설명 |
| CL_CODE | char(3) | NULL | FK → LETTCCMMNCLCODE |

### LETTCCMMNDETAILCODE (공통 상세 코드)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| CODE_ID | varchar(6) | NOT NULL | PK(복합), FK → LETTCCMMNCODE |
| CODE | varchar(15) | NOT NULL | PK(복합), 상세코드 |
| CODE_NM | varchar(60) | NULL | 상세코드명 |
| CODE_DC | varchar(200) | NULL | 상세코드 설명 |

---

### LETTNORGNZTINFO (조직 정보)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| ORGNZT_ID | char(20) | NOT NULL | PK, 조직ID |
| ORGNZT_NM | varchar(20) | NOT NULL | 조직명 |

### LETTNAUTHORGROUPINFO (권한 그룹)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| GROUP_ID | char(20) | NOT NULL | PK, 그룹ID |
| GROUP_NM | varchar(60) | NOT NULL | 그룹명 |

### LETTNAUTHORINFO (권한 정보)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| AUTHOR_CODE | varchar(30) | NOT NULL | PK, 권한코드 |
| AUTHOR_NM | varchar(60) | NOT NULL | 권한명 |

### LETTNEMPLYRINFO (직원/업무사용자)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| EMPLYR_ID | varchar(20) | NOT NULL | PK, 사용자ID |
| ORGNZT_ID | char(20) | NULL | FK → LETTNORGNZTINFO |
| USER_NM | varchar(60) | NOT NULL | 사용자명 |
| PASSWORD | varchar(200) | NOT NULL | 비밀번호(암호화) |
| GROUP_ID | char(20) | NULL | FK → LETTNAUTHORGROUPINFO |
| EMPLYR_STTUS_CODE | varchar(15) | NOT NULL | 상태코드 |
| ESNTL_ID | char(20) | NOT NULL | 고유식별자 |

---

### LETTNFILE (파일 마스터)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| ATCH_FILE_ID | char(20) | NOT NULL | PK, 첨부파일ID |
| CREAT_DT | datetime | NOT NULL | 생성일시 |
| USE_AT | char(1) | NULL | 사용여부 |

### LETTNFILEDETAIL (파일 상세)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| ATCH_FILE_ID | char(20) | NOT NULL | PK(복합), FK → LETTNFILE |
| FILE_SN | decimal(10,0) | NOT NULL | PK(복합), 파일순번 |
| FILE_STRE_COURS | varchar(2000) | NOT NULL | 저장경로 |
| STRE_FILE_NM | varchar(255) | NOT NULL | 저장파일명 |
| ORIGNL_FILE_NM | varchar(255) | NULL | 원본파일명 |
| FILE_EXTSN | varchar(20) | NOT NULL | 파일확장자 |

---

## 비즈니스 테이블

<!-- 새 비즈니스 테이블을 아래에 추가하세요. DB_DESIGN_GUIDE.md의 형식을 따릅니다. -->
