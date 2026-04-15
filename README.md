# 🚀 Spring Boot Portfolio (JWT 인증 게시판)

## 📌 프로젝트 소개

JWT 기반 인증 시스템을 적용한 게시판 프로젝트입니다.  
회원가입 / 로그인 / 게시글 / 댓글 / 좋아요 기능을 구현하였으며,  
Spring Security + JWT + Redis + MySQL 기반으로 설계된 백엔드 구조입니다.

---

## 🛠 기술 스택

### Backend

* Java 21
* Spring Boot 3
* Spring Security
* JWT
* Spring Data JPA
* MySQL
* Redis

### DevOps

* Gradle
* GitHub
* Docker
* Docker Compose

---

## 📂 프로젝트 구조

```
com.example.portfolio
 ├─ config
 ├─ domain
 │   ├─ user
 │   ├─ post
 │   ├─ comment
 │   └─ like
 ├─ jwt
 └─ global
```

➡ 도메인 중심 패키지 구조를 적용하여 유지보수성과 확장성을 고려했습니다.

---

## 🗄 ERD (Entity Relationship Diagram)

```
User
 ├─ id (PK)
 ├─ username
 ├─ password
 └─ created_at

Post
 ├─ id (PK)
 ├─ title
 ├─ content
 ├─ user_id (FK)
 └─ created_at

Comment
 ├─ id (PK)
 ├─ content
 ├─ user_id (FK)
 ├─ post_id (FK)
 └─ created_at

Like
 ├─ id (PK)
 ├─ user_id (FK)
 ├─ post_id (FK)
```

### 🔗 관계 설명

* User 1 : N Post
* User 1 : N Comment
* Post 1 : N Comment
* User N : N Post (Like)

---

## 🔐 인증 방식 (JWT)

* 로그인 시 JWT 토큰 발급
* 이후 요청 시 Header 포함

```
Authorization: Bearer {JWT_TOKEN}
```


* Spring Security Filter 기반 인증 처리

---

## 📡 API 명세

### 🔑 인증 API

| 기능   | Method | URL              | Request            | Response  |
|--------|--------|------------------|--------------------|----------|
| 회원가입 | POST   | /api/auth/signup | username, password | 성공 메시지 |
| 로그인  | POST   | /api/auth/login  | username, password | JWT Token |

---

### 📝 게시글 API

| 기능     | Method | URL                       | 설명     |
|----------|--------|---------------------------|----------|
| 게시글 생성 | POST   | /api/posts                | 게시글 작성 |
| 게시글 목록 | GET    | /api/posts?page=0&size=10 | 페이징 조회 |
| 게시글 조회 | GET    | /api/posts/{id}           | 단건 조회  |

---

### 💬 댓글 API

| 기능    | Method | URL                    | 설명    |
|---------|--------|------------------------|--------|
| 댓글 작성 | POST   | /api/comments/{postId} | 댓글 등록 |
| 댓글 조회 | GET    | /api/comments/{postId} | 댓글 목록 |

---

### ❤️ 좋아요 API

| 기능     | Method | URL                 | 설명        |
|----------|--------|---------------------|------------|
| 좋아요 토글 | POST   | /api/likes/{postId} | 추가/취소     |
| 좋아요 개수 | GET    | /api/likes/{postId} | Redis 캐시 조회 |

---

## ⚙ 주요 구현 포인트

### 🔐 인증
* JWT 기반 Stateless 인증 구조
* Spring Security Filter 직접 구현

### 🏗 아키텍처
* 도메인 중심 패키지 구조 설계
* DTO 기반 API 설계

### ⚡ 성능 최적화
* JPA N+1 문제 해결 (Fetch Join)
* DTO 직접 조회로 성능 개선
* Pageable 기반 페이징 처리

### 🚀 Redis 캐싱
* Cache Aside 패턴 적용
* 좋아요 수 Redis 캐싱
* Redis Set을 활용한 사용자 중복 방지

### 🔒 동시성 처리
* DB Unique Constraint 적용
* Redis 기반 빠른 처리 + DB 보완 구조

---

## 🧪 실행 방법

### 1. 프로젝트 클론

```
git clone https://github.com/doltank777/portfolio-backend.git
```
### 2. Docker 실행
```
docker-compose up -d
```

### 3. 애플리케이션 실행

```
./gradlew bootRun
```

또는 IntelliJ에서 실행

---

### 4. 접속

```
http://localhost:8080
```

---

## 🐳 Docker 구성

* MySQL 컨테이너
* Redis 컨테이너
* Spring Boot 애플리케이션

[Client]
↓
Spring Boot
↓
MySQL (DB)
Redis (Cache)

---

## 🔐 보안 고려사항

* JWT Secret Key 256bit 이상 설정
* 토큰 검증 로직 서버 측 처리
* JWT 라이브러리 취약점(CVE-2024-31033) 인지 및 안전한 사용

---

## 🔧 향후 개선 사항

* Redis TTL 및 캐시 전략 고도화
* QueryDSL 도입
* AWS EC2 배포
* CI/CD 구축 (GitHub Actions)
* MSA 구조 확장

---

## 👨‍💻 개발자

* 이름: Y.YB
* GitHub: https://github.com/doltank777

---
