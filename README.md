# 🚀 Spring Boot Portfolio (JWT 인증 게시판)

## 📌 프로젝트 소개

JWT 기반 인증 시스템을 적용한 게시판 프로젝트입니다.
회원가입 / 로그인 / 게시글 / 댓글 / 좋아요 기능을 구현하였으며,
Spring Security + JWT + 도메인 중심 설계를 기반으로 개발했습니다.

---

## 🛠 기술 스택

### Backend

* Java 21
* Spring Boot 3
* Spring Security
* JWT
* Spring Data JPA
* H2 Database
* Redis

### DevOps

* Gradle
* GitHub
* Docker (Redis 컨테이너 실행)

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
| ---- | ------ | ---------------- | ------------------ | --------- |
| 회원가입 | POST   | /api/auth/signup | username, password | 성공 메시지    |
| 로그인  | POST   | /api/auth/login  | username, password | JWT Token |

---

### 📝 게시글 API

| 기능     | Method | URL                       | 설명     |
| ------ | ------ | ------------------------- | ------ |
| 게시글 생성 | POST   | /api/posts                | 게시글 작성 |
| 게시글 목록 | GET    | /api/posts?page=0&size=10 | 페이징 조회 |
| 게시글 조회 | GET    | /api/posts/{id}           | 단건 조회  |

---

### 💬 댓글 API

| 기능    | Method | URL                    | 설명    |
| ----- | ------ | ---------------------- | ----- |
| 댓글 작성 | POST   | /api/comments/{postId} | 댓글 등록 |
| 댓글 조회 | GET    | /api/comments/{postId} | 댓글 목록 |

---

### ❤️ 좋아요 API

| 기능     | Method | URL                 | 설명    |
| ------ | ------ | ------------------- | ----- |
| 좋아요 토글 | POST   | /api/likes/{postId} | 추가/취소 |
| 좋아요 개수 | GET    | /api/likes/{postId} | 개수 조회 |

---

## ⚙ 주요 구현 포인트

* JWT 기반 Stateless 인증 구조
* Spring Security Filter 직접 구현
* 도메인 중심 패키지 구조 설계
* Redis 기반 좋아요 캐싱 (Cache Aside 패턴)
* 좋아요 동시성 문제 해결 (DB Unique + 예외 처리)
* 페이징 처리 (Pageable)

---

## 🧪 실행 방법

### 1. 프로젝트 클론

```
git clone https://github.com/doltank777/portfolio-backend.git
```
### 2. Redis 실행 (Docker)
```
docker run -d -p 6379:6379 redis
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

## 🔧 향후 개선 사항

* MySQL 적용
* Redis 캐시 TTL 및 고도화 전략
* Docker Compose 구성
* AWS EC2 배포
* CI/CD 구축 (GitHub Actions)

---

## 👨‍💻 개발자

* 이름: Y.YB
* GitHub: https://github.com/doltank777

---
