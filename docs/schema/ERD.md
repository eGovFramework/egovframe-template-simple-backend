# ER 다이어그램 (텍스트)

> 테이블 간의 관계를 텍스트 기반으로 표현합니다.

---

## 도메인: 공통 코드

```
LETTCCMMNCLCODE (공통 분류 코드)
  │ PK: CL_CODE
  │
  └──< LETTCCMMNCODE (공통 코드)
        │ PK: CODE_ID
        │ FK: CL_CODE → LETTCCMMNCLCODE.CL_CODE
        │
        └──< LETTCCMMNDETAILCODE (공통 상세 코드)
              PK: CODE_ID + CODE
              FK: CODE_ID → LETTCCMMNCODE.CODE_ID
```

---

## 도메인: 사용자/권한

```
LETTNORGNZTINFO (조직 정보)
  │ PK: ORGNZT_ID
  │
  └──< LETTNEMPLYRINFO (직원 정보)
        PK: EMPLYR_ID
        FK: ORGNZT_ID → LETTNORGNZTINFO.ORGNZT_ID (CASCADE)
        FK: GROUP_ID → LETTNAUTHORGROUPINFO.GROUP_ID (CASCADE)

LETTNAUTHORGROUPINFO (권한 그룹)
  │ PK: GROUP_ID
  │
  ├──< LETTNEMPLYRINFO (직원)
  │     FK: GROUP_ID (CASCADE)
  │
  ├──< LETTNGNRLMBER (일반회원)
  │     PK: MBER_ID
  │     FK: GROUP_ID (CASCADE)
  │
  └──< LETTNENTRPRSMBER (기업회원)
        PK: ENTRPRS_MBER_ID
        FK: GROUP_ID (CASCADE)

LETTNAUTHORINFO (권한 정보)
  │ PK: AUTHOR_CODE
  │
  └──< LETTNEMPLYRSCRTYESTBS (보안 설정)
        PK: SCRTY_DTRMN_TRGET_ID
        FK: AUTHOR_CODE → LETTNAUTHORINFO.AUTHOR_CODE

COMVNUSERMASTER (통합 사용자 VIEW)
  = LETTNGNRLMBER UNION ALL LETTNEMPLYRINFO UNION ALL LETTNENTRPRSMBER
```

---

## 도메인: 게시판

```
LETTNBBSMASTER (게시판 마스터)
  │ PK: BBS_ID
  │
  ├──< LETTNBBSUSE (게시판 사용 정보)
  │     PK: BBS_ID + TRGET_ID
  │     FK: BBS_ID → LETTNBBSMASTER.BBS_ID
  │
  ├──< LETTNBBS (게시물)
  │     PK: NTT_ID + BBS_ID
  │     FK: BBS_ID → LETTNBBSMASTER.BBS_ID
  │
  └─── LETTNBBSMASTEROPTN (게시판 옵션)
        PK: BBS_ID
```

---

## 도메인: 파일 관리

```
LETTNFILE (파일 마스터)
  │ PK: ATCH_FILE_ID
  │
  └──< LETTNFILEDETAIL (파일 상세)
        PK: ATCH_FILE_ID + FILE_SN
        FK: ATCH_FILE_ID → LETTNFILE.ATCH_FILE_ID
```

---

## 도메인: 일정 관리

```
LETTNSCHDULINFO (일정 정보)
  PK: SCHDUL_ID
  (독립 테이블 - FK 없음)
```

---

## 도메인: 직원 이력

```
LETTNEMPLYRINFO (직원 정보)
  │ PK: EMPLYR_ID
  │
  └──< LETTHEMPLYRINFOCHANGEDTLS (직원 정보 변경 이력)
        PK: EMPLYR_ID + CHANGE_DE
        FK: EMPLYR_ID → LETTNEMPLYRINFO.EMPLYR_ID
```
