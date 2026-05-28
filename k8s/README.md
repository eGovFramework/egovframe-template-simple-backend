# Kubernetes 배포 가이드

## 개요

이 디렉토리에는 `egovframe-template-simple-backend`를 Kubernetes 클러스터에 배포하기 위한 매니페스트가 포함되어 있습니다.

| 파일 | 설명 |
|------|------|
| `configmap.yaml` | 애플리케이션 설정(프로파일, DB URL 등) |
| `service.yaml` | ClusterIP 서비스(포트 8080) |
| `deployment.yaml` | 애플리케이션 Deployment(레플리카 2, 롤링 업데이트) |

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

## DB 시크릿 생성

`deployment.yaml`은 `egovframe-template-simple-backend-db` 이름의 Secret에서 DB 자격증명을 읽습니다.
배포 전 아래 명령으로 Secret을 생성하세요.

```bash
kubectl create secret generic egovframe-template-simple-backend-db \
  --from-literal=username=<DB_USER> \
  --from-literal=password=<DB_PASSWORD>
```

## 배포

```bash
# 네임스페이스 생성(필요 시)
kubectl create namespace egovframe

# 매니페스트 적용
kubectl apply -f k8s/configmap.yaml -n egovframe
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
| `SPRING_DATASOURCE_URL` | MySQL 로컬 | DB 접속 URL |

## 삭제

```bash
kubectl delete -f k8s/ -n egovframe
```
