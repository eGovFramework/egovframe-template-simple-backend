# 표준프레임워크 심플홈페이지 BackEnd

※ 본 프로젝트는 기존 JSP 뷰 방식에서 벗어나 BackEnd와 FrontEnd를 분리하기 위한 예시 파일로 참고만 하시길 바랍니다.


## 환경 설정
프로젝트에서 사용된 환경 프로그램 정보는 다음과 같다.
| 프로그램 명 | 버전 명 |
| :--------- | :------ |
| java       | 1.8     |
| maven      | 3.8.1   |



## BackEnd 구동

### CLI 구동 방법
```bash
mvn spring-boot:run
```
### IDE 구동 방법
개발환경에서 프로젝트 우클릭 > Run As > Spring Boot App을 통해 구동한다.



## FrontEnd 구동 (React)

현재 FrontEnd는 React 관련 예제로 구성되어 있다.
[심플홈페이지FrontEnd](https://github.com/eGovFramework/egovframe-template-simple-react.git) 소스를 받아 구동한다.



## 변경 사항

###  1. [Java Config 변환](./Docs/java-config-convert.md)

#### 1) Web.xml -> WebApplicationInitializer 구현체로 변환 


#### 2) context-*.xml -> @Configuration 변환


#### 3) properties 변환(예정) boot 지원


### 2. API 변환
직접 View와 연결하던 방법에서 API 형식으로 변환 -> 다양한 프론트에서 적용 가능 하도록 예제 제공\
※ API를 사용한 Controller들은 ~ControllerAPI.java에서 확인 가능합니다.

