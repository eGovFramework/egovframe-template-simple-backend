# Kubernetes 배포 가이드

## 개요

이 디렉토리에는 `egovframe-template-simple-backend`를 Kubernetes 클러스터에 배포하기 위한 매니페스트가 포함되어 있습니다.

| 파일 | 설명 |
|------|------|
| `configmap.yaml` | 애플리케이션 설정(프로파일, DB URL, 파일 저장 경로 등) |
| `service.yaml` | ClusterIP 서비스(포트 8080) |
| `pvc.yaml` | 게시판 첨부파일 영속 저장소(ReadWriteOnce, 1Gi) |
| `deployment.yaml` | 애플리케이션 Deployment(레플리카 1, Recreate 전략, 로그용 emptyDir·첨부파일용 PVC 마운트) |

> 컨테이너 기동 시 `docker-entrypoint.sh`(리포 루트)가 `DB_*`·`FILE_STORE_PATH` 환경변수를
> 앱이 실제로 읽는 `Globals.*` 키로 매핑합니다. 자세한 내용은 아래 "DB 설정", "파일 업로드
> 저장소" 절을 참고하세요.

## 사전 조건

- Kubernetes 1.24 이상
- `kubectl` CLI 설치 및 클러스터 접속 구성
- MySQL 8.x 또는 호환 DB 클러스터 내 접근 가능
- 컨테이너 이미지 빌드 및 레지스트리 푸시 완료

## 빌드 및 이미지 준비

```bash
# 프로젝트 루트에서 실행
docker build -t <registry>/egovframe-template-simple-backend:5.0.0 .
docker push <registry>/egovframe-template-simple-backend:5.0.0
```

`deployment.yaml`의 `image` 필드를 실제 레지스트리 경로로 교체하세요.

```yaml
image: <registry>/egovframe-template-simple-backend:5.0.0
```

## DB 설정 (중요)

이 애플리케이션의 DataSource(`EgovConfigAppDatasource`)는 Spring 표준 키
(`SPRING_DATASOURCE_*`)가 **아니라** `application.properties`의 `Globals.DbType` 및
`Globals.<dbtype>.Url/UserName/Password/DriverClassName` 값만 읽습니다.
따라서 `SPRING_DATASOURCE_URL` 같은 환경변수를 주입해도 DB가 교체되지 않고
내장 HSQL로 기동됩니다.

이를 해결하기 위해 컨테이너 기동 스크립트(`docker-entrypoint.sh`)가
아래 환경변수를 받아 `Globals.*` 키로 매핑한 오버라이드 properties 파일을 생성하고
Spring의 `spring.config.additional-location`으로 추가 로딩합니다.

| 환경변수 | 매핑되는 키 | 예시 |
|----------|-------------|------|
| `DB_TYPE` | `Globals.DbType` | `mysql` |
| `DB_URL` | `Globals.<dbtype>.Url` | `jdbc:mysql://mysql:3306/egovdb` |
| `DB_USERNAME` | `Globals.<dbtype>.UserName` | `egov` |
| `DB_PASSWORD` | `Globals.<dbtype>.Password` | (Secret) |
| `DB_DRIVER_CLASS_NAME` | `Globals.<dbtype>.DriverClassName` | (미지정 시 DB 타입별 기본값) |

- `DB_TYPE`이 비어 있으면(기본) 내장 HSQL로 기동합니다.
- `DB_TYPE=mysql` 등으로 외부 DB를 지정하면 위 매핑값으로 DataSource가 구성됩니다.
- 직접 매핑 지점을 수정하려면 `src/main/resources/application.properties`의
  `Globals.DbType`(22행 부근)과 `Globals.<dbtype>.Url/UserName/Password`(36행 이후)를
  변경한 뒤 이미지를 재빌드하면 됩니다. 다만 컨테이너 환경에서는 위 환경변수 주입을 권장합니다.

