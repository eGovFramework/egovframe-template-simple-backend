# context-validator.xml 설정 변환

> validator(입력값 검증) 설정 파일을 등록하는 역할을 한다.
> XML을 JavaConfig로 옮기는 일반적인 변환 방식과 이 프로젝트가 실제로 택한 방식으로 나눈다.

## 1. 일반적인 변환 방식 (Commons Validator: XML → JavaConfig)

기존 XML에서는 validator 설정 파일들의 위치를 지정해 Commons Validator 검증기를 등록한다.

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

이를 JavaConfig로 옮기면 일반적으로 다음과 같이 `DefaultBeanValidator` / `DefaultValidatorFactory` 를 `@Bean` 으로 등록한다.

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

## 2. 이 프로젝트에서 사용한 방식 (JSR-303 / Jakarta Bean Validation)

이 프로젝트는 `beanValidator` / `validatorFactory` 를 JavaConfig로 등록하지 않고, 대신 표준 `JSR-303` / `Jakarta Bean Validation` 을 사용한다. 따라서 별도의 validator 설정
클래스 및 빈이 존재하지 않는다.

1) 모델(DTO/VO)에 제약 애노테이션(`jakarta.validation.constraints.*`)을 선언한다.

```java
import jakarta.validation.constraints.NotBlank;

public class BbsAttributeInsertRequestDTO {
    @NotBlank
    private String bbsNm;
    // ...
}
```

2) 컨트롤러 파라미터에 `@Valid` 를 붙이고, 바로 뒤 `BindingResult` 로 검증 결과를 받는다.

```java
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public ResultVO insertMber(@Valid MberManageVO mberManageVO, BindingResult bindingResult) throws Exception {
    if (bindingResult.hasErrors()) {
        // 검증 실패 처리
    }
    // ...
}
```

검증 프로바이더는 `hibernate-validator` 이며, Spring Boot 가 `LocalValidatorFactoryBean` 을 자동 등록하므로 validator 를 명시적으로 등록하는 `@Bean` 설정은 필요 없다.