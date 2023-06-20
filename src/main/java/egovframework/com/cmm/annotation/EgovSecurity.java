package egovframework.com.cmm.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * fileName       : EgovSecurity
 * author         : crlee
 * date           : 2023/06/20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/20        crlee       최초 생성
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
public @interface EgovSecurity {
}
