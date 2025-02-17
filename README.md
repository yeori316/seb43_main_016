<h1>EduSync Refactoring</h1>
스터디 모집 & 관리 플랫폼
<br><br>
기존 <br>
<a href="https://www.edusync.site">프론트 배포</a> / <a href="https://wish17.store">백엔드 배포</a>
<br>

리팩토링 <br>
<a href="http://edusync-refector.s3-website-us-east-1.amazonaws.com">프론트 배포</a> / 
<a href="https://ec2-3-36-48-195.ap-northeast-2.compute.amazonaws.com">백엔드 배포</a>
<br>

## Refactoring 진행
1. DB 정규화
2. Git Action AWS 자동 배포
3. AWS S3 이미지 저장
4. URL 쿼리 스트링 난독화
5. 클린 코딩
6. 테스트 코드 작성
7. 성능 부하 테스트
<br>

## Refactoring 멤버
<table>
    <thead>
        <tr>
            <th align="center">FE</th>
            <th align="center">BE</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td align="center"><a href="https://github.com/Whaleinmilktea"><img src="https://avatars.githubusercontent.com/u/109408848?v=4" alt="강하성" style="max-width: 10%;"></a></td>
            <td align="center"><a href="https://github.com/yeori316"><img src="https://avatars.githubusercontent.com/u/78740368?v=4" alt="양도열" style="max-width: 10%;"></a></td>
        </tr>
        <tr>
            <td align="center"><a href="https://whaleinmilktea.tistory.com/">강하성</a></td>
            <td align="center"><a href="https://velog.io/@yeori316">양도열</a></td>
        </tr>
    </tbody>
</table>
<br>

***

## 프로젝트 기간 및 소개
> 프로젝트 기간 : 2023.04.28 ~ 2023.05.25
>
> 프로젝트 소개
> Edusync는 자신이 원하는 IT 분야의 스터디 그룹을 찾고, 스터디 그룹원의 모집 / 구성 / 관리를 돕는 플랫폼입니다.

<br>

## 팀원 소개
- 이미지 누르면 GitHub 주소로 연결됩니다.
- 이름을 누르면 블로그 주소로 연결됩니다.

<table>
<thead>
<tr>
<th align="center">BE(팀장)</th>
<th align="center">BE</th>
<th align="center">BE</th>
<th align="center">FE(팀장)</th>
<th align="center">FE</th>
<th align="center">FE</th>
</tr>
</thead>
<tbody>
<tr>
<td align="center"><a href="https://github.com/serveman"><img src="https://avatars.githubusercontent.com/u/120380939?v=4" alt="정동우" style="max-width: 100%;"></a></td>
<td align="center"><a href="https://github.com/wish9"><img src="https://avatars.githubusercontent.com/u/120456261?v=4" alt="위원종" style="max-width: 100%;"></a></td>
<td align="center"><a href="https://github.com/yeori316"><img src="https://avatars.githubusercontent.com/u/78740368?v=4" alt="양도열" style="max-width: 100%;"></a></td>
<td align="center"><a href="https://github.com/songhaeunsong"><img src="https://avatars.githubusercontent.com/u/84169393?v=4" alt="송하은" style="max-width: 100%;"></a></td>
<td align="center"><a href="https://github.com/Whaleinmilktea"><img src="https://avatars.githubusercontent.com/u/109408848?v=4" alt="강하성" style="max-width: 100%;"></a></td>
<td align="center"><a href="https://github.com/lain-alice"><img src="https://avatars.githubusercontent.com/u/119744952?v=4" alt="이승현" style="max-width: 100%;"></a></td>
</tr>
<tr>
<td align="center"><a href="https://github.com/serveman">정동우</a></td>
<td align="center"><a href="https://velog.io/@wish17">위원종</a></td>
<td align="center"><a href="https://velog.io/@yeori316">양도열</a></td>
<td align="center"><a href="https://github.com/songhaeunsong">송하은</a></td>
<td align="center"><a href="https://whaleinmilktea.tistory.com/">강하성</a></td>
<td align="center"><a href="https://lain-alice.tistory.com/">이승현</a></td>
</tr>
</tbody>
</table>

