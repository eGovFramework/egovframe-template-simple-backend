# context-properties.xml  설정 변환

> 프로퍼티 정보 설정



프로젝트 내에서 사용할 EgovPropertyService에 값을 등록 하는 예제이다.

<context-properties.xml>

```xml
<bean name="propertiesService" class="egovframework.rte.fdl.property.impl.EgovPropertyServiceImpl" destroy-method="destroy">
    <property name="properties">
        <map>
            <entry key="pageUnit" value="10"/>
            <entry key="pageSize" value="10"/>
            <entry key="posblAtchFileSize" value="5242880"/>
            <entry key="Globals.fileStorePath" value="/user/file/sht/"/>
            <entry key="Globals.addedOptions" value="false"/>
        </map>
    </property>
</bean>
```

<EgovConfigAppProperties.class>

```java
@Bean(destroyMethod = "destroy")
public EgovPropertyServiceImpl propertiesService() {
    EgovPropertyServiceImpl egovPropertyServiceImpl = new EgovPropertyServiceImpl();

    Map<String, String> properties = new HashMap<String, String>();
    properties.put("pageUnit", "10");
    properties.put("pageSize", "10");
    properties.put("posblAtchFileSize", "5242880");
    properties.put("Globals.fileStorePath", "/user/file/sht/");
    properties.put("Globals.addedOptions", "false");

    egovPropertyServiceImpl.setProperties(properties);
    return egovPropertyServiceImpl;
}
```