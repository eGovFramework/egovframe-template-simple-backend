package egovframework.com.cmm.annotation;


import egovframework.com.security.pass.SecurityPassConst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * fileName       : SecurityPass
 * author         : crlee
 * date           : 2023/08/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/04        crlee       최초 생성
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecurityPass {
    String[] role() default {SecurityPassConst.ALL};
}