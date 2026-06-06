#!/bin/sh
# 컨테이너 기동 시 환경변수로 DataSource 설정을 주입한다.
#
# 애플리케이션의 DataSource(EgovConfigAppDatasource)는 Spring 표준 키
# (SPRING_DATASOURCE_*)가 아니라 Globals.DbType / Globals.<dbtype>.* 값만 읽는다.
# 따라서 환경변수를 그대로 넘겨도 DB가 교체되지 않으므로, 여기서 전달받은
# DB_* 환경변수를 Globals.* 키로 매핑한 오버라이드 properties 파일을 만들고
# Spring의 spring.config.additional-location 으로 추가 로딩한다.
#
# readOnlyRootFilesystem 환경을 고려해 쓰기 가능한 디렉터리에 파일을 생성한다.
set -e

# DB 교체용 환경변수가 하나도 없으면 내장 HSQL 기본 설정으로 기동한다.
if [ -n "${DB_TYPE}" ]; then
  CONF_DIR="${EGOV_CONFIG_DIR:-/tmp}"
  CONF_FILE="${CONF_DIR}/application-db.properties"

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
    echo "# 이 파일은 docker-entrypoint.sh가 환경변수로부터 생성한다. 직접 수정하지 말 것."
    echo "Globals.DbType=${DB_TYPE}"
    [ -n "${DRIVER}" ]       && echo "Globals.${DB_TYPE}.DriverClassName=${DRIVER}"
    [ -n "${DB_URL}" ]       && echo "Globals.${DB_TYPE}.Url=${DB_URL}"
    [ -n "${DB_USERNAME}" ]  && echo "Globals.${DB_TYPE}.UserName=${DB_USERNAME}"
    [ -n "${DB_PASSWORD}" ]  && echo "Globals.${DB_TYPE}.Password=${DB_PASSWORD}"
  } > "${CONF_FILE}"

  # 위에서 파일을 생성한 경우에만 옵션을 추가하므로 파일은 항상 존재한다.
  SPRING_CONFIG_OPT="--spring.config.additional-location=file:${CONF_FILE}"
fi

# shellcheck disable=SC2086
exec java ${JAVA_OPTS} -jar /app/app.jar ${SPRING_CONFIG_OPT}
