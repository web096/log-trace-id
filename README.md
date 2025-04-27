# Spring Boot API Response DEMO

> Java 17 + Spring Boot 기반으로 "traceId 기반 통합 API 응답 시스템"을 구축한 프로젝트입니다.

---

## ✨ 프로젝트 개요

이 프로젝트는 다음 기능을 목표로 개발되었습니다.

- 요청마다 고유한 `traceId`를 생성하고, 모든 응답에 포함
- 성공/실패 응답 모두 `{meta, data}` 구조로 통일
- `ResponseBodyAdvice`를 이용해 자동 감싸기
- `GlobalExceptionHandler`를 이용해 모든 예외 응답 일관화
- `MDC`를 이용해 모든 로그에 `traceId` 자동 삽입
- 특정 요청(URL 패턴) 제외 기능 지원 (ex. `/actuator`, `/swagger`)

---

## 📦 기술 스택

- Java 17
- Spring Boot 3.x
- Lombok
- SLF4J + Logback (MDC 활용)
- Jackson (ObjectMapper 사용)
- Gradle

---

## 🛠️ 주요 기능

### 1. TraceId 관리
- 모든 요청마다 UUID 기반 `traceId`를 생성
- `ThreadLocal` + `MDC`를 통해 요청 단위로 관리
- `traceId`는 응답 meta 정보와 로그에 자동 삽입

### 2. 공통 응답 포맷

모든 API 응답은 다음 구조를 따른다:

```json
{
  "meta": {
    "code": 200,
    "message": "success",
    "traceId": "생성된-traceId"
  },
  "data": { ... }
}
```

| meta 항목 | 설명 |
|:---|:---|
| code | HTTP 상태 코드 (ex. 200, 400, 500) |
| message | 성공/에러 메시지 |
| traceId | 요청별 고유 ID |

### 3. 예외 처리 일관화

- `BaseCustomException`을 상속받은 예외 관리
- `ErrorCode` enum을 통해 에러코드/메시지 일관화
- `GlobalExceptionHandler`를 통해 모든 예외 통합 처리

### 4. 로그 추적성 향상 (MDC)

- `MDC`를 활용해 모든 로그에 `traceId` 자동 삽입
- `logback-spring.xml` 또는 `application.yml`을 통해 로그 패턴 설정

---

## 📜 주요 클래스 구조

| 클래스 | 역할 |
|:---|:---|
| `TraceContext` | ThreadLocal로 traceId 저장/조회/삭제 관리 |
| `TraceIdFilter` | 요청마다 traceId 생성 + MDC 저장 |
| `CommonResponse` | meta+data 구조를 가진 공통 응답 DTO |
| `CommonResponseAdvice` | Controller 응답을 자동으로 CommonResponse로 감싸는 Advice |
| `ResultResponse` | Controller에서 명시적으로 성공/에러 응답을 생성할 때 사용 |
| `GlobalExceptionHandler` | 모든 예외를 처리하고 일관된 에러 응답 반환 |
| `ErrorCode` | 에러 코드/메시지를 enum으로 관리 |
| `BaseCustomException`, `ApiException`, `JwtException` | 커스텀 예외 계층 구성 |

---

## 🧭 시스템 전체 흐름

```
[Client Request]
    ↓
[TraceIdFilter] → UUID 생성 + TraceContext(ThreadLocal) + MDC(traceId) 저장
    ↓
[Spring DispatcherServlet]
    ↓
[Controller] → TraceContext.getTraceId()로 traceId 조회 및 사용 가능
    ↓
[ResponseBodyAdvice] → CommonResponse(meta + data + traceId)로 감싸기
    ↓
[HTTP Response 반환]
    ↓
[Client Response]
```

### 🔥 흐름 요약
- 요청이 들어오면 가장 먼저 Filter가 동작해 traceId를 생성하고 저장
- Controller에서 별다른 설정 없이 traceId 조회 가능
- ResponseBodyAdvice가 모든 응답을 meta+data+traceId 구조로 통일
- 로그에는 MDC를 통해 traceId가 자동으로 삽입
- Client는 항상 일관된 구조(meta, data, traceId)를 가진 응답을 받게 됨

---

## 🛠️ 예제

### 성공 응답 예시
```http
GET /api/v1/users/1

RESPONSE 200 OK
{
  "meta": {
    "code": 200,
    "message": "success",
    "traceId": "생성된-traceId"
  },
  "data": {
    "id": 1,
    "name": "홍길동",
    "email": "hong@example.com"
  }
}
```

### 에러 응답 예시
```http
GET /api/v1/errors/api-exception

RESPONSE 401 Unauthorized
{
  "meta": {
    "code": 401,
    "message": "Invalid token",
    "traceId": "f7e4d3b2-9c1a-48d7-8e67-abc123def456"
  },
  "data": null
}
```

---

## 🔥 주요 특징 요약

- 모든 요청 추적이 가능한 `traceId` 관리
- 성공/실패 응답 포맷 통일로 프론트엔드 처리 일관성 제공
- Controller 개발자가 실수해도 ResponseBodyAdvice로 응답 포맷 보호
- 예외 상황에서도 meta+data 구조 통일
- 모든 로그에 traceId 삽입 → 디버깅/트러블슈팅 강력 지원

---

## 🚀 향후 확장 계획 (Optional)

- Validation 에러(`MethodArgumentNotValidException`) 커스터마이징
- OAuth2, JWT 인증 로직 추가 시 traceId 전파
- Kafka, Redis 등 외부 통신에도 traceId 삽입

---

## 📂 프로젝트 실행 방법

```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun
```
또는

```bash
mvn spring-boot:run
```

---

## 🧹 주의사항

- Swagger, Actuator, HealthCheck 등 외부 인터페이스 URL은 공통 응답 감싸기를 제외하도록 설정되어 있습니다.
- 요청 필터 체인(Filter) 초기에 traceId가 생성되므로, 컨트롤러/서비스 어디서든 추적 가능합니다.

---
