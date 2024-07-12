package egovframework.com.cmm.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.egovframe.rte.fdl.cmmn.aspect.ExceptionTransfer;

@Aspect
public class AopExceptionTransfer {
	private ExceptionTransfer exceptionTransfer;

	public void setExceptionTransfer(ExceptionTransfer exceptionTransfer) {
		this.exceptionTransfer = exceptionTransfer;
	}

	@Pointcut("execution(* egovframework.let..impl.*Impl.*(..)) or execution(* egovframework.com..impl.*Impl.*(..))")
	private void exceptionTransferService() {}

	@AfterThrowing(pointcut= "exceptionTransferService()", throwing="ex")
	public void doAfterThrowingExceptionTransferService(JoinPoint thisJoinPoint, Exception ex) throws Exception{
		exceptionTransfer.transfer(thisJoinPoint, ex);
	}
}
