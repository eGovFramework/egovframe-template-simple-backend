# context-validator.xml  설정 변환

> validator 설정 파일을 등록하는 역할을 한다.



validator 설정 파일들의 위치를 지정해 준다.

<context-validator.xml>

```xml
<bean id="beanValidator" class="org.springmodules.validation.commons.DefaultBeanValidator">
    <property name="validatorFactory" ref="validatorFactory"/>
</bean>

<bean id="validatorFactory" class="org.springmodules.validation.commons.DefaultValidatorFactory">
    <property name="validationConfigLocations">
        <list>
            <!-- 경량환경 템플릿 밸리데이터 설정 -->
            <value>classpath:/egovframework/validator/validator-rules-let.xml</value>
            <value>classpath:/egovframework/validator/let/**/*.xml</value>
        </list>
    </property>
</bean>
```

<EgovConfigAppValidator.class>

```java
@Bean
public DefaultBeanValidator beanValidator() {
    DefaultBeanValidator defaultBeanValidator = new DefaultBeanValidator();
    defaultBeanValidator.setValidatorFactory(validatorFactory());
    return defaultBeanValidator;
}

@Bean
public DefaultValidatorFactory validatorFactory() {
    DefaultValidatorFactory defaultValidatorFactory = new DefaultValidatorFactory();
    defaultValidatorFactory.setValidationConfigLocations(getValidationConfigLocations());
    return defaultValidatorFactory;
}

private Resource[] getValidationConfigLocations() {
    PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
    List<Resource> validationConfigLocations = new ArrayList<Resource>();
    Resource[] validationRulesConfigLocations = new Resource[] {
        pathMatchingResourcePatternResolver
            .getResource("classpath:/egovframework/validator/validator-rules-let.xml")
    };

    Resource[] validationFormSetLocations = new Resource[] {};
    try {
        validationFormSetLocations = pathMatchingResourcePatternResolver
            .getResources("classpath:/egovframework/validator/let/**/*.xml");
    } catch (IOException e) {
    }

    validationConfigLocations.addAll(Arrays.asList(validationRulesConfigLocations));
    validationConfigLocations.addAll(Arrays.asList(validationFormSetLocations));

    return validationConfigLocations.toArray(new Resource[validationConfigLocations.size()]);
}
```