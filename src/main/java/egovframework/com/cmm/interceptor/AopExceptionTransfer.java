package egovframework.com.cmm.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.egovframe.rte.fdl.cmmn.aspect.ExceptionTransfer;

@Aspect
public class AopExceptionTransfer {
	private ExceptionTransfer exceptionTransfer;

	// 예외 전달 객체를 설정합니다. (중앙 집중화된 예외 처리를 위해 사용됩니다.)
	public void setExceptionTransfer(ExceptionTransfer exceptionTransfer) {
		this.exceptionTransfer = exceptionTransfer;
	}

	// 예외 전달 대상이 되는 서비스 메서드들을 정의합니다.
        // egovframework.let 패키지와 egovframework.com 패키지의 impl 하위 메서드를 대상으로 설정합니다.
	@Pointcut("execution(* egovframework.let..impl.*Impl.*(..)) or execution(* egovframework.com..impl.*Impl.*(..))")
	private void exceptionTransferService() {}

	// 예외가 발생한 후에 실행되는 메서드입니다.
        // 예외가 발생한 시점의 JoinPoint와 발생한 예외를 파라미터로 받아 예외 전달 객체를 통해 예외 정보를 처리합니다.
	@AfterThrowing(pointcut= "exceptionTransferService()", throwing="ex")
	public void doAfterThrowingExceptionTransferService(JoinPoint thisJoinPoint, Exception ex) throws Exception{
		exceptionTransfer.transfer(thisJoinPoint, ex);
	}
}