<br>

## Project Diagram & Stack

✨Project Diagram✨
<a><img src="https://i.ibb.co/t3dvGX2/diagram.jpg" alt="diagram" border="0" width="130%" width="130%"></a>
✨Backend✨

<div align="center">
    <a href="https://velog.io/@wish17?tag=Java"><img src="https://img.shields.io/badge/Java11-007396?style=flat&logo=java&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/%EC%BD%94%EB%93%9C%EC%8A%A4%ED%85%8C%EC%9D%B4%EC%B8%A0-%EB%B0%B1%EC%97%94%EB%93%9C-%EB%B6%80%ED%8A%B8%EC%BA%A0%ED%94%84-49%EC%9D%BC%EC%B0%A8-Spring-MVC-JPA-%EA%B8%B0%EB%B0%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%95%A1%EC%84%B8%EC%8A%A4-%EA%B3%84%EC%B8%B5"><img src="https://img.shields.io/badge/JPA-339933?style=flat&logo=hibernate&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/%EC%BD%94%EB%93%9C%EC%8A%A4%ED%85%8C%EC%9D%B4%EC%B8%A0-%EB%B0%B1%EC%97%94%EB%93%9C-%EB%B6%80%ED%8A%B8%EC%BA%A0%ED%94%84-34%EC%9D%BC%EC%B0%A8-Spring-Core-Spring-Framework-%EA%B8%B0%EB%B3%B8#spring-boot"><img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=spring&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/%EC%BD%94%EB%93%9C%EC%8A%A4%ED%85%8C%EC%9D%B4%EC%B8%A0-%EB%B0%B1%EC%97%94%EB%93%9C-%EB%B6%80%ED%8A%B8%EC%BA%A0%ED%94%84-57%EC%9D%BC%EC%B0%A8-Spring-MVC-API-%EB%AC%B8%EC%84%9C%ED%99%94#spring-rest-docs"><img src="https://img.shields.io/badge/RestDocs-8CA1AF?style=flat&logo=asciidoctor&logoColor=white" /></a>
    <a href="https://velog.io/@wish17?tag=db"><img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" /></a>
    <a href="https://velog.io/@wish17?tag=spring-security"><img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat&logo=spring&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/%EC%BD%94%EB%93%9C%EC%8A%A4%ED%85%8C%EC%9D%B4%EC%B8%A0-%EB%B0%B1%EC%97%94%EB%93%9C-%EB%B6%80%ED%8A%B8%EC%BA%A0%ED%94%84-65%EC%9D%BC%EC%B0%A8-JWT-%EC%9D%B8%EC%A6%9DAuthentication"><img src="https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtoken&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/%EC%BD%94%EB%93%9C%EC%8A%A4%ED%85%8C%EC%9D%B4%EC%B8%A0-%EB%B0%B1%EC%97%94%EB%93%9C-%EB%B6%80%ED%8A%B8%EC%BA%A0%ED%94%84-67%EC%9D%BC%EC%B0%A8-OAuth2-%EC%9D%B8%EC%A6%9D"><img src="https://img.shields.io/badge/OAuth2-3EAAAF?style=flat&logo=openid&logoColor=white" /></a>
    <a href="https://velog.io/@wish17?tag=aws"><img src="https://img.shields.io/badge/AWS-232F3E?style=flat&logo=amazonaws&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/%EC%84%9C%EB%B2%84-%EB%B0%B0%ED%8F%ACaws-Session-Manager-%EC%97%B0%EA%B2%B0"><img src="https://img.shields.io/badge/EC2-232F3E?style=flat&logo=amazonaws&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/u1p70k28"><img src="https://img.shields.io/badge/RDS-232F3E?style=flat&logo=amazonaws&logoColor=white" /></a>
    <a href="https://velog.io/@wish17/JWT-%EB%B3%B4%EC%95%88%EC%97%90-%EA%B4%80%ED%95%9C-%EA%B3%A0%EC%B0%B0"><img src="https://img.shields.io/badge/Redis-D92E20?style=flat&logo=redis&logoColor=white" /></a>
