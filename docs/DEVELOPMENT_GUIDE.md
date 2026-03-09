# 백엔드 개발 가이드 (AI용)

> **이 프로젝트는 eGovFrame 기반 백엔드 템플릿입니다.**
> 기존 샘플 비즈니스 코드(게시판, 일정, 회원관리)는 삭제되었으며,
> `com/` 패키지의 인프라 코드만 남아있습니다.
> 새로운 서비스를 처음부터 개발할 때 아래 규칙과 패턴을 따르세요.

---

## 1. 기술 스택

| 항목 | 기술 | 버전 |
|------|------|------|
| Language | Java | 17 |
| Framework | Spring Boot (eGovFrame Boot) | 5.0.0 |
| Build Tool | Maven | - |
| ORM/SQL | MyBatis (EgovAbstractMapper) | - |
| API 문서 | SpringDoc OpenAPI (Swagger) | 2.6.0 |
| 인증 | JWT (jjwt) | 0.12.6 |
| DB | HSQLDB (기본), MySQL, Oracle, Altibase, Tibero, Cubrid 지원 | - |
| 기타 | Lombok, QueryDSL (test scope), Hibernate Validator | - |

---

## 1-1. 주요 인프라 클래스 import 경로

새 코드 작성 시 아래 클래스를 사용합니다:

| 클래스 | import 경로 | 용도 |
|--------|-------------|------|
| `IntermediateResultVO<T>` | `egovframework.com.cmm.service.IntermediateResultVO` | 표준 응답 래퍼 |
| `ResultVO` | `egovframework.com.cmm.service.ResultVO` | 레거시 응답 래퍼 |
| `ResponseCode` | `egovframework.com.cmm.ResponseCode` | 응답 코드 Enum |
| `LoginVO` | `egovframework.com.cmm.LoginVO` | 인증된 사용자 정보 |
| `EgovAbstractServiceImpl` | `org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl` | 서비스 구현체 부모 클래스 |
| `EgovAbstractMapper` | `org.egovframe.rte.psl.dataaccess.EgovAbstractMapper` | DAO 부모 클래스 (MyBatis) |
| `EgovPropertyService` | `org.egovframe.rte.fdl.property.EgovPropertyService` | 설정값 조회 서비스 |
| `PaginationInfo` | `org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo` | 페이징 처리 |
| `EgovIdGnrService` | `org.egovframe.rte.fdl.idgnr.EgovIdGnrService` | ID 생성 서비스 |

---

## 2. 프로젝트 패키지 구조

```
src/main/java/egovframework/
├── com/                              # [인프라] 공통 모듈 (수정하지 않음)
│   ├── cmm/                          # 공통 유틸, VO, 필터, 인터셉터
│   │   ├── annotation/               # 커스텀 어노테이션
│   │   ├── filter/                   # HTML 태그 필터
│   │   ├── interceptor/              # 인증 인터셉터, AOP
│   │   ├── service/                  # 공통 서비스 인터페이스 및 구현체
│   │   │   └── impl/                 # 공통 서비스 구현, DAO
│   │   ├── util/                     # 유틸리티 클래스
│   │   └── web/                      # 파일 관리 컨트롤러
│   ├── config/                       # Spring 설정 클래스
│   ├── jwt/                          # JWT 인증 관련
│   ├── security/                     # Spring Security 설정
│   └── sns/                          # SNS 로그인
│
└── let/                              # [개발] 비즈니스 모듈 (여기에 새 모듈 추가)
    ├── uat/uia/                      # [인프라] 로그인/인증 (유지)
    ├── utl/                          # [인프라] 유틸리티 (유지)
    │   ├── fcc/                      # 파일, 날짜, 문자열 유틸
    │   └── sim/                      # 보안 유틸
    │
    └── {새 도메인}/                    # ← 새 비즈니스 모듈을 여기에 추가
        ├── controller/               # API 컨트롤러
        ├── domain/
        │   ├── model/                # 도메인 모델 (Xxx, XxxVO)
        │   └── repository/           # DAO 클래스
        ├── dto/
        │   ├── request/              # 요청 DTO
        │   └── response/             # 응답 DTO
        ├── enums/                    # Enum 클래스
        └── service/                  # 서비스 인터페이스
            └── impl/                 # 서비스 구현체
```

---

## 3. 레이어 아키텍처 및 코드 패턴

### 3.1 Controller 레이어

**위치**: `{도메인}/controller/` 또는 `{도메인}/web/`

