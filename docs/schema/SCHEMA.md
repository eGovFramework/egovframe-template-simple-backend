# DB 스키마 명세

> 현재 프로젝트에서 사용하는 전체 테이블 스키마를 정리합니다.
> 상세 DDL은 `DATABASE/` 디렉토리의 SQL 파일을 참조하세요.

---

## 1. 시퀀스/ID 관리

### IDS (ID 시퀀스 관리)

| 컬럼명 | 타입 | NULL | 기본값 | 설명 |
|--------|------|------|--------|------|
| TABLE_NAME | varchar(20) | NOT NULL | - | PK, 테이블명 |
| NEXT_ID | decimal(30,0) | NOT NULL | 0 | 다음 ID 값 |

---

## 2. 공통 코드

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

## 3. 사용자 관리

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

### LETTNGNRLMBER (일반회원)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| MBER_ID | varchar(20) | NOT NULL | PK, 회원ID |
| PASSWORD | varchar(200) | NOT NULL | 비밀번호(암호화) |
| MBER_NM | varchar(50) | NOT NULL | 회원명 |
| GROUP_ID | char(20) | NULL | FK → LETTNAUTHORGROUPINFO |
| ESNTL_ID | char(20) | NOT NULL | 고유식별자 |

### COMVNUSERMASTER (통합 사용자 VIEW)

일반회원 + 직원 + 기업회원을 UNION ALL로 통합 조회하는 뷰

---

## 4. 게시판

### LETTNBBSMASTER (게시판 마스터)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| BBS_ID | char(20) | NOT NULL | PK, 게시판ID |
| BBS_NM | varchar(255) | NOT NULL | 게시판명 |
| BBS_TY_CODE | char(6) | NOT NULL | 게시판유형코드 |
| BBS_ATTRB_CODE | char(6) | NOT NULL | 게시판속성코드 |
| REPLY_POSBL_AT | char(1) | NULL | 답변가능여부 |
| FILE_ATCH_POSBL_AT | char(1) | NOT NULL | 파일첨부가능여부 |
| ATCH_POSBL_FILE_NUMBER | decimal(2,0) | NOT NULL | 첨부가능파일수 |
| USE_AT | char(1) | NOT NULL | 사용여부 |

### LETTNBBS (게시물)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| NTT_ID | decimal(20,0) | NOT NULL | PK(복합), 게시물ID |
| BBS_ID | char(20) | NOT NULL | PK(복합), FK → LETTNBBSMASTER |
| NTT_SJ | varchar(2000) | NULL | 게시물 제목 |
| NTT_CN | mediumtext | NULL | 게시물 내용 |
| PARNTSCTT_NO | decimal(10,0) | NULL | 부모글 번호 |
| ANSWER_AT | char(1) | NULL | 답변여부 |
| ANSWER_LC | int | NULL | 답변위치 |
| SORT_ORDR | decimal(8,0) | NULL | 정렬순서 |
| RDCNT | decimal(10,0) | NULL | 조회수 |
| USE_AT | char(1) | NOT NULL | 사용여부 |
| ATCH_FILE_ID | char(20) | NULL | 첨부파일ID |

---

## 5. 파일 관리

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

## 6. 일정 관리

### LETTNSCHDULINFO (일정 정보)

| 컬럼명 | 타입 | NULL | 설명 |
|--------|------|------|------|
| SCHDUL_ID | char(20) | NOT NULL | PK, 일정ID |
| SCHDUL_SE | char(1) | NULL | 일정구분 |
| SCHDUL_KND_CODE | varchar(20) | NULL | 일정종류코드 |
| SCHDUL_BEGINDE | datetime | NULL | 시작일시 |
| SCHDUL_ENDDE | datetime | NULL | 종료일시 |
| SCHDUL_NM | varchar(255) | NULL | 일정명 |
| SCHDUL_CN | varchar(2500) | NULL | 일정내용 |
| SCHDUL_PLACE | varchar(255) | NULL | 장소 |
| SCHDUL_IPCR_CODE | char(1) | NULL | 중요도코드 |
| ATCH_FILE_ID | char(20) | NULL | 첨부파일ID |

---

## 7. 권한 관리

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
