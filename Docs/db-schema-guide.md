# [컨트리뷰터 참고 권장] DB 스키마 가이드


> - 목적 : 소스 문맥을 모르는 기여자도 테이블 용도, 컬럼 등 DB 구조를 한눈에 파악할 수 있도록 문서화
> - 대상 파일 : `src/main/resources/db/shtdb.sql`, `DATABASE/all_sht_ddl_OOOOO.sql`
> - 작성 기준 : `shtdb.sql` 파일의 DDL을  검토하여 반영
> - 변경 규칙 : 스키마를 수정하는 PR은 본 문서(`Docs/db-schema-guide.md`)도 동시에 업데이트해야 합니다. 
>   - 변경 사항이 발생할 경우 아래의 “변경 이력”에 커밋 해시/릴리스 태그와 함께 기록해 주세요.
> - 문의/오류 제보 : Github Issues에 등록 또는 직접 수정하여 자유롭게 PR을 올려주세요.
---

## 변경 이력
| 날짜         | 변경 내용    | 커밋/태그 | 작성자        |
|------------|----------|-------|------------|
| 2025-09-17 | 초기 작성    | -     | KoreaNirsa |
| 2025-09-19 | 대상 파일 추가 | -     | KoreaNirsa |

---

## 표기 규칙
- **PK**: 기본키, **FK**: 외래키, **NN**: NOT NULL, **DF**: DEFAULT
- FK의 삭제 규칙(예: `ON DELETE CASCADE`)은 **제약**에 명시했습니다.

---

## 테이블 리스트