```java
@RestController
@RequiredArgsConstructor
@Tag(name = "컨트롤러클래스명", description = "설명")
public class XxxApiController {

    // 의존성은 생성자 주입 (final + @RequiredArgsConstructor)
    private final XxxService xxxService;
    private final EgovPropertyService propertyService;

    @Operation(summary = "요약", description = "설명", tags = {"컨트롤러클래스명"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping(value = "/api-path")
    public IntermediateResultVO<XxxResponseDTO> selectXxx(
            @ModelAttribute XxxSearchRequestDTO searchDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal LoginVO user)
        throws Exception {

        // 비즈니스 로직 호출
        XxxResponseDTO response = xxxService.selectXxx(searchDTO);

        return IntermediateResultVO.success(response);
    }
}
```

**규칙**:
- 클래스명: `Egov{도메인}ApiController` 또는 `{도메인}ApiController`
- `@RestController` 사용 (REST API 전용)
- 생성자 주입: `@RequiredArgsConstructor` + `final` 필드
- Swagger 어노테이션 필수: `@Tag`, `@Operation`, `@ApiResponses`
- 인증 필요 API: `security = {@SecurityRequirement(name = "Authorization")}`
- 응답 객체: `IntermediateResultVO<T>` (신규) 또는 `ResultVO` (레거시)
- 인증된 사용자 정보: `@AuthenticationPrincipal LoginVO user` 또는 JWT에서 직접 추출
- XSS 방지: `unscript()` 메서드로 사용자 입력 필터링

### 3.2 Service 레이어

**위치**: `{도메인}/service/` (인터페이스), `{도메인}/service/impl/` (구현체)

**인터페이스**:
```java
public interface EgovXxxService {
    public XxxResponseDTO selectXxx(XxxSearchRequestDTO dto) throws Exception;
    public void insertXxx(XxxRequestDTO dto) throws Exception;
    public void updateXxx(XxxRequestDTO dto) throws Exception;
    public void deleteXxx(XxxDeleteRequestDTO dto, LoginVO user) throws Exception;
}
```

**구현체**:
```java
@Service("EgovXxxService")
@RequiredArgsConstructor
public class EgovXxxServiceImpl extends EgovAbstractServiceImpl implements EgovXxxService {
    private final XxxDAO xxxDAO;

    @Override
    public XxxResponseDTO selectXxx(XxxSearchRequestDTO dto) throws Exception {
        // DTO -> VO 변환
        XxxVO vo = dto.toVO();

        // DAO 호출
        List<XxxVO> list = xxxDAO.selectXxxList(vo);
        int cnt = xxxDAO.selectXxxListCnt(vo);

        // VO -> Response DTO 변환
        return XxxResponseDTO.builder()
                .resultList(list.stream().map(XxxItemDTO::from).collect(Collectors.toList()))
                .resultCnt(cnt)
                .build();
    }
}
```

**규칙**:
- 인터페이스 + 구현체 분리 패턴
- `@Service("서비스명")` - Bean 이름 명시
- `EgovAbstractServiceImpl` 상속 (eGovFrame 표준)
- 생성자 주입 사용
- 메서드는 `throws Exception` 선언

### 3.3 DAO(Repository) 레이어

**위치**: `{도메인}/domain/repository/` 또는 `{도메인}/service/impl/`

```java
@Repository("XxxDAO")
public class XxxDAO extends EgovAbstractMapper {

    public List<XxxVO> selectXxxList(XxxVO vo) throws Exception {
        return selectList("XxxDAO.selectXxxList", vo);
    }

    public int selectXxxListCnt(XxxVO vo) throws Exception {
        return (Integer) selectOne("XxxDAO.selectXxxListCnt", vo);
    }

    public void insertXxx(XxxVO vo) throws Exception {
        insert("XxxDAO.insertXxx", vo);
    }

    public void updateXxx(XxxVO vo) throws Exception {
        update("XxxDAO.updateXxx", vo);
    }
}
```

**규칙**:
- `EgovAbstractMapper` 상속 (MyBatis 연동)
- `@Repository("DAO명")` 사용
- SQL 호출 시 네임스페이스: `"DAO클래스명.SQL_ID"`
- 반환 타입 캐스팅 필요: `(Integer)selectOne(...)`, `(Long)selectOne(...)`

### 3.4 MyBatis SQL Mapper

**위치**: `src/main/resources/egovframework/mapper/{모듈경로}/`

