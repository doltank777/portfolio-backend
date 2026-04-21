# 📌 Portfolio Backend (Spring Boot)

## 📖 프로젝트 소개
React 프론트엔드와 연동되는 **게시판 REST API 서버**입니다.  
JWT 기반 인증을 적용했으며, 게시글 / 댓글 / 좋아요 / 조회수 기능을 포함한 포트폴리오용 백엔드 프로젝트입니다.

단순 CRUD를 넘어서 **인증, 권한 처리, 조회수 증가, Redis 기반 좋아요 캐싱, 연관 데이터 삭제 처리**까지 고려하여 구현했습니다.

---

## 🛠 기술 스택

### Backend
- Java 21
- Spring Boot 3
- Spring Security
- JWT
- Spring Data JPA
- MySQL
- Redis

### DevOps / Infra
- Gradle
- Docker
- Docker Compose
- GitHub

---

## 📂 프로젝트 구조

```
com.example.portfolio
 ├─ controller
 ├─ domain
 │   ├─ user
 │   ├─ post
 │   ├─ comment
 │   └─ like
 └─ global
     ├─ config
     ├─ error
     └─ security
```

➡ 도메인 중심 패키지 구조를 적용하여 유지보수성과 확장성을 고려했습니다.

---

## 🧱 ERD

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
 ├─ view_count
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
 └─ post_id (FK)
```

### 🔗 관계 설명

* User 1 : N Post
* User 1 : N Comment
* Post 1 : N Comment
* User N : N Post (Like)

---

## 🔐 인증 방식 (JWT)

* 회원가입 후 로그인 시 JWT 토큰 발급
* 이후 요청 시 Header 포함

```
Authorization: Bearer {JWT_TOKEN}
```


* Spring Security Filter 기반으로 인증 처리

---
## ✨ 주요 기능

### 🔐 인증
- 회원가입
- 로그인
- JWT 토큰 발급 및 인증 처리

### 📝 게시글
- 게시글 작성
- 게시글 목록 조회 (페이징)
- 게시글 상세 조회
- 게시글 수정
- 게시글 삭제
- 게시글 상세 조회 시 조회수 증가

### 💬 댓글
- 댓글 작성
- 게시글별 댓글 조회
- 댓글 삭제

### ❤️ 좋아요
- 좋아요 토글
- 좋아요 개수 조회
- 현재 로그인 사용자의 좋아요 여부 조회
- Redis 기반 좋아요 개수 캐싱

---

## 🧾 API 명세

### 인증 API

| 기능 | Method | URL | 설명 |
|---|---|---|---|
| 회원가입 | POST | `/api/auth/signup` | 회원가입 |
| 로그인 | POST | `/api/auth/login` | JWT 문자열 반환 |

---

### 게시글 API

| 기능 | Method | URL | 설명 |
|---|---|---|---|
| 게시글 생성 | POST | `/api/posts` | 게시글 작성 |
| 게시글 목록 | GET | `/api/posts?page=0&size=10` | 페이징 조회 |
| 게시글 조회 | GET | `/api/posts/{id}` | 단건 조회 + 조회수 증가 |
| 게시글 수정 | PUT | `/api/posts/{id}` | 작성자 본인만 수정 가능 |
| 게시글 삭제 | DELETE | `/api/posts/{id}` | 작성자 본인만 삭제 가능 |

---

### 댓글 API

| 기능 | Method | URL | 설명 |
|---|---|---|---|
| 댓글 작성 | POST | `/api/comments/{postId}` | 댓글 등록 |
| 댓글 조회 | GET | `/api/comments/{postId}` | 댓글 목록 조회 |
| 댓글 삭제 | DELETE | `/api/comments/{commentId}` | 댓글 삭제 |

---

### 좋아요 API

| 기능 | Method | URL | 설명 |
|---|---|---|---|
| 좋아요 토글 | POST | `/api/likes/{postId}` | 좋아요 추가/취소 |
| 좋아요 개수 | GET | `/api/likes/{postId}` | 좋아요 수 조회 |
| 내 좋아요 상태 | GET | `/api/likes/{postId}/me` | 현재 로그인 사용자 기준 |

---

## ⚙️ 실행 방법

### 1. 애플리케이션 실행

```
./gradlew bootRun
```

### 2. Docker 환경 실행
```
docker-compose up -d
```
---

## 💡 주요 구현 포인트

### 1. JWT 기반 인증
- Stateless 방식 인증 처리
- Spring Security Filter 기반 인증 구조 적용

### 2. 조회수 기능
- 게시글 상세 조회 시 조회수 증가
- 중복 증가 방지 구조 고려
- 응답 DTO에 조회수 포함

### 3. 좋아요 캐싱
- Redis를 활용한 좋아요 개수 캐싱
- TTL 기반 캐시 전략 적용
- DB와 캐시 간 동기화 구조 설계

### 4. 권한 처리
- 게시글 수정/삭제 시 작성자 본인 여부 검증
- 인증 사용자 기반 접근 제어

### 5. 연관 데이터 삭제 처리
- 게시글 삭제 시 좋아요 / 댓글 선삭제 처리
- FK 제약 조건 문제 해결

### 6. DTO 기반 설계
- Entity 직접 노출 방지
- 프론트 요구사항에 맞는 응답 구조 제공
- likeCount, viewCount 포함

---

## 🐳 Docker 구성

- MySQL, Redis를 Docker Compose로 구성
- 로컬 개발 환경에서 동일한 실행 환경 제공

[Client]
↓
Spring Boot
↓
MySQL (DB)
Redis (Cache)

---

## 🔐 보안 고려사항

- JWT 기반 인증 처리 (Stateless)
- 인증 API 접근 시 토큰 검증 필터 적용
- 게시글 수정/삭제 시 작성자 권한 검증
- 비밀번호 BCrypt 암호화 저장

---

## 🚧 향후 개선 사항

- Redis 캐시 전략 고도화
- 조회수 중복 방지 로직 개선 (세션/IP 기반)
- 이미지 업로드 기능 (S3 연동)
- API 응답 표준화
- 에러 처리 및 사용자 메시지 개선

---

## 🔗 프론트엔드 레포지토리

👉 https://github.com/doltank777/portfolio-frontend

---

## 👨‍💻 개발자

- 이름: Y.YB
- GitHub: https://github.com/doltank777- 
---