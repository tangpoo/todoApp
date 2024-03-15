tangpoo의 back-end 개인프로젝트 todoApp 의 README 입니다.

---

## 주요 기능
- 회원가입, 로그인
- 스케줄 작성, 조회, 수정, 삭제
    - 스케줄 관련 모든 기능은 유저 권한이 있어야 한다.
    - 스케줄 조회는 본인이 작성한 게시글만 가능하다.
- 댓글 작성, 조회, 수정, 삭제
    - 댓글 관련 모든 기능은 유저 권한이 있어야 한다.
    - 댓글 조회는 게시글 조회와 함께 이루어진다.

---

## 프로젝트 기간
- 2024-02-02 ~ 2024-02-05 기본 api 완성
- ~ 리팩토링 및 추가 기능, 기술 구현

---

## Prerequisites
- JDK amazoncorretto 17
- Spring Boot 3.2.2
- Springdoc 2.2.0
- Gradle 8.5
- MySQL 8.0.35

## ERD
![ERD](https://github.com/tangpoo/todoApp/assets/131866367/3caafaab-5779-4b17-aace-80f1ab06cc91)

---

## API 명세서
SAGGER API 명세서
![image](https://github.com/tangpoo/todoApp/assets/131866367/a579a083-a01c-452a-aa7c-a0eb1943ce3b)

[Postman API 명세서](https://documenter.getpostman.com/view/32381127/2s9YyweKDA)


## 개선사항
- 2024-02-20 : 테스트 코드 작성
- 2024-02-23 : AWS 배포
- 2024-03-15 : 로그인 환경 변화로 인한 주석처리
- 2024-03-15 : refreshToken 구현