# Dishcovery

공무원 업무추진비 내역을 기반으로 한 로컬 맛집 추천 서비스
- 2023년 제 15회 창의설계경진대회 우수상
<img src="https://github.com/capstone-miso/miso-server/assets/86183856/f3deca72-37b1-4331-95ab-f121083bc348" height="400px">

### 접속 도메인
[https://dishcovery.life](https://dishcovery.life)  
본 서비스는 Mobile 환경에 최적화 되어 있습니다.

---
## 기획 배경

1. 기존의 맛집 추천 및 검색 방식의 신뢰 문제
SNS, 포털 사이트 검색을 통한 맛집들은 광고 기반인 경우가 많습니다. 따라서, 신뢰성 있는 리뷰 정보와 방문 정보를 찾는 것에서 많은 사람이 어려움을 겪고 있습니다.
2. 로컬 맛집에 대한 사람들의 니즈가 커짐.
신뢰성 있는 리뷰 정보를 찾는 사람들이 많아짐에 따라 사람들이 많이 가는 집을 소개하는 유튜브 채널 ‘또간집’이 매회 조회수 100만 이상일 정도로 큰 인기를 끌고 있습니다.
3. 공무원 업무추진비 내역에 대한 접근성이 낮음.
공무원 업무 추진비 내역이 신뢰성 있다는 사실을 알더라도 일반 사람들이 이를 이용하여 서비스를 사용하는 것은 어려움이 많습니다. 발생할 수 있는 어려움으로는 파일 열람의 어려움, 가게정보 파악 어려움, 선호도 파악 어려움, 방대한 데이터로 인하여 인기있는 매장 파악의 어려움의 있을 수 있습니다.
4. 해결 방안
공무원 업무추진비 내역을 바탕으로 맛집을 추천해 주는 서비스 개발.
공무원 업무추진비 내역에는 방문한 매장명, 이용 금액, 방문 시간, 방문 인원 등의 정보가 있으므로 다양한 정보를 사용자에게 제공하기 좋습니다. 또한, 공문서를 이용해서 서비스를 제공하므로 투명성과 신빙성이 보장 됩니다.
따라서, 기존의 리뷰 정보를 공무원 업무추진비 내역 데이터로 대체하여 제공한다면 사람들에게 신뢰성있는 정보를 제공할 수 있을 것으로 기대할 수 있습니다.

## 사용 기술 및 API

1. AWS, Github
2. Kakao Social Login API, Kakao Map API, Kakao Search API (매장 데이터 연동, 지도 랜더링)
3. 서울열린데이터 Open API (공영주차장 정보)
4. Spring (서버 구성)
    1. Spring Batch (업무추진비 내역 수집)
    2. Spring JPA (데이터베이스 서버 연동)
    3. Querydsl (데이터베이스 서버 연동)
    4. Spring OAuth2 (소셜로그인)
    5. Spring Security (로그인 및 권한 설정)
    6. Swagger (API 문서화)
5. MySQL (데이터 저장 및 관리)
6. React (웹페이지 구성)
    1. Chakra UI (애플리케이션 개발)
    2. Axios (비동기 통신을 통한 서버 연동)

## 시퀸스 다이어그램
<img src="https://github.com/capstone-miso/miso-server/assets/86183856/4c8edcba-66bb-49db-9882-729210e82b83" width="600px">
<img src="https://github.com/capstone-miso/miso-server/assets/86183856/ea217ca4-0057-400c-ac53-4e4101d39e7f" width="600px">

## 서비스 아키텍처
- Client
<img src="https://github.com/capstone-miso/miso-server/assets/86183856/42cdc524-ae58-4e4c-a89d-a34d65ff98d9" width="600px">
- Server
<img src="https://github.com/capstone-miso/miso-server/assets/86183856/5acd141b-cd94-43de-a8a5-ef918314a9f3" width="600px">

## ERD 설계
<img src="https://github.com/capstone-miso/miso-server/assets/86183856/01c3cb97-e3f8-4e97-8557-c67eda68ac29" width="600px"/>

## 화면 구성
<div>
  <div>
    <img src="https://github.com/capstone-miso/miso-server/assets/86183856/287c4938-7a28-46c5-aeb9-068b75e2daae" width="30%">
    <img src="https://github.com/capstone-miso/miso-server/assets/86183856/e258c3ed-478d-4a33-98ff-13b9103c3a51" width="60%">
  </div>
  <div>
    <li>로그인 - 카카오 로그인 및 비회원 접속</li>
    <li>맛집 지도 - 매장 마커 및 가게 스크롤 리스트</li>
  </div>
</div>

---
![Untitled 2](https://github.com/capstone-miso/miso-server/assets/86183856/ff461f0f-51c6-4eba-9c41-50195a02a3f8)
- 맛집 정보 - 가게에 대한 간략한 정보, 메뉴 정보
---
![Untitled 3](https://github.com/capstone-miso/miso-server/assets/86183856/eb358b6a-5e40-4f1e-ad4c-c430f2c8c8d9)
- 맛집 정보 - 교통편, 가게 키워드, 업무 추진비 내역 기반 시각화 자료
---
![Untitled 4](https://github.com/capstone-miso/miso-server/assets/86183856/0c26f7a9-e07a-44f1-b645-c964664c5b68)
- 매장 상세페이지 내 비슷한 맛집 추천
---
![Untitled 5](https://github.com/capstone-miso/miso-server/assets/86183856/a4536034-3a7b-42fc-8369-6f3f7c1db8a1)
- 맛집 리스트 - 전체 맛집 조회 페이지
---
![Untitled 6](https://github.com/capstone-miso/miso-server/assets/86183856/bf661e53-ac47-415b-b724-971a7a55eff9)
- 맛집 리스트 - 카테고리 별 맛집 조회
---
![Untitled 7](https://github.com/capstone-miso/miso-server/assets/86183856/4bb9f313-1626-4782-89f4-7494f417529e)
- 맛집 리스트 - 키워드 별 가게 조회
---
![Untitled 8](https://github.com/capstone-miso/miso-server/assets/86183856/ffbc913a-b71f-451b-ad2f-4517b8822f38)
- 또갈집 - 찜한 가게 및 비슷한 맛집 추천

---

## 작품 제작 후기

지역 상권 분석 및 추천 시스템 주제를 맡아 프로젝트를 주제를 생각해보며 얼마전 공무원 업무추진비 내역을 기반으로한 맛집 선정이 화제가 되었다는 것이 생각났습니다. 공무원 업무추진비 내역을 통해 맛집 추천을 해주고 업무추진비 내역을 수집하는 과정에서 얻게된 데이터를 시각적으로 사용자에게 제공한다면 신뢰성있는 맛집 추천 서비스를 개발할 수 있을 것이라고 생각하였습니다.
이번 프로젝트를 수행하면서 가장 중요하게 생각했던 것은 각자의 역할에 충실하게 임하는 것입니다. 프로젝트를 수행하며 이런 점을 지키기 위해서 git을 활용해 진행사항을 꼼꼼히 체크해 나갔으며 자주 같이 작업 하는 시간을 가졌습니다. 같이 작업을 하는 과정에서는 서로 모르는 부분에 관하여 물어보고 괜찮은 아이디어가 생각날때 프로젝트에 바로바로 적용할 수 있었습니다. 팀원과 함께 협력해 나가는 과정에서 팀원과의 협업의 중요함을 다시 한번 알 수 있었던 시간이 였습니다.
또한, 작업을 하면서 모르는 부분이 나오면 같이 공부하고 프로젝트에서 응용해 보는 것을 통해 이번 작업을 하면서 각자 맡은 파트 뿐만 아니라 다른 파트의 부분에도 좀 더 이해하고 실력 역시 향상 시킬 수 있는 시간을 가졌습니다.

---

## Contributor

🐣 [duckbill413](https://github.com/duckbill413)

👩🏻‍💻 [littleemm](https://github.com/littleemm)

👩🏻‍💻 [ming-taro](https://github.com/ming-taro)

👨🏻‍💻 [RecordOfJun](https://github.com/RecordOfJun)
