# context-transaction.xml  설정 변환

> Transanction 관련 설정



Transanction 설정을 관리하는 곳이다.

<context-transaction.xml>

```xml
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<tx:advice id="txAdvice" transaction-manager="txManager">
    <tx:attributes>
        <tx:method name="*" rollback-for="Exception"/>
    </tx:attributes>
</tx:advice>

<aop:config>
    <aop:pointcut id="requiredTx" expression="execution(* egovframework.let..impl.*Impl.*(..)) or execution(* egovframework.com..*Impl.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="requiredTx" />
</aop:config>
```

<EgovConfigAppTransaction.class>

```java
@Bean
public DataSourceTransactionManager txManager() {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
    dataSourceTransactionManager.setDataSource(dataSource);
    return dataSourceTransactionManager;
}

// -------------------------------------------------------------
// TransactionAdvice 설정
// -------------------------------------------------------------

@Bean
public TransactionInterceptor txAdvice(DataSourceTransactionManager txManager) {
    TransactionInterceptor txAdvice = new TransactionInterceptor();
    txAdvice.setTransactionManager(txManager);
    txAdvice.setTransactionAttributeSource(getNameMatchTransactionAttributeSource());
    return txAdvice;
}

private NameMatchTransactionAttributeSource getNameMatchTransactionAttributeSource() {
    NameMatchTransactionAttributeSource txAttributeSource = new NameMatchTransactionAttributeSource();
    txAttributeSource.setNameMap(getRuleBasedTxAttributeMap());
    return txAttributeSource;
}

private HashMap<String, TransactionAttribute> getRuleBasedTxAttributeMap() {
    HashMap<String, TransactionAttribute> txMethods = new HashMap<String, TransactionAttribute>();

    RuleBasedTransactionAttribute txAttribute = new RuleBasedTransactionAttribute();
    txAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    txAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
    txMethods.put("*", txAttribute);

    return txMethods;
}

// -------------------------------------------------------------
// TransactionAdvisor 설정
// -------------------------------------------------------------

@Bean
public Advisor txAdvisor(DataSourceTransactionManager txManager) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(
        "execution(* egovframework.let..impl.*Impl.*(..)) or execution(* egovframework.com..*Impl.*(..))");
    return new DefaultPointcutAdvisor(pointcut, txAdvice(txManager));
}
```