설정값은 `configmap.yaml`(`DB_TYPE`, `DB_URL`)과 Secret(`username`, `password`)에서 주입됩니다.
배포 전 아래 명령으로 Secret을 생성하세요.

```bash
kubectl create secret generic egovframe-template-simple-backend-db \
  --from-literal=username=<DB_USER> \
  --from-literal=password=<DB_PASSWORD>
```

## 외부 DB 스키마 적재 (필수)

내장 HSQL은 기동 시 `classpath:/db/shtdb.sql`이 자동 적재되지만,
**외부 DB(MySQL 등)는 스키마(DDL)와 초기 데이터(DML)를 직접 적재해야** 합니다.
적재하지 않으면 테이블 부재로 런타임 오류가 발생합니다.

리포지토리 `DATABASE/` 디렉터리에 DB별 스크립트가 제공됩니다.

| DB | DDL | DML |
|----|-----|-----|
| MySQL | `DATABASE/all_sht_ddl_mysql.sql` | `DATABASE/all_sht_data_mysql.sql` |
| Oracle | `DATABASE/all_sht_ddl_oracle.sql` | `DATABASE/all_sht_data_oracle.sql` |
| Tibero | `DATABASE/all_sht_ddl_tibero.sql` | `DATABASE/all_sht_data_tibero.sql` |
| Altibase | `DATABASE/all_sht_ddl_altibase.sql` | `DATABASE/all_sht_data_altibase.sql` |
| CUBRID | `DATABASE/all_sht_ddl_cubrid.sql` | `DATABASE/all_sht_data_cubrid.sql` |

MySQL 예시(클러스터 외부에서 DB에 직접 접속하여 적재):

```bash
mysql -h <DB_HOST> -u <DB_USER> -p <DB_NAME> < DATABASE/all_sht_ddl_mysql.sql
mysql -h <DB_HOST> -u <DB_USER> -p <DB_NAME> < DATABASE/all_sht_data_mysql.sql
```

> 참고: `docker-compose.yml`은 MySQL 컨테이너의 `/docker-entrypoint-initdb.d`에
> 위 DDL/DML을 마운트하여 최초 기동 시 자동 적재합니다. Kubernetes에서는 관리형/외부 DB를
> 사용하는 경우가 일반적이므로, 위 수동 적재 또는 별도 Job/initContainer로 적재하세요.

## 지원 DB 범위

이 Docker/Kubernetes 배포 구성(`docker-compose.yml`, `docker-entrypoint.sh`,
`configmap.yaml` 등)에서 **기본 배선 및 동작 검증이 완료된 DB**는 다음 두 가지입니다.

| DB | 구성 | 비고 |
|----|------|------|
| **HSQL** (내장) | `DB_TYPE` 미지정 시 기본값 | 별도 DB 서버 불필요, 개발·데모 용도 |
| **MySQL** | `DB_TYPE=mysql` + ConfigMap/Secret | `docker-compose.yml`·ConfigMap 기본값 |

위 DDL/DML 표에 나열된 Oracle, Tibero, Altibase, CUBRID용 스크립트는 **base 템플릿
리포지토리(`DATABASE/` 디렉터리)에서 제공하는 것**으로, 해당 DB를 사용하려면 다음
절차가 추가로 필요합니다.

1. **JDBC 드라이버 의존성 추가** — `pom.xml`에 해당 DB 드라이버를 추가하고 이미지를 재빌드합니다.
2. **DDL/DML 적재** — 위 표의 스크립트를 대상 DB에 수동 적재합니다.
3. **환경변수 설정** — `DB_TYPE`, `DB_URL`, `DB_DRIVER_CLASS_NAME` 등을 ConfigMap/Secret에 맞게 지정합니다.
4. **`application.properties` 확인** — `Globals.<dbtype>.DriverClassName` 등 키가 올바르게 매핑되는지 확인합니다.

> 자세한 DB 확장 설정은 base 템플릿의 `application.properties` 및
> `src/main/java/egovframework/com/config/EgovConfigAppDatasource.java`를 참고하세요.

