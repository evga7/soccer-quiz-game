# 축구지식테스트 :soccer:

### 개요 :bulb:
축구를 좋아하는 사람들에게 축구에 대한 퀴즈를 풀수있는 어플리케이션입니다. 사용자를 위한 게시판도 있습니다. 

### 멤버 (1명)
|<img src="https://avatars.githubusercontent.com/u/44694917?v=4"  width="100" height="100"/> </br> https://github.com/evga7 </br> 홍원표  |
|:-:|
|백앤드,프론트|

</br>


## 🖥️ 프로젝트 내용

- 프론트앤드는 Android(Kotlin)으로 제작
- 모바일 기기마다 반응형으로 화면이 나오도록 제작
- 이미지 파일은 내부 캐시로 저장해서 S3 서버의 액세스 횟수를 줄일 수 있도록 함
- 백앤드는 Spring boot로 제작 Spring Data Jpa와 Mysql를 활용 Rest API 제작
- 관리자페이지는 따로 제작하여 게시판 및 퀴즈를 추가 삭제할 수 있음
- 도커허브를 이용하여 도커 이미지로 Ec2 안에서 실행
- JenKins를 활용하여 CI/CD pipeline 구성
- Nginx를 이용 웹 서버와 WAS 서버 분리 (도커 컨테이너로 띄워서 서버와 연결)

## ✨느낀 점

- 프론트앤드와 백앤드의 통신을 위한 초기 설계가 정말 중요하다는 것을 느꼈다.
- 처음으로 기획부터 배포까지 맡았으며 앱을 출시하기까지 다양한 것을 신경 써야 한다는 것을 느꼈다.
- 젠킨스와 도커허브를통하여 이미지를 이용해서 서버에서 구동하는 게 정말 좋다.
- 최대한 서버의 부하를 줄이는 코드를 고민을 더욱 해야겠다.


</br>


### 링크    

|<img src="https://play-lh.googleusercontent.com/JuTwaunKP9rFAf5aswYycbEldXlmLTye6Y8M2VNHGSTfYhy7hOok0z85bW9JijM0zcGi=w240-h480-rw"  width="200" height="200"/>|
|:--:|
|[구글](https://play.google.com/store/apps/details?id=com.pyo.quizfrontapp&hl=ko&gl=US), [원스토어](https://m.onestore.co.kr/mobilepoc/apps/appsDetail.omp?prodId=0000758040)|

</br>

### 기술 스택
BackEnd :  <img alt="Html" src ="https://img.shields.io/badge/2.5.3-007396.svg??style=plastic&logo=springboot&label=Spring Boot&color=green"/> <img alt="Html" src ="https://img.shields.io/badge/11-007396.svg??style=plastic&logo=java&label=JAVA&color=007396"/>  
FrontEnd : <img alt="Html" src ="https://img.shields.io/badge/Bumblebee-007396.svg??style=plastic&logo=Android Studio&label=Android Studio&color=3DDC84"/> <img alt="Html" src ="https://img.shields.io/badge/1.5.2-007396.svg??style=plastic&logo=kotlin&label=kotlin&color=7F52FF"/>   
DB : <img alt="Html" src ="https://img.shields.io/badge/2.5.3-007396.svg??style=plastic&logo=MYSQL&logoColor=white&label=MYSQL&color=4479A1"/>  
Devops : <img alt="Html" src ="https://img.shields.io/badge/20.10.14-007396.svg??style=plastic&logo=docker&label=docker&color=2496ED"/> <img alt="Html" src ="https://img.shields.io/badge/2.332.2-007396.svg??style=plastic&logo=jenkins&label=jenkins&color=D24939"/>  
WebServer : 
<img alt="Html" src ="https://img.shields.io/badge/2.3.2-007396.svg??style=plastic&logo=apachetomcat&label=Tomcat&color=F8DC75"/> <img alt="Html" src ="https://img.shields.io/badge/1.20.2-007396.svg??style=plastic&logo=nginx&label=Nginx&color=009639"/>  
Server : <img alt="Html" src ="https://img.shields.io/badge/t2.micro-007396.svg??style=plastic&logo=amazonaws&label=AWS EC2&color=232F3E"/>  
Storage : <img alt="Html" src ="https://img.shields.io/badge/30GB-007396.svg??style=plastic&logo=amazons3&label=Amazon S3&color=569A31"/>  
OS : <img alt="Html" src ="https://img.shields.io/badge/20.04 LTS-007396.svg??style=plastic&logo=Ubuntu&label=Ubuntu&color=E95420"/>  



</br>

### 아키텍쳐  
![soccer_project_structure](https://user-images.githubusercontent.com/44694917/178496778-b288dbe9-91f4-41d9-aaf6-7fe5296080ff.png)


### 앱 화면
<img src="https://user-images.githubusercontent.com/44694917/178524018-20096222-8fd0-44ed-b602-056b3c2761d7.jpg" width="230" height="400"/>ㅤㅤㅤㅤ<img src="https://user-images.githubusercontent.com/44694917/178524127-3daef629-1a43-4562-a993-e85a25b9241f.jpg" width="230" height="400"/>ㅤㅤㅤㅤ<img src="https://user-images.githubusercontent.com/44694917/178524188-89d5c608-5285-41b1-8122-154ca2e816c2.jpg" width="230" height="400"/>
<br>
<img src="https://user-images.githubusercontent.com/44694917/178524162-3836059f-8c57-42e1-b659-4d0dd845faca.jpg" width="230" height="400"/>ㅤㅤㅤㅤ<img src="https://user-images.githubusercontent.com/44694917/178524242-631b1d1d-588c-432b-b1a4-cf71f1d74f7e.jpg" width="230" height="400"/>ㅤㅤㅤㅤ<img src="https://user-images.githubusercontent.com/44694917/178524246-5b4c9475-980b-4b80-aa82-93ca31e0e48a.jpg" width="230" height="400"/>
<br>
<img src="https://user-images.githubusercontent.com/44694917/178524782-d2872862-f64d-43d9-a3c3-f88ede01b032.png" width="230" height="400"/>

![ezgif-4-0557ff2638f7](https://user-images.githubusercontent.com/44694917/184810202-e62359ca-218d-4653-b93c-01e93a8e09d3.gif)

