# 🚀 Spring Boot Portfolio (Backend)

## 📌 프로젝트 소개
JWT 인증과 Redis 캐싱을 적용한 게시판 서비스입니다.  
실무에서 사용하는 구조와 기술을 기반으로 개발했습니다.

---

## 🛠 기술 스택

- Backend: Spring Boot 3
- Security: Spring Security + JWT
- Database: JPA (H2 / MySQL)
- Cache: Redis
- Build Tool: Gradle

---

## 📌 주요 기능

### 👤 회원
- 회원가입
- 로그인 (JWT 발급)

### 📝 게시판
- 게시글 작성
- 게시글 조회
- 게시글 수정
- 게시글 삭제
- 페이징 처리

### 💬 댓글
- 댓글 작성
- 댓글 조회

### ❤️ 좋아요
- 좋아요 / 취소 (토글)
- Redis 캐싱 적용 (성능 최적화)

---

## 📌 아키텍처

- Domain 중심 패키지 구조
- JWT 기반 Stateless 인증
- Global Exception Handler 적용
- Redis를 활용한 캐싱 구조

---

## 📌 프로젝트 구조
com.example.portfolio
├ config
├ domain
│ ├ user
│ ├ post
│ ├ comment
│ └ like
├ controller
├ jwt
└ global

---

## 🚀 실행 방법

### 1️⃣ Redis 실행 (Docker)
docker run -d -p 6379:6379 redis

### 2️⃣ 프로젝트 실행
./gradlew bootRun

---

## 📌 API 예시

### 🔐 로그인
POST /api/auth/login

### 📝 게시글 조회
GET /api/posts

### ❤️ 좋아요
POST /api/likes/{postId}


---

## 💡 설계 포인트

- JWT 기반 인증으로 Stateless 구조 구현
- 좋아요 기능에 Redis 캐싱 적용으로 DB 부하 감소
- GlobalExceptionHandler를 통한 일관된 에러 처리

---

## 👨‍💻 개발자
- 이름: Y.YB
- 역할: 백엔드 개발
- 