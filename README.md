# BUDDY READ ME
<img width="716" height="581" alt="Image" src="https://github.com/user-attachments/assets/107f540b-3b2a-44db-b4e5-063ee4ff300e" />

※ 현재 업데이트 문제로 간단 확인(웹)은 일부분 기능이 적용되지 못했습니다. 앱을 다운할 경우 문제 없이 현재까지 구현된 모든 기능을 즐길 수 있습니다.
- 프로젝트 간단 확인(웹) : http://134.185.118.165/
- 앱으로 확인 : https://drive.google.com/file/d/1IgCEUIqn0-lRNOlwlRHAhG-Rh9f7UKbR/view?usp=drive_link
+ 확인용 유저 아이디 : user@example.com, 비밀번호 : test1234!

## 프로젝트 소개
러닝 시대에 혼자서 러닝하기 어려운 러닝 초보, 즐겁게 러닝을 즐기고싶은 러닝 고수 모두 러닝에 대한 꿀팁, 사용자들의 러닝 인증 등의 SNS 활성화, 러닝 크루를 찾기 위한 채팅방 활성화와 나만의 귀여운 캐릭터를 꾸미며 만보기를 채워나간다.


## 1. 개발 환경
- 언어 : Java(JDK-17), Dart
- 프레임워크 : Spring Boot(백엔드), Flutter(프론트엔드)
- 빌드 및 의존성 관리 : Gradle
- 테스트 : Postman
- 서버 및 데이터베이스 : Oracle Compute(가상머신,Ubuntu) + Nginx + VM 내부 DB(MySQL)
- [요구사항정의서 & 기능 명세서](https://docs.google.com/spreadsheets/d/1Vvn1udEAVgYEPEjNs64PDrixBiAMN6lbupFDXtUm5oE/edit?usp=sharing)
- [개발 진행 보고서](https://grey-lasagna-97d.notion.site/buddy-27d1032a6fc680998b46fc9b5f5b785a?source=copy_link)

## 2. 채택한 개발 기술
### Flutter/Dart
- 한 번의 개발로 Android 외에도 필요시 웹 또는 ios까지 확장 가능한 구조가 필요했습니다. Flutter는 원소스 멀티 플랫폼 대응이 가능하기에 다운로드 없이 포트폴리오 프로젝트에서 확장성을 보여주기에 적합하다고 생각했습니다.
- Dart의 비동기 처리가 HTTP 통신과 실시간 기능 구현에 자연스럽게 연결되어, 클라이언트 로직을 명확하게 유지할 수 있었습니다.

### Nginx
- 정적 리소스 서빙 + 리버스 프록시 + Web Socket 프록시를 한 번에 관리하기 위해 채택했습니다.

### Web Socket (STOMP)
- 채팅/알림 등 실시간 기능 구현을 위해 서버 푸시가 가능한 양방향 통신 방식을 채택하였습니다.

## 3. 프로젝트 구조
```
📦src
┣ 📂main
┃ ┣ 📂java
┃ ┃ ┗ 📂me
┃ ┃ ┃ ┗ 📂minkyoung
┃ ┃ ┃ ┃ ┗ 📂buddy_back
┃ ┃ ┃ ┃ ┃ ┣ 📂config
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂jwt
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenAuthenticationFilter.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenProvider.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OciObjectStorageConfig.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebSocketConfig.java
┃ ┃ ┃ ┃ ┃ ┣ 📂controller
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatRestController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OciTestController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PostController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenController.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserController.java
┃ ┃ ┃ ┃ ┃ ┣ 📂domain
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PenaltyStatus.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RefreshToken.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportStatus.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportType.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Role.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RoomType.java
┃ ┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatMessageBroadcastResponseDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatMessageListResponseDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatMessageSendRequestDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatRoomListItemResponseDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatRoomResponseDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentResponse.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CreateAccessTokenRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CreateAccessTokenResponse.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CreateGroupRoomRequestDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CreateRoomDirectRequestDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LogInRequestDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LogInResponseDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MyCommentResponseDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MyPostResponseDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PenaltyCreateRequestDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PenaltyDetailDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PenaltySummaryDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportDetailDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportRequestDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportSummaryDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RequestPostDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ResponsePostDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SignUpRequestDto.java
┃ ┃ ┃ ┃ ┃ ┣ 📂entity
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Chat_Message.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Chat_Room.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Chat_Room_Member.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Comment.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Penalty.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Post.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Report.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜User.java
┃ ┃ ┃ ┃ ┃ ┣ 📂jwt
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtProperties.java
┃ ┃ ┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatMessageRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatRoomMemberRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatRoomRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PenaltyRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PostRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RefreshTokenRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRepository.java
┃ ┃ ┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MyPageService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OciPostImageService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PenaltyService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PostService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RefreshTokenService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserDetailService.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
┃ ┃ ┃ ┃ ┃ ┣ 📂util
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CookieUtil.java
┃ ┃ ┃ ┃ ┃ ┗ 📜BuddyBackApplication.java
┃ ┗ 📂resources
┃ ┃ ┣ 📜application-local.yml
┃ ┃ ┣ 📜application.properties
┃ ┃ ┗ 📜application.yml
┗ 📂test
┃ ┗ 📂java
┃ ┃ ┗ 📂me
┃ ┃ ┃ ┗ 📂minkyoung
┃ ┃ ┃ ┃ ┗ 📂buddy_back
┃ ┃ ┃ ┃ ┃ ┗ 📜BuddyBackApplicationTests.java
```
## 4. 개발 기간
- 기능 구현 기간 : 2025.11 ~ 2025.12
- 프론트 연동 및 1차 완료 및 배포 기간 : 2025.12 ~ 2026.01
- 2차 수정 기간 : 2026.01 ~ 진행중

## 5. 페이지별 기능

### 신고 및 제재 기능(페이지 미구현)

#### 신고 기능(부분 구현)
- 제재 기능은 아직 미구현이기에 제재와의 연동은 추후 진행될 예정입니다.

#### 신고 기능 정리 
1. 신고 테이블 : 유저가 누군가를 신고한 기록(허위신고 및 악성 신고를 제외하고자 상태를 통해 구분을 진행하며, 패널티 테이블과 조인하여 신고 내역에 대한 문의 시 언제든 답변할 수 있도록 구성하였습니다.)
2. 제재 테이블 : 운영자 / 시스템이 실제로 유저에게 내린 조치(경고, 정지 등)에 대한 정보를 담고 있습니다.
3. 유저 테이블 : 현재 유저가 제재 상태인지 빠른 판단과 패널티 테이블 조회를 줄이고 위해 패널티 상태에 대한 컬럼을 추가하였습니다. / 추후에 로그인에 대해 제한 로직을 추가할 예정입니다.
4. 신고 서비스 : 신고 생성과 동시에 신고 당한 횟수에 따른 자동 제재(경고)까지만 현재 구현되었습니다.

#### 흐름
1. 다량의 신고가 들어온다.
2. 일정 기준(하루에 욕설 신고 5회 이상) 만족 시 자동으로 경고를 부여하며 신고와 제재 이력이 쌓인다.
3. 해당 과정이 반복될 경우 제재의 강도가 강해지며, 일정기간 정지 이전까지는 자동으로 처리되며, 이후에는 관리자가 해당 이력을 확인한 이후 수동으로 정치처분 등을 부여한다.

#### 제재 기능 (미구현)
- 구현 예정 기능으로 현재는 신고 기능의 일부 기능만 구현이 진행되었습니다.

#### 자동으로 진행되는 기능(신고, 경고, 일시 정지)
- 실시간 이벤트 로직과 스케줄링 로직을 이용하여 제재 기능을 강화합니다.
- 스케줄러를 1시간마다 실행하여 현재 상태가 제재 받은 상태인지 확인하고 시간이 지났을 경우 만료됨 상태로 변경되며 원래 상태로 반환할 예정입니다.
- 제재를 당한 시점부터 감시 종료 기간까지 12시간을 간격으로 추가적인 신고에 대한 확인과 일정 수위가 넘는 신고들이 누적되면 자동으로 한 단계 높은 제재를 부여합니다.

#### 수동으로 진행되는 기능(영구 정지)
- 스케줄러를 이용하여 일정기간 정지를 받은 사용자는 이후에 매 순간 스케줄러의 타겟으로 24시간을 간격으로 추가적인 신고를 확인하며, 추가적인 신고가 발생할 경우 관리자에게 알림을 발송하여 영구 정지 여부를 결정할 수 있도록 돕습니다.

### 푸터
<img src="https://github.com/user-attachments/assets/c78febc4-b531-468d-af55-97d7eee8507a">

- 메인 페이지, 만보기 페이지, 채팅 페이지, 상점 페이지, 마이페이지로 이루어져있으며, 네임드 라우터를 사용해 라우트를 중앙에서 관리하고, 화면 전환을 라우트 이름 기반으로 통일했습니다.

### 로그인을 안할 경우
<img src="https://github.com/user-attachments/assets/9785b722-0e94-49bd-a351-b3a73d676132">

- 로그인을 하지 않을 경우 게시물을 조회하는 것 이외에 모든 행동이 제한되며, 인증 및 인가가 필요한 페이지(생성/수정/삭제, 채팅, 마이페이지 등)에 접근할 수 없도록 사전에 차단한 페이지입니다. 

### 회원가입
<img src="https://github.com/user-attachments/assets/b08ed8c4-32fb-4947-ae41-5a2d60f0dd80">

- 이메일, 이름, 비밀번호를 함께 등록하여 JWT 기반 회원가입을 진행합니다.

### 로그인 
<img src="https://github.com/user-attachments/assets/40dce83e-9179-46b3-b653-8eecc9687085">

- 이메일, 비밀번호를 이용하여 JWT 기반 로그인을 진행합니다.
- 추후에 OAuth 기반 로그인(Google 로그인)을 추가할 예정입니다.

### 메인페이지
<img src = "https://github.com/user-attachments/assets/fb391816-1293-4f8c-adf8-1bf77fb83d73">

- 메인페이지로 모든 게시글을 확인할 수 있습니다.

### 게시글 상세 조회 및 댓글 조회(+수정)
| <img src ="https://github.com/user-attachments/assets/6cb711fd-2074-4b68-bce3-49d92381095c"> | <img src ="https://github.com/user-attachments/assets/e9f56046-95e5-44d0-a68c-dfc1bc84bf88"> |
|----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| 상세페이지 + 댓글 작성 확인                                                                             | 상세 페이지 + 댓글 수정 확인                                                                            |

- 작성된 글에 대한 상세페이지로 댓글을 포함되어있습니다.

### 게시글 작성 및 수정
| <img src ="https://github.com/user-attachments/assets/616ae926-8fdc-477c-adc7-cda0e970fa3a"> | <img src = "https://github.com/user-attachments/assets/24ee93fa-b1f1-4f64-94b1-8a09a7acd209"> |
|----------------------------------------------------------------------------------|----------------|
| 게시글 작성                                                                           | 게시글 수정

- 게시글을 작성할 수 있으며, 갤러리 속 사진을 업로드 할 수 있습니다.
- 오라클 클라우드에서 제공하는 Object Storage를 이용해 데이터(이미지)를 객체 형식으로 저장하고 관리하여 이미지 업로드를 가능하도록 하였습니다.

### 작성자/비작성자의 권한
| <img src="https://github.com/user-attachments/assets/815acf2f-ea27-4a69-bb42-abfc4de21056">       | <img src="https://github.com/user-attachments/assets/0d813eaf-2976-4c87-b17d-94d35f7bf1d0">          |
|---------|------------|
| 작성자의 경우 | 작성자가 아닐 경우 |

- 메인 페이지 또는 상세 페이지에서 글/댓글에 아이콘 중 ...을 클릭할 경우 토큰을 확인 비교를 통해 작성자의 경우 수정, 삭제가 가능하며, 작성자가 아닐 경우 신고와 공유 기능만을 제공합니다.
- 추후에 공유와 신고 기능에 대해 추가할 예정입니다.

### 만보기 페이지(미구현)
<img src="https://github.com/user-attachments/assets/d8a1cc5b-0df2-47ef-8de1-1c217ddbbaa6">

- 현재 미구현된 기능이지만 페이지는 따로 구현했습니다.
- Buddy의 정체성인 캐릭터를 추가하여 만보기에 따라 캐릭터 상태(표정, 나이 등)이 변화하며, 사용자에게 러닝에 대한 의욕을 불어널을 수 있도록 도와주는 도구입니다.

### 채팅방 목록
<img src="https://github.com/user-attachments/assets/2f1abde8-a653-418f-9283-fb1e456fbb34">

- 생성된 채팅방 목록을 확인할 수 있으며, 가장 최근에 작성한 채팅이 있는 채팅방이 최상단으로 올라갑니다.
- 추후에 읽지 않은 채팅에 대한 숫자 표시(+1과 같은) 기능을 추가할 예정입니다

### 채팅방 생성
| <img src="https://github.com/user-attachments/assets/48a30b50-2d8b-4688-a92c-8db4112c9eb2">      | <img src="https://github.com/user-attachments/assets/32e1dc0a-8f24-4843-a1b3-e8317d0fc499">      | <img src="https://github.com/user-attachments/assets/6082937b-113a-460d-bd73-5f5f36a31c83">      |
|--------|--------|--------|
| 채팅방 생성 | 개인방 생성 | 그룹방 생성 |

 - 1:1 채팅방 또는 그룹방 채팅을 생성하며, 상대의 Id(primary)을 기반으로 채팅방을 생성합니다.
 - 추후에 친구 추가 로직을 이용하여 상대의 Id가 아닌 친구(이름)을 통해 채팅방을 생성할 수 있도록 할 예정입니다.

### 채팅 확인
| <img src="https://github.com/user-attachments/assets/bf8bb163-c733-4776-819f-fafb88dd565b"> | <img src="https://github.com/user-attachments/assets/9f9b2ddb-793c-49f4-b047-00b25cf99131"> |
|--------------|--------------|
| 1:1 채팅 전송    | 1:1 채팅 수신    |
| <img src="https://github.com/user-attachments/assets/6c35d141-c6b8-4a33-8ee0-62d13ee7bbfe">            | <img src="https://github.com/user-attachments/assets/69fa0267-16e5-4c94-afd2-27159d2cdf57">            |
| 그룹 채팅 전송     | 그룹 채팅 수신     |

- WebSocket(STOMP)를 이용하여 구현된 실시간 채팅 기능으로 채팅방은 /topic 기반 pub/sub 구조로 메시지를 브로드캐스트하고, 필요 시 /user경로를 통해 특정 사용자에게만 전달되는 개인 메시지 또한 지원합니다.
- 추후에 1:1 채팅방 생성시 생성자 기준의 상대 이름으로 표기되는 문제 수정 및 채팅자가 누군지 구분하기 위해 이름 표기할 예정입니다.

### 상점 페이지(미구현)
<img src="https://github.com/user-attachments/assets/d943afdb-2376-4840-a1cb-400add0f0763">

- 현재는 미구현 페이지로 아이템, 옷장, 재화 구매(PG사 연동 - 아임포트 API 사용 예정) 등의 기능을 추가할 예정입니다.

### 내 프로필

#### 로그아웃
<img src="https://github.com/user-attachments/assets/d4ce2d7f-d7bc-48e2-b7fc-9a0d7e69f9f6">

- 로그아웃 즉시 로컬에 저장된 토큰을 즉시 삭제(clear)합니다.

#### 내가 작성한 글/댓글/좋아요
| <img src="https://github.com/user-attachments/assets/85814e2a-1745-4bea-ba48-c252043d69a7">        | <img src="https://github.com/user-attachments/assets/1cfe5e0b-1f25-473f-a733-18ab4cac141e">         |
|----------|-----------|
| 내가 작성한 글 | 내가 작성한 댓글 |

- 사용자가 작성한 글/댓글을 사용자의 이메일을 기준으로 조회하고 가져옵니다.
- 추후에 좋아요 기능을 추가하여 좋아요한 글 또한 가져올 수 있도록 할 예정입니다.


## 6. 트러블 슈팅
### 6-1. 중복 신고 방지(어뷰징 차단 + 제재 폭주 방지)
#### 문제

- 동일 사용자가 같은 대상/같은 신고 타입을 반복적으로 신고하면 DB에 중복 데이터가 누적되어, 제재 로직이 과도하게 반복 적용될 수 있는 문제가 있습니다.

#### 해결방법

- 신고 생성 전에 existsByReporterIdAndReportedUserIdAndReportType로 중복 여부를 확인하고, 중복일 경우 예외로 저장 및 제재 호출을 차단했습니다.

#### 개선효과

-  동일 조건 중복 신고가 DB에 저장되지 않게 되어 불필요한 제재 반복 적용을 방지했고,신고 데이터 품질과 운영 안정성을 개선했습니다.

### 6-2. WebSocket(STOMP) 연동 문제 해결로 실시간 메시징 안정화
#### 문제

- 배포 환경에서 WebSocket(STOMP) 기반 실시간 기능이 정상 동작하지 않아 사용자가 접속해도 실시간 메시지를 수신하지 못하거나 연결이 불안정한 문제가 발생했습니다.

#### 해결방법

- 클라이언트 로그에서 연결 실패 유형(업그레이드 실패 / 재연결 반복 / 타임아웃 등)을 분류했습니다.
- Nginx 리버스 프록시 WebSocket 업그레이드 헤더/HTTP1.1/타임아웃을 적절히 전달하는지 확인하고 설정을 보완했습니다.

#### 개선효과

- 배포 환경에서 WebSocket 연결이 안정적으로 성립되어 실시간 메시지 수신이 정상화 되었습니다.

## 7. 개선 목표
| 확장 범위               | Description                                                                      |
|---------------------|----------------------------------------------------------------------------------|
| 제재 기능 추가 및 신고 기능 보강 | 기존에 빈약했던 신고 기능에 대한 보강과 제재 기능을 추가하여 신고 시 자동 제재 로직 구현                              |
| 친구추가 기능 추가          | 채팅방 생성시 id를 통해 불특정한 누군가와 채팅방을 생성하는 것이 아닌 친구 기능을 통해 친구인 사람 중 채팅방을 초대할 수 있도록 기능 추가 |
| 채팅방 참여 유저 확인 및 삭제   | 단체방의 경우 이용자 확인이 불가능하기에 참여 유저 확인 및 채팅방 구독 해제 기능 추가                                |
| 좋아요 기능 추가           | 게시글에 대한 좋아요 기능으로 추후 내가 누를 좋아요 게시글을 내 프로필을 통해 손쉽게 확인                              |
| OAuth 기능 추가         | Google의 OAuth를 이용하여 손쉬운 회원가입 및 로그인 기능                                            |
| 만보기 기능 추가           | 해당 앱의 정체성이며 캐릭터를 추가하여 만보기에 따른 캐릭터 상태 변화가 추가될 예정                                  |
| 원스토어 배포             | 직접 만든 프로젝트를 원스토어 배포하여 트래픽 관리 경험                                                  |


<br/>

