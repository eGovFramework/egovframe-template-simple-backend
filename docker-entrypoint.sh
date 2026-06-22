#!/bin/sh
# 컨테이너 기동 시 환경변수로 애플리케이션 설정을 주입한다.
#
# 애플리케이션의 DataSource(EgovConfigAppDatasource)는 Spring 표준 키
# (SPRING_DATASOURCE_*)가 아니라 Globals.DbType / Globals.<dbtype>.* 값만 읽고,
# 업로드 파일 저장 경로도 Globals.fileStorePath 값을 사용한다. 따라서 표준
# 환경변수를 그대로 넘겨도 반영되지 않으므로, 여기서 전달받은 DB_* / FILE_STORE_PATH
# 환경변수를 Globals.* 키로 매핑한 오버라이드 properties 파일을 만들고
# Spring의 spring.config.additional-location 으로 추가 로딩한다.
#
# readOnlyRootFilesystem 환경을 고려해 쓰기 가능한 디렉터리에 파일을 생성한다.
set -e

CONF_DIR="${EGOV_CONFIG_DIR:-/tmp}"
CONF_FILE="${CONF_DIR}/application-db.properties"
NEED_OVERRIDE=""

# DB 교체(DB_TYPE) 또는 파일 저장 경로(FILE_STORE_PATH) 주입이 필요한 경우에만
# 오버라이드 파일을 생성한다. 둘 다 없으면 내장 HSQL·기본 경로로 기동한다.
if [ -n "${DB_TYPE}" ] || [ -n "${FILE_STORE_PATH}" ]; then
  NEED_OVERRIDE=1
  echo "# 이 파일은 docker-entrypoint.sh가 환경변수로부터 생성한다. 직접 수정하지 말 것." > "${CONF_FILE}"
fi

if [ -n "${DB_TYPE}" ]; then
  # 드라이버 클래스명이 지정되지 않은 경우 DB 타입별 기본값을 사용한다.
  DRIVER="${DB_DRIVER_CLASS_NAME}"
  if [ -z "${DRIVER}" ]; then
    case "${DB_TYPE}" in
      mysql)    DRIVER="com.mysql.cj.jdbc.Driver" ;;
      oracle)   DRIVER="oracle.jdbc.driver.OracleDriver" ;;
      tibero)   DRIVER="com.tmax.tibero.jdbc.TbDriver" ;;
      altibase) DRIVER="Altibase.jdbc.driver.AltibaseDriver" ;;
      cubrid)   DRIVER="cubrid.jdbc.driver.CUBRIDDriver" ;;
      *)        DRIVER="" ;;
    esac
  fi

  {
    echo "Globals.DbType=${DB_TYPE}"
    [ -n "${DRIVER}" ]       && echo "Globals.${DB_TYPE}.DriverClassName=${DRIVER}"
    [ -n "${DB_URL}" ]       && echo "Globals.${DB_TYPE}.Url=${DB_URL}"
    [ -n "${DB_USERNAME}" ]  && echo "Globals.${DB_TYPE}.UserName=${DB_USERNAME}"
    [ -n "${DB_PASSWORD}" ]  && echo "Globals.${DB_TYPE}.Password=${DB_PASSWORD}"
  } >> "${CONF_FILE}"
fi

# 업로드 파일 저장 경로. 기본값(상대경로 ./files)은 readOnlyRootFilesystem 환경에서
# 서블릿 임시디렉터리 기준으로 풀려 쓰기에 실패하므로, 쓰기 가능한 볼륨이 마운트된
# 절대경로를 주입한다.
if [ -n "${FILE_STORE_PATH}" ]; then
  echo "Globals.fileStorePath=${FILE_STORE_PATH}" >> "${CONF_FILE}"
fi

if [ -n "${NEED_OVERRIDE}" ]; then
  SPRING_CONFIG_OPT="--spring.config.additional-location=file:${CONF_FILE}"
fi

# shellcheck disable=SC2086
exec java ${JAVA_OPTS} -jar /app/app.jar ${SPRING_CONFIG_OPT}