</div>

✨Frontend✨

<div align="center">
<a href="https://img.shields.io/badge/React-61DAFB?style=flat&logo=React&logoColor=black">
<img src="https://img.shields.io/badge/React-61DAFB?style=flat&logo=React&logoColor=black"/>
</a>
<a href="https://img.shields.io/badge/vite-646cff?style=flat&logo=vite&logoColor=white">
<img src="https://img.shields.io/badge/vite-646cff?style=flat&logo=vite&logoColor=white"/>
<a href="https://img.shields.io/badge/ReactRouter-CA4245?style=flat&logo=ReactRouter&logoColor=white">
</a>
<a href="https://img.shields.io/badge/ReactRouter-CA4245?style=flat&logo=ReactRouter&logoColor=white">
<img src="https://img.shields.io/badge/ReactRouter-CA4245?style=flat&logo=ReactRouter&logoColor=white"/>
</a>
<a href="https://img.shields.io/badge/Recoil-3578e5?style=flat&logo=Recoil&logoColor=white">
<img src="https://img.shields.io/badge/Recoil-3578e5?style=flat&logo=Recoil&logoColor=white"/>
</a>
<a href="https://img.shields.io/badge/axios-5A29E4?style=flat&logo=axios&logoColor=white">
<img src="https://img.shields.io/badge/axios-5A29E4?style=flat&logo=axios&logoColor=white"/>
</a>
<a href="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white"/>
</a>
<a href="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=black">
</a>
<a href="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=black">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=black"/>
</a>
<a href="https://img.shields.io/badge/TypeScript-3178C6?style=flat&logo=TypeScript&logoColor=white">
</a>
<a href="https://img.shields.io/badge/TypeScript-3178C6?style=flat&logo=TypeScript&logoColor=white">
<img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat&logo=TypeScript&logoColor=white"/>
</a>
<a href="https://img.shields.io/badge/CssModules-1572B6?style=flat&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/CssModules-1572B6?style=flat&logo=css3&logoColor=white"/>
</a>
<a href="https://img.shields.io/badge/styledComponents-DB7093?style=flat&logo=styledComponents&logoColor=white">
<img src="https://img.shields.io/badge/styledComponents-DB7093?style=flat&logo=styledComponents&logoColor=white"/>
</a>
<a href="https://img.shields.io/badge/Prettier-F7B93E?style=flat&logo=Prettier&logoColor=black"></a>
<img src="https://img.shields.io/badge/Prettier-F7B93E?style=flat&logo=Prettier&logoColor=black"/>
</a>
<a href="https://img.shields.io/badge/ESLint-4B32C3?style=flat&logo=ESLint&logoColor=white">
<img src="https://img.shields.io/badge/ESLint-4B32C3?style=flat&logo=ESLint&logoColor=white"/>
</a>
</div>

<br>

## 화면구성
<table>
<thead>
<tr>
<th align="center">스터디 그룹 탐색</th>
<th align="center">스터디 그룹 생성</th>
</tr>
</thead>
<tbody>
<tr>
<td align="center"><img src="https://i.ibb.co/hV5HNCP/main-pre-1.gif" alt="main-pre-1" border="0"</td>
<td align="center"><img src="https://i.ibb.co/mDGV8bD/main-pre-2.gif" alt="main-pre-2" border="0"></td>
</tr>
<tr>
<th align="center">스터디 그룹 관리</th>
<th align="center">스터디 스케줄 & 개인 스케줄</th>
</tr>
</thead>
<tbody>
<tr>
<td align="center"><img src="https://i.ibb.co/nbm0HP0/main-pre-6.jpg" alt="main-pre-6" border="0"></td>
<td align="center"><img src="https://i.ibb.co/xG7WpPW/main-pre-4.gif" alt="main-pre-4" border="0"></td>
</tr>
</tbody>
</table>

<br>

