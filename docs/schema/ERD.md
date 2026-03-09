# ER 다이어그램 (텍스트)

> 테이블 간의 관계를 텍스트 기반으로 표현합니다.
> 새 도메인 테이블을 추가할 때 아래 형식으로 관계를 기록하세요.

---

## 인프라: 사용자/권한

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
  └──< LETTNEMPLYRINFO (직원)
        FK: GROUP_ID (CASCADE)

LETTNAUTHORINFO (권한 정보)
  │ PK: AUTHOR_CODE
  │
  └──< LETTNEMPLYRSCRTYESTBS (보안 설정)
        PK: SCRTY_DTRMN_TRGET_ID
        FK: AUTHOR_CODE → LETTNAUTHORINFO.AUTHOR_CODE
```

---

## 인프라: 파일 관리

```
LETTNFILE (파일 마스터)
  │ PK: ATCH_FILE_ID
  │
  └──< LETTNFILEDETAIL (파일 상세)
        PK: ATCH_FILE_ID + FILE_SN
        FK: ATCH_FILE_ID → LETTNFILE.ATCH_FILE_ID
```

---

## 인프라: 공통 코드

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

## 비즈니스 도메인

<!-- 새 도메인 테이블 관계를 아래 형식으로 추가하세요:

## 도메인: {도메인명}

```
LETTN{부모테이블} ({설명})
  │ PK: {PK컬럼}
  │
  └──< LETTN{자식테이블} ({설명})
        PK: {PK컬럼}
        FK: {FK컬럼} → LETTN{부모테이블}.{PK컬럼}
```

-->
