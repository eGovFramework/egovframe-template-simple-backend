# JavaConfig 변환

## 1. web.xml 변환
기존의 web.xml에서 설정을 변환하여 EgovWebApplicationInitializer 에서 설정

```
src/main/java/egovframework/com/config/EgovWebApplicationInitializer.java
```

- [WebApplicationInitial 변환 방법](./WebApplicationInitializer-convert.md)



## 2. context-*.xml 변환

`src/main/java/egovframework/com/config/` 아래에 설정 파일 위치

기존 ApplicationContext 레벨의 설정들은 `EgovConfigApp*.java` 로 구성.
기존 WebApplicationContext 레벨의 설정들은 `EgovConfigWeb*.java` 로 구성 


- [설정파일 변환 방법](./configuration-setting-bean-regist.md)
- [context-aspect 변환](./context-aspect-convert.md)
- [context-common 변환](./context-common-convert.md)
- [context-datasource 변환](./context-datasource-convert.md)
- [context-idgen 변환](./context-idgen-convert.md)
- [context-mapper 변환](./context-mapper-convert.md)
- [context-properties 변환](./context-properties-convert.md)
- [context-transaction 변환](./context-transaction-convert.md)
- [context-validator 변환](./context-validator-convert.md)
- [context-whitelist 변환](./context-whitelist-convert.md)


---

참고 사항

[Context의 계층 관계](./context-hierarchy.md)

[WebApplicationInitial이란](./WebApplicationInitializer.md)
