package egovframework.com.security.pass;

import egovframework.com.cmm.annotation.SecurityPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * fileName       : SecurityUtils
 * author         : crlee
 * date           : 2023/08/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/04        crlee       최초 생성
 */
public class SecurityPassUtils {
    @Autowired
    ApplicationContext applicationContext;

    public String[] getUrls(){
        return getUrls(SecurityPassConst.ALL);
    }
    public String[] getUrls(String roleType){
        List<String> auth_whitelists = new ArrayList<>();
        try{
            auth_whitelists = searchMappingUrls(roleType);
        }catch (Exception e){
            e.printStackTrace();
        }

        return auth_whitelists.toArray(new String[0]);
    }
    private List<String> searchMappingUrls(String roleType) throws NoSuchMethodException, SecurityException, InvocationTargetException, IllegalAccessException {

        List<String> auth_whitelists = new ArrayList<>();

        List<Class<? extends Annotation>> mappingAnnotations = Arrays.asList(
                GetMapping.class,
                PostMapping.class,
                PutMapping.class,
                DeleteMapping.class,
                PatchMapping.class
        );
        List<Class> classes = findController();
        for (Class clazz : classes) {
            for(Method method : clazz.getMethods()){
                if (method.isAnnotationPresent(SecurityPass.class)) {
                    String[] roles = (String[]) method.getAnnotation(SecurityPass.class).getClass().getMethod("role").invoke(method.getAnnotation(SecurityPass.class));
                    if( Arrays.asList(roles).contains(roleType) ){
                        for (Class<? extends Annotation> annotationClass : mappingAnnotations) {
                            if( method.isAnnotationPresent(annotationClass) ){
                                String[] values = (String[]) method.getAnnotation(annotationClass).getClass().getMethod("value").invoke(method.getAnnotation(annotationClass));
                                auth_whitelists.addAll(Arrays.asList(values));
                            };
                        }
                    }

                }
            }
        }
        return auth_whitelists;
    }

    private List<Class> findController(){
        List<Class> classes = new ArrayList<>();
        Map<String, Object> beansWithControllerAnnotation = applicationContext.getBeansWithAnnotation(Controller.class);
        for(Object controller : beansWithControllerAnnotation.values()) {
            classes.add(controller.getClass());
        }
        return classes;
    }
}