**파일 네이밍**: `Egov{도메인}_SQL_{DB타입}.xml` (예: `EgovBoard_SQL_mysql.xml`)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="XxxDAO">

    <resultMap id="xxxList" type="egovframework.let.xxx.domain.model.XxxVO">
        <result property="자바필드명" column="DB_COLUMN_NAME"/>
    </resultMap>

    <select id="selectXxxList" parameterType="egovframework.let.xxx.domain.model.XxxVO"
            resultMap="xxxList">
        SELECT column1, column2
        FROM TABLE_NAME
        WHERE USE_AT = 'Y'
        <if test="searchCnd == 0">
            AND COLUMN LIKE CONCAT('%', #{searchWrd}, '%')
        </if>
        ORDER BY SORT_ORDR DESC
        LIMIT #{recordCountPerPage} OFFSET #{firstIndex}
    </select>

    <select id="selectXxxListCnt" parameterType="..." resultType="java.lang.Integer">
        SELECT COUNT(*) FROM TABLE_NAME WHERE USE_AT = 'Y'
    </select>

    <insert id="insertXxx" parameterType="...">
        INSERT INTO TABLE_NAME (COL1, COL2, FRST_REGIST_PNTTM)
        VALUES (#{col1}, #{col2}, SYSDATE())
    </insert>
</mapper>
```

**규칙**:
- DB 타입별 SQL 파일 분리 (hsql, mysql, oracle, altibase, tibero, cubrid)
- namespace는 DAO 클래스명과 일치
- `resultMap`으로 Java 필드 ↔ DB 컬럼 매핑
- 페이징: `LIMIT #{recordCountPerPage} OFFSET #{firstIndex}` (MySQL 기준)
- 소프트 삭제: `USE_AT = 'N'` (실제 DELETE 대신 UPDATE)
- 동적 쿼리: `<if test="조건">` 사용
- 날짜 삽입: `SYSDATE()` (MySQL) / `SYSDATE` (Oracle)

### 3.5 도메인 모델 (Model/VO)

**위치**: `{도메인}/domain/model/`

```java
// 기본 모델 (DB 컬럼과 1:1 매핑)
@Schema(description = "Xxx 모델")
@Getter @Setter
public class Xxx implements Serializable {
    @Schema(description = "필드 설명")
    private String fieldName = "";  // 기본값 빈 문자열
}

// VO (검색/페이징 확장)
@Schema(description = "Xxx VO")
@Getter @Setter
public class XxxVO extends Xxx implements Serializable {
    @Schema(description = "검색조건")
    private String searchCnd = "";
    @Schema(description = "검색단어")
    private String searchWrd = "";
    @Schema(description = "현재페이지")
    private int pageIndex = 1;
    private int firstIndex = 1;
    private int lastIndex = 1;
    private int recordCountPerPage = 10;
}
```

### 3.6 DTO

**위치**: `{도메인}/dto/request/`, `{도메인}/dto/response/`

**Request DTO**:
```java
@Getter @Setter
@Schema(description = "검색 조건 DTO")
public class XxxSearchRequestDTO {
    @Schema(description = "ID", example = "값")
    private String id = "";
    @Schema(description = "페이지 번호", example = "1")
    private int pageIndex = 1;
}
```

**Response DTO**:
```java
@Getter @Setter @Builder(toBuilder = true)
@Schema(description = "응답 DTO")
public class XxxResponseDTO {
    private List<XxxItemDTO> resultList;
    private int resultCnt;

    // VO -> DTO 변환 팩토리 메서드
    public static XxxItemDTO from(XxxVO vo) {
        return XxxItemDTO.builder()
                .field(vo.getField())
                .build();
    }
}
```

**규칙**:
- Request DTO: `Xxx{기능}RequestDTO`
- Response DTO: `Xxx{기능}ResponseDTO`
- Lombok `@Builder`로 객체 생성 (Response)
- `from()` 정적 팩토리 메서드로 VO → DTO 변환

---

## 4. 응답 객체 패턴

### 4.1 IntermediateResultVO<T> (권장 - 신규 개발 시 사용)

```java
{
    "resultCode": 200,
    "resultMessage": "성공했습니다.",
    "result": { /* 응답 데이터 */ }
}
```

```java
// 성공
return IntermediateResultVO.success(responseDTO);

// 입력 오류
return IntermediateResultVO.inputCheckError(null);
```

### 4.2 ResultVO (레거시)

```java
{
    "resultCode": 200,
    "resultMessage": "성공했습니다.",
    "result": { "key": "value" }
}
```

```java
ResultVO resultVO = new ResultVO();
resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
resultVO.putResult("key", value);
```

### 4.3 ResponseCode

| 코드 | 상수 | 메시지 |
|------|------|--------|
| 200 | SUCCESS | 성공했습니다. |
| 403 | AUTH_ERROR | 인가된 사용자가 아닙니다. |
| 700 | DELETE_ERROR | 삭제 중 내부 오류가 발생했습니다. |
| 800 | SAVE_ERROR | 저장시 내부 오류가 발생했습니다. |
| 900 | INPUT_CHECK_ERROR | 입력값 무결성 오류 입니다. |

---

## 5. 인증/인가

### 5.1 JWT 토큰 처리

```java
// 방법 1: @AuthenticationPrincipal (권장)
@GetMapping("/api")
public ResultVO method(@AuthenticationPrincipal LoginVO user) {
    String uniqId = user.getUniqId();
}

// 방법 2: JWT에서 직접 추출 (PUT/POST에서 MultipartHttpServletRequest 사용 시)
private LoginVO extractUserFromJwt(HttpServletRequest request) {
    String jwtToken = EgovStringUtil.isNullToString(request.getHeader("Authorization"));
    String uniqId = jwtTokenUtil.getInfoFromToken("uniqId", jwtToken);
    String userNm = jwtTokenUtil.getInfoFromToken("name", jwtToken);
    LoginVO user = new LoginVO();
    user.setUniqId(uniqId);
    user.setName(userNm);
    return user;
}
```

### 5.2 인증이 필요한 API 표시 (Swagger)

```java
@Operation(
    summary = "...",
    security = {@SecurityRequirement(name = "Authorization")}
)
```

### 5.3 SecurityConfig.java에 새 API 등록

새 API를 추가한 후 `com/security/SecurityConfig.java`에서 접근 권한을 설정해야 합니다.

```java
// 1. 인증 없이 GET 접근 허용 (공개 조회 API)
private String[] AUTH_GET_WHITELIST = {
    "/products",              // 상품 목록 조회
    "/products/{productId}",  // 상품 상세 조회
};

// 2. 인증 없이 모든 메서드 허용 (로그인, 정적 리소스 등)
private String[] AUTH_WHITELIST = {
    "/auth/login-jwt",  // 이미 등록되어 있음
    // 새로 추가할 경로...
};

// 3. 역할 기반 접근 제어 (filterChain 메서드 내부)
http.authorizeHttpRequests(authorize -> authorize
    .requestMatchers("/admin/**").hasRole("ADMIN")
    // 새 역할 기반 규칙 추가:
    // .requestMatchers("/manager/**").hasRole("MANAGER")
    .anyRequest().authenticated()
);
```

**규칙**: 인증이 필요 없는 API → `AUTH_GET_WHITELIST` 또는 `AUTH_WHITELIST`에 추가. 그 외는 기본적으로 인증 필요.

---

## 6. 파일 업로드/다운로드

```java
// 파일 업로드 (컨트롤러에서)
final Map<String, MultipartFile> files = multiRequest.getFileMap();
if (!files.isEmpty()) {
    List<FileVO> result = fileUtil.parseFileInf(files, "PREFIX_", 0, "", "");
    String atchFileId = fileMngService.insertFileInfs(result);
}

// 파일 ID 암호화 (보안)
String encrypted = Base64.getEncoder().encodeToString(
    cryptoService.encrypt(fileId.getBytes(), ALGORITM_KEY));
```

---

## 7. 페이징 처리

```java
PaginationInfo paginationInfo = new PaginationInfo();
paginationInfo.setCurrentPageNo(searchDTO.getPageIndex());
paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

// VO에 페이징 값 세팅
vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
vo.setLastIndex(paginationInfo.getLastRecordIndex());
vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

// 조회 후 총 건수 반영
paginationInfo.setTotalRecordCount(resultCnt);
```

---

## 8. ID 생성

eGovFrame의 `EgovIdGnrService`를 사용하여 고유 ID를 생성합니다.

- `IDS` 테이블: 시퀀스 관리 테이블
- 설정: `EgovConfigAppIdGen.java`

```java
// Bean 설정 예시 (EgovConfigAppIdGen.java에서)
@Bean(destroyMethod = "destroy")
public EgovTableIdGnrServiceImpl egovXxxIdGnrService() {
    return new EgovIdGnrBuilder()
            .setDataSource(dataSource)
            .setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
            .setBlockSize(10)
            .setTable("IDS")
            .setTableName("XXX_ID")       // IDS 테이블에 등록한 TABLE_NAME
            .setPreFix("XXX_")            // 생성될 ID의 접두어
            .setCipers(12)                // 접두어 제외 자릿수
            .setFillChar('0')             // 빈자리 채움 문자
            .build();
}

// IDS 테이블에도 시퀀스를 등록해야 합니다:
// INSERT INTO IDS (TABLE_NAME, NEXT_ID) VALUES ('XXX_ID', 1);
```

---

## 9. 설정 파일

### application.properties 주요 항목

```properties
# DB 타입 선택 (hsql, mysql, oracle, altibase, tibero, cubrid)
Globals.DbType=hsql

# 페이징
Globals.pageUnit=10
Globals.pageSize=10

# 파일 업로드
Globals.posblAtchFileSize=5242880
Globals.fileStorePath=./files

# JWT Secret Key (반드시 변경하여 사용)
Globals.jwt.secret=my-secret-jwt-key

# CORS
Globals.Allow.Origin=http://localhost:3000

# Swagger
springdoc.packages-to-scan=egovframework
```

---

## 10. 새 기능 추가 체크리스트

새로운 CRUD 기능을 추가할 때 아래 순서를 따르세요:

### Step 1: DB 테이블/스키마 준비
- [ ] DDL 작성 (DATABASE 디렉토리 참고)
- [ ] 시퀀스 테이블(`IDS`)에 새 테이블 등록

### Step 2: 도메인 모델 생성
- [ ] `domain/model/Xxx.java` - 기본 모델
- [ ] `domain/model/XxxVO.java` - 검색/페이징 확장 VO

### Step 3: DTO 생성
- [ ] `dto/request/XxxSearchRequestDTO.java` - 검색 요청
- [ ] `dto/request/XxxInsertRequestDTO.java` - 등록 요청
- [ ] `dto/response/XxxListResponseDTO.java` - 목록 응답
- [ ] `dto/response/XxxDetailResponseDTO.java` - 상세 응답

### Step 4: DAO 생성
- [ ] `domain/repository/XxxDAO.java` (EgovAbstractMapper 상속)
- [ ] SQL Mapper XML 파일 작성 (DB 타입별)

### Step 5: Service 생성
- [ ] `service/EgovXxxService.java` - 인터페이스
- [ ] `service/impl/EgovXxxServiceImpl.java` - 구현체

### Step 6: Controller 생성
- [ ] `controller/EgovXxxApiController.java`
- [ ] Swagger 어노테이션 추가

### Step 7: 설정
- [ ] ID 생성기 설정 (필요 시 `EgovConfigAppIdGen.java`)
- [ ] 보안 설정 확인 (`SecurityConfig.java`)

### Step 8: 테스트
- [ ] API 테스트 (Swagger UI)

---

## 11. 네이밍 컨벤션

| 구분 | 규칙 | 예시 (도메인=Product) |
|------|------|------|
| Controller | `Egov{도메인}ApiController` | `EgovProductApiController` |
| Service Interface | `Egov{도메인}Service` | `EgovProductService` |
| Service Impl | `Egov{도메인}ServiceImpl` | `EgovProductServiceImpl` |
| DAO | `{도메인}DAO` 또는 `{도메인}Dao` | `ProductDAO` |
| Model | `{도메인}` / `{도메인}VO` | `Product` / `ProductVO` |
| Request DTO | `{도메인}{기능}RequestDTO` | `ProductSearchRequestDTO` |
| Response DTO | `{도메인}{기능}ResponseDTO` | `ProductListResponseDTO` |
| SQL Mapper | `Egov{도메인}_SQL_{dbType}.xml` | `EgovProduct_SQL_mysql.xml` |
| Enum | `{도메인}{구분}` | `ProductStatusType` |

---

## 12. DB 컬럼 네이밍 규칙

- 모든 컬럼명은 **대문자 + 언더스코어** 형식
- 공통 컬럼:
  - `FRST_REGISTER_ID` - 최초 등록자 ID
  - `FRST_REGIST_PNTTM` - 최초 등록 시점
  - `LAST_UPDUSR_ID` - 최종 수정자 ID
  - `LAST_UPDT_PNTTM` - 최종 수정 시점
  - `USE_AT` - 사용 여부 (Y/N) - 소프트 삭제용

---

## 13. 프로파일 설정

| 프로파일 | 파일 | 용도 |
|----------|------|------|
| 기본 | `application.properties` | 공통 설정, DB 타입 선택 |
| dev | `application-dev.properties` | 개발 환경 |
| prod | `application-prod.properties` | 운영 환경 |

```bash
# 실행
mvn spring-boot:run
java -jar app.jar --spring.profiles.active=prod
```
