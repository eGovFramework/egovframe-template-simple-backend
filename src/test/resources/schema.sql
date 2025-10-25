-- 테스트 실행 전, 기존 테이블이 있다면 삭제하여 환경을 초기화합니다.
DROP TABLE IF EXISTS LETTNFILEDETAIL;
DROP TABLE IF EXISTS LETTNFILE;

-- 파일 마스터 테이블을 생성합니다.
CREATE TABLE LETTNFILE (
                           ATCH_FILE_ID VARCHAR(20) NOT NULL PRIMARY KEY,
                           CREAT_DT DATETIME,
                           USE_AT CHAR(1)
);

-- 파일 상세 테이블을 생성합니다.
CREATE TABLE LETTNFILEDETAIL (
                                 ATCH_FILE_ID VARCHAR(20) NOT NULL,
                                 FILE_SN DECIMAL(10,0) NOT NULL,
                                 FILE_STRE_COURS VARCHAR(2000) NOT NULL,
                                 STRE_FILE_NM VARCHAR(255) NOT NULL,
                                 ORIGNL_FILE_NM VARCHAR(255),
                                 FILE_EXTSN VARCHAR(20) NOT NULL,
                                 FILE_CN LONGTEXT,
                                 FILE_SIZE DECIMAL(8,0),
                                 PRIMARY KEY (ATCH_FILE_ID, FILE_SN)
);