### 공통코드
- [LETTCCMMNCLCODE (공통코드 대분류)](#lettccmmnclcode-공통코드-대분류)
- [LETTCCMMNCODE (공통코드 중분류)](#lettccmmncode-공통코드-중분류)
- [LETTCCMMNDETAILCODE (공통코드 소분류)](#lettccmmndetailcode-공통코드-소분류)

### 권한·그룹·조직·사용자
- [LETTHEMPLYRINFOCHANGEDTLS (사용자 변경이력)](#letthemplyrinfochangedtls-사용자-변경이력)
- [LETTNAUTHORGROUPINFO (권한그룹)](#lettnauthorgroupinfo-권한그룹)
- [LETTNAUTHORINFO (권한)](#lettnauthorinfo-권한)
- [LETTNEMPLYRINFO (사용자직원)](#lettnemplyrinfo-사용자직원)
- [LETTNEMPLYRSCRTYESTBS (사용자 권한 매핑)](#lettnemplyrscrtyestbs-사용자-권한-매핑)
- [LETTNENTRPRSMBER (기업회원)](#lettnentrprsmber-기업회원)
- [LETTNGNRLMBER (일반회원)](#lettngnrlmber-일반회원)
- [LETTNORGNZTINFO (조직)](#lettnorgnztinfo-조직)

### 게시판
- [LETTNBBS (게시물)](#lettnbbs-게시물)
- [LETTNBBSMASTER (게시물 마스터)](#lettnbbsmaster-게시판-마스터)
- [LETTNBBSMASTEROPTN (게시판 옵션)](#lettnbbsmasteroptn-게시판-옵션)
- [LETTNBBSUSE (게시판 사용대상)](#lettnbbsuse-게시판-사용대상)

### 파일
- [LETTNFILE (첨부파일 묶음 헤더)](#lettnfile-첨부파일-묶음-헤더)
- [LETTNFILEDETAIL (첨부파일 상세)](#lettnfiledetail-첨부파일-상세)

### 일정
- [LETTNSCHDULINFO (일정)](#lettnschdulinfo-일정)

### 시퀀스/ID
- [IDS (내장샘플 시퀀스)](#ids-내장샘플-시퀀스)
- [COMTECOPSEQ (범용 시퀀스)](#comtecopseq-범용-시퀀스)

---

# 테이블 사전

## 공통코드

### LETTCCMMNCLCODE (공통코드 대분류)
**용도**: 공통코드 대분류 정보

| 컬럼                | 타입           | 제약       | 기본값 | 설명        |
|-------------------|--------------|----------|-----|-----------|
| CL_CODE           | CHAR(3)      | PK, NN   |     | 분류코드      |
| CL_CODE_NM        | VARCHAR(60)  |          |     | 분류코드명     |
| CL_CODE_DC        | VARCHAR(200) |          |     | 분류코드 설명   |
| USE_AT            | CHAR(1)      |          |     | 사용여부(Y/N) |
| FRST_REGIST_PNTTM | TIMESTAMP    |          |     | 최초등록시각    |
| FRST_REGISTER_ID  | VARCHAR(20)  |          |     | 최초등록자ID   |
| LAST_UPDT_PNTTM   | TIMESTAMP    |          |     | 최종수정시각    |
| LAST_UPDUSR_ID    | VARCHAR(20)  |          |     | 최종수정자ID   |

---

### LETTCCMMNCODE (공통코드 중분류)
**용도**: 공통코드 중분류 정보

| 컬럼                | 타입           | 제약                          | 기본값 | 설명        |
|-------------------|--------------|-----------------------------|-----|-----------|
| CODE_ID           | VARCHAR(6)   | PK, NN                      |     | 코드ID      |
| CODE_ID_NM        | VARCHAR(60)  |                             |     | 코드ID명     |
| CODE_ID_DC        | VARCHAR(200) |                             |     | 코드ID 설명   |
| USE_AT            | CHAR(1)      |                             |     | 사용여부(Y/N) |
| CL_CODE           | CHAR(3)      | FK→LETTCCMMNCLCODE(CL_CODE) |     | 분류코드      |
| FRST_REGIST_PNTTM | TIMESTAMP    |                             |     | 최초등록시각    |
| FRST_REGISTER_ID  | VARCHAR(20)  |                             |     | 최초등록자ID   |
| LAST_UPDT_PNTTM   | TIMESTAMP    |                             |     | 최종수정시각    |
| LAST_UPDUSR_ID    | VARCHAR(20)  |                             |     | 최종수정자ID   |

---

### LETTCCMMNDETAILCODE (공통코드 소분류)
**용도**: 공통코드 소분류 종류

| 컬럼                | 타입           | 제약                             | 기본값 | 설명        |
|-------------------|--------------|--------------------------------|-----|-----------|
| CODE_ID           | VARCHAR(6)   | PK, FK→LETTCCMMNCODE(CODE_ID)  |     | 코드ID      |
| CODE              | VARCHAR(15)  | PK                             |     | 상세코드      |
| CODE_NM           | VARCHAR(60)  |                                |     | 상세코드명     |
| CODE_DC           | VARCHAR(200) |                                |     | 상세코드 설명   |
| USE_AT            | CHAR(1)      |                                |     | 사용여부(Y/N) |
| FRST_REGIST_PNTTM | TIMESTAMP    |                                |     | 최초등록시각    |
| FRST_REGISTER_ID  | VARCHAR(20)  |                                |     | 최초등록자ID   |
| LAST_UPDT_PNTTM   | TIMESTAMP    |                                |     | 최종수정시각    |
| LAST_UPDUSR_ID    | VARCHAR(20)  |                                |     | 최종수정자ID   |

> \* 복합 PK: `(CODE_ID, CODE)`

---

## 권한·그룹·조직·사용자

### LETTHEMPLYRINFOCHANGEDTLS (사용자 변경이력)
**용도**: 직원/사용자 정보 변경 이력(스냅샷)

| 컬럼                 | 타입            | 제약      | 기본값 | 설명        |
|--------------------|---------------|---------|-----|-----------|
| EMPLYR_ID          | VARCHAR(20)   | PK, NN  |     | 사용자ID     |
| CHANGE_DE          | CHAR(20)      | PK, NN  |     | 변경일자      |
| ORGNZT_ID          | CHAR(20)      |         |     | 조직ID      |
| GROUP_ID           | CHAR(20)      |         |     | 그룹ID      |
| EMPL_NO            | VARCHAR(20)   | NN      |     | 사번        |
| SEXDSTN_CODE       | CHAR(1)       |         |     | 성별코드      |
| BRTHDY             | CHAR(20)      |         |     | 생년월일      |
| FXNUM              | VARCHAR(20)   |         |     | 팩스        |
| HOUSE_ADRES        | VARCHAR(100)  | NN      |     | 자택주소      |
| HOUSE_END_TELNO    | VARCHAR(4)    |         |     | 자택전화 끝번호  |
| AREA_NO            | VARCHAR(4)    |         |     | 지역번호      |
| DETAIL_ADRES       | VARCHAR(100)  | NN      |     | 상세주소      |
| ZIP                | VARCHAR(6)    | NN      |     | 우편번호      |
| OFFM_TELNO         | VARCHAR(20)   |         |     | 사무실전화     |
| MBTLNUM            | VARCHAR(20)   | NN      |     | 휴대전화      |
| EMAIL_ADRES        | VARCHAR(50)   |         |     | 이메일       |
| HOUSE_MIDDLE_TELNO | VARCHAR(4)    |         |     | 자택전화 중간번호 |
| PSTINST_CODE       | CHAR(8)       |         |     | 소속기관코드    |
| EMPLYR_STTUS_CODE  | VARCHAR(15)   | NN      |     | 사용자상태코드   |
| ESNTL_ID           | CHAR(20)      |         |     | 고유ID      |

> \* 복합 PK: `(EMPLYR_ID, CHANGE_DE)`

---

### LETTNAUTHORGROUPINFO (권한그룹)
**용도**: 권한그룹 정의

| 컬럼             | 타입           | 제약       | 기본값 | 설명    |
|----------------|--------------|----------|-----|-------|
| GROUP_ID       | CHAR(20)     | PK, NN   |     | 그룹ID  |
| GROUP_NM       | VARCHAR(60)  | NN       |     | 그룹명   |
| GROUP_CREAT_DE | CHAR(20)     | NN       |     | 생성일자  |
| GROUP_DC       | VARCHAR(100) |          |     | 그룹 설명 |

---

### LETTNAUTHORINFO (권한)
**용도**: 권한(역할) 기준 정보

| 컬럼              | 타입           | 제약       | 기본값 | 설명    |
|-----------------|--------------|----------|-----|-------|
| AUTHOR_CODE     | VARCHAR(30)  | PK, NN   |     | 권한코드  |
| AUTHOR_NM       | VARCHAR(60)  | NN       |     | 권한명   |
| AUTHOR_DC       | VARCHAR(200) |          |     | 권한 설명 |
| AUTHOR_CREAT_DE | CHAR(20)     | NN       |     | 생성일자  |

---

### LETTNORGNZTINFO (조직)
**용도**: 조직 정보

| 컬럼         | 타입           | 제약      | 기본값 | 설명     |
|------------|--------------|---------|-----|--------|
| ORGNZT_ID  | CHAR(20)     | PK, NN  |     | 조직ID   |
| ORGNZT_NM  | VARCHAR(20)  | NN      |     | 조직명    |
| ORGNZT_DC  | VARCHAR(100) |         |     | 조직 설명  |

---

### LETTNEMPLYRINFO (사용자/직원)
**용도**: 사용자(직원) 기본 정보

| 컬럼                 | 타입           | 제약                                                   | 기본값 | 설명        |
|--------------------|--------------|------------------------------------------------------|-----|-----------|
| EMPLYR_ID          | VARCHAR(20)  | PK, NN                                               |     | 사용자ID     |
| ORGNZT_ID          | CHAR(20)     |                                                      |     | 조직ID      |
| USER_NM            | VARCHAR(60)  | NN                                                   |     | 사용자명      |
| PASSWORD           | VARCHAR(200) | NN                                                   |     | 비밀번호(해시)  |
| EMPL_NO            | VARCHAR(20)  |                                                      |     | 사번        |
| IHIDNUM            | VARCHAR(13)  |                                                      |     | 주민등록번호    |
| SEXDSTN_CODE       | CHAR(1)      |                                                      |     | 성별코드      |
| BRTHDY             | CHAR(20)     |                                                      |     | 생년월일      |
| FXNUM              | VARCHAR(20)  |                                                      |     | 팩스번호      |
| HOUSE_ADRES        | VARCHAR(100) | NN                                                   |     | 자택주소      |
| PASSWORD_HINT      | VARCHAR(100) | NN                                                   |     | 비밀번호 힌트   |
| PASSWORD_CNSR      | VARCHAR(100) | NN                                                   |     | 비밀번호 정답   |
| HOUSE_END_TELNO    | VARCHAR(4)   | NN                                                   |     | 자택전화 끝번호  |
| AREA_NO            | VARCHAR(4)   | NN                                                   |     | 지역번호      |
| DETAIL_ADRES       | VARCHAR(100) |                                                      |     | 상세주소      |
| ZIP                | VARCHAR(6)   | NN                                                   |     | 우편번호      |
| OFFM_TELNO         | VARCHAR(20)  |                                                      |     | 사무실전화     |
| MBTLNUM            | VARCHAR(20)  | NN                                                   |     | 휴대전화      |
| EMAIL_ADRES        | VARCHAR(50)  |                                                      |     | 이메일       |
| OFCPS_NM           | VARCHAR(60)  |                                                      |     | 직책명       |
| HOUSE_MIDDLE_TELNO | VARCHAR(4)   | NN                                                   |     | 자택전화 중간번호 |
| GROUP_ID           | CHAR(20)     | FK→LETTNAUTHORGROUPINFO(GROUP_ID), ON DELETE CASCADE |     | 권한그룹ID    |
| PSTINST_CODE       | CHAR(8)      |                                                      |     | 소속기관코드    |
| EMPLYR_STTUS_CODE  | VARCHAR(15)  | NN                                                   |     | 사용자상태코드   |
| ESNTL_ID           | CHAR(20)     | NN                                                   |     | 고유ID      |
| CRTFC_DN_VALUE     | VARCHAR(20)  |                                                      |     | 인증DN      |
| SBSCRB_DE          | TIMESTAMP    |                                                      |     | 가입일시      |

---

### LETTNEMPLYRSCRTYESTBS (사용자-권한 매핑)
**용도**: 보안결정대상ID ↔ 권한코드 매핑

| 컬럼                     | 타입            | 제약                                     | 기본값 | 설명                   |
|------------------------|---------------|----------------------------------------|-----|----------------------|
| SCRTY_DTRMN_TRGET_ID   | VARCHAR(20)   | PK, NN, FK→LETTNEMPLYRINFO(EMPLYR_ID)  |     | 보안결정대상ID(보통 사용자ID)   |
| MBER_TY_CODE           | VARCHAR(15)   |                                        |     | 회원유형코드               |
| AUTHOR_CODE            | VARCHAR(30)   | NN, FK→LETTNAUTHORINFO(AUTHOR_CODE)    |     | 권한코드                 |

> **참고**: 본 테이블의 PK는 `SCRTY_DTRMN_TRGET_ID` 단일 컬럼입니다. (복합 PK 아님)

---

### LETTNGNRLMBER (일반회원)
**용도**: 일반회원 정보

| 컬럼               | 타입            | 제약                                                   | 기본값 | 설명       |
|------------------|---------------|------------------------------------------------------|-----|----------|
| MBER_ID          | VARCHAR(20)   | PK, NN                                               |     | 회원ID     |
| PASSWORD         | VARCHAR(200)  | NN                                                   |     | 비밀번호(해시) |
| PASSWORD_HINT    | VARCHAR(100)  |                                                      |     | 비밀번호 힌트  |
| PASSWORD_CNSR    | VARCHAR(100)  |                                                      |     | 비밀번호 정답  |
| IHIDNUM          | VARCHAR(13)   |                                                      |     | 주민등록번호   |
| MBER_NM          | VARCHAR(50)   | NN                                                   |     | 회원명      |
| ZIP              | VARCHAR(6)    | NN                                                   |     | 우편번호     |
| ADRES            | VARCHAR(100)  | NN                                                   |     | 주소       |
| AREA_NO          | VARCHAR(4)    | NN                                                   |     | 지역번호     |
| MBER_STTUS       | VARCHAR(15)   |                                                      |     | 회원상태코드   |
| DETAIL_ADRES     | VARCHAR(100)  |                                                      |     | 상세주소     |
| END_TELNO        | VARCHAR(4)    | NN                                                   |     | 전화 끝번호   |
| MBTLNUM          | VARCHAR(20)   | NN                                                   |     | 휴대전화번호   |
| GROUP_ID         | CHAR(20)      | FK→LETTNAUTHORGROUPINFO(GROUP_ID), ON DELETE CASCADE |     | 권한그룹ID   |
| MBER_FXNUM       | VARCHAR(20)   |                                                      |     | 팩스번호     |
| MBER_EMAIL_ADRES | VARCHAR(50)   |                                                      |     | 이메일주소    |
| MIDDLE_TELNO     | VARCHAR(4)    | NN                                                   |     | 전화 중간번호  |
| SBSCRB_DE        | TIMESTAMP     |                                                      |     | 가입일시     |
| SEXDSTN_CODE     | CHAR(1)       |                                                      |     | 성별코드     |
| ESNTL_ID         | CHAR(20)      | NN                                                   |     | 고유ID     |

---

### LETTNENTRPRSMBER (기업회원)
**용도**: 기업회원 정보

| 컬럼                         | 타입            | 제약                                                   | 기본값 | 설명         |
|----------------------------|---------------|------------------------------------------------------|-----|------------|
| ENTRPRS_MBER_ID            | VARCHAR(20)   | PK, NN                                               |     | 기업회원ID     |
| ENTRPRS_SE_CODE            | CHAR(15)      |                                                      |     | 기업구분코드     |
| BIZRNO                     | VARCHAR(10)   |                                                      |     | 사업자등록번호    |
| JURIRNO                    | VARCHAR(13)   |                                                      |     | 법인등록번호     |
| CMPNY_NM                   | VARCHAR(60)   | NN                                                   |     | 회사명        |
| CXFC                       | VARCHAR(50)   |                                                      |     | 대표자명       |
| ZIP                        | VARCHAR(6)    | NN                                                   |     | 우편번호       |
| ADRES                      | VARCHAR(100)  | NN                                                   |     | 주소         |
| ENTRPRS_MIDDLE_TELNO       | VARCHAR(4)    | NN                                                   |     | 전화 중간번호    |
| FXNUM                      | VARCHAR(20)   |                                                      |     | 팩스번호       |
| INDUTY_CODE                | CHAR(15)      |                                                      |     | 업종코드       |
| APPLCNT_NM                 | VARCHAR(50)   | NN                                                   |     | 신청자명       |
| APPLCNT_IHIDNUM            | VARCHAR(13)   |                                                      |     | 신청자 주민등록번호 |
| SBSCRB_DE                  | TIMESTAMP     |                                                      |     | 가입일시       |
| ENTRPRS_MBER_STTUS         | VARCHAR(15)   |                                                      |     | 회원상태코드     |
| ENTRPRS_MBER_PASSWORD      | VARCHAR(200)  |                                                      |     | 비밀번호(해시)   |
| ENTRPRS_MBER_PASSWORD_HINT | VARCHAR(100)  | NN                                                   |     | 비밀번호 힌트    |
| ENTRPRS_MBER_PASSWORD_CNSR | VARCHAR(100)  | NN                                                   |     | 비밀번호 정답    |
| GROUP_ID                   | CHAR(20)      | FK→LETTNAUTHORGROUPINFO(GROUP_ID), ON DELETE CASCADE |     | 권한그룹ID     |
| DETAIL_ADRES               | VARCHAR(100)  |                                                      |     | 상세주소       |
| ENTRPRS_END_TELNO          | VARCHAR(4)    | NN                                                   |     | 전화 끝번호     |
| AREA_NO                    | VARCHAR(4)    | NN                                                   |     | 지역번호       |
| APPLCNT_EMAIL_ADRES        | VARCHAR(50)   | NN                                                   |     | 신청자 이메일    |
| ESNTL_ID                   | CHAR(20)      | NN                                                   |     | 고유ID       |

---

## 게시판

### LETTNBBS (게시물)
**용도**: 게시물 데이터

| 컬럼                | 타입            | 제약       | 기본값 | 설명             |
|-------------------|---------------|----------|-----|----------------|
| NTT_ID            | NUMERIC(20)   | PK, NN   |     | 게시물ID          |
| BBS_ID            | CHAR(20)      | PK, NN   |     | 게시판ID          |
| NTT_NO            | NUMERIC(20)   |          |     | 게시물 번호(정렬/표시용) |
| NTT_SJ            | VARCHAR(2000) |          |     | 제목             |
| NTT_CN            | LONGVARCHAR   |          |     | 내용             |
| ANSWER_AT         | CHAR(1)       |          |     | 답변글 여부(Y/N)    |
| PARNTSCTT_NO      | NUMERIC(10)   |          |     | 부모글 번호         |
| ANSWER_LC         | INTEGER       |          |     | 답변 계층(레벨)      |
| SORT_ORDR         | NUMERIC(8)    |          |     | 정렬순서           |
| RDCNT             | NUMERIC(10)   |          |     | 조회수            |
| USE_AT            | CHAR(1)       | NN       |     | 사용여부(Y/N)      |
| NTCE_BGNDE        | CHAR(20)      |          |     | 공지 시작일시        |
| NTCE_ENDDE        | CHAR(20)      |          |     | 공지 종료일시        |
| NTCR_ID           | VARCHAR(20)   |          |     | 게시자ID          |
| NTCR_NM           | VARCHAR(20)   |          |     | 게시자명           |
| PASSWORD          | VARCHAR(200)  |          |     | 글 비밀번호(옵션)     |
| ATCH_FILE_ID      | CHAR(20)      |          |     | 첨부파일 묶음ID      |
| FRST_REGIST_PNTTM | TIMESTAMP     | NN       |     | 최초등록시각         |
| FRST_REGISTER_ID  | VARCHAR(20)   | NN       |     | 최초등록자ID        |
| LAST_UPDT_PNTTM   | TIMESTAMP     |          |     | 최종수정시각         |
| LAST_UPDUSR_ID    | VARCHAR(20)   |          |     | 최종수정자ID        |

> \* 복합 PK: `(NTT_ID, BBS_ID)`

---

### LETTNBBSMASTER (게시판 마스터)
**용도**: 게시판 속성(메타) 정보

| 컬럼                     | 타입            | 제약       | 기본값 | 설명                    |
|------------------------|---------------|----------|-----|-----------------------|
| BBS_ID                 | CHAR(20)      | PK, NN   |     | 게시판ID                 |
| BBS_NM                 | VARCHAR(255)  | NN       |     | 게시판명                  |
| BBS_INTRCN             | VARCHAR(2400) |          |     | 게시판 소개                |
| BBS_TY_CODE            | CHAR(6)       | NN       |     | 게시판 유형코드              |
| BBS_ATTRB_CODE         | CHAR(6)       | NN       |     | 게시판 속성코드              |
| REPLY_POSBL_AT         | CHAR(1)       |          |     | 답글 가능(Y/N)            |
| FILE_ATCH_POSBL_AT     | CHAR(1)       | NN       |     | 파일첨부 가능(Y/N)          |
| ATCH_POSBL_FILE_NUMBER | NUMERIC(2)    | NN       |     | 첨부가능 파일 수             |
| ATCH_POSBL_FILE_SIZE   | NUMERIC(8)    |          |     | 첨부가능 총 용량(단위: 시스템 정의) |
| USE_AT                 | CHAR(1)       | NN       |     | 사용여부(Y/N)             |
| TMPLAT_ID              | CHAR(20)      |          |     | 템플릿ID                 |
| FRST_REGISTER_ID       | VARCHAR(20)   | NN       |     | 최초등록자ID               |
| FRST_REGIST_PNTTM      | TIMESTAMP     | NN       |     | 최초등록시각                |
| LAST_UPDUSR_ID         | VARCHAR(20)   |          |     | 최종수정자ID               |
| LAST_UPDT_PNTTM        | TIMESTAMP     |          |     | 최종수정시각                |

---

### LETTNBBSMASTEROPTN (게시판 옵션)
**용도**: 게시판 세부 옵션(만족도/답변 기능 등)

| 컬럼                | 타입          | 제약      | 기본값                        | 설명             |
|-------------------|-------------|---------|----------------------------|----------------|
| BBS_ID            | CHAR(20)    | PK, NN  | DF ''                      | 게시판ID          |
| ANSWER_AT         | CHAR(1)     | NN      | DF ''                      | 답변 기능 사용(Y/N)  |
| STSFDG_AT         | CHAR(1)     | NN      | DF ''                      | 만족도 조사 사용(Y/N) |
| FRST_REGIST_PNTTM | TIMESTAMP   | NN      | DF '1970-01-01 00:00:00.0' | 최초등록시각         |
| LAST_UPDT_PNTTM   | TIMESTAMP   |         |                            | 최종수정시각         |
| FRST_REGISTER_ID  | VARCHAR(20) | NN      | DF ''                      | 최초등록자ID        |
| LAST_UPDUSR_ID    | VARCHAR(20) |         |                            | 최종수정자ID        |

---

### LETTNBBSUSE (게시판 사용대상)
**용도**: 대상(사이트/조직/커뮤니티 등)에 게시판 연결

| 컬럼                | 타입          | 제약                                 | 기본값 | 설명        |
|-------------------|-------------|------------------------------------|-----|-----------|
| BBS_ID            | CHAR(20)    | PK, NN, FK→LETTNBBSMASTER(BBS_ID)  |     | 게시판ID     |
| TRGET_ID          | CHAR(20)    | PK, NN                             |     | 사용대상ID    |
| USE_AT            | CHAR(1)     | NN                                 |     | 사용여부(Y/N) |
| REGIST_SE_CODE    | CHAR(6)     |                                    |     | 등록구분코드    |
| FRST_REGIST_PNTTM | TIMESTAMP   |                                    |     | 최초등록시각    |
| FRST_REGISTER_ID  | VARCHAR(20) | NN                                 |     | 최초등록자ID   |
| LAST_UPDT_PNTTM   | TIMESTAMP   |                                    |     | 최종수정시각    |
| LAST_UPDUSR_ID    | VARCHAR(20) |                                    |     | 최종수정자ID   |

> \* 복합 PK: `(BBS_ID, TRGET_ID)`

---

## 파일

### LETTNFILE (첨부파일 묶음 헤더)
**용도**: 첨부파일 세트의 헤더

| 컬럼            | 타입         | 제약      | 기본값 | 설명          |
|---------------|------------|---------|-----|-------------|
| ATCH_FILE_ID  | CHAR(20)   | PK, NN  |     | 첨부파일ID(묶음)  |
| CREAT_DT      | TIMESTAMP  | NN      |     | 생성시각        |
| USE_AT        | CHAR(1)    |         |     | 사용여부(Y/N)   |

---

### LETTNFILEDETAIL (첨부파일 상세)
**용도**: 첨부파일 개별 행

| 컬럼              | 타입            | 제약                                   | 기본값 | 설명          |
|-----------------|---------------|--------------------------------------|-----|-------------|
| ATCH_FILE_ID    | CHAR(20)      | PK, NN, FK→LETTNFILE(ATCH_FILE_ID)   |     | 첨부파일ID(묶음)  |
| FILE_SN         | NUMERIC(10)   | PK, NN                               |     | 파일 일련번호     |
| FILE_STRE_COURS | VARCHAR(2000) | NN                                   |     | 파일 저장경로     |
| STRE_FILE_NM    | VARCHAR(255)  | NN                                   |     | 저장파일명       |
| ORIGNL_FILE_NM  | VARCHAR(255)  |                                      |     | 원파일명        |
| FILE_EXTSN      | VARCHAR(20)   | NN                                   |     | 파일확장자       |
| FILE_CN         | LONGVARCHAR   |                                      |     | 파일 내용/비고    |
| FILE_SIZE       | NUMERIC(8)    |                                      |     | 파일크기(Byte)  |

> \* 복합 PK: `(ATCH_FILE_ID, FILE_SN)`

---

## 일정

### LETTNSCHDULINFO (일정)
**용도**: 일정/스케줄 관리

| 컬럼                | 타입            | 제약      | 기본값 | 설명         |
|-------------------|---------------|---------|-----|------------|
| SCHDUL_ID         | CHAR(20)      | PK, NN  |     | 일정ID       |
| SCHDUL_SE         | CHAR(1)       |         |     | 일정구분코드     |
| SCHDUL_DEPT_ID    | VARCHAR(20)   |         |     | 부서ID       |
| SCHDUL_KND_CODE   | VARCHAR(20)   |         |     | 일정종류코드     |
| SCHDUL_BEGINDE    | TIMESTAMP     |         |     | 시작일시       |
| SCHDUL_ENDDE      | TIMESTAMP     |         |     | 종료일시       |
| SCHDUL_NM         | VARCHAR(255)  |         |     | 일정명        |
| SCHDUL_CN         | VARCHAR(2500) |         |     | 일정 내용      |
| SCHDUL_PLACE      | VARCHAR(255)  |         |     | 장소         |
| SCHDUL_IPCR_CODE  | CHAR(1)       |         |     | 중요도코드      |
| SCHDUL_CHARGER_ID | VARCHAR(20)   |         |     | 담당자ID      |
| ATCH_FILE_ID      | CHAR(20)      |         |     | 첨부파일ID(묶음) |
| FRST_REGIST_PNTTM | TIMESTAMP     |         |     | 최초등록시각     |
| FRST_REGISTER_ID  | VARCHAR(20)   |         |     | 최초등록자ID    |
| LAST_UPDT_PNTTM   | TIMESTAMP     |         |     | 최종수정시각     |
| LAST_UPDUSR_ID    | VARCHAR(20)   |         |     | 최종수정자ID    |
| REPTIT_SE_CODE    | CHAR(3)       |         |     | 반복구분코드     |

---

## 시퀀스/ID

### IDS (내장/샘플 시퀀스)
**용도**: 내장 DB에서 사용하는 단순 시퀀스

| 컬럼          | 타입            | 제약      | 기본값   | 설명        |
|-------------|---------------|---------|-------|-----------|
| TABLE_NAME  | VARCHAR(20)   | PK, NN  | DF '' | 대상 테이블명   |
| NEXT_ID     | NUMERIC(30)   | NN      | DF 0  | 다음 ID 값   |

---

### COMTECOPSEQ (범용 시퀀스)
**용도**: 범용 시퀀스

| 컬럼           | 타입            | 제약      | 기본값   | 설명        |
|--------------|---------------|---------|-------|-----------|
| TABLE_NAME   | VARCHAR(20)   | PK, NN  | DF '' | 대상 테이블명   |
| NEXT_ID      | NUMERIC(30)   | NN      | DF 0  | 다음 ID 값   |