## 주요 기능 - <a href="https://github.com/yeori316/seb43_main_016/blob/main/EduSync_Manual.pdf">매뉴얼</a>
#### 👩‍💻 회원가입 & 로그인
- Form 회원가입 및 로그인을 지원합니다.
- 소셜 회원가입 및 로그인을 지원합니다.

#### 👩 마이페이지
- 회원탈퇴를 할 수 있습니다.
- 소셜 회원의 경우 로그인 정보 수정이 불가합니다.
- 자기소개 항목을 통해 자신에 대해 소개하는 글을 작성할 수 있습니다.
- 자신이 원하는 스터디원의 특징을 작성할 수 있습니다.

#### 📖 스터디 조회, 등록, 수정, 삭제
- 자신만의 스터디를 조회 / 등록 / 수정 / 삭제를 할 수 있습니다.
- 타 회원이 등록한 스터디를 조회 / 가입 신청 / 탈퇴를 할 수 있습니다.
- 타 회원이 등록한 모집 글에 댓글로 의견을 남길 수 있습니다.
- 스터디 목록을 자신이 원하는 필터에 따라 재정렬할 수 있습니다.
#### 👑 권한에 따른 예외처리
- 스터디 리더는 스터디 가입신청 승인 / 거부, 스터디 멤버 강퇴 등의 권한을 갖고 있습니다.
- 스터디 리더는 스터디를 해체할 수 있으며, 리더는 스스로 스터디 탈퇴가 불가능합니다.
- 스터디 리더는 스터디 그룹 내에 다른 멤버에게 리더 권한을 위임할 수 있습니다.

#### 🗓️ 캘린더
- 자신이 속한 스터디의 일정을 조회할 수 있습니다.
- 자신만의 스터디 일정을 추가할 수 있습니다.

<br>

## DB ERD
![image](https://github.com/wish9/EduSync_Project/assets/120456261/cf6fef12-33a2-48e3-80ff-7ff2291bdf6a)

<br>

## <a href="https://documenter.getpostman.com/view/25534184/2s93eU3EHc">API 명세서</a>

<br>

## 디렉토리 구조
```bash
├── .github/workflows
├── gradle/wrapper
├── scripts/
├── .gitignore
├── EduSync_Manual.pdf
├── LICENSE
├── README.md
├── appspec.yml
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
├── start.sh
├── stop.sh
└── src
     ├── main/java/com/codestates/edusync
     │    ├── config
     │    ├── exception
     │    ├── filter
     │    ├── handler
     │    ├── model
     │    ├── security/auth
     │    └── EdusyncApplication.java
     └── test/java/com/codestates/edusync
          └── EdusyncApplicationTests.java

```

<br>

## 시작 가이드
***required***
- [JDK 11](https://jdk.java.net/11/)
- [Node 6.9.0](https://nodejs.org/en/blog/release/v6.9.0)

#### Backend
```
// 시작
$ ./start.sh
// 종료
$ ./stop.sh
```

#### Frontend
```
$ cd client
$ npm install
$ npm run dev
$ npm run preview
```

<br>

## Git Convention
***Commit Message***

|  Message   | 설명                                                  |
| :--------: | :---------------------------------------------------- |
|   [feat]   | 새로운 기능을 추가할 경우                             |
|   [fix]    | 버그를 고친 경우                                      |
|  [design]  | CSS 등 사용자 UI 디자인 변경                          |
|  [style]   | 코드 포맷변경, 세미콜론 누락, 코드수정이 없는 경우.   |
| [refactor] | 프로덕션 코드 리펙토링할 경우                         |
| [comment]  | 필요한 주석 추가 및 변경                              |
|   [docs]   | 문서를 수정한 경우                                    |
|   [test]   | 테스트 코드 작업을할 경우                             |
|  [chore]   | 빌드 테스트 업데이트, 패키지 매니저를 설정하는 경우   |
|  [rename]  | 파일 혹은 폴더명을 수정하거나 옮기는 작업만 하는 경우 |
|  [remove]  | 삭제하는 작업만 수행한 경우                           |
|   [init]   | 브랜치 초기화 및 초기셋팅 관련된 설정일 경우          |

<br/>
