# Todo List Service

Vue 프론트엔드와 Spring Boot 백엔드로 구성된 할 일 관리 서비스입니다.  
사용자는 날짜별 할 일을 기록하고, 완료 상태를 관리할 수 있습니다.

## Repository 구조
- Frontend (Vue): https://github.com/USER/todo-frontend
- Backend (Spring Boot): https://github.com/USER/todo-backend

---

## 주요 기능
| 기능 | 설명 |
|---|---|
| 회원 인증 (옵션) | 사용자 로그인 / 세션 유지 |
| 날짜별 Todo 조회 | 특정 날짜의 할 일 목록 제공 |
| Todo 생성/수정/삭제 | 기본적인 CRUD 지원 |
| 완료 체크 | Todo 완료 여부 토글 |

---

## 사용 기술

### Frontend
- Vue 3 / Composition API
- TypeScript
- Pinia / Axios
- Vite

### Backend
- Java 17 / Spring Boot
- Spring Data JPA / Hibernate
- MariaDB
- Gradle
- Docker / Docker Compose
- Github Actions

---

## 시스템 아키텍처
[Vue Client] ⇄ [REST API - Spring Boot] ⇄ [DB]
---

## 실행 방법
각 저장소의 README 참고