## 파일 업로드 저장소 (중요)

게시판 첨부파일은 앱이 `Globals.fileStorePath` 경로에 저장합니다. base 템플릿 기본값은
상대경로 `./files`인데, 이 값은 `readOnlyRootFilesystem: true` 환경에서 서블릿 임시
디렉터리 기준으로 풀려 쓰기에 실패합니다(첨부 업로드 시 `FileNotFoundException` → 500).

본 구성은 이를 다음과 같이 해결합니다.

- `configmap.yaml`의 `FILE_STORE_PATH=/app/files`를 `docker-entrypoint.sh`가
  `Globals.fileStorePath`(절대경로)로 매핑합니다.
- `pvc.yaml`(ReadWriteOnce, 1Gi)을 `deployment.yaml`의 `/app/files`에 마운트하여,
  쓰기 가능하고 Pod 재시작 후에도 보존되는 영속 저장소를 제공합니다.

> **레플리카와 스토리지** — `ReadWriteOnce` PVC는 하나의 Pod만 마운트할 수 있으므로
> `replicas: 1` + `strategy: Recreate`로 구성했습니다. 다중 레플리카로 확장하려면
> `ReadWriteMany`를 지원하는 스토리지(NFS, CephFS, 클라우드 파일스토리지 등)로
> `pvc.yaml`의 `accessModes`를 변경하고 `replicas`를 늘리세요.

저장 경로를 바꾸려면 `configmap.yaml`의 `FILE_STORE_PATH`와 `deployment.yaml`의
`app-files` 볼륨 `mountPath`를 동일하게 맞추면 됩니다.

## 배포

```bash
# 네임스페이스 생성(필요 시)
kubectl create namespace egovframe

# 매니페스트 적용
kubectl apply -f k8s/configmap.yaml -n egovframe
kubectl apply -f k8s/pvc.yaml       -n egovframe
kubectl apply -f k8s/service.yaml   -n egovframe
kubectl apply -f k8s/deployment.yaml -n egovframe
```

## 배포 확인

```bash
# Pod 상태 확인
kubectl get pods -n egovframe -l app.kubernetes.io/name=egovframe-template-simple-backend

# 롤아웃 완료 대기
kubectl rollout status deployment/egovframe-template-simple-backend -n egovframe

# 로그 확인
kubectl logs -l app.kubernetes.io/name=egovframe-template-simple-backend -n egovframe --tail=50
```

## 헬스체크

`readinessProbe`와 `livenessProbe`는 Spring Boot Actuator 엔드포인트를 사용합니다.

| 프로브 | 경로 | 설명 |
|--------|------|------|
| readiness | `/actuator/health/readiness` | 요청 수신 준비 여부 |
| liveness | `/actuator/health/liveness` | 프로세스 정상 동작 여부 |

Actuator는 `spring-boot-starter-actuator` 의존성으로 활성화됩니다.
`management.endpoint.health.probes.enabled=true` 설정으로 readiness/liveness 경로가 노출됩니다.

수동으로 헬스 상태를 확인하려면:

```bash
kubectl port-forward svc/egovframe-template-simple-backend 8080:8080 -n egovframe
curl http://localhost:8080/actuator/health
```

정상 응답 예시:

```json
{"status":"UP","groups":["liveness","readiness"]}
```

## 환경변수 커스터마이징

`configmap.yaml`에서 아래 항목을 운영 환경에 맞게 조정합니다.

| 키 | 기본값 | 설명 |
|----|--------|------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Spring 프로파일 |
| `DB_TYPE` | `mysql` | DB 종류(`Globals.DbType`로 매핑). 비우면 내장 HSQL |
| `DB_URL` | MySQL 로컬 | DB 접속 URL(`Globals.<dbtype>.Url`로 매핑) |

## 삭제

```bash
kubectl delete -f k8s/ -n egovframe
```